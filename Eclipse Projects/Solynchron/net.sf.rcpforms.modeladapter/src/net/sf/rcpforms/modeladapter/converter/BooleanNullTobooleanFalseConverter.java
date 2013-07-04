/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.modeladapter.converter;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * Class BooleanNullTobooleanFalseConverter converts a Boolean null to boolean false
 * 
 * @author Marco van Meegen
 */
public class BooleanNullTobooleanFalseConverter implements IConverter
{

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
     */
    public Object convert(Object fromObject)
    {
        Object result = fromObject;
        if (fromObject == null)
        {
            fromObject = Boolean.FALSE;
        }
        return result;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
     */
    public Object getFromType()
    {
        return Boolean.class;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
     */
    public Object getToType()
    {
        return boolean.class;
    }

}
