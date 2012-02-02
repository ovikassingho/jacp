/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jacp.api.base;

/**
 * represents an editor handled by a perspective
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
public interface IEditor<C, L, A, M> extends IExtendedComponent<C>,
		ISubComponent<C, L, A, M> {

}
