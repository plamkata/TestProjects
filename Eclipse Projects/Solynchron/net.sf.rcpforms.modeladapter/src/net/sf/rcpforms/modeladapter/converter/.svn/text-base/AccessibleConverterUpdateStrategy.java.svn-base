
package net.sf.rcpforms.modeladapter.converter;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;

/**
 * interface to hack update strategy to get access to default converters and validators; contains
 * the converter methods from update value strategy but made public
 * 
 * @author Marco van Meegen
 */
public interface AccessibleConverterUpdateStrategy
{
    public IConverter createConverter(Object fromType, Object toType);

    public IValidator createValidator(Object fromType, Object toType);

    public boolean isDefaultConverter(IConverter converter);
}