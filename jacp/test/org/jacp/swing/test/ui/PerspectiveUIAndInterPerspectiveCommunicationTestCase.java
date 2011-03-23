package org.jacp.swing.test.ui;

import java.awt.Component;

import org.jacp.swing.test.ui.main.UnitTestBenchMain;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.MainClassAdapter;

public class PerspectiveUIAndInterPerspectiveCommunicationTestCase extends UISpecTestCase {
	protected void setUp() throws Exception {
		super.setUp();
		setAdapter(new MainClassAdapter(UnitTestBenchMain.class));
	}

	public void testGetWindow() {
		Window window = getMainWindow();
		assertNotNull(window);
	}
	
	
	public void testMessageToOtherPerspective() throws InterruptedException {
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
	}


}
