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
// Created on 28.05.2008

package net.sf.rcpforms.test.adapter;

import junit.framework.TestCase;
import junit.framework.TestResult;
import net.sf.rcpforms.modeladapter.configuration.BeanAdapter;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

/**
 * @author Marco van Meegen
 */
public class MapAdapter_Test extends TestCase
{

    private boolean flag = false;

    /**
     * overloaded to make sure the default realm is set to the default display, otherwise
     * WritableValue's cannot be created without a realm
     */
    @Override
    public void run(final TestResult result)
    {
        Realm.runWithDefault(SWTObservables.getRealm(Display.getDefault()), new Runnable()
        {
            public void run()
            {
                MapAdapter_Test.super.run(result);
            }
        });
    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.test.adapter.test.MapAdapter#getMetaClass(java.lang.Object)}.
     */

    public void testGetMetaClass()
    {
        MapAdapter adapter = new MapAdapter();
        WritableMap map = new WritableMap();
        assertSame(WritableMap.class, adapter.getMetaClass(map));
    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.test.adapter.test.MapAdapter#canAdapt(java.lang.Object)}.
     */

    public void testCanAdapt()
    {
        MapAdapter adapter = new MapAdapter();
        assertTrue(adapter.canAdaptClass(WritableMap.class));
    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.test.adapter.test.MapAdapter#getObservableValue(java.lang.Object, net.sf.rcpforms.widgetwrapper.binding.IPropertyChain)}
     * .
     */

    public void testGetObservableValue()
    {
        WritableMap map = new WritableMap();
        map.put("key", "value");
        MapAdapter adapter = new MapAdapter();

        // define access chain to property named "key"
        IPropertyChain propertyChain = adapter.getPropertyChain(WritableMap.class, "key");
        IObservableValue observableValue = adapter.getObservableValue(map, propertyChain);
        assertTrue(observableValue instanceof MapObservableValue);
        assertEquals("value", observableValue.getValue());

        // changing observableValue should change map entry
        flag = false;
        observableValue.setValue("newValue");
        assertEquals("newValue", map.get("key"));
        observableValue.addChangeListener(new IChangeListener()
        {
            public void handleChange(ChangeEvent event)
            {
                flag = true;
            }
        });
        // and vice versa, event should be fired
        map.put("key", "yetAnotherValue");
        assertEquals("yetAnotherValue", observableValue.getValue());
        assertTrue(flag);

    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.test.adapter.test.MapAdapter#getPropertyChain(java.lang.Object, java.lang.Object[])}
     * .
     */

    public void testGetPropertyChain()
    {
        MapAdapter adapter = new MapAdapter();

        // define access chain to property named "key"
        IPropertyChain propertyChain = adapter.getPropertyChain(WritableMap.class, "key");
        assertNotNull(propertyChain);
        assertTrue(WritableMap.class.isAssignableFrom((Class<?>) propertyChain.getModelMeta()));
    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.widgetwrapper.configuration.ModelAdapter#registerAdapter(net.sf.rcpforms.widgetwrapper.configuration.ModelAdapter)}
     * .
     */

    public void testRegisterAdapter()
    {
        // default adapter for all objects is bean adapter
        WritableMap map = new WritableMap();
        ModelAdapter adapter = ModelAdapter.getAdapterForInstance(map);
        assertTrue(adapter instanceof BeanAdapter);
        adapter = ModelAdapter.getAdapterForMetaClass(WritableMap.class);
        assertTrue(adapter instanceof BeanAdapter);

        // now register map adapter
        ModelAdapter.registerAdapter(new MapAdapter());
        adapter = ModelAdapter.getAdapterForInstance(map);
        assertTrue(adapter instanceof MapAdapter);
        adapter = ModelAdapter.getAdapterForMetaClass(WritableMap.class);
        assertTrue(adapter instanceof MapAdapter);

    }

}
