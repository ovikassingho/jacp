/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ahcp.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.ahcp.api.ria.base.IWorkbench;
import org.ahcp.swing.test.workbench.TestSwingWorkbench;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author amo
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

	public static void main(final String[] args) throws ClassNotFoundException {
		final String[] contextPath = new String[] { "org/ahcp/impl/resources/ahcpWorkbench.xml" };
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				contextPath);
		final IWorkbench workbench = (IWorkbench) context.getBean("workbench");
		workbench.init();
	}

	private static void setOsSpecificSettings() throws ClassNotFoundException {
		final String osName = System.getProperty("os.name");
		if (osName.toLowerCase().trim().contains("mac")) {
			setOSXspecific();
		} else {
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
}
