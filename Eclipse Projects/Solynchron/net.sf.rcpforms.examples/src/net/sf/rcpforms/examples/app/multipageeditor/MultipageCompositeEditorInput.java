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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class MultipageCompositeEditorInput implements IEditorInput
{

    public AddressEditorInput getAddressEditorInput()
    {
        return new AddressEditorInput();
    }

    public TableEditorInput getTableEditorInput()
    {
        return new TableEditorInput();
    }

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
        return "MultipageSampleEditorInput";
    }

    public IPersistableElement getPersistable()
    {
        return null;
    }

    public String getToolTipText()
    {
        return "MultipageSampleEditorInput";
    }

    public Object getAdapter(Class adapter)
    {
        return null;
    }
}
