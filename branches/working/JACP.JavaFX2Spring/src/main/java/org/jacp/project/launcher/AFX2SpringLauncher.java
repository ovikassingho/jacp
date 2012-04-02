package org.jacp.project.launcher;

import org.jacp.api.launcher.Launcher;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx.rcp.workbench.AFX2Workbench;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * JavaFX2 / Spring application launcher; This abstract class handles reference
 * to spring context and contains the JavaFX2 start method; Implement this
 * abstract class and add a main method to call the default JavaFX2 launch
 * ("Application.launch(args);") sequence
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class AFX2SpringLauncher extends Application {
	private Launcher<ClassPathXmlApplicationContext> launcher = null;
	private final String springXML;
	private final String workbenchName;
	/**
	 * default constructor; add reference to valid spring.xml
	 * @param springXML
	 */
	public AFX2SpringLauncher(final String springXML) {
		this.springXML = springXML;
		this.workbenchName = null;
	}

	public AFX2SpringLauncher(final String springXML, final String workbenchName) {
		this.springXML = springXML;
		this.workbenchName = workbenchName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage stage) throws Exception {
		this.launcher = new SpringLauncher(this.springXML);
		final IWorkbench<Node, EventHandler<Event>, Event, Object> workbench = (IWorkbench<Node, EventHandler<Event>, Event, Object>) this.launcher
				.getContext().getBean(
						this.workbenchName != null ? this.workbenchName
								: "workbench");
		workbench.init(this.launcher);
		((AFX2Workbench) workbench).start(stage);
		postInit(stage);

	}

	public abstract void postInit(Stage stage);

}
