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
// Created on 27.05.2008

package net.sf.rcpforms.test.adapter;

import net.sf.rcpforms.modeladapter.configuration.IRangeAdapter;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.BeanConverterRegistry;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;

import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * model adapter for hashmaps wrapped into an WritableMap for change notification
 * 
 * @author Marco van Meegen
 */
public class MapAdapter extends ModelAdapter
{

    public MapAdapter()
    {
        super(new BeanConverterRegistry());
    }

    @Override
    public boolean canAdaptClass(Object metaClass)
    {
        return metaClass instanceof Class && WritableMap.class.isAssignableFrom((Class<?>) metaClass);
    }

    @Override
    public Object getMetaClass(Object modelObjectToAdapt)
    {
        return modelObjectToAdapt.getClass();
    }

    @Override
    public IObservableValue getObservableValue(Object modelInstance, IPropertyChain propertyChain)
    {
        return new MapObservableValue((WritableMap) modelInstance, (MapPropertyChain) propertyChain);
    }

    @Override
    public IPropertyChain getPropertyChain(Object beanClass, Object... properties)
    {
        return new MapPropertyChain((Class<?>) beanClass, properties);
    }

    @Override
    public IRangeAdapter getRangeAdapter(IPropertyChain propertyChain)
    {
        return null;
    }

    @Override
    public void validatePropertyPath(Object metaClass, String propertyPath, boolean writable)
    {
    }
    
    @Override
    public IStructuredContentProvider createDefaultContentProvider()
    {
        return new ObservableListContentProvider();
    }

}
