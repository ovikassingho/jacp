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
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.stage.StageStyle;

import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.api.util.ToolbarPriority;
import org.jacp.api.util.ToolbarProperty;
import org.jacp.api.util.Tupel;

/**
 * defines basic layout of workbench; define if menus are enabled; declare tool
 * bars; set workbench size
 * 
 * @author Andy Moncsek
 */
public class FX2WorkbenchLayout implements IWorkbenchLayout<Node> {

	private boolean menueEnabled;
	private final Tupel<Integer, Integer> size = new Tupel<Integer, Integer>();
	private final Map<Layout, ToolBar> toolbars = new ConcurrentHashMap<Layout, ToolBar>();
	private final Map<ToolbarProperty, ToolBar> registeredToolbars = new TreeMap<ToolbarProperty, ToolBar>();
	private MenuBar menu;
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

	@Override
	@Deprecated
	public void registerToolBar(Layout name) {
		if (!this.toolbars.containsKey(name)) {
			final ToolBar bar = new ToolBar();
			bar.setId(name.getLayout() + "-bar");
			this.toolbars.put(name, bar);
		}
	}

	private ToolBar initToolBar(ToolbarPosition position) {
		final ToolBar bar = new ToolBar();
		bar.setId(position.getName() + "-bar");
		return bar;
	}

	private ToolbarProperty initToolBarProperty(ToolbarPosition position,
			ToolbarPriority priority) {
		ToolbarProperty property = new ToolbarProperty();
		property.setPosition(position);
		property.setPriority(priority);
		return property;
	}

	@Override
	public void registerToolBar(ToolbarPosition position,
			ToolbarPriority priority) {
		// TODO: @PETE: remove priority due to BorderPane can't handle
		// priorities
		registeredToolbars.put(initToolBarProperty(position, priority),
				initToolBar(position));
	}

	@Override
	public void registerToolBar(ToolbarPosition position) {
		registerToolBar(position, ToolbarPriority.ONE);
	}

	@Override
	@Deprecated
	public ToolBar getToolBar(Layout name) {
		return this.toolbars.get(name);
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
	 * Returns the map with all tool bars registered.
	 * 
	 * @return
	 */
	public Map<Layout, ToolBar> getToolBarMap() {
		return this.toolbars;
	}

	/**
	 * Gets the registered toolbars.
	 * 
	 * @return the registered toolbars
	 */
	public Map<ToolbarProperty, ToolBar> getRegisteredToolbars() {
		return registeredToolbars;
	}

	public ToolBar getRegisteredToolBar(ToolbarPosition position) {
		ToolBar toolbar = null;
		Set<ToolbarProperty> keySet = this.registeredToolbars.keySet();
		for (final ToolbarProperty prop : keySet) {
			if (prop.getPosition().getId() == position.getId()) {
				toolbar = this.registeredToolbars.get(prop);
				break;
			}
		}
		return toolbar;
	}

}
