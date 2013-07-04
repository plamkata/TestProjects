/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Remo Loetscher - initial API and implementation
 *
 ******************************************************************************/

package net.sf.rcpforms.widgetwrapper.builder;

import net.sf.rcpforms.widgetwrapper.wrapper.RCPLabeledControl;

import org.eclipse.jface.resource.JFaceResources;

/**
 * interface defines all color constants used by the widget wrappers. Colors were meant to be placed
 * in the {@link JFaceResources#getColorRegistry()}. Therefore they can be replaced either using
 * this ColorRegistry directly or the (eclipse) theme extension point.
 * 
 * @author Remo Loetscher
 */
public interface IColorConstants
{
    public static final String PREFIX = "net.sf.rcpforms."; //$NON-NLS-1$

    /**
     * default background color for all widgets
     */
    public static final String DEFAULT_BG = IColorConstants.PREFIX + "defaultdBackground"; //$NON-NLS-1$

    /**
     * default foreground color for all widgets
     */
    public static final String DEFAULT_FG = IColorConstants.PREFIX + "defaultdForground"; //$NON-NLS-1$

    /**
     * background color for widgets with mandatory state
     */
    public static final String MANDATORY_BG = IColorConstants.PREFIX + "mandatoryBackground"; //$NON-NLS-1$

    /**
     * background color for widgets with "nice2have" state
     */
    public static final String MANDATORYM2_BG = IColorConstants.PREFIX + "mandatoryM2Background"; //$NON-NLS-1$

    /**
     * background color for widgets in error state
     */
    public static final String ERROR_BG = IColorConstants.PREFIX + "errorBackground"; //$NON-NLS-1$

    /**
     * background color for widgets in warning state
     */
    public static final String WARNING_BG = IColorConstants.PREFIX + "warningBackground"; //$NON-NLS-1$

    /**
     * default foreground color for all disabled widgets
     */
    public static final String DISABLED_FG = IColorConstants.PREFIX + "disabledForeground"; //$NON-NLS-1$

    /**
     * default background color for all disabled widgets
     */
    public static final String DISABLED_BG = IColorConstants.PREFIX + "disabledBackground"; //$NON-NLS-1$

    /**
     * default background color for prescribed terminology.
     */
    public static final String TERMINOLOGY_BG = IColorConstants.PREFIX + "terminologyBackground"; //$NON-NLS-1$

    /**
     * efault foreground color for all labels used e.g. in {@link RCPLabeledControl}
     */
    public static final String LABEL_FG = IColorConstants.PREFIX + "labelForeground"; //$NON-NLS-1$

}
