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

import java.util.Date;

import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * @author mukhinr
 */
public class DateToStringConverter implements IConverter
{

    public DateToStringConverter()
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
     */
    public Object convert(Object fromObject)
    {
        Validate.isTrue(null == fromObject || fromObject instanceof Date,
                "Object fromObject must be Date"); //$NON-NLS-1$

        return DateConv.toDateString((Date) fromObject);
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
     */
    public Object getFromType()
    {
        return Date.class;
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
     */
    public Object getToType()
    {
        return String.class;
    }
}
