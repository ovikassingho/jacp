package org.ahcp.api.ria.observers;

import java.util.List;

import org.ahcp.api.ria.action.IAction;
import org.ahcp.api.ria.base.IComponent;

public interface IObserver<C, A, E, T> {
	/**
	 * handles an action and delegate it to addressed perspective
	 * 
	 * @param action
	 */
	public abstract void handle(IAction<T, E> action);

	public abstract void handleMessage(final String id, IAction<T, E> action);

	/**
	 * delegate message from a subcomponent or "outside" to target perspective
	 * TODO better javadoc!!!
	 * 
	 * @param target
	 * @param action
	 */
	public void delegateMessage(final String target, final IAction<T, E> action);

	/**
	 * returns specific, observed perspective or component by id
	 * 
	 * @param id
	 * @return
	 */
	public <M extends IComponent<C, A, E, T>> M getObserveableById(
			final String id, final List<M> perspectives);

	/**
	 * handle message to active component
	 * 
	 * @param <M>
	 * @param component
	 * @param action
	 */
	public <M extends IComponent<C, A, E, T>> void handleActive(
			final M component, final IAction<T, E> action);

	/**
	 * handle message to inactive component
	 * 
	 * @param <M>
	 * @param component
	 * @param action
	 */
	public <M extends IComponent<C, A, E, T>> void handleInActive(
			final M component, final IAction<T, E> action);
}
