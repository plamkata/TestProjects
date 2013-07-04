/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 ******************************************************************************
 */
// Created on 27.04.2008

package net.sf.rcpforms.bindingvalidation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.modeladapter.configuration.BeanAdapter;
import net.sf.rcpforms.modeladapter.configuration.IRangeAdapter;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.IModelValidator;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;
import net.sf.rcpforms.modeladapter.converter.IValidatorConverterStrategy;
import net.sf.rcpforms.modeladapter.converter.ObjectToObjectWithNullValueConverter;
import net.sf.rcpforms.modeladapter.converter.ObjectWithNullValueToObjectConverter;
import net.sf.rcpforms.modeladapter.converter.ObservableStatusToBooleanAdapter;
import net.sf.rcpforms.modeladapter.converter.ValidatorConverterStrategy;
import net.sf.rcpforms.widgetwrapper.Activator;
import net.sf.rcpforms.widgetwrapper.util.Debug;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.IPresentationConfiguration;
import net.sf.rcpforms.widgetwrapper.wrapper.IViewer;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCombo;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCompound;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPRadioGroup;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleCombo;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleText;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget;
import net.sf.rcpforms.widgetwrapper.wrapper.radio.RadioGroupManager;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.ISWTObservable;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ICheckable;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IMessageManager;

/**
 * manages bindings of n RCPFormPart parts to one or more models.
 * <p>
 * The BindingManager uses eclipse databinding and a set of default rules to create the most
 * suitable ObservableValues and offer an easy to use facade for binding control values to data
 * model values and control state to data model state.
 * <p>
 * 
 * @author Marco van Meegen
 * @author Remo Loetscher
 */

public class ValidationManager
{
    private static final Logger LOG = Logger.getLogger(ValidationManager.class.getName());

    private AggregateValidationStatus bindingStatus = null;

    private IValueChangeListener bindingStatusChangeListener = null;

    /**
     * map caching current statuses without control association to avoid unneeded updates in
     * MessageManager
     */
    private Map<String, IStatus> messageMap = new HashMap<String, IStatus>();

    /**
     * map caching current statuses with control association to avoid unneeded updates in
     * MessageManager
     */
    private Map<Control, Map<String, IStatus>> controlToMessageMapMap = new HashMap<Control, Map<String, IStatus>>();

    /**
     * flag if combos should be automatically bound to range (e.g. Enum) properties
     */
    private boolean setRanges = true;

    private FormPartInfo currentInfo = null;

    private DataBindingContext dbContext = null;

    private IMessageManager messageManager;

    private WritableValue validationState;

    /** name of instance for debugging purposes */
    private String instanceName;

    /**
     * maps a form part to the information associated with it: validators, model, bindings
     */
    private Map<RCPFormPart, FormPartInfo> viewPartToBindingMap = new HashMap<RCPFormPart, FormPartInfo>();

    private volatile boolean isValidationEnabled = true;

    /** gathers all info related to one form part */
    private class FormPartInfo
    {
        /** flag if form part was already bound before */
        public boolean isBound = false;

        public List<Binding> bindings = new ArrayList<Binding>();

        public List<IModelValidator> validators = new ArrayList<IModelValidator>();

        /**
         * for each bound model property the associated widget is stored to enable decoration on
         * validation changes.
         */
        public Map<Object, RCPControl> propertyWidgetMap = new HashMap<Object, RCPControl>();

        public Object model;

        public FormPartInfo(Object model)
        {
            super();
            this.model = model;
        }

        /**
         * get widget for property
         * 
         * @param model model
         * @param properties either a list of properties addressing the nested property or if only
         *            one argument, a non-nested property or a nested property in "." notation, e.g.
         *            a.b.c is equivalent to a,b,c varargs
         * @return control, null if none found
         */
        public RCPControl getWidgetForProperty(Object model, Object... properties)
        {
            if (properties.length == 1)
            {
                if (properties[0] instanceof String && ((String) properties[0]).indexOf(".") > 0) //$NON-NLS-1$
                {
                    properties = ((String) properties[0]).split("\\."); //$NON-NLS-1$
                }
            }
            IPropertyChain propertyChain = createPropertyChain(model, properties);
            return propertyWidgetMap.get(propertyChain);
        }

        /**
         * disposes all binding information for this part, includes bindings, validators and widget
         * mappings
         */
        public void dispose()
        {
            // remove bindings
            for (Binding binding : bindings)
            {
                dbContext.removeBinding(binding);
                binding.dispose();
            }
            propertyWidgetMap.clear();
            validators.clear();
            bindings.clear();
            model = null;
            isBound = false;
        }

    }

    public ValidationManager(String instanceName)
    {
        this.instanceName = instanceName;
    }

    /**
     * sets the message manager to display validation and binding errors on.
     */
    public void initialize(IMessageManager messageManager)
    {
        dbContext = new DataBindingContext();
        validationState = new WritableValue(Status.OK_STATUS, IStatus.class);
        this.messageManager = messageManager;
        bindingStatus = new AggregateValidationStatus(getBindingContext().getBindings(),
                AggregateValidationStatus.MERGED);
        if (bindingStatusChangeListener != null)
        {
            bindingStatus.removeValueChangeListener(bindingStatusChangeListener);
        }
        bindingStatusChangeListener = new IValueChangeListener()
        {
            public void handleValueChange(ValueChangeEvent event)
            {
                // make sure trackers are registered, thus this seemingly
                // useless assignment is
                // absolutely vital !
                ValueDiff valueDiff = event.diff;
                @SuppressWarnings("unused")
                Object oldValue = valueDiff.getOldValue();
                @SuppressWarnings("unused")
                Object newValue = valueDiff.getNewValue();
                updateValidationStatus(null);

            }
        };
        bindingStatus.addValueChangeListener(bindingStatusChangeListener);
        updateValidationStatus(null);
    }

    protected UpdateValueStrategy createModelToTargetUpdateStrategy(ModelAdapter adapter)
    {
        // TODO: use model adapter strategy
        return new RCPUpdateValueStrategy(adapter)
        {
            @Override
            protected IStatus doSet(final IObservableValue observableValue, final Object value)
            {
                final IStatus result = super.doSet(observableValue, value);
                // update status asynchronously since this binding message has
                // not yet been updated
                Display.getCurrent().asyncExec(new Runnable()
                {
                    public void run()
                    {
                        modelValueChanged(true, observableValue, value, result);
                    };
                });
                return result;
            }
        };
    }

    protected UpdateValueStrategy createTargetToModelUpdateStrategy(ModelAdapter adapter)
    {
        // TODO: use model adapter strategy
        return new RCPUpdateValueStrategy(adapter)
        {
            @Override
            protected IStatus doSet(final IObservableValue observableValue, final Object value)
            {
                final IStatus result = super.doSet(observableValue, value);
                // update status asynchronously since this binding message has
                // not yet been updated
                Display.getCurrent().asyncExec(new Runnable()
                {
                    public void run()
                    {
                        modelValueChanged(false, observableValue, value, result);
                    };
                });
                return result;
            }
        };
    }

