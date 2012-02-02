package org.jacp.project.JACP.UnitTests;

import java.awt.Container;
import java.awt.LayoutManager2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jacp.api.launcher.Launcher;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.project.JACP.Util.impl.SpringLauncher;
import org.jacp.swing.rcp.util.WorkspaceMode;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * this component acts as an application startup component for unit test one;
 * this test checks correctnes of basic workspace functionallity an perspective
 * local and inter perspective communication
 * 
 * @author Andy Moncsek
 * 
 */
public class UnitTestBenchMain {

	static {
		try {
			setOsSpecificSettings();
		} catch (final ClassNotFoundException ex) {
			Logger.getLogger(UnitTestBenchMain.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static void main(final String[] args) {
		final Launcher<ClassPathXmlApplicationContext> launcher = new SpringLauncher(
				"main.xml");
		final IWorkbench<Container, LayoutManager2, ActionListener, ActionEvent, Object, WorkspaceMode> workbench = (IWorkbench<Container, LayoutManager2, ActionListener, ActionEvent, Object, WorkspaceMode>) launcher
				.getContext().getBean("workbench");
		workbench.init(launcher);
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
				"JACP Demo");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final InstantiationException ex) {
			Logger.getLogger(UnitTestBenchMain.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (final IllegalAccessException ex) {
			Logger.getLogger(UnitTestBenchMain.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (final UnsupportedLookAndFeelException ex) {
			Logger.getLogger(UnitTestBenchMain.class.getName()).log(
					Level.SEVERE, null, ex);
		}

	}

	private static void setDefault() throws ClassNotFoundException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final InstantiationException ex) {
			Logger.getLogger(UnitTestBenchMain.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (final IllegalAccessException ex) {
			Logger.getLogger(UnitTestBenchMain.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (final UnsupportedLookAndFeelException ex) {
			Logger.getLogger(UnitTestBenchMain.class.getName()).log(
					Level.SEVERE, null, ex);
		}

	}
}
