package org.jacp.api.base;

import java.util.List;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IWorkbenchLayout;

/**
 * base component for an application, handles perspectives and containing
 * components
 * 
 * @param <P>
 *            defines the default layout manager
 * 
 * @param <C>
 *            defines the base component where others extend from
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IWorkbench<P, C, L, A, M> {

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
			final List<IPerspective<L, A, M>> perspectives);

	/**
	 * get perspectives in workbench
	 * 
	 * @return
	 */
	public abstract List<IPerspective<L, A, M>> getPerspectives();

	/**
	 * init single perspective instance
	 * 
	 * @param perspective
	 * @param action
	 */
	public abstract void initPerspective(
			final IPerspective<L, A, M> perspective, IAction<A, M> action);

	/**
	 * calls perspective handle method and replaces the old entry by new one
	 * 
	 * @param perspective
	 * @param action
	 */
	public void replacePerspective(final IPerspective<L, A, M> perspective,
			final IAction<A, M> action);

	/**
	 * set visibility of all components in workspace wrapper to false
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
	public abstract void handleInitialLayout(IAction<A, M> action,
			IWorkbenchLayout<P> layout);

	/**
	 * returns workbench layout object
	 * 
	 * @return
	 */
	public abstract IWorkbenchLayout<P> getWorkbenchLayout();

}
