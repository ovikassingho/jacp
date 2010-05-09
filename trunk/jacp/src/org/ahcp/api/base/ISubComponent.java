package org.ahcp.api.base;

/**
 * defines a subcomponent handled by a root component
 * 
 * @author Andy Moncsek
 * 
 * @param <C>
 *            defines the base component where others extend from
 * @param <A>
 *            defines the action listener type
 * @param <E>
 *            defines the basic action type
 * @param <T>
 *            defines the basic message type
 */
public interface ISubComponent<C, A, E, T> extends IComponent<C, A, E, T> {
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
			final IPerspective<C, A, E, T> perspective);

	/**
	 * returns responsible perspective
	 * 
	 * @return
	 */
	public abstract IPerspective<C, A, E, T> getParentPerspective();
}
