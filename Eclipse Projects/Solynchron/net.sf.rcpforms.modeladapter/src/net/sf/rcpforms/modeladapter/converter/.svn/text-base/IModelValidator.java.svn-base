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

import org.eclipse.core.databinding.validation.IValidator;

/**
 * interface for a validator. It knows the properties it is bound to and returns a validation
 * status. For compatibility with future API extensions, please DO NOT inherit directly, subclass
 * {@link AbstractModelValidator} !!!
 *
 * @author Marco van Meegen
 */
public interface IModelValidator extends IValidator
{
    /**
     * properties the validator depends upon.
     *
     * @return array of property chains (here object since representation is private to model
     *         adapter) describing the properties the validator depends upon; null if validator
     *         should be updated on any changes known to the validation manager. ATTENTION: This
     *         might be imperformant.
     */
    public abstract Object[] getProperties();

    /**
     * @return validation manager wide unique id of this validator instance. Used to identify
     *         messages created by this validator in the message manager, thus really make sure
     *         theyre unique, otherwise you'll have nasty long debug sessions !
     */
    public String getId();

    /**
     * @return the property names of the fields which are invalid. Clients can use this to mark the
     *         invalid fields. This must be a subset (or same) of {@link #getProperties()}.
     */
    public Object[] getInvalidProperties(Object model);

    /**
     * specifiy if fields bound to properties of this validator should be decorated if validator
     * fails
     */
    public void setDecorateField(boolean newState);

    /**
     * true if fields bound to properties of this validator should be decorated if validator fails,
     * default: true
     */
    public boolean isDecorateField();

    /** specify if a message will be shown in the form title if validator fails, default: true */
    public void setShowMessage(boolean newState);

    /** true if a message will be shown in the form title if validator fails, default: true */
    public boolean isShowMessage();

}