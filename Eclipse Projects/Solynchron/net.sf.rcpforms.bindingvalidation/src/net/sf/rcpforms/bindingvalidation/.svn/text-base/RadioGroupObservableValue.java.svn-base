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

package net.sf.rcpforms.bindingvalidation;

import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.modeladapter.util.Validate;
import net.sf.rcpforms.widgetwrapper.wrapper.radio.RadioGroupManager;

import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * binds a RadioGroupManager to a model value.
 * <p>
 * If a radio button is selected, the associated value is set into this ObservableValue (remember a
 * {@link RadioGroupManager} associates n radio buttons with n values) and vice versa.
 * <p>
 * Example:
 * 
 * <pre>
 * 
 * </pre>
 * taken and modified from Databinding newsgroup
 * <p>
 */
public class RadioGroupObservableValue extends AbstractObservableValue
{
    private RadioGroupManager group;

    private Object selection = null;

    public RadioGroupObservableValue(RadioGroupManager group)
    {
        Validate.notNull(group);
        this.group = group;
        group.addSelectionListener(selectionListener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#dispose()
     */
    public synchronized void dispose()
    {
        group.removeSelectionListener(selectionListener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doSetValue(java.lang.Object)
     */
    protected void doSetValue(Object value)
    {
        // SPECIAL HANDLING FOR NullValue OBJECT
        Object modifiedValue = value == null ? NullValue.getInstance() : value;
        group.setSelection(modifiedValue);
        selection = modifiedValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doGetValue()
     */
    protected Object doGetValue()
    {
        return group.getSelection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.observable.value.IObservableValue#getValueType()
     */
    public Object getValueType()
    {
        return Object.class;
    }

    SelectionListener selectionListener = new SelectionListener()
    {
        public void widgetDefaultSelected(SelectionEvent e)
        {
            widgetSelected(e);
        }

        public void widgetSelected(SelectionEvent e)
        {
            final Object newSelection = group.getSelection();
            fireValueChange(new ValueDiff()
            {
                public Object getNewValue()
                {
                    return newSelection;
                }

                public Object getOldValue()
                {
                    return selection;
                }
            });
            selection = newSelection;
        }

    };
}