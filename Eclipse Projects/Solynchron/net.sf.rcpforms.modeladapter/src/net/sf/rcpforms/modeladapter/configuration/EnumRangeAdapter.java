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
// Created on 02.05.2008

package net.sf.rcpforms.modeladapter.configuration;

import java.lang.reflect.Method;

import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * range adapter for java enums. Provides suitable input, content and label provider for the combo
 * viewer when binding an enum to a combo box.
 * 
 * @author Marco van Meegen
 */
public class EnumRangeAdapter implements IRangeAdapter
{

    private Class<? extends Enum<?>> clazz;

    public EnumRangeAdapter(Class<? extends Enum<?>> clazz)
    {
        Validate.isTrue(clazz.isEnum());
        this.clazz = clazz;
    }

    public IStructuredContentProvider getContentProvider(final Object nullValue)
    {
        return new ArrayContentProvider()
        {
            public Object[] getElements(Object inputElement)
            {
                Object[] result = super.getElements(inputElement);
                if (nullValue != null)
                {
                    // add null value to elements
                    Object[] resultWithNull = new Object[result.length + 1];
                    resultWithNull[0] = nullValue;
                    System.arraycopy(result, 0, resultWithNull, 1, result.length);
                    result = resultWithNull;
                }
                return result;
            }
        };
    }

    public Object getInput()
    {
        // return result of values() method
        Object result = null;
        try
        {
            Method m = clazz.getMethod("values", new Class[0]); //$NON-NLS-1$
            result = m.invoke(null, new Object[0]);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new IllegalArgumentException(
                    "EnumRangeAdapter: Could not read enum values via reflection for class " //$NON-NLS-1$
                            + clazz.getName());
        }
        return result;
    }

    public ILabelProvider getLabelProvider(String presentationKey,
                                           final String nullValuePresentationKey)
    {
        return new LabelProvider()
        {
            @Override
            public String getText(Object element)
            {
                String text = super.getText(element);
                if (NullValue.isNullValue(element))
                {
                    text = NullValue.getRepresentation(nullValuePresentationKey);
                }
                return text;
            }
        };
    }

}
