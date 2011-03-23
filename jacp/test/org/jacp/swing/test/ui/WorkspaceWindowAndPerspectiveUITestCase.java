package org.jacp.swing.test.ui;

import java.awt.Component;

import org.jacp.swing.test.ui.main.UnitTestBenchMain;
import org.uispec4j.Button;
import org.uispec4j.MenuBar;
import org.uispec4j.MenuItem;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.MainClassAdapter;

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
				.getSwingComponents(org.jacp.swing.rcp.util.OSXBottomBarPanel.class);
		assertNotNull(resultBottom);
		assertTrue(resultBottom.length == 1);

		// tool bar
		Component[] result = window
				.getSwingComponents(org.jacp.swing.rcp.util.OSXToolBar.class);
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
				.getSwingComponents(org.jacp.swing.rcp.util.OSXToolBar.class);
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
				.getSwingComponents(org.jacp.swing.rcp.util.OSXBottomBarPanel.class);
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

	private boolean contains(final String[] array, final String searchString) {
		for (int i = 0; i < array.length; i++) {
			String arrayString = array[i];
			if (arrayString.toLowerCase().equals(searchString))
				return true;
		}
		return false;
	}
}
