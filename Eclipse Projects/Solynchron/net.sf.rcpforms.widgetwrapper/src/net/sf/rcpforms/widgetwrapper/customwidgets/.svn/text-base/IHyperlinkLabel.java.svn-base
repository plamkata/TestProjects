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

import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.swt.graphics.Color;

/**
 * IHyperlinkLabel<br>
 * <br>
 * Interface to a Label, which can be represented as Hyperlink or as Label. Element could be in
 * Hyperlink state or not. Hyperlink state is kind of "Master Switch" for hyperlink mode. If the
 * Label in hyperlink state then it is possible to use it as a Hyperlink or as a Label switching
 * with {@link #setHyperlinkMode(boolean)}. If this Lable not in Hyperlink state then it could be
 * used only as a simple label. Hyperlink state could be changed with
 * {@link #setUseAsHyperlink(boolean)} .
 * 
 * @author wymanns
 * @author mukhinr
 * @see BindingFactory, HyperlinkLabel
 */
public interface IHyperlinkLabel
{
    /**
     * PROP_HYPERLINKMODE is used, to set the hyperlinkMode (visualize as Hyperlink or as Label) by
     * a Binding
     */
    public final static String PROP_HYPERLINKMODE = "hyperlinkMode"; //$NON-NLS-1$

    /**
     * @return true, if this IHyperlinkLabel is visualized as a Hyperlink, false if it is visualized
     *         as a Label
     */
    boolean getHyperlinkMode();

    /**
     * Sets the visualization mode. This label must be in Hyperlink state
     * 
     * @param mode true, if this IHyperlinkLabel shall be visualized as a Hyperlink, false if it
     *            shall be visualized as a Label.
     * @see #getUseAsHyperlink()
     * @exception IllegalStateException if this lable not in Hyperlink state
     */
    void setHyperlinkMode(boolean mode) throws IllegalStateException;

    /**
     * @see AbstractHyperlink#addHyperlinkListener(IHyperlinkListener)
     */
    void addHyperlinkListener(IHyperlinkListener listener);

    /**
     * @see AbstractHyperlink#removeHyperlinkListener(IHyperlinkListener)
     */
    void removeHyperlinkListener(IHyperlinkListener listener);

    /**
     * @return The text of this IHyperlinkLabel.
     */
    String getText();

    /**
     * @param text of the IHyperlinkLabel
     */
    void setText(String text);

    /**
     * @return The tooltip text of this IHyperlinkLabel.
     */
    String getToolTipText();

    /**
     * Sets the tooltip text of this IHyperlinkLabel.
     * 
     * @param tooltipText The tooltip text.
     */
    void setToolTipText(String tooltipText);

    /**
     * Sets the visibility of this IHyperlinkLabel.
     * 
     * @param visible true, if this IHyperlinkLabel shall be visible, false otherwise.
     */
    void setVisible(boolean visible);

    void setBackground(Color color);

    /**
     * Identify whether this IHyperlinkLabel is in hyperlink state.
     * 
     * @return <code>true</code> if label in hyperlink state, <code>false</code> if not
     * @see #setHyperlinkMode(boolean)
     */
    boolean getUseAsHyperlink();

    /**
     * Set hyperlink state of this label
     * 
     * @param useAsHyperlink new hyperlink state
     * @see #setHyperlinkMode(boolean)
     */
    void setUseAsHyperlink(boolean useAsHyperlink);
}
