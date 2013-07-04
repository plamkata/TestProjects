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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * @author lindoerfern
 */
public class StringToCalendarConverter implements IConverter
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

        final Date date = DateConv.toDate((String) fromObject);
        Calendar result = null;
        if (date != null)
        {
            result = new GregorianCalendar();
            result.setTime(date);
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
        return Calendar.class;
    }
}