package org.jacp.api.observers;

import java.util.List;

import org.jacp.api.action.IAction;
import org.jacp.api.base.IComponent;
import org.jacp.api.base.ISubComponent;

/**
 * Defines a basic observer for component messages; handles the message and
 * delegate to responsible component
 * 
 * @author Andy Moncsek
 * 
 * @param <C>
 *            defines the base component where others extend from
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface IObserver<L, A, M> {
	/**
	 * handles an action and delegate it to addressed perspective
	 * 
	 * @param action
	 */
	public abstract void handle(IAction<A, M> action);

	/**
	 * handles message to specific component addressed by id
	 * 
	 * @param id
	 * @param action
	 */
	public abstract void handleMessage(final String id, IAction<A, M> action);

	/**
	 * delegate message from a subcomponent or "outside" to target perspective
	 * TODO better javadoc!!!
	 * 
	 * @param target
	 * @param action
	 */
	public void delegateMessage(final String target, final IAction<A, M> action);

	/**
	 * returns specific, observed perspective or component by id
	 * 
	 * @param id
	 * @return
	 */
	public <P extends IComponent<L, A, M>> P getObserveableById(
			final String id, final List<P> perspectives);

	/**
	 * handle message to active component
	 * 
	 * @param <M>
	 * @param component
	 * @param action
	 */
	public <P extends IComponent<L, A, M>> void handleActive(final P component,
			final IAction<A, M> action);

	/**
	 * handle message to inactive component
	 * 
	 * @param <M>
	 * @param component
	 * @param action
	 */
	public <P extends IComponent<L, A, M>> void handleInActive(
			final P component, final IAction<A, M> action);

	/**
	 * delegate component target change to an other perspective
	 * 
	 * @param target
	 * @param component
	 */
	public abstract void delegateTargetChange(final String target,
			final ISubComponent<L, A, M> component);
}
