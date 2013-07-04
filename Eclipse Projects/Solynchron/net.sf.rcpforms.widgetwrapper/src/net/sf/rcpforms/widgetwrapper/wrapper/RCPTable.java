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

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;

/**
 * a compound of a label and a {@link RCPSimpleTable}.
 * 
 * @author Marco van Meegen
 */
public class RCPTable extends RCPLabeledControl<RCPSimpleTable> implements IViewer
{
    public RCPTable(String label)
    {
        this(label, SWT.DEFAULT);
    }

    public RCPTable(String label, int style)
    {
        this(label, new RCPSimpleTable(style));
    }

    protected RCPTable(String label, RCPSimpleTable table)
    {
        super(label, table);

    }

    public final Table getSWTTable()
    {
        return getRCPControl().getSWTTable();
    }

    public ContentViewer getViewer()
    {
        return getRCPControl().getViewer();
    }
}
