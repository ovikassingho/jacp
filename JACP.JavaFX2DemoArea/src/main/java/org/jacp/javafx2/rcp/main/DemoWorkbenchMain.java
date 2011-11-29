package org.jacp.javafx2.rcp.main;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.jacp.api.launcher.Launcher;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx2.rcp.launcher.SpringLauncher;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoWorkbenchMain extends Application {
	final Launcher<ClassPathXmlApplicationContext> launcher = new SpringLauncher(
			"main.xml");

	private Scene scene;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Application.launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {
		final IWorkbench< Node, EventHandler<Event>, Event, Object> workbench = (IWorkbench< Node, EventHandler<Event>, Event, Object>) launcher
				.getContext().getBean("workbench");
		workbench.init(launcher);
		((AFX2Workbench) workbench).start(stage);
		// Adding CSS to the stage
		boolean is3dSupported = Platform
				.isSupported(ConditionalFeature.SCENE3D);
		scene = stage.getScene();
		if (is3dSupported) {
			// RT-13234
			scene.setCamera(new PerspectiveCamera());
		}
		scene.getStylesheets().addAll(
				DemoWorkbenchMain.class.getResource("main.css")
						.toExternalForm(),
				// Workaround for CSS issue with HTML Editor
				com.sun.javafx.scene.web.skin.HTMLEditorSkin.class.getResource(
						"html-editor.css").toExternalForm());

	}

}
