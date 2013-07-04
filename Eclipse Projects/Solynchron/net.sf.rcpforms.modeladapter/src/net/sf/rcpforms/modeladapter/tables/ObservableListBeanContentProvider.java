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

import java.beans.PropertyChangeListener;
import java.util.Iterator;

import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.list.ObservableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * * Content Provider which observes a writable list of beans for changes. It attaches to all beans
 * in * the list and refreshes the viewer if a property of the bean changes. * * @author vanmeegenm
 */
public class ObservableListBeanContentProvider extends ObservableListContentProvider
{
    /** observed list */
    private ObservableList observableList = null;

    /** support for managing bean listeners */
    private ListenerSupport listenerSupport;

    private StructuredViewer viewer;

    /** change listener for bean property changes of beans contained in list */
    private IListChangeListener listener = new IListChangeListener()
    {
        public void handleListChange(ListChangeEvent event)
        {
            ListDiffEntry[] differences = event.diff.getDifferences();
            for (int i = 0; i < differences.length; i++)
            {
                ListDiffEntry entry = differences[i];
                if (entry.isAddition())
                {
                    listenerSupport.hookListener(entry.getElement());
                }
                else
                {
                    listenerSupport.unhookListener(entry.getElement());
                }
            }
        }
    };

    private PropertyChangeListener beanListener = new PropertyChangeListener()
    {
        public void propertyChange(final java.beans.PropertyChangeEvent event)
        {
            handleBeanPropertyChange(event.getSource(), event.getPropertyName());
        }
    };

    /** * Constructor for ObservableListBeanContentProvider */
    public ObservableListBeanContentProvider()
    {
        listenerSupport = new ListenerSupport(beanListener);
    }

    /**
     * * called when one of the beans in the observed list fires a property change * * @param source
     * * @param propertyName
     */
    protected void handleBeanPropertyChange(Object bean, String propertyName)
    {
        viewer.update(bean, new String[]{propertyName});
    }

    /**
     * * (non-Javadoc) * * @see
     * org.eclipse.jface.databinding.viewers.ObservableListContentProvider#inputChanged
     * (org.eclipse.jface.viewers.Viewer, * java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
        listenerSupport.unhookAllTargets();
        if (observableList != null)
        {
            observableList.removeListChangeListener(listener);
            observableList = null;
        }
        super.inputChanged(viewer, oldInput, newInput);
        this.viewer = (StructuredViewer) viewer;
        observableList = (ObservableList) newInput;
        if (observableList != null)
        {
            observableList.addListChangeListener(listener);
            for (Iterator iterator = observableList.iterator(); iterator.hasNext();)
            {
                listenerSupport.hookListener(iterator.next());
            }
        }
    }
}