    /**
     * all bindings are monitored for changes; this method updates all validators on changes
     * 
     * @param modelToTarget true, if the change originated from the model, false if from user. This
     *            can be used by subclasses to init default values which depend on user selections
     */
    protected void modelValueChanged(boolean modelToTarget, IObservableValue observableValue,
                                     Object value, IStatus result)
    {
        updateValidationStatus(observableValue);
    }

    /**
     * update the aggregated validation status of all parts managed by this validation manager based
     * on the change in the given observableValue
     * 
     * @param observableValue changed observable, null: recalculate complete status
     */
    public IStatus updateValidationStatus(IObservableValue observableValue)
    {
        // check errors
        if (isValidationEnabled)
        {
            MultiStatus status = updateBindingMessages();
            MultiStatus validationErrors = updateValidationMessages(observableValue);
            status.merge(validationErrors);
            validationState.setValue(status);
            return status;
        }
        else
        {
            // should never happen after initialization of ValidationManager
            return ValidationStatus
                    .error("Internal Error: Validation is disabled. If you disable validation, make sure enableValidation(true) is called afterwards.");//$NON-NLS-1$
        }
    }

    /**
     * Default: validation is enabled! Enables or disables validation. Can be used to prevent
     * flickering while creating and binding ui elements. if validation is set to enabled (true): a
     * revalidation will be done automatically. if validation is set to disabled (false): no
     * validation messages were displayed until validtation is enabled. you should use this function
     * the following way:
     * 
     * <pre>
     * try{
     *      //disable validation for better performance
     *      validationManagerObject.enableValidation(false);
     *      ... create and bind ui elements...
     * finally{
     *      //enables validation in ANY (-&gt; finally{})) case (revalidation will be done automatically)
     *      validationManagerObject.enableValidation(true);
     * }
     * 
     * </pre>
     * 
     * @param enableValidation indicates if validation should be enabled (true) or disabled.
     */
    public void enableValidation(final boolean enableValidation)
    {
        if (enableValidation)
        {
            // run revalidation in async mode, since update of values
            // (modelValueChanged) also runs async!
            Display.getCurrent().asyncExec(new Runnable()
            {
                public void run()
                {
                    // enable validation even "before" revalidate will be
                    // called, not earlier! -> not thread safe!
                    isValidationEnabled = true;
                    revalidate();
                };
            });
        }
        else
        {
            // disable validation
            this.isValidationEnabled = false;
        }

    }

    /**
     * revalidate the whole form; usually this need not be called since validation state is
     * maintained automatically; it might be useful if you have validators which do not update
     * automatically or parts of the datamodel validated do not fire change notifications, or your
     * validator validates lists which are not supported for auto-update yet.
     * 
     * @return current validation status
     */
    public IStatus revalidate()
    {
        IStatus result = updateValidationStatus(null);
        return result;
    }

    /**
     * force an update of all bindings from the model to the form; this can be used for models which
     * do not fire proper change events
     */
    public void updateModelToForm()
    {
        getBindingContext().updateTargets();
        revalidate();
    }

    /**
     * force an update of all bindings from the form to the model ; this can be used for models
     * which do not fire proper change events
     */
    public void updateFormToModel()
    {
        getBindingContext().updateModels();
        revalidate();
    }

    /**
     * Register a widget manually to enable decoration without explicitly bind the rcpWidget.
     * Used only for tables, trees or lists where input is "bound" using setInput(...) not ValidationManager.bind...(...) method.
     * @param rcpWidget widget to add the map
     * @param model bean model
     * @param properties list of properties
     */
    public void registerWidgetForProperty(RCPControl rcpWidget, Object model, Object... properties)
    {
        IPropertyChain chain = createPropertyChain(model, properties);
        if (chain != null && rcpWidget != null)
        {
            currentInfo.propertyWidgetMap.put(chain, rcpWidget);
        }
    }
    
    /**
     * Removes the custom registration for a widget to enable decoration without explicitly bind the rcpWidget.
     * Used only for tables, trees or lists where input is "bound" using setInput(...) not ValidationManager.bind...(...) method.
     * @param rcpWidget widget to add the map
     * @param model bean model
     * @param properties list of properties
     */
    public void unregisterWidgetForProperty(RCPControl rcpWidget, Object model, Object... properties)
    {
        IPropertyChain chain = createPropertyChain(model, properties);
        if (chain != null && rcpWidget != null)
        {
            currentInfo.propertyWidgetMap.remove(chain);
        }
    }
    
    private MultiStatus updateValidationMessages(IObservableValue observableValue)
    {
        // revalidate all parts and their validators
        MultiStatus result = new MultiStatus(Activator.PLUGIN_ID, 1, Messages
                .getString("ValidationManager.ValidationErrors"), null); //$NON-NLS-1$
        for (FormPartInfo partInfo : viewPartToBindingMap.values())
        {
            for (IModelValidator validator : partInfo.validators)
            {
                IStatus status = validator.validate(partInfo.model);
                List<Object> invalidProperties = Arrays.asList(validator
                        .getInvalidProperties(partInfo.model));
                boolean decorateField = validator.isDecorateField();
                boolean showMessage = validator.isShowMessage();
                for (Object property : validator.getProperties())
                {
                    // TODO LATER: more than one control could be bound,
                    // which one to decorate ?
                    RCPControl widget = partInfo.getWidgetForProperty(partInfo.model, property);
                    if (widget == null)
                    {
                        LOG.warning("Widget not found for validator \"" + validator.toString()//$NON-NLS-1$
                                + "\" on model \"" + partInfo.getClass().getCanonicalName()//$NON-NLS-1$
                                + "\" for property \"" + property + "\"");//$NON-NLS-1$ //$NON-NLS-2$
                    }

                    if (invalidProperties.contains(property))
                    {
                        // show only marker if widget is: enabled, visible or
                        // even no widget was found!
//                        if (widget != null && widget.isChainEnabled() && widget.isChainVisible()
                      if (widget != null && widget.hasState(EControlState.ENABLED) && widget.isChainEnabled() && widget.hasState(EControlState.VISIBLE) && widget.isChainVisible()
                                        
                                || widget == null)
                        {
                            updateMessage(validator.getId(), status,
                                    widget == null ? null : (Control) widget.getSWTWidget(),
                                    decorateField, showMessage);
                        }
                        else
                        {
                            // otherwise do not show an error marker
                            updateMessage(validator.getId(), Status.OK_STATUS,
                                    widget == null ? null : (Control) widget.getSWTWidget(),
                                    decorateField, showMessage);

                        }
                    }
                    else
                    {
                        updateMessage(validator.getId(), Status.OK_STATUS,
                                widget == null ? null : (Control) widget.getSWTWidget(),
                                decorateField, showMessage);

                    }
                }
                result.merge(status);
            }
        }
        return result;
    }

