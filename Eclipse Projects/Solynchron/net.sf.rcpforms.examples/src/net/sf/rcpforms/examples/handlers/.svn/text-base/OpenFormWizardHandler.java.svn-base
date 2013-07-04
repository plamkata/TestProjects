
package net.sf.rcpforms.examples.handlers;

import net.sf.rcpforms.examples.complete.AddressModel;
import net.sf.rcpforms.examples.complete.TestModel;
import net.sf.rcpforms.examples.wizards.ExampleFormWizard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * handler to open a wizard using form wizard pages
 */
public class OpenFormWizardHandler extends AbstractHandler
{
    /**
     * The constructor.
     */
    public OpenFormWizardHandler()
    {
    }

    /**
     * start a dialog wrapping the form
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        ExampleFormWizard newWizard = new ExampleFormWizard();
        newWizard.setInput(new Object[]{new TestModel(), new AddressModel()});
        WizardDialog dlg = new WizardDialog(window.getShell(), newWizard);
        int result = dlg.open();
        return result;
    }
}
