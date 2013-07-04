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

package net.sf.rcpforms.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.rcpforms.bindingvalidation.Binding_BotTest;
import net.sf.rcpforms.bindingvalidation.ComboBinding_BotTest;
import net.sf.rcpforms.bindingvalidation.NestedBinding_BotTest;
import net.sf.rcpforms.bindingvalidation.NestedNullObjectBinding_BotTest;
import net.sf.rcpforms.test.adapter.*;
import net.sf.rcpforms.test.table.ObservableListBeanContentProvider_Test;
import net.sf.rcpforms.widgetwrapper.util.NullValue_Test;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPRadioGroup_Test;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPWidgetState_BotTest;

/**
 * Test Suite for all tests which do not need OSGI
 * 
 * @author Marco van Meegen
 */
public class AllTests
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for net.sf.rcpforms.test");
        suite.addTestSuite(BeanAdapter_Test.class);
        suite.addTestSuite(PropertyLabelProviderAndCellModifierBean_Test.class);
        suite.addTestSuite(MapAdapter_Test.class);
        suite.addTestSuite(ConverterRegistry_Test.class);
        suite.addTestSuite(ObservableListBeanContentProvider_Test.class);
        suite.addTestSuite(NullValue_Test.class);
        suite.addTestSuite(RCPRadioGroup_Test.class);
        suite.addTestSuite(Binding_BotTest.class);
        suite.addTestSuite(NestedBinding_BotTest.class);
        suite.addTestSuite(NestedNullObjectBinding_BotTest.class);
        suite.addTestSuite(ComboBinding_BotTest.class);
        suite.addTestSuite(RCPWidgetState_BotTest.class);
        suite.addTestSuite(ValidatorRegistry_Test.class);
        return suite;
    }

}
