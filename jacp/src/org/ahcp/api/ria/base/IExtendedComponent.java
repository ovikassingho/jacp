package org.ahcp.api.ria.base;

/**
 * represents a extended component with menu entries and toolbar access
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
	public abstract void addMenuEntries(final C meuneBar);

	/**
	 * add custom actions to toolbar
	 * 
	 * @param toolBar
	 */
	public abstract void handleBarEntries(C toolBar, C bottomBar);

}
