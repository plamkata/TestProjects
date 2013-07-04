
package net.sf.rcpforms.form;

import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IMessage;

/**
 * Wrapper to use an RCPForm in a wizard as wizard page. The validation state of the form is used to
 * indicate when the page is complete.
 * 
 * @author Marco van Meegen
 */
public class RCPFormWizardPage extends WizardPage implements IMessageDisplay
{
    private RCPForm form;

    // change listener for button enablement
    private IValueChangeListener m_statusChangeListener;

    /**
     * This kind of wizard can take a <code>AbstractDlamStackForm</code> to display.
     * 
     * @param pageId wizard page id
     * @param form form to embed into wizard page
     */
    public RCPFormWizardPage(String pageId, RCPForm form)
    {
        this(pageId, form, null);
    }

    /**
     * create wizard page with an image displayed in the header
     * 
     * @param pageId wizard page id
     * @param form form to embed into wizard page
     * @param imageDescriptor image desscriptor of the image to create
     */
    public RCPFormWizardPage(String pageId, RCPForm form, ImageDescriptor imageDescriptor)
    {
        super(pageId);
        this.setTitle(form.getTitle());
        setImageDescriptor(imageDescriptor);
        this.form = form;
    }

    /**
     * Create contents of the wizard
     * 
     * @param pageContainer
     */
    public void createControl(Composite pageContainer)
    {
        pageContainer.setBackground(pageContainer.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        pageContainer.getParent().setBackground(
                pageContainer.getDisplay().getSystemColor(SWT.COLOR_WHITE));

        // create form and set as page control
        form.createUI(pageContainer);
        setControl(form.getScrolledForm());

        // add change listener for updating completion state
        addStatusChangeListener(form);
    }

    /**
     * add a status change listener to the form to track weiter button enablement
     * 
     * @param form
     */
    protected void addStatusChangeListener(RCPForm form)
    {
        if (form != null)
        {

            // register status change listener
            m_statusChangeListener = new IValueChangeListener()
            {
                public void handleValueChange(ValueChangeEvent event)
                {
                    IStatus newStatus = (IStatus) event.getObservableValue().getValue();
                    setPageComplete(newStatus.isOK());
                }
            };

            IStatus newStatus = (IStatus) form.getObservableValidationStatus().getValue();
            setPageComplete(newStatus.isOK());
            form.getObservableValidationStatus().addValueChangeListener(m_statusChangeListener);
        }
    }

    protected void removeStatusChangeListener(RCPForm form)
    {
        if (m_statusChangeListener != null && form != null)
        {
            // deregister listener
            form.getObservableValidationStatus().removeValueChangeListener(m_statusChangeListener);
            m_statusChangeListener = null;
        }
    }

    protected IValueChangeListener getStatusChangeListener()
    {
        return m_statusChangeListener;
    }

    protected void setStatusChangeListener(IValueChangeListener ivcl)
    {
        m_statusChangeListener = ivcl;
    }

    @Override
    public void dispose()
    {
        super.dispose();
        removeStatusChangeListener(getForm());
        form.getScrolledForm().dispose();
    }

    /**
     * Returns the <code>StackForm</code> which is hold inside the wizard container
     * 
     * @return
     */
    public RCPForm getForm()
    {
        return form;
    }

    /**
     * redirects form error messages to wizard message area
     */
    public final void setMessage(String newMessage, int newType, IMessage[] messages)
    {
        setMessage(newMessage, newType);

    }
}
