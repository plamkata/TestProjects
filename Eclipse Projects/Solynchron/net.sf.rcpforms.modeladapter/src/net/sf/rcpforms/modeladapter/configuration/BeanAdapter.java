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
// Created on 02.05.2008

package net.sf.rcpforms.modeladapter.configuration;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.rcpforms.modeladapter.configuration.IntegerRangeAdapter.IntRange;
import net.sf.rcpforms.modeladapter.converter.BeanConverterRegistry;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;
import net.sf.rcpforms.modeladapter.tables.ObservableListBeanContentProvider;
import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * Adapter to use plain java beans as presentation model.
 * 
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class BeanAdapter extends ModelAdapter
{
    private static final Logger LOG = Logger.getLogger(BeanAdapter.class.getName());

    private static class PropertyChain implements IPropertyChain
    {
        private String[] properties;

        private Class<?> beanMeta;

        public PropertyChain(Object beanMeta, Object... properties)
        {
            Validate.isTrue(beanMeta instanceof Class);
            Validate.isTrue(properties != null && properties.length > 0);
            this.beanMeta = (Class<?>) beanMeta;
            this.properties = new String[properties.length];
            System.arraycopy(properties, 0, this.properties, 0, properties.length);
        }

        public Object getModelMeta()
        {
            return beanMeta;
        }

        @Override
        public boolean equals(Object obj)
        {
            boolean result = false;
            if (obj instanceof PropertyChain)
            {
                PropertyChain propChain = (PropertyChain) obj;
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

        public Class<?> getType()
        {
            return getPropertyDescriptor().getPropertyType();
        }

        private static PropertyDescriptor getUnnestedPropertyDescriptor(Class<?> beanClazz,
                                                                        String property)
            throws IntrospectionException
        {
            PropertyDescriptor descriptor = null;

            BeanInfo beanInfo = Introspector.getBeanInfo(beanClazz);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : descriptors)
            {
                if (property.equals(propertyDescriptor.getName()))
                {
                    descriptor = propertyDescriptor;
                }
            }
            return descriptor;
        }

        /**
         * gets the property descriptor for the given path of properties starting from beanClass,
         * e.g. Address.class, "work","phone" will get the property descriptor of property
         * Address.getWork().getPhone()
         */
        private PropertyDescriptor getPropertyDescriptor()
        {
            PropertyDescriptor desc = null;
            String path = beanMeta.getName();
            try
            {
                String property = null;
                Class<?> modelObjectClass = beanMeta;
                for (Object prop : properties)
                {
                    property = (String) prop;
                    path += "." + property; //$NON-NLS-1$
                    desc = getUnnestedPropertyDescriptor(modelObjectClass, property);
                    modelObjectClass = desc.getPropertyType();
                }
            }
            catch (Exception ex)
            {
                throw new IllegalArgumentException("BeanAdapter: Exception getting property '" //$NON-NLS-1$
                        + path + "'"); //$NON-NLS-1$
            }
            return desc;
        }

        /**
         * retrieves the value of the property in the given model
         * 
         * @param model
         */
        public Object getValue(Object model)
        {
            Validate
                    .isTrue(
                            ModelAdapter.getAdapterForInstance(model).getMetaClass(model) == beanMeta,
                            "Model Object has not the same metaclass which was passed for property descriptor construction"); //$NON-NLS-1$
            PropertyDescriptor descriptor = null;
            String path = beanMeta.getName();
            Object result = model;
            try
            {
                String property = null;
                Class<?> modelObjectClass = beanMeta;
                for (Object prop : properties)
                {
                    
                    property = (String) prop;
                    path += "." + property; //$NON-NLS-1$
                    descriptor = getUnnestedPropertyDescriptor(modelObjectClass, property);
                    modelObjectClass = descriptor.getPropertyType();
                    // access property of current "value" which can be again a
                    // model
                    try
                    {
                        Method method = descriptor.getReadMethod();
                        result = method.invoke(result, new Object[]{});
                        //if a (nested) property is null, all the properties of the nested property will also be null, and so on...
                        //break here search for prop value and return null
                        if(result == null)
                            break;
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        String message = "Error in Provider: " + getClass().getName() //$NON-NLS-1$
                                + " accessing property " + property + ": " + ex.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
                        LOG.severe(message);
                        throw new IllegalArgumentException(message);
                    }

                }
            }
            catch (Exception ex)
            {
                throw new IllegalArgumentException("BeanAdapter: Exception getting property '" //$NON-NLS-1$
                        + path + "'"); //$NON-NLS-1$
            }
            return result;

        }

        /**
         * sets the value of the property in the given model
         * 
         * @param model
         */
        public void setValue(Object model, Object value)
        {
            Validate
                    .isTrue(
                            ModelAdapter.getAdapterForInstance(model).getMetaClass(model) == beanMeta,
                            "Model Object has not the same metaclass which was passed for property descriptor construction"); //$NON-NLS-1$
            PropertyDescriptor descriptor = null;
            String path = beanMeta.getName();
            Object result = model;
            boolean wasSet = false;
            try
            {
                String property = null;
                Class<?> modelObjectClass = beanMeta;
                for (Object prop : properties)
                {
                    property = (String) prop;
                    path += "." + property; //$NON-NLS-1$
                    descriptor = getUnnestedPropertyDescriptor(modelObjectClass, property);
                    modelObjectClass = descriptor.getPropertyType();
                    if (prop == properties[properties.length - 1])
                    {
                        try
                        {
                            Method method = descriptor.getWriteMethod();
                            method.invoke(result, new Object[]{value});
                            wasSet = true;
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            String message = "Exception in BeanAdapter: " + getClass().getName() //$NON-NLS-1$
                                    + " setting property " + property + ": " + ex.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
                            LOG.severe(message);
                            throw new IllegalArgumentException(message);
                        }
                    }
                    else
                    {
                        // access property of current "value" which can be again
                        // a model
                        try
                        {
                            Method method = descriptor.getReadMethod();
                            result = method.invoke(result, new Object[]{});
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            String message = "Exception in BeanAdapter: " + getClass().getName() //$NON-NLS-1$
                                    + " accessing property " + property + ": " + ex.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
                            LOG.severe(message);
                            throw new IllegalArgumentException(message);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                throw new IllegalArgumentException("BeanAdapter: Exception setting property '" //$NON-NLS-1$
                        + path + "'"); //$NON-NLS-1$
            }
            if (!wasSet)
            {
                throw new IllegalArgumentException("BeanAdapter: Exception setting property " //$NON-NLS-1$
                        + path);
            }
        }

        @Override
        public String toString()
        {
            String result = "PropChain(" + beanMeta.getName(); //$NON-NLS-1$
            for (String prop : properties)
            {
                result += "." + prop; //$NON-NLS-1$
            }
            return result + ")"; //$NON-NLS-1$
        }

        @Override
        public int hashCode()
        {
            return toString().hashCode();
        }
    }

    private static BeanAdapter instance = new BeanAdapter();

    private BeanAdapter()
    {
        super(new BeanConverterRegistry());
    }

    public static BeanAdapter getInstance()
    {
        return instance;
    }

    /**
     * a bean adapter can adapt any class
     */
    @Override
    public boolean canAdaptClass(Object metaClassToAdapt)
    {
        return metaClassToAdapt instanceof Class;
    };

    /**
     * gets an observable value for the given property from the given data model bean
     */
    public IObservableValue getObservableValue(Object bean, IPropertyChain propertyChain)
    {
        IObservableValue returnValue = null;
//        Object originalBean = bean;
        PropertyChain chain = (PropertyChain) propertyChain;
        // handle nested properties here...
        String property = ""; //$NON-NLS-1$

        if (chain.properties.length > 1)
        {
            //handle every nested property as master-detail observable
            returnValue = this.createMasterDetailBindingForNestedNullProperty(bean, chain);

//            bean = this.getNestedProperty(bean, chain);
            //fix if nested bean is not set yet -> use master-detail binding as workaround for < Eclipse 3.5 binding apis
            //FIXME remsy refactor to: "IObservableValue v = BeanProperties.value("address.street.foo.bar...").observe(bean);" as soon as eclipse 3.5 is used
//            if(bean == null)
//            {
//                returnValue = this.createMasterDetailBindingForNestedNullProperty(originalBean, chain);
//            }
            // take last segment as new property
//            property = chain.properties[chain.properties.length - 1];

        }
        else
        {
            property = chain.properties[0];
            returnValue = BeansObservables.observeValue(bean, property);
        }

        return returnValue;
    }
    
    /*
     * eclipse.platform.jface: [Databinding] binding to objects attributes from 10.06.2009 14:45
     * Hi,
     * 
     * With 3.4 you need to do this your own
     * -----------8<-----------
     * IObservable parent = BeansObservables.observeValue(person,"address");
     * IObservableValue child =
     * BeansObservables.observeDetail(parent,"street",Address.class);
     * -----------8<-----------
     * 
     * With 3.5 this works out of the box (when using Properties-API):
     * 
     * -----------8<-----------
     * IObservableValue v = BeanProperties.value("address.street").observe(person);
     * -----------8<-----------
     * 
     * You can read some details in my current blog. I'm discussing this at
     * EMF-Level but the code is the same for Beans. The nice thing with 3.5 is
     * that this even works in Table/Trees  :-) 
     * 
     * Tom
     * 
     * 
     * 
     */
    /**
     * @param bean
     * @param propertyChain with chain.properties.length == 2
     * @return
     */
    private IObservableValue createMasterDetailBindingForNestedNullProperty(Object bean, PropertyChain propertyChain)
    {
        Object masterBean = bean;
        
        IObservableValue parent = null;
        IObservableValue child = null;
        for(int c = 0; c < propertyChain.properties.length - 1; ++c)
        {
            String currentMasterProperty = propertyChain.properties[c];
            PropertyDescriptor pd = this.getPropertyDescriptor(masterBean.getClass(), currentMasterProperty);
            // access property of current "value" which can be again a model
            Method method = pd.getReadMethod();
            try
            {
                parent = BeansObservables.observeValue(masterBean, currentMasterProperty);
                //FIXME when is the parent observable value disposed?! how to dispose parent observable value if rebind is enabled? possible memory leak!
                Class<?> propertyType = this.getPropertyType(masterBean, currentMasterProperty, propertyChain.properties[c+1]);
                masterBean = method.invoke(masterBean, new Object[]{});
                child =
                BeansObservables.observeDetailValue(parent.getRealm(), parent, propertyChain.properties[c+1], propertyType);
            }
            catch (IllegalArgumentException e)
            {
                LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            catch (IllegalAccessException e)
            {
                LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            catch (InvocationTargetException e)
            {
                LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            //if a nested bean in nesting hierarchy is null -> no binding is possible
            //except: the last bean object can be null!
            if(masterBean == null &&
                    c < propertyChain.properties.length - 2)
            {
                throw new IllegalArgumentException("Nested bean object \"" + propertyChain.properties[c+1] + "\" in PropertyChain \"" + propertyChain.toString() + "\" is null! Only the last nested bean is allowed to be null. No binding is done!");
            }
        }
        return child;
    }

    private Class<?> getPropertyType(Object bean, String masterProperty, String detailProperty)
    {
        try
        {
            Class<?> propertyType = null;
            PropertyDescriptor descriptor = this.getPropertyDescriptor(bean.getClass(), masterProperty);
            if(descriptor != null)
            {
                descriptor = this.getPropertyDescriptor(descriptor.getPropertyType(), detailProperty);
                if(descriptor != null)
                    propertyType = descriptor.getPropertyType();
            }
            
            return propertyType;
        }
        catch (SecurityException e)
        {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return null;
    }

    private PropertyDescriptor getPropertyDescriptor(Class<? extends Object> clazz,
                                                     String property)
    {
        BeanInfo beanInfo;
        PropertyDescriptor descriptor = null;
        try
        {
            beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : descriptors)
            {
                if (property.equals(propertyDescriptor.getName()))
                {
                    descriptor = propertyDescriptor;
                }
            }
        }
        catch (IntrospectionException e)
        {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return descriptor;
    }

    /**
     * gets a detail observable value for the given property from the given data model bean
     */
    public IObservableValue getObservableDetailValue(IObservableValue masterBeanObservableValue,
                                                     IPropertyChain propertyChain)
    {
        PropertyChain chain = (PropertyChain) propertyChain;
        // handle nested properties here...
        String property = ""; //$NON-NLS-1$

        // FIXME nested properties not yet supported
        Validate.isTrue(chain.properties.length == 1,
                "NestedProperties for master detail binding are not supported!"); //$NON-NLS-1$
        property = chain.properties[0];

        Validate
                .notNull(
                        Realm.getDefault(),
                        "Make sure a Databinding Realm is set; you should wrap your main method into Realm.runWithDefault() to provide one"); //$NON-NLS-1$

        return BeansObservables.observeDetailValue(masterBeanObservableValue.getRealm(),
                masterBeanObservableValue, property, chain.getType());
    }

    private Object getNestedProperty(Object bean, PropertyChain chain)
    {
        //TODO refactor to use commons beanutils
//        StringBuilder sb = new StringBuilder(chain.properties.length * 5);
//        for(String c : chain.properties)
//        {
//            sb.append(c);
//            sb.append(".");
//        }
//        String nestedPropertyString = sb.substring(0, sb.length() - 1); // remove last point!
//        try
//        {
//            Object nestedPropertyObject = PropertyUtils.getNestedProperty(bean, nestedPropertyString);
//        }
//        catch (IllegalAccessException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        catch (InvocationTargetException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        catch (NoSuchMethodException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        Object nestedBean = bean;
        Class<?> metaClass = chain.beanMeta;
        for (int c = 0; c < chain.properties.length - 1; ++c)
        {
            try
            {
                PropertyDescriptor pd = PropertyChain.getUnnestedPropertyDescriptor(metaClass,
                        chain.properties[c]);
                // access property of current "value" which can be again a model
                Method method = pd.getReadMethod();
                nestedBean = method.invoke(nestedBean, new Object[]{});
                //check that nestedBean is set an not null, otherwise return null
                if(null != nestedBean){
                    metaClass = nestedBean.getClass();
                }else
                {
                    nestedBean = null;
                    break;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                String message = "Error in Adapter: " //$NON-NLS-1$
                        + getClass().getName()
                        + ". Error occurred in Object: \"" //$NON-NLS-1$
                        + nestedBean.toString()
                        + "\" accessing property " //$NON-NLS-1$
                        + chain.properties[c]
                        + "; !\n ErrorMessage is: " //$NON-NLS-1$
                        + ex.getMessage();
                LOG.severe(message);
                throw new IllegalArgumentException(message);
            }

        }
        return nestedBean;
    }

    @Override
    public Class<?> getMetaClass(Object modelObjectToAdapt)
    {
        Validate.isTrue(!(modelObjectToAdapt instanceof Class),
                "Please pass an instance of the class, not the class itself"); //$NON-NLS-1$
        return modelObjectToAdapt.getClass();
    }

    /**
     * get a range adapter for a property which is used to fill combo items when combo box binding
     * is used. very convenient for enums and enum-like data types with a limited range of values.
     * <p>
     * This can even be used for choosing references to other objects via combo.
     */
    @SuppressWarnings("unchecked")
    @Override
    public IRangeAdapter getRangeAdapter(IPropertyChain propertyChain)
    {
        Validate.isTrue(propertyChain instanceof PropertyChain);
        PropertyChain chain = (PropertyChain) propertyChain;
        if (chain.getType().isEnum())
        {
            return new EnumRangeAdapter((Class<? extends Enum<?>>) chain.getType());

        }
        // check for an integerRange annotation on the field
        if (chain.getType().equals(Integer.TYPE))
        {
            // TODO make sure only one property is set. meta information for nested properties not
            // supported yet
            if (chain.properties.length == 1)
            {
                try
                {
                    Field[] fields = chain.beanMeta.getFields();
                    for (Field f : fields)
                    {
                        if (!f.isSynthetic() && Modifier.isStatic(f.getModifiers())) {
                            // get the value of the public static field
                            // only static members can be accessed from the modelMeta Class
                            Object fieldName = f.get(chain.getModelMeta());
                            if (fieldName.equals(chain.properties[0]))
                            {
                                for (Annotation a : f.getAnnotations())
                                {
                                    if (a instanceof IntRange)
                                        return new IntegerRangeAdapter(((IntRange) a).minValue(),
                                                ((IntRange) a).maxValue(), ((IntRange) a).step());
                                }
                            }
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    LOG.severe("Exception in BeanAdapter.getRangeAdapter() for IntRange annotation: " //$NON-NLS-1$
                                    + e.getLocalizedMessage());
                }
            }
        }

        return null;
    }

    @Override
    public IPropertyChain getPropertyChain(Object beanMeta, Object... properties)
    {
        return new PropertyChain(beanMeta, properties);
    }

    /**
     * checks if the given propertyPath is valid, throws an exception if not
     * 
     * @param metaClass metaclass
     * @param propertyPath path
     * @param writable if true, validation is done to ensure property is writable
     */
    public void validatePropertyPath(Object metaClass, String propertyPath, boolean writable)
    {
        String message = ""; //$NON-NLS-1$
        String msgHead = "PropertyPath " + propertyPath + ": "; //$NON-NLS-1$ //$NON-NLS-2$
        try
        {

            PropertyChain propertyChain = (PropertyChain) getPropertyChain(metaClass, propertyPath);
            PropertyDescriptor desc = propertyChain.getPropertyDescriptor();
            if (desc.getReadMethod() == null)
            {
                message += msgHead + " read method not available\n"; //$NON-NLS-1$
            }
            if (writable && desc.getWriteMethod() == null)
            {
                message += msgHead + " write method not available for editable column\n"; //$NON-NLS-1$
            }
        }
        catch (IllegalArgumentException ex)
        {
            message += msgHead + " exception accessing property " + ex.getMessage() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * create a list content provider which reacts on changes of contained beans too
     */
    @Override
    public IStructuredContentProvider createDefaultContentProvider()
    {
        return new ObservableListBeanContentProvider();
    }
}
