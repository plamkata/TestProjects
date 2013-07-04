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

package net.sf.rcpforms.emf;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import net.sf.rcpforms.modeladapter.configuration.IRangeAdapter;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.*;
import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * model adapter to bind against EMF models. You need to use EMFFormFactory to create
 * ValidationManager, since this returns a ValidationManager which creates EMFUpdateValueStrategy
 * instead of standard ones; </pre> Hopefully in future versions we find a better way to handle
 * this.
 * <p>
 * Types of EMF "properties" are the EStructuralFeature of the associated attributes, not the
 * datatypes of these attributes !
 * 
 * @author Marco van Meegen
 */
public class EMFModelAdapter extends ModelAdapter
{
    private static class EMFPropertyChain implements IPropertyChain
    {
        private String[] properties;

        private EClass beanMeta;

        public EMFPropertyChain(Object beanMeta, Object... properties)
        {
            assert beanMeta instanceof Class;
            assert properties != null && properties.length > 0;
            this.beanMeta = (EClass) beanMeta;
            this.properties = new String[properties.length];
            for (int i = 0; i < properties.length; i++)
            {
                if (properties[i] instanceof String)
                {
                    this.properties[i] = (String) properties[i];
                }
                else if (properties[i] instanceof EStructuralFeature)
                {
                    // TODO: optimize this access pattern
                    this.properties[i] = ((EStructuralFeature) properties[i]).getName();

                }
            }
        }

        public Object getModelMeta()
        {
            return beanMeta;
        }

        @Override
        public boolean equals(Object obj)
        {
            boolean result = false;
            if (obj instanceof EMFPropertyChain)
            {
                EMFPropertyChain propChain = (EMFPropertyChain) obj;
                if (propChain.getModelMeta() == getModelMeta()
                        && propChain.properties.length == properties.length)
                {
                    result = true;
                    for (int i = 0; i < properties.length; i++)
                    {
                        if (!properties[i].equals(propChain.properties[i]))
                        {
                            result = false;
                        }
                    }
                }
            }
            return result;
        }

        public EStructuralFeature getType()
        {
            return getPropertyDescriptor();
        }

        private static EStructuralFeature getUnnestedPropertyDescriptor(EClass beanClazz,
                                                                        String property)
            throws IntrospectionException
        {
            EStructuralFeature feature = beanClazz.getEStructuralFeature(property);
            return feature;
        }

