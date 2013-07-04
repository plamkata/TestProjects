/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Remo Loetscher - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.form;

import org.eclipse.ui.IEditorInput;

/**
 * Interface to define input for Editors. Is used to fill up {@link RCPForm} with the appropriate
 * data models. ATTENTION: Interface may change. Depends on input-definition in
 * {@link RCPForm#setInput(Object)} TODO define the model type. Currently it's an object array!
 * {@link RCPForm#setInput(Object)} see tracker 152079
 * 
 * @author Remo Loetscher
 */
public interface IRCPFormEditorInput extends IEditorInput
{
    /**
     * @return input array to initialise the RCPFormPart. See {@link RCPForm#setInput(Object)}.
     *         Number of DataModels has to be equal to number of ViewModels!
     */
    public Object[] getModels();

}
