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
// Created on 28.05.2008

package net.sf.rcpforms.test.adapter;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;
import junit.framework.TestResult;
import net.sf.rcpforms.modeladapter.configuration.BeanAdapter;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.DateConv;
import net.sf.rcpforms.tablesupport.tables.ColumnConfiguration;
import net.sf.rcpforms.tablesupport.tables.ECellEditorType;
import net.sf.rcpforms.tablesupport.tables.PropertyLabelProviderAndCellModifier;
import net.sf.rcpforms.test.adapter.TestModel.Gender;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * Test for PropertyLabelProviderAndCellModifier, checks null value handling and converters for all
 * standard types.
 * <p>
 * Code is a bit strange, to share tests between Bean and EMF tests, thus make sure emf tests work
 * if you enhance this !
 * 
 * @author Marco van Meegen
 */
public class PropertyLabelProviderAndCellModifierBean_Test extends TestCase
{
    /** example of nested property access using dot notation */
    protected static final String P_NESTED_ZIP_CODE = TestModel.P_Address + "."
            + AddressModel.P_ZipCode;

    protected ColumnConfiguration[] columnConfiguration = new ColumnConfiguration[]{
            new ColumnConfiguration("Name", TestModel.P_Name, 100, SWT.LEFT, false,
                    ECellEditorType.TEXT).setGrabHorizontal(true),
            new ColumnConfiguration("Gender", TestModel.P_Gender, 60, SWT.LEFT, getGenderValues()),
            new ColumnConfiguration("Birthdate", TestModel.P_BirthDate, 80, SWT.LEFT, false,
                    ECellEditorType.TEXT),
            new ColumnConfiguration("Overdraw", TestModel.P_OverdrawAccount, 50, SWT.CENTER, false,
                    ECellEditorType.CHECK),
            new ColumnConfiguration("Children", TestModel.P_ChildCount, 50, SWT.RIGHT, false,
                    ECellEditorType.TEXT),
            new ColumnConfiguration("Balance", TestModel.P_AccountBalance, 70, SWT.RIGHT, false,
                    ECellEditorType.TEXT),
            new ColumnConfiguration("Zip", P_NESTED_ZIP_CODE, 70, SWT.RIGHT, false,
                    ECellEditorType.TEXT)};

    /**
     * overloaded to make sure the default realm is set to the default display, otherwise
     * WritableValue's cannot be created without a realm
     */
    @Override
    public void run(final TestResult result)
    {
        Realm.runWithDefault(SWTObservables.getRealm(Display.getDefault()), new Runnable()
        {
            public void run()
            {
                PropertyLabelProviderAndCellModifierBean_Test.super.run(result);
            }
        });
    }

    /** test the table binding via model adapter */
    public void testString()
    {
        PropertyLabelProviderAndCellModifier cellmodifier = createLabelProviderAndCellModifier();
        ITestModel model = createTestModel();

        // check getter/setter and converter for string
        model.setName("Muster");
        // getter for table label provider
        String col1Text = cellmodifier.getColumnText(model, 0);
        assertEquals("Muster", col1Text);
        // getter for cell editor
        Object col1Value = cellmodifier.getValue(model, TestModel.P_Name);
        assertTrue(col1Value instanceof String);
        assertEquals("Muster", col1Value);

        // null value handling
        model.setName(null);
        Object col1Value1 = cellmodifier.getValue(model, TestModel.P_Name);
        assertEquals("", col1Value1);

        // setter for cell editor
        cellmodifier.modify(model, TestModel.P_Name, "Meister");
        assertEquals("Meister", model.getName());

        // setter for cell editor
        cellmodifier.modify(model, TestModel.P_Name, "");
        assertEquals(null, model.getName());
    }

