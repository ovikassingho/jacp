package org.jacp.api.componentLayout;
/**
 * Defines a bean containing the defined tool bars and the main menu
 * @author Andy Moncsek
 * @param <C>
 *            defines the base component where others extend from
 */
public interface IBaseLayout<C> {
	/**
	 * Returns all registered tool bars of workbench.
	 * 
	 * @return a map containing the defined tool bars
	 */
	C getToolBar(Layout layout);

	/**
	 * Returns the application menu instance.
	 * 
	 * @return
	 */
	C getMenu();
}
