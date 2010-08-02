package org.jacp.api.component;

/**
 * represents a extended component with menu entries and tool bar access
 * 
 * @author Andy Moncsek
 * @param <C>
 *            defines the base component where others extend from
 */
public interface IExtendedComponent<C> {

	/**
	 * get custom menu entries
	 * 
	 * @return
	 */
	public abstract void handleMenuEntries(final C meuneBar);

	/**
	 * add custom actions to toolbar
	 * 
	 * @param toolBar
	 */
	public abstract void handleBarEntries(final C toolBar, final C bottomBar);

}