    /** test the table binding via model adapter */
    public void testEnum()
    {
        PropertyLabelProviderAndCellModifier cellmodifier = createLabelProviderAndCellModifier();
        ITestModel model = createTestModel();
        // check getter/setter and converter for enum
        setGender(model, getFemaleConstant());
        String col2Text = cellmodifier.getColumnText(model, 1);
        assertEquals("FEMALE", col2Text);
        Object col2Value = cellmodifier.getValue(model, TestModel.P_Gender);
        // for combo editors the value is translated to an index into the values array
        assertTrue(col2Value instanceof Integer);
        assertEquals(1, col2Value);
        // setter for cell editor uses index for combo cell editor
        cellmodifier.modify(model, TestModel.P_Gender, 2);
        assertEquals(getUnknownConstant(), getGender(model));
    }

    protected Object getUnknownConstant()
    {
        return Gender.UNKNOWN;
    }

    protected Object getFemaleConstant()
    {
        return Gender.FEMALE;
    }

    /** test the table binding via model adapter */
    public void testBoolean()
    {
        PropertyLabelProviderAndCellModifier cellmodifier = createLabelProviderAndCellModifier();
        ITestModel model = createTestModel();

        // check getter/setter and converter for string
        model.setOverdrawAccount(true);
        // getter for table label provider
        String col1Text = cellmodifier.getColumnText(model, 3);
        assertEquals("true", col1Text);
        // getter for cell editor
        Object col1Value = cellmodifier.getValue(model, TestModel.P_OverdrawAccount);
        assertTrue(col1Value instanceof Boolean);
        assertEquals(Boolean.TRUE, col1Value);

        // setter for cell editor
        cellmodifier.modify(model, TestModel.P_OverdrawAccount, Boolean.FALSE);
        assertEquals(Boolean.FALSE, model.getOverdrawAccount());
        // and null
        cellmodifier.modify(model, TestModel.P_OverdrawAccount, null);
        assertEquals(null, model.getOverdrawAccount());
    }

    /** test the table binding via model adapter */
    public void testInteger()
    {
        PropertyLabelProviderAndCellModifier cellmodifier = createLabelProviderAndCellModifier();
        ITestModel model = createTestModel();

        // check getter/setter and converter for string
        model.setChildCount(3);
        // getter for table label provider
        String col1Text = cellmodifier.getColumnText(model, 4);
        assertEquals("3", col1Text);
        // getter for cell editor
        Object col1Value = cellmodifier.getValue(model, TestModel.P_ChildCount);
        // cell editor type is string
        assertTrue(col1Value instanceof String);
        assertEquals("3", col1Value);

        // setter for cell editor
        cellmodifier.modify(model, TestModel.P_ChildCount, "7");
        assertEquals(new Integer(7), model.getChildCount());
        // and null
        cellmodifier.modify(model, TestModel.P_ChildCount, null);
        assertEquals(null, model.getChildCount());
        cellmodifier.modify(model, TestModel.P_ChildCount, "");
        assertEquals(null, model.getChildCount());

    }

    /** test the table binding via model adapter */
    public void testDouble()
    {
        NumberFormat format = NumberFormat.getNumberInstance();
        PropertyLabelProviderAndCellModifier cellmodifier = createLabelProviderAndCellModifier();
        ITestModel model = createTestModel();

        // check getter/setter and converter for string
        model.setAccountBalance(3.7);
        // getter for table label provider
        String col1Text = cellmodifier.getColumnText(model, 5);
        String localeSpecificNumberString = format.format(3.7);
        assertEquals(localeSpecificNumberString, col1Text);
        // getter for cell editor
        Object col1Value = cellmodifier.getValue(model, TestModel.P_AccountBalance);
        // cell editor type is string
        assertTrue(col1Value instanceof String);
        assertEquals(localeSpecificNumberString, col1Value);

        String localSpecific75 = format.format(7.5);
        // setter for cell editor
        cellmodifier.modify(model, TestModel.P_AccountBalance, localSpecific75);
        assertEquals(new Double(7.5), model.getAccountBalance());
        // and null
        cellmodifier.modify(model, TestModel.P_AccountBalance, null);
        assertEquals(null, model.getAccountBalance());
        cellmodifier.modify(model, TestModel.P_AccountBalance, "");
        assertEquals(null, model.getAccountBalance());
    }

