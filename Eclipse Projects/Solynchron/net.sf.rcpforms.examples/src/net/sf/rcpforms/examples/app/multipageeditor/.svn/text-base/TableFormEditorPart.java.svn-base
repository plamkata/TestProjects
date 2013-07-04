/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Remo Loetscher - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.examples.app.multipageeditor;

import net.sf.rcpforms.form.RCPFormEditorPart;

import org.eclipse.core.runtime.IProgressMonitor;

public class TableFormEditorPart extends RCPFormEditorPart<SandboxTableForm>
{

    public TableFormEditorPart(SandboxTableForm form)
    {
        super(form);
    }

    @Override
    public void doSave(IProgressMonitor monitor)
    {
        setDirty(false);
    }

    @Override
    public void doSaveAs()
    {
    }

    @Override
    public boolean isSaveAsAllowed()
    {
        return false;
    }

}
