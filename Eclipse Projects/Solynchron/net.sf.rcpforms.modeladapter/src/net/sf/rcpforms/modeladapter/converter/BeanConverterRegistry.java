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
// Created on 21.05.2009

package net.sf.rcpforms.modeladapter.converter;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;

/**
 * standard converter registry for beans. Additionally to the converters/validators defined in
 * eclipse databinding registers converters/validators for:
 * <ul>
 * <li>converter from and to date and calendar using current locale
 * <li>converter converting empty strings to null strings and vice versa to enable dealing with null
 * strings
 * <li>converter from Boolean to boolean interpreting null as false
 * <li>date validator
 * </ul>
 * 
 * @author Marco van Meegen
 */
public class BeanConverterRegistry extends ConverterRegistry
{
    /**
     * hack default update strategy to get access to default converters and validators
     * 
     * @author Marco van Meegen
     */
    private final static class StandardConverterUpdateStrategy extends UpdateValueStrategy
        implements AccessibleConverterUpdateStrategy
    {
        @Override
        public IConverter createConverter(Object fromType, Object toType)
        {
            return super.createConverter(fromType, toType);
        }

        @Override
        public IValidator createValidator(Object fromType, Object toType)
        {
            return super.createValidator(fromType, toType);
        }

        public boolean isDefaultConverter(IConverter converter)
        {
            return converter instanceof DefaultConverter;
        }
    }

    public BeanConverterRegistry()
    {
        super(new StandardConverterUpdateStrategy());
        reset();
    }

    /** reset all registered converters and validators to the default */
    public void reset()
    {
        super.reset();
        registerDefaultConverters();
        registerDefaultValidators();
    }

    private void registerDefaultConverters()
    {

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
        this.registerValidator(String.class, Date.class, new DateToStringValidator());
    }

}
