/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Roman Mukhin - initial implementation
 *     Marco van Meegen - refactor to use here
 *
 ******************************************************************************
 */

package net.sf.rcpforms.modeladapter.converter;

import java.util.Date;

import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * @author mukhinr, Marco van Meegen
 */
public class StringToDateConverter implements IConverter
{
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
     */
    public Object convert(Object fromObject)
    {
        Validate.isTrue(null == fromObject || fromObject instanceof String,
                "Object fromObject must be String"); //$NON-NLS-1$

        return DateConv.toDate((String) fromObject);
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
        return Date.class;
    }
}