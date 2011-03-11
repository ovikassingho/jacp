package org.jacp.swing.test;

import java.awt.Component;

import org.jacp.swing.rcp.util.OSXToolBar;
import org.jacp.swing.test.main.UnitTestBenchMain;
import org.uispec4j.MenuBar;
import org.uispec4j.MenuItem;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;



public class WorkspaceWindowTestCase extends UISpecTestCase {
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
		 //Asynchrony behavior
		 Thread.sleep(500);
		 assertTrue(window.containsMenuBar().isTrue());
	}
	
	public void testGetBars() throws InterruptedException {
		// bottom bar
		 Window window = getMainWindow();	
		 //Asynchrony behavior
		 Thread.sleep(500);
		 Component[] resultBottom = window.getSwingComponents(org.jacp.swing.rcp.util.OSXBottomBarPanel.class);
		 assertNotNull(resultBottom);
		 assertTrue(resultBottom.length==1);
		 
		 
		 // tool bar
		 Component[] result = window.getSwingComponents(org.jacp.swing.rcp.util.OSXToolBar.class);
		 assertNotNull(result);
		 assertTrue(result.length==1);
		 
		
	}
	
	public void testGetMenuEntries() throws InterruptedException {
		Window window = getMainWindow();
		 //Asynchrony behavior
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
		 //Asynchrony behavior
		 Thread.sleep(500);
		 // tool bar
		 Component[] result = window.getSwingComponents(org.jacp.swing.rcp.util.OSXToolBar.class);
		 assertNotNull(result);
		 assertTrue(result.length==1);
		 Component toolBar = result[0];
		 assertNotNull(toolBar);
		 if(toolBar instanceof OSXToolBar) {
			 Component[] buttons = ((OSXToolBar)toolBar).getComponents();
			 assertNotNull(buttons);
			 assertFalse(buttons.length==0);
			 for(int i=0;i<buttons.length;i++){
				 Component button = buttons[i];
				 assertNotNull(button);
			 }
			 
		 } else {
			 assertTrue(false);
		 }
		 
		
	}
	
}
