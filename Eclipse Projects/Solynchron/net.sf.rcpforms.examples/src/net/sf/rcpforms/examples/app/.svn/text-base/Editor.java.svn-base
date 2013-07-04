
package net.sf.rcpforms.examples.app;

import net.sf.rcpforms.examples.complete.SandboxStackForm;
import net.sf.rcpforms.form.RCPFormEditorPart;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;

public class Editor extends RCPFormEditorPart<SandboxStackForm>
{
    public static final String ID = "net.sf.rcpforms.examples.app.editor";

    public Editor()
    {
        // create
        super(new SandboxStackForm());
    }

    @Override
    public void doSave(IProgressMonitor monitor)
    {
        // sample editors cannot be saved, only dirty state is managed
        this.setDirty(false);
    }

    @Override
    public void doSaveAs()
    {
        // not supported
        throw new IllegalStateException("Eclipse FW Error, isSaveAsAllowed() returns false !");
    }

    @Override
    public boolean isSaveAsAllowed()
    {
        return false;
    }

}
