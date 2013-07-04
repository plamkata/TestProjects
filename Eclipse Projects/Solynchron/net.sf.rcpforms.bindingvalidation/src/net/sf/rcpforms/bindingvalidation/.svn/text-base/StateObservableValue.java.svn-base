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

import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;

/**
 * synchronizes state information of an RCPWidget
 * 
 * @author Marco van Meegen
 */
public class StateObservableValue extends AbstractObservableValue
{

    private final RCPControl control;

    private final EControlState state;

    /**
     * @param control
     * @param state
     */
    public StateObservableValue(RCPControl control, EControlState state)
    {
        this.control = control;
        this.state = state;
    }

    public void doSetValue(Object value)
    {
        Object oldValue = doGetValue();
        control.setState(state, ((Boolean) value).booleanValue());
        fireValueChange(Diffs.createValueDiff(oldValue, value));
    }

    public Object doGetValue()
    {
        return control.hasState(state);
    }

    public Object getValueType()
    {
        return Boolean.TYPE;
    }

}
