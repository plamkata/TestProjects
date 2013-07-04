
package net.sf.rcpforms.examples.handlers;

import net.sf.rcpforms.examples.app.multipageeditor.MultipageCompositeEditorInput;
import net.sf.rcpforms.examples.app.multipageeditor.MultipageEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class OpenMultipageEditorHandler extends AbstractHandler
{

    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        try
        {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(
                    new MultipageCompositeEditorInput(), MultipageEditor.ID);
        }
        catch (PartInitException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
