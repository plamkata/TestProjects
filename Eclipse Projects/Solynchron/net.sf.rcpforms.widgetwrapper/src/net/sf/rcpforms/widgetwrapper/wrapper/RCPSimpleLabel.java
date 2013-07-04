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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * creates a label. Since label/widget pairs are extremely common in user interfaces, for all
 * controls there is a "Simple" class and a normal class derived from {@link RCPLabeledControl}
 * which is composed of the simple widget and a label.
 * <p>
 * 
 * @author Marco van Meegen
 */
public class RCPSimpleLabel extends RCPControl
{
    public RCPSimpleLabel(String label)
    {
        this(label, SWT.DEFAULT);
    }

    public RCPSimpleLabel(String label, int style)
    {
        super(label, style);
    }

    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Label result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = formToolkit.createLabel(getSWTParent(), getLabel());
        }
        else
        {
            result = formToolkit.createLabel(getSWTParent(), getLabel(), getStyle());
        }

        return result;
    }

    public final Label getSWTLabel()
    {
        return getTypedWidget();
    }

    @Override
    public void createUI(FormToolkit formToolkit)
    {
        super.createUI(formToolkit);
        getSWTLabel().setText(getLabel());
    }
}
