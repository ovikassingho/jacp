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
public interface IActionListener<L, M, A> {

	/**
	 * notify component when action fired
	 * 
	 * @param action
	 */
	public abstract void notifyComponents(final IAction<M, A> action);

	/**
	 * set Action to listener
	 * 
	 * @param action
	 */
	public abstract void setAction(final IAction<M, A> action);

	/**
	 * returns the action
	 * 
	 * @return
	 */
	public IAction<M, A> getAction();

	/**
	 * returns implementation specific ActionListener
	 * 
	 * @return
	 */
	public abstract L getListener();
}
