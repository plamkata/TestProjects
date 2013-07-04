package net.sf.rcpforms.modeladapter.converter;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.internal.databinding.conversion.IdentityConverter;

@SuppressWarnings("restriction")
public class IdentityConverterWithPrimitiveNullSupport extends IdentityConverter
{

    public IdentityConverterWithPrimitiveNullSupport(Class type)
    {
        super(type);
    }

    public IdentityConverterWithPrimitiveNullSupport(Class fromType, Class toType)
    {
        super(fromType, toType);
        // TODO Auto-generated constructor stub
    }

    /**
     * Special implementation for null --> primitive conversion.
     * Originial Implementation throws for that type of conversion a BindingException("Cannot convert null to a primitive").
     * 
     * Conversion logic for null --> primitve is as follows:
     * <ul>
     * <li> null --> int = 0
     * <li> null --> short = 0
     * <li> null --> long = 0L
     * <li> null --> byte = 0
     * <li> null --> float = 0.0f
     * <li> null --> double = 0.d
     * <li> null --> boolean = false
     * <li> null --> char = ' '
     * </ul>
     * 
     * @see org.eclipse.core.internal.databinding.conversion.IdentityConverter#convert(java.lang.Object)
     */
    @Override
    public Object convert(Object source)
    {
        Object result = null;
        if (((Class)getToType()).isPrimitive() && source == null) {
            //handle null to primitive conversion
            if(int.class.equals(getToType()))
            {
                result = 0;
            }else if(short.class.equals(getToType()))
            {
                result = 0;
            }else if(long.class.equals(getToType()))
            {
                result = 0L;
            }else if(byte.class.equals(getToType()))
            {
                result = 0;
            }else if(float.class.equals(getToType()))
            {
                result = 0.0f;
            }else if(double.class.equals(getToType()))
            {
                result = 0.0d;
            }else if(boolean.class.equals(getToType()))
            {
                result = false;
            }else if(char.class.equals(getToType()))
            {
                result = ' ';
            }
            
        }else
        {
            result = super.convert(source);
        }
        return result;
    }
    
    

}
