/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.bindingvalidation;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;

import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.ConverterRegistry;
import net.sf.rcpforms.modeladapter.util.Validate;

/**
 * Class RCPUpdateValueStrategy uses converters registered in {@link ConverterRegistry}
 * 
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class RCPUpdateValueStrategy extends UpdateValueStrategy
{
    private ModelAdapter adapter;
    
    public RCPUpdateValueStrategy(ModelAdapter adapter)
    {
        super();
        Validate.notNull(adapter);
        this.adapter = adapter;
    }

    @Override
    protected IConverter createConverter(Object fromType, Object toType)
    {
        IConverter result = converter;
        if(result == null)
            result = adapter.getConverterRegistry().getConverter(fromType, toType);
        return result;
    }

    @Override
    protected IValidator createValidator(Object fromType, Object toType)
    {
        IValidator result = afterGetValidator;
        // TODO: use adapter to retrieve converter
        if(result == null)
            result = adapter.getConverterRegistry().getValidator(fromType, toType);
        return result;
    }
}