/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.api.observers;

import org.jacp.api.base.IPerspective;
import org.jacp.api.base.IWorkbench;

/**
 * notifies perspectives and components included in workbench C defines the base
 * component where others extend A defines the action listener type E defines
 * the basic action type T defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IPerspectiveObserver<C, A, E, T> extends IObserver<C, A, E, T> {

	/**
	 * add perspective to observe
	 * 
	 * @param perspective
	 */
	public abstract void addPerspective(
			final IPerspective<C, A, E, T> perspective);

	/**
	 * remove perspective; e.g. when perspective is deactivated
	 * 
	 * @param perspective
	 */
	public abstract void removePerspective(
			final IPerspective<C, A, E, T> perspective);

	/**
	 * returns the parent workbench
	 * 
	 * @return
	 */
	public abstract IWorkbench<C, ?, A, E, T> getPerentWorkbench();

	/**
	 * set the parent workbench of observed perspectives
	 * 
	 * @param workbench
	 */
	public abstract void setParentWorkbench(
			final IWorkbench<C, ?, A, E, T> workbench);

}
