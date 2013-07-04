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
import org.eclipse.swt.widgets.Tree;

/**
 * a compound of a label and a {@link RCPSimpleTree}.
 * 
 * @author Marco van Meegen
 */
public class RCPTree extends RCPLabeledControl<RCPSimpleTree> implements IViewer
{
    public RCPTree(String label)
    {
        this(label, SWT.DEFAULT);
    }

    public RCPTree(String label, int style)
    {
        super(label, new RCPSimpleTree(style));
    }

    public final Tree getSWTTree()
    {
        return getRCPControl().getSWTTree();
    }

    public ContentViewer getViewer()
    {
        return getRCPControl().getViewer();
    }

}
