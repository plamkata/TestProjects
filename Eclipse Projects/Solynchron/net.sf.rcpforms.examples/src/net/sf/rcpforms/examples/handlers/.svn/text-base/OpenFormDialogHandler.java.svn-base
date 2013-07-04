
package net.sf.rcpforms.examples.handlers;

import net.sf.rcpforms.examples.complete.Sandbox2FormPart;
import net.sf.rcpforms.examples.complete.TestModel;
import net.sf.rcpforms.form.RCPForm;
import net.sf.rcpforms.form.RCPFormDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class OpenFormDialogHandler extends AbstractHandler
{
    /**
     * The constructor.
     */
    public OpenFormDialogHandler()
    {
    }

    /**
     * start a dialog wrapping the form
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        RCPForm form = new RCPForm("Form Dialog", new Sandbox2FormPart());
        RCPFormDialog<RCPForm> dlg = new RCPFormDialog<RCPForm>(window.getShell(), form, true);
        dlg.setInput(new Object[]{new TestModel()});
        int result = dlg.open();
        return result;
    }
}
