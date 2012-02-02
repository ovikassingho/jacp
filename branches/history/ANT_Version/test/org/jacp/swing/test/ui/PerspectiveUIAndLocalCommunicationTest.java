package org.jacp.swing.test.ui;

import java.awt.Component;

import org.jacp.swing.test.ui.main.UnitTestBenchMain;
import org.uispec4j.Button;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.MainClassAdapter;

/**
 * This Test checks basic UI defined by perspective, includes components defined
 * in perspective (instead of being defined in subcomponents) and it test local
 * communication which means massages to component it self; all communication on perspective level is SYNCHRON; asynchrony communication appears only on subcomponent level
 * 
 * @author Andy Moncsek
 * 
 */
public class PerspectiveUIAndLocalCommunicationTest extends UISpecTestCase {
    protected void setUp() throws Exception {
	super.setUp();
	setAdapter(new MainClassAdapter(UnitTestBenchMain.class));
    }

    public void testGetWindow() {
	Window window = getMainWindow();
	assertNotNull(window);
    }

    public void testPerspectiveOneBasicUIAndLocalMessageFunction()
	    throws InterruptedException {

	Window window = getMainWindow();
	// Asynchrony behavior
	Thread.sleep(1000);
	Assertion assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("splitPanePerspectiveOne") : false;
	    }
	});
	assertTrue(assertion.isTrue());
	Button buttonOne = window.getButton("PerspectiveOneToolBarButton");
	// ////////NEGATIVE TEST 1 //////////////////
	assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("ButtonOnePerspectiveOneTOP") : false;
	    }
	});
	assertFalse(assertion.isTrue());
	assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("BottonTwoPerspectiveOneBOTTOM") : false;
	    }
	});
	assertFalse(assertion.isTrue());
	// ////////NEGATIVE TEST 1 END //////////////////

	// //////////POSITIVE TEST 1 /////////////////////////
	buttonOne.click();
	// Asynchrony behavior
	Thread.sleep(500);
	assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("ButtonOnePerspectiveOneTOP") : false;
	    }
	});
	assertTrue(assertion.isTrue());
	assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("BottonTwoPerspectiveOneBOTTOM") : false;
	    }
	});
	assertTrue(assertion.isTrue());
	// ////////POSITIVE TEST 1 END //////////////////

	// ////////NEGATIVE TEST 2 //////////////////
	assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("ButtonOnePerspectiveOneLEFT") : false;
	    }
	});
	assertFalse(assertion.isTrue());
	assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("BottonTwoPerspectiveOneRIGHT") : false;
	    }
	});
	assertFalse(assertion.isTrue());
	// ////////NEGATIVE TEST 2 END //////////////////
	Button buttonTwo = window.getButton("PerspectiveOneBottomBarButton");
	buttonTwo.click();
	// Asynchrony behavior
	Thread.sleep(500);
	// //////////POSITIVE TEST 2 /////////////////////////

	assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("ButtonOnePerspectiveOneLEFT") : false;
	    }
	});
	assertTrue(assertion.isTrue());
	assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("BottonTwoPerspectiveOneRIGHT") : false;
	    }
	});
	assertTrue(assertion.isTrue());
	// ////////POSITIVE TEST 2 END //////////////////

	// ////////NEGATIVE TEST 3 //////////////////
	assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("ButtonOnePerspectiveOneTOP") : false;
	    }
	});
	assertFalse(assertion.isTrue());
	assertion = window.containsComponent(new ComponentMatcher() {

	    @Override
	    public boolean matches(Component component) {
		return component.getName() != null ? component.getName()
			.equals("BottonTwoPerspectiveOneBOTTOM") : false;
	    }
	});
	assertFalse(assertion.isTrue());
	// ////////NEGATIVE TEST 3 END //////////////////
    }

}
