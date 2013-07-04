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

package net.sf.rcpforms.common.util;

import net.sf.rcpforms.common.NullValue;

/**
 * utility for dealing with the <null> placeholder without a dependency to the model adapter layer
 * TODO: not needed anymore due to common plugin, remove
 * 
 * @deprecated
 * @author Marco van Meegen
 */
public class NullValueUtil
{

    /**
     * the class name of the NullValue class, needed to decouple wrapper from modeladapter layer
     */
    public static final String NULL_VALUE_CLASS_NAME = "net.sf.rcpforms.common.NullValue"; //$NON-NLS-1$

    /**
     * @return true if the given object is the null value place holder
     */
    public static boolean isNullValue(Object fromObject)
    {
        return fromObject == NullValue.getInstance();
    }

}
