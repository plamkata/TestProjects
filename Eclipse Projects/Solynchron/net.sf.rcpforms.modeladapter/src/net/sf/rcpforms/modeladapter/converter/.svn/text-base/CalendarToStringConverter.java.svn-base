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

import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * @author lindoerfern
 */
public class CalendarToStringConverter implements IConverter
{

    public CalendarToStringConverter()
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
     */
    public Object convert(Object fromObject)
    {
        Validate.isTrue(null == fromObject || fromObject instanceof Calendar,
                "Object fromObject must be Calendar"); //$NON-NLS-1$

        return DateConv.toDateString(fromObject == null ? null : ((Calendar) fromObject).getTime());
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
     */
    public Object getFromType()
    {
        return Calendar.class;
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
     */
    public Object getToType()
    {
        return String.class;
    }
}
