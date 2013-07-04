/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Remo Loetscher - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.form;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.rcpforms.common.util.Validate;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * Class RCPFormEditorPart embeds an RCPForm into an Eclipse Editor.
 * <p>
 * Usage:
 * 
 * <pre>
 * public class XYEditor extends RCPFormEditorPart&lt;SandboxStackForm&gt;
 * {
 *     public static final String ID = &quot;net.sf.rcpforms.examples.app.editorw&quot;;
 * 
 *     public XYEditor()
 *     {
 *         super(new SandboxStackForm());
 *     }
 * }
 * </pre>
 * 
 * You need to subclass the RCPFormEditorPart for being able to declare it in an extension point.
 * 
 * @author Remo Loetscher
 */
public abstract class RCPFormEditorPart<T extends RCPForm> extends EditorPart
{
    private T form = null;

    private boolean dirtyFlag = false;

    private static final Logger LOG = Logger.getLogger(RCPFormEditorPart.class.getName());

    private PropertyChangeListener dirtyChangeListener = new PropertyChangeListener()
    {

        public void propertyChange(PropertyChangeEvent evt)
        {
            RCPFormEditorPart.this.setDirty(true);
            // RCPFormEditorPart.this.getForm().getManagedForm().dirtyStateChanged();
        }
    };

    /**
     * @return Returns the form.
     */
    public T getForm()
    {
        return form;
    }

    /**
     * creates a view part around a form
     */
    public RCPFormEditorPart(T form)
    {
        this.form = form;
    }

    /**
     * create the form
     */
    public void createPartControl(Composite parent)
    {
        form.createUI(parent);
    }

    /**
     * Pass the focus request to the form
     */
    public void setFocus()
    {
        form.setFocus();
    }

    /*
     * ATTENTION: input has to be of type IRCPFormEditorInput
     * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException
    {
        this.setSite(site);
        Validate.isTrue(input instanceof IRCPFormEditorInput,
                "Input in RCPFormEditorPart has to implement Interface IRCPFormEditorInput"); //$NON-NLS-1$
        this.setInput(input);
        form.setInput(((IRCPFormEditorInput) input).getModels());
    }

    protected void setDirty(boolean value)
    {
        if (this.dirtyFlag == value)
            return;
        dirtyFlag = value;
        firePropertyChange(PROP_DIRTY);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
     */
    public void setInput(IEditorInput input)
    {
        IEditorInput oldInput = this.getEditorInput();
        // remove old listener
        if (oldInput != null)
        {
            IRCPFormEditorInput rcpInput = (IRCPFormEditorInput) oldInput;
            for (Object o : rcpInput.getModels())
            {
                this.processListener("removePropertyChangeListener", //$NON-NLS-1$
                        "Could not remove PropertyChangeListener from ", o); //$NON-NLS-1$
            }
        }
        // addpropertychange listener
        IRCPFormEditorInput rcpInput = (IRCPFormEditorInput) input;
        for (Object o : rcpInput.getModels())
        {
            this.processListener("addPropertyChangeListener", //$NON-NLS-1$
                    "Could not add PropertyChangeListener to ", o); //$NON-NLS-1$
        }
        super.setInput(input);
        form.setInput(((IRCPFormEditorInput) input).getModels());
        this.setDirty(false);
    }

    @Override
    public boolean isDirty()
    {
        return dirtyFlag;
    }

    /**
     * Invokes the method for the provided <code>methodName</code> attempting to first use the
     * method with the property name and then the unnamed version.
     * 
     * @param methodName either addPropertyChangeListener or removePropertyChangeListener
     * @param message string that will be prefixed to the target in an error message
     * @param target object to invoke the method on
     * @return <code>true</code> if the method was invoked successfully
     */
    private boolean processListener(String methodName, String message, Object target)
    {
        Method method = null;
        Object[] parameters = null;

        try
        {
            method = target.getClass().getMethod(methodName,
                    new Class[]{PropertyChangeListener.class});

            parameters = new Object[]{dirtyChangeListener};
        }
        catch (SecurityException e)
        {
            LOG.log(Level.WARNING, message + target, e.getMessage());
        }
        catch (NoSuchMethodException e)
        {
            LOG.log(Level.WARNING, message + target, e.getMessage());
        }

        if (method != null)
        {
            if (!method.isAccessible())
            {
                method.setAccessible(true);
            }
            try
            {
                method.invoke(target, parameters);
                return true;
            }
            catch (IllegalArgumentException e)
            {
                LOG.log(Level.WARNING, message + target, e.getMessage());
            }
            catch (IllegalAccessException e)
            {
                LOG.log(Level.WARNING, message + target, e.getMessage());
            }
            catch (InvocationTargetException e)
            {
                LOG.log(Level.WARNING, message + target, e.getMessage());
            }
        }
        return false;
    }
}