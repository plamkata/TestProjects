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

package net.sf.rcpforms.tablesupport.tables;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * Table is overwritten to make automatic layout of columns possible. computeSize() calculates the
 * actual sum of columnsizes, which is wrong. It should deliver the preferred minimum column size
 * sum.
 * 
 * @author vanmeegenm
 */
public class SizeFixedTable extends Table
{
    private int accumulatedColumnMinWidth = -1;

    public SizeFixedTable(Composite parent, int style)
    {
        super(parent, style);
    }

    @Override
    protected void checkSubclass()
    {
        // yes I agree and am responsible
    }

    @Override
    public Point computeSize(int wHint, int hHint, boolean changed)
    {
        Point point = super.computeSize(wHint, hHint, changed);
        if (accumulatedColumnMinWidth != -1 && wHint == -1)
        {
            point.x = accumulatedColumnMinWidth; // TODO: + trim
        }
        return point;
    }

    public int getAccumulatedColumnMinWidth()
    {
        return accumulatedColumnMinWidth;
    }

    public void setAccumulatedColumnMinWidth(int accumulatedColumnMinWidth)
    {
        this.accumulatedColumnMinWidth = accumulatedColumnMinWidth;
    }

}
