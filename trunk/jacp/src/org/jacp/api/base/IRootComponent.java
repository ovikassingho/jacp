package org.jacp.api.base;

/**
 * all root components have containing sub components (workspace ->
 * perspectives; perspective - editors) and listeners; all sub components have
 * to be registered
 * 
 * @author Andy Moncsek
 * 
 * @param <T>
 *            component to register
 * @param <H>
 *            handler where component have to be registered
 */
public interface IRootComponent<T, H> {

	/**
	 * register component at listener
	 * 
	 * @param component
	 */
	abstract public void registerComponent(final T component, final H handler);

	/**
	 * unregister component from current perspective
	 * 
	 * @param component
	 */
	abstract public void unregisterComponent(final T component, final H handler);

}
