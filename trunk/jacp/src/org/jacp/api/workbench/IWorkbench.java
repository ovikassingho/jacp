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
package org.jacp.api.workbench;

import java.util.List;
import java.util.Map;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.perspective.IPerspective;

/**
 * base component for an application, handles perspectives and containing
 * components
 * 
 * @param <P>
 *            defines the default layout manager
 * 
 * @param <C>
 *            defines the base component where others extend from
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IWorkbench<P, C, L, A, M> {

	/**
	 * returns basic container to handle perspectives
	 * 
	 * @return
	 */
	public abstract C init();

	/**
	 * init default workbench menu
	 */
	public abstract void initWorkbenchMenu();

	/**
	 * set default menu bar instance to workspace
	 */
	public abstract void initMenuBar();

	/**
	 * returns default workbench menu
	 * 
	 * @return
	 */
	public abstract C getDefaultMenu();

	/**
	 * add/remove menu entries to workbench instance
	 * 
	 * @return
	 */
	public abstract C handleMenuEntries(final C meuBar);


	/**
	 * add default workbench actions to tool bar
	 * 
	 * @param toolBar
	 * @param bottomBar
	 */
	public abstract void handleBarEntries(final Map<Layout,C> bars);

	/**
	 * set perspectives to workbench
	 * 
	 * @param perspectives
	 */
	public abstract void setPerspectives(
			final List<IPerspective<L, A, M>> perspectives);

	/**
	 * get perspectives in workbench
	 * 
	 * @return
	 */
	public abstract List<IPerspective<L, A, M>> getPerspectives();



	/**
	 * set visibility of all components in workspace wrapper to false
	 * 
	 * @param component
	 */
	public abstract void disableComponents();

	/**
	 * anable all components in workspace; for use in
	 * initPerspectiveInWindowMode
	 */
	public abstract void enableComponents();

	/**
	 * handle workbench layout
	 * 
	 * @param action
	 * @param layout
	 */
	public abstract void handleInitialLayout(final IAction<A, M> action,
			final IWorkbenchLayout<P> layout);

	/**
	 * returns workbench layout object
	 * 
	 * @return
	 */
	public abstract IWorkbenchLayout<P> getWorkbenchLayout();


}
