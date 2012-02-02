package org.jacp.api.component;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.observers.IObserver;

/**
 * represents a basic component
 * 
 * @author Andy Moncsek
 * @param <C>
 *            defines the return type of component handling; when component is
 *            an UI component C is the basic type where other elements extends
 *            from
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface IComponent<L, A, M> {

	/**
	 * handles component when called
	 * 
	 * @param action
	 * @return view component
	 */
	public abstract <C> C handle(final IAction<A, M> action);

	/**
	 * returns action listener (for local, target and global use)
	 * 
	 * @return
	 */
	public abstract IActionListener<L, A, M> getActionListener();

	/**
	 * returns id of component
	 * 
	 * @return
	 */
	public abstract String getId();

	/**
	 * set unique id of component
	 * 
	 * @param id
	 */
	public abstract void setId(final String id);

	/**
	 * get active status of perspective
	 * 
	 * @return
	 */
	public abstract boolean isActive();

	/**
	 * set active state of perspective
	 * 
	 * @param active
	 */
	public abstract void setActive(boolean active);

	/**
	 * returns the name of a component
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * defines the name of a component
	 * 
	 * @param name
	 */
	public abstract void setName(String name);

	/**
	 * observer to handle changes in components
	 * 
	 * @return
	 */
	public abstract void setObserver(final IObserver<L, A, M> observer);

}
