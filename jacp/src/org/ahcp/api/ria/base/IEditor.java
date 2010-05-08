/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ahcp.api.ria.base;

/**
 * represents an editor handled by a perspective
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
public interface IEditor<C, A, E, T> extends IExtendedComponent<C>,
		ISubComponent<C, A, E, T> {

}
