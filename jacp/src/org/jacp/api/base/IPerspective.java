package org.jacp.api.base;

import java.util.List;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.observers.IComponentObserver;

/**
 * a perspective is a root component, handled by an workbench and contains
 * subcomponents
 * 
 * @author Andy Moncsek
 * 
 * @param <C>
 *            defines the base component where others extend from
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface IPerspective<C, L, A, M>
		extends
		IExtendedComponent<C>,
		IComponent<C, L, A, M>,
		IRootComponent<ISubComponent<C, L, A, M>, IComponentObserver<C, L, A, M>> {

	/**
	 * the initialization method
	 */
	public abstract void init();

	/**
	 * set all available views of perspective
	 * 
	 * @param views
	 */
	public abstract void setViews(final List<IView<C, L, A, M>> views);

	/**
	 * returns all available views of perspective
	 * 
	 * @return
	 */
	public abstract List<IView<C, L, A, M>> getViews();

	/**
	 * set editors of a perspective
	 * 
	 * @param editors
	 */
	public abstract void setEditors(final List<IEditor<C, L, A, M>> editors);

	/**
	 * get perspectives editors
	 * 
	 * @return
	 */
	public abstract List<IEditor<C, L, A, M>> getEditors();

	/**
	 * handle baselayout when perspective started
	 * 
	 * @param action
	 * @param perspectiveLayout
	 */
	public abstract void handleInitialLayout(final IAction<A, M> action,
			final IPerspectiveLayout<? extends C, C> perspectiveLayout);

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
	public abstract void initSubcomponents(final IAction<A, M> action,
			final IPerspectiveLayout<? extends C, C> layout,
			final IPerspective<C, L, A, M> perspective);

	/**
	 * TODO move to IRoot component handles initialisation of a single
	 * component; TODO change from IEditor to global interface
	 * 
	 * @param layout
	 * @param editor
	 */
	public abstract void initSubcomonent(final IAction<A, M> action,
			final IPerspectiveLayout<? extends C, C> layout,
			final ISubComponent<C, L, A, M> component);

	/**
	 * handles init and replace of subcomponents in perspective
	 * 
	 * @param layout
	 * @param component
	 * @param action
	 */
	public abstract void replaceSubcomponent(
			final IPerspectiveLayout<? extends C, C> layout,
			final ISubComponent<C, L, A, M> component,
			final IAction<A, M> action);

	/**
	 * delegates massage to responsible componentObserver to notify target component
	 * 
	 * @param target
	 * @param action
	 */
	public abstract void delegateComponentMassege(final String target,
			final IAction<A, M> action);

	/**
	 * delegates message to responsible perspectiveObserver to notify
	 * target perspective
	 * 
	 * @param target
	 * @param action
	 */
	public abstract void delegateMassege(final String target, final IAction<A, M> action);

}
