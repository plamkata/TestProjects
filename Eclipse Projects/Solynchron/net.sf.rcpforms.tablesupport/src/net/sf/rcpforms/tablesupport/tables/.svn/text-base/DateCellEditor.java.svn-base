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

package net.sf.rcpforms.tablesupport.tables;

/**
 * Class represents a special cell editor which will open a calendar dialog in which the date can be selected.
 * @author Remo Loetscher
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import net.sf.rcpforms.tablesupport.Messages;
import net.sf.rcpforms.widgetwrapper.customwidgets.RCPDatePickerDialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class DateCellEditor extends DialogCellEditor
{

    public DateCellEditor()
    {
        super();
    }

    public DateCellEditor(Composite parent, int style)
    {
        super(parent, style);
    }

    public DateCellEditor(Composite parent)
    {
        super(parent);
    }

    @Override
    protected Object openDialogBox(Control cellEditorWindow)
    {
        RCPDatePickerDialog dialog = new RCPDatePickerDialog(cellEditorWindow.getShell(), Messages.getString("DateCellEditor.DatePickerDialogTitle")); //$NON-NLS-1$
        String text = (String) super.doGetValue();
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
        }
        return text;
    }

    /**
     * @return localized date formatter to use
     */
    protected DateFormat getDateFormatter()
    {
        return DateFormat.getDateInstance(DateFormat.SHORT);
    }

}
