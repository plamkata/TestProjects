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

package net.sf.rcpforms.widgetwrapper.util;

/**
 * central debug flags for easier form layout and binding debugging.
 * 
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class Debug
{
    private static final String DEBUG_LAYOUT = "net.sf.rcpforms.debug.layout"; //$NON-NLS-1$

    private static final String DEBUG_BINDING = "net.sf.rcpforms.debug.bindings"; //$NON-NLS-1$

    /**
     * @return true if layout debugging is activated. All layout information is set as tooltip to
     *         controls by builder
     */
    public static boolean layout()
    {
        return Boolean.getBoolean(DEBUG_LAYOUT);
    }

    /**
     * @return true if binding debugging is activated. Bound controls are flagged with a "bind"
     *         decoration and a tooltip is set to the decoration indicating how it was bound.
     */
    public static boolean binding()
    {
        return Boolean.getBoolean(DEBUG_BINDING);
    }

}
