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
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.util.Tupel;
import org.jacp.api.util.WorkspaceMode;

/**
 * defines basic layout of workbench; define if menus are enabled; declare tool
 * bars; set workbench size
 * @author Andy Moncsek
 */
public class FX2WorkbenchLayout implements IWorkbenchLayout<Region, Parent> {
    
    private WorkspaceMode workspaceMode;
    private boolean menueEnabled;
    private Region layout;
    private Tupel<Integer,Integer> size;
    private Map<Layout, Parent> toolbars = new ConcurrentHashMap<Layout, Parent>(); 

    public WorkspaceMode getWorkspaceMode() {
        return workspaceMode;
    }

    public void setWorkspaceMode(WorkspaceMode mode) {
       this.workspaceMode = mode;
    }

    public boolean isMenuEnabled() {
        return menueEnabled;
    }

    public void setMenuEnabled(boolean enabled) {
       this. menueEnabled = enabled;
    }

    public void setLayoutManager(Region layout) {
        this.layout = layout;
    }

    public Region getLayoutManager() {
        return this.layout;
    }

    public void setWorkbenchXYSize(int x, int y) {
       size.setX(x);
       size.setY(y);
    }

    public Tupel<Integer, Integer> getWorkbenchSize() {
        return size;
    }

    public void registerToolBar(Layout name, Parent toolBar) {
       toolbars.put(name, toolBar);
    }

    public Map<Layout, Parent> getToolBars() {
       return toolbars;
    }
    
}
