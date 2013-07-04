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

import java.util.Calendar;

import net.sf.rcpforms.widgetwrapper.Messages;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Class RCPDatePickerDialog. A dialog where the user can select a date using a month view.
 * 
 * @author Marco van Meegen
 */
public class RCPDatePickerDialog extends Dialog
{
    private Calendar m_result;

    private String m_desc;

    private DateTime m_swtCalendar;

    /**
     * Create the dialog
     * 
     * @param parent
     */
    public RCPDatePickerDialog(Shell parent, String subject)
    {
        super(parent);
        m_desc = subject;
        setDate(null);
    }

    /**
     * Set the date (used as start date in calendar)
     * 
     * @param value date to start with, if null, the current date is used and displayed in the
     *            default locale
     */
    public void setDate(Calendar value)
    {
        if (null == value)
        {
            m_result = Calendar.getInstance();
        }
        else
        {
            m_result = value;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell newShell)
    {
        newShell.setText(Messages.getString("RCPDatePickerDialog.DatePickerTitle")); //$NON-NLS-1$
        super.configureShell(newShell);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent)
    {

        Composite client = (Composite) super.createDialogArea(parent);
        Label lbl = new Label(client, SWT.NULL);
        lbl.setText(m_desc);

        m_swtCalendar = new DateTime(parent, SWT.CALENDAR | SWT.BORDER);
        // double click does not work since the clicked area cannot be determined,
        // it might have been at the month scroll button.
        // m_swtCalendar.addMouseListener(new MouseAdapter()
        // {
        // @Override
        // public void mouseDoubleClick(MouseEvent e)
        // {
        // okPressed();
        // }
        // });
        GridData gd = new GridData();
        gd.horizontalSpan = 2;
        gd.horizontalAlignment = SWT.CENTER;
        gd.verticalAlignment = SWT.CENTER;
        m_swtCalendar.setLayoutData(gd);
        m_swtCalendar.setYear(m_result.get(Calendar.YEAR));
        m_swtCalendar.setMonth(m_result.get(Calendar.MONTH));
        m_swtCalendar.setDay(m_result.get(Calendar.DAY_OF_MONTH));
        return client;
    }

    /**
     * sets the selected date into result and closes the dialog
     */
    @Override
    protected void okPressed()
    {
        m_result.set(m_swtCalendar.getYear(), m_swtCalendar.getMonth(), m_swtCalendar.getDay());
        super.okPressed();
    }

    /**
     * @return the selected date as calendar in the current time zone or the time zone of the
     *         calendar passed with last {@link #setDate(Calendar)}
     */
    public Calendar getDate()
    {
        return m_result;
    }
}