    /** test nested property */
    public void testNestedInteger()
    {
        PropertyLabelProviderAndCellModifier cellmodifier = createLabelProviderAndCellModifier();
        ITestModel model = createTestModel();

        // check getter/setter and converter for string
        setNestedZip(model, 3);
        // getter for table label provider
        String col1Text = cellmodifier.getColumnText(model, 6);
        assertEquals("3", col1Text);
        // getter for cell editor
        Object col1Value = cellmodifier.getValue(model, P_NESTED_ZIP_CODE);
        // cell editor type is string
        assertTrue(col1Value instanceof String);
        assertEquals("3", col1Value);

        // setter for cell editor
        cellmodifier.modify(model, P_NESTED_ZIP_CODE, "7");
        assertEquals(new Integer(7), getNestedZip(model));
        // and null
        cellmodifier.modify(model, P_NESTED_ZIP_CODE, null);
        assertEquals(null, getNestedZip(model));
        cellmodifier.modify(model, P_NESTED_ZIP_CODE, "");
        assertEquals(null, getNestedZip(model));

    }
    
    /** test the table binding via model adapter */
    public void testDate() throws ParseException
    {
        // localized date format might be overridden from message resources
        DateFormat format = DateConv.getDateFormat(DateConv.LOCALIZED_DATE_FORMAT);
//        DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.GERMANY);
        DateFormat testCaseFormat = new SimpleDateFormat("dd/MM/yyyy");
        PropertyLabelProviderAndCellModifier cellmodifier = createLabelProviderAndCellModifier();
        ITestModel model = createTestModel();

        // check getter/setter and converter for string
        Date date = testCaseFormat.parse("09/08/1964");
        model.setBirthDate(date);
        // getter for table label provider
        String col1Text = cellmodifier.getColumnText(model, 2);
        String localeSpecificDate = format.format(date);
        assertEquals(localeSpecificDate, col1Text);
        // getter for cell editor
        Object col1Value = cellmodifier.getValue(model, TestModel.P_BirthDate);
        // cell editor type is string
        assertTrue(col1Value instanceof String);
        assertEquals(localeSpecificDate, col1Value);

        Date date2 = testCaseFormat.parse("24/12/2000");
        String localSpecific2412 = format.format(date2);
        // setter for cell editor
        cellmodifier.modify(model, TestModel.P_BirthDate, localSpecific2412);
        assertEquals(date2, model.getBirthDate());
        // and null
        cellmodifier.modify(model, TestModel.P_BirthDate, null);
        assertEquals(null, model.getBirthDate());
        cellmodifier.modify(model, TestModel.P_BirthDate, "");
        assertEquals(null, model.getBirthDate());
    }

 
 
    // TODO: test these
    // new ColumnConfiguration("Zip", TestModel.P_Address + "."
    // + AddressModelConstants.P_ZipCode, 70, SWT.RIGHT, false, ECellEditorType.TEXT)};

    protected PropertyLabelProviderAndCellModifier createLabelProviderAndCellModifier()
    {
        Class<?> rowElementMetaClass = TestModel.class;
        assertSame("ModelAdapter not registered or not retrieved correctly", BeanAdapter
                .getInstance(), ModelAdapter.getAdapterForMetaClass(rowElementMetaClass));
        PropertyLabelProviderAndCellModifier cellmodifier = new PropertyLabelProviderAndCellModifier(
                columnConfiguration, rowElementMetaClass);
        return cellmodifier;
    }

    protected ITestModel createTestModel()
    {
        return new TestModel();
    }

    /** abstracted since gender type different in emf and bean */
    protected Object getGender(ITestModel model)
    {
        return ((TestModel) model).getGender();
    }

    /** abstracted since gender type different in emf and bean */
    protected void setGender(ITestModel model, Object gender)
    {
        ((TestModel) model).setGender((Gender) gender);
    }

    protected Object[] getGenderValues(){
        return Gender.values();
    }
    /**
     * set nested zip code; abstracted since AddressModel is different for EMF and Bean
     */
    protected void setNestedZip(ITestModel model, Integer i)
    {
        ((TestModel) model).getAddress().setZipCode(i);
    }

    protected Integer getNestedZip(ITestModel model)
    {
        return ((TestModel) model).getAddress().getZipCode();

    }


    
}