/*
 * Copyright (C) 2010,2011.
 * AHCP Project (http://code.google.com/p/jacp/)
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
package org.jacp.api.component;

import java.util.Map;

import org.jacp.api.componentLayout.Layout;



/**
 * Represents a extended component with menu entries and tool bar access.
 * 
 * @author Andy Moncsek
 * @param <C>
 *            defines the base component where others extend from
 */
public interface IExtendedComponent<C> {

    /**
     * Get the custom menu entries.
     * 
     * @param menuBar
     */
    void handleMenuEntries(final C menuBar);

    /**
     * Add custom actions to tool bar; set key for bar type (south, north, east, west).
     * 
     * @param bars
     */
    void handleBarEntries(final Map<Layout, C> bars);
    

}
