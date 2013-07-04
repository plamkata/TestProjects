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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.rcpforms.modeladapter.converter.ConverterRegistry;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;
import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * abstract base class to make your data model work with the rcpforms framework. All communication
 * of the framework with the model is routed through this adapter, thus enabling to use arbitrary
 * model classes with the framework.
 * <p>
 * The recommended type is a standard java bean, another type which is supported by the rcpforms emf
 * plugins is an emf EClass or EObject.
 * <p>
 * A ModelAdapter accesses properties of a model using a <b>propertyPath</b> string representation
 * which addresses an <b>attribute</b> of the model using nested properties, e.g. if personInstance
 * is an object of type Person.class and has an attribute address of type Address.class which in
 * turn has an attribute "street" of type String.class, the propertyPath "attribute.street" would
 * address this attribute.
 * <p>
 * For more efficient handling a propertyPath can be converted into an internal representation <b>
 * {@link IPropertyChain}</b> which is model dependent, but might cache property lookup information.
 * <p>
 * Responsibilities of a ModelAdapter:
 * <ol>
 * <li>provide an {@link IObservableValue} or {@link IObservableList} for a property addressed by
 * meta-information, which will be used by data binding to bind against widgets.
 * <li>provide read/write methods for a property
 * <li>provide binding type information for a property
 * <li>provide allowed value sets (ranges) for properties; these come in handy if enums or enum-like
 * types are bound.
 * <li>TODO LATER: provide information if a property is required
 * <li>TODO LATER: provide additional validators for the data model and/or the property value, e.g.
 * email address, phone number etc.
 * </ol>
 * 
 * @author Marco van Meegen
 */
public abstract class ModelAdapter
{
    private ConverterRegistry converterRegistry = null;

    private static final Logger LOG = Logger.getLogger(ModelAdapter.class.getName());

    private static List<ModelAdapter> adapters = new ArrayList<ModelAdapter>();

    /** create a new model adapter using the given converter registry */
    public ModelAdapter(ConverterRegistry converterRegistry)
    {
        Validate.notNull(converterRegistry);
        this.converterRegistry = converterRegistry;
    }

    /**
     * get a model adapter for the given model instance. All registered adapters are tested if they
     * can adapt the given instance. If none is found, the default bean adapter is returned.
     * 
     * @return a model adapter for the given instance, never null
     */
    public static ModelAdapter getAdapterForInstance(Object object)
    {
        for (ModelAdapter adapter : adapters)
        {
            if (adapter.canAdapt(object))
            {
                return adapter;
            }
        }
        return BeanAdapter.getInstance();
    }

    /**
     * if the passed object is an object which can be handled by this adapter, it returns the
     * metaclass for this object.
     * 
     * @return meta class or null if object cannot be handled by this adapter
     */
    public abstract Object getMetaClass(Object modelInstanceToAdapt);

    /**
     * get a model adapter for the given model object metaclass/type. All registered adapters are
     * tested if they can adapt the given type. If none is found, the default bean adapter is
     * returned.
     * 
     * @return a model adapter for the given class, never null
     * @throws IllegalArgumentException if no adapter can be found for the given object
     */
    public static ModelAdapter getAdapterForMetaClass(Object metaClassToAdapt)
    {
        for (ModelAdapter adapter : adapters)
        {
            if (adapter.canAdaptClass(metaClassToAdapt))
            {
                return adapter;
            }
        }
        return BeanAdapter.getInstance();
    }

    /** check if the given model object can be handled by this adapter */
    public boolean canAdapt(Object modelInstance)
    {
        return canAdaptClass(getMetaClass(modelInstance));
    }

    /**
     * check if objects of the given meta class object can be handled by this adapter
     */
    public abstract boolean canAdaptClass(Object metaClass);

    /**
     * checks if the given propertyPath is valid, throws an exception if not
     * 
     * @param metaClass metaclass
     * @param propertyPath path
     * @param writable if true, validation is done to ensure property is writable
     * @throws IllegalArgumentException with descriptive error message if property is not valid
     */
    public abstract void validatePropertyPath(Object metaClass, String propertyPath,
                                              boolean writable);

    /**
     * gets an observable value from the given model object for the property of the model defined by
     * the given property meta information.
     * <p>
     * For a bean this encapsulates the property name, for an emf model this is an instance of
     * EStructuralFeature, for your special data model this might be your special metadata object.
     * <p>
     * The metadata is passed as object around the system; the only class which must be able to deal
     * with these meta-objects is your ModelAdapter.
     */
    public abstract IObservableValue getObservableValue(Object modelInstance,
                                                        IPropertyChain propertyChain);

    /**
     * gets a detail observable value from the given model object for the property of the model
     * defined by the given property meta information. NOTE: This function provides no useful
     * implementation and will throw an IllegalArgumentException. You have to override it in
     * subclasses.
     * 
     * @param masterBeanObservableValue master value to bind the detail
     * @param propertyChain property chain for the detail property
     * @return Observable value for the detail binding
     */
    public IObservableValue getObservableDetailValue(IObservableValue masterBeanObservableValue,
                                                     IPropertyChain propertyChain)
    {
        throw new IllegalArgumentException(
                "This function provides no useful implementation. You have to override it in subclasses if you need detail binding support"); //$NON-NLS-1$
    }

