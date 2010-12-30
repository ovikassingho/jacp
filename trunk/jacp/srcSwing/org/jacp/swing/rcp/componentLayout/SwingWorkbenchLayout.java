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
package org.jacp.swing.rcp.componentLayout;

import java.awt.Container;
import java.awt.LayoutManager2;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.util.Tupel;
import org.jacp.api.util.WorkspaceMode;

/**
 * defines basic layout of workbench; define if menus are enabled; declare tool
 * bars; set workbench size
 * 
 * @author Andy Moncsek
 */
public class SwingWorkbenchLayout implements
	IWorkbenchLayout<LayoutManager2, Container> {

    private LayoutManager2 layoutManager;
    private boolean menuEnabled;
    private WorkspaceMode workspaceMode;
    private final Tupel<Integer, Integer> size = new Tupel<Integer, Integer>();

    private final Map<Layout, Container> toolBars = new ConcurrentHashMap<Layout, Container>();

    @Override
    public final WorkspaceMode getWorkspaceMode() {
	return workspaceMode;
    }

    @Override
    public final void setWorkspaceMode(final WorkspaceMode mode) {
	workspaceMode = mode;
    }

    @Override
    public final boolean isMenuEnabled() {
	return menuEnabled;
    }

    @Override
    public final void setMenuEnabled(final boolean enabled) {
	menuEnabled = enabled;
    }

    @Override
    public final void setLayoutManager(final LayoutManager2 layout) {
	layoutManager = layout;
    }

    @Override
    public final LayoutManager2 getLayoutManager() {
	return layoutManager;
    }

    @Override
    public final void setWorkbenchXYSize(final int x, final int y) {
	size.setX(x);
	size.setY(y);
    }

    @Override
    public final Tupel<Integer, Integer> getWorkbenchSize() {
	return size;
    }

    @Override
    public final void registerToolBar(final Layout name, final Container toolBar) {
	toolBars.put(name, toolBar);
    }

    @Override
    public final Map<Layout, Container> getToolBars() {
	return toolBars;
    }

}
