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

import net.sf.rcpforms.examples.complete.AddressModel;
import net.sf.rcpforms.examples.complete.TestModel;
import net.sf.rcpforms.form.IRCPFormEditorInput;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

public class AddressEditorInput implements IRCPFormEditorInput
{
    Object[] models = new Object[]{new TestModel(), new AddressModel()};

    public Object[] getModels()
    {
        return models;
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
        return "AddressInput";
    }

    public IPersistableElement getPersistable()
    {
        return null;
    }

    public String getToolTipText()
    {
        return "AddressInput";
    }

    public Object getAdapter(Class adapter)
    {
        return null;
    }

}