    /**
     * get the value of the attribute defined by the propertyChain.
     * 
     * @param modelInstance model instance to retrieve attribute from
     * @param propertyChain addresses the attribute
     * @return value
     */
    public final Object getValue(Object element, IPropertyChain propertyChain)
    {
        return propertyChain.getValue(element);
    }

    public void setValue(Object modelInstance, IPropertyChain propertyChain, Object value)
    {
        propertyChain.setValue(modelInstance, value);
    }

    /**
     * convenience method to get the value of the attribute defined by the propertyPath, which may
     * be nested like "person.address.street".
     * 
     * @param modelInstance model instance to retrieve attribute from
     * @param propertyChain addresses the attribute
     * @return value
     */
    public final Object getValue(Object modelInstance, String propertyPath)
    {
        Object result = null;
        try
        {
            Object metaClass = getMetaClass(modelInstance);
            String[] properties = split(propertyPath);
            IPropertyChain chain = getPropertyChain(metaClass, (Object[]) properties);
            result = getValue(modelInstance, chain);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            String message = "Error in ModelAdapter: " + getClass().getName() //$NON-NLS-1$
                    + " accessing property " + propertyPath + ": " + ex.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
            LOG.severe(message);
            throw new IllegalArgumentException(message);
        }
        return result;
    }

    /**
     * @param propertyChain attribute
     * @return type of the attribute, this is a meta concept and is used by databinding to determine
     *         converters; usually it will be the Class of the attribute, e.g. Integer.class
     */
    public final Object getType(IPropertyChain propertyChain)
    {
        return propertyChain.getType();
    }

    /**
     * convenience method to set the value of the attribute defined by the propertyPath.
     * 
     * @param modelInstance model instance to retrieve attribute from
     * @param propertyPath addresses the attribute
     * @param value value to set
     */
    public final void setValue(Object modelInstance, String propertyPath, Object value)
    {
        Object metaClass = getMetaClass(modelInstance);
        String[] properties = split(propertyPath);
        IPropertyChain chain = getPropertyChain(metaClass, (Object[]) properties);
        setValue(modelInstance, chain, value);
    }

    /**
     * gets a range adapter for the attribute addressed by the propertyChain. This enables models to
     * deliver a range of values which the property can take and which can be displayed in combo box
     * lists or in radio groups.
     * <p>
     * The range adapter will deliver an input, label and content provider which defines the range
     * of values and how to display it. This information will be automatically utilized by the
     * framework to configure combo boxes to enable easy binding of enum-like values without
     * explicitly defining the range, since it can be deduced from meta information (Java enums or
     * extensible enums read from a database or ...).
     * <p>
     * Another example: For EMF EReference references a range adapter can be provided which
     * retrieves all objects of the type of the EReference in the model.
     * 
     * @param propertyChain property chain addressing the attribute
     * @return range adapter or null, if no range adapter is available
     */
    public abstract IRangeAdapter getRangeAdapter(IPropertyChain propertyChain);

    /**
     * wraps the property chain into a suitable object for easier passing around . validates if the
     * properties are valid.
     */
    public abstract IPropertyChain getPropertyChain(Object metaClass, Object... properties);

    public final IPropertyChain getPropertyChain(Object metaClass, String propertyPath)
    {
        return getPropertyChain(metaClass, (Object[]) split(propertyPath));
    }

    /**
     * registers a model adapter to use the framework with different kinds of data models, e.g.
     * Maps, Beans, EMF Objects
     * 
     * @return true if adapter was added, false if adapter was not added because it was already
     *         registered
     */
    public static boolean registerAdapter(ModelAdapter adapter)
    {
        boolean result = false;
        if (!adapters.contains(adapter))
        {
            result = adapters.add(adapter);
        }
        return result;
    }

    /**
     * unregisters a model adapter to use the framework with different kinds of data models, e.g.
     * Maps, Beans, EMF Objects
     * 
     * @return true if adapter was removed, false if it was not found
     */
    public static boolean unregisterAdapter(ModelAdapter adapter)
    {
        boolean found = adapters.remove(adapter);
        return found;
    }

    /**
     * splits the given nested property of type string into an array of properties. An nested
     * property is specified as string1[\..+]*, e.g. person.address.street and addresses properties
     * which are nested in other properties.
     * 
     * @param nestedProperty , not null, must have at least one fragment
     * @return array of properties, first in string is array[0], array.length >= 1
     * @throws IllegalArgumentException if string is not valid, i.e. contains empty segments
     */
    public String[] split(String nestedProperty)
    {
        Validate.notNull(nestedProperty);
        Validate.isTrue(nestedProperty.length() > 0, "nestedProperty must not be empty"); //$NON-NLS-1$
        String[] result = nestedProperty.split("\\."); //$NON-NLS-1$
        Validate.notEmpty(result, "nestedProperty must contain at least one segment"); //$NON-NLS-1$
        for (String string : result)
        {
            Validate.notEmpty(string, nestedProperty + " must not contain empty segments"); //$NON-NLS-1$
        }
        return result;
    }

    /**
     * @return converter registry associated with this model adapter; all converters and validators
     *         are taken from here
     */
    public ConverterRegistry getConverterRegistry()
    {
        return converterRegistry;

    }

    /**
     * @return new default content provider instance used by TableUtil to configure a table 
     */
    public abstract IStructuredContentProvider createDefaultContentProvider();
}
