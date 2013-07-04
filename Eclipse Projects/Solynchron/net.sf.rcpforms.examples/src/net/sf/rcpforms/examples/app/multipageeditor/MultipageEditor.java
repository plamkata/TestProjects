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

import net.sf.rcpforms.examples.complete.Sandbox2FormPart;
import net.sf.rcpforms.examples.complete.Sandbox3FormPart;
import net.sf.rcpforms.examples.complete.SandboxTablePart;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

public class MultipageEditor extends FormEditor
{

    public static final String ID = "net.sf.rcpforms.examples.multipageeditor";

    protected void addPages()
    {
        try
        {
            MultipageCompositeEditorInput input = (MultipageCompositeEditorInput) this
                    .getEditorInput();
            SandboxAddressForm addressForm = new SandboxAddressForm("Address",
                    new Sandbox2FormPart(), new Sandbox3FormPart());
            int index = addPage(new AddressFormEditorPart(addressForm), input
                    .getAddressEditorInput());
            setPageText(index, "AddressPart");
            SandboxTableForm tableForm = new SandboxTableForm("Table", new SandboxTablePart());
            index = addPage(new TableFormEditorPart(tableForm), input.getTableEditorInput());
            setPageText(index, "TablePart");
        }
        catch (PartInitException e)
        {
        }
    }

    @Override
    public void doSave(IProgressMonitor monitor)
    {
        if (pages != null)
        {
            for (Object page : pages)
            {
                if (page instanceof IEditorPart)
                {
                    ((IEditorPart) page).doSave(monitor);
                }
            }
        }
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

    public boolean isDirty()
    {
        if (pages != null)
        {
            for (Object page : pages)
            {
                if (page instanceof IEditorPart)
                {
                    IEditorPart ePage = (IEditorPart) page;
                    if (ePage.isDirty())
                        return true;
                }
            }
        }
        return false;
    }

}