/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Remo Loetscher - initial API and implementation
 *
 ******************************************************************************
 */

package net.sf.rcpforms.bindingvalidation;

import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;

/**
 * WidgetObservableValue wraps a widget to unidirectionally synchronize widget state like ENABLED,
 * EDITABLE from a model boolean value. The state is set to the inverted value of the boolean value;
 * if value is true, state is set to false
 * 
 * @author Remo Loetscher
 */
public class InvertedStateObservableValue extends AbstractObservableValue
{

    private final RCPControl control;

    private final EControlState attribute;

    /**
     * @param control
     * @param attribute
     */
    public InvertedStateObservableValue(RCPControl control, EControlState state)
    {
        this.control = control;
        this.attribute = state;
    }

    public void doSetValue(Object value)
    {
        Object oldValue = doGetValue();
        control.setState(attribute, !((Boolean) value).booleanValue());
        fireValueChange(Diffs.createValueDiff(oldValue, value));
    }

    public Object doGetValue()
    {
        return !control.hasState(attribute);
    }

    public Object getValueType()
    {
        return Boolean.TYPE;
    }

}
