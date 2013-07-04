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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * wraps a checkbox table, which is a table where each line is prefixed by a checkbox.
 * 
 * @author Marco van Meegen
 */
public class RCPSimpleCheckboxTable extends RCPSimpleTable
{
    public RCPSimpleCheckboxTable()
    {
        super(SWT.DEFAULT);
    }

    public RCPSimpleCheckboxTable(int style)
    {
        super(style == SWT.DEFAULT ? SWT.DEFAULT : style | SWT.CHECK);
    }

    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Table result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createCheckboxTable(getSWTParent());
        }
        else
        {
            result = getFormToolkitEx().createCheckboxTable(getSWTParent(), getStyle());
        }
        createViewer(result);
        return result;
    }

}
