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
import java.util.HashMap;
import java.util.Map;

import org.jacp.api.componentLayout.IPerspectiveLayout;

/**
 * Configuration handler for perspective components, used in handle method for
 * configuration and registration of layout 'leaves' where subcomponents can
 * live in. Create your own complex layout, return the root node and register
 * parts of your layout that can handle subcomponents
 * 
 * @author Andy Moncsek
 */
public class SwingPerspectiveLayout implements
	IPerspectiveLayout<Container, Container> {

    private Container layoutComponent;
    private final boolean scrollable = true;
    private boolean replaceMode = true;
    private final Map<String, Container> targetComponents = new HashMap<String, Container>();
    private final ThreadLocal<Map<String, Container>> tComponents = new ThreadLocal<Map<String, Container>>() {
	@Override
	protected Map<String, Container> initialValue() {
	    return targetComponents;
	}
    };

    public SwingPerspectiveLayout() {
    }

    @Override
    public void setReplaceMode(final boolean replace) {
	replaceMode = replace;
    }

    @Override
    public boolean isReplaceMode() {
	return replaceMode;
    }

    @Override
    public void setRootLayoutComponent(final Container comp) {
	layoutComponent = comp;
    }

    @Override
    public Container getRootLayoutComponent() {
	return layoutComponent;
    }

    public boolean isScrollable() {
	return scrollable;
    }

    @Override
    public void registerTargetLayoutComponent(final String id,
	    final Container target) {
	targetComponents.put(id, target);
    }

    @Override
    public Map<String, Container> getTargetLayoutComponents() {
	return tComponents.get();
    }

}
