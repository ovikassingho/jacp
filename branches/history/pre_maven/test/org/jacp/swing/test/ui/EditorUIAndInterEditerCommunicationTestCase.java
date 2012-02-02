package org.jacp.swing.test.ui;

import java.awt.Component;
import java.util.Random;

import org.jacp.swing.test.ui.main.UnitTest2BenchMain;
import org.uispec4j.Button;
import org.uispec4j.TextBox;
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
public class EditorUIAndInterEditerCommunicationTestCase extends
		UISpecTestCase {

	private int counterOne = 0;
	private int counterTwo = 0;
	private static final int MESSAGE_COUNT = 50;
	private String[] components = { "PerspectiveOneToolBarButton",
			"POneComponentOneButton", "POneComponentTwoButton",
			"ButtonOneEditorOne", "ButtonOneEditorTwo",
			"PerspectiveOneBottomBarButton", "oneTextFiledComponentTwo" };

	protected void setUp() throws Exception {
		super.setUp();
		setAdapter(new MainClassAdapter(UnitTest2BenchMain.class));
	}

	public void testGetWindow() {
		Window window = getMainWindow();
		assertNotNull(window);
	}

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

	public void testMessageFromPerspectiveOneToTwo()
			throws InterruptedException {
		Window window = getMainWindow();
		
		// Asynchrony behavior
		Thread.sleep(1000);
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
	public void testMessageFromEditorOne2Two() throws InterruptedException {

		Window window = getMainWindow();
		// Asynchrony behavior
		Thread.sleep(1000);
		Button buttonOne = window.getButton("ButtonOneEditorOne");
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			buttonOne.click();
			counterTwo++;
		}
		Thread.sleep(1000);

		TextBox textbox2 = window.getTextBox("oneTextFiledComponentTwo");
		assertTrue(textbox2.getText().substring(9)
				.equals("" + (counterTwo - 1)));
		counterTwo = 0;

	}

	// Send several messages from Editor Two to Editor One
	public void testMessageFromEditorTwo2One() throws InterruptedException {
		Window window = getMainWindow();
		// Asynchrony behavior
		Thread.sleep(1000);
		Button buttonTwo = window.getButton("ButtonOneEditorTwo");
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			buttonTwo.click();
			counterOne++;
		}
		Thread.sleep(1000);
		TextBox textbox = window.getTextBox("oneTextFiledComponentOne");
		assertTrue(textbox.getText().substring(9).equals("" + (counterOne - 1)));
		counterOne = 0;
	}

	/*
	 *  A Fuzzy "stress"-Test
	 */

	public void testFuzzyMessages() throws InterruptedException {

		Window window = getMainWindow();
		// Asynchrony behavior
		Thread.sleep(1000);
		Button buttonTwo = window.getButton("ButtonOneEditorTwo");
		Button buttonOne = window.getButton("ButtonOneEditorOne");

		Random randomGenerator = new Random();
		for (int i = 0; i < (MESSAGE_COUNT * MESSAGE_COUNT); i++) {
			int randomInt = randomGenerator.nextInt(10);
			if (randomInt % 2 == 0) {

				buttonTwo.click();
				counterOne++;
			} else {
				buttonOne.click();
				counterTwo++;
			}
		}
		// Asynchrony behavior
		Thread.sleep(1000);
		
		TextBox textbox2 = window.getTextBox("oneTextFiledComponentTwo");
		TextBox textbox = window.getTextBox("oneTextFiledComponentOne");

		assertTrue(textbox2.getText().substring(9)
				.equals("" + (counterTwo - 1)));
		assertTrue(textbox.getText().substring(9).equals("" + (counterOne - 1)));

	}

}
