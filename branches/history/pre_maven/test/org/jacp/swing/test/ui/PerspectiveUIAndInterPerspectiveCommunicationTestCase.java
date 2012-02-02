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
 * This test checks the inter perspective communication; messages are send from
 * one perspective to an other to check if behavior is correct; two perspectives
 * are defined (perspective two and three) both perspectives have a
 * toolbarButton and a bottomBar button; the toolBar button sends a message to
 * corresponding perspective (toolBar button perspective two, sends message to
 * perspective three and vice versa); the buttonBar button sends a local message
 * to restore previous state
 * 
 * @author Andy Moncsek
 * 
 */
public class PerspectiveUIAndInterPerspectiveCommunicationTestCase extends
		UISpecTestCase {
	protected void setUp() throws Exception {
		super.setUp();
		setAdapter(new MainClassAdapter(UnitTestBenchMain.class));
	}

	public void testGetWindow() {
		Window window = getMainWindow();
		assertNotNull(window);
	}

	public void testMessageFromPerspectiveTwoToThree()
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
		// //////////POSITIVE TEST 1 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveThreeLEFT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveThreeRIGHT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		// ////////POSITIVE TEST 1 END //////////////////

		// this perspective should not be shown after inital startup

		// //////////NEGATIVE TEST 1 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveThreeTOP") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveThreeBOTTOM") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		// ////////NEGATIVE TEST 1 END //////////////////
		// switch perspective !
		Button buttonTwo = window.getButton("PerspectiveTwoToolBarButton");
		buttonTwo.click();
		// Asynchrony behavior -> wait 
		Thread.sleep(500);

		// //////////POSITIVE TEST 2 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveThreeTOP") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveThreeBOTTOM") : false;
			}
		});
		assertTrue(assertion.isTrue());
		// ////////POSITIVE TEST 2 END //////////////////
		// //////////NEGATIVE TEST 2 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveThreeLEFT") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveThreeRIGHT") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		// ////////NEGATIVE TEST 2 END //////////////////

		Button buttonThree = window
				.getButton("PerspectiveThreeBottomBarButton");
		buttonThree.click();
		// Asynchrony behavior
		Thread.sleep(500);

		// //////////POSITIVE TEST 3 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveThreeLEFT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveThreeRIGHT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		// ////////POSITIVE TEST 3 END //////////////////
		// //////////NEGATIVE TEST 3 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveThreeTOP") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveThreeBOTTOM") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		// ////////NEGATIVE TEST 3 END //////////////////
	}

	public void testMessageFromPerspectiveThreeToTwo()
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
		// //////////POSITIVE TEST 1 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveTwoLEFT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveTwoRIGHT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		// ////////POSITIVE TEST 1 END //////////////////
		// //////////NEGATIVE TEST 1 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveTwoTOP") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveTwoBOTTOM") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		// ////////NEGATIVE TEST 1 END //////////////////

		Button buttonTwo = window.getButton("PerspectiveThreeToolBarButton");
		buttonTwo.click();
		// Asynchrony behavior
		Thread.sleep(500);

		// //////////POSITIVE TEST 2 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveTwoTOP") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveTwoBOTTOM") : false;
			}
		});
		assertTrue(assertion.isTrue());
		// ////////POSITIVE TEST 2 END //////////////////
		// //////////NEGATIVE TEST 2 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveTwoLEFT") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveTwoRIGHT") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		// ////////NEGATIVE TEST 2 END //////////////////

		Button buttonThree = window.getButton("PerspectiveTwoBottomBarButton");
		buttonThree.click();
		// Asynchrony behavior
		Thread.sleep(500);

		// //////////POSITIVE TEST 3 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveTwoLEFT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveTwoRIGHT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		// ////////POSITIVE TEST 3 END //////////////////
		// //////////NEGATIVE TEST 3 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveTwoTOP") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveTwoBOTTOM") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		// ////////NEGATIVE TEST 3 END //////////////////
	}

	public void testSendMessageFromTwoToThreeAndViceVersaSterssTest()
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
		// //////////POSITIVE TEST 1 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveTwoLEFT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveTwoRIGHT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		// ////////POSITIVE TEST 1 END //////////////////

		// //////////POSITIVE TEST 2 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveThreeLEFT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveThreeRIGHT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		// ////////POSITIVE TEST 2 END //////////////////

		// //////////NEGATIVE TEST 1 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveTwoTOP") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveTwoBOTTOM") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		// ////////NEGATIVE TEST 1 END //////////////////

		// //////////NEGATIVE TEST 2 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveThreeTOP") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveThreeBOTTOM") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		// ////////NEGATIVE TEST 2 END //////////////////

		Button buttonThree = window.getButton("PerspectiveTwoBottomBarButton");
		buttonThree.click();

		// ///////STRESS TEST //////////////

		Button buttonTwo = window.getButton("PerspectiveThreeToolBarButton");
		for (int i = 0; i < 50000; i++) {
			buttonTwo.click();
			buttonThree.click();
		}

		buttonThree = window.getButton("PerspectiveThreeBottomBarButton");
		buttonThree.click();

		buttonTwo = window.getButton("PerspectiveTwoBottomBarButton");
		buttonTwo.click();
		// Asynchrony behavior
		Thread.sleep(30000);

		// //////////STRESS TEST END ////////////Ã�

		// //////////POSITIVE TEST 3 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveTwoLEFT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveTwoRIGHT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		// ////////POSITIVE TEST 3 END //////////////////

		// //////////POSITIVE TEST 4 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveThreeLEFT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveThreeRIGHT") : false;
			}
		});
		assertTrue(assertion.isTrue());
		// ////////POSITIVE TEST 4 END //////////////////

		// //////////NEGATIVE TEST 3 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveTwoTOP") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveTwoBOTTOM") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		// ////////NEGATIVE TEST 3 END //////////////////

		// //////////NEGATIVE TEST 4 /////////////////////////

		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("ButtonOnePerspectiveThreeTOP") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("BottonTwoPerspectiveThreeBOTTOM") : false;
			}
		});
		assertTrue(!assertion.isTrue());
		// ////////NEGATIVE TEST 4 END //////////////////
	}

}
