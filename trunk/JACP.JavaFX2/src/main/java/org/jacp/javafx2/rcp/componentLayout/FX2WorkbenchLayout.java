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
import java.util.concurrent.ConcurrentHashMap;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.util.Tupel;

/**
 * defines basic layout of workbench; define if menus are enabled; declare tool
 * bars; set workbench size
 * 
 * @author Andy Moncsek
 */
public class FX2WorkbenchLayout implements
		IWorkbenchLayout<Node> {

	private boolean menueEnabled;
	private Tupel<Integer, Integer> size = new Tupel<Integer, Integer>();
	private Map<Layout, Node> toolbars = new ConcurrentHashMap<Layout, Node>();
	private StageStyle style = StageStyle.DECORATED;
	private Stage root;

	@Override
	public boolean isMenuEnabled() {
		return menueEnabled;
	}

	@Override
	public void setMenuEnabled(boolean enabled) {
		this.menueEnabled = enabled;
	}


	@Override
	public void setWorkbenchXYSize(int x, int y) {
		size.setX(x);
		size.setY(y);
	}

	@Override
	public Tupel<Integer, Integer> getWorkbenchSize() {
		return size;
	}

	@Override
	public void registerToolBar(Layout name, Node toolBar) {
		toolbars.put(name, toolBar);
	}

	@Override
	public Map<Layout, Node> getToolBars() {
		return toolbars;
	}

	@Override
	public <S extends Enum> void setStyle(S style) {
		this.style = (StageStyle) style;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Enum> S getStyle() {
		return (S) style;
	}



	

}
