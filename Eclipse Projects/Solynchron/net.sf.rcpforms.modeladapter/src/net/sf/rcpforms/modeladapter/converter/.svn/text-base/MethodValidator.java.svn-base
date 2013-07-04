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
// Created on 02.05.2008

package net.sf.rcpforms.modeladapter.converter;

import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.runtime.IStatus;

/**
 * abstract implementation of a model validator for properties using a method to overwrite.
 * <p>
 * Example:
 * 
 * <pre>
 * validationManager.addValidator(new MethodValidator(TestModel.P_Age,
 * TestModel.P_Birthdate) {
 * 
 * &#064;Override public IStatus validate(Object value) { IStatus result = ok();
 *           TestModel model = (TestModel) value; Calendar calendar =
 *           GregorianCalendar.getInstance();
 *           calendar.setTime(model.getBirthdate()); Calendar today =
 *           GregorianCalendar.getInstance(); today.setTime(new Date()); if
 *           (model.getAge() != today.get(Calendar.YEAR) -
 *           calendar.get(Calendar.YEAR)) { result = error(&quot;Are you
 *           cheating about your age ?&quot;); } return result; } });
 * 
 * </pre>
 * 
 * @author Marco van Meegen
 */
public abstract class MethodValidator extends AbstractModelValidator
{
    private Object[] properties = null;

    /**
     * @param flatProperties list of property names the validator depends upon. ATTENTION: nested
     *            properties are not supported here !
     */
    public MethodValidator(Object... flatProperties)
    {
        // TODO support nested properties for validator 152077
        for (Object object : flatProperties)
        {
            Validate.isTrue(object != null);
        }
        this.properties = flatProperties;
    }

    public String getId()
    {
        return super.toString();
    }

    public Object[] getProperties()
    {
        return properties;
    }

    /**
     * validates the given model, which may be a composite model.
     * 
     * @param model model to pass to the validator. The whole data model is passed to the validator
     * @return validation status of this validator.
     */
    public abstract IStatus validate(Object model);
}
