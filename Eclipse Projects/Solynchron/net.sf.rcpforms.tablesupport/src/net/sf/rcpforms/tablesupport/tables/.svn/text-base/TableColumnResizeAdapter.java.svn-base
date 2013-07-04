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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Adapter which automatically resizes the column widths of a table when the table is resized. This
 * was created according to Snippet77 because TableLayout does not work on resize of a table.
 * <p>
 * Additionally it respects if the table gets smaller or bigger to reduce flickering of scrollbars.
 * 
 * @author Marco van Meegen
 */
public class TableColumnResizeAdapter extends ControlAdapter
{
    private Table table;

    private Point lastSize = null;

    private double accumulatedWeight;

    private int[] columnWeights;

    /**
     * creates a new resize adapter for the given table. It watches for size changes of the table
     * and resizes the columns according to their weights. The weight defines the relative size of
     * the column, i.e. col.size = tablewidth * col.weight/sum(all col.weights)
     * 
     * @param table table to resize columns for
     * @param columnWeights weights for columns. Size must match existing columns in table, all
     *            weights must be >0. Thus first create and add columns, then resize adapter.
     */
    public TableColumnResizeAdapter(final Table table, final int[] columnWeights)
    {
        super();
        this.table = table;
        this.columnWeights = columnWeights;
        if (columnWeights.length != table.getColumnCount())
        {
            throw new IllegalArgumentException(
                    "columnWeights size must match column count in table"); //$NON-NLS-1$
        }
        if (columnWeights.length != 0)
        {
            accumulatedWeight = 0;
            for (int i = 0; i < columnWeights.length; i++)
            {
                if (columnWeights[i] <= 0)
                {
                    throw new IllegalArgumentException("Columnweight must be > 0:" //$NON-NLS-1$
                            + columnWeights[i]);
                }
                accumulatedWeight += columnWeights[i];
            }
        }
        else
        {
            throw new IllegalArgumentException("columnWeights size must be > 0"); //$NON-NLS-1$
        }
        // attach to table
        table.addControlListener(this);
    }

    /**
     * resizes the columns when the table is resized.
     */
    public void controlResized(final ControlEvent e)
    {
        // this crashes jvm with Eclipse 3.2 on maximize/unmaximize window
        // when invoked synchronous
        // see MWS-1109 for the problem

        Display.getCurrent().asyncExec(new Runnable()
        {
            public void run()
            {
                doResize();
            }
        });
    }

    private void doResize()
    {
        // Rectangle area = comp.getClientArea();
        Rectangle area = table.getBounds();
        TableColumn[] columns = table.getColumns();
        Point preferredSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        int width = area.width - 2 * table.getBorderWidth();
        if (preferredSize.y > area.height + table.getHeaderHeight())
        {
            // Subtract the scrollbar width from the total column width
            // if a vertical scrollbar will be required
            Point vBarSize = table.getVerticalBar().getSize();
            width -= vBarSize.x;
        }
        Point oldSize = lastSize == null ? table.getSize() : lastSize;
        if (oldSize.x > area.width)
        {
            // table is getting smaller so make the columns
            // smaller first and then resize the table to
            // match the client area width
            resizeColumns(width, columns);
            table.setSize(area.width, area.height);
        }
        else
        {
            // table is getting bigger so make the table
            // bigger first and then make the columns wider
            // to match the client area width
            table.setSize(area.width, area.height);
            resizeColumns(width, columns);
        }
        lastSize = table.getSize();
    }

    /**
     * resizes the given columns using their respective weight to fill the new width.
     * 
     * @param width new sum of column widths
     * @param columns columns to resize according to their weights
     */
    private void resizeColumns(final int width, final TableColumn[] columns)
    {
        int currentAccumulatedWidth = 0;
        // resize all but last columns
        for (int i = 0; i < columns.length - 1; i++)
        {
            int newWidth = (int) (width * columnWeights[i] / accumulatedWeight);
            if (newWidth < 0)
            {
                throw new IllegalArgumentException("setting negative width " + newWidth //$NON-NLS-1$
                        + " to column " + i); //$NON-NLS-1$
            }
            columns[i].setWidth(newWidth);
            currentAccumulatedWidth += newWidth;
        }
        // to avoid rounding problems set last column to remaining pixels
        if (columns.length > 0)
        {
            int newWidth = width - currentAccumulatedWidth;
            if (newWidth < 0)
            {
                throw new IllegalArgumentException("setting negative width " + newWidth //$NON-NLS-1$
                        + " to column " + (columns.length - 1)); //$NON-NLS-1$
            }
            columns[columns.length - 1].setWidth(width - currentAccumulatedWidth);
        }
    }
}
