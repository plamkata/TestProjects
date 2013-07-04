/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 ******************************************************************************
 */

package net.sf.rcpforms.widgetwrapper.wrapper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * wraps a Group control
 * 
 * @author Marco van Meegen
 */
public class RCPGroup extends RCPComposite
{

    public RCPGroup(String label)
    {
        this(label, SWT.FLAT);
    }

    public RCPGroup(String label, int style)
    {
        super(label, style);
    }

    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Group result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createGroup(getSWTParent(), getLabel());
        }
        else
        {
            result = getFormToolkitEx().createGroup(getSWTParent(), getLabel(), getStyle());
        }
        return result;
    }

    public Group getSwtGroup()
    {
        return getTypedWidget();
    }

    @Override
    public void createUI(FormToolkit formToolkit)
    {
        super.createUI(formToolkit);
        getSwtGroup().setText(getLabel());
    }
}
