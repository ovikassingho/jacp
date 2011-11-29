package org.jacp.api.componentLayout;

import org.jacp.api.util.ToolbarPosition;

/**
 * Defines a bean containing the defined tool bars and the main menu
 * 
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
	@Deprecated
	C getToolBar(Layout layout);

	/**
	 * Gets the registered tool bar.
	 *
	 * @param position the position
	 * @return the registered tool bar
	 */
	C getRegisteredToolBar(ToolbarPosition position);

	/**
	 * Returns the application menu instance.
	 * 
	 * @return
	 */
	C getMenu();
}
