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

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * wraps a table. A table can be very comfortably configured using the {@link TableUtil} class of
 * the tablesupport layer of rcpforms.
 * 
 * @author Marco van Meegen
 */
public class RCPSimpleTable extends RCPControl implements IViewer
{
    private TableViewer viewer;

    public RCPSimpleTable()
    {
        this(SWT.DEFAULT);
    }

    public RCPSimpleTable(int style)
    {
        super(null, style);
    }

    /**
     * creates the table and an appropriate viewer
     */
    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Table result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createTable(getSWTParent());
        }
        else
        {
            result = getFormToolkit().createTable(getSWTParent(), getStyle());
        }
        createViewer(result);
        return result;
    }

    /**
     * creates the viewer for the table
     * 
     * @param widget
     */
    protected void createViewer(Table widget)
    {
        viewer = getFormToolkitEx().createTableViewer(widget);
    }

    public final Table getSWTTable()
    {
        return getTypedWidget();
    }

    public ContentViewer getViewer()
    {
        return viewer;
    }
}
