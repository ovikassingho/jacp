/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.api.observers;

import org.jacp.api.base.IPerspective;
import org.jacp.api.base.ISubComponent;
import org.jacp.api.base.IWorkbench;

/**
 * notifies perspectives and components included in workbench C defines the base
 * component where others extend A defines the action listener type E defines
 * the basic action type T defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IPerspectiveObserver<C, L, A, M> extends IObserver<C, L, A, M> {

	/**
	 * add perspective to observe
	 * 
	 * @param perspective
	 */
	public abstract void addPerspective(
			final IPerspective<C, L, A, M> perspective);

	/**
	 * remove perspective; e.g. when perspective is deactivated
	 * 
	 * @param perspective
	 */
	public abstract void removePerspective(
			final IPerspective<C, L, A, M> perspective);

	/**
	 * returns the parent workbench
	 * 
	 * @return
	 */
	public abstract IWorkbench<?, C, L, A, M> getPerentWorkbench();

	/**
	 * set the parent workbench of observed perspectives
	 * 
	 * @param workbench
	 */
	public abstract void setParentWorkbench(
			final IWorkbench<?, C, L, A, M> workbench);
	


}
