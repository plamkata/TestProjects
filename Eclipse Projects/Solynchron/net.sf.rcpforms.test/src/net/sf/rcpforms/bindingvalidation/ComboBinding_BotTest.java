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

import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.test.adapter.TestModel;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCombo;
import net.sf.swtbot.widgets.SWTBotCCombo;

/**
 * UI Unit Test which binds a combo and ccombo to a value and tests the binding
 * 
 * @author Marco van Meegen
 */
public class ComboBinding_BotTest extends BindingBaseTestCase
{
    private static final String CCOMBO_LABEL = "CCombo:";

    public void testViewToModelCCombo() throws Exception
    {
        RCPCombo widget = new RCPCombo(CCOMBO_LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_Gender);
        SWTBotCCombo ccomboTester = bot.ccomboBoxWithLabel(CCOMBO_LABEL);
        ccomboTester.setSelection(TestModel.Gender.MALE.toString());
        assertEquals(TestModel.Gender.MALE, model.getGender());
        ccomboTester.setSelection(TestModel.Gender.FEMALE.toString());
        assertEquals(TestModel.Gender.FEMALE, model.getGender());
    }

    // -----------------------------------
    // Model to View Tests test the data binding sync from model to view
    // for the different types of fields
    // -----------------------------------

    public void testModelToViewCCombo() throws Exception
    {
        RCPCombo widget = new RCPCombo(CCOMBO_LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_Gender);
        SWTBotCCombo ccomboTester = bot.ccomboBoxWithLabel(CCOMBO_LABEL);
        model.setGender(TestModel.Gender.MALE);
        assertEquals(0, ccomboTester.selectionIndex());
        model.setGender(TestModel.Gender.FEMALE);
        assertEquals(1, ccomboTester.selectionIndex());
    }

    public void testNullViewToModelCCombo() throws Exception
    {
        RCPCombo widget = new RCPCombo(CCOMBO_LABEL, true);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_Gender);
        SWTBotCCombo ccomboTester = bot.ccomboBoxWithLabel(CCOMBO_LABEL);
        ccomboTester.setSelection(TestModel.Gender.MALE.toString());
        assertEquals(TestModel.Gender.MALE, model.getGender());
        ccomboTester.setSelection(NullValue.getRepresentation(NullValue.GENERIC_NULL_VALUE_REPRESENTATION_EMPTY));
        assertEquals(null, model.getGender());
    }

    /**
     * tests null value handling of combo
     */
    public void testNullModelToViewCCombo() throws Exception
    {
        RCPCombo widget = new RCPCombo(CCOMBO_LABEL, true); // combo with null value
        builder.add(widget);
        SWTBotCCombo ccomboTester = bot.ccomboBoxWithLabel(CCOMBO_LABEL);
        validationManager.bindValue(widget, model, TestModel.P_Gender);
        model.setGender(TestModel.Gender.MALE);
        assertEquals(1, ccomboTester.selectionIndex()); // now 1, since null value is first
        model.setGender(null);
        assertEquals(0, ccomboTester.selectionIndex());
        // null value representation should be the generic one
        assertEquals(NullValue.GENERIC_NULL_VALUE_REPRESENTATION_EMPTY, ccomboTester.getText());
    }

    /**
     * tests handling of custom null value representation
     */
    public void testNullValueRepresentationCombo() throws Exception
    {
        final String key = "de.mvmsoft.testrepresentation";
        final String REPRESENTATION = "<Please select an entry>";
        final String ANOTHER_REPRESENTATION = "----";
        RCPCombo widget = new RCPCombo(CCOMBO_LABEL, true); // combo with null value
        // tell combo to use different presentation than default
        widget.setNullValuePresentationKey(key);
        // register the individual representation for this key
        NullValue.registerRepresentations(key, REPRESENTATION);

        builder.add(widget);
        SWTBotCCombo ccomboTester = bot.ccomboBoxWithLabel(CCOMBO_LABEL);
        validationManager.bindValue(widget, model, TestModel.P_Gender);
        model.setGender(null);
        assertEquals(0, ccomboTester.selectionIndex());
        // null value representation should be the generic one
        assertEquals(REPRESENTATION, ccomboTester.getText());

        // should dynamically readjust but only after refreshing the combo viewer
        NullValue.registerRepresentations(key, ANOTHER_REPRESENTATION);
        widget.getViewer().refresh();
        assertEquals(ANOTHER_REPRESENTATION, ccomboTester.getText());

    }
}
