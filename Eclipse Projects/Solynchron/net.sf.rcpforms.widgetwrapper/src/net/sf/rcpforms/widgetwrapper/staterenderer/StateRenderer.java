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

package net.sf.rcpforms.widgetwrapper.staterenderer;

import java.util.HashMap;
import java.util.Map;

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.util.WidgetUtil;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * RCPControls delegate state rendering (e.g. mandatory, readonly..) to this class. State rendering
 * can be customized by installing a subclass of this state renderer for a specific widget class;
 * all subclasses of this widget class use the same state renderer. Currently specific state
 * renderers are registered for RCPControl, RCPIconText and RCPSection, all others are inherited.
 * <p>
 * TODO: refactor state rendering to use formtoolkit and keys for colors etc.<BR>
 * -> see tracker 152103
 * 
 * @author Marco van Meegen
 */
public class StateRenderer
{
    private static Map<String, Color> colorMap = new HashMap<String, Color>();

    private static Map<Class<? extends RCPControl>, StateRenderer> instanceMap;

    public static final RGB standardRGB_BG = new RGB(255, 255, 255);

    public static final RGB requiredRGB_BG = new RGB(255, 255, 225);

    public static final RGB disabledRGB_BG = new RGB(240, 240, 240);

    static
    {
        instanceMap = new HashMap<Class<? extends RCPControl>, StateRenderer>();
        instanceMap.put(RCPControl.class, new StateRenderer());
        instanceMap.put(RCPSection.class, new StateRenderer()
        {
            @Override
            protected void setStateBackground(RCPControl section)
            {
                // ignore state backgrounds on a section
            }

        });
    }

    /**
     * @return the system wide default state renderer to use for the given control class. If no
     *         renderer is registered for this class, the next superclass is tested until one is
     *         found; this StateRenderer is registered for RCPControl, thus always one is found.
     */
    public static StateRenderer getInstance(Class<? extends RCPControl> controlClass)
    {
        Class<?> currentClass = controlClass;
        while (currentClass != null && !instanceMap.containsKey(currentClass))
        {
            currentClass = currentClass.getSuperclass();
        }
        StateRenderer result = instanceMap.get(currentClass);
        Validate.notNull(result,
                "Internal Error: No State Renderer is registered for the class or any superclass of " //$NON-NLS-1$
                        + controlClass.getName());
        return result;
    }

    /**
     * registers a new state renderer for the given control class and all superclasses.
     * 
     * @param controlClass control class to register state renderer for
     * @param renderer renderer to use, maybe null in which case only a renderer entry is removed if
     *            existing
     * @return old renderer or null if none was directly registered for this class
     */
    public static StateRenderer registerStateRenderer(Class<? extends RCPControl> controlClass,
                                                      StateRenderer renderer)
    {
        StateRenderer old;
        if (renderer != null)
        {
            old = instanceMap.put(controlClass, renderer);
        }
        else
        {
            old = instanceMap.remove(controlClass);
        }
        return old;
    }

    /**
     * create a control decoration with the clipping parent got mainly right. This does not work in
     * all cases, see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=172294
     * 
     * @param position decoration position e.g. SWT.LEFT|SWT.TOP
     * @return control decoration
     */
    protected ControlDecoration createControlDecoration(Control swtControl, int position)
    {
        Control parent = swtControl;
        // walk up parent chain to find a scrolled form
        while (parent != null && !(parent instanceof ScrolledForm))
        {
            parent = parent.getParent();
        }
        Composite clippingParent = null;
        if (parent instanceof ScrolledForm)
        {
            clippingParent = ((ScrolledForm) parent).getBody();
        }

        ControlDecoration result = new ControlDecoration(swtControl, position, clippingParent);
        return result;
    }

