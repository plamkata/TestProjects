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

package net.sf.rcpforms.tablesupport.tables;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class CustomTooltipListener
{
    /*package*/Listener labelListener = null;

    /*package*/Listener tableListener = null;

    private Label label = null;

    private Table table = null;

    private Hashtable<Integer, Listener> listenerList = new Hashtable<Integer, Listener>();

    private static final Logger LOG = Logger.getLogger(CustomTooltipListener.class.getName());

    private Hashtable<Integer, String> columnTooltip = new Hashtable<Integer, String>();

    /**
     * Register listener to table to create custom tooltips
     * 
     * @param table
     */
    void registerListener(final Table table)
    {
        this.table = table;
        labelListener = new Listener()
        {
            public void handleEvent(Event event)
            {
                Label label = (Label) event.widget;
                Shell shell = label.getShell();
                switch (event.type)
                {
                    case SWT.MouseDown:
                        Event e = new Event();
                        e.item = (TableItem) label.getData("_TABLEITEM"); //$NON-NLS-1$
                        // Assuming table is single select, set the selection as if
                        // the mouse down event went through to the table
                        table.setSelection(new TableItem[]{(TableItem) e.item});
                        table.notifyListeners(SWT.Selection, e);
                        shell.dispose();
                        table.setFocus();
                        break;
                    case SWT.MouseExit:
                        shell.dispose();
                        break;
                }
            }
        };

        tableListener = new Listener()
        {
            Shell tip = null;

            public void handleEvent(Event event)
            {
                switch (event.type)
                {
                    case SWT.Dispose:
                    case SWT.KeyDown:
                    case SWT.MouseMove:
                    {
                        if (tip == null)
                            break;
                        tip.dispose();
                        tip = null;
                        label = null;
                        break;
                    }
                    case SWT.MouseHover:
                    {
                        Display display = table.getDisplay();
                        Point pt = new Point(event.x, event.y);
                        TableItem item = table.getItem(pt);

                        if (item != null)
                        {
                            if (tip != null && !tip.isDisposed())
                                tip.dispose();
                            tip = new Shell(table.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
                            tip.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                            FillLayout layout = new FillLayout();
                            layout.marginWidth = 2;
                            tip.setLayout(layout);
                            label = new Label(tip, SWT.NONE);
                            label.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
                            label.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                            label.setData("_TABLEITEM", item); //$NON-NLS-1$
                            int colIndex = getColumnIndex(table, pt);
                            String tooltip = ""; //$NON-NLS-1$
                            if (item.getData() instanceof ITableSelectable)
                            {
                                tooltip = columnTooltip.get(colIndex);
                                if (tooltip == null || "".equals(tooltip)) //$NON-NLS-1$
                                {
                                    tooltip = item.getText(colIndex);
                                }
                            }
                            if (!"".equals(tooltip)) //$NON-NLS-1$
                            {
                                label.setText(tooltip);
                                label.addListener(SWT.MouseExit, labelListener);
                                listenerList.put(SWT.MouseExit, labelListener);
                                label.addListener(SWT.MouseDown, labelListener);
                                listenerList.put(SWT.MouseDown, labelListener);
                                Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                                Rectangle rect = item.getBounds(colIndex);
                                Point pt2 = table.toDisplay(rect.x, rect.y - 10);
                                tip.setBounds(pt2.x, pt2.y, size.x, size.y);
                                tip.setVisible(true);
                            }
                        }
                    }
                }
            }
        };

        table.addListener(SWT.Dispose, tableListener);
        listenerList.put(new Integer(SWT.Dispose), tableListener);
        table.addListener(SWT.KeyDown, tableListener);
        listenerList.put(new Integer(SWT.KeyDown), tableListener);
        table.addListener(SWT.MouseMove, tableListener);
        listenerList.put(new Integer(SWT.MouseMove), tableListener);
        table.addListener(SWT.MouseHover, tableListener);
        listenerList.put(new Integer(SWT.MouseHover), tableListener);
    }

    /**
     * Set tooltip explicitly to a column.
     * 
     * @param column column index beginning with 0 for the first column
     * @param tooltiptext Tooltip to be set
     */
    public void setTooltip(int column, String tooltiptext)
    {
        this.columnTooltip.put(new Integer(column), tooltiptext);
    }

    /**
     * @param table
     * @param pt
     * @return
     */
    private int getColumnIndex(final Table table, Point pt)
    {
        int columnCount = table.getColumnCount();
        int colIndex = 0;
        int rowIndex = table.getTopIndex();
        while (rowIndex < table.getItemCount())
        {
            boolean visible = false;
            TableItem item2 = table.getItem(rowIndex);
            for (colIndex = 0; colIndex < columnCount; colIndex++)
            {
                Rectangle rect = item2.getBounds(colIndex);
                if (rect.contains(pt))
                {
                    LOG.finest("Item " + rowIndex + "-" + colIndex); //$NON-NLS-1$ //$NON-NLS-2$
                    visible = true;
                    break;
                }
            }
            if (visible)
            {
                break;
            }
            rowIndex++;
        }
        return colIndex;
    }

    /**
     * removes all registered listeners
     */
    public void removeTooltipListener()
    {
        Enumeration<Integer> en = listenerList.keys();
        while (en.hasMoreElements())
        {
            Integer event = (Integer) en.nextElement();
            Listener listen = (Listener) listenerList.get(event);
            switch (event.intValue())
            {
                case SWT.Dispose:
                    table.removeListener(SWT.Dispose, listen);
                    break;
                case SWT.KeyDown:
                    table.removeListener(SWT.KeyDown, listen);
                    break;
                case SWT.MouseMove:
                    table.removeListener(SWT.MouseMove, listen);
                    break;
                case SWT.MouseHover:
                    table.removeListener(SWT.MouseHover, listen);
                    break;
                case SWT.MouseExit:
                    label.removeListener(SWT.MouseExit, listen);
                    break;
                case SWT.MouseDown:
                    label.removeListener(SWT.MouseDown, listen);
                    break;

            }
        }
    }

}
