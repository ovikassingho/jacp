package org.jacp.javafx2.rcp.main;


import org.jacp.project.launcher.AFX2SpringLauncher;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DemoWorkbenchMain extends AFX2SpringLauncher {

	public DemoWorkbenchMain() {
		super("main.xml");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}
	

	private Scene scene;

	@Override
	public void postInit(Stage stage) {
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
