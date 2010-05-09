/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jacp.api.base;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.observers.IObserver;

/**
 * represents a basic component
 * 
 * @author Andy Moncsek
 * @param <C>
 *            defines the base component where others extend from
 * @param <A>
 *            defines the action listener type
 * @param <E>
 *            defines the basic action type
 * @param <T>
 *            defines the basic message type
 */
public interface IComponent<C, A, E, T> {

	/**
	 * handles component when called
	 * 
	 * @param action
	 * @return view component
	 */
	public abstract C handle(final IAction<T, E> action);

	/**
	 * returns action listener (for local, target and global use)
	 * 
	 * @return
	 */
	public abstract IActionListener<A, E, T> getActionListener();

	/**
	 * returns id of component
	 * 
	 * @return
	 */
	public abstract String getId();

	/**
	 * set unique id of component
	 * 
	 * @param id
	 */
	public abstract void setId(String id);

	/**
	 * get active status of perspective
	 * 
	 * @return
	 */
	public abstract boolean isActive();

	/**
	 * set active state of perspective
	 * 
	 * @param active
	 */
	public abstract void setActive(boolean active);

	/**
	 * returns the name of a component
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * defines the name of a component
	 * 
	 * @param name
	 */
	public abstract void setName(String name);

	/**
	 * observer to handle changes in components
	 * 
	 * @return
	 */
	public abstract void setObserver(IObserver<C, A, E, T> observer);

	public abstract void setBlocked(boolean blocked);

	public abstract boolean isBlocked();

}
