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

package net.sf.rcpforms.widgetwrapper.util;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.modeladapter.converter.BeanConverterRegistry;
import net.sf.rcpforms.modeladapter.converter.ConverterRegistry;
import net.sf.rcpforms.modeladapter.converter.ObjectWithNullValueToObjectConverter;
import net.sf.rcpforms.modeladapter.converter.StringToStringConverter;
import net.sf.rcpforms.test.RCPFormBaseTestCase;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCombo;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * Class NullValueTest. Tests NullValue representation and instance handling in rcpforms.
 * 
 * @author Marco van Meegen
 */
public class NullValue_Test extends RCPFormBaseTestCase
{
    ConverterRegistry reg = new BeanConverterRegistry();

    public void testNullValueUtil()
    {
        assertTrue(NullValue.isNullValue(NullValue.getInstance()));
        assertFalse(NullValue.isNullValue(new Object()));

    }

    /**
     * Test method for
     * {@link net.sf.rcpforms.common.NullValue#registerRepresentations(java.lang.Class, java.lang.String[])}
     * .
     */

    public void testRegisterRepresentations()
    {
        String rep1 = "<none>";
        String rep2 = "<no selection>";
        String key1 = "de.mvmsoft.test.key1";
        String key2 = "de.mvmsoft.test.key2";
        NullValue.registerRepresentations(key1, rep1);
        NullValue.registerRepresentations(key2, rep2);
        assertEquals(rep1, NullValue.getRepresentation(key1));
        assertEquals(rep2, NullValue.getRepresentation(key2));
        assertEquals(NullValue.GENERIC_NULL_VALUE_REPRESENTATION, NullValue.getRepresentation(null));

    }

    // VM: problem with mixing (expected = IllegalArgumentException.class)
    public void testUnknownRepresentations()
    {
        try
        {
            NullValue.getRepresentation("unknown");
            fail("IllegalArgumentException was not thrown");
        }
        catch (IllegalArgumentException ex)
        {

        }
    }

    private static final class TestValidationManager extends ValidationManager
    {
        private TestValidationManager(String instanceName)
        {
            super(instanceName);
        }

        @Override
        public IConverter getConverter(net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget widget,
                                       boolean modelToTarget)
        {
            return super.getConverter(widget, modelToTarget);
        }
    }

    private static class TestClass
    {
        // nothing needed here
    }

    /**
     * test if validation manager uses a converter null --> NullValue.getInstance() for combos and
     * radio groups
     */

    public void testNullValueConverter()
    {
        TestValidationManager vm = new TestValidationManager("test");
        IConverter converter = vm.getConverter(new RCPCombo("test", true), false);
        assertTrue(converter instanceof ObjectWithNullValueToObjectConverter);
        TestClass instance = new TestClass();
        // instance should be passed
        assertSame(instance, converter.convert(instance));

        // and NullValue should be converted to null
        assertNull(converter.convert(NullValue.getInstance()));

        // test some other cases
        IConverter converter1 = vm.getConverter(new RCPCombo("test", true), true);
        assertFalse(converter1 instanceof ObjectWithNullValueToObjectConverter);
        IConverter converter2 = vm.getConverter(new RCPCombo("test", false), false);
        assertFalse(converter2 instanceof ObjectWithNullValueToObjectConverter);
    }

    public void testStringConverter()
    {
        IConverter converter = reg.getConverter(String.class,
                String.class);
        assertTrue(converter instanceof StringToStringConverter);
        String instance = "bla";
        // instance should be passed
        assertSame(instance, converter.convert(instance));

        // and "" should be converted to null
        assertNull(converter.convert(""));

    }
}
