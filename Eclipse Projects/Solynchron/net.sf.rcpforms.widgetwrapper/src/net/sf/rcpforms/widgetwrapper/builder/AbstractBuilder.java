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
// Created on 27.04.2008

package net.sf.rcpforms.widgetwrapper.builder;

import java.util.ArrayList;
import java.util.List;

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.util.Debug;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPComposite;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCompound;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget;

import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * base class for all RCP builders. Contains layout-independent logic for builders:
 * <ul>
 * <li>control life cycle
 * <li>label creation for labeled controls
 * <li>viewer configuration
 * <li>parent relationship
 * <li>maintains list of all created controls
 * </ul>
 * 
 * @author Marco van Meegen
 */
public class AbstractBuilder
{

    protected RCPComposite currentParent;

    /** list holding only "interesting" controls, no labels and no filler */
    protected List<RCPWidget> createdControls = new ArrayList<RCPWidget>();

    protected FormToolkit formToolkit;

    protected AbstractBuilder(FormToolkit formToolkit)
    {
        Validate.notNull(formToolkit);
        this.formToolkit = formToolkit;
    }

    /**
     * create control, its label control and maintain all relationships
     * 
     * @param control control to create
     */
    protected void createControl(RCPControl control)
    {
        Validate.notNull(formToolkit);
        control.setRcpParent(currentParent);
        control.createUI(formToolkit);
        createdControls.add(control);
    }

    /**
     * true if the builder should automatically add a layout to the given control. For simple
     * controls it always adds a layout specified by the add methods defined for the builder.
     * <p>
     * For compound controls it applies auto layout for non extensible components, which are
     * prefabricated arrangements of controls which clients can use without any configuration. This
     * is opposite to extensible components where clients will add children themselves and thus take
     * over layout, binding and creation of children.
     * 
     * @param control control to check
     * @return true, if builder should manage component automatically
     */
    protected boolean isAutoLayoutControl(RCPControl control)
    {
        return !control.isContainer() || !((RCPCompound) control).isExtensible();
    }

    /**
     * apply the default layout for the given control in the context of the given parent
     * 
     * @param control
     */
    protected void applyLayout(RCPControl parent, RCPControl control)
    {

    }

    /**
     * if debugging is enabled (see {@link Debug}), debug info is set to the widget as tooltip
     * and/or decoration. Can be overridden by subclass to add specific debug info
     * 
     * @param widget widget to apply debug info to
     */
    protected void setDebugInfo(RCPWidget widget)
    {
        if (Debug.layout())
        {
            String debugInfo = ""; //$NON-NLS-1$
            if (widget instanceof RCPControl)
            {
                if (widget instanceof RCPComposite)
                {
                    Layout layout = ((RCPComposite) widget).getClientComposite().getLayout();
                    if (layout != null)
                    {
                        debugInfo += widget.getId() + ".layout = " + layout.toString() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
                RCPControl control = (RCPControl) widget;
                Object layoutData = control.getSWTControl().getLayoutData();
                if (layoutData != null)
                {
                    debugInfo += widget.getId() + ".layoutData = " + layoutData.toString() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
                }
                control.getSWTControl().setToolTipText(debugInfo);
            }
        }
    }
}