    /**
     * for all bindings: applies status to the bound target controls (e.g. error marker if an error
     * occured) and updates the message manager with corresponding error messages. This is
     * automatically done on status changes
     */
    private MultiStatus updateBindingMessages()
    {
        MultiStatus result = new MultiStatus(Activator.PLUGIN_ID, 1, Messages
                .getString("ValidationManager.ValidationErrors"), null); //$NON-NLS-1$
        for (Object o : getBindingContext().getBindings())
        {
            Binding binding = (Binding) o;
            IStatus status = (IStatus) binding.getValidationStatus().getValue();
            Control control = null;
            if (binding.getTarget() instanceof ISWTObservable)
            {
                ISWTObservable swtObservable = (ISWTObservable) binding.getTarget();
                control = (Control) swtObservable.getWidget();
            }
            if (!status.isOK())
            {

                result.merge(status);
            }
            updateMessage("Binding-" + binding, status, control, true, true); //$NON-NLS-1$
        }
        return result;
    }

    public DataBindingContext getBindingContext()
    {
        return dbContext;
    }

    private FormPartInfo getInfo(RCPFormPart part)
    {
        if (!viewPartToBindingMap.containsKey(part))
        {
            viewPartToBindingMap.put(part, new FormPartInfo(null));
        }
        return viewPartToBindingMap.get(part);
    }

    public void addValidator(RCPFormPart part, IModelValidator validator)
    {
        getInfo(part).validators.add(validator);
    }

    /**
     * updates the message associated with the given key and control according to the given status
     * 
     * @param key message key, must be unique among all messages which might be generated by the
     *            managed model
     * @param status status
     * @param control control which should be marked with the message, may be null
     * @param decorateField true if field should be decorated with marker if status not ok
     * @param showMessage true if message should be shown in form heading
     */
    private void updateMessage(String key, IStatus status, Control control, boolean decorateField,
                               boolean showMessage)
    {

        // for this ValidationManager has to keep a duplicate list of current
        // messages
        // since message manager does not provide querying existing messages
        int messageType = getMessageTypeFromStatus(status);

        boolean messageExists = false; // true if message is already displayed
        boolean messageUpdated = false; // true if message exists but text or
        // status has changed

        // check if already there and not attached to control
        if ((control == null || !decorateField) && messageMap.containsKey(key))
        {
            messageExists = true;
            IStatus oldStatus = messageMap.get(key);
            if (!nullEquals(status.getMessage(), oldStatus.getMessage())
                    || !nullEquals(messageType, getMessageTypeFromStatus(oldStatus)))
            {
                // message has changed
                messageUpdated = true;
            }

        }
        // check if already there and attached to control
        if (control != null && controlToMessageMapMap.get(control) != null)
        {
            Map<String, IStatus> controlMessages = controlToMessageMapMap.get(control);
            IStatus oldStatus = controlMessages.get(key);
            if (oldStatus != null)
            {
                // same message already there
                messageExists = true;
                if (!nullEquals(status.getMessage(), oldStatus.getMessage())
                        || !nullEquals(messageType, getMessageTypeFromStatus(oldStatus)))
                {
                    // message has changed
                    messageUpdated = true;
                }
            }
        }

        if (!status.isOK())
        {
            if (control != null && decorateField)
            {
                if (!messageExists || messageUpdated)
                {
                    Map<String, IStatus> controlMessageMap = controlToMessageMapMap.get(control);
                    if (controlMessageMap == null)
                    {
                        // create entry
                        controlMessageMap = new HashMap<String, IStatus>();
                        controlToMessageMapMap.put(control, controlMessageMap);
                    }
                    controlMessageMap.put(key, status);
                    messageManager.addMessage(key, status.getMessage(), null, messageType, control);
                }
            }
            else if (showMessage)
            {
                if (!messageExists || messageUpdated)
                {
                    messageMap.put(key, status);
                    messageManager.addMessage(key, status.getMessage(), null, messageType);
                }
            }
        }
        else
        {
            if (control != null && decorateField)
            {
                if (messageExists)
                {
                    Map<String, IStatus> controlMessageMap = controlToMessageMapMap.get(control);
                    Validate
                            .notNull(controlMessageMap,
                                    "Error in Message Caching: message should exist in controlMap but controlMap does not exist");//$NON-NLS-1$
                    controlMessageMap.remove(key);
                    Validate
                            .notNull(controlMessageMap,
                                    "Error in Message Caching: message should exist in controlMap but does not");//$NON-NLS-1$
                    messageManager.removeMessage(key, control);
                }
            }
            else
            {
                if (messageExists)
                {
                    IStatus oldStatus = messageMap.remove(key);
                    Validate
                            .notNull(oldStatus,
                                    "Error in Message Caching: message should exist in messageMap but does not");//$NON-NLS-1$
                    messageManager.removeMessage(key);
                }
            }
        }
    }

    /**
     * converts status severity to IMessageProvider type
     * 
     * @param status
     * @return IMessageProvider type code
     */
    public static int getMessageTypeFromStatus(IStatus status)
    {
        int result = IMessageProvider.NONE;
        switch (status.getSeverity())
        {
            case IStatus.OK:
                result = IMessageProvider.NONE;
                break;
            case IStatus.WARNING:
                result = IMessageProvider.WARNING;
                break;
            case IStatus.ERROR:
                result = IMessageProvider.ERROR;
                break;
            case IStatus.INFO:
                result = IMessageProvider.INFORMATION;
                break;
        }
        return result;
    }

    /**
     * bind the given widget value to the given model property using default binding. This is the
     * main bind method and should be used by all value bindings. It checks if the model adapter has
     * a range adapter for the bound attribute and for {@link RCPRadioGroup} and
     * {@link RCPSimpleCombo} and {@link RCPCombo} it installs the range adapter if one is found.
     * <p>
     * For some widgets specific converters are installed.
     */
    public Binding bindValue(RCPControl rcpWidget,
                             IValidatorConverterStrategy validatorConverterStrategy, Object bean,
                             Object... properties)
    {
        Validate.isTrue(rcpWidget != null);
        IPropertyChain propertyChain = createPropertyChain(bean, properties);
        handleRangeAdapter(rcpWidget, bean, propertyChain);
        IObservableValue widgetObservableValue = getObservableValue(rcpWidget);
        ValidatorConverterStrategy vcs = new ValidatorConverterStrategy();
        // set default converters
        if (validatorConverterStrategy.getModelToTargetConverter() == null)
            vcs.setModelToTargetConverter(getConverter(rcpWidget, true, bean, properties));
        else
            vcs.setModelToTargetConverter(validatorConverterStrategy.getModelToTargetConverter());
        if (validatorConverterStrategy.getTargetToModelConverter() == null)
            vcs.setTargetToModelConverter(getConverter(rcpWidget, false, bean, properties));
        else
            vcs.setTargetToModelConverter(validatorConverterStrategy.getTargetToModelConverter());
        // set validators
        vcs.setTargetToModelValidator(validatorConverterStrategy.getTargetToModelValidator());
        vcs.setModelToTargetValidator(validatorConverterStrategy.getModelToTargetValidator());

        Binding binding = bindValue(widgetObservableValue, vcs, bean, properties);
        associateBindingWithFormPart(binding, rcpWidget, propertyChain);

        debugBindingValue(rcpWidget, propertyChain.toString());

        return binding;
    }

