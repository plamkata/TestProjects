/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.widgetwrapper.wrapper;

/**
 * interface defining widgets which can show a null value and use different presentations for their
 * model elements and null value. This is solely used for model element based widgets which
 * represent a range of values, e.g. combo box and radio group.
 * 
 * @author Marco van Meegen
 */
public interface IPresentationConfiguration
{

    /**
     * @return Returns the presentationKey to use for the presentation of model elements, can be
     *         used for enums to define which presentation to show, e.g. short or long description
     *         or integer value.
     */
    public String getPresentationKey();

    /**
     * @return Returns the key for the representation of the null value. Can be used to display
     *         different messages in combo boxes, e.g. <no selection>, <unknown>, <undefined> if
     *         model element is null
     */
    public String getNullValuePresentationKey();

    /**
     * @return true if this IPresentationConfiguration should support a null value
     */
    public boolean hasNullValue();

}
