/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ahcp.swing.rcp.componentLayout;

import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

import org.ahcp.api.componentLayout.IPerspectiveLayout;

/**
 * Configuration handler for perspective components, used in handle method for
 * configuration and registration of layout 'leaves' where subcomponents can
 * live in. Create your own complex layout, return the root node and register
 * parts of your layout that can handle subcomponents
 * 
 * @author Andy Moncsek
 */
public class SwingPerspectiveLayout<T> implements
		IPerspectiveLayout<T, Container> {

	private T layoutComponent;
	private final boolean scrollable = true;
	private boolean replaceMode = true;
	private final Map<String, Container> targetComponents = new HashMap<String, Container>();

	public SwingPerspectiveLayout() {
	}

	@Override
	public void setReplaceMode(final boolean replace) {
		this.replaceMode = replace;
	}

	@Override
	public boolean isReplaceMode() {
		return replaceMode;
	}

	@Override
	public void setRootLayoutComponent(final T comp) {
		this.layoutComponent = comp;
	}

	@Override
	public T getRootLayoutComponent() {
		return this.layoutComponent;
	}

	public boolean isScrollable() {
		return scrollable;
	}

	@Override
	public void registerTargetLayoutComponent(final String id,
			final Container target) {
		this.targetComponents.put(id, target);
	}

	@Override
	public Map<String, Container> getTargetLayoutComponents() {
		return this.targetComponents;
	}

}
