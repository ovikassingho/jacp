package org.jacp.api.base;

import org.jacp.api.action.IAction;

/**
 * defines a subcomponent handled by a root component
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
public interface ISubComponent<C, L, A, M> extends IComponent<C, L, A, M> {
	/**
	 * returns the ui target; defines the target container in perspective
	 * 
	 * @param target
	 */
	public abstract String getTarget();

	/**
	 * set the ui target; defines the target container in perspective
	 * 
	 * @param target
	 */
	public abstract void setTarget(final String target);

	/**
	 * set the root ui component created by the handle method
	 * 
	 * @param root
	 */
	public abstract void setRoot(C root);

	/**
	 * returns 'root' ui component created by the handle method
	 * 
	 * @return
	 */
	public abstract C getRoot();

	/**
	 * set responsible perspective
	 * 
	 * @param perspective
	 */
	public abstract void setParentPerspective(
			final IPerspective<C, L, A, M> perspective);

	/**
	 * returns responsible perspective
	 * 
	 * @return
	 */
	public abstract IPerspective<C, L, A, M> getParentPerspective();

	/**
	 * returns true if component has message in pipe
	 * 
	 * @return
	 */
	public abstract boolean hasIncomingMessage();

	/**
	 * add new message to component
	 * 
	 * @param action
	 */
	public abstract void putIncomingMessage(final IAction<A, M> action);

	/**
	 * returns next message in pipe
	 * 
	 * @return
	 */
	public IAction<A, M> getNextIncomingMessage();

	/**
	 * component is blocked when executed in thread
	 * 
	 * @return
	 */
	public boolean isBlocked();

	/**
	 * block component when run in thread
	 * 
	 * @param blocked
	 */
	public void setBlocked(final boolean blocked);
}
