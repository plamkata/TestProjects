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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.TestCase;
import junit.framework.TestResult;
import net.sf.rcpforms.modeladapter.configuration.BeanAdapter;
import net.sf.rcpforms.modeladapter.configuration.EnumRangeAdapter;
import net.sf.rcpforms.modeladapter.configuration.IRangeAdapter;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

import com.damnhandy.aspects.bean.JavaBean;

/**
 * Test for BeanAdapter. If you write your own model adapter, this might be a good starting point to
 * get an idea what is expected from the adapter. Make sure to check the
 * {@link PropertyLabelProviderAndCellModifierBean_Test} too to make your adapter work with tables
 * too.
 * 
 * @author Marco van Meegen
 */
public class BeanAdapter_Test extends TestCase
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
                BeanAdapter_Test.super.run(result);
            }
        });
    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.widgetwrapper.test.BeanAdapter#getMetaClass(java.lang.Object)}.
     */

    public void testGetMetaClass()
    {
        BeanAdapter adapter = BeanAdapter.getInstance();
        TestModel model = new TestModel();
        assertSame(TestModel.class, adapter.getMetaClass(model));
    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.widgetwrapper.test.BeanAdapter#canAdapt(java.lang.Object)}.
     */

    public void testCanAdapt()
    {
        BeanAdapter adapter = BeanAdapter.getInstance();
        assertTrue(adapter.canAdaptClass(TestModel.class));
        assertTrue(adapter.canAdapt(new TestModel()));
    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.widgetwrapper.test.BeanAdapter#getObservableValue(java.lang.Object, net.sf.rcpforms.widgetwrapper.binding.IPropertyChain)}
     * .
     */

    public void testGetObservableValue()
    {
        TestModel model = new TestModel();
        ModelAdapter adapter = BeanAdapter.getInstance();

        // define access chain to property named "key"
        IPropertyChain propertyChain = adapter.getPropertyChain(TestModel.class, TestModel.P_Name);
        IObservableValue observableValue = adapter.getObservableValue(model, propertyChain);
        model.setName("Muster");
        assertTrue(observableValue.getClass().getName().contains("BeanObservableValue"));
        assertEquals("Muster", observableValue.getValue());

        // changing observableValue should change map entry
        flag = false;
        observableValue.setValue("newValue");
        assertEquals("newValue", model.getName());
        observableValue.addChangeListener(new IChangeListener()
        {
            public void handleChange(ChangeEvent event)
            {
                flag = true;
            }
        });
        // and vice versa, event should be fired
        model.setName("newMuster");
        assertEquals("newMuster", observableValue.getValue());
        assertTrue(flag);

    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.widgetwrapper.test.BeanAdapter#getRangeAdapter(net.sf.rcpforms.widgetwrapper.binding.IPropertyChain)}
     * .
     */

    public void testGetRangeAdapter()
    {
        BeanAdapter adapter = BeanAdapter.getInstance();

        // define access chain to property named "key"
        IPropertyChain propertyChain = adapter
                .getPropertyChain(TestModel.class, TestModel.P_Gender);
        IRangeAdapter rangeAdapter = adapter.getRangeAdapter(propertyChain);
        assertTrue(rangeAdapter instanceof EnumRangeAdapter);
    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.widgetwrapper.test.BeanAdapter#getPropertyChain(java.lang.Object, java.lang.Object[])}
     * .
     */

    public void testGetPropertyChain()
    {
        BeanAdapter adapter = BeanAdapter.getInstance();

        // define access chain to property named "key"
        IPropertyChain propertyChain = adapter.getPropertyChain(WritableMap.class, "key");
        assertNotNull(propertyChain);
        assertTrue(WritableMap.class.isAssignableFrom((Class<?>) propertyChain.getModelMeta()));
    }

    /**
     * Test method for {@link BeanAdapter#getValue(Object, IPropertyChain)} .
     */

    public void testGetPropertyValue()
    {
        BeanAdapter adapter = BeanAdapter.getInstance();
        TestModel model = new TestModel();
        final String propertyPath = TestModel.P_Address + "." + AddressModel.P_Street;
        ((JavaBean) model).addPropertyChangeListener(propertyPath, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event)
            {
                System.out.println("!!! Changing " + propertyPath + 
                        " (" + event.getOldValue() + " -> " + event.getNewValue() + ")");
                
            }});
        
        // test null value
        model.getAddress().setStreet(null);
        Object value = adapter.getValue(model, propertyPath);
        assertEquals(null, value);

        // test non-null value
        String street = "Teststrasse";
        model.getAddress().setStreet(street);
        value = adapter.getValue(model, propertyPath);
        assertEquals(street, value);
    }

    /**
     * Test method for {@link BeanAdapter#setValue(Object, IPropertyChain, Object)} .
     */

    public void testSetPropertyValue()
    {
        BeanAdapter adapter = BeanAdapter.getInstance();
        TestModel model = new TestModel();
        String propertyPath = TestModel.P_Address + "." + AddressModel.P_Street;
        // null value
        model.getAddress().setStreet("bla");
        adapter.setValue(model, propertyPath, null);
        assertEquals(null, model.getAddress().getStreet());

        // non null value
        String street = "Teststrasse";
        adapter.setValue(model, propertyPath, street);
        assertEquals(street, model.getAddress().getStreet());
    }
}