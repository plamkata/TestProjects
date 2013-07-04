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

package net.sf.rcpforms.bindingvalidation.toolkit;

import net.sf.rcpforms.tablesupport.tables.DisabledRowCheckboxTableTreeViewer;
import net.sf.rcpforms.tablesupport.tables.DisabledRowCheckboxTableViewer;
import net.sf.rcpforms.tablesupport.tables.DisabledRowTableViewer;
import net.sf.rcpforms.tablesupport.tables.DisabledRowTreeViewer;
import net.sf.rcpforms.tablesupport.tables.SizeFixedTable;
import net.sf.rcpforms.widgetwrapper.builder.RCPFormToolkit;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

/**
 * RCPTableFormToolkit extends the standard toolkit to create table widgets with advanced feature
 * support:
 * <ul>
 * <li>table rows can be disabled and then are not selectable anymore (works only with non-editable
 * tables)
 * <li>table columns are resized automatically when the table is resized according to column weight
 * </ul>
 * 
 * @author Marco van Meegen
 */
public class RCPTableFormToolkit extends RCPFormToolkit
{

    /**
     * Constructor for RCPTableFormToolkit, modifies table and table viewer creation for additional
     * features
     */
    public RCPTableFormToolkit(Display display)
    {
        super(display);
    }

    /**
     * create a table which supports better resize behavior
     */
    @Override
    public Table createTable(final Composite parent, final int style)
    {
        Table table = new SizeFixedTable(parent, style | getBorderStyle() | getOrientation());
        adapt(table, false, false);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        // setApplicationDefaultFontToControl(table);
        return table;
    }

    /**
     * create viewer whic is able to disable rows
     */
    @Override
    public TableViewer createTableViewer(Table widget)
    {
        TableViewer viewer = null;
        if ((widget.getStyle() & SWT.CHECK) == SWT.CHECK)
        {
            viewer = new DisabledRowCheckboxTableViewer(widget);
        }
        else
        {
            viewer = new DisabledRowTableViewer(widget);
        }
        return viewer;
    }

    public TreeViewer createTreeViewer(Tree widget)
    {
        TreeViewer viewer = null;
        if ((widget.getStyle() & SWT.CHECK) == SWT.CHECK)
        {
            viewer = new DisabledRowCheckboxTableTreeViewer(widget);
        }
        else
        {
            viewer = new DisabledRowTreeViewer(widget);
        }
        return viewer;
    }
}
