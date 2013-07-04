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

import java.util.logging.Logger;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Table;

/**
 * A TableViewer which is capable of making rows not selectable. The underlying model object must
 * implement {@link ITableSelectable} to maintain this state.
 * 
 * @author Remo Loetscher
 */
public class DisabledRowTableViewer extends TableViewer implements IFontProvider
{
    private static final Logger LOG = Logger.getLogger(DisabledRowTableViewer.class.getName());

    private boolean enabled = true;

    Table table = null;

    CustomTooltipListener customTooltipListener = new CustomTooltipListener();

    public DisabledRowTableViewer(final Table table)
    {
        super(table);
        this.table = table;
        // Implement a "fake" tooltip
        customTooltipListener.registerListener(table);

    }

    /**
     * removes all registered listeners
     */
    public void removeTooltipListener()
    {
        customTooltipListener.removeTooltipListener();
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
     * Set tooltip explicitly to a column.
     * 
     * @param column column index beginning with 0 for the first column
     * @param tooltiptext Tooltip to be set
     */
    public void setTooltip(int column, String tooltiptext)
    {
        customTooltipListener.setTooltip(column, tooltiptext);
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
