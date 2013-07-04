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

package net.sf.rcpforms.widgetwrapper.customwidgets;

import java.util.logging.Logger;

import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * a label which can be changed to a hyperlink
 * 
 * @author schaefersn
 * @author mukhinr
 */
public class HyperlinkLabel extends Hyperlink implements IHyperlinkLabel
{
    private static final Logger LOG = Logger.getLogger(HyperlinkLabel.class.getName());

    private static final Color HYPERLINKCOLOR = JFaceColors.getHyperlinkText(Display.getCurrent());

    private static Color m_enabledLabelColor = null;

    private static Color m_disabledLabelColor = null;

    private boolean m_hyperlinkMode = true; // Bestimmt, ob das Control als Label (false) oder als

    // Hyperlink (true) dargestellt wird

    private boolean m_useAsHyperlink = false; // define "Master Switch" for Hyperlink State

    private boolean m_enabled = true;

    public HyperlinkLabel(final Composite parent, int style)
    {
        super(parent, style);

        // FocusListener hinzufügen, um den Focus im Labelmode nicht auf dem Label zu halten:
        addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (!m_hyperlinkMode)
                    parent.setFocus(); // Bewirkt eine Weitergabe des Focus auf das nächste Control
            }
        });
    }

    public boolean getHyperlinkMode()
    {
        return m_hyperlinkMode;
    }

    /**
     * Ermöglicht den Wechsel zwischen den Erscheinungsformen Hyperlink und Label
     * 
     * @param enabled
     */
    public void setHyperlinkMode(boolean enabled)
    {
        if (enabled && !getUseAsHyperlink())
        {
            // if you see this exception: probably it is logical error in client code
            throw new IllegalStateException(
                    "HyperlinkLabel is not in Hyperlink State, use setUseAsHyperlink(true) first"); //$NON-NLS-1$
        }

        setUnderlined(enabled);
        if (enabled)
        {
            setCursor(new Cursor(getDisplay(), SWT.CURSOR_HAND));
            setForeground(HYPERLINKCOLOR);
        }
        else
        {
            setCursor(new Cursor(getDisplay(), SWT.CURSOR_ARROW));
            if (m_enabled)
            {
                setForeground(m_enabledLabelColor);
            }
            else
            {
                setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
            }
        }
        m_hyperlinkMode = enabled;
    }

    public void setLabelColors(Color enabledColor, Color disabledColor)
    {
        m_enabledLabelColor = enabledColor;
        m_disabledLabelColor = disabledColor;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        m_enabled = enabled;
        if (m_hyperlinkMode)
        {
            super.setEnabled(enabled);
        }
        if (enabled)
        {
            setForeground(m_enabledLabelColor);
        }
        else
        {
            setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
        }
    }

    @Override
    protected void handleActivate(Event e)
    {
        if (m_hyperlinkMode)
        {
            super.handleActivate(e);
        }
    }

    @Override
    protected void handleExit(Event e)
    {
        if (m_hyperlinkMode)
        {
            super.handleExit(e);
        }
    }

    @Override
    protected void handleEnter(Event e)
    {
        if (m_hyperlinkMode)
        {
            super.handleEnter(e);
        }
    }

    @Override
    public String getToolTipText()
    {
        return super.getToolTipText();
    }

    @Override
    public void setToolTipText(String tooltipText)
    {
        super.setToolTipText(tooltipText);
    }

    @Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
    }

    public boolean getUseAsHyperlink()
    {
        return m_useAsHyperlink;
    }

    /* (non-Javadoc)
     * @see ch.post.pf.zvis.team.ui.factories.IHyperlinkLabel#setUseAsHyperlink(boolean)
     */
    public void setUseAsHyperlink(boolean useAsHyperlink)
    {
        m_useAsHyperlink = useAsHyperlink;
    }
}
