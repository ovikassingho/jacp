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
package org.jacp.api.workbench;

import java.util.Map;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;


/**
 * Base component for an JACP UI application, handles perspectives and containing
 * components. A workbench is the root of all JACP perspectives an component. You can have 1 workbench including (1-n) perspectives which can include (1-n) components.
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
 * @param <S>
 *            defines the workbench style
 * 
 * @author Andy Moncsek
 */
public interface IWorkbench<C, L, A, M> extends IBase<L, A, M>{


	/**
	 * Initializes the default workbench menu.
	 */
	void initWorkbenchMenu();

	/**
	 * Set default menu bar instance to workspace.
	 */
	void initMenuBar();

	/**
	 * Handle the workbench layout.
	 * 
	 * @param action
	 * @param layout
	 */
	void handleInitialLayout(final IAction<A, M> action,
			final IWorkbenchLayout<C> layout);

	/**
	 * Returns workbench layout object.
	 * 
	 * @return the workbench layout class, defining basic settings for the workbench
	 */
	IWorkbenchLayout<C> getWorkbenchLayout();

}
