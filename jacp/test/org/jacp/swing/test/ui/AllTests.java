package org.jacp.swing.test.ui;

import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * jacp swing ui test suite 
 * @author Andy Moncsek
 *
 */
public class AllTests {

    public static Test suite() {
	TestSuite suite = new TestSuite(AllTests.class.getName());
	//$JUnit-BEGIN$
	suite.addTestSuite(PerspectiveUIAndLocalCommunicationTest.class);
	suite.addTestSuite(PerspectiveUIAndInterPerspectiveCommunicationTestCase.class);
	suite.addTestSuite(PerspectiveUIAndInterPerspectiveCommunicationTestCase2.class);
	suite.addTestSuite(WorkspaceWindowAndPerspectiveUITestCase.class);
	//$JUnit-END$
	return suite;
    }

}
