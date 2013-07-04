
package net.sf.rcpforms.form;

import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Class RCPFormDialog embeds an RCPForm into a Dialog, thus a Form can be used in a dialog passing
 * it to the constructor. An input can be set. Per default the ok button will only be enabled if the
 * form is valid. This can be changed using {@link #setForceValidOnOk(boolean)}.
 * <p>
 * Usage (will fill the dialog with the given form):
 * 
 * <pre>
 * Dialog dlg = new RCPFormDialog&lt;KontoStackForm&gt;(window.getShell(), IConstants.ID_STACKFORM_KONTO,
 *         instanceId);
 * dlg.setInput(model); // will set the input to the form
 * int result = dlg.open();
 * if (result == Dialog.OK)
 * {
 *     // ...
 * }
 * </pre>
 * 
 * The dialog will be resizable and initially be sized to fit the form.
 * <p>
 * An input can be set and will be passed to the form. The passed input will hold the modified
 * values after the dialog has finished, independent if Cancel or Ok was pressed.
 * 
 * @author Marco van Meegen
 */
public class RCPFormDialog<T extends RCPForm> extends Dialog
{
    private Composite m_parent;

    /** references the button which should be bound to validation state of the form */
    private Button validationStateButton = null;

    private T form;

    /** flag if form should be valid before ok can be pressed */
    private boolean forceValidOnOk = false;

    private IValueChangeListener m_statusChangeListener;

    public RCPFormDialog(Shell parent, T form)
    {
        this(parent, form, true);
    }

    public RCPFormDialog(Shell parent, T form, boolean resizeable)
    {
        super(parent);
        if (resizeable)
        {
            setShellStyle(getShellStyle() | SWT.RESIZE);
        }
        this.form = form;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(final Composite parent)
    {
        m_parent = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        m_parent.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        m_parent.setLayoutData(gd);
        applyDialogFont(m_parent);
        form.createUI(m_parent);

        // Fix for Task 152082
        // setting the layout grid data manually. otherwise a default griddata will be set and form
        // will NOT grab excess space
        GridData form_gd = new GridData(GridData.FILL_BOTH);
        form_gd.grabExcessHorizontalSpace = true;
        form_gd.grabExcessVerticalSpace = true;
        form_gd.horizontalAlignment = SWT.FILL;
        form_gd.verticalAlignment = SWT.FILL;
        form.getScrolledForm().setLayoutData(form_gd);

        return form.getScrolledForm();
    }

    protected void initStatusChangeHandler(RCPForm form, final Button button)
    {
        Validate.notNull(form);
        Validate.notNull(button);
        validationStateButton = button;
        setForceValidOnOk(true);
        m_statusChangeListener = new IValueChangeListener()
        {
            public void handleValueChange(ValueChangeEvent event)
            {
                IStatus newStatus = (IStatus) event.getObservableValue().getValue();
                if (forceValidOnOk)
                {
                    button.setEnabled(newStatus.isOK());
                }
            }
        };

        form.getObservableValidationStatus().addValueChangeListener(m_statusChangeListener);
    }

    public T getForm()
    {
        return form;
    }

    /**
     * set the input for the form hosted by this dialog
     * 
     * @param input input data model
     */
    public void setInput(Object input)
    {
        form.setInput(input);
    }

    /**
     * get the input for the form hosted by this dialog
     */
    public Object getInput()
    {
        return form.getInput();
    }

    /**
     * overridden to add status handler to button
     */
    @Override
    protected Control createContents(Composite parent)
    {
        Control result = super.createContents(parent);
        initStatusChangeHandler(form, getButton(IDialogConstants.OK_ID));
        return result;

    }

    /**
     * sets en/disablement of ok button dependent on validation state
     * 
     * @param forceValidOnOk true: button will only be enabled if form is valid, false: button will
     *            always be enabled
     */
    public void setForceValidOnOk(boolean forceValidOnOk)
    {
        this.forceValidOnOk = forceValidOnOk;
        if (validationStateButton != null && form != null)
        {
            // already existing ? --> update state
            boolean enabled = true;
            if (forceValidOnOk)
            {
                IStatus newStatus = (IStatus) form.getObservableValidationStatus().getValue();
                enabled = newStatus.isOK();
            }
            validationStateButton.setEnabled(enabled);
        }
    }
}
