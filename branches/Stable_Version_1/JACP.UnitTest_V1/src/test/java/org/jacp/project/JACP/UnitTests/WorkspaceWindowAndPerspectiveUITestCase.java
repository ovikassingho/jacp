package org.jacp.project.JACP.UnitTests;

import java.awt.Component;

import org.jacp.project.JACP.Util.util.OSXBottomBarPanel;
import org.jacp.project.JACP.Util.util.OSXToolBar;
import org.uispec4j.MenuBar;
import org.uispec4j.MenuItem;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.MainClassAdapter;
/**
 * This test checks the correct initialization of workbench UI, tool bars and perspective buttons registered in tool bars
 * @author Andy Moncsek
 *
 */
public class WorkspaceWindowAndPerspectiveUITestCase extends UISpecTestCase {
	protected void setUp() throws Exception {
		super.setUp();
		setAdapter(new MainClassAdapter(UnitTestBenchMain.class));
	}

	public void testGetWindow() {
		Window window = getMainWindow();
		assertNotNull(window);
	}

	public void testGetMenue() throws InterruptedException {
		Window window = getMainWindow();
		// Asynchrony behavior
		Thread.sleep(500);
		assertTrue(window.containsMenuBar().isTrue());
	}

	public void testGetBars() throws InterruptedException {
		// bottom bar
		Window window = getMainWindow();
		// Asynchrony behavior
		Thread.sleep(500);
		Component[] resultBottom = window
				.getSwingComponents(OSXBottomBarPanel.class);
		assertNotNull(resultBottom);
		assertTrue(resultBottom.length == 1);

		// tool bar
		Component[] result = window
				.getSwingComponents(OSXToolBar.class);
		assertNotNull(result);
		assertTrue(result.length == 1);

	}

	public void testGetMenuEntries() throws InterruptedException {
		Window window = getMainWindow();
		// Asynchrony behavior
		Thread.sleep(500);
		MenuBar menuBar = window.getMenuBar();
		assertNotNull(menuBar);
		MenuItem menuPerspectiveOne = menuBar.getMenu("perspectiveOne");
		assertNotNull(menuPerspectiveOne);
		assertNotNull(menuPerspectiveOne.getSubMenu("PerspectiveOne"));

		MenuItem menuPerspectiveTwo = menuBar.getMenu("perspectiveTwo");
		assertNotNull(menuPerspectiveTwo);
		assertNotNull(menuPerspectiveTwo.getSubMenu("perspectiveTwo"));

		MenuItem menuPerspectiveThree = menuBar.getMenu("perspectiveThree");
		assertNotNull(menuPerspectiveThree);
		assertNotNull(menuPerspectiveThree.getSubMenu("PerspectiveThree"));
	}

	public void testGetPerspectiveToolBarEntries() throws InterruptedException {
		Window window = getMainWindow();
		// Asynchrony behavior
		Thread.sleep(500);
		// tool bar
		Component[] result = window
				.getSwingComponents(OSXToolBar.class);
		assertNotNull(result);
		assertTrue(result.length == 1);
		Component toolBar = result[0];
		assertNotNull(toolBar);
		Assertion assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("PerspectiveOneToolBarButton") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("PerspectiveTwoToolBarButton") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("PerspectiveThreeToolBarButton") : false;
			}
		});
		assertTrue(assertion.isTrue());
	}

	public void testGetPerspectiveBottomBarEntries()
			throws InterruptedException {
		Window window = getMainWindow();
		// Asynchrony behavior
		Thread.sleep(500);
		// tool bar
		Component[] result = window
				.getSwingComponents(OSXBottomBarPanel.class);
		assertNotNull(result);
		assertTrue(result.length == 1);
		Component toolBar = result[0];
		assertNotNull(toolBar);
		Assertion assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("PerspectiveOneBottomBarButton") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("PerspectiveTwoBottomBarButton") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("PerspectiveThreeBottomBarButton") : false;
			}
		});
		assertTrue(assertion.isTrue());

	}

	public void testPerspectiveOneBasicUIFunctionsIsUIObjectSIncluded()
			throws InterruptedException {
		// bottom bar
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
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("panelOnePerspectiveOne") : false;
			}
		});
		assertTrue(assertion.isTrue());
		assertion = window.containsComponent(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return component.getName() != null ? component.getName()
						.equals("panelTwoPerspectiveOne") : false;
			}
		});
		assertTrue(assertion.isTrue());
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
	}

	
	
}
