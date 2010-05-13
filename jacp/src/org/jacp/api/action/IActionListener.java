/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.api.action;

/**
 * handles implementation specific ActionListener
 * 
 * 
 * @param <L>
 *            defines the basic listener type 
 * @param <M>
 *            defines the type of message ActionEvent
 * @param <A>
 *            defines the type of ActionEvent
 * @author Andy Moncsek
 */
public interface IActionListener<L, A, M> {

	/**
	 * notify component when action fired
	 * 
	 * @param action
	 */
	public abstract void notifyComponents(final IAction<A, M> action);

	/**
	 * set Action to listener
	 * 
	 * @param action
	 */
	public abstract void setAction(final IAction<A, M> action);

	/**
	 * returns the action
	 * 
	 * @return
	 */
	public IAction<A, M> getAction();

	/**
	 * returns implementation specific ActionListener
	 * 
	 * @return
	 */
	public abstract L getListener();
}
