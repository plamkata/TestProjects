
package net.sf.rcpforms.test.adapter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import net.sf.rcpforms.modeladapter.converter.*;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.internal.databinding.conversion.ObjectToStringConverter;

@SuppressWarnings("restriction")
public class ConverterRegistry_Test extends TestCase
{

    ConverterRegistry reg = new BeanConverterRegistry();

    /**
     * Test if a converter for specified conversion is available
     */

    public void testDefaultConverters()
    {
        IConverter conv = reg.getConverter(String.class, Date.class);
        assertNotNull(conv);
        assertSame(StringToDateConverter.class, conv.getClass());
        conv = reg.getConverter(String.class, Calendar.class);
        assertNotNull(conv);
        assertSame(StringToCalendarConverter.class, conv.getClass());
        conv = reg.getConverter(String.class, String.class);
        assertNotNull(conv);
        assertSame(StringToStringConverter.class, conv.getClass());
        conv = reg.getConverter(Date.class, String.class);
        assertNotNull(conv);
        assertSame(DateToStringConverter.class, conv.getClass());
        conv = reg.getConverter(Calendar.class, String.class);
        assertNotNull(conv);
        assertSame(CalendarToStringConverter.class, conv.getClass());
        conv = reg.getConverter(Boolean.class, boolean.class);
        assertNotNull(conv);
        assertSame(BooleanNullTobooleanFalseConverter.class, conv.getClass());
    }

    /**
     * Test if a converter for specified conversion is found
     */

    public void testSubclassedConverters()
    {
        // test from type -> subclass of calendar
        IConverter conv = reg.getConverter(GregorianCalendar.class, String.class);
        assertSame(CalendarToStringConverter.class, conv.getClass());

        // test to type -> subclass of calendar
        conv = reg.getConverter(String.class, GregorianCalendar.class);
        assertSame(StringToCalendarConverter.class, conv.getClass());

        // test exact matching by registering directly GregorianCalendar class
        CustomConverter cc = new CustomConverter();
        reg.registerConverter(GregorianCalendar.class, String.class, cc);
        conv = reg.getConverter(GregorianCalendar.class, String.class);
        assertEquals(cc, conv);

        // vice versa
        reg.registerConverter(String.class, GregorianCalendar.class, cc);
        conv = reg.getConverter(String.class, GregorianCalendar.class);
        assertEquals(cc, conv);

        // Registry State:
        // - GregorianCalendar1 is not registered
        // - GregorianCalendar (superclass) has a registered converter
        // converter returned: converter for GregorianCalendar
        MyGregorianToStringConverter mg2sc = new MyGregorianToStringConverter("",
                GregorianCalendar.class, String.class);
        // remove default converter
        reg.removeConverter(Calendar.class, String.class);
        reg.registerConverter(GregorianCalendar.class, String.class, mg2sc);
        // re-register default calendar converter to change a possible order!
        reg.registerConverter(Calendar.class, String.class, new CalendarToStringConverter());
        conv = reg.getConverter(GregorianCalendar1.class, String.class);
        assertEquals(mg2sc, conv);
        reg.removeConverter(GregorianCalendar.class, String.class);

        // vice versa
        MyStringToGregorianConverter ms2gc = new MyStringToGregorianConverter();
        // remove default converter
        reg.removeConverter(String.class, Calendar.class);
        reg.registerConverter(String.class, GregorianCalendar.class, ms2gc);
        // re-register default calendar converter to change a possible order!
        reg.registerConverter(String.class, Calendar.class, new StringToCalendarConverter());
        conv = reg.getConverter(String.class, GregorianCalendar1.class);
        assertEquals(ms2gc, conv);
        reg.removeConverter(String.class, GregorianCalendar.class);

    }

