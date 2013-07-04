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

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * A TableViewer which is capable of making rows not selectable. The underlying model object must
 * implement {@link ITableSelectable} to maintain this state.
 * 
 * @author Remo Loetscher
 */
public class DisabledRowTreeViewer extends TreeViewer implements IFontProvider
{
    private static final Logger LOG = Logger.getLogger(DisabledRowTreeViewer.class.getName());

    private boolean enabled = true;

    private Hashtable<Integer, String> columnTooltip = new Hashtable<Integer, String>();

    private Hashtable<Integer, Listener> listenerList = new Hashtable<Integer, Listener>();

    Label label = null;

    Tree tree = null;

    public DisabledRowTreeViewer(final Tree tree)
    {
        super(tree);
        this.tree = tree;
        // Implement a "fake" tooltip
        registerListener(tree);

    }

    /**
     * @param table
     */
    private void registerListener(final Tree tree)
    {
        final Listener labelListener = new Listener()
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
                        tree.setSelection(new TreeItem[]{(TreeItem) e.item});
                        tree.notifyListeners(SWT.Selection, e);
                        shell.dispose();
                        tree.setFocus();
                        break;
                    case SWT.MouseExit:
                        shell.dispose();
                        break;
                }
            }
        };

        Listener tableListener = new Listener()
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
                        Display display = tree.getDisplay();
                        Point pt = new Point(event.x, event.y);
                        TreeItem item = tree.getItem(pt);

                        if (item != null)
                        {
                            if (tip != null && !tip.isDisposed())
                                tip.dispose();
                            tip = new Shell(tree.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
                            tip.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                            FillLayout layout = new FillLayout();
                            layout.marginWidth = 2;
                            tip.setLayout(layout);
                            label = new Label(tip, SWT.NONE);
                            label.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
                            label.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                            label.setData("_TABLEITEM", item); //$NON-NLS-1$
                            int colIndex = getColumnIndex(tree, pt);
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
                                Point pt2 = tree.toDisplay(rect.x, rect.y - 10);
                                tip.setBounds(pt2.x, pt2.y, size.x, size.y);
                                tip.setVisible(true);
                            }
                        }
                    }
                }
            }
        };
        tree.addListener(SWT.Dispose, tableListener);
        listenerList.put(new Integer(SWT.Dispose), tableListener);
        tree.addListener(SWT.KeyDown, tableListener);
        listenerList.put(new Integer(SWT.KeyDown), tableListener);
        tree.addListener(SWT.MouseMove, tableListener);
        listenerList.put(new Integer(SWT.MouseMove), tableListener);
        tree.addListener(SWT.MouseHover, tableListener);
        listenerList.put(new Integer(SWT.MouseHover), tableListener);
    }

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
                    tree.removeListener(SWT.Dispose, listen);
                    break;
                case SWT.KeyDown:
                    tree.removeListener(SWT.KeyDown, listen);
                    break;
                case SWT.MouseMove:
                    tree.removeListener(SWT.MouseMove, listen);
                    break;
                case SWT.MouseHover:
                    tree.removeListener(SWT.MouseHover, listen);
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

    /**
     * @param table
     * @param pt
     * @param columnCount
     * @param colIndex
     * @return
     */
    private int getColumnIndex(final Tree tree, Point pt)
    {
        int columnCount = tree.getColumnCount();
        int colIndex = 0;
        int rowIndex = 0;
        while (rowIndex < tree.getItemCount())
        {
            boolean visible = false;
            TreeItem item2 = tree.getItem(rowIndex);
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
     * Override this method in order to make rows disabled by asking if item has implemented
     * <code>ITableSelectable</code>. If the given table item is not selectable
     * <code>{@link #setSelection(null)} </code> is called, so the item is inactive.
     */
    protected void updateSelection(ISelection selection) {
        if(selection instanceof StructuredSelection)
        {
            StructuredSelection sel = (StructuredSelection) selection;
            Object obj = sel.getFirstElement();
            //simulate disable row
            if (obj != null && 
                    (!enabled || 
                            (obj instanceof ITableSelectable && !((ITableSelectable) obj).getIsSelectable())))
            {
                //set null selection to simulate disabled row
                this.setSelection((ISelection)null);
                return;
            }
        }
        super.updateSelection(selection);
    }

    /**
     * enable the table viewer
     * 
     * @param enabled true: table is enabled, false: table is not enabled, all rows are grayed and
     *            not selectable
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public Font getFont(Object element)
    {
        return JFaceResources.getDialogFont();
    }

}
