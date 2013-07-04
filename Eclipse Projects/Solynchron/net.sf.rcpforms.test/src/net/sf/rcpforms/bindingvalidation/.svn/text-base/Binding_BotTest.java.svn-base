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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.rcpforms.test.adapter.TestModel;
import net.sf.rcpforms.test.formpart.PersonDataModel;
import net.sf.rcpforms.test.formpart.PersonFormPart;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCheckbox;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPGroup;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPRadioGroup;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;
import net.sf.swtbot.widgets.SWTBotCheckBox;
import net.sf.swtbot.widgets.SWTBotText;

import org.eclipse.core.databinding.Binding;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * UI Test for testing all bindings and conversions of a widget against supported types of model
 * values. The following is tested:
 * <ul>
 * <li>propagation of property changes to widget
 * <li>propagation of widget changes by user to model
 * <li>null value handling
 * </ul>
 * 
 * @author Marco van Meegen
 */
public class Binding_BotTest extends BindingBaseTestCase
{
    private static final String LABEL = "Control:";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public void testViewToModelText() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_Name);
        SWTBotText tester = bot.textWithLabel(LABEL);
        assertEquals(null, model.getName());
        tester.setText("Muster");
        assertEquals("Muster", model.getName());
        tester.setText("");
        assertEquals(null, model.getName());
    }

    /**
     * test if caret position changed after typing through binding problems
     */
    public void testViewToModelTextSaveCaret() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_Name);
        SWTBotText tester = bot.textWithLabel(LABEL);
        model.setName("");
        tester.setFocus();
        tester.typeText("Muster", 100);
        assertEquals("Muster", model.getName());
    }

    public void testViewToModelDate() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_BirthDate);
        SWTBotText tester = bot.textWithLabel(LABEL);

        // birthdate
        assertEquals(null, model.getBirthDate());
        tester.setText("11.05.2003");
        Date expected = dateFormat.parse("11.05.2003");
        assertEquals(expected, model.getBirthDate());
        // test minimal date input
        tester.setText("1.7.05");
        Date expected1 = dateFormat.parse("01.07.2005");
        assertEquals(expected1, model.getBirthDate());

        tester.setText("");
        assertEquals(null, model.getBirthDate());
    }

    public void testViewToModelBoolean() throws Exception
    {
        RCPCheckbox widget = new RCPCheckbox(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_OverdrawAccount);
        SWTBotCheckBox tester = bot.checkBox(LABEL);

        // Kontoueberzug
        assertEquals(null, model.getOverdrawAccount());
        boolean initial = tester.isChecked();
        tester.click();
        assertEquals(Boolean.valueOf(!initial), model.getOverdrawAccount());
        tester.click();
        assertEquals(Boolean.valueOf(initial), model.getOverdrawAccount());
    }

    public void testViewToModelInteger() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_ChildCount);
        SWTBotText textTester = bot.textWithLabel(LABEL);

        // Kinderanzahl
        assertEquals(null, model.getChildCount());
        textTester.setText("378");
        assertEquals(new Integer(378), model.getChildCount());
        textTester.setText("");
        assertEquals(null, model.getChildCount());
    }

    public void testViewToModelInt() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        Binding binding = validationManager.bindValue(widget, model, TestModel.P_Age);
        SWTBotText textTester = bot.textWithLabel(LABEL);

        // age
        assertEquals(0, model.getAge());
        textTester.setText("378");
        assertEquals(378, model.getAge());
        textTester.setText("3");
        assertEquals(3, model.getAge());
        assertValid(binding);
        textTester.setText("");
        assertEquals(3, model.getAge());
        assertInvalid(binding);

    }

    public void testViewToModelDouble() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_AccountBalance);
        SWTBotText textTester = bot.textWithLabel(LABEL);
        double value = 378.44d;
        // Kontostand
        assertEquals(null, model.getAccountBalance());
        textTester.setText(doubleText(value));
        assertEquals(value, model.getAccountBalance());
        textTester.setText("");
        assertEquals(null, model.getAccountBalance());
    }

    // -----------------------------------
    // View to Model Tests test the data binding sync from view to model
    // for the different types of fields
    // -----------------------------------

    public void testModelToViewText() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_Name);
        SWTBotText textTester = bot.textWithLabel(LABEL);
        model.setName("Lober");
        assertEquals("Lober", textTester.getText());
        model.setName(null);
        assertEquals("", textTester.getText());
    }

    public void testModelToViewDate() throws Exception
    {
        // birthdate
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_BirthDate);
        SWTBotText textTester = bot.textWithLabel(LABEL);
        Date expected = dateFormat.parse("11.05.2003");
        model.setBirthDate(expected);
        assertEquals("11.05.2003", textTester.getText());
        model.setBirthDate(null);
        assertEquals("", textTester.getText());
    }

    public void testModelToViewBoolean() throws Exception
    {
        // Kontoueberzug
        RCPCheckbox widget = new RCPCheckbox(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_OverdrawAccount);
        SWTBotCheckBox buttonTester = bot.checkBox(LABEL);

        model.setOverdrawAccount(false);
        assertFalse(buttonTester.isChecked());
        model.setOverdrawAccount(true);
        assertTrue(buttonTester.isChecked());
    }

    public void testModelToViewInteger() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_ChildCount);
        SWTBotText textTester = bot.textWithLabel(LABEL);

        // Kinderanzahl
        model.setChildCount(33);
        assertEquals("33", textTester.getText());
        model.setChildCount(-33);
        assertEquals("-33", textTester.getText());
        model.setChildCount(null);
        assertEquals("", textTester.getText());
    }

    public void testModelToViewInt() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_Age);
        SWTBotText textTester = bot.textWithLabel(LABEL);

        // age
        model.setAge(33);
        assertEquals("33", textTester.getText());
        model.setAge(-3);
        assertEquals("-3", textTester.getText());
    }

    public void testModelToViewDouble() throws Exception
    {
        // Kontostand
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        validationManager.bindValue(widget, model, TestModel.P_AccountBalance);
        SWTBotText textTester = bot.textWithLabel(LABEL);
        double value = 33.3;
        model.setAccountBalance(value);
        assertEquals(doubleText(value), textTester.getText());
        model.setAccountBalance(null);
        assertEquals("", textTester.getText());
    }

    public void testModelToViewEnumRadio() throws Exception
    {
        RCPRadioGroup group = new RCPRadioGroup(LABEL);
        builder.add(group);
        validationManager.bindValue(group, model, TestModel.P_Gender);

        // Geschlecht
        Control[] radioButtons = getRadioButtons(group);
        model.setGender(TestModel.Gender.MALE);
        assertRadioSelected(radioButtons, TestModel.Gender.MALE.toString());
        // if null, no radio should be selected
        model.setGender(null);
        assertNoRadioSelected(radioButtons);
    }

    public void testViewToModelEnumRadio() throws Exception
    {
        RCPRadioGroup group = new RCPRadioGroup(LABEL);
        builder.add(group);
        validationManager.bindValue(group, model, TestModel.P_Gender);

        Control[] radioButtons = getRadioButtons(group);
        assertEquals(3, radioButtons.length);
        bot.radio(TestModel.Gender.FEMALE.toString()).click();
        assertEquals(TestModel.Gender.FEMALE, model.getGender());
        bot.radio(TestModel.Gender.MALE.toString()).click();
        assertEquals(TestModel.Gender.MALE, model.getGender());
    }

    /** tests form part binding and rebinding */
    public void testFormPartBinding() throws Exception
    {
        PersonDataModel model = new PersonDataModel();
        PersonFormPart formPart = new PersonFormPart();
        // create form part ui in shell
        formPart.createUI(toolkit, getShell());
        // ATTENTION: DO NOT USE formPart.bind() since this is not intended to be used from clients
        // !
        assertEquals(0, validationManager.getBindingCount(null));
        assertEquals(0, validationManager.getValidatorCount(null));
        validationManager.bindPart(formPart, model);
        assertEquals(7, validationManager.getBindingCount(null));
        assertEquals(7, validationManager.getBindingCount(formPart));
        assertEquals(2, validationManager.getValidatorCount(null));
        assertEquals(2, validationManager.getValidatorCount(formPart));

        // test text binding on part
        String city = "Munich";
        model.setCity(city);
        assertEquals(city, bot.textWithLabel(PersonFormPart.CITY_LABEL).getText());

        // test date binding on part
        String dateString = "11.05.2003";
        Date expected = dateFormat.parse(dateString);
        model.setBirthdate(expected);
        assertEquals(dateString, bot.textWithLabel(PersonFormPart.BIRTHDATE_LABEL).getText());

        // now rebind to new model and test
        PersonDataModel newModel = new PersonDataModel();
        validationManager.bindPart(formPart, newModel);
        assertEquals(7, validationManager.getBindingCount(null));
        assertEquals(7, validationManager.getBindingCount(formPart));
        assertEquals(2, validationManager.getValidatorCount(null));
        assertEquals(2, validationManager.getValidatorCount(formPart));

        // test again
        String newdateString = "12.06.2002";
        Date newexpected = dateFormat.parse(newdateString);
        newModel.setBirthdate(newexpected);
        assertEquals(newdateString, bot.textWithLabel(PersonFormPart.BIRTHDATE_LABEL).getText());

        // now unbind and test
        validationManager.unbindAllParts();
        assertEquals(0, validationManager.getBindingCount(null));
        assertEquals(0, validationManager.getBindingCount(formPart));
        assertEquals(0, validationManager.getValidatorCount(null));
        assertEquals(0, validationManager.getValidatorCount(formPart));


        
    }

    /**
     * return double formatted for current locale
     * 
     * @param value
     * @return double as string
     */
    private String doubleText(double value)
    {
        String result = NumberFormat.getNumberInstance().format(value);
        return result;
    }

    private Control[] getRadioButtons(RCPGroup group)
    {
        assertNotNull(group);
        Composite swtGroup = group.getSwtGroup();
        assertNotNull(swtGroup);

        Control[] radioButtons = swtGroup.getChildren();
        return radioButtons;
    }
}
