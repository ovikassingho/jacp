package org.jacp.api.observers;

import org.jacp.api.base.ISubComponent;

/**
 * notifies components included in perspective
 * 
 * @param <C>
 *            defines the base component where others extend from
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IComponentObserver<C, L, A, M> extends IObserver<C, L, A, M> {

	/**
	 * add component to observe
	 * 
	 * @param component
	 */
	public abstract void addComponent(ISubComponent<C, L, A, M> component);

	/**
	 * remove component; e.g. when component is deactivated
	 * 
	 * @param component
	 */
	public abstract void removeComponent(ISubComponent<C, L, A, M> component);

}
