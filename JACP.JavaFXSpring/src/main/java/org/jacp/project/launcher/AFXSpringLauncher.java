package org.jacp.project.launcher;

import org.jacp.api.launcher.Launcher;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx.rcp.workbench.AFXWorkbench;
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
public abstract class AFXSpringLauncher extends Application {
    private final String springXML;
	private final String workbenchName;
	/**
	 * default constructor; add reference to valid spring.xml
	 * @param springXML
	 */
    protected AFXSpringLauncher(final String springXML) {
		this.springXML = springXML;
		this.workbenchName = null;
	}

	public AFXSpringLauncher(final String springXML, final String workbenchName) {
		this.springXML = springXML;
		this.workbenchName = workbenchName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage stage) throws Exception {
        Launcher<ClassPathXmlApplicationContext> launcher = new SpringLauncher(this.springXML);
		final IWorkbench<Node, EventHandler<Event>, Event, Object> workbench = (IWorkbench<Node, EventHandler<Event>, Event, Object>) launcher
				.getContext().getBean(
						this.workbenchName != null ? this.workbenchName
								: "workbench");
		workbench.init(launcher);
		((AFXWorkbench) workbench).start(stage);
		postInit(stage);

	}

	protected abstract void postInit(Stage stage);

}
