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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * wraps a List
 * 
 * @author Marco van Meegen
 */
public class RCPSimpleList extends RCPControl implements IViewer
{
    private ListViewer viewer;

    public RCPSimpleList()
    {
        super(null, SWT.DEFAULT);
    }

    public RCPSimpleList(int style)
    {
        super(null, style);
    }

    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        List result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createList(getSWTParent());
        }
        else
        {
            result = getFormToolkitEx().createList(getSWTParent(), getStyle());
        }
        viewer = new ListViewer(result);
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setLabelProvider(new LabelProvider());
        return result;
    }
    

    public final List getSWTList()
    {
        return getTypedWidget();
    }

    public ContentViewer getViewer()
    {
        return viewer;
    }

}
