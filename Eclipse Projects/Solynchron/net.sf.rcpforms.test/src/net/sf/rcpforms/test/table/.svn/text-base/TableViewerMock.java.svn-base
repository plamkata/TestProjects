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

package net.sf.rcpforms.test.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

/**
 * mock table viewer to check communication with content provider
 * 
 * @author Marco van Meegen
 */

class TableViewerMock extends org.eclipse.jface.viewers.TableViewer

{

    public TableViewerMock(Composite parent)

    {

        super(parent);

    }

    @Override
    public void add(Object element)

    {

        addedElements.add(element);

    }

    @Override
    public void insert(Object element, int position)

    {

        addedElements.add(element);

    }

    @Override
    public void remove(Object element)

    {

        removedElements.add(element);

    }

    @Override
    public void update(Object element, String[] properties)

    {

        updatedElements.add(element);

    }

    @Override
    protected void inputChanged(Object input, Object oldInput)

    {

    }

    public List<Object> updatedElements = new ArrayList<Object>();

    public List<Object> addedElements = new ArrayList<Object>();

    public List<Object> removedElements = new ArrayList<Object>();

    public void resetMockData()

    {

        updatedElements.clear();

        addedElements.clear();

        removedElements.clear();

    }

}