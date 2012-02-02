/*
 * Copyright (C) 2010,2011.
 * AHCP Project
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
package org.jacp.javafx2.rcp.componentLayout;

import java.util.Map;
import java.util.TreeMap;

import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;

import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.api.util.Tupel;
import org.jacp.javafx2.rcp.components.toolBar.JACPToolBar;

/**
 * defines basic layout of workbench; define if menus are enabled; declare tool
 * bars; set workbench size
 * 
 * @author Andy Moncsek
 */
public class FX2WorkbenchLayout implements IWorkbenchLayout<Node> {

	private boolean menueEnabled;
	private final Tupel<Integer, Integer> size = new Tupel<Integer, Integer>();
	private final Map<ToolbarPosition, ToolBar> registeredToolbars = new TreeMap<ToolbarPosition, ToolBar>();
	private MenuBar menu;
	private Pane glassPane;
	private StageStyle style = StageStyle.DECORATED;

	@Override
	public boolean isMenuEnabled() {
		return this.menueEnabled;
	}

	@Override
	public void setMenuEnabled(boolean enabled) {
		this.menueEnabled = enabled;
		if (enabled && this.menu == null) {
			this.menu = new MenuBar();
			this.menu.setId("main-menu");
		}
	}

	@Override
	public void setWorkbenchXYSize(int x, int y) {
		this.size.setX(x);
		this.size.setY(y);
	}

	@Override
	public Tupel<Integer, Integer> getWorkbenchSize() {
		return this.size;
	}

	private ToolBar initToolBar(ToolbarPosition position) {
		final JACPToolBar bar = new JACPToolBar();
		bar.setId(position.getName() + "-bar");
		return bar;
	}

	@Override
	public void registerToolBar(ToolbarPosition position) {
		registeredToolbars.put(position, initToolBar(position));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <S extends Enum> void setStyle(S style) {
		this.style = (StageStyle) style;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <S extends Enum> S getStyle() {
		return (S) this.style;
	}

	@Override
	public MenuBar getMenu() {
		return this.menu;
	}

	/**
	 * Gets the registered toolbars.
	 * 
	 * @return the registered toolbars
	 */
	public Map<ToolbarPosition, ToolBar> getRegisteredToolbars() {
		return registeredToolbars;
	}

	@Override
	public ToolBar getRegisteredToolBar(ToolbarPosition position) {
		return registeredToolbars.get(position);
	}

	public Pane getGlassPane() {
		if (glassPane == null)
			glassPane = new Pane();
		return glassPane;
	}

}
