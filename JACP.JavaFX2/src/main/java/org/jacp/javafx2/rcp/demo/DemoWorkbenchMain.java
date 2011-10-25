package org.jacp.javafx2.rcp.demo;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import org.jacp.api.launcher.Launcher;
import org.jacp.api.workbench.IWorkbench;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoWorkbenchMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Launcher<ClassPathXmlApplicationContext> launcher = new SpringLauncher(
				"main_2.xml");
		final IWorkbench<Region, Node, EventHandler<ActionEvent>, ActionEvent, Object> workbench = (IWorkbench<Region, Node, EventHandler<ActionEvent>, ActionEvent, Object>) launcher
				.getContext().getBean("workbench");
		workbench.init(launcher);
	}

}
