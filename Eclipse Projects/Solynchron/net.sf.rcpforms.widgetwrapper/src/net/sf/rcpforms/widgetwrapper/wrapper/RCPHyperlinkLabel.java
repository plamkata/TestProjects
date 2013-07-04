/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Remo Loetscher - initial API and implementation
 *
 ******************************************************************************
 */

package net.sf.rcpforms.widgetwrapper.wrapper;

import java.util.HashSet;
import java.util.Set;

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.builder.IColorConstants;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * creates a hyperlink as a label. Since label/widget pairs are extremely common in user interfaces, for all
 * controls there is a "Simple" class and a normal class derived from {@link RCPLabeledControl}
 * which is composed of the simple widget and a label.
 * Per default the hyperlink functionality is disabled. Hyperlink is enabled even after a 
 * IHyperlinkListener is added using {@link RCPHyperlinkLabel#addHyperlinkListener()}
 * <p>
 * 
 * @author Remo Loetscher
 */
/**
 * @author loetscherr
 */
public class RCPHyperlinkLabel extends RCPControl
{
    private Set<IHyperlinkListener> listenerSet = null;

    public RCPHyperlinkLabel(String label)
    {
        this(label, SWT.DEFAULT);
    }

    public RCPHyperlinkLabel(String label, int style)
    {
        super(label == null ? "" : label, style);
    }

    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Hyperlink result = formToolkit.createHyperlink(getSWTParent(), getLabel(), getStyle()
                | SWT.FLAT | SWT.WRAP);
        return result;
    }

    public final Hyperlink getSWTHyperlink()
    {
        return getTypedWidget();
    }

    @Override
    public void createUI(FormToolkit formToolkit)
    {
        super.createUI(formToolkit);
        getSWTHyperlink().setText(getLabel());
        // add focus listener to hand over focus to the next control
        // getSWTHyperlink().addFocusListener(new FocusAdapter() {
        // public void focusGained(FocusEvent e) {
        // getSWTHyperlink().getParent().setFocus();
        // }
        // });
        // disable hyperlink functionality
        getSWTHyperlink().setUnderlined(false);
        getSWTHyperlink().setEnabled(false);
    }

    /**
     * Add a hyperlinklistener to the hyperlink. if no listener was registered yet, hyperlink
     * functionality (underlined and cursor switch to "hand") will be enabled automatically. adding
     * the same listener multiple times to the same hyperlinklabel will not work. in that case only
     * one listener will be added. NOTE: If you register a listener not using this function but the
     * swt-control: you have to take care yourself about the hyperlink widget state (underlined
     * and/or disabled)
     * 
     * @param listener HyperlinkListener to register; not null
     */
    public void addHyperlinkListener(IHyperlinkListener listener)
    {
        Validate.notNull(listener);

        // initialize lazy to save memory
        if (listenerSet == null)
            listenerSet = new HashSet<IHyperlinkListener>();
        if (listener == null)
            return;
        if (getSWTHyperlink() != null && !listenerSet.contains(listener))
        {
            getSWTHyperlink().addHyperlinkListener(listener);
        }
        this.listenerSet.add(listener);
        // enable hyperlink functionality
        this.updateHyperlinkState();
    }

    /**
     * Remove hyperlinklistener from the hyperlink. if the last listener was deregistered, hyperlink
     * functionality (underlined and cursor switch to "hand") will be automatically disabled NOTE:
     * only use this function if listener was added using
     * {@link RCPHyperlinkLabel#addHyperlinkListener(IHyperlinkListener)}
     * 
     * @param listener HyperlinkListener to remove; not null
     */
    public void removeHyperlinkListener(IHyperlinkListener listener)
    {
        Validate.notNull(listener);
        if (getSWTHyperlink() != null && listener != null && this.listenerSet != null
                && this.listenerSet.contains(listener))
        {
            getSWTHyperlink().removeHyperlinkListener(listener);
            this.listenerSet.remove(listener);
            this.updateHyperlinkState();
        }
    }

    private void updateHyperlinkState()
    {
        if (getSWTHyperlink() != null)
        {
            if (listenerSet != null)
            {
                // enable hyperlink only if at least one listener is registered
                // and control (hierarchy) is enabled
                boolean enableHyperlink = !listenerSet.isEmpty()
                        && (this.getState(EControlState.ENABLED) && isChainEnabled());
                getSWTHyperlink().setUnderlined(enableHyperlink);
                getSWTHyperlink().setEnabled(enableHyperlink);

            }
            else
            {
                // disable hyperlink functionality
                getSWTHyperlink().setUnderlined(false);
                getSWTHyperlink().setEnabled(false);
            }

            if (this.hasState(EControlState.ENABLED) && this.isChainEnabled())
            {
                getSWTHyperlink().setForeground(
                        JFaceResources.getColorRegistry().get(IColorConstants.LABEL_FG));
            }
            else
            {
                getSWTHyperlink().setForeground(
                        JFaceResources.getColorRegistry().get(IColorConstants.DISABLED_FG));
            }
        }

    }

    protected void updateState()
    {
        super.updateState();
        this.updateHyperlinkState();
    }
}
