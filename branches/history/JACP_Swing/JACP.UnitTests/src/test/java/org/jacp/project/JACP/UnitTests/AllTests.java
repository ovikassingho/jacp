package org.jacp.project.JACP.UnitTests;

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
	suite.addTestSuite(EditorUIAndInterEditerCommunicationTestCase.class); 
	suite.addTestSuite(WorkspaceWindowAndPerspectiveUITestCase.class);
	suite.addTestSuite(EditorUIAndInterEditerCommunicationTestCaseNoMembers.class);
	//$JUnit-END$
	return suite;
    }

}
