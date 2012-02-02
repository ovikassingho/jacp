/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.api.observers;

import org.jacp.api.perspective.IPerspective;

/**
 * notifies perspectives and components included in workbench C defines the base
 * component where others extend A defines the action listener type E defines
 * the basic action type T defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IPerspectiveObserver<L, A, M> extends IObserver<L, A, M> {

	/**
	 * add perspective to observe
	 * 
	 * @param perspective
	 */
	public abstract void addPerspective(final IPerspective<L, A, M> perspective);

	/**
	 * remove perspective; e.g. when perspective is deactivated
	 * 
	 * @param perspective
	 */
	public abstract void removePerspective(
			final IPerspective<L, A, M> perspective);

}
