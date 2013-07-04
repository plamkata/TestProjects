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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.LinkedList;
import java.util.List;

import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * range adapter for integer types. Provides suitable input, content and label provider for the
 * combo viewer when binding an integer to a combo box. ranges are defined using {@link IntRange}
 * annotation
 * 
 * @author Remo Loetscher
 */
public class IntegerRangeAdapter implements IRangeAdapter
{

    /**
     * Provides the possibility to declare the range of integer properties in datamodel.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface IntRange {
        int minValue();

        int maxValue();

        int step() default 1;
    }

    private int minVal, maxVal, step;

    /**
     * Creates a new RangeAdapter for integer values.
     * 
     * @param minValue defines minimal integer (including)
     * @param maxValue defines maximal integer (including). Has to be greater then minValue
     * @param step specifies the in-/decrement of the {@link IntegerRangeAdapter#minVal} (default is
     *            1). 0 is not allowed.
     */
    public IntegerRangeAdapter(int minValue, int maxValue, int step)
    {
        Validate.isTrue(minValue < maxValue, "Minimum value \"" + minValue //$NON-NLS-1$
                + "\" has to be greater than maximum value \"" + maxValue + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        Validate.isTrue(step != 0, "Value for step: \"" + step + "\" is not allowed to be zero!"); //$NON-NLS-1$ //$NON-NLS-2$
        this.minVal = minValue;
        this.maxVal = maxValue;
        this.step = step;
    }

    public IStructuredContentProvider getContentProvider(final Object nullValue)
    {
        return new ArrayContentProvider()
        {
            public Object[] getElements(Object inputElement)
            {
                List<Integer> range = new LinkedList<Integer>();

                int tempStep = Math.abs(step);
                for (int c = minVal; c <= maxVal; c += tempStep)
                {
                    if (step > 0)
                        range.add(c);
                    else
                        // build the list in "descending" order
                        range.add(0, c);
                }

                return range.toArray();
            }
        };
    }

    public Object getInput()
    {
        List<Integer> range = new LinkedList<Integer>();
        for (int c = minVal; c <= maxVal; ++c)
        {
            range.add(c);
        }

        return range.toArray();
    }

    public ILabelProvider getLabelProvider(String presentationKey, String nullValuePresentationKey)
    {
        return new LabelProvider()
        {
            @Override
            public String getText(Object element)
            {
                String text = super.getText(element);
                return text;
            }
        };
    }

}
