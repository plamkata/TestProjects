/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.form;

import java.util.logging.Logger;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormFactory;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.builder.ColumnLayoutFactory;
import net.sf.rcpforms.widgetwrapper.util.WidgetUtil;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.internal.forms.MessageManager;
import org.eclipse.ui.internal.forms.widgets.FormHeading;

/**
 * /** The main form concept of rcpforms. An RCPForm is composed of a Header which displays error
 * markers and heading of the whole form, as well as one or more RCPFormPart
 * <p>
 * A most simple form contains one RCPFormPart as a view and an arbitrary object which represents
 * the presentation model.
 * <p>
 * TODO LATER: explain concept more clearly with integration into existing Eclipse concepts like
 * IManagedForm, FormDialog, ViewPart, Wizard, MultipageEditor<BR>
 * TODO LATER: implement the form and integration layer
 * <p>
 * EXPERIMENTAL API, WILL CHANGE AND MOVE TO FORM LAYER IN THE FUTURE !
 * 
 * @author Marco van Meegen
 */
public class RCPForm
{

    private static final Logger LOG = Logger.getLogger(RCPForm.class.getName());

    /**
     * validation manager used by this form, spans all form part to enable part-spanning validations
     */
    private ValidationManager validationManager = null;

    /**
     * managed form which contains and manages the ScrolledForm swt control
     */
    private ManagedForm managedForm;

    /**
     * form parts passed in constructor
     */
    private RCPFormPart[] formParts;

    /**
     * title passed in constructor
     */
    private String title;

    /**
     * @return Returns the title.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * form tempInput, intermediately saved to allow flexible call order of createUI and setInput()
     */
    private Object tempInput = null;

    /**
     * Constructor for a RCPForm with the given formParts using a custom validation manager
     * subclass, no ui is created and no binding done yet
     * 
     * @param title title, maybe null if no title area is needed
     * @param validationManager validation manager to use, maybe null
     * @param formParts formParts to create, they will be created in the order as lined out
     */
    public RCPForm(String title, ValidationManager validationManager, RCPFormPart... parts)
    {
        Validate.noNullElements(parts);
        this.title = title;
        this.formParts = parts;
        this.validationManager = validationManager;
    }

    /**
     * Constructor for a RCPForm with the given formParts, no ui is created and no binding done yet
     * 
     * @param title title, maybe null if no title area is needed
     * @param formParts formParts to create, they will be created in the order as lined out
     */
    public RCPForm(String title, RCPFormPart... parts)
    {
        this(title, null, parts);
    }

    /**
     * creates and fully initializes the form.
     * <p>
     * This implies:
     * <ul>
     * <li>create a Validation Manager shared for all bindings in the form parts
     * <li>create the managed form and its {@link ScrolledForm} widget including
     * {@link MessageManager}, {@link FormHeading} to display errors
     * <li>
     * 
     * @param parent parent to create form in
     */
    public void createUI(Composite parent)
    {
        Validate
                .notNull(
                        Realm.getDefault(),
                        "Make sure a Databinding Realm is set; you should wrap your main method into Realm.runWithDefault() to provide one"); //$NON-NLS-1$
        Validate.notNull(parent);
        WidgetUtil.ensureValid(parent);
        try
        {
            // create the validation manager to use
            if (validationManager == null)
            {
                // if no manager was passed in constructor, create default one
                validationManager = RCPFormFactory.getInstance().createBindingAndValidationManager(
                        title);
            }

            // create the scrolled form swt control and a managed form
            // maintaining it
            // each form part is created too
            FormToolkit toolkit = RCPFormFactory.getInstance().getToolkit(Display.getCurrent());
            managedForm = createManagedForm(parent, title, toolkit);

            // disable validation -> prevent flickering during ui and binding
            // creation
            validationManager.enableValidation(false);
            // init form body layout; default is column layout
            initBodyLayout();
            for (int i = 0; i < formParts.length; i++)
            {
                formParts[i].createUI(toolkit, managedForm.getForm().getBody());
            }

            // if tempInput was already set, use it and bind
            if (tempInput != null)
            {
                setInput(tempInput);
            }
            initializeUI();

        }
        catch (RuntimeException ex)
        {
            LOG.severe("Exception in createUI: " + ex.getLocalizedMessage()); //$NON-NLS-1$
            ex.printStackTrace();
            throw ex;

        }
        finally
        {
            // enable validation
            validationManager.enableValidation(true);

            // relayout
            reflow(true);
        }
    }

    /**
     * @return the ScrolledForm widget representing the form
     */
    public ScrolledForm getScrolledForm()
    {
        Validate.notNull(managedForm, "getScrolledForm() must be called after createUI()"); //$NON-NLS-1$
        return managedForm.getForm();
    }

    /**
     * Constructor for RCPForm using a separate toolkit and form
     * 
     * @param toolkit toolkit to use
     * @param form form to use
     */
    public RCPForm(FormToolkit toolkit, ScrolledForm form)
    {
        managedForm = new ManagedForm(toolkit, form);
        validationManager = RCPFormFactory.getInstance().createBindingAndValidationManager(
                "BindingManager:" + this.getClass().getName()); //$NON-NLS-1$
    }

    /**
     * @return the binding and validation manager used by this form. This is needed to check the
     *         form validation state, add new validators or create bindings directly instead through
     *         form formParts.
     */
    public ValidationManager getValidationManager()
    {
        return validationManager;
    }

