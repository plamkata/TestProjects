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
 * converter from Object to Object, used to convert null to the {@link NullValue} placeholder
 * object.
 * 
 * @author vanmeegenm
 */
public class ObjectToObjectWithNullValueConverter implements IConverter
{
    private Object model;

    public ObjectToObjectWithNullValueConverter()
    {
    }

    public ObjectToObjectWithNullValueConverter(Object model)
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
        // translate null value to NullValue placeholder
        if (fromObject == null)
        {
            result = NullValue.getInstance();
        }
        return result;
    }

    public Object getFromType()
    {
        return model != null ? model : Object.class;
    }

    public Object getToType()
    {
        return Object.class;
    }
}
