package org.ahcp.api.ria.observers;

import org.ahcp.api.ria.base.IPerspective;
import org.ahcp.api.ria.base.ISubComponent;

/**
 * notifies components included in perspective C defines the base component
 * where others extend A defines the action listener type E defines the basic
 * action type T defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IComponentObserver<C, A, E, T> extends IObserver<C, A, E, T> {

	/**
	 * add component to observe
	 * 
	 * @param component
	 */
	public abstract void addComponent(ISubComponent<C, A, E, T> component);

	/**
	 * remove component; e.g. when component is deactivated
	 * 
	 * @param component
	 */
	public abstract void removeComponent(ISubComponent<C, A, E, T> component);

	/**
	 * set parent perspective of observed components (1P -> NComp.)
	 * 
	 * @param perspective
	 */
	public abstract void setParentPerspective(
			IPerspective<C, A, E, T> perspective);

}
