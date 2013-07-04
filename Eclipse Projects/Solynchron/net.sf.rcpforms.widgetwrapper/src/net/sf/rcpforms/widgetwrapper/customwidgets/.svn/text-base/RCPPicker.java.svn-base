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

package net.sf.rcpforms.widgetwrapper.customwidgets;

import net.sf.rcpforms.widgetwrapper.util.WidgetUtil;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCompound;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPHyperlinkButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Generic PickerCompound. Compound consists of a label, textfield and a button Normally used for
 * DatePickers, TimePickers or DataPickers in general
 *
 * @author Remo Loetscher
 */

public class RCPPicker extends RCPCompound
{
    private RCPText m_textField;

    private RCPHyperlinkButton m_pickerBtn;

    private String buttonText;

    private Image buttonImage;

    
    /**
     * Constructor for RCPPicker
     *
     * @param label label for the textfield
     * @param buttonImage image for the button to be diplayed
     */
    public RCPPicker(String label, Image buttonImage)
    {
        this(label, buttonImage, SWT.DEFAULT);
    }

    /**
     * Constructor for RCPPicker
     *
     * @param label label for the textfield
     * @param buttonImage image for the button to be diplayed
     * @param style SWT style
     */
    public RCPPicker(String label, Image buttonImage, int style)
    {
        super(label, style);
        this.buttonImage = buttonImage;
        this.createWidgets();
    }
    
    private void createWidgets()
    {
        //create internal widgets
        m_textField = new RCPText(getLabel(), getStyle());
        m_pickerBtn = new RCPHyperlinkButton(buttonText, buttonImage);
    }

    @Override
    protected Widget getWrappedWidget()
    {
        return this.getMainControl() == null || !WidgetUtil.isValid(this.getMainControl())? null : this.getMainControl().getSWTControl();
    }

    /**
     * overridden to create composed widgets If this method is overwritten by subclasses,
     * super.createUI() must be called.
     *
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPCompound#createUI(org.eclipse.ui.forms.widgets.FormToolkit)
     */
    @Override
    public void createUI(FormToolkit formToolkit)
    {
        // add internal widgets
        internalAdd(m_textField);
        internalAdd(m_pickerBtn);
        super.createUI(formToolkit);

    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        //delegate state either to textfield or if disabled or readonly to button
        if(EControlState.MANDATORY.equals(state))
        {
            //reset state
            m_textField.setState(EControlState.MANDATORY, false);
            m_pickerBtn.setState(EControlState.MANDATORY, false);
            //apply only mandatory state to the textfield if its editable and enabled
            if(!m_textField.hasState(EControlState.READONLY))
            {
                m_textField.setState(EControlState.MANDATORY, value);
            }else
            {
                m_pickerBtn.setState(EControlState.MANDATORY, value);
            }
        }else if(EControlState.READONLY.equals(state))
        {
            m_textField.setState(EControlState.READONLY, value);
            //switch mandatory state from textfield to picker button if readonly...
            if(m_textField.hasState(EControlState.MANDATORY) && value)
            {
                //textfield has now readonly state -> decorate therefore picker button
                m_pickerBtn.setState(EControlState.MANDATORY, true);
                
            }else if(m_pickerBtn.hasState(EControlState.MANDATORY))
            {
                //picker button has only mandatory state if text field is not enabled or readonly
                m_textField.setState(EControlState.MANDATORY, true);
                m_pickerBtn.setState(EControlState.MANDATORY, false);
            }
        }else
        {
            super.setState(state, value);
        }
    }

    /**
     * @return reference to the text.
     */
    public RCPText getText()
    {
        return m_textField;
    }

    /**
     * Attach a listener to the button to get informed about events like mouse down or key key down
     * on the button.
     *
     * @param listenerType specifies the type. e.g. SWT.MouseDown or SWT.KeyDown
     * @param listener listener implementation to get informed about button events.
     */
    public void addListener(int listenerType, Listener listener)
    {
        CustomButton swtButton = m_pickerBtn.getTypedWidget();
        if (swtButton != null)
            swtButton.addListener(listenerType, listener);
    }

    /**
     * This method can be used to disable the button manually. e.g. if control is marked as
     * read-only (no direct entries in textfield are possible), default behaviour for buttons is to
     * be still enabled. if the whole compound should be read-only and no changes to the textfield
     * should be allowed (neither direct or throug picker button) you can disable the picker button
     * using enableButton(false). Other possibility is to build your own subclass and overwrite
     * setState(...) Method to change default behaviour
     *
     * @param enableButton en/disables the picker button
     */
    public void enableButton(boolean enableButton)
    {
        m_pickerBtn.setState(EControlState.ENABLED, enableButton);
    }

    @Override
    public RCPControl getMainControl()
    {
        return m_textField;
    }

    @Override
    public int getNumberOfControls()
    {
        // TODO children are not available at time this is needed by builder,
        // thus hardcoded here
        return 3;
    }

}
