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

package net.sf.rcpforms.modeladapter.tables;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.util.Policy;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * manages property change listeners on a list of beans which concentrate all change events into one
 * listener passed in constructor. mainly taken from internal of databinding beans
 * 
 * @author vanmeegenm
 */
public class ListenerSupport
{
    /** make sure each list object has a unique identity */
    private static class IdentityWrapper
    {
        final Object o;

        /**
         * @param o
         */
        public IdentityWrapper(Object o)
        {
            this.o = o;
        }

        /**
         * @return the unwrapped object
         */
        public Object unwrap()
        {
            return o;
        }

        public boolean equals(Object obj)
        {
            if (obj == null || obj.getClass() != IdentityWrapper.class)
            {
                return false;
            }
            return o == ((IdentityWrapper) obj).o;
        }

        public int hashCode()
        {
            return System.identityHashCode(o);
        }
    }

    private Set elementsListenedTo = new HashSet();

    private PropertyChangeListener listener;

    /**
     * Constructs a new instance.
     * 
     * @param listener is the callback that will be called when a <code>PropertyChangeEvent</code>
     *            is fired on any of the target objects. Will only receive change events when the
     *            provided <code>propertyName</code> changes.
     * @param propertyName
     */
    public ListenerSupport(final PropertyChangeListener listener)
    {
        Assert.isNotNull(listener);

        this.listener = new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listener.propertyChange(evt);
            }
        };
    }

    /**
     * Start listen to target (if it supports the JavaBean property change listener pattern)
     * 
     * @param target
     */
    public void hookListener(Object target)
    {
        if (processListener("addPropertyChangeListener", "Could not attach listener to ", target)) { //$NON-NLS-1$ //$NON-NLS-2$
            elementsListenedTo.add(new IdentityWrapper(target));
        }
    }

    /**
     * Add listeners for new targets (those this instance of<code>ListenerSupport</code> does not
     * already listen to), Stop to listen to those object that this instance listen to and is one of
     * the object in targets
     * 
     * @param targets
     */
    public void setHookTargets(Object[] targets)
    {
        Set elementsToUnhook = new HashSet(elementsListenedTo);
        if (targets != null)
        {
            for (int i = 0; i < targets.length; i++)
            {
                Object newValue = targets[i];
                IdentityWrapper identityWrapper = new IdentityWrapper(newValue);
                if (!elementsToUnhook.remove(identityWrapper))
                    hookListener(newValue);
            }
        }

        for (Iterator it = elementsToUnhook.iterator(); it.hasNext();)
        {
            Object o = it.next();
            if (o.getClass() != IdentityWrapper.class)
                o = new IdentityWrapper(o);
            elementsListenedTo.remove(o);
            unhookListener(o);
        }
    }

    /**
     * Stop listen to target
     * 
     * @param target
     */
    public void unhookListener(Object target)
    {
        if (target.getClass() == IdentityWrapper.class)
            target = ((IdentityWrapper) target).unwrap();

        if (processListener(
                "removePropertyChangeListener", "Cound not remove listener from ", target)) { //$NON-NLS-1$//$NON-NLS-2$
            elementsListenedTo.remove(new IdentityWrapper(target));
        }
    }

    /**
     * 
     */
    public void dispose()
    {
        unhookAllTargets();
        listener = null;
        elementsListenedTo = null;
    }

    public void unhookAllTargets()
    {
        if (elementsListenedTo != null)
        {
            Object[] targets = elementsListenedTo.toArray();
            for (int i = 0; i < targets.length; i++)
            {
                unhookListener(targets[i]);
            }
        }
    }

    /**
     * @return elements that were registred to
     */
    public Object[] getHookedTargets()
    {
        Object[] targets = null;
        if (elementsListenedTo != null && elementsListenedTo.size() > 0)
        {
            Object[] identityList = elementsListenedTo.toArray();
            targets = new Object[identityList.length];
            for (int i = 0; i < identityList.length; i++)
                targets[i] = ((IdentityWrapper) identityList[i]).unwrap();
        }
        return targets;
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

            parameters = new Object[]{listener};
        }
        catch (SecurityException e)
        {
            log(IStatus.WARNING, message + target, e);
        }
        catch (NoSuchMethodException e)
        {
            log(IStatus.WARNING, message + target, e);
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
                log(IStatus.WARNING, message + target, e);
            }
            catch (IllegalAccessException e)
            {
                log(IStatus.WARNING, message + target, e);
            }
            catch (InvocationTargetException e)
            {
                log(IStatus.WARNING, message + target, e);
            }
        }
        return false;
    }

    /**
     * Logs a message to the Data Binding logger.
     */
    private void log(int severity, String message, Throwable throwable)
    {
        if (BeansObservables.DEBUG)
        {
            Policy.getLog().log(
                    new Status(severity, Policy.JFACE_DATABINDING, IStatus.OK, message, throwable));
        }
    }
}
