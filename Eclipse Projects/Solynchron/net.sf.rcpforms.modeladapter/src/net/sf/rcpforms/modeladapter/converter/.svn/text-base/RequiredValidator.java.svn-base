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

import java.util.ArrayList;
import java.util.List;

import net.sf.rcpforms.modeladapter.Messages;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.runtime.IStatus;

/**
 * validates that all properties defined in the constructor are not null.
 * <p>
 * 
 * @author Marco van Meegen
 */
public class RequiredValidator extends MethodValidator
{
    public RequiredValidator(Object... flatProperties)
    {
        super(flatProperties);
    }

    /**
     * check all fields if they are required
     */
    @Override
    public IStatus validate(Object model)
    {
        List<Object> missing = null;
        try
        {
            missing = getMissingFields(model, false);
        }
        catch (Exception ex)
        {
            return error("Exception in RequiredValidator: " + ex); //$NON-NLS-1$
        }
        Validate.notNull(missing, "if no exception was thrown, missing should have been != null"); //$NON-NLS-1$
        // return missing.isEmpty() ? ok() : info("Not all required Fields are filled.");
        // for backward compatibility with version 3.3 missing required fields have to be decorated
        // with warnings.
        // controlDecorator#update in MessageManager doesn't support information decorations...
        return missing.isEmpty() ? ok() : warning(Messages.getString("RequiredValidator.NotAllRequiredFieldsFilled")); //$NON-NLS-1$
    }

    /**
     * @param model model to check for required fields
     * @param ignoreExceptions flag if exceptions should be ignored
     * @return list of property names of still missing fields, empty list if all required fields are
     *         filled
     */
    private List<Object> getMissingFields(Object model, boolean ignoreExceptions)
    {
        List<Object> result = new ArrayList<Object>();
        try
        {
            ModelAdapter modelAdapter = ModelAdapter.getAdapterForInstance(model);
            for (int i = 0; i < getProperties().length; i++)
            {
                String prop = (String) getProperties()[i];
                Object value = modelAdapter.getValue(model, prop);
                if (value == null)
                {
                    result.add(prop);
                }
            }
        }
        catch (RuntimeException ex)
        {
            ex.printStackTrace();
            if (!ignoreExceptions)
            {
                throw ex;
            }
        }
        return result;
    }

    /**
     * @return the property names of the fields which are invalid. Clients can use this to mark the
     *         invalid fields.
     */
    public Object[] getInvalidProperties(Object model)
    {
        return getMissingFields(model, true).toArray();

    }
}