    /**
     * bind the given widget value to the given model property using default binding. This is the
     * main bind method and should be used by all value bindings. It checks if the model adapter has
     * a range adapter for the bound attribute and for {@link RCPRadioGroup} and
     * {@link RCPSimpleCombo} and {@link RCPCombo} it installs the range adapter if one is found.
     * <p>
     * For some widgets specific converters are installed.
     */
    public Binding bindValue(RCPControl rcpWidget, Object bean, Object... properties)
    {
        ValidatorConverterStrategy vcs = new ValidatorConverterStrategy();
        vcs.setModelToTargetConverter(getConverter(rcpWidget, true, bean, properties));
        vcs.setTargetToModelConverter(getConverter(rcpWidget, false, bean, properties));
        return this.bindValue(rcpWidget, vcs, bean, properties);
    }

    /**
     * bind the given widgets values to the given model properties using default detail binding.
     * This is the main bind method and should be used by all value bindings. It checks if the model
     * adapter has a range adapter for the bound attributes and for {@link RCPRadioGroup} and
     * {@link RCPSimpleCombo} and {@link RCPCombo} it installs the range adapters if found.
     * <p>
     * For some widgets specific converters are installed.
     * 
     * @param bean object which contains the master value. Not null
     * @param masterProperty property of the master value. Not null
     * @param rcpWidgets list of widgets to bind the detail values. number of widgets has to be
     *            equal to number of detail properties. Not null and no null elements
     * @param detailProperties list of properties to bind the widgets. number of properties has to
     *            be equal to number of widgets. Not null and no null elements
     * @return observable to the master object
     */

    public IObservableValue bindDetailValue(Object bean, String masterProperty,
                                            RCPControl[] rcpWidgets, String[] detailProperties)
    {
        Validate.notNull(bean);
        Validate.notNull(masterProperty);

        // get observable value for the master object
        IPropertyChain masterPropertyChain = createPropertyChain(bean, masterProperty);
        IObservableValue masterObservable = getObservableForProperty(bean, masterPropertyChain);

        // get the master object
        ModelAdapter modelAdapter = this.getModelAdapter(bean);
        Object masterBean = modelAdapter.getValue(bean, masterPropertyChain);
        this.bindDetailValues(modelAdapter, masterBean, masterObservable, rcpWidgets,
                detailProperties);

        return masterObservable;
    }

    private void bindDetailValues(ModelAdapter modelAdapter, Object masterBean,
                                  IObservableValue masterObservable, RCPControl[] rcpWidgets,
                                  String[] detailProperties)
    {
        Validate.notNull(rcpWidgets);
        Validate.notNull(detailProperties);
        Validate.isTrue(rcpWidgets.length == detailProperties.length);
        Validate.noNullElements(rcpWidgets);
        Validate.noNullElements(detailProperties);

        ModelAdapter adapter = getModelAdapter(masterBean);
        for (int i = 0; i < rcpWidgets.length; ++i)
        {
            RCPControl rcpWidget = rcpWidgets[i];

            IPropertyChain detailPropertyChain = createPropertyChain(masterBean,
                    detailProperties[i]);
            handleRangeAdapter(rcpWidget, masterBean, detailPropertyChain);

            // create update strategies
            IObservableValue widgetObservableValue = getObservableValue(rcpWidget);
            UpdateValueStrategy targetToModelUpdateStrategy = createTargetToModelUpdateStrategy(adapter);

            // create converter
            IConverter modelToTargetConverter = getConverter(rcpWidget, true, masterBean, detailPropertyChain);
            IConverter targetToModelConverter = getConverter(rcpWidget, false, masterBean, detailPropertyChain);
            if (targetToModelConverter != null)
            {
                targetToModelUpdateStrategy.setConverter(targetToModelConverter);
            }
            UpdateValueStrategy modelToTargetUpdateStrategy = createModelToTargetUpdateStrategy(adapter);
            if (modelToTargetConverter != null)
            {
                modelToTargetUpdateStrategy.setConverter(modelToTargetConverter);
            }

            // createObservable
            IObservableValue detailBeansObservableValue = modelAdapter.getObservableDetailValue(
                    masterObservable, detailPropertyChain);

            Binding binding = getBindingContext().bindValue(widgetObservableValue,
                    detailBeansObservableValue, targetToModelUpdateStrategy,
                    modelToTargetUpdateStrategy);

            associateBindingWithFormPart(binding, rcpWidget, detailPropertyChain);

            debugBindingValue(rcpWidget, detailPropertyChain.toString());
        }

    }

    private void associateBindingWithFormPart(Binding binding, RCPControl rcpWidget,
                                              IPropertyChain propertyChain)
    {
        if (currentInfo != null)
        {
            if (!currentInfo.bindings.contains(binding))
            {
                currentInfo.bindings.add(binding);
            }
            if (propertyChain != null && rcpWidget != null)
            {
                currentInfo.propertyWidgetMap.put(propertyChain, rcpWidget);
            }
        }
        else
        {
            LOG.info("ValidationManager(" + instanceName + ").bindValue called  for "//$NON-NLS-1$ //$NON-NLS-2$
                    + propertyChain
                    + " without formpart context (bindPart() was not called); rebind not possible");//$NON-NLS-1$
        }
    }

