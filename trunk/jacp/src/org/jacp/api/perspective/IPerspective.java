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
package org.jacp.api.perspective;

import java.util.List;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.IRootComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.IComponentCoordinator;

/**
 * a perspective is a root component, handled by an workbench and contains
 * subcomponents
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
		IRootComponent<ISubComponent<L, A, M>, IComponentCoordinator<L, A, M>> {

	/**
	 * the initialization method
	 */
	public abstract void init();

	/**
	 * get all subcomponents in perspective
	 * 
	 * @return
	 */
	public abstract List<ISubComponent<L, A, M>> getSubcomponents();

	/**
	 * set all subcomponents of perspective
	 * 
	 * @param subComponents
	 */
	public abstract void setSubcomponents(
			final List<ISubComponent<L, A, M>> subComponents);

	/**
	 * handle baselayout when perspective started
	 * 
	 * @param action
	 */
	public abstract void handlePerspective(final IAction<A, M> action);


	/**
	 * handles init of subcomponents in perspective TODO move to IRootcomponent
	 * 
	 * @param action
	 * @param perspective
	 */
	public abstract void initSubcomponents(final IAction<A, M> action,
			final IPerspective<L, A, M> perspective);

	/**
	 * TODO move to IRoot component handles initialisation of a single
	 * component; TODO change from IVComponent to global interface
	 * 
	 * @param action
	 * @param editor
	 */
	public abstract void initSubcomonent(final IAction<A, M> action,
			final ISubComponent<L, A, M> component);

	/**
	 * runs 'handle' method and replace of subcomponent in perspective
	 * 
	 * @param layout
	 * @param component
	 * @param action
	 */
	public abstract void handleAndReplaceSubcomponent(
			final IAction<A, M> action, final ISubComponent<L, A, M> component);

	/**
	 * add active component after component.handle was executed
	 * 
	 * @param component
	 */
	public abstract void addActiveComponent(
			final ISubComponent<L, A, M> component);

	/**
	 * delegate target change to an other perspective
	 * 
	 * @param target
	 * @param component
	 */
	public void delegateTargetChange(final String target,
			final ISubComponent<L, A, M> component);

	/**
	 * delegates massage to responsible componentObserver to notify target
	 * component
	 * 
	 * @param target
	 * @param action
	 */
	public abstract void delegateComponentMassege(final String target,
			final IAction<A, M> action);

	/**
	 * delegates message to responsible perspectiveObserver to notify target
	 * perspective
	 * 
	 * @param target
	 * @param action
	 */
	public abstract void delegateMassege(final String target,
			final IAction<A, M> action);

}
