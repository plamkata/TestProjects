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

package net.sf.rcpforms.modeladapter.converter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sf.rcpforms.modeladapter.util.ClassLookupSupport;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;

/**
 * Class ConverterRegistry enhances the default converters defined in the standard databinding
 * {@link UpdateValueStrategy}.
 * <p>
 * Converters are defined for a from and a to type; usually this are class objects, but for models
 * which supply their own meta data (e.g. EMF), they are not; thus the type Object is used.
 * 
 * @author Marco van Meegen, Remo Loetscher
 */
public abstract class ConverterRegistry
{
    private AccessibleConverterUpdateStrategy defaultStrategy;

    protected volatile HashMap<Object, HashMap<Object, IConverter>> converterMap = new HashMap<Object, HashMap<Object, IConverter>>();

    protected volatile HashMap<Object, HashMap<Object, IValidator>> validatorMap = new HashMap<Object, HashMap<Object, IValidator>>();

    /**
     * creates a new converter registry which takes its default converters and validators from the
     * hacked UpdateStrategy from standard eclipse databinding
     */
    protected ConverterRegistry(AccessibleConverterUpdateStrategy updateStrategy)
    {
        defaultStrategy = updateStrategy;
    }

    /**
     * removes all registered converters and validators. Must be overridden by subclass if subclass
     * adds specific converters and validators.
     */
    public void reset()
    {
        converterMap.clear();
        validatorMap.clear();
    }

    /**
     * Register converter for the given tromType and toType. If a converter already exists it will
     * be replaced
     * 
     * @param fromType source type
     * @param toType type to convert to
     * @param converter converter which handle the conversion process
     */
    public void registerConverter(Object fromType, Object toType, IConverter converter)
    {
        HashMap<Object, IConverter> fromConverterMap = converterMap.get(fromType);
        // from converter added the first time
        if (fromConverterMap == null)
        {
            fromConverterMap = new HashMap<Object, IConverter>();
            converterMap.put(fromType, fromConverterMap);
        }
        // register converter under the toType
        fromConverterMap.put(toType, converter);

    }

    /**
     * Removes an already registered converter or throws an IllegalArgumentException if no
     * registered converter is found
     * 
     * @param fromType source type
     * @param toType type to convert to
     * @throws IllegalArgumentException Throws IllegalArgumentException exception if either fromType
     *             ore toType were not registered
     */
    public void removeConverter(Object fromType, Object toType)
    {
        HashMap<Object, IConverter> fromConverterMap = converterMap.get(fromType);
        // from converter not yet added or toConverter not yet added
        if (fromConverterMap == null || fromConverterMap.get(toType) == null)
        {
            throw new IllegalArgumentException("Converter from " + fromType + " to " + toType //$NON-NLS-1$ //$NON-NLS-2$
                    + " was not registered!"); //$NON-NLS-1$
        }
        // remove converter from map
        fromConverterMap.remove(toType);
        // remove map, if its empty
        if (fromConverterMap.isEmpty())
            converterMap.remove(fromType);
    }

    /**
     * Register validotor for the given tromType and toType. If a validator already exists it will
     * be replaced
     * 
     * @param fromType source type
     * @param toType type to convert to
     * @param valdiator valdiator which handle the validation process
     */
    public void registerValidator(Object fromType, Object toType, IValidator valdiator)
    {
        HashMap<Object, IValidator> fromValidatorMap = validatorMap.get(fromType);
        // from converter added the first time
        if (fromValidatorMap == null)
        {
            fromValidatorMap = new HashMap<Object, IValidator>();
            validatorMap.put(fromType, fromValidatorMap);
        }
        // register converter under the toType
        fromValidatorMap.put(toType, valdiator);

    }

