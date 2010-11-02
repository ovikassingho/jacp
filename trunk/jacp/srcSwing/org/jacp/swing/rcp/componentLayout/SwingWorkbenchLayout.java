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

import java.awt.LayoutManager2;

import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.util.Tupel;
import org.jacp.api.util.WorkspaceMode;

/**
 * 
 * @author Andy Moncsek
 */
public class SwingWorkbenchLayout implements IWorkbenchLayout<LayoutManager2> {

    private LayoutManager2 layoutManager;
    private boolean bottomBarEnabled;
    private boolean toolBarEnabled;
    private boolean menuEnabled;
    private WorkspaceMode workspaceMode;
    private Layout toolBarLayout;
    private Layout bottomBarLayout;
    private final Tupel<Integer, Integer> size = new Tupel<Integer, Integer>();

    @Override
    public WorkspaceMode getWorkspaceMode() {
	return workspaceMode;
    }

    @Override
    public void setWorkspaceMode(final WorkspaceMode mode) {
	workspaceMode = mode;
    }

    @Override
    public boolean isMenuEnabled() {
	return menuEnabled;
    }

    @Override
    public void setMenuEnabled(final boolean enabled) {
	menuEnabled = enabled;
    }

    @Override
    public void setToolBarEnabled(final boolean enabled, final Layout position) {
	toolBarEnabled = enabled;
	toolBarLayout = position;
    }

    @Override
    public void setBottomBarEnabled(final boolean enabled, final Layout position) {
	bottomBarEnabled = enabled;
	bottomBarLayout = position;
    }

    @Override
    public void setLayoutManager(final LayoutManager2 layout) {
	layoutManager = layout;
    }

    @Override
    public LayoutManager2 getLayoutManager() {
	return layoutManager;
    }

    @Override
    public boolean isToolbarEnabled() {
	return toolBarEnabled;
    }

    @Override
    public Layout getToolBarLayout() {
	return toolBarLayout;
    }

    @Override
    public boolean isBottomBarEnabled() {
	return bottomBarEnabled;
    }

    @Override
    public Layout getBottomBarLayout() {
	return bottomBarLayout;
    }

    @Override
    public void setWorkbenchXYSize(final int x, final int y) {
	size.setX(x);
	size.setY(y);
    }

    @Override
    public Tupel<Integer, Integer> getWorkbenchSize() {
	return size;
    }

}
