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

package net.sf.rcpforms.examples.complete;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.form.RCPForm;
import net.sf.rcpforms.modeladapter.converter.AbstractModelValidator;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import com.damnhandy.aspects.bean.JavaBean;

/**
 * This is the main class of the example, demonstrating an overview of all features available in
 * RCPForms. It is a good start to get an idea how to do things using RCPForms.
 * <p>
 * 
 * @author vanmeegenm
 * @author Remo Loetscher
 */
public class SandboxStackForm extends RCPForm
{

    /**
     * Class DateRangeValidator is an example for a validator working on more than one field
     * 
     * @author Marco van Meegen
     */
    public static final class DateRangeValidator extends AbstractModelValidator
    {
        public Object[] getProperties()
        {
            return new String[]{AddressModel.P_ValidFrom, AddressModel.P_ValidTo};
        }

        public IStatus validate(Object value)
        {
            AddressModel model = (AddressModel) value;
            IStatus result = ok();
            if (model.getValidFrom() != null && model.getValidTo() != null
                    && model.getValidFrom().after(model.getValidTo()))
            {
                result = error("From Date must be earlier than To Date");
            }
            return result;
        }
    }

    /**
     * Constructor for SandboxStackForm
     */
    public SandboxStackForm()
    {
        // create form with the given title and form parts
        this(null);
    }
    
    public SandboxStackForm(ValidationManager vm)
    {
        super("RCPForm Sandbox Example", vm, new Sandbox2FormPart(), new Sandbox3FormPart(),
                new SandboxTablePart(), new SandboxRangeSampleFormPart(),
                new SandboxIconTextFormPart(), new SandboxLayoutFormPart(), new SandboxMasterDetailFormPart(), new SandboxTableMasterDetailFormPart());

    }

    public void initializeUI()
    {
        // in initializeUI the form is created,
        // input-independent listeners, validators and stuff should be initialized here
        getValidationManager().addValidator(getSandbox3Part(), new DateRangeValidator());

        // initializations which span multiple parts must be done here
        getSandbox3Part().getEnableButton().getSWTButton().addSelectionListener(
                new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent e)
                    {
                        getSandbox2Part().setState(EControlState.ENABLED,
                                !((Button) e.widget).getSelection());
                        getSandboxTablePart().setState(EControlState.ENABLED,
                                !((Button) e.widget).getSelection());
                        getSandboxRangeSampleFormPart().setState(EControlState.ENABLED,
                                !((Button) e.widget).getSelection());
                        getValidationManager().revalidate();
                    }
                });

        // initializations which span multiple parts must be done here
        getSandbox3Part().getVisibleButton().getSWTButton().addSelectionListener(
                new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent e)
                    {
                        getSandbox2Part().setState(EControlState.VISIBLE,
                                !((Button) e.widget).getSelection());
                        getSandboxTablePart().setState(EControlState.VISIBLE,
                                !((Button) e.widget).getSelection());
                        getSandboxRangeSampleFormPart().setState(EControlState.VISIBLE,
                                !((Button) e.widget).getSelection());
                        getValidationManager().revalidate();
                    }
                });
    }

    public void setFocus()
    {
        getSandbox2Part().setFocus();
    }

    private Sandbox2FormPart getSandbox2Part()
    {
        return ((Sandbox2FormPart) getPart(0));
    }

    private Sandbox3FormPart getSandbox3Part()
    {
        return ((Sandbox3FormPart) getPart(1));
    }

    private SandboxTablePart getSandboxTablePart()
    {
        return ((SandboxTablePart) getPart(2));
    }

    private SandboxRangeSampleFormPart getSandboxRangeSampleFormPart()
    {
        return ((SandboxRangeSampleFormPart) getPart(3));
    }

    private SandboxIconTextFormPart getSandboxIconTextFormPart()
    {
        return ((SandboxIconTextFormPart) getPart(4));
    }

    /**
     * creates the models needed for the form and attaches listeners which echo changes to stdout
     * 
     * @return created models
     */
    public static Object[] createModels()
    {
        // and models
        final TableModel model1 = new TableModel();
        final TestModel model2 = new TestModel();
        final AddressModel model3 = new AddressModel();
        final IntegerRangeTestModel model4 = new IntegerRangeTestModel();
        final Object emptyModel = new SimpleBean();
        final SimpleBean simpleModel = new SimpleBean();
        final AddressModel masterDetailModel = new AddressModel();
        masterDetailModel.setAddress(new NestedAddressModel());
        masterDetailModel.getAddress().setCity("SampleCity");
        masterDetailModel.getAddress().setZipCode(1234);
        final TableModel masterDetailSelection = new TableModel();
        

        // add some listeners to check how databinding works
        ((JavaBean) model2).addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("Model2 changed: " + model2);
            }
        });
        model3.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("Model3 changed: " + model3);
            }
        });

        model3.getAddress().addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("NestedAddressModel in Model3 changed: " + model3);
            }
        });

        JavaBean country = (JavaBean) model3.getAddress().getCountry();
        country.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("NestedCountryModel in NestedAddressModel in Model3 changed: "
                        + model3);
            }
        });

        model1.getList().addChangeListener(new IChangeListener()
        {
            public void handleChange(ChangeEvent event)
            {
                System.out.println("Model1 list changed:" + model1);

            }
        });
        model1.getSelectedList().addChangeListener(new IChangeListener()
        {
            public void handleChange(ChangeEvent event)
            {
                System.out.println("Model1 checked list changed:" + model1);

            }
        });

        // set models as input
        // either create ui first and then set input, or if we use startTestShell(),
        // we must first set the input
        Object[] models = new Object[]{model2, model3, model1, model4, emptyModel, simpleModel, masterDetailModel, masterDetailSelection};
        return models;
    }

    /**
     * start the form as SWT application
     * 
     * @param args ignored
     */
    public static void main(String[] args)
    {
        // create form
        Object[] models = createModels();
        // create the form, no ui is created yet
        final SandboxStackForm rcpForm = new SandboxStackForm();
        // set input, since form is not created the input is not bound yet, but saved for createUI()
        rcpForm.setInput(models);

        // convenience method, creates a shell and creates the form ui in the shell
        // since an input has been set before, the form is bound to the model and ready to go
        rcpForm.startTestShell();
    }

}
