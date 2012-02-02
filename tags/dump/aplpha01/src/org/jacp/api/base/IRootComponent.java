package org.jacp.api.base;

/**
 * all root components have containing sub components (workspace ->
 * perspectives; perspective - editors) and listeners; all sub components have
 * to be registerd
 * 
 * @author Andy Moncsek
 * 
 * @param <T>
 *            component to register
 * @param <H>
 *            handler where component have to be registered
 */
public interface IRootComponent<T> {

	/**
	 * register component at listener
	 * 
	 * @param component
	 */
	abstract public void registerComponent(final T component);

}