        public void setValue(Object model, Object value)
        {
            // if (!(model instanceof EObject))
            // {
            // throw new IllegalArgumentException(
            // "EMFModelAdapter: Model Object must be an EObject");
            // }
            // EObject eobject = (EObject) model;
            // EStructuralFeature feature = getPropertyDescriptor();
            // try
            // {
            // eobject.eSet(feature, value);
            // }

            EStructuralFeature descriptor = null;
            String path = beanMeta.getName();
            EObject eobject = (EObject) model;
            boolean wasSet = false;
            try
            {
                String property = null;
                for (Object prop : properties)
                {
                    property = (String) prop;
                    path += "." + property; //$NON-NLS-1$
                    descriptor = getUnnestedPropertyDescriptor(eobject.eClass(), property);
                    if (prop == properties[properties.length - 1])
                    {
                        try
                        {
                            eobject.eSet(descriptor, value);
                            wasSet = true;
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            String message = "Exception in BeanAdapter: " + getClass().getName() //$NON-NLS-1$
                                    + " setting property " + property + ": " + ex.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
                            throw new IllegalArgumentException(message);
                        }
                    }
                    else
                    {
                        // access property of current "value" which can be again
                        // a model
                        try
                        {
                            eobject = (EObject) eobject.eGet(descriptor);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            String message = "Exception in BeanAdapter: " + getClass().getName() //$NON-NLS-1$
                                    + " accessing property " + property + ": " + ex.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
                            throw new IllegalArgumentException(message);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                throw new IllegalArgumentException(
                        "EMFModelAdapter: Exception setting feature \" + feature + \" on EObject "
                                + eobject + " to value " + value);
            }
            if (!wasSet)
            {
                throw new IllegalArgumentException("EMFModelAdapter: Exception setting property " //$NON-NLS-1$
                        + path);
            }
        }

        public Object getValue(Object model)
        {
            if (!(model instanceof EObject))
            {
                throw new IllegalArgumentException(
                        "EMFModelAdapter: Model Object must be an EObject");
            }
            EStructuralFeature descriptor = null;
            String path = beanMeta.getName();
            Object result = model;
            try
            {
                String property = null;
                for (Object prop : properties)
                {
                    property = (String) prop;
                    path += "." + property; //$NON-NLS-1$
                    EObject eobject = (EObject) result;
                    descriptor = getUnnestedPropertyDescriptor(eobject.eClass(), property);
                    // access property of current "value" which can be again a
                    // model
                    try
                    {
                        result = eobject.eGet(descriptor);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        String message = "Error in Provider: " + getClass().getName() //$NON-NLS-1$
                                + " accessing property " + property + ": " + ex.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
                        throw new IllegalArgumentException(message);
                    }

                }
            }
            catch (Exception ex)
            {
                System.err.println("Exception getting feature " + path + " on EObject " + result);
                ex.printStackTrace();
            }
            return result;
        }

        /**
         * gets the structural feature for the given path of properties starting from beanClass,
         * e.g. Address.class, "work","phone" will get the structural feature of property
         * Address.getWork().getPhone()
         */
        private EStructuralFeature getPropertyDescriptor()
        {
            EStructuralFeature desc = null;
            String path = beanMeta.getName();
            try
            {
                String property = null;
                EClassifier modelObjectClass = beanMeta;
                for (Object prop : properties)
                {
                    property = (String) prop;
                    path += "." + property;
                    desc = getUnnestedPropertyDescriptor((EClass) modelObjectClass, property);
                    modelObjectClass = desc.getEType();
                }
            }
            catch (ClassCastException ex)
            {
                throw new IllegalArgumentException("BeanAdapter: Exception getting property '"
                        + path + "': cannot access nested property of datatype, must be EClass");
            }
            catch (Exception ex)
            {
                throw new IllegalArgumentException("BeanAdapter: Exception getting property '"
                        + path + "'");
            }
            return desc;
        }

        @Override
        public String toString()
        {
            String result = "PropChain(" + beanMeta.getName();
            for (String prop : properties)
            {
                result += "." + prop;
            }
            return result + ")";
        }

        @Override
        public int hashCode()
        {
            // TODO: provide more accurate implementation
            return toString().hashCode();
        }
    }

    public EMFModelAdapter()
    {
        super(new EMFConverterRegistry());
    }

    private static EMFModelAdapter instance = new EMFModelAdapter();

    public static EMFModelAdapter getInstance()
    {
        return instance;
    }

    @Override
    public boolean canAdaptClass(Object metaClass)
    {
        return metaClass instanceof EClass;
    }

    public boolean canAdapt(Object modelInstance)
    {
        if (!(modelInstance instanceof EObject))
        {
            return false;
        }
        return super.canAdapt(modelInstance);
    }

    @Override
    public Object getMetaClass(Object modelInstanceToAdapt)
    {
        assert modelInstanceToAdapt instanceof EObject;
        return ((EObject) modelInstanceToAdapt).eClass();
    }

    @Override
    public IObservableValue getObservableValue(Object modelInstance, IPropertyChain propertyChain)
    {
        assert modelInstance instanceof EObject;
        assert propertyChain instanceof EMFPropertyChain;
        EMFPropertyChain chain = (EMFPropertyChain) propertyChain;
        EStructuralFeature feature = chain.getPropertyDescriptor();
        return EMFObservables.observeValue((EObject) modelInstance, feature);
    }

    @Override
    public IPropertyChain getPropertyChain(Object metaClass, Object... properties)
    {
        assert metaClass instanceof EClass;
        assert properties.length > 0;
        return new EMFPropertyChain(metaClass, properties);
    }

    /**
     * range adapters apply to EReference and EEnum types
     */
    @Override
    public IRangeAdapter getRangeAdapter(IPropertyChain propertyChain)
    {
        assert propertyChain instanceof EMFPropertyChain;
        EMFPropertyChain chain = (EMFPropertyChain) propertyChain;
        if (chain.getType().getEType() instanceof EEnum)
        {
            return new EEnumRangeAdapter((EEnum) chain.getType().getEType());
        }

        // TODO: generic way of retrieving instances for EReference;
        // does this make sense ? set a global resource to search for ?
        // if (chain.getType() instanceof EReference) {
        // return new EReferenceRangeAdapter((EReference) chain.getType());
        // }
        return null;
    }

    @Override
    public void validatePropertyPath(Object metaClass, String propertyPath, boolean writable)
    {
        // TODO: implement
    }

    @Override
    public IStructuredContentProvider createDefaultContentProvider()
    {
        return new ObservableListEObjectContentProvider();
    }
}
