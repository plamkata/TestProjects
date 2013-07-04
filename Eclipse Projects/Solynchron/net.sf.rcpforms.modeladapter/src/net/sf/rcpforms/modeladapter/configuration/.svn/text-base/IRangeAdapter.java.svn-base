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

package net.sf.rcpforms.modeladapter.configuration;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * interface for associating a range of possible values with a property.
 * <p>
 * This is used by the ValidationManager during binding to combo boxes for providing a list of
 * possible values
 * 
 * @author Marco van Meegen
 */
public interface IRangeAdapter
{

    public Object getInput();

    /**
     * get a label provider which uses the given presentationIndex to create the presentation
     * 
     * @param presentationKey presentation key to use for the label, null for default presentation
     * @param nullValuePresentationKey presentation key for the null value, if no null value is
     *            used, this key is ignored
     * @return
     */
    public ILabelProvider getLabelProvider(String presentationKey, String nullValuePresentationKey);

    /**
     * retrieve a content provider for providing the list of model elements which are allowed as
     * value for the attribute. If nullValue is set, this object is added to the list of model
     * elements to represent the null value since viewers cannot handle null elements.
     * 
     * @param nullValue object representing the null value. This must be the instance of the
     *            NullValue class defined in widget wrapper layer.
     * @return content provider
     */
    public IStructuredContentProvider getContentProvider(Object nullValue);

}
