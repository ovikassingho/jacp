package org.ahcp.api.ria.base;

import java.util.List;

import org.ahcp.api.ria.action.IAction;
import org.ahcp.api.ria.componentLayout.IWorkbenchLayout;

/**
 * base component fpor an application, handles perspectives and containing
 * components C defines the base component where others extend L defines the
 * basic layout manager A defines the basic action listener E defines a basic
 * event T defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IWorkbench<C, L, A, E, T> {

	/**
	 * returns basic container to handle perspectives
	 * 
	 * @return
	 */
	public abstract C init();

	/**
	 * init default workbench menu
	 */
	public abstract void initWorkbenchMenu();

	/**
	 * set default menu bar instance to workspace
	 */
	public abstract void initMenuBar();

	/**
	 * returns default workbench menu
	 * 
	 * @return
	 */
	public abstract C getDefaultMenu();

	/**
	 * add/remove menu entries to workbench instance
	 * 
	 * @return
	 */
	public abstract C handleMenuEntries(C meuBar);

	/**
	 * set toolBar to workspace
	 */
	public abstract void initToolBar();

	/**
	 * returns default toolbar implementation
	 * 
	 * @return
	 */
	public abstract C getToolBar();

	/**
	 * set bottomBar to workspace
	 */
	public abstract void initBottomBar();

	/**
	 * returns default bottom bar implementation
	 * 
	 * @return
	 */
	public abstract C getBottomBar();

	/**
	 * add default workbench actions to tool bar
	 * 
	 * @param toolBar
	 */
	public abstract void handleBarEntries(C toolBar, C bottomBar);

	/**
	 * set perspectives to workbench
	 * 
	 * @param perspectives
	 */
	public abstract void setPerspectives(
			final List<IPerspective<C, A, E, T>> perspectives);

	/**
	 * get perspectives in workbench
	 * 
	 * @return
	 */
	public abstract List<IPerspective<C, A, E, T>> getPerspectives();

	/**
	 * init single perspective instance
	 * 
	 * @param perspective
	 * @param action
	 */
	public abstract void initPerspective(
			final IPerspective<C, A, E, T> perspective, IAction<T, E> action);

	/**
	 * calls perspective handle method and replaces the old entry by new one
	 * 
	 * @param perspective
	 * @param action
	 */
	public void replacePerspective(final IPerspective<C, A, E, T> perspective,
			final IAction<T, E> action);

	/**
	 * set visibility of all components in workspacewrapper to false
	 * 
	 * @param component
	 */
	public abstract void disableComponents();

	/**
	 * anable all components in workspace; for use in
	 * initPerspectiveInWindowMode
	 */
	public abstract void enableComponents();

	/**
	 * handle workbench layout
	 * 
	 * @param action
	 * @param layout
	 */
	public abstract void handleInitialLayout(IAction<T, E> action,
			IWorkbenchLayout<L> layout);

	/**
	 * returns workbench layout object
	 * 
	 * @return
	 */
	public abstract IWorkbenchLayout<L> getWorkbenchLayout();

}
