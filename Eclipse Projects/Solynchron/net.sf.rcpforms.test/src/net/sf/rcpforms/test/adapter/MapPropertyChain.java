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

package net.sf.rcpforms.test.adapter;

import net.sf.rcpforms.modeladapter.converter.IPropertyChain;

import org.eclipse.core.databinding.observable.map.WritableMap;

/**
 * example model adapter for maps to verify flexibility of model adapter approach.
 * 
 * @author Marco van Meegen
 */
public class MapPropertyChain implements IPropertyChain
{
    private String[] properties;

    private Class<?> beanMeta;

    public MapPropertyChain(Class<?> clazz, Object... properties)
    {
        assert clazz == WritableMap.class;
        assert properties != null && properties.length == 1; // nested prop chains not supported yet
        this.beanMeta = clazz;
        this.properties = new String[properties.length];
        System.arraycopy(properties, 0, this.properties, 0, properties.length);
    }

    public Object getModelMeta()
    {
        return beanMeta;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof MapPropertyChain)
        {
            MapPropertyChain propChain = (MapPropertyChain) obj;
            if (propChain.getModelMeta() == getModelMeta()
                    && propChain.properties.length == properties.length)
            {
                result = true;
                for (int i = 0; i < properties.length; i++)
                {
                    if (!properties[i].equals(propChain.properties[i]))
                    {
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    public Class<?> getType()
    {
        return Object.class;
    }

    @Override
    public String toString()
    {
        String result = "MapPropChain(" + beanMeta.getName();
        for (String prop : properties)
        {
            result += "." + prop;
        }
        return result + ")";
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    public String getKey()
    {
        return properties[0];
    }

    /**
     * (non-Javadoc)
     * 
     * @see net.sf.rcpforms.widgetwrapper.binding.IPropertyChain#getValue(java.lang.Object)
     */
    public Object getValue(Object model)
    {
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see net.sf.rcpforms.widgetwrapper.binding.IPropertyChain#setValue(java.lang.Object,
     *      java.lang.Object)
     */
    public void setValue(Object model, Object value)
    {

    }

}
