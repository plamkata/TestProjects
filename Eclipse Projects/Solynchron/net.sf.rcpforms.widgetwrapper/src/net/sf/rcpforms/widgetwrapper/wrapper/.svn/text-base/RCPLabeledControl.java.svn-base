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

package net.sf.rcpforms.widgetwrapper.wrapper;

import net.sf.rcpforms.common.util.Validate;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Class RCPLabeledControl is a compound of a standard swt control which are named
 * RCPSimple<ControlName> since it is assumed that normally they have a label and an optional label.
 * It has one or two children: a RCPSimpleLabel and the simple control.
 * <p>
 * Usage:
 * 
 * <pre>
 * public class RCPText extends RCPLabeledControl&lt;RCPSimpleText&gt;
 * {
 *     public RCPText(String label, int style)
 *     {
 *         super(label, new RCPSimpleText(null, style));
 *     }
 * 
 *     public final Text getSWTText()
 *     {
 *         return getRCPText().getSWTText();
 *     }
 * 
 * }
 * </pre>
 * 
 * @author Marco van Meegen
 */
public abstract class RCPLabeledControl<T extends RCPControl> extends RCPCompound
{
    private RCPHyperlinkLabel labelWrapper;

    private T simpleWrapper;

    /**
     * create a labeled control wrapper for the given label and the given instance of a wrapped
     * control.
     * <p>
     * Usage:
     * 
     * @param label
     * @param simpleWrapper
     */
    public RCPLabeledControl(String label, T simpleWrapper)
    {
        super(label);
        // create the label with default style
        this.simpleWrapper = simpleWrapper;
        if (label != null || !"".equals(label))
        {
            labelWrapper = new RCPHyperlinkLabel(label, SWT.DEFAULT);
            internalAdd(labelWrapper);
        }
        internalAdd(simpleWrapper);
    }

    public final RCPHyperlinkLabel getRCPHyperlink()
    {
        Validate.notNull(labelWrapper);
        return labelWrapper;
    }

    /**
     * @return true if for this control a separate label control has to be created. Builders will
     *         create a label if this is true, otherwise a filler.
     */
    public boolean hasLabel()
    {
        return getLabel() != null;
    }

    /**
     * overridden to apply special layout information directly to the main control ignoring the
     * label
     */
    @Override
    public void setLayoutData(Object layoutData)
    {
        super.setLayoutData(layoutData);
    }

    public final T getRCPControl()
    {
        Validate.notNull(simpleWrapper);
        return simpleWrapper;
    }

    /**
     * override to return the control and ignore the label
     */
    @Override
    public Widget getWrappedWidget()
    {
        return getRCPControl().getWrappedWidget();
    }

    /**
     * default is to set the state on all children, but not for mandatory and recommended
     */
    @Override
    public void setState(EControlState state, boolean value)
    {
        if (state == EControlState.MANDATORY || state == EControlState.RECOMMENDED
                || state == EControlState.OTHER)
        {
            // mandatory and recommended do not apply to the label
            getRCPControl().setState(state, value);
        }
        else
        {
            for (RCPWidget child : getRcpChildren())
            {
                if (child instanceof RCPControl)
                {
                    ((RCPControl) child).setState(state, value);
                }
            }
        }
    }

    @Override
    public boolean hasState(EControlState state)
    {
        //FIXME remsy getState can return an incorrect value!
        //try e.g.: RCPLabeledControl.setState(EControlState.READONLY, true)
        //but after immediate calling: RCPLabeledControl.getState(EcontrolState.READONLY) one would expect that its "true", but its "false"!
        //note: getState is final an cannot be overwritten!
        return getRCPControl().hasState(state);
    }

    /**
     * the labeled control should be used for layout parameters
     * 
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPCompound#getMainControl()
     */
    @Override
    public RCPControl getMainControl()
    {
        return getRCPControl();
    }

    public ControlDecoration getDecoration(EControlState state)
    {
        return getRCPControl().getDecoration(EControlState.OTHER);
    }

    @Override
    public void createUI(FormToolkit formToolkit)
    {
        super.createUI(formToolkit);
        // add focus listener to hand over focus to the control
        if (hasLabel())
        {
            labelWrapper.getSWTControl().addFocusListener(new FocusAdapter()
            {
                public void focusGained(FocusEvent e)
                {
                    getRCPControl().getSWTControl().setFocus();
                }
            });
        }
    }
}