    /**
     * creates the managed form including the scrolled form and form header
     * 
     * @param parent parent to create form in
     * @param title title
     * @param toolkit toolkit to use
     * @return the managed form
     */
    protected ManagedForm createManagedForm(Composite parent, String title, FormToolkit toolkit)
    {
        ManagedForm managedForm = null;
        ScrolledForm form = new ScrolledForm(parent, SWT.V_SCROLL | SWT.H_SCROLL
                | Window.getDefaultOrientation())
        {
            // HACK to avoid loading of images which needs osgi
            // runtime in Eclipse 3.1, so this can be used with swt too.
            // in 3.4 this bug was fixed
            @Override
            public void setMessage(String newMessage, int newType, IMessage[] messages)
            {
                try
                {
                    super.setMessage(newMessage, newType, messages);
                }
                catch (RuntimeException ex)
                {
                    // if this does not work, set message without image
                    super.setMessage(newMessage, 0, messages);
                }
                catch (NoClassDefFoundError ex)
                {
                    // using 3.3 libraries "standalone" a NoClassDefFoundError
                    // could be thrown
                    super.setMessage(newMessage, 0, messages);
                }
                catch (ExceptionInInitializerError eiie)
                {
                    // using 3.3 libraries "standalone" an
                    // ExceptionInInitializeError could be thrown
                    super.setMessage(newMessage, 0, messages);
                }
            }
        };

        managedForm = new ManagedForm(toolkit, form);
        toolkit.decorateFormHeading(form.getForm());
        form.setExpandHorizontal(true);
        form.setExpandVertical(true);
        form.setBackground(toolkit.getColors().getBackground());
        form.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
        form.setFont(JFaceResources.getHeaderFont());
        if (title != null)
        {
            form.setText(title);
        }
        return managedForm;
    }

    /**
     * sets the layout for the form body. Default implementation sets a ColumnLayout.
     */
    protected void initBodyLayout()
    {
        // Sets the Layout to the form body which is also the parent to the
        // sections

        managedForm.getForm().getBody().setLayout(ColumnLayoutFactory.createFormLayout());
    }

    /**
     * sets the given data model as datamodel for the form and binds all view models against it.
     * <p>
     * @param input an array of data models; -> see tracker 152079
     * LATER: extend to hierarchical models ? 
     */
    public void setInput(Object input)
    {
        try
        {
            Validate.isTrue(input == null || input.getClass().isArray(),
                    "Your Input must be null or an array with the same length as form formParts"); //$NON-NLS-1$
            Object[] inputArray = (Object[]) input;
            if (input != null)
            {
                Validate.isTrue(inputArray.length == formParts.length,
                        "Input array must have same length as form part array"); //$NON-NLS-1$
                Validate.noNullElements(inputArray, "No null Input elements allowed"); //$NON-NLS-1$
            }
            if (managedForm != null)
            {
                managedForm.setInput(input);
                bind(inputArray);
            }
            else
            {
                // save until createUI was called
                this.tempInput = input;
            }
        }
        catch (RuntimeException ex)
        {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * initializeUI is called after ui has been created, this happens only once in the form life
     * cycle.
     * <p>
     * Listeners to widgets, binding to form-internal extra data, part spanning validators should be
     * registered here.
     * <p>
     * For input-dependent data use {@link #initialize()}.
     */
    public void initializeUI()
    {

    }

    /**
     * initialize is called after an input has been set and bound. input dependent
     * information/listeners should be initialized here
     */
    public void initialize()
    {

    }

    /**
     * check if no data binding error exist and all validation rules are ok. Error markers and the
     * observable validation status are updated as side effect.
     * 
     * @return status of the validation; status.isOk() is true if the form does not contain any
     *         validation errors, otherwise a status or multistatus containing the errors is
     *         returned.
     */
    public IStatus validateForm()
    {
        return validationManager.revalidate();
    }

    /**
     * returns the validation state of the view model managed by this. The IObservableValue type is
     * IStatus and is automatically updated if the validation state changes.
     * 
     * @return IObservableValue of type IStatus
     */
    public IObservableValue getObservableValidationStatus()
    {
        return validationManager.getValidationState();
    }

    /**
     * override in subclass to define the control which gets the focus when entering the form
     */
    public void setFocus()
    {
        // do nothing here
    }

    /**
     * binds the form parts agains the models. calls {@link #initialize()} after everything is bound
     * 
     * @param models models to bind against
     */
    protected void bind(Object[] models)
    {
        Validate.noNullElements(models, "Models must not be null"); //$NON-NLS-1$
        Validate.isTrue(formParts.length == models.length);
        ValidationManager vm = validationManager;
        vm.initialize(managedForm.getMessageManager());
        for (int i = 0; i < formParts.length; i++)
        {
            vm.bindPart(formParts[i], models[i]);
        }
        initialize();
    }

    /**
     * @see org.eclipse.ui.forms.IManagedForm#reflow(boolean)
     */
    public void reflow(boolean changed)
    {
        managedForm.reflow(changed);
    }

    /**
     * retrieve the form part with the given index
     * 
     * @param index 0-based part index
     * @return part
     */
    public RCPFormPart getPart(int index)
    {
        Validate.isTrue(index >= 0 && index < formParts.length, "Invalid part index: " + index //$NON-NLS-1$
                + ", only " + formParts.length + " parts registered"); //$NON-NLS-1$ //$NON-NLS-2$
        return formParts[index];
    }

    /**
     * convenience test method to start a shell containing this form, used for easy testing
     */
    public void startTestShell()
    {

        final Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());

        Realm.runWithDefault(SWTObservables.getRealm(Display.getDefault()), new Runnable()
        {
            public void run()
            {
                createUI(shell);
            }
        });
        shell.open();
        shell.pack(true);
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();
    }

    /**
     * @return currently set input on this form
     */
    public Object getInput()
    {
        Object result;
        if (managedForm != null)
        {
            result = managedForm.getInput();
        }
        else
        {
            result = tempInput;
        }
        return result;
    }
}
