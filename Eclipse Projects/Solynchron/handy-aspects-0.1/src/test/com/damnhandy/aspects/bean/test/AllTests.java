package com.damnhandy.aspects.bean.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	
	public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for handy-aspects-1.0");
        suite.addTestSuite(JavaBeanTest.class);
        suite.addTestSuite(JavaBeanNestedTest.class);
        suite.addTestSuite(JavaBeanCollectionNestedTest.class);
        return suite;
    }

}
