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

package net.sf.rcpforms.widgetwrapper.wrapper;

import net.sf.rcpforms.bindingvalidation.DummyMessageManager;
import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.test.RCPFormBaseTestCase;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.builder.RCPFormToolkit;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;

/**
 * tests widget state behavior (editable, enabled, visible ...) and binding to a Boolean and deco
 * behavior and binding to Text/boolean
 * 
 * @author Marco van Meegen
 */
public class RCPWidgetState_BotTest extends RCPFormBaseTestCase
{

    private GridBuilder builder;

    private ValidationManager validationManager;

    private RCPText textWidget;

    private String TEXT_LABEL = "Text:";

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        // get shell created by super class and initiate tests
        // all tests share the same display and shell !
        Shell shell = getShell();
        builder = new GridBuilder(new RCPFormToolkit(getDisplay()), shell, 2);
        validationManager = new ValidationManager(getClass().getName());
        validationManager.initialize(new DummyMessageManager());
        activateShell(shell);
    }

    /**
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        if (validationManager != null)
        {
            // TODO: should dispose bindings m_manager.dispose();
        }
    }

    /**
     * test if initial state of widget is visible, enabled, editable
     */
    public void testInitialState()
    {
        textWidget = new RCPText(TEXT_LABEL);
        builder.add(textWidget);;
        assertNotNull(textWidget.getSWTControl());
        assertWidgetState(textWidget, true, true, true);
    }

    /**
     * test if state set on wrapper before construction of swt control is passed
     */
    public void testPreCreateState()
    {
        textWidget = new RCPText(TEXT_LABEL);
        textWidget.setState(EControlState.ENABLED, false);
        textWidget.setState(EControlState.VISIBLE, false);
        builder.add(textWidget);;
        assertNotNull(textWidget.getSWTControl());
        assertWidgetState(textWidget, false, false, true);
    }

    public void testModifyState()
    {
        textWidget = new RCPText(TEXT_LABEL);
        builder.add(textWidget);
        assertNotNull(textWidget.getSWTControl());
        assertWidgetState(textWidget, true, true, true);

        // enable
        textWidget.setState(EControlState.ENABLED, false);
        assertWidgetState(textWidget, true, false, true);
        textWidget.setState(EControlState.ENABLED, true);
        assertWidgetState(textWidget, true, true, true);

        // visible
        textWidget.setState(EControlState.VISIBLE, false);
        assertWidgetState(textWidget, false, true, true);
        textWidget.setState(EControlState.VISIBLE, true);
        assertWidgetState(textWidget, true, true, true);

        // editable
        textWidget.setState(EControlState.READONLY, true);
        assertWidgetState(textWidget, true, true, false);
        textWidget.setState(EControlState.READONLY, false);
        assertWidgetState(textWidget, true, true, true);
    }

    public void testBindEnabled()
    {
        textWidget = new RCPText(TEXT_LABEL);
        WritableValue state = new WritableValue(false, Boolean.class);
        builder.add(textWidget);

        validationManager.bindState(textWidget, EControlState.ENABLED, state);
        state.setValue(Boolean.TRUE);
        assertWidgetState(textWidget, true, true, true);
        state.setValue(Boolean.FALSE);
        assertWidgetState(textWidget, true, false, true);
    }

    public void testBindVisible()
    {
        textWidget = new RCPText(TEXT_LABEL);
        WritableValue state = new WritableValue(false, Boolean.class);
        builder.add(textWidget);

        validationManager.bindState(textWidget, EControlState.VISIBLE, state);
        state.setValue(Boolean.TRUE);
        assertWidgetState(textWidget, true, true, true);
        state.setValue(Boolean.FALSE);
        assertWidgetState(textWidget, false, true, true);
    }

    public void testBindEditable()
    {
        textWidget = new RCPText(TEXT_LABEL);
        WritableValue state = new WritableValue(false, Boolean.class);
        builder.add(textWidget);

        validationManager.bindState(textWidget, EControlState.READONLY, state);
        state.setValue(Boolean.FALSE);
        assertWidgetState(textWidget, true, true, true);
        state.setValue(Boolean.TRUE);
        assertWidgetState(textWidget, true, true, false);
    }

    public void testBindRequired() throws Exception
    {
        textWidget = new RCPText(TEXT_LABEL);
        WritableValue state = new WritableValue(false, Boolean.class);
        builder.add(textWidget);
        Color usualBackground = textWidget.getSWTControl().getBackground();

        validationManager.bindState(textWidget, EControlState.MANDATORY, state);
        state.setValue(Boolean.TRUE);
        assertRequiredDeco(textWidget.getRCPControl(), true);
        assertFalse(colorsEqual(usualBackground, textWidget.getSWTControl().getBackground()));
        state.setValue(Boolean.FALSE);
        assertRequiredDeco(textWidget.getRCPControl(), false);
        assertTrue(colorsEqual(usualBackground, textWidget.getSWTControl().getBackground()));
    }

    public void testBindShould() throws Exception
    {
        textWidget = new RCPText(TEXT_LABEL);
        builder.add(textWidget);
        Color usualBackground = textWidget.getSWTControl().getBackground();
        WritableValue state = new WritableValue(false, Boolean.class);
        validationManager.bindState(textWidget, EControlState.RECOMMENDED, state);
        state.setValue(Boolean.TRUE);
        assertFalse(colorsEqual(usualBackground, textWidget.getSWTControl().getBackground()));
        assertRequiredDeco(textWidget, false);

        state.setValue(Boolean.FALSE);
        assertTrue(colorsEqual(usualBackground, textWidget.getSWTControl().getBackground()));
        assertRequiredDeco(textWidget, false);
    }

    /**
     * compares two colors if they represent the same color
     * 
     * @param color1
     * @param color2
     * @return true, if colors are equal
     */
    private boolean colorsEqual(Color color1, Color color2)
    {
        return color1.getRGB().equals(color2.getRGB());
    }

    private void assertWidgetState(RCPText widget, boolean visible, boolean enabled,
                                   boolean editable)
    {
        assertEquals(visible, widget.getSWTText().isVisible());
        assertEquals(enabled, widget.getSWTText().isEnabled());
        assertEquals(editable, widget.getSWTText().getEditable());
    }

    private void assertRequiredDeco(RCPControl widget, boolean visible) throws Exception
    {
        assertEquals(visible, widget.isRequiredDecorationVisible());

    }
}
