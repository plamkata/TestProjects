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

/**
 * marker interface for property chaining. Used internally to encapsulate all types of
 * meta-information which lead from a model object to a property of the model or of a nested model.
 * 
 * @author Marco van Meegen
 */
public interface IPropertyChain
{

    /**
     * gets meta information for the model object where the properties are living
     */
    public Object getModelMeta();

    /**
     * retrieves the value of the property in the given model
     * 
     * @param model
     */
    public Object getValue(Object model);

    /**
     * sets the value of the property in the given model.
     * 
     * @param model
     * @param value
     */
    public void setValue(Object model, Object value);

    /**
     * @return type of the attribute addressed by this chain, this is a meta concept and is used by
     *         databinding to determine converters; usually it will be the Class of the attribute,
     *         e.g. Integer.class
     */
    public Object getType();

}
