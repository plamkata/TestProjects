
package net.sf.rcpforms.emf;

import net.sf.rcpforms.modeladapter.converter.AccessibleConverterUpdateStrategy;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EAttribute;

public class EMFDefaultConverterUpdateStrategy extends EMFUpdateValueStrategy
    implements AccessibleConverterUpdateStrategy
{
    /**
     * wraps a converter to replace the empty string by null before converting
     * 
     * @author Marco van Meegen
     */
    private static class EmptyStringToNullWrapperConverter implements IConverter
    {

        private IConverter converter;

        public EmptyStringToNullWrapperConverter(IConverter converter)
        {
            this.converter = converter;
        }

        public Object convert(Object fromObject)
        {
            Object modifiedFrom = fromObject;
            if ("".equals(fromObject))
            {
                modifiedFrom = null;
            }
            return converter.convert(modifiedFrom);
        }

        public Object getFromType()
        {
            return converter.getFromType();
        }

        public Object getToType()
        {
            return converter.getToType();
        }

    }

    @Override
    public IConverter createConverter(Object fromType, Object toType)
    {
        IConverter result = super.createConverter(fromType, toType);
        // convert string to emf datatype, make sure "" is handled as null value
        if (fromType == String.class && toType instanceof EAttribute)
        {
            result = new EmptyStringToNullWrapperConverter(result);
        }
        return result;
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