    /**
     * handles auto-creation of ranges for radio groups and combos if a range adapter is available
     * 
     * @param rcpWidget widget
     * @param bean bean to bind against
     * @param propertyChain propertyChain for the addressed property
     */
    protected void handleRangeAdapter(RCPControl rcpWidget, Object bean,
                                      IPropertyChain propertyChain)
    {
        try
        {
            if (setRanges
                    && (rcpWidget instanceof RCPSimpleCombo || rcpWidget instanceof RCPCombo || rcpWidget instanceof RCPRadioGroup))
            {
                // check if a range adapter is available; if yes, get
                // label/content
                // provider and set input
                IRangeAdapter rangeAdapter = ModelAdapter.getAdapterForInstance(bean)
                        .getRangeAdapter(propertyChain);
                if (rangeAdapter != null)
                {
                    if (rcpWidget instanceof RCPRadioGroup)
                    {
                        RCPRadioGroup radioGroup = (RCPRadioGroup) rcpWidget;
                        Object nullValue = radioGroup.hasNullValue() ? NullValue.getInstance()
                                                                    : null;
                        if (radioGroup.getRadioGroupManager() == null)
                        {
                            Object input = rangeAdapter.getInput();
                            IStructuredContentProvider provider = rangeAdapter
                                    .getContentProvider(nullValue);
                            Object[] elements = provider.getElements(input);
                            radioGroup.createRadioButtons(elements, rangeAdapter.getLabelProvider(
                                    radioGroup.getPresentationKey(), radioGroup
                                            .getNullValuePresentationKey()));
                            LOG.log(Level.FINE,
                                    "Auto-created radio buttons from model range adapter for property " //$NON-NLS-1$
                                            + propertyChain.toString());

                        }
                    }
                    else
                    {
                        Validate.isTrue(rcpWidget instanceof RCPCombo
                                || rcpWidget instanceof RCPSimpleCombo);
                        RCPSimpleCombo combo = rcpWidget instanceof RCPCombo
                                                                            ? ((RCPCombo) rcpWidget)
                                                                                    .getRCPControl()
                                                                            : (RCPSimpleCombo) rcpWidget;

                        Object nullValue = combo.hasNullValue() ? NullValue.getInstance() : null;

                        IViewer comboViewer = (IViewer) rcpWidget;
                        // set content/label provider and input
                        if (comboViewer.getViewer().getContentProvider() == null)
                        {
                            comboViewer.getViewer().setContentProvider(
                                    rangeAdapter.getContentProvider(nullValue));
                            LOG.log(Level.FINE,
                                    "Auto-created content provider from model range adapter for property " //$NON-NLS-1$
                                            + propertyChain.toString());
                        }
                        if (comboViewer.getViewer().getLabelProvider() == null
                        // set special viewer for <null> representations
                                || nullValue == NullValue.getInstance())
                        {
                            comboViewer.getViewer().setLabelProvider(
                                    rangeAdapter.getLabelProvider(combo.getPresentationKey(), combo
                                            .getNullValuePresentationKey()));
                            LOG.log(Level.FINE,
                                    "Auto-created label provider from model range adapter for property " //$NON-NLS-1$
                                            + propertyChain.toString());
                        }
                        if (comboViewer.getViewer().getInput() == null)
                        {
                            comboViewer.getViewer().setInput(rangeAdapter.getInput());
                            LOG.log(Level.FINE,
                                    "Auto-created input from model range adapter for property " //$NON-NLS-1$
                                            + propertyChain.toString());
                        }
                    }
                }
            }

        }
        catch (Exception ex)
        {
            System.err.println("Exception setting RangeAdapter"); //$NON-NLS-1$
            ex.printStackTrace();
        }
    }

    /**
     * bind the given observable value to the given model property using default binding. USE WITH
     * CARE SINCE FRAMEWORK NEEDS MAPPING OF PROPERTY TO WIDGET TO WORK PROPERLY; WIDGET IS NOT
     * KNOWN HERE THUS VALIDATOR ERROR MARKER HANDLING WILL NOT WORK!
     */
    public Binding bindValue(IObservableValue widgetObservableValue,
                             IConverter modelToTargetConverter, IConverter targetToModelConverter,
                             Object bean, Object... properties)
    {
        ValidatorConverterStrategy vcs = new ValidatorConverterStrategy();
        vcs.setModelToTargetConverter(modelToTargetConverter);
        vcs.setTargetToModelConverter(targetToModelConverter);
        return this.bindValue(widgetObservableValue, vcs, bean, properties);
    }

    /**
     * bind the given observable value to the given model property using default binding. USE WITH
     * CARE SINCE FRAMEWORK NEEDS MAPPING OF PROPERTY TO WIDGET TO WORK PROPERLY; WIDGET IS NOT
     * KNOWN HERE THUS VALIDATOR ERROR MARKER HANDLING WILL NOT WORK!
     */
    public Binding bindValue(IObservableValue widgetObservableValue, Object bean,
                             Object... properties)
    {
        return this.bindValue(widgetObservableValue, new ValidatorConverterStrategy(), bean,
                properties);
    }

    /**
     * bind the given observable value to the given model property using default binding. USE WITH
     * CARE SINCE FRAMEWORK NEEDS MAPPING OF PROPERTY TO WIDGET TO WORK PROPERLY; WIDGET IS NOT
     * KNOWN HERE THUS VALIDATOR ERROR MARKER HANDLING WILL NOT WORK!
     */
    public Binding bindValue(IObservableValue widgetObservableValue,
                             IValidatorConverterStrategy validatorConverterStrategy, Object bean,
                             Object... properties)
    {
        ModelAdapter adapter = getModelAdapter(bean);
        Validate.notNull(adapter);
        IPropertyChain propertyChain = createPropertyChain(bean, properties);
        UpdateValueStrategy targetToModelUpdateStrategy = createTargetToModelUpdateStrategy(adapter);
        if (validatorConverterStrategy.getTargetToModelConverter() != null)
        {
            targetToModelUpdateStrategy.setConverter(validatorConverterStrategy
                    .getTargetToModelConverter());
        }
        if (validatorConverterStrategy.getTargetToModelValidator() != null)
        {
            targetToModelUpdateStrategy.setAfterGetValidator(validatorConverterStrategy
                    .getTargetToModelValidator());
        }
        UpdateValueStrategy modelToTargetUpdateStrategy = createModelToTargetUpdateStrategy(adapter);
        if (validatorConverterStrategy.getModelToTargetConverter() != null)
        {
            modelToTargetUpdateStrategy.setConverter(validatorConverterStrategy
                    .getModelToTargetConverter());
        }
        if (validatorConverterStrategy.getModelToTargetValidator() != null)
        {
            modelToTargetUpdateStrategy.setAfterGetValidator(validatorConverterStrategy
                    .getModelToTargetValidator());
        }
        IObservableValue beansObservableValue = getObservableForProperty(bean, propertyChain);
        if(properties.length > 1)
        {            
            //why this listener is needed: if nested property is used and a nested property
            //is null, a special master-detail binding is used instead of "direct" creation of the
            //beans observable value. hence the "master value" is not available here validationManager
            //needs to revalidate if master value changes!
            beansObservableValue.addChangeListener(new IChangeListener(){
                
                public void handleChange(ChangeEvent event)
                {
                    revalidate();
                }});
        }
        Binding binding = getBindingContext().bindValue(widgetObservableValue,
                beansObservableValue, targetToModelUpdateStrategy, modelToTargetUpdateStrategy);
        associateBindingWithFormPart(binding, null, null);
        Validate.isTrue(binding != null);
        return binding;
    }

    private IPropertyChain createPropertyChain(Object bean, Object... properties)
    {
        ModelAdapter modelAdapter = getModelAdapter(bean);
        Object metaClass = modelAdapter.getMetaClass(bean);
        return modelAdapter.getPropertyChain(metaClass, properties);
    }

    private ModelAdapter getModelAdapter(Object bean)
    {
        return ModelAdapter.getAdapterForInstance(bean);
    }

    public Binding bindValue(RadioGroupManager rgManager, Object bean, Object... properties)
    {
        Validate.isTrue(rgManager != null);
        IPropertyChain propertyChain = createPropertyChain(bean, properties);
        UpdateValueStrategy targetToModelUpdateStrategy = createTargetToModelUpdateStrategy(getModelAdapter(bean));
        UpdateValueStrategy modelToTargetUpdateStrategy = createModelToTargetUpdateStrategy(getModelAdapter(bean));
        IObservableValue widgetObservableValue = new RadioGroupObservableValue(rgManager);
        IObservableValue beansObservableValue = getObservableForProperty(bean, propertyChain);
        Binding binding = getBindingContext().bindValue(widgetObservableValue,
                beansObservableValue, targetToModelUpdateStrategy, modelToTargetUpdateStrategy);
        Validate.isTrue(binding != null);
        associateBindingWithFormPart(binding, null, null);
        return binding;
    }

