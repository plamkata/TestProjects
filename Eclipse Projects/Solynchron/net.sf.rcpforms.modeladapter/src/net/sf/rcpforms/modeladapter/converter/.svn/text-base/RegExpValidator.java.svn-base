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
import java.util.regex.Pattern;

import net.sf.rcpforms.modeladapter.Messages;
import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * @author lindoerfern
 */
public class RegExpValidator implements IValidator
{
    public static final String EMAIL_REGEXP = Messages.getString("RegExpValidator.EMAIL_REGEXP"); //$NON-NLS-1$

    private Pattern regexp;

    public RegExpValidator(String regexp)
    {
        Validate.notNull(regexp);
        this.regexp = Pattern.compile(regexp);
    }

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
            // remove carriage returns
            str = str.replace("\r\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
            if (regexp != null && !"".equals(regexp) && !regexp.matcher(str).matches()) //$NON-NLS-1$
            {
                String message = MessageFormat.format(Messages.getString("RegExpValidator.ValidationErrorMessage"), str, regexp.toString()); //$NON-NLS-1$
                result = ValidationStatus.error(message);
            }
        }

        return result;
    }
}
