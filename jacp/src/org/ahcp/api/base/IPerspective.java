package org.ahcp.api.base;

import java.util.List;

import org.ahcp.api.action.IAction;
import org.ahcp.api.componentLayout.IPerspectiveLayout;
import org.ahcp.api.observers.IComponentObserver;

/**
 * a perspective is a root component handled by an workbench and contains
 * subcomponents
 * 
 * @author Andy Moncsek
 * 
 * @param <C>
 *            defines the base component where others extend from
 * @param <A>
 *            defines the action listener type
 * @param <E>
 *            defines the basic action type
 * @param <T>
 *            defines the basic message type
 */
public interface IPerspective<C, A, E, T>
		extends
		IExtendedComponent<C>,
		IComponent<C, A, E, T>,
		IRootComponent<ISubComponent<C, A, E, T>, IComponentObserver<C, A, E, T>> {

	/**
	 * the initialization method
	 */
	public abstract void init();

	/**
	 * set all available views of perspective
	 * 
	 * @param views
	 */
	public abstract void setViews(List<IView<C, A, E, T>> views);

	/**
	 * returns all available views of perspective
	 * 
	 * @return
	 */
	public abstract List<IView<C, A, E, T>> getViews();

	/**
	 * set editors of a perspective
	 * 
	 * @param editors
	 */
	public abstract void setEditors(List<IEditor<C, A, E, T>> editors);

	/**
	 * get perspectives editors
	 * 
	 * @return
	 */
	public abstract List<IEditor<C, A, E, T>> getEditors();

	/**
	 * handle baselayout when perspective started
	 * 
	 * @param action
	 * @param perspectiveLayout
	 */
	public abstract void handleInitialLayout(IAction<T, E> action,
			IPerspectiveLayout<? extends C, C> perspectiveLayout);

	/**
	 * returns perspectives layout dto
	 * 
	 * @return
	 */
	public abstract IPerspectiveLayout<? extends C, C> getIPerspectiveLayout();

	/**
	 * handles init of subcomponents in perspective TODO move to IRootcomponent
	 * 
	 * @param layout
	 * @param perspective
	 */
	public abstract void initSubcomponents(final IAction<T, E> action,
			final IPerspectiveLayout<? extends C, C> layout,
			final IPerspective<C, A, E, T> perspective);

	/**
	 * TODO move to IRoot component handles initialisation of a single
	 * component; TODO change from IEditor to global interface
	 * 
	 * @param layout
	 * @param editor
	 */
	public abstract void initSubcomonent(final IAction<T, E> action,
			final IPerspectiveLayout<? extends C, C> layout,
			final ISubComponent<C, A, E, T> component);

	/**
	 * handles init and replace of subcomponents in perspective
	 * 
	 * @param layout
	 * @param component
	 * @param action
	 */
	public abstract void replaceSubcomponent(
			final IPerspectiveLayout<? extends C, C> layout,
			final ISubComponent<C, A, E, T> component,
			final IAction<T, E> action);

	/**
	 * delegates massage to responsible componentObserver to notify component
	 * 
	 * @param target
	 * @param action
	 */
	public void delegateComponentMassege(final String target,
			final IAction<T, E> action);

	/**
	 * delegates message to responsible perspectiveObserver to notify
	 * perspective
	 * 
	 * @param target
	 * @param action
	 */
	public void delegateMassege(final String target, final IAction<T, E> action);

}