    /**
     * binds the given radio buttons to the given bean property of type Boolean.
     * 
     * @param trueRadioButton radio button bound to the true value
     * @param falseRadioButton radio button bound to the false value
     * @param bean bean to bind to
     * @param propertyName Boolean property to bind to
     * @return binding
     */
    public Binding bindRadioToBoolean(RCPSimpleButton trueRadioButton,
                                      RCPSimpleButton falseRadioButton, Object bean,
                                      Object... properties)
    {
        // associate buttons with true/false and bind using RadioObservableValue
        RadioGroupManager manager = new RadioGroupManager(new RCPSimpleButton[]{trueRadioButton,
                falseRadioButton}, new Object[]{Boolean.TRUE, Boolean.FALSE});
        Binding binding = bindValue(manager, bean, properties);
        Validate.notNull(binding);

        // debug bindings
        IPropertyChain propertyChain = createPropertyChain(bean, properties);
        debugBindingValue(trueRadioButton, propertyChain.toString());
        debugBindingValue(falseRadioButton, propertyChain.toString());

        return binding;
    }

    /**
     * bind any observable to an rcpWidget using standard update strategies and observableValue for
     * widget
     * 
     * @deprecated needs model adapter to create update strategies to use, use new method with
     *             additional model adapter
     */
    public Binding bindValue(RCPWidget rcpWidget, IObservableValue observable)
    {
        return bindValue(BeanAdapter.getInstance(), rcpWidget, observable);
    }

    /**
     * bind any observable to an rcpWidget using standard update strategies and observableValue for
     * widget
     * 
     * @deprecated needs model adapter to create update strategies to use, use new method with
     *             additional model adapter
     */
    public Binding bindValue(ModelAdapter adapter, RCPWidget rcpWidget, IObservableValue observable)
    {
        Validate.isTrue(rcpWidget != null);
        Validate.isTrue(observable != null);
        UpdateValueStrategy targetToModelUpdateStrategy = createTargetToModelUpdateStrategy(adapter);
        UpdateValueStrategy modelToTargetUpdateStrategy = createModelToTargetUpdateStrategy(adapter);
        IObservableValue widgetObservableValue = getObservableValue(rcpWidget);
        IObservableValue beansObservableValue = observable;
        Binding binding = getBindingContext().bindValue(widgetObservableValue,
                beansObservableValue, targetToModelUpdateStrategy, modelToTargetUpdateStrategy);
        Validate.isTrue(binding != null);
        associateBindingWithFormPart(binding, null, null);
        if (rcpWidget instanceof RCPControl)
            debugBindingValue((RCPControl) rcpWidget, beansObservableValue.toString());
        return binding;
    }

    private IObservableValue getObservableForProperty(Object bean, IPropertyChain propertyChain)
    {
        return getModelAdapter(bean).getObservableValue(bean, propertyChain);
    }

    /**
     * create converter for the given widget
     * 
     * @param widget
     * @param modelToTarget
     * @return converter or null if default converters should be used
     * 
     * @deprecated use {@link ValidationManager#getConverter(RCPWidget, boolean, Object, Object...)} instead.
     */
    protected IConverter getConverter(RCPWidget widget, boolean modelToTarget)
    {
        return this.getConverter(widget, modelToTarget, null, null);
    }
    
    /**
     * create converter for the given widget
     * 
     * @param widget
     * @param modelToTarget
     * @param model object, not null
     * @param properties a list of properties addressing the nested property, not null
     * @return converter or null if default converters should be used
     */
    protected IConverter getConverter(RCPWidget widget, boolean modelToTarget, Object model, Object... properties)
    {
        IConverter result = null;
        // if the widget is a combo type or radio group type, a converter is
        // used for the NullValue
        // placeholder
        if (widget instanceof IPresentationConfiguration
                && ((IPresentationConfiguration) widget).hasNullValue())
        {
            if(null == model || null == properties)
            {
                LOG.warning("Either model or properties list is null. For widgets which are of type IPresentationConfiguration and has null value representation (IPresentationConfiguration.hasNullValue ist true) it's strongly recommended to use ValidationManager#getConverter(RCPWidget widget, boolean modelToTarget, Object model, Object... properties) with appropriate values!");
                //statement added for backward compatibility for binding api < 3.5
                result = modelToTarget ? new ObjectToObjectWithNullValueConverter()
                : new ObjectWithNullValueToObjectConverter();
            }else
            {
                IPropertyChain propertyChain = createPropertyChain(model, properties);
                result = modelToTarget ? new ObjectToObjectWithNullValueConverter(propertyChain.getType())
                : new ObjectWithNullValueToObjectConverter(propertyChain.getType());
            }
        }
        return result;
    }

    /**
     * retrieves suitable ObservableValues for different widgets
     * 
     * @param rcpWidget widget to wrap
     * @return IObservableValue suitable for the widget
     */
    protected IObservableValue getObservableValue(RCPWidget rcpWidget)
    {
        IObservableValue iObservableValue = null;
        Control control = rcpWidget.getTypedWidget();
        if (control != null)
        {
            if (rcpWidget instanceof IViewer)
            {
                iObservableValue = ViewersObservables.observeSingleSelection(((IViewer) rcpWidget)
                        .getViewer());
            }
            else if (control instanceof Text)
            {
                iObservableValue = SWTObservables.observeText(control, SWT.Modify);
            }
            else if (control instanceof org.eclipse.swt.widgets.List)
            {
                iObservableValue = SWTObservables.observeSingleSelectionIndex(control);
            }
            else if (control instanceof Table)
            {
                iObservableValue = SWTObservables.observeSingleSelectionIndex(control);
            }
            else if (control instanceof Button)
            {
                iObservableValue = SWTObservables.observeSelection(control);
            }
            else if (rcpWidget instanceof RCPRadioGroup)
            {
                RadioGroupManager radioGroupManager = ((RCPRadioGroup) rcpWidget)
                        .getRadioGroupManager();
                Validate
                        .notNull(
                                radioGroupManager,
                                "No RadioGroupManager available for the widget " //$NON-NLS-1$
                                        + rcpWidget.getClass().getName()
                                        + ". Please make sure the modeladapter provides a range adapter for the attribute to bind or you have explicitly called one of the e.g. RCPRadioGroup.createRadioButtons() methods before binding."); //$NON-NLS-1$
                iObservableValue = new RadioGroupObservableValue(radioGroupManager);
            }
        }
        if (iObservableValue == null)
        {
            throw new IllegalArgumentException("No Default ObservableValue found for Widget type: " //$NON-NLS-1$
                    + rcpWidget.getClass().getName());
        }
        return iObservableValue;
    }

