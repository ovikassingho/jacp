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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private Container rootComponent;
    private final Map<String, Container> targetComponents = new ConcurrentHashMap<String, Container>();

    @Override
    public final void setRootComponent(final Container comp) {
	rootComponent = comp;
    }

    @Override
    public final Container getRootComponent() {
	return rootComponent;
    }

    @Override
    public final void registerTargetLayoutComponent(final String id,
	    final Container target) {
	targetComponents.put(id, target);
    }

    @Override
    public final Map<String, Container> getTargetLayoutComponents() {
	return targetComponents;
    }

}
