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
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * wrapper around tree
 * 
 * @author Marco van Meegen
 */
public class RCPSimpleTree extends RCPControl implements IViewer
{
    private TreeViewer viewer;

    public RCPSimpleTree()
    {
        super(null, SWT.DEFAULT);
    }

    public RCPSimpleTree(int style)
    {
        super(null, style);
    }

    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Tree result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createTree(getSWTParent());
        }
        else
        {
            result = getFormToolkit().createTree(getSWTParent(), getStyle());
        }
        createViewer(result);
        return result;
    }

    /**
     * creates the viewer for the table
     * 
     * @param widget
     */
    protected void createViewer(Tree widget)
    {
        viewer = getFormToolkitEx().createTreeViewer(widget);
    }

    public final Tree getSWTTree()
    {
        return getTypedWidget();
    }

    public ContentViewer getViewer()
    {
        return viewer;
    }
}
