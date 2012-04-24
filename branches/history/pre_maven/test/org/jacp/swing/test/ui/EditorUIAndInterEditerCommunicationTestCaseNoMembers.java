package org.jacp.swing.test.ui;

import java.awt.Component;
import java.util.Random;

import org.jacp.swing.test.ui.main.UnitTest2BenchMainNoMember;
import org.uispec4j.Button;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.MainClassAdapter;

// TODO: Auto-generated Javadoc
/**
 * This test checks the inter perspective communication; messages are send from
 * one perspective to an other to check if behavior is correct; two perspectives
 * are defined (perspective two and three) both perspectives have a
 * toolbarButton and a bottomBar button; the toolBar button sends a message to
 * corresponding perspective (toolBar button perspective two, sends message to
 * perspective three and vice versa); the buttonBar button sends a local message
 * to restore previous state.
 * 
 * @author Andy Moncsek
 */
public class EditorUIAndInterEditerCommunicationTestCaseNoMembers extends
		UISpecTestCase {

	/** The TIM e_ t o_ wait. */
	private static int TIME_TO_WAIT = 2000;

	/** The counter one. */
	private int counterOne = 0;

	/** The counter two. */
	private int counterTwo = 0;

	/** The Constant MESSAGE_COUNT. */
	private static final int MESSAGE_COUNT = 50;

	/** The components. */
	private String[] components = { "PerspectiveOneToolBarButton",
			"POneComponentOneButton", "POneComponentTwoButton",
			"ButtonOneEditorOne", "ButtonOneEditorTwo",
			"PerspectiveOneBottomBarButton", "oneTextFieldComponentTwo",
			"oneTextFieldComponentOne" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.uispec4j.UISpecTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		setAdapter(new MainClassAdapter(UnitTest2BenchMainNoMember.class));
	}

	/**
	 * Test get window.
	 */
	public void testGetWindow() {
		Window window = getMainWindow();
		assertNotNull(window);
	}

	/**
	 * Check components.
	 * 
	 * @param names
	 *            the names
	 * @return true, if successful
	 */
	private boolean checkComponents(String[] names) {
		Window window = getMainWindow();
		for (final String name : names) {
			Assertion assertion = window
					.containsComponent(new ComponentMatcher() {

						@Override
						public boolean matches(Component component) {
							return component.getName() != null ? component
									.getName().equals(name) : false;
						}
					});
			if (!assertion.isTrue()) {
				return false;
			}
		}

		return true;

	}

	// Check Components in GUI

	/**
	 * Test message from perspective one to two.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public void testMessageFromPerspectiveOneToTwo()
			throws InterruptedException {
		Window window = getMainWindow();

		// Asynchrony behavior
		Thread.sleep(TIME_TO_WAIT);
		assertTrue(checkComponents(components));

		Button buttonOne = window.getButton("PerspectiveOneToolBarButton");
		buttonOne.click();

		// Asynchrony behavior
		Thread.sleep(500);

		// Perspective two is shown, thus the splitpane has changed orientations

		Button buttonTwo = window.getButton("PerspectiveOneBottomBarButton");
		buttonTwo.click();

		// Asynchrony behavior
		Thread.sleep(500);

		assertTrue(checkComponents(components));

		// GUI Checked
	}

	// Send several messages from Editor One to Editor Two
	/**
	 * Test message from editor one2 two.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public void testMessageFromEditorOne2Two() throws InterruptedException {

		Window window = getMainWindow();
		// Asynchrony behavior
		Thread.sleep(TIME_TO_WAIT);
		Button buttonOne = window.getButton("ButtonOneEditorOne");
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			buttonOne.click();
			counterTwo++;

			Thread.sleep(200);
		}
		Thread.sleep(TIME_TO_WAIT);

		TextBox textbox2 = window.getTextBox("oneTextFieldComponentTwo");
		System.out.println(textbox2.getText().substring(9));
		System.out.println(counterTwo);
		assertTrue(textbox2.getText().substring(9)
				.equals("" + (counterTwo - 1)));

		// counterTwo = 0;

	}

	// Send several messages from Editor Two to Editor One
	/**
	 * Test message from editor two2 one.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public void testMessageFromEditorTwo2One() throws InterruptedException {
		Window window = getMainWindow();
		// Asynchrony behavior
		Thread.sleep(TIME_TO_WAIT);
		Button buttonTwo = window.getButton("ButtonOneEditorTwo");
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			buttonTwo.click();
			counterOne++;
			Thread.sleep(200);
		}
		Thread.sleep(TIME_TO_WAIT);
		TextBox textbox = window.getTextBox("oneTextFieldComponentOne");
		assertTrue(textbox.getText().substring(9).equals("" + (counterOne - 1)));
		// counterOne = 0;
	}

	/*
	 * A Fuzzy "stress"-Test
	 */

	/**
	 * Test fuzzy messages.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public void testFuzzyMessages() throws InterruptedException {
		Window window = getMainWindow();

		counterOne = 50;
		counterTwo = 50;
		// Asynchrony behavior
		Thread.sleep(TIME_TO_WAIT);
		Button buttonTwo = window.getButton("ButtonOneEditorTwo");
		Button buttonOne = window.getButton("ButtonOneEditorOne");
		Random randomGenerator = new Random();
		for (int i = 0; i < (5); i++) {
			System.out.println(i);
			int randomInt = randomGenerator.nextInt(10);
			if (randomInt % 2 == 0) {

				buttonTwo.click();
				counterOne++;
			} else {
				buttonOne.click();
				counterTwo++;
			}
			// Asynchrony behavior
			Thread.sleep(500);
		}
		// Asynchrony behavior
		Thread.sleep(TIME_TO_WAIT + TIME_TO_WAIT);

		TextBox textbox2 = window.getTextBox("oneTextFieldComponentTwo");
		TextBox textbox = window.getTextBox("oneTextFieldComponentOne");
		final String t2 = textbox2.getText();
		final String t = textbox.getText();
		if (t2.length() > 9) 
			assertTrue(t2.substring(9)
					.equals("" + (counterTwo - 1)));
		if(t.length() > 9)
			assertTrue(t.substring(9)
					.equals("" + (counterOne - 1)));
		}
		

	}