    /**
     * create an observable set for the given widget observing the multi selection. works for
     * checked viewers and selection providing viewers.
     * 
     * @param viewer to bind
     * @return observable set bound against the selection of the viewer; for {@link ICheckable}
     *         viewers this is the set of checked elements, otherwise it is the selection as set.
     */
    protected IObservableSet getObservableSet(Viewer viewer)
    {
        Validate.notNull(viewer,
                "Viewer must have been created before bind. Did you forget add(control) ?"); //$NON-NLS-1$
        if (viewer instanceof ICheckable)
        {
            return ViewersObservables.observeCheckedElements((ICheckable) viewer, null);
        }
        else
        {
            return new ModifiableListToSetAdapter(ViewersObservables.observeMultiSelection(viewer));
        }
    }

    /**
     * bind an inverted observable to the given state of a rcpControl
     * 
     * @param rcpControl Control to bind state
     * @param state see {@link EControlState} for supported states. state could be
     *            {@link EControlState#ENABLED}, {@link EControlState#READONLY},
     *            {@link EControlState#VISIBLE}
     * @param observable observable value to bind with (e.g. an object of type
     *            {@link ObservableStatusToBooleanAdapter})
     * @return
     */
    public Binding bindInvertedState(RCPControl rcpControl, EControlState state,
                                     IObservableValue observable)
    {
        Validate.isTrue(rcpControl != null);
        Validate.isTrue(observable != null);
        IObservableValue widgetObservableValue = getInvertedObservableState(rcpControl, state);
        IObservableValue beansObservableValue = observable;
        Binding binding = getBindingContext().bindValue(widgetObservableValue,
                beansObservableValue, null, null);
        associateBindingWithFormPart(binding, null, null);
        Validate.isTrue(binding != null);
        Validate.isTrue(binding != null);

        debugBindingDeco(rcpControl, state, beansObservableValue.toString());

        return binding;
    }

    /**
     * bind an observable to the given state of a rcpControl
     * 
     * @param rcpControl Control to bind state
     * @param state see {@link EControlState} for supported states. state could be
     *            {@link EControlState#ENABLED}, {@link EControlState#READONLY},
     *            {@link EControlState#VISIBLE}
     * @param observable observable value to bind with (e.g. an object of type
     *            {@link ObservableStatusToBooleanAdapter})
     * @return
     */
    public Binding bindState(RCPControl rcpControl, EControlState state, IObservableValue observable)
    {
        Validate.isTrue(rcpControl != null);
        Validate.isTrue(observable != null);
        IObservableValue widgetObservableValue = getObservableState(rcpControl, state);
        IObservableValue beansObservableValue = observable;
        Binding binding = getBindingContext().bindValue(widgetObservableValue,
                beansObservableValue, null, null);
        Validate.isTrue(binding != null);
        associateBindingWithFormPart(binding, null, null);
        debugBindingDeco(rcpControl, state, beansObservableValue.toString());

        return binding;
    }

    public Binding bindState(RCPControl rcpWidget, EControlState state, Object bean,
                             Object... properties)
    {
        Validate.isTrue(rcpWidget != null);
        IPropertyChain propertyChain = createPropertyChain(bean, properties);
        IObservableValue stateObservableValue = getObservableState(rcpWidget, state);
        IObservableValue beansObservableValue = getObservableForProperty(bean, propertyChain);
        Binding binding = getBindingContext().bindValue(stateObservableValue, beansObservableValue,
                null, null);
        Validate.isTrue(binding != null);
        associateBindingWithFormPart(binding, rcpWidget, propertyChain);
        debugBindingDeco(rcpWidget, state, propertyChain.toString());

        return binding;
    }

    private IObservableValue getObservableState(RCPControl rcpControl, EControlState state)
    {
        Validate.isTrue(state != EControlState.READONLY || rcpControl instanceof RCPSimpleText
                || rcpControl instanceof RCPText || rcpControl instanceof RCPSimpleCombo
                || rcpControl instanceof RCPCombo);

        IObservableValue result = new StateObservableValue(rcpControl, state);
        return result;
    }

    /**
     * get an observable for binding the given widget state (as opposed to value) to the given
     * widget inverting the state.
     * 
     * @param rcpcontrol widget to bind
     * @param state state to bind; EDITABLE is only supported for text controls, CONTAINED and
     *            MASKED are not supported at all
     * @return IObservableValue for binding, this will be inverted.
     */
    private IObservableValue getInvertedObservableState(RCPControl rcpControl, EControlState state)
    {
        Validate.isTrue(state != EControlState.READONLY || rcpControl instanceof RCPText,
                "Editable state only applies to text fields"); //$NON-NLS-1$
        IObservableValue result = new InvertedStateObservableValue(rcpControl, state);
        return result;
    }

    /**
     * this binds a part to a model. It can be called several times on the same form part with
     * different models. All bindings previously associated with the form part will be destroyed.
     * <p>
     * Must be called before any binding method call.
     */
    public void bindPart(RCPFormPart formPart, Object model)
    {
        FormPartInfo info = internalUnbindPart(formPart);
        info.model = model;
        // make sure all created bindings are registered in the current part
        currentInfo = info;
        formPart.bind(this, model);
        info.isBound = true;
        currentInfo = null;
    }

    /**
     * disposes all bindings and validators associated with the given form part. Only Bindings and
     * Validators which were created in the context of a {@link #bindPart(RCPFormPart, Object)} call
     * are removed.
     * 
     * @param formPart formPart to unbind information for
     */

    public void unbindPart(RCPFormPart formPart)
    {
        internalUnbindPart(formPart);
        viewPartToBindingMap.remove(formPart);
    }

    /**
     * disposes all bindings and validators associated with a form part. Only Bindings and
     * Validators which were created in the context of a {@link #bindPart(RCPFormPart, Object)} call
     * are removed.
     */

    public void unbindAllParts()
    {
        for (RCPFormPart part : viewPartToBindingMap.keySet())
        {
            unbindPart(part);
        }
    }

    /**
     * disposes all bindings and validators associated with the given form part. Only Bindings and
     * Validators which were created in the context of a {@link #bindPart(RCPFormPart, Object)} call
     * are removed.
     * 
     * @param formPart formPart to dispose
     * @return disposed {@link FormPartInfo} associated with the form part, ready for reuse. If none
     *         was found, a new one is created and returned.
     */
    private FormPartInfo internalUnbindPart(RCPFormPart formPart)
    {
        FormPartInfo info = getInfo(formPart);
        if (info.isBound)
        {
            info.dispose();
        }
        return info;

    }

    /**
     * bind widget wrapper to a writable list; this binding is available only for checked tables
     * with CheckboxTableViewer and Tables with TableViewer; the list will be synchronized with the
     * checked elements (for checked tables) or selected elements (for non SWT.CHECK tables)
     * 
     * @param wrapperWithViewer widget wrapper, must be Table with TableViewer or
     *            CheckboxTableViewer
     * @param modelWritableList list to bind
     * @return list binding synchronizing the checked state of the lists
     * @deprecated needs model adapter to create update strategies to use, use new method with
     *             additional model adapter
     */
    public Binding bindSelection(IViewer wrapperWithViewer, IObservableList modelWritableList)
    {
        return bindSelection(BeanAdapter.getInstance(), wrapperWithViewer, modelWritableList);
    }