    /**
     * Removes an already registered validator or throws an IllegalArgumentException if no
     * registered validator is found
     * 
     * @param fromType source type
     * @param toType type to convert to
     * @throws IllegalArgumentException Throws IllegalArgumentException exception if either fromType
     *             ore toType were not registered
     */
    public void removeValidator(Object fromType, Object toType)
    {
        HashMap<Object, IValidator> fromValidatorMap = validatorMap.get(fromType);
        // from converter not yet added or toConverter not yet added
        if (fromValidatorMap == null || fromValidatorMap.get(toType) == null)
        {
            throw new IllegalArgumentException("Validator from " + fromType + " to " + toType //$NON-NLS-1$ //$NON-NLS-2$
                    + " was not registered!"); //$NON-NLS-1$
        }
        // remove converter from map
        fromValidatorMap.remove(toType);
        // remove map, if its empty
        if (fromValidatorMap.isEmpty())
            validatorMap.remove(fromType);
    }

    /**
     * create a converter from fromType to toType, using all default converters, and the ones needed
     * by rcpforms or added to the registry. This method returns the converter that matches best.
     * <ul>
     * <li>If fromType is registered: the converter for that type will be returned.
     * <li>Only if fromType and toType are class objects: If fromType is not registered but the one
     * interface which is realized in this type: the converter for this interface will be returned.
     * <li>Only if fromType and toType are class objects: If fromType is not registered but there
     * are >1 interfaces which are realized in this type: not defined which converter for which
     * interface will be returned.
     * <li>Only if fromType and toType are class objects:If fromType itself (or realized interfaces
     * in this type) is not registered but is a subtype of an already registered converter, this
     * converter will be returned. superclass-implementation has higher priority than in superclass
     * realized interface(s).
     * <li>Only if fromType and toType are class objects: If in superclass more than one interfaces
     * are realized: not defined which converter for which interface will be returned.
     * </ul>
     * The same way works the toType matching-logic.
     * 
     * @param fromType class or other meta type to convert instances from
     * @param toType class or other meta type to convert instances to
     * @return converter or null if none found
     */
    public IConverter getConverter(Object fromTypeObject, Object toTypeObject)
    {
        IConverter result = null;
        // check if objects are of type Class
        if (fromTypeObject instanceof Class && toTypeObject instanceof Class)
        {
            Class<?> fromType = (Class<?>) fromTypeObject;
            Class<?> toType = (Class<?>) toTypeObject;
            if (converterMap.get(fromType) != null
                    && converterMap.get(fromType).get(toType) != null)
            {
                result = converterMap.get(fromType).get(toType);
            }
            // if no converter was found -> have a detailed look
            if (result == null)
            {
                // search here the whole hierarchy of fromType and/or toType
                result = searchClassHierarchiesForConverter(fromType, toType);
            }

        }
        else if (converterMap.get(fromTypeObject) != null
                && converterMap.get(fromTypeObject).get(toTypeObject) != null)
        {
            // direct match of from and to object found
            result = converterMap.get(fromTypeObject).get(toTypeObject);
        }

        // if no converter was found -> create defaults.
        if (result == null)
        {
            result = defaultStrategy.createConverter(fromTypeObject, toTypeObject);
        }
        return result;
    }

