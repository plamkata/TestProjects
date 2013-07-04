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

import java.util.ArrayList;
import java.util.List;

import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget;

import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * Utility class to check widget validity
 * 
 * @author mukhinr
 * @author dietisheima
 */
public class WidgetUtil
{

    /**
     * Instantiates a new widget utils.
     */
    private WidgetUtil()
    {
    }

    /**
     * returns whether the widget passed in as parameter may be used. By used is meant that it is an
     * existing instance (not null reference) and it is not disposed
     * 
     * @param widget the widget the be checked
     * @return true, if the widget may be used (reference is not null and widget is not disposed)
     */
    public static final boolean isValid(final Widget widget)
    {
        return null != widget && !widget.isDisposed();
    }

    /**
     * Ensure that the widget may be used. Throws a {@link IllegalStateException} if the widget may
     * not be used. This checks if the widget is disposed and if the current thread is the display
     * thread.
     * 
     * @param widget the widget
     * @see #canBeUsed
     */
    public static final void ensureValid(final Widget widget)
    {
        if (!isValid(widget))
        {
            throw new IllegalStateException("widget is null or disposed!"); //$NON-NLS-1$
        }
    }

    /**
     * returns whether the resource passed in as parameter may be used. By used is meant that it is
     * an existing instance (not null reference) and it is not disposed
     * 
     * @param resource the resource the be checked
     * @return true, if the resource may be used (reference is not null and resource is not
     *         disposed)
     */
    public static final boolean isValid(final Resource resource)
    {
        return null != resource && !resource.isDisposed();
    }

    /**
     * Ensure that the resource may be used. Throws a {@link IllegalStateException} if the resource
     * may not be used
     * 
     * @param resource the resource
     * @see #canBeUsed
     */
    public static final void ensureValid(final Resource resource)
    {
        if (!isValid(resource))
        {
            throw new IllegalStateException("Resource is null or disposed!"); //$NON-NLS-1$
        }
    }

    /**
     * Sets the enabled disabled look and feel.
     * 
     * @param hyperlink the new enabled disabled look and feel
     */
    public static void setEnabledDisabledLookAndFeel(final Hyperlink hyperlink)
    {
        if (isValid(hyperlink))
        {
            final Display display = hyperlink.getDisplay();

            Color color;
            if (hyperlink.isEnabled())
            {
                color = JFaceColors.getHyperlinkText(display);
            }
            else
            {
                color = display.getSystemColor(SWT.COLOR_DARK_GRAY);
            }
            hyperlink.setUnderlined(hyperlink.isEnabled());
            hyperlink.setForeground(color);
        }
    }

    /**
     * returns whether the widget wrapper is valid.
     * 
     * @param wrapper wrapper to be checked
     * @return true, if it is not null, the corresponding swt contrl is not null and not disposed
     */
    public static final boolean isValid(final RCPWidget wrapper)
    {
        return null != wrapper && null != wrapper.getSWTWidget()
                && !wrapper.getSWTWidget().isDisposed();
    }

    /**
     * Ensure that the widget wrapper is valid, i.e. the wrapped swt control is created and not
     * disposed. Throws a {@link IllegalStateException} if the widget wrapper is invalid.
     * 
     * @param wrapper the widget wrapper to check
     * @see #canBeUsed
     */
    public static void ensureValid(RCPWidget wrapper)
    {
        if (!isValid(wrapper))
        {
            throw new IllegalStateException("Wrapper is null or disposed!"); //$NON-NLS-1$
        }
    }

    /**
     * filter the list of controls from the widgets
     * 
     * @param createdControls
     * @return an unmodifiable list of the widgets which are controls; this includes composites but
     *         not the children of compounds
     */
    public static List<RCPControl> getControls(List<RCPWidget> createdControls)
    {
        List<RCPControl> result = new ArrayList<RCPControl>();
        for (RCPWidget widget : createdControls)
        {
            if (widget instanceof RCPControl)
            {
                result.add((RCPControl) widget);
            }
        }
        return result;
    }
}
