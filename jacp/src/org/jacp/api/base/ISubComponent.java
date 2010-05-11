package org.jacp.api.base;

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
}
