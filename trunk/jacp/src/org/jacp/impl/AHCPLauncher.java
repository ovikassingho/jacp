/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.impl;

import java.awt.Container;
import java.awt.LayoutManager2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jacp.api.workbench.IWorkbench;
import org.jacp.swing.demo1.workbench.TestSwingWorkbench;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Launches the spring context, handles workbench initialization and set OS
 * specific settings
 * 
 * @author Andy Moncsek
 */
public class AHCPLauncher {

	static {
		try {
			setOsSpecificSettings();
		} catch (final ClassNotFoundException ex) {
			Logger.getLogger(AHCPLauncher.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static void main(final String[] args) throws ClassNotFoundException {
		final String[] contextPath = new String[] { "org/jacp/impl/resources/ahcpWorkbench.xml" };
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				contextPath);
		final IWorkbench<Container, LayoutManager2, ActionListener, ActionEvent, Object> workbench = (IWorkbench<Container, LayoutManager2, ActionListener, ActionEvent, Object>) context
				.getBean("workbench");
		workbench.init();
	}

	private static void setOsSpecificSettings() throws ClassNotFoundException {
		final String osName = System.getProperty("os.name");
		if (osName.toLowerCase().trim().contains("mac")) {
			setOSXspecific();
		} else {
			setDefault();
		}
	}

	private static void setOSXspecific() throws ClassNotFoundException {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
				"TestApp");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final InstantiationException ex) {
			Logger.getLogger(AHCPLauncher.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (final IllegalAccessException ex) {
			Logger.getLogger(AHCPLauncher.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (final UnsupportedLookAndFeelException ex) {
			Logger.getLogger(TestSwingWorkbench.class.getName()).log(
					Level.SEVERE, null, ex);
		}

	}

	private static void setDefault() throws ClassNotFoundException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final InstantiationException ex) {
			Logger.getLogger(AHCPLauncher.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (final IllegalAccessException ex) {
			Logger.getLogger(AHCPLauncher.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (final UnsupportedLookAndFeelException ex) {
			Logger.getLogger(TestSwingWorkbench.class.getName()).log(
					Level.SEVERE, null, ex);
		}

	}
}
