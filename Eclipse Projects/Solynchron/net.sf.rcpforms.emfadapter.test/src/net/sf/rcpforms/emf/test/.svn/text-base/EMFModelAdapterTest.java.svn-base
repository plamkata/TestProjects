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

package net.sf.rcpforms.emf.test;

import net.sf.rcpforms.emf.EEnumRangeAdapter;
import net.sf.rcpforms.emf.EMFModelAdapter;
import net.sf.rcpforms.emf.ObservableListEObjectContentProvider;
import net.sf.rcpforms.modeladapter.configuration.IRangeAdapter;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;
import net.sf.rcpforms.test.adapter.BeanAdapter_Test;

import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EClass;

/**
 * Test for EMFModelAdapter
 * 
 * @author Marco van Meegen
 */
public class EMFModelAdapterTest extends BeanAdapter_Test
{
    private boolean flag = false;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        ModelAdapter.registerAdapter(EMFModelAdapter.getInstance());
    }
    
    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        ModelAdapter.unregisterAdapter(EMFModelAdapter.getInstance());
    }
    /**
     * Test method for {@link EMFModelAdapter#getMetaClass(java.lang.Object)}.
     */
    public void testGetMetaClass()
    {
        EMFModelAdapter adapter = EMFModelAdapter.getInstance();
        TestModel model = createTestModel();
        EClass testClass = getModelMeta();
        assertSame(testClass, adapter.getMetaClass(model));
    }

    /**
     * Test method for {@link net.sf.rcpforms.test.EMFModelAdapter#canAdapt(java.lang.Object)} .
     */
    public void testCanAdapt()
    {
        EMFModelAdapter adapter = EMFModelAdapter.getInstance();
        assertTrue(adapter.canAdaptClass(getModelMeta()));
        assertTrue(adapter.canAdapt(createTestModel()));
        assertFalse(adapter.canAdapt(new Object()));
    }

    private EClass getModelMeta()
    {
        return TestPackage.eINSTANCE.getTestModel();
    }

    private TestModel createTestModel()
    {
        return TestFactory.eINSTANCE.createTestModel();
    }

    public void testContentProvider()
    {

    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.test.EMFModelAdapter#getObservableValue(java.lang.Object, net.sf.rcpforms.binding.IPropertyChain)}
     * .
     */
    public void testGetObservableValue()
    {
        TestModel model = createTestModel();
        EMFModelAdapter adapter = EMFModelAdapter.getInstance();

        // define access chain to property named "key"
        IPropertyChain propertyChain = adapter.getPropertyChain(getModelMeta(),
                TestModelConstants.P_Name);
        IObservableValue observableValue = adapter.getObservableValue(model, propertyChain);
        model.setName("Muster");
        assertTrue(observableValue.getClass().getName().contains("EObjectObservableValue"));
        assertEquals("Muster", observableValue.getValue());

        // changing observableValue should change map entry
        flag = false;
        observableValue.setValue("newValue");
        assertEquals("newValue", model.getName());
        observableValue.addChangeListener(new IChangeListener()
        {
            public void handleChange(org.eclipse.core.databinding.observable.ChangeEvent event)
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
     * {@link net.sf.rcpforms.test.EMFModelAdapter#getRangeAdapter(net.sf.rcpforms.binding.IPropertyChain)}
     * .
     */

    public void testGetRangeAdapter()
    {
        EMFModelAdapter adapter = EMFModelAdapter.getInstance();

        // define access chain to property named "gender"
        IPropertyChain propertyChain = adapter.getPropertyChain(getModelMeta(),
                TestModelConstants.P_Gender);
        IRangeAdapter rangeAdapter = adapter.getRangeAdapter(propertyChain);
        assertTrue(rangeAdapter instanceof EEnumRangeAdapter);
    }

    public void testDefaultStructuredContentProvider()
    {
        TestModel model = createTestModel();
        ModelAdapter adapter = ModelAdapter.getAdapterForInstance(model);
        assertTrue(adapter.createDefaultContentProvider() instanceof ObservableListEObjectContentProvider);
    }

}
