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

import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogPage;

/**
 * Original copied from {@link org.eclipse.pde.internal.ui.util.PixelConverter}
 * 
 * @author mukhinr
 */
public class PixelConverter
{
    private FontMetrics m_fontMetrics;

    public PixelConverter(Control control)
    {
        GC gc = new GC(control);
        gc.setFont(control.getFont());
        m_fontMetrics = gc.getFontMetrics();
        gc.dispose();
    }

    /**
     * @see DialogPage#convertHeightInCharsToPixels
     */
    public int convertHeightInCharsToPixels(int chars)
    {
        return Dialog.convertHeightInCharsToPixels(m_fontMetrics, chars);
    }

    /**
     * @see DialogPage#convertHorizontalDLUsToPixels
     */
    public int convertHorizontalDLUsToPixels(int dlus)
    {
        return Dialog.convertHorizontalDLUsToPixels(m_fontMetrics, dlus);
    }

    /**
     * @see DialogPage#convertVerticalDLUsToPixels
     */
    public int convertVerticalDLUsToPixels(int dlus)
    {
        return Dialog.convertVerticalDLUsToPixels(m_fontMetrics, dlus);
    }

    /**
     * @see DialogPage#convertWidthInCharsToPixels
     */
    public int convertWidthInCharsToPixels(int chars)
    {
        return Dialog.convertWidthInCharsToPixels(m_fontMetrics, chars);
    }

}
