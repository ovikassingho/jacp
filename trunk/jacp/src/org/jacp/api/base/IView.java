/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jacp.api.base;

/**
 *represents an view handeld by a perspective C defines the base component
 * where others extend A defines the action listener type E defines the basic
 * action type T defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IView<C, A, E, T> extends ISubComponent<C, A, E, T> {

}
