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
package org.jacp.api.componentLayout;

import java.awt.Container;
import java.util.Map;

import org.jacp.api.util.Tupel;
import org.jacp.api.util.WorkspaceMode;

/**
 * defines the base layout of a workbench and the application
 * 
 * @author Andy Moncsek
 */
public interface IWorkbenchLayout<L> {

    /**
     * get defined workspace layout
     * 
     * @return
     */
    public abstract WorkspaceMode getWorkspaceMode();

    /**
     * set workspace layout to window-, stack- or tabbed-mode
     * 
     * @param mode
     */
    public abstract void setWorkspaceMode(WorkspaceMode mode);

    /**
     * check if menues are enabled
     * 
     * @return
     */
    public abstract boolean isMenuEnabled();

    /**
     * set menues enabled
     * 
     * @param enabled
     */
    public abstract void setMenuEnabled(boolean enabled);


    /**
     * set default layout manager to workspace
     * 
     * @param layout
     */
    public abstract void setLayoutManager(L layout);

    /**
     * get defined layout manager
     * 
     * @return
     */
    public abstract L getLayoutManager();

    /**
     * set size of workbench
     * 
     * @param x
     * @param y
     */
    public abstract void setWorkbenchXYSize(int x, int y);

    /**
     * returns a tupel defining the workbench size
     * 
     * @return
     */
    public abstract Tupel<Integer, Integer> getWorkbenchSize();


    /**
     * register a tool bar for workbench
     * 
     * @param name
     * @param toolBar
     */
    public abstract void registerToolBar(final Layout name,
	    final Container toolBar);

    /**
     * returns all registered tool bars of workbench
     * 
     * @return
     */
    public abstract Map<Layout, Container> getToolBars();

}
