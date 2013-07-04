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

package net.sf.rcpforms.examples.complete.widgets;

import net.sf.rcpforms.widgetwrapper.wrapper.RCPLabeledControl;

import org.eclipse.swt.SWT;

/**
 * Generic PickerCompound. Compound consists of a label, textfield and a button Normally used for
 * DatePickers, TimePickers or DataPickers in general
 *
 * @author Remo Loetscher
 */

public class RCPLabeledPickerComposite extends RCPLabeledControl<RCPPickerCompositeWidget>
{
    /**
     * Constructor for RCPPicker
     *
     * @param label label for the textfield
     * @param buttonImage image for the button to be diplayed
     */
    public RCPLabeledPickerComposite(String label)
    {
        this(label, SWT.DEFAULT);
    }

    /**
     * Constructor for RCPPicker
     *
     * @param label label for the textfield
     * @param buttonImage image for the button to be diplayed
     * @param style SWT style
     */
    public RCPLabeledPickerComposite(String label, int style)
    {
        super(label, new RCPPickerCompositeWidget());
    }

    /**
     * overridden to create composed widgets If this method is overwritten by subclasses,
     * super.createUI() must be called.
     *
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPCompound#createUI(org.eclipse.ui.forms.widgets.FormToolkit)
     */

    @Override
    public int getNumberOfControls()
    {
        // TODO children are not available at time this is needed by builder,
        // thus hardcoded here
        return 2;
    }

}
