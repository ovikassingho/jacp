/*
 * Copyright (C) 2010,2011.
 * AHCP Project (http://code.google.com/p/jacp)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jacp.demo.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.jacp.project.launcher.AFX2SpringLauncher;
/**
 * JacpFX application launcher and main method for contact demo. This is the entry
 * class to start initial context of an JacpFX application. Here you define the
 * location of the spring-xml and can handle settings like css configuration.
 * 
 * @author Andy Moncsek
 * 
 */
public class ContactMain extends AFX2SpringLauncher {

	private Scene scene;

	public ContactMain() {
		super("main.xml");
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void postInit(final Stage stage) {
		this.scene = stage.getScene();
		// add style sheet
		this.scene.getStylesheets().addAll(
				ContactMain.class.getResource("/styles/main.css")
						.toExternalForm());

	}
}