    /**
     * bind widget wrapper to a writable list; this binding is available only for checked tables
     * with CheckboxTableViewer and Tables with TableViewer; the list will be synchronized with the
     * checked elements (for checked tables) or selected elements (for non SWT.CHECK tables)
     * 
     * @param wrapperWithViewer widget wrapper, must be Table with TableViewer or
     *            CheckboxTableViewer
     * @param modelWritableList list to bind
     * @return list binding synchronizing the checked state of the lists
     */
    public Binding bindSelection(ModelAdapter adapter, IViewer wrapperWithViewer,
                                 IObservableList modelWritableList)
    {

        Validate
                .isTrue(
                        wrapperWithViewer instanceof IViewer,
                        "List Binding is only possible for controls having a viewer like RCPSimpleTable, RCPSimpleTree, RCPSimpleList, RCPSimpleCombo"); //$NON-NLS-1$
        Validate.notNull(wrapperWithViewer);
        ContentViewer viewer = wrapperWithViewer.getViewer();
        Validate.notNull(modelWritableList);
        // the update strategy notifies the model manager of changes of
        // bound view objects
        // so validation and dirty status can be refreshed
        UpdateSetStrategy targetToModelUpdateStrategy = createTargetToModelListUpdateStrategy(adapter);
        UpdateSetStrategy modelToTargetUpdateStrategy = createModelToTargetListUpdateStrategy(adapter);
        IObservableSet widgetObservableList = getObservableSet(viewer);
        Binding binding = getBindingContext().bindSet(widgetObservableList,
                new ModifiableListToSetAdapter(modelWritableList), targetToModelUpdateStrategy,
                modelToTargetUpdateStrategy);
        associateBindingWithFormPart(binding, null, null);
        Validate.notNull(binding);
        return binding;
    }
    /**
     * @deprecated needs model adapter to create update strategies to use, use new method with
     *             additional model adapter
     */

    public Binding bindSelection(IViewer wrapperWithViewer, IObservableValue observableValue)
    {
        return bindSelection(BeanAdapter.getInstance(), wrapperWithViewer, observableValue);
    }
 
    /**
     * @deprecated needs model adapter to create update strategies to use, use new method with
     *             additional model adapter
     */

    public Binding bindSelection(ModelAdapter adapter, IViewer wrapperWithViewer, IObservableValue observableValue)
    {
        Validate
                .isTrue(
                        wrapperWithViewer instanceof IViewer,
                        "List Binding is only possible for controls having a viewer like RCPSimpleTable, RCPSimpleTree, RCPSimpleList, RCPSimpleCombo"); //$NON-NLS-1$
        Validate.notNull(wrapperWithViewer);
        ContentViewer viewer = wrapperWithViewer.getViewer();
        Validate.notNull(observableValue);
        // the update strategy notifies the model manager of changes of
        // bound view objects
        // so validation and dirty status can be refreshed
        UpdateValueStrategy targetToModelUpdateStrategy = createTargetToModelUpdateStrategy(adapter);
        UpdateValueStrategy modelToTargetUpdateStrategy = createModelToTargetUpdateStrategy(adapter);
        IObservableValue observedSelection = ViewersObservables.observeSingleSelection(viewer);
        Binding binding = getBindingContext().bindValue(observedSelection, observableValue,
                targetToModelUpdateStrategy, modelToTargetUpdateStrategy);
        associateBindingWithFormPart(binding, null, null);
        Validate.notNull(binding);
        return binding;
    }

    /**
     * @return observable value<IStatus> defining the validation state
     */
    public IObservableValue getValidationState()
    {
        return validationState;
    }

    protected UpdateSetStrategy createModelToTargetListUpdateStrategy(ModelAdapter adapter)
    {
        // TODO: use model adapter strategy

        return new UpdateSetStrategy();
    }

    protected UpdateSetStrategy createTargetToModelListUpdateStrategy(ModelAdapter adapter)
    {
        // TODO: use model adapter strategy
       return new UpdateSetStrategy()
        {
            @Override
            protected IStatus doAdd(IObservableSet observableSet, Object element)
            {
                // TODO LATER: handle set change for validation
                return super.doAdd(observableSet, element);
            }

            @Override
            protected IStatus doRemove(IObservableSet observableSet, Object element)
            {
                // TODO LATER: handle set change for validation
                return super.doRemove(observableSet, element);
            }
        };
    }

    /** compares two objects for equality, accepting null values too */
    private boolean nullEquals(Object o1, Object o2)
    {
        if (o1 == null)
        {
            return o2 == null;
        }
        else
        {
            return o1.equals(o2);
        }
    }

    private void debugBindingValue(RCPControl rcpcontrol, String modelTarget)
    {
        debugBinding(rcpcontrol, "Value; ", modelTarget); //$NON-NLS-1$
    }

    private void debugBindingDeco(RCPControl rcpcontrol, EControlState state, String modelTarget)
    {
        debugBinding(rcpcontrol, "State; " + state, modelTarget); //$NON-NLS-1$
    }

    private void debugBinding(RCPControl rcpcontrol, String text, String modelTarget)
    {
        if (Debug.binding())
        {
            if(rcpcontrol instanceof RCPCompound)
            {
                RCPControl mainControl = ((RCPCompound)rcpcontrol).getMainControl();
                if(mainControl != null)
                {
                    rcpcontrol = mainControl;
                }
            }
            
            rcpcontrol.setState(EControlState.OTHER, true);
            if (rcpcontrol.getDecoration(EControlState.OTHER) != null)
            {
                String desc = rcpcontrol.getDecoration(EControlState.OTHER).getDescriptionText();
                if (desc != null)
                    desc += ", "; //$NON-NLS-1$
                else
                    desc = ""; //$NON-NLS-1$
                rcpcontrol.getDecoration(EControlState.OTHER).setDescriptionText(
                        desc + "BindingTyp: " + text + "\nTarget: " + modelTarget); //$NON-NLS-1$ //$NON-NLS-2$
            }
            
        }
    }

    /**
     * dispose all bindings
     */
    public void dispose()
    {
        dbContext.dispose();
    }

    /** NO PUBLIC API, just for debugging binding management */
    public int getBindingCount(RCPFormPart formPart)
    {
        if (formPart != null)
        {
            return getInfo(formPart).bindings.size();
        }
        else
        {
            return dbContext.getBindings().size();
        }
    }

    /** NO PUBLIC API, just for debugging validator management */
    public int getValidatorCount(RCPFormPart formPart)
    {
        if (formPart != null)
        {
            return getInfo(formPart).validators.size();
        }
        else
        {
            int count = 0;
            for (FormPartInfo partInfo : viewPartToBindingMap.values())
            {
                count += partInfo.validators.size();
            }
            return count;
        }
    }

}
