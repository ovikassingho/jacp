/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ahcp.api.componentLayout;

import java.util.Map;

/**
 * defines layout of a perspective and the container for included editors and
 * views (target components); for use in perspectives handle method M - type of
 * root component B . type cof target components
 * 
 * @author Andy Moncsek
 */
public interface IPerspectiveLayout<M, B> {

	/**
	 * set behaviour of perspective if container can't handle parralel
	 * subcomponents; decide if subcomponents are replaced or not
	 * 
	 * @param replace
	 */
	public abstract void setReplaceMode(boolean replace);

	/**
	 * cheack replacement of subcomponents behaviour
	 * 
	 * @return
	 */
	public abstract boolean isReplaceMode();

	/**
	 * set Layoutwrapper for perspective; this wrapper contains wrappers for
	 * editors and views
	 * 
	 * @param comp
	 */
	public abstract void setRootLayoutComponent(M comp);

	/**
	 * getLayoutwrapper for perspective
	 * 
	 * @return
	 */
	public abstract M getRootLayoutComponent();

	/**
	 * returns map of target components and ids key - id value - target
	 * component
	 * 
	 * @return
	 */
	public Map<String, B> getTargetLayoutComponents();

	/**
	 * register a target component; a target component defines a wrapper where
	 * editors and views can "live" in; you can define a target for each editor
	 * or view component; create an root component, a complex layout an register
	 * all components where editors/views should displayed in
	 * 
	 * @param id
	 * @param target
	 */
	public void registerTargetLayoutComponent(final String id, final B target);
}
