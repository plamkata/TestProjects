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

package net.sf.rcpforms.bindingvalidation;

import java.util.Collection;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.set.ListToSetAdapter;

/**
 * Class ModifiableListToSetAdapter. Extends ListToSetAdapter to work in both directions.
 * 
 * @author Marco van Meegen
 */
public class ModifiableListToSetAdapter extends ListToSetAdapter
{
    private IObservableList list;

    public ModifiableListToSetAdapter(IObservableList list)
    {
        super(list);
        this.list = list;
    }

    public boolean add(Object o)
    {
        return list.add(o);
    }

    public boolean addAll(Collection c)
    {
        return list.addAll(c);
    }

    public boolean remove(Object o)
    {
        return list.remove(o);
    }

    public boolean removeAll(Collection c)
    {
        return list.removeAll(c);
    }

    public boolean retainAll(Collection c)
    {
        return list.retainAll(c);
    }

    public void clear()
    {
        list.clear();
    }

}
