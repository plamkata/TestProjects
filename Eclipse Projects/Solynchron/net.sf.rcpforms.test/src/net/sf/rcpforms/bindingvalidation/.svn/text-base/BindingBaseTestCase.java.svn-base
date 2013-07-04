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

package net.sf.rcpforms.bindingvalidation;

import net.sf.rcpforms.test.RCPFormBaseTestCase;
import net.sf.rcpforms.test.adapter.TestModel;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.builder.RCPFormToolkit;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IMessageManager;

/**
 * base test case for binding and validation functionality. Sets up a validation manager, model and
 * a builder to test bindings
 * 
 * @author Marco van Meegen
 */
public abstract class BindingBaseTestCase extends RCPFormBaseTestCase
{
    protected GridBuilder builder;

    protected TestModel model;

    protected ValidationManager validationManager;

    protected RCPFormToolkit toolkit;

    /**
     * @throws java.lang.Exception
     */
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        // get shell created by super class and initiate tests
        // all tests share the same display and shell !
        Shell shell = getShell();
        toolkit = new RCPFormToolkit(getDisplay());
        builder = new GridBuilder(toolkit, shell, 2);
        validationManager = new ValidationManager(getClass().getName());
        IMessageManager mm = new DummyMessageManager();
        validationManager.initialize(mm);
        model = new TestModel();
        activateShell(shell);

    }

    @Override
    public void tearDown() throws Exception
    {
        super.tearDown();
        // ATTENTION: THIS IS IMPORTANT SINCE the SHELL IS SHARED AMONG ALL UNIT TESTS !
        // dispose bindings
        validationManager.dispose();
        // destroy widgets
        for (RCPWidget widget : builder.getCreatedControls())
        {
            widget.dispose();
        }
    }

    protected void assertValid(Binding binding)
    {
        IStatus status = (IStatus) binding.getValidationStatus().getValue();
        assertTrue(status.isOK());

    }

    protected void assertInvalid(Binding binding)
    {
        IStatus status = (IStatus) binding.getValidationStatus().getValue();
        assertFalse(status.isOK());

    }

    /**
     * @param radioButtons
     * @param text
     */
    protected void assertRadioSelected(Control[] radioButtons, String text)
    {
        for (Control control : radioButtons)
        {
            Button button = (Button) control;
            if (text.equals(button.getText()))
            {
                assertTrue("The radio button named " + text + " is not selected", button
                        .getSelection());
                return;
            }
        }
        fail("Radio button named '" + text + "' not found");

    }

    /**
     * make sure no radio button is selected
     * 
     * @param radioButtons
     * @param text
     */
    protected void assertNoRadioSelected(Control[] radioButtons)
    {
        for (Control control : radioButtons)
        {
            Button button = (Button) control;
            assertFalse("The radio button named " + button.getText() + " is selected", button
                    .getSelection());
        }
    }

}
