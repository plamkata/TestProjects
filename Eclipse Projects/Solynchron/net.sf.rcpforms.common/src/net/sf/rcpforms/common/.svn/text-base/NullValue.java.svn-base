/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.rcpforms.common.util.Validate;

/**
 * place holder for the undefined (null) value in cases where viewers are involved, e.g. combo boxes
 * and radio groups. This place holder must not be used in the model itself, but only on GUI
 * elements; the binding layer should transform the NullValue object into a <null> in the model
 * attribute.
 * <p>
 * There maybe different representations available or registered for different widget types. Some
 * RCPWidgets support setting a representation key which should be used for the null value. If no
 * representation key is supported or set, the representation for key null is used. If no
 * representations are registered at all, the {@value #GENERIC_NULL_VALUE_REPRESENTATION} is used.
 * <p>
 * 
 * @author Marco van Meegen
 */

public class NullValue
{
    public static final String GENERIC_NULL_VALUE_REPRESENTATION = "<no value>"; //$NON-NLS-1$

    public static final String GENERIC_NULL_VALUE_REPRESENTATION_NONE = "<none>"; //$NON-NLS-1$

    public static final String GENERIC_NULL_VALUE_REPRESENTATION_NO_SELECTION = "<no selection>"; //$NON-NLS-1$

    public static final String GENERIC_NULL_VALUE_REPRESENTATION_DEFAULT = "default"; //$NON-NLS-1$

    public static final String GENERIC_NULL_VALUE_REPRESENTATION_ALL = "all"; //$NON-NLS-1$

    public static final String GENERIC_NULL_VALUE_REPRESENTATION_SPECIAL = "special"; //$NON-NLS-1$

    public static final String GENERIC_NULL_VALUE_REPRESENTATION_DASH = "-"; //$NON-NLS-1$

    public static final String GENERIC_NULL_VALUE_REPRESENTATION_EMPTY = ""; //$NON-NLS-1$

    private static NullValue instance = new NullValue();

    private static Map<String, String> representationMap = new HashMap<String, String>();

    private NullValue()
    {

    }

    /**
     * @return the only instance of the null value object
     */
    public static NullValue getInstance()
    {
        return instance;
    }

    /**
     * @param representationKey representation key for the representation to retrieve; null is the
     *            default representation choice
     * @return representation of the null value for the given widget type
     */
    public static String getRepresentation(String representationKey)
    {
        String representation = null;
        // check for default representations
        if (representationKey == null)
        {
            representation = GENERIC_NULL_VALUE_REPRESENTATION;
        }
        else if (representationKey.equals(GENERIC_NULL_VALUE_REPRESENTATION_NONE))
        {
            representation = GENERIC_NULL_VALUE_REPRESENTATION_NONE;

        }
        else if (representationKey.equals(GENERIC_NULL_VALUE_REPRESENTATION_NO_SELECTION))
        {
            representation = GENERIC_NULL_VALUE_REPRESENTATION_NO_SELECTION;

        }
        else if (representationKey.equals(GENERIC_NULL_VALUE_REPRESENTATION_DEFAULT))
        {
            representation = GENERIC_NULL_VALUE_REPRESENTATION_DEFAULT;

        }
        else if (representationKey.equals(GENERIC_NULL_VALUE_REPRESENTATION_ALL))
        {
            representation = GENERIC_NULL_VALUE_REPRESENTATION_ALL;

        }
        else if (representationKey.equals(GENERIC_NULL_VALUE_REPRESENTATION_SPECIAL))
        {
            representation = GENERIC_NULL_VALUE_REPRESENTATION_SPECIAL;

        }
        else if (representationKey.equals(GENERIC_NULL_VALUE_REPRESENTATION_DASH))
        {
            representation = GENERIC_NULL_VALUE_REPRESENTATION_DASH;

        }
        else if (representationKey.equals(GENERIC_NULL_VALUE_REPRESENTATION_EMPTY))
        {
            representation = GENERIC_NULL_VALUE_REPRESENTATION_EMPTY;

        }
        else if (representationMap.containsKey(representationKey))
        {
            representation = representationMap.get(representationKey);
        }
        else
        {
            // error if representation not available
            String reps = printRepresentations();
            Validate.isTrue(false, "Representation with key " + representationKey //$NON-NLS-1$
                    + " is not available, available representations are: " + reps); //$NON-NLS-1$
        }

        return representation;
    }

    /**
     * returns a human-readable string of the available representations
     * 
     * @return debug-suited string of the available representations
     */
    public static String printRepresentations()
    {
        Set<String> representations = representationMap.keySet();
        StringBuffer buffer = new StringBuffer();
        buffer.append('['); //$NON-NLS-1$
        if (representations != null)
        {
            int i = 0;
            for (String key : representations)
            {
                if (i != 0)
                {
                    buffer.append(", "); //$NON-NLS-1$
                }
                buffer.append("Key: \"" + key + "\" = " + representationMap.get(key)); //$NON-NLS-1$ //$NON-NLS-2$
                i++;
            }
            buffer.append(']'); //$NON-NLS-1$
        }
        else
        {
            buffer.append(GENERIC_NULL_VALUE_REPRESENTATION);
        }
        return buffer.toString();
    }

    /**
     * representation if no special logic is coded
     */
    @Override
    public String toString()
    {
        return GENERIC_NULL_VALUE_REPRESENTATION;
    }

    /**
     * register representations for null values for the given key
     * 
     * @param key key of representation, should start with bundle prefix to avoid conflicts among
     *            different bundles sharing rcpforms !
     * @param representation string representation of the null value for this key
     */
    public static void registerRepresentations(String key, String representation)
    {
        Validate.notEmpty(representation);
        Validate.notNull(representation);
        representationMap.put(key, representation);
    }

    /**
     * @param element element to check
     * @return true if this element is the NullValue placeholder
     */
    public static boolean isNullValue(Object element)
    {
        return element == getInstance();
    }
}