    /**
     * create a validator from fromType to toType, using all default validators, and the ones needed
     * by rcpforms or added to the registry. This method returns the validator that matches best.
     * <ul>
     * <li>If fromType is registered: the validator for that type will be returned.
     * <li>Only if fromType and toType are class objects: If fromType is not registered but the one
     * interface which is realized in this type: the validator for this interface will be returned.
     * <li>Only if fromType and toType are class objects: If fromType is not registered but there
     * are >1 interfaces which are realized in this type: not defined which validator for which
     * interface will be returned.
     * <li>Only if fromType and toType are class objects:If fromType itself (or realized interfaces
     * in this type) is not registered but is a subtype of an already registered validator, this
     * validator will be returned. superclass-implementation has higher priority than in superclass
     * realized interface(s).
     * <li>Only if fromType and toType are class objects: If in superclass more than one interfaces
     * are realized: not defined which validator for which interface will be returned.
     * </ul>
     * The same way works the toType matching-logic.
     * 
     * @param fromType class or other meta type to validate instances from
     * @param toType class or other meta type to validate instances to
     * @return validator or null if none found
     */
    public IValidator getValidator(Object fromTypeObject, Object toTypeObject)
    {
        IValidator result = null;
        // check if objects are of type Class
        if (fromTypeObject instanceof Class && toTypeObject instanceof Class)
        {
            Class<?> fromType = (Class<?>) fromTypeObject;
            Class<?> toType = (Class<?>) toTypeObject;
            if (validatorMap.get(fromType) != null
                    && validatorMap.get(fromType).get(toType) != null)
            {
                result = validatorMap.get(fromType).get(toType);
            }
            // if no converter was found -> have a detailed look
            if (result == null)
            {
                // search here the whole hierarchy of fromType and/or toType
                result = searchClassHierarchiesForValidator(fromType, toType);
            }

        }
        else if (validatorMap.get(fromTypeObject) != null
                && validatorMap.get(fromTypeObject).get(toTypeObject) != null)
        {
            // direct match of from and to object found
            result = validatorMap.get(fromTypeObject).get(toTypeObject);
        }

        // if no converter was found -> create defaults.
        if (result == null)
        {
            result = defaultStrategy.createValidator(fromTypeObject, toTypeObject);
        }
        return result;
    }

    private IConverter searchClassHierarchiesForConverter(Class<?> fromType, Class<?> toType)
    {

        Class<?>[] fromHierarchy = ClassLookupSupport.getTypeHierarchyFlattened(fromType);
        Class<?>[] toHierarchy = ClassLookupSupport.getTypeHierarchyFlattened(toType);
        for (Class<?> fromClazz : fromHierarchy)
        {
            // check if converter is registered for specific class, realized
            // interface in this class or superclasse(s) with realized
            // interface(s)
            if (converterMap.get(fromClazz) != null)
            {
                for (Class<?> toClazz : toHierarchy)
                {
                    IConverter conv = converterMap.get(fromClazz).get(toClazz);
                    if (conv != null)
                        return conv;
                }
            }
        }

        return null;
    }

    private IValidator searchClassHierarchiesForValidator(Class<?> fromType, Class<?> toType)
    {

        Class<?>[] fromHierarchy = ClassLookupSupport.getTypeHierarchyFlattened(fromType);
        Class<?>[] toHierarchy = ClassLookupSupport.getTypeHierarchyFlattened(toType);
        for (Class<?> fromClazz : fromHierarchy)
        {
            // check if converter is registered for specific class, realized
            // interface in this class or superclasse(s) with realized
            // interface(s)
            if (validatorMap.get(fromClazz) != null)
            {
                for (Class<?> toClazz : toHierarchy)
                {
                    IValidator val = validatorMap.get(fromClazz).get(toClazz);
                    if (val != null)
                        return val;
                }
            }
        }

        return null;
    }

    /**
     * create a validator for the given types using the given regular expression
     * 
     * @deprecated use {@link ConverterRegistry#getValidator(Object, Object)} instead
     */
    public IValidator createValidator(Object fromType, Object toType)
    {
        IValidator result = null;
        if (String.class == fromType
                && (Date.class == toType || toType instanceof Class
                        && Calendar.class.isAssignableFrom((Class<?>) toType)))
        {
            result = new DateToStringValidator();
        }
        else
        {
            result = defaultStrategy.createValidator(fromType, toType);
        }
        return result;
    }

    /**
     * @param regexp regular expression using Java syntax as described in {@link Pattern}
     * @return
     * @throws IllegalArgumentException if regexp is null
     * @throws PatternSyntaxException if regexp syntax is invalid
     */
    public IValidator createRegexpValidator(String regexp)
    {
        IValidator result = new RegExpValidator(regexp);
        return result;
    }
}
