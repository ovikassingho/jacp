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

/**
 * all root components have containing sub components (workspace ->
 * perspectives; perspective - editors) and listeners; all sub components have
 * to be initialized, registered and handled
 * 
 * @author Andy Moncsek
 * 
 * @param <T>
 *            component type to register
 * @param <A>
 *            action type to use in registration process
 */
public interface IRootComponent<T, A> {

    /**
     * register component at listener
     * 
     * @param component
     */
    abstract public void registerComponent(final T component);

    /**
     * unregister component from current perspective
     * 
     * @param component
     */
    abstract public void unregisterComponent(final T component);

    /**
     * handles initialization of subcomponents
     * 
     * @param action
     */
    public abstract void initComponents(final A action);

    /**
     * handles initialization of a single component;
     * 
     * @param action
     * @param editor
     */
    public abstract void initComponent(final A action, final T component);

    /**
     * runs 'handle' method and replace of subcomponent in perspective
     * 
     * @param layout
     * @param component
     * @param action
     */
    public abstract void handleAndReplaceComponent(final A action,
	    final T component);

}
