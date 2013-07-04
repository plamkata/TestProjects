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

import java.text.MessageFormat;

import net.sf.rcpforms.modeladapter.Messages;
import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * @author mukhinr
 */
public class DateToStringValidator implements IValidator
{
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
     */
    public IStatus validate(Object value)
    {
        Validate.isTrue(null == value || value instanceof String, "Object value must be String"); //$NON-NLS-1$

        IStatus result = Status.OK_STATUS;

        String str = (String) value;
        if (str != null && str.length() > 0)
        {
            if (str.matches(Messages.getString("DateToStringValidator.DateRegexp"))) { //$NON-NLS-1$
                try
                {
                    DateConv.toDate(str); // just verify
                }
                catch (IllegalArgumentException e)
                {
                    result = ValidationStatus.error(MessageFormat.format(Messages.getString("DateToStringValidator.NoValidDate"), value), e); //$NON-NLS-1$
                }
            }
            else
            {
                result = ValidationStatus
                        .error(Messages.getString("DateToStringValidator.NoValidDateErrorMessage")); //$NON-NLS-1$
            }
        }

        return result;
    }
}
