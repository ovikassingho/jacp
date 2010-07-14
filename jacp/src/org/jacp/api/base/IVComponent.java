/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jacp.api.base;

/**
 * represents an ui component handled by a perspective
 * 
 * @author Andy Moncsek
 * @param <C>
 *            defines the base component where others extend from
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface IVComponent<C, L, A, M> extends IExtendedComponent<C>,
		ISubComponent<L, A, M> {

	/**
	 * set the root ui component created by the handle method
	 * 
	 * @param root
	 */
	public abstract void setRoot(C root);

	/**
	 * returns 'root' ui component created by the handle method
	 * 
	 * @return
	 */
	public abstract C getRoot();

}
