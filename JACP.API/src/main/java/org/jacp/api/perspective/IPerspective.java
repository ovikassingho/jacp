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
package org.jacp.api.perspective;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.IDelegateDTO;
import org.jacp.api.component.IHandleable;
import org.jacp.api.component.IRootComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.launcher.Launcher;

/**
 * Defines a perspective, a perspective is a root component handled by an
 * workbench and contains subcomponents such as visibla UI components or
 * background components. A workbench can handle one or more perspectives (1-n)
 * and every perspective can handle one ore more components (1-n).
 * 
 * @author Andy Moncsek
 * 
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface IPerspective<L, A, M> extends IComponent<L, A, M>,
		IRootComponent<ISubComponent<L, A, M>, IAction<A, M>>,
		IHandleable<A, M> {

	/**
	 * The initialization method. The launcher is the access to the DI container
	 * instance.
	 * 
	 * @param launcher
	 */
	void init(final Launcher<?> launcher,final BlockingQueue<IDelegateDTO<L, A, M>> queue);

	/**
	 * Returns all subcomponents in perspective.
	 * 
	 * @return a list of all handled components in current perspective.
	 */
	List<ISubComponent<L, A, M>> getSubcomponents();

	/**
	 * Set all subcomponents handled by the perspective.
	 * 
	 * @param subComponents
	 */
	void setSubcomponents(final List<ISubComponent<L, A, M>> subComponents);

	/**
	 * Handle a message call on perspective instance. This method should be override to handle the layout of an perspective.
	 * 
	 * @param action
	 */
	void handlePerspective(final IAction<A, M> action);

	/**
	 * Add an active component after component.handle was executed.
	 * 
	 * @param component
	 */
	void addActiveComponent(final ISubComponent<L, A, M> component);

	/**
	 * Delegate target change to an other perspective.
	 * 
	 * @param target
	 * @param component
	 */
	void delegateTargetChange(final String target,
			final ISubComponent<L, A, M> component);

	/**
	 * Delegates massage to responsible componentObserver to notify target
	 * component.
	 * 
	 * @param target
	 * @param action
	 */
	void delegateComponentMassege(final String target,
			final IAction<A, M> action);
	/**
	 * Delegates message to responsible perspectiveObserver to notify target
	 * perspective, check if message is local (message to component it self) or
	 * if message has to be delegate to an other component.
	 * 
	 * @param target
	 * @param action
	 */
	void delegateMassege(final String target, final IAction<A, M> action);

}
