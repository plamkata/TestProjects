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

package net.sf.rcpforms.emf;

import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.modeladapter.configuration.IRangeAdapter;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;

public class EEnumRangeAdapter implements IRangeAdapter
{

    EEnum type;

    public EEnumRangeAdapter(EEnum type)
    {
        this.type = type;
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
        return type.getELiterals();
    }

    public ILabelProvider getLabelProvider(String presentationIndex,
                                           String nullValuePresentationIndex)
    {
        return new LabelProvider()
        {
            @Override
            public String getText(Object element)
            {
                String text = super.getText(element);
                if (NullValue.isNullValue(element))
                {
                    // TODO: how to access NullValue representations and maps
                    // from here ?
                    text = "<null>";
                }
                return text;
            }
        };
    }
}
