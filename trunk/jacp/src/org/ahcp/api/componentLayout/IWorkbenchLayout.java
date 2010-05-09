/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ahcp.api.componentLayout;

import org.ahcp.api.base.Tupel;
import org.ahcp.api.base.WorkspaceMode;

/**
 * defines the base layout of a workbench and the application
 * 
 * @author Andy Moncsek
 */
public interface IWorkbenchLayout<L> {

	/**
	 * get defined workspace layout
	 * 
	 * @return
	 */
	public abstract WorkspaceMode getWorkspaceMode();

	/**
	 * set workspace layout to window or stack mode
	 * 
	 * @param mode
	 */
	public abstract void setWorkspaceMode(WorkspaceMode mode);

	/**
	 * check if menues are enabled
	 * 
	 * @return
	 */
	public abstract boolean isMenuEnabled();

	/**
	 * set menues enabled
	 * 
	 * @param enabled
	 */
	public abstract void setMenuEnabled(boolean enabled);

	/**
	 * set toolbar enabled and define position in workspace
	 * 
	 * @param enabled
	 * @param position
	 */
	// TODO allow use of greneric list of hints like P... p
	public abstract void setToolBarEnabled(boolean enabled, Layout position);

	/**
	 * check if toolbar is enabled
	 * 
	 * @return
	 */
	public abstract boolean isToolbarEnabled();

	/**
	 * get defined toolbar layout
	 * 
	 * @return
	 */
	public abstract Layout getToolBarLayout();

	/**
	 * set bottom bar enabled and define position
	 * 
	 * @param enabled
	 * @param position
	 */
	// TODO allow use of greneric list of hints like P... p
	public abstract void setBottomBarEnabled(boolean enabled, Layout position);

	/**
	 * check if bottom bar is enabled
	 * 
	 * @return
	 */
	public abstract boolean isBottomBarEnabled();

	/**
	 * get defined bottom bar layout
	 * 
	 * @return
	 */
	public abstract Layout getBottomBarLayout();

	/**
	 * set default layout manager to workspace
	 * 
	 * @param layout
	 */
	public abstract void setLayoutManager(L layout);

	/**
	 * get defined layout manager
	 * 
	 * @return
	 */
	public abstract L getLayoutManager();

	/**
	 * set size of workbench
	 * 
	 * @param x
	 * @param y
	 */
	public abstract void setWorkbenchXYSize(int x, int y);

	/**
	 * returns a tupel defining the workbench size
	 * 
	 * @return
	 */
	public abstract Tupel<Integer, Integer> getWorkbenchSize();

}
