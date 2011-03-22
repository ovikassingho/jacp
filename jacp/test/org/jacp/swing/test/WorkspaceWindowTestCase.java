package org.jacp.swing.test;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jacp.swing.rcp.util.OSXBottomBarPanel;
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
		 String[] perspectiveButtons={"PerspectiveOneToolBarButton","PerspectiveTwoToolBarButton","PerspectiveThreeToolBarButton"};
		 if(toolBar instanceof OSXToolBar) {
			 Component[] buttons = ((OSXToolBar)toolBar).getComponents();
			 assertNotNull(buttons);
			 assertFalse(buttons.length==0);
			 List<String> errorList = new ArrayList<String>( Arrays.asList(perspectiveButtons));

			 for(int i=0;i<buttons.length;i++){
				 Component button = buttons[i];
				 System.out.println(button.getName());
				 assertNotNull(button);
				 if(!contains(perspectiveButtons, button.getName())){ errorList.remove(button.getName()+"");}
			 }
			 assertTrue(errorList.isEmpty());
			 
		 } else {
			 assertTrue(false);
		 }
		 
		
	}
	
	public void testGetPerspectiveBottomBarEntries() throws InterruptedException {
		Window window = getMainWindow();
		 //Asynchrony behavior
		 Thread.sleep(500);
		 // tool bar
		 Component[] result = window.getSwingComponents(org.jacp.swing.rcp.util.OSXBottomBarPanel.class);
		 assertNotNull(result);
		 assertTrue(result.length==1);
		 Component toolBar = result[0];
		 assertNotNull(toolBar);
		 String[] perspectiveButtons={"PerspectiveOneBottomBarButton","PerspectiveTwoBottomBarButton","PerspectiveThreeBottomBarButton"};
		 if(toolBar instanceof OSXBottomBarPanel) {
			 Component[] buttons = ((OSXBottomBarPanel)toolBar).getComponents();
			 assertNotNull(buttons);
			 assertFalse(buttons.length==0);
			 List<String> errorList = new ArrayList<String>( Arrays.asList(perspectiveButtons));

			 for(int i=0;i<buttons.length;i++){
				 Component button = buttons[i];
				 System.out.println(button.getName());
				 assertNotNull(button);
				 if(!contains(perspectiveButtons, button.getName())){ errorList.remove(button.getName()+"");}
			 }
			 assertTrue(errorList.isEmpty());
			 
		 } else {
			 assertTrue(false);
		 }
		 
		
	}
	
	
	private boolean contains(final String[] array, final String searchString) {
		for(int i=0; i<array.length;i++) {
			String arrayString = array[i];
			if(arrayString.toLowerCase().equals(searchString)) return true;
		}		
		return false;
	}
}
