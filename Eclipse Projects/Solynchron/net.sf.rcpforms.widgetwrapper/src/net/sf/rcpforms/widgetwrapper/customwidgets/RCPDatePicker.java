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

package net.sf.rcpforms.widgetwrapper.customwidgets;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.util.RCPFormsResources;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.ibm.icu.text.DateFormat;

/**
 * Class RCPDatePicker is a compound widget composed of a label, a text field and a button which
 * opens a date selection dialog.
 * <p>
 * It is a non-extensible component and thus can be used with a {@link GridBuilder} with autolayout.
 * 
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class RCPDatePicker extends RCPPicker
{

    /**
     * Constructor for RCPDatePicker
     * 
     * @param label
     */
    public RCPDatePicker(String label)
    {
        this(label, SWT.DEFAULT);
    }

    /**
     * Constructor for RCPDatePicker
     * 
     * @param label
     * @param style
     */
    public RCPDatePicker(String label, int style)
    {
        super(label, RCPFormsResources.getImage(RCPFormsResources.IMG_KEY_CALENDAR), style);
    }

    /**
     * overridden to init inherited text field for text limit and verify listener
     * 
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPCompound#createUI(org.eclipse.ui.forms.widgets.FormToolkit)
     */
    @Override
    public void createUI(FormToolkit formToolkit)
    {
        super.createUI(formToolkit);
        // TODO i18n or replace by DateTime
        getText().getSWTText().setTextLimit(10);
        Listener listener = new Listener()
        {

            public void handleEvent(Event event)
            {
                selectDate();
            }
        };
        addListener(SWT.MouseDown, listener);
        addListener(SWT.KeyDown, listener);

    }

    /**
     * picks a date and sets it into the text field
     */
    protected void selectDate()
    {
        RCPDatePickerDialog dialog = new RCPDatePickerDialog(getSWTParent().getShell(), getLabel());
        String text = getText().getSWTText().getText();
        try
        {
            java.util.Calendar c = Calendar.getInstance();
            Date d = getDateFormatter().parse(text);
            if (d != null)
            {
                c.setTime(d);
            }
            dialog.setDate(c);
        }
        catch (ParseException ex)
        {
            // ignore and start with current date
        }
        if (dialog.open() != Dialog.CANCEL)
        {
            text = getDateFormatter().format(dialog.getDate().getTime());
            getText().getSWTText().setText(text);
        }
    }

    /**
     * @return localized date formatter to use
     */
    protected DateFormat getDateFormatter()
    {
        return DateFormat.getDateInstance(DateFormat.SHORT);
    }
}
