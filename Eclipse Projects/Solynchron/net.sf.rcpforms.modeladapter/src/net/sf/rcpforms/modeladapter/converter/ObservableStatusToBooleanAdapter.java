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

package net.sf.rcpforms.modeladapter.converter;

import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IStatus;

/**
 * represents the valid/invalid state of an observable<IStatus>, automatically adapts on changes
 * 
 * @author Marco van Meegen
 */
public class ObservableStatusToBooleanAdapter extends WritableValue implements IValueChangeListener
{

    private IObservableValue m_observableStatus;

    public ObservableStatusToBooleanAdapter(IObservableValue observableStatus)
    {
        super(getBooleanState(observableStatus), Boolean.class);
        Validate.isTrue(observableStatus != null);
        Validate.isTrue(IStatus.class.isAssignableFrom((Class<?>) observableStatus.getValueType()));
        m_observableStatus = observableStatus;
        m_observableStatus.addValueChangeListener(this);
    }

    /**
     * @param observable
     * @return
     */
    private static Boolean getBooleanState(IObservableValue observable)
    {
        Validate.isTrue(observable.getValue() instanceof IStatus,
                "only IObservable of type IStatus allowed"); //$NON-NLS-1$
        return ((IStatus) observable.getValue()).isOK();
    }

    public void handleValueChange(ValueChangeEvent event)
    {
        setValue(getBooleanState(m_observableStatus));
    }

    public synchronized void dispose()
    {
        m_observableStatus.removeValueChangeListener(this);
        super.dispose();
    }
}