    public void testSubclassedAndInterfaceConverters()
    {
        MyGregorianToStringConverter mg2sc = new MyGregorianToStringConverter("Class",
                GregorianCalendar1.class, String.class);
        MyGregorianToStringConverter mg2scInterface = new MyGregorianToStringConverter("Interface",
                IGregorianCalendar1.class, String.class);

        // Registry State:
        // - Class is registered
        // - >1 interface realised in this class is registered
        // converter returned: for this class
        reg.registerConverter(GregorianCalendar1.class, String.class, mg2sc);
        reg.registerConverter(IGregorianCalendar1.class, String.class, mg2scInterface);
        IConverter conv = reg.getConverter(GregorianCalendar1.class, String.class);
        assertEquals(mg2sc, conv);

        // Registry State:
        // - interface is registered
        // converter returned: for this interface
        conv = reg.getConverter(IGregorianCalendar1.class, String.class);
        assertEquals(mg2scInterface, conv);

        // Registry State:
        // - Class is not registered
        // - >1 interface realised in this class is registered
        // converter returned: for this interface
        reg.removeConverter(GregorianCalendar1.class, String.class);
        conv = reg.getConverter(GregorianCalendar1.class, String.class);
        assertEquals(mg2scInterface, conv);
        reg.registerConverter(GregorianCalendar1.class, String.class, mg2sc);

        // Registry State:
        // - Class is not registered
        // - Superclass has a registered converter
        // - >1 interface realised in the superclass are registered
        // converter returned: converter for superclass
        conv = reg.getConverter(GregorianCalendar2.class, String.class);
        assertEquals(mg2sc, conv);

        // Registry State:
        // - Class is not registered
        // - Superclass is not registered
        // - >1 interface realised in the superclass are registered
        // converter returned: converter for superclass interface
        reg.removeConverter(GregorianCalendar1.class, String.class);
        conv = reg.getConverter(GregorianCalendar2.class, String.class);
        assertEquals(mg2scInterface, conv);

        // Registry State:
        // - Interface is not registered
        // - Superinterface is registered
        // converter returned: converter for superinterface
        reg.registerConverter(GregorianCalendar1.class, String.class, mg2sc);
        conv = reg.getConverter(IGregorianCalendar2.class, String.class);
        assertEquals(mg2scInterface, conv);
    }

    /**
     * Test if a converter for specified conversion is found
     */
    public void testRemoveUnregisteredConverter()
    {
        try
        {
            reg.removeConverter(this.getClass(), this.getClass());
            fail("removeConverter must throw an IllegalArgumentException if unregistered converters should be removed!");
        }
        catch (IllegalArgumentException iae)
        {
            // do nothing
        }
    }

    /**
     * Test if a converter is successful added, used and removed from registry
     */

    public void testAddRemoveCustomConverter()
    {
        CustomConverter cc = new CustomConverter();
        reg.registerConverter(CustomConverter.class, String.class, cc);
        assertEquals(cc, reg.getConverter(CustomConverter.class, String.class));
        reg.removeConverter(CustomConverter.class, String.class);
        assertEquals(ObjectToStringConverter.class, reg.getConverter(CustomConverter.class,
                String.class).getClass());
    }

    /**
     * Test a converter is added using any object (not a Class-Object) -> no class hierarchy lookup
     * in converter-registry is supported!
     */
    public void testAddObjectSavedConverter()
    {
        CustomConverter cc = new CustomConverter();
        reg.registerConverter(this, String.class, cc);
        assertEquals(reg.getConverter(this, String.class), cc);
    }
}



class CustomConverter implements IConverter
{

    public Object convert(Object fromObject)
    {
        // dummy implementation
        return null;
    }

    public Object getFromType()
    {
        return null;
    }

    public Object getToType()
    {
        return null;
    }

}



class MyGregorianToStringConverter implements IConverter
{

    private String purpose;

    private Class<?> toType;

    private Class<?> fromType;

    public MyGregorianToStringConverter(String purpose, Class<?> fromType, Class<?> toType)
    {
        this.purpose = purpose;
        this.toType = toType;
        this.fromType = fromType;
    }

    public Object convert(Object fromObject)
    {
        // dummy implementation
        return ((GregorianCalendar) fromObject).toString();
    }

    public Object getFromType()
    {
        return fromType;
    }

    public Object getToType()
    {
        return toType;
    }

    @Override
    public String toString()
    {
        return this.getClass().getCanonicalName() + "[GregorianCalendar.class-String.class]" + ": "
                + purpose;
    }

}



class MyStringToGregorianConverter implements IConverter
{

    public Object convert(Object fromObject)
    {
        // dummy implementation
        return ((GregorianCalendar) fromObject).toString();
    }

    public Object getFromType()
    {
        return String.class;
    }

    public Object getToType()
    {
        return GregorianCalendar.class;
    }

}



class GregorianCalendar1 extends GregorianCalendar implements IGregorianCalendar1
{
    private static final long serialVersionUID = -7351451065548460747L;
}



class GregorianCalendar2 extends GregorianCalendar1
{
    private static final long serialVersionUID = -1047844767417215990L;
}



class GregorianCalendar3 extends GregorianCalendar2 implements IGregorianCalendar2
{
    private static final long serialVersionUID = 5244889639391405599L;
}



interface IGregorianCalendar1
{
}



interface IGregorianCalendar2 extends IGregorianCalendar1
{
}