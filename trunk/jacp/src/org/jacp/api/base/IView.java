/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jacp.api.base;

/**
 *represents an view handled by a perspective 
 *
 * @param <C>
 *            defines the base component where others extend from
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 * @author Andy Moncsek
 */
public interface IView<C, L, A, M> extends ISubComponent<C, L, A, M> {

}
