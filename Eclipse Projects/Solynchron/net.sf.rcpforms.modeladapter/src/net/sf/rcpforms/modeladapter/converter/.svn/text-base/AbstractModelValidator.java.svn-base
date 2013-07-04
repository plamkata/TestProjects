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

import net.sf.rcpforms.modeladapter.Activator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * abstract implementation of a model validator for properties.
 *
 * @author Marco van Meegen
 */
public abstract class AbstractModelValidator implements IModelValidator
{
    private boolean showMessage = true;

    private boolean decorateField = true;

    public String getId()
    {
        return super.toString();
    }

    public Object[] getProperties()
    {
        return null;
    }

    /**
     * default implementation: returns all properties
     *
     * @see net.sf.rcpforms.modeladapter.converter.IModelValidator#getInvalidProperties(java.lang.Object)
     */
    public Object[] getInvalidProperties(Object model)
    {
        return getProperties();
    }

    /**
     * TODO: refine concept of what is passed. How to implement model-spanning validations ?
     *
     * @param model model to pass to the validator. The whole data model is passed to the validator
     * @return validation status of this validator.
     */
    public abstract IStatus validate(Object model);

    /**
     * convenience method to return an error status.
     */
    public static IStatus error(String message)
    {
        return new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, message, null);
    }

    public static IStatus warning(String message)
    {
        return new Status(IStatus.WARNING, Activator.PLUGIN_ID, IStatus.OK, message, null);
    }

    public static IStatus info(String message)
    {
        return new Status(IStatus.INFO, Activator.PLUGIN_ID, IStatus.OK, message, null);
    }

    public static IStatus ok()
    {
        return Status.OK_STATUS;

    }

    public boolean isDecorateField()
    {
        return decorateField;
    }

    public void setDecorateField(boolean newState)
    {
        decorateField = newState;
    }

    public boolean isShowMessage()
    {
        return showMessage;

    }

    public void setShowMessage(boolean newState)
    {
        showMessage = newState;
    }
}
