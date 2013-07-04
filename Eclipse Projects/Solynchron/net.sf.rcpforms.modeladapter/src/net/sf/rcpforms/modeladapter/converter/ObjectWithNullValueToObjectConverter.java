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

package net.sf.rcpforms.modeladapter.converter;

import net.sf.rcpforms.common.NullValue;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * converter from Object to Object, used to convert the {@link NullValue} object to a real null
 * 
 * @author vanmeegenm
 */
public class ObjectWithNullValueToObjectConverter implements IConverter
{
    private Object model;

    public ObjectWithNullValueToObjectConverter()
    {
    }

    public ObjectWithNullValueToObjectConverter(Object model)
    {
        this.model = model;
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
     */
    public Object convert(Object fromObject)
    {
        Object result = fromObject;
        // translate null value placeholder used in viewers to real null before writing to model
        if (isNullValue(fromObject))
        {
            result = null;
        }
        return result;
    }

    /**
     * @param object object to check
     * @return true if the object is the NullValue placeholder
     */
    private boolean isNullValue(Object object)
    {
        return object == NullValue.getInstance();
    }

    public Object getFromType()
    {
        return Object.class;
    }

    public Object getToType()
    {
        return model != null ? model : Object.class;
    }
}
