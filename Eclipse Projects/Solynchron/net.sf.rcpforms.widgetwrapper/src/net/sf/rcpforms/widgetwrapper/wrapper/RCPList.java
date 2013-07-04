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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;

/**
 * a compound of a label and a {@link RCPSimpleList}.
 * 
 * @author Marco van Meegen
 */
public class RCPList extends RCPLabeledControl<RCPSimpleList> implements IViewer
{
    public RCPList(String label)
    {
        this(label, SWT.DEFAULT);
    }

    public RCPList(String label, int style)
    {
        super(label, new RCPSimpleList(style));
    }

    public final List getSWTList()
    {
        return getRCPControl().getSWTList();
    }

    public ContentViewer getViewer()
    {
        return getRCPControl().getViewer();
    }

}
