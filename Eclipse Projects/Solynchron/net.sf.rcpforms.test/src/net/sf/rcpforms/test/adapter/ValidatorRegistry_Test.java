
package net.sf.rcpforms.test.adapter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import net.sf.rcpforms.modeladapter.converter.BeanConverterRegistry;
import net.sf.rcpforms.modeladapter.converter.ConverterRegistry;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

public class ValidatorRegistry_Test extends TestCase
{

    ConverterRegistry reg = new BeanConverterRegistry();
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        this.initValidators();
    }
    
    protected void tearDown()
    {
        try
        {
            super.tearDown();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally{
            this.removeValidators();
        }

    }

    private void removeValidators()
    {
        reg.removeValidator(String.class, Date.class);
        reg.removeValidator(String.class, Calendar.class);
        reg.removeValidator(String.class, String.class);
        reg.removeValidator(Date.class, String.class);
        reg.removeValidator(Calendar.class, String.class);
        reg.removeValidator(Boolean.class, boolean.class);        
    }

    private void initValidators()
    {
        reg.registerValidator(String.class, Date.class, new StringToDateValidator());
        reg.registerValidator(String.class, Calendar.class, new StringToCalendarValidator());
        reg.registerValidator(String.class, String.class, new StringToStringValidator());
        reg.registerValidator(Date.class, String.class, new DateToStringValidator());
        reg.registerValidator(Calendar.class, String.class, new CalendarToStringValidator());
        reg.registerValidator(Boolean.class, boolean.class, new BooleanNullTobooleanFalseValidator());
    }

    /**
     * Test if a converter for specified conversion is available
     */

    public void testDefaultValidators()
    {
        IValidator validator = reg.getValidator(String.class, Date.class);
        assertNotNull(validator);
        assertSame(StringToDateValidator.class, validator.getClass());
        validator = reg.getValidator(String.class, Calendar.class);
        assertNotNull(validator);
        assertSame(StringToCalendarValidator.class, validator.getClass());
        validator = reg.getValidator(String.class, String.class);
        assertNotNull(validator);
        assertSame(StringToStringValidator.class, validator.getClass());
        validator = reg.getValidator(Date.class, String.class);
        assertNotNull(validator);
        assertSame(DateToStringValidator.class, validator.getClass());
        validator = reg.getValidator(Calendar.class, String.class);
        assertNotNull(validator);
        validator = reg.getValidator(GregorianCalendar.class, String.class);
        assertEquals(validator.getClass(), CalendarToStringValidator.class);
        assertSame(CalendarToStringValidator.class, validator.getClass());
        validator = reg.getValidator(Boolean.class, boolean.class);
        assertNotNull(validator);
        assertSame(BooleanNullTobooleanFalseValidator.class, validator.getClass());
    }

    /**
     * Test if a converter for specified conversion is found
     */

    public void testSubclassedValidators()
    {
        // test from type -> subclass of calendar
        IValidator conv = reg.getValidator(GregorianCalendar.class, String.class);
        assertSame(CalendarToStringValidator.class, conv.getClass());
        
        conv = reg.getValidator(GregorianCalendar3.class, String.class);
        assertSame(CalendarToStringValidator.class, conv.getClass());

        // test to type -> subclass of calendar
        conv = reg.getValidator(String.class, GregorianCalendar.class);
        assertSame(StringToCalendarValidator.class, conv.getClass());
        
        // test to type -> subclass of calendar
        conv = reg.getValidator(String.class, GregorianCalendar3.class);
        assertSame(StringToCalendarValidator.class, conv.getClass());
    }

    /**
     * Test if a converter for specified conversion is found
     */
    public void testRemoveUnregisteredValidator()
    {
        try
        {
            reg.removeValidator(this.getClass(), this.getClass());
            fail("removeValidator must throw an IllegalArgumentException if unregistered converters should be removed!");
        }
        catch (IllegalArgumentException iae)
        {
            // do nothing
        }
    }

    /**
     * Test if a converter is successful added, used and removed from registry
     */

    public void testAddRemoveCustomValidator()
    {
        CustomValidator cc = new CustomValidator();
        reg.registerValidator(CustomValidator.class, String.class, cc);
        assertEquals(cc, reg.getValidator(CustomValidator.class, String.class));
        reg.removeValidator(CustomValidator.class, String.class);
//        assertEquals(ObjectToStringValidator.class, reg.getValidator(CustomValidator.class,
//                String.class).getClass());
    }

    /**
     * Test a converter is added using any object (not a Class-Object) -> no class hierarchy lookup
     * in converter-registry is supported!
     */
    public void testAddObjectSavedValidator()
    {
        CustomValidator cc = new CustomValidator();
        reg.registerValidator(this, String.class, cc);
        assertEquals(reg.getValidator(this, String.class), cc);
        reg.removeValidator(this, String.class);
    }
}



class CustomValidator implements IValidator
{
    public IStatus validate(Object value)
    {
        // TODO Auto-generated method stub
        return null;
    }

}

class StringToDateValidator implements IValidator
{
    public IStatus validate(Object value)
    {
        // TODO Auto-generated method stub
        return null;
    }

}

class StringToCalendarValidator implements IValidator
{
    public IStatus validate(Object value)
    {
        // TODO Auto-generated method stub
        return null;
    }

}

class StringToStringValidator implements IValidator
{
    public IStatus validate(Object value)
    {
        // TODO Auto-generated method stub
        return null;
    }

}

class DateToStringValidator implements IValidator
{
    public IStatus validate(Object value)
    {
        // TODO Auto-generated method stub
        return null;
    }

}

class CalendarToStringValidator implements IValidator
{
    public IStatus validate(Object value)
    {
        // TODO Auto-generated method stub
        return null;
    }

}

class BooleanNullTobooleanFalseValidator implements IValidator
{
    public IStatus validate(Object value)
    {
        // TODO Auto-generated method stub
        return null;
    }

}



