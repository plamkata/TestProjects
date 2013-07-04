
package net.sf.rcpforms.examples.handlers;

import net.sf.rcpforms.examples.app.Editor;
import net.sf.rcpforms.examples.complete.SandboxStackForm;
import net.sf.rcpforms.form.IRCPFormEditorInput;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.part.NullEditorInput;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */

public class OpenSampleEditorHandler extends AbstractHandler
{

    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        try
        {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(
                    new DummyEditorInput(), Editor.ID);
        }
        catch (PartInitException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}



class DummyEditorInput implements IRCPFormEditorInput
{
    private Object[] models = SandboxStackForm.createModels();

    public boolean exists()
    {
        return false;
    }

    public ImageDescriptor getImageDescriptor()
    {
        return null;
    }

    public String getName()
    {
        return "DummyInput";
    }

    public IPersistableElement getPersistable()
    {
        return null;
    }

    public String getToolTipText()
    {
        return "DummyTooltipText";
    }

    public Object getAdapter(Class adapter)
    {
        return null;
    }

    public Object[] getModels()
    {
        return models;
    }

}