    protected ControlDecoration initOtherDeco(Control control, int oDecoPos)
    {
        FieldDecoration deco = FieldDecorationRegistry.getDefault().getFieldDecoration(
                FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
        ControlDecoration result = createControlDecoration(control, oDecoPos);
        result.setImage(deco.getImage());
        result.setDescriptionText(null);
        result.setMarginWidth(5);
        result.setShowOnlyOnFocus(false);
        result.show();
        return result;
    }

    public void updateState(RCPControl rcpControl, int state)
    {
        Validate.notNull(rcpControl);
        Control swtControl = rcpControl.getTypedWidget();
        if (WidgetUtil.isValid(rcpControl))
        {
            swtControl.setVisible(rcpControl.getState(EControlState.VISIBLE));
            if (!rcpControl.isKeepSpaceIfInvisible()
                    && swtControl.getLayoutData() instanceof GridData)
            {
                // exclude/included in layout
                GridData data = (GridData) swtControl.getLayoutData();
                data.exclude = !rcpControl.getState(EControlState.VISIBLE);
                swtControl.setLayoutData(data);
                // if labeled, remove label too
                rcpControl.reflowForm();
            }

            // important ! some controls e.g. button render readonly the same as disabled !
            rcpControl.getFormToolkitEx().setReadonly(swtControl,
                    rcpControl.getState(EControlState.READONLY));
            swtControl.setEnabled(rcpControl.isChainEnabled());

            if (rcpControl.isChainEnabled() && rcpControl.getState(EControlState.MANDATORY)
                    && !rcpControl.getState(EControlState.READONLY)
                    && rcpControl.requiredDecoration == null)
            {
                // show required decoration
                rcpControl.requiredDecoration = createControlDecoration(swtControl, SWT.LEFT
                        | SWT.TOP);
                rcpControl.requiredDecoration.setImage(FieldDecorationRegistry.getDefault()
                        .getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
            }
            else if ((!rcpControl.isChainEnabled() || !rcpControl.hasState(EControlState.MANDATORY) || rcpControl
                    .getState(EControlState.READONLY))
                    && rcpControl.requiredDecoration != null)
            {
                // hide required decoration
                rcpControl.requiredDecoration.hide();
                rcpControl.requiredDecoration.dispose();
                rcpControl.requiredDecoration = null;
            }

            if (rcpControl.hasState(EControlState.OTHER) && rcpControl.isChainVisible())
            {
                // configure other state.
                if (rcpControl.otherDecoration == null)
                    rcpControl.otherDecoration = initOtherDeco(swtControl, SWT.LEFT | SWT.CENTER);

            }
            else if (!rcpControl.hasState(EControlState.OTHER)
                    && rcpControl.otherDecoration != null)
            {
                // hide other decoration
                rcpControl.otherDecoration.hide();
                rcpControl.otherDecoration.dispose();
                rcpControl.otherDecoration = null;
            }
            setStateBackground(rcpControl);
        }
    }

    /**
     * sets the background defined by the widget state; subclasses may override to not use state
     * background colors
     */
    protected void setStateBackground(RCPControl rcpControl)
    {
        Color color = null;
        if (rcpControl.isChainEnabled())
        {
            if ((rcpControl.getState(EControlState.RECOMMENDED) || rcpControl
                    .getState(EControlState.MANDATORY))
                    && !rcpControl.getState(EControlState.READONLY))
            {
                color = getColor(requiredRGB_BG);
            }
            else
            {
                // set parent background
                color = rcpControl.getSWTParent().getBackground();
            }
        }
        else
        {
            color = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
        }
        if (color != null)
        {
            rcpControl.getSWTControl().setBackground(color);
        }
    }

    /**
     * get color with given rgb value, create if not existing
     * 
     * @param descriptor rgb values needed
     * @return color, cached or newly created; do not dispose !
     */
    public static Color getColor(RGB descriptor)
    {
        // TODO: delegate to form colors and color key
        String key = descriptor.toString();
        if (!colorMap.containsKey(key))
        {
            Color color = new Color(Display.getCurrent(), descriptor);
            colorMap.put(key, color);
        }
        return colorMap.get(key);
    }

}
