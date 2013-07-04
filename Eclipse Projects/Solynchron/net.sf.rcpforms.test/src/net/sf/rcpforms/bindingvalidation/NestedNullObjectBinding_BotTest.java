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

import net.sf.rcpforms.test.adapter.AddressModel;
import net.sf.rcpforms.test.adapter.TestModel;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;
import net.sf.swtbot.widgets.SWTBotText;

/**
 * UI Test for testing bindings of a widget against supported types of a not initialized nested model
 * value. The following is tested:
 * <ul>
 * <li>propagation of property changes to widget
 * <li>propagation of widget changes by user to model
 * <li>null value handling for nested object with properties
 * </ul>
 * 
 * @author Remo Loetscher
 */
public class NestedNullObjectBinding_BotTest extends BindingBaseTestCase
{
    private static final String LABEL = "Control:";

    
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        //remove nested address
        model.setAddress(null);
    }


    public void testViewToModelText() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        //test binding again nested property which is not yet set (equals null)
        validationManager.bindValue(widget, model, TestModel.P_Address, AddressModel.P_ZipCode);
        SWTBotText tester = bot.textWithLabel(LABEL);
        assertEquals(null, model.getAddress());
        //set new address-object
        model.setAddress(new AddressModel());
        assertNotNull(model.getAddress());
        tester.setText("12345");
        assertEquals(new Integer(12345), model.getAddress().getZipCode());
        tester.setText("");
        assertEquals(null, model.getAddress().getZipCode());
    }


    // -----------------------------------
    // View to Model Tests test the data binding sync from view to model
    // for the different types of fields
    // -----------------------------------

    public void testModelToViewText() throws Exception
    {
        RCPText widget = new RCPText(LABEL);
        builder.add(widget);
        //test binding again nested property which is not yet set (equals null)
        validationManager.bindValue(widget, model, TestModel.P_Address, AddressModel.P_ZipCode);
        SWTBotText textTester = bot.textWithLabel(LABEL);
        assertEquals(null, model.getAddress());
        //set new address-object
        model.setAddress(new AddressModel());
        
        Integer value = new Integer(6789);
        model.getAddress().setZipCode(value);
        // use locale specific formatting for the integer value
        String expected = NumberFormat.getIntegerInstance().format(value);
        assertEquals(expected, textTester.getText());
        
        model.getAddress().setZipCode(null);
        assertEquals("", textTester.getText());
    }
}