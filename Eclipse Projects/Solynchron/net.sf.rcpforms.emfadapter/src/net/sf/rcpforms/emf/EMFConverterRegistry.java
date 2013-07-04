
package net.sf.rcpforms.emf;

import java.util.Calendar;
import java.util.Date;

import net.sf.rcpforms.modeladapter.converter.*;

/**
 * standard converter registry for beans. Additionally to the converters/validators defined in
 * eclipse databinding registers some converters for dealing with booleans, null values and dates.
 * <p>
 * TODO: rework for standard test cases with dates etc.
 * 
 * @author Marco van Meegen
 */
public class EMFConverterRegistry extends ConverterRegistry
{

    public EMFConverterRegistry()
    {
        super(new EMFDefaultConverterUpdateStrategy());
        reset();
    }

    private void registerDefaultConverters()
    {
        // TODO: register suitable converters for emf
        this.registerConverter(String.class, Date.class, new StringToDateConverter());
        this.registerConverter(Date.class, String.class, new DateToStringConverter());
        this.registerConverter(String.class, String.class, new StringToStringConverter());
        this.registerConverter(String.class, Calendar.class, new StringToCalendarConverter());
        this.registerConverter(Calendar.class, String.class, new CalendarToStringConverter());
        this.registerConverter(Boolean.class, boolean.class,
                new BooleanNullTobooleanFalseConverter());
    }

    private void registerDefaultValidators()
    {
        // TODO: register suitable validators for emf
        this.registerValidator(String.class, Date.class, new DateToStringValidator());
    }

    @Override
    public void reset()
    {
        super.reset();
        registerDefaultConverters();
        registerDefaultValidators();
    }
}
