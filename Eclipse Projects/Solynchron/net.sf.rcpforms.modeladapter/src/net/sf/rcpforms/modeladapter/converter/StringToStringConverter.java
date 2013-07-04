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

import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * converter from string to string, used to convert empty strings to null strings. This is needed
 * for text widget binding because they dont show a difference between null and ""
 * 
 * @author vanmeegenm
 */
public class StringToStringConverter implements IConverter
{

    public StringToStringConverter()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
     */
    public Object convert(Object fromObject)
    {
        Validate.isTrue(null == fromObject || fromObject instanceof String,
                "Object fromObject must be String"); //$NON-NLS-1$
        String fromString = (String) fromObject;
        String result = fromString;
        if (fromString != null && fromString.length() == 0)
        {
            result = null;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
     */
    public Object getFromType()
    {
        return String.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
     */
    public Object getToType()
    {
        return String.class;
    }
}
