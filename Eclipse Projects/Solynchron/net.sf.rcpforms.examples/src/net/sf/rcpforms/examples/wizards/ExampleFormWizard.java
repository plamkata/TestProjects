
package net.sf.rcpforms.examples.wizards;

import net.sf.rcpforms.examples.complete.AddressModel;
import net.sf.rcpforms.examples.complete.Sandbox2FormPart;
import net.sf.rcpforms.examples.complete.Sandbox3FormPart;
import net.sf.rcpforms.examples.complete.SandboxStackForm;
import net.sf.rcpforms.examples.complete.TestModel;
import net.sf.rcpforms.form.RCPForm;
import net.sf.rcpforms.form.RCPFormWizardPage;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * shows how to embed RCPForms as wizard pages into wizards
 * 
 * @author Marco van Meegen
 */

public class ExampleFormWizard extends Wizard implements INewWizard
{
    private RCPForm form1;

    private RCPForm form2;

    public ExampleFormWizard()
    {
        super();
        // create generic forms using parts
        form1 = new RCPForm("Sandbox2Part Title", new Sandbox2FormPart());
        final Sandbox3FormPart part = new Sandbox3FormPart();
        form2 = new RCPForm("Sandbox3Part Title", part)
        {
            // add a date range validator to this part
            @Override
            public void initializeUI()
            {
                getValidationManager()
                        .addValidator(part, new SandboxStackForm.DateRangeValidator());
            }
        }

        ;
    }

    /**
     * Adding the page to the wizard.
     */

    public void addPages()
    {
        // wrap forms into a page and add
        addPage(new RCPFormWizardPage("PAGE1", form1));
        addPage(new RCPFormWizardPage("PAGE2", form2));
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. We will create an
     * operation and run it using wizard as execution context.
     */
    public boolean performFinish()
    {
        return true;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        // usually you would want to set the input from outside the wizard,
        // define your setInput method as below, the models passed in will contain the changed
        // models on wizard exit
        setInput(new Object[]{new TestModel(), new AddressModel()});
    }

    /**
     * set input consisting of an array of {TestModel, AddressModel}
     */
    public void setInput(Object input)
    {
        Object[] inputArray = (Object[]) input;
        form1.setInput(new Object[]{inputArray[0]});
        form2.setInput(new Object[]{inputArray[1]});
    }

}