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

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * A CheckboxTableViewer which is capable of making rows not selectable. The underlying model object
 * must implement {@link ITableSelectable} to maintain this state.
 * 
 * @author lindoerfern
 * @author Remo Loetscher
 */
public class DisabledRowCheckboxTableViewer extends CheckboxTableViewer implements IFontProvider
{

    private boolean enabled = true;

    public DisabledRowCheckboxTableViewer(Table table)
    {
        super(table);
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
                this.setSelection(null);
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

        // gray checkboxes of disabled rows or if table disabled, gray all!

        if (enabled) // take care of disabled rows
        {
            for (TableItem item : this.getTable().getItems())
            {
                if (item.getData() != null && item.getData() instanceof ITableSelectable) // toggle
                                                                                          // checkbox
                                                                                          // if row
                                                                                          // is not
                                                                                          // selectable!
                {
                    this
                            .grayTableItem(item, !((ITableSelectable) item.getData())
                                    .getIsSelectable());
                }
                else
                {
                    // only support ITableselectable -> all other items are paint in black/white
                    this.grayTableItem(item, false);
                }

            }
        }
        else
        // disable all checkboxes
        {
            for (TableItem item : this.getTable().getItems())
                item.setGrayed(true);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.AbstractTableViewers#doUpdateItem(org.eclipse.swt.widgets.Widget,
     *      java.lang.Object, boolean)
     */

    protected void doUpdateItem(Widget widget, Object element, boolean fullMap)
    {
        TableItem item = null;
        if (widget instanceof TableItem)
        {
            item = (TableItem) widget;
            if (element != null && element instanceof ITableSelectable) // toggle checkbox if row is
                                                                        // not selectable!
            {
                this.grayTableItem(item, !((ITableSelectable) element).getIsSelectable()
                        || !this.enabled);
            }
            super.doUpdateItem(widget, element, fullMap);
        }

    }

    private void grayTableItem(TableItem item, boolean paintGrayCheckbox)
    {
        item.setGrayed(paintGrayCheckbox);
    }

    /**
     * handle check events in table. if table or row is not enabled or not selectable don't
     * propagate this event.
     */
    public void handleSelect(SelectionEvent event)
    {
        if (event.detail == SWT.CHECK)
        {
            Widget widget = event.item;
            if (widget instanceof TableItem)
            {
                TableItem item = (TableItem) widget;
                if (!this.enabled) // toggle checkbox if table is not enabled!
                {
                    item.setChecked(!item.getChecked());
                    return; // interrupt event handling here and do not notify any listener
                            // (including ICheckStateListener)

                }
                else if (item.getData() != null && item.getData() instanceof ITableSelectable) // toggle
                                                                                               // checkbox
                                                                                               // if
                                                                                               // row
                                                                                               // is
                                                                                               // not
                                                                                               // selectable!
                {
                    ITableSelectable dlam = ((ITableSelectable) item.getData());
                    if (!dlam.getIsSelectable())
                    {
                        item.setChecked(!item.getChecked());
                        return; // interrupt event handling here and do not notify any listener
                                // (including ICheckStateListener)
                    }
                }
            }
        }
        super.handleSelect(event);

    }

    public Font getFont(Object element)
    {
        return JFaceResources.getDialogFont();
    }
}
