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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * this class enables support for automatic scaling all the table columns on resize events set this
 * class as the controlListener of a table: Table.addControlListener(...)
 * 
 * @author loetscherr
 * @since iteration 4
 */
class TableResizeListener extends ControlAdapter
{
    private Table table;

    public TableResizeListener(Table table)
    {
        this.table = table;
    }

    public void controlResized(ControlEvent e)
    {

        Rectangle area = table.getParent().getClientArea();
        Point preferredSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        int width = area.width - 2 * table.getBorderWidth();
        if (preferredSize.y > area.height + table.getHeaderHeight())
        {
            // Subtract the scrollbar width from the total column width
            // if a vertical scrollbar will be required
            Point vBarSize = table.getVerticalBar().getSize();
            width -= vBarSize.x;
        }

        int totalWidth = 0;
        for (TableColumn tc : table.getColumns())
        {

            totalWidth += tc.getWidth();
        }

        Point oldSize = table.getSize();
        if (oldSize.x > area.width)
        {
            // table is getting smaller so make the columns
            // smaller first and then resize the table to
            // match the client area width
            this.calculateNewColumnSize(width, totalWidth);
            table.setSize(area.width, area.height);
        }
        else if (oldSize.x < area.width)
        {
            // table is getting bigger so make the table
            // bigger first and then make the columns wider
            // to match the client area width
            table.setSize(area.width, area.height);
            this.calculateNewColumnSize(width, totalWidth);
        }
        // else if (oldSize.x == area.width) //do nothing

    }

    /**
     * Calculates new TableColumn size and use excess table space. Takes care of the existing column
     * size ratio
     * 
     * @param width New total available width for all the table columns
     * @param totalColWidth Current width of all table columns
     */
    private void calculateNewColumnSize(int width, int totalColWidth)
    {
        // positive value if new size is greater, otherwise negative
        int excessSpace = width - totalColWidth;
        double delta = 0.0;

        for (TableColumn tc : table.getColumns())
        {
            // calculate additional column length
            double relativeColLengts = (double) tc.getWidth() / totalColWidth;
            double lengthDiff = relativeColLengts * excessSpace;
            // Math.floor guarantees that new total table column length is smaller than available
            // table space -> no horizontal scrollbar!
            int columnSizeDelta = (int) Math.floor(lengthDiff);
            // take care of rounding errors
            delta += lengthDiff - columnSizeDelta;

            if (delta >= 1.0)
            {
                // add integers to length diff
                columnSizeDelta += (int) delta;
                // store new floating point value < 1 in delta
                delta = delta - ((int) delta);
            }
            tc.setWidth(tc.getWidth() + columnSizeDelta);
        }
    }
}