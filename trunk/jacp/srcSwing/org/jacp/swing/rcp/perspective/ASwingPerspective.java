/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.rcp.perspective;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IExtendedComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.observers.IComponentObserver;
import org.jacp.api.observers.IObserver;
import org.jacp.api.perspective.IPerspective;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.action.SwingActionListener;
import org.jacp.swing.rcp.component.AStateComponent;
import org.jacp.swing.rcp.component.ASwingComponent;
import org.jacp.swing.rcp.componentLayout.SwingPerspectiveLayout;
import org.jacp.swing.rcp.observers.SwingComponentObserver;
import org.jacp.swing.rcp.util.ComponentAddWorker;
import org.jacp.swing.rcp.util.ComponentInitWorker;
import org.jacp.swing.rcp.util.ComponentReplaceWorker;
import org.jacp.swing.rcp.util.StateComponentInitWorker;

/**
 * represents a basic swing perspective that handles subcomponents
 * 
 * @author Andy Moncsek
 */
public abstract class ASwingPerspective implements
		IPerspective<ActionListener, ActionEvent, Object>,
		IExtendedComponent<Container> {

	private final List<ISubComponent<ActionListener, ActionEvent, Object>> subcomponents = new CopyOnWriteArrayList<ISubComponent<ActionListener, ActionEvent, Object>>();
	private IObserver<ActionListener, ActionEvent, Object> perspectiveObserver;
	private final IComponentObserver<ActionListener, ActionEvent, Object> componentObserver = new SwingComponentObserver(
			this);
	private final IPerspectiveLayout<Container, Container> perspectiveLayout = new SwingPerspectiveLayout();
	private String id;
	private String name;
	private boolean active;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void init() {
		((SwingComponentObserver) componentObserver).execute();
	}

	/**
	 * add menu entries to perspective specific menu
	 * 
	 * @param menuBar
	 */
	public abstract void handleMenuEntries(final JMenu menuBar);

	@Override
	public abstract void handleBarEntries(final Container toolBar,
			final Container bottomBar);

	@Override
	public <C> C handle(final IAction<ActionEvent, Object> action) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * handle perspective call, create structure of perspective, set base layout
	 * and register component containers
	 * 
	 * @param action
	 * @param perspectiveLayout
	 */
	public abstract void handlePerspective(final SwingAction action,
			final SwingPerspectiveLayout perspectiveLayout);

	@Override
	public void handlePerspective(final IAction<ActionEvent, Object> action) {
		handlePerspective((SwingAction) action,
				(SwingPerspectiveLayout) perspectiveLayout);

	}

	@Override
	public void registerComponent(
			final ISubComponent<ActionListener, ActionEvent, Object> component,
			final IComponentObserver<ActionListener, ActionEvent, Object> handler) {
		synchronized (handler) {
			log("register component: " + component.getId());
			handler.addComponent(component);
			subcomponents.add(component);
			component.setParentPerspective(this);
		}

	}

	@Override
	public void unregisterComponent(
			final ISubComponent<ActionListener, ActionEvent, Object> component,
			final IComponentObserver<ActionListener, ActionEvent, Object> handler) {
		synchronized (handler) {
			log("unregister component: " + component.getId());
			handler.removeComponent(component);
			subcomponents.remove(component);
			component.setParentPerspective(null);
		}
	}

	@Override
	public void handleMenuEntries(final Container meuneBar) {
		synchronized (meuneBar) {
			if (meuneBar instanceof JMenu) {
				this.handleMenuEntries((JMenu) meuneBar);
			}
		}
	}

	@Override
	public void initSubcomponents(final IAction<ActionEvent, Object> action,
			final IPerspective<ActionListener, ActionEvent, Object> perspective) {
		synchronized (action) {
			final String targetId = getTargetComponentId(action.getTargetId());
			log("3.4.4.1: subcomponent targetId: " + targetId);
			for (final ISubComponent<ActionListener, ActionEvent, Object> component : perspective
					.getSubcomponents()) {
				if (component.getId().equals(targetId)) {
					log("3.4.4.2: subcomponent init with custom action");
					initSubcomonent(action, component);
				} else if (component.isActive()) {
					log("3.4.4.2: subcomponent init with default action");
					initSubcomonent(new SwingAction(component.getId(),
							component.getId(), "init"), component);
				} // if END

			} // for END
		}
	}

	@Override
	public void initSubcomonent(final IAction<ActionEvent, Object> action,
			final ISubComponent<ActionListener, ActionEvent, Object> component) {
		synchronized (action) {
			if (component instanceof ASwingComponent) {
				log("COMPONENT EXECUTE INIT:::" + component.getName());
				final ComponentInitWorker tmp = new ComponentInitWorker(
						perspectiveLayout.getTargetLayoutComponents(),
						((ASwingComponent) component), action);
				tmp.execute();
				log("COMPONENT DONE EXECUTE INIT:::" + component.getName());
			} else if (component instanceof AStateComponent) {
				log("BACKGROUND COMPONENT EXECUTE INIT:::"
						+ component.getName());
				final StateComponentInitWorker worker = new StateComponentInitWorker(
						component, action);
				worker.execute();
				log("BACKGROUND COMPONENT DONE EXECUTE INIT:::"
						+ component.getName());
			}

		}
	}

	@Override
	public void handleAndReplaceSubcomponent(
			final IAction<ActionEvent, Object> action,
			final ISubComponent<ActionListener, ActionEvent, Object> component) {
		// TODO when "component" is synchronized execution is 30x times faster
		// but will lock sometimes !!
		synchronized (action) {
			if (component.isBlocked()) {
				prepareMessageHandling(component, action);
				log("ADD TO QUEUE:::" + component.getName());
			} else {
				prepareMessageHandling(component, action);
				executeComponentReplaceThread(perspectiveLayout, component,
						action);
				log("CREATE NEW THREAD:::" + component.getName());
			}
			log("DONE EXECUTE REPLACE:::" + component.getName());
		}

	}

	/**
	 * start component replace thread
	 * 
	 * @param layout
	 * @param component
	 * @param action
	 */
	private void executeComponentReplaceThread(
			final IPerspectiveLayout<? extends Container, Container> layout,
			final ISubComponent<ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		if (component instanceof ASwingComponent) {
			final ComponentReplaceWorker tmp = new ComponentReplaceWorker(
					layout.getTargetLayoutComponents(),
					((ASwingComponent) component), action);
			tmp.execute();
		}

	}

	/**
	 * set component blocked and add message to queue
	 * 
	 * @param component
	 * @param action
	 */
	private void prepareMessageHandling(
			final ISubComponent<ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		component.setBlocked(true);
		component.putIncomingMessage(action);
	}

	@Override
	public void addActiveComponent(
			final ISubComponent<ActionListener, ActionEvent, Object> component) {
		synchronized (component) {
			// register new component at perspective
			registerComponent(component, componentObserver);
			// add component root to correct target
			addComponentUIValue(getIPerspectiveLayout()
					.getTargetLayoutComponents(), component);
		}
	}

	/**
	 * handles ui return value and it to perspective TODO check correctness of
	 * implementation
	 * 
	 * @param targetComponents
	 * @param component
	 */
	private void addComponentUIValue(
			final Map<String, Container> targetComponents,
			final ISubComponent<ActionListener, ActionEvent, Object> component) {
		if (component instanceof ASwingComponent) {
			final ComponentAddWorker worker = new ComponentAddWorker(
					targetComponents, ((ASwingComponent) component));
			worker.execute();
		}
	}

	@Override
	public void delegateTargetChange(final String target,
			final ISubComponent<ActionListener, ActionEvent, Object> component) {
		synchronized (target) {
			final String parentId = getTargetParentId(target);
			if (!id.equals(parentId)) {
				// unregister component in current perspective
				unregisterComponent(component, componentObserver);
				// delegate to perspective observer
				perspectiveObserver.delegateTargetChange(target, component);

			}
		}
	}

	@Override
	public void delegateMassege(final String target,
			final IAction<ActionEvent, Object> action) {
		synchronized (action) {
			perspectiveObserver.delegateMessage(target, action);
		}
	}

	@Override
	public void delegateComponentMassege(final String target,
			final IAction<ActionEvent, Object> action) {
		synchronized (action) {
			componentObserver.delegateMessage(target, action);
		}
	}

	@Override
	public String getName() {
		if (id == null) {
			throw new UnsupportedOperationException("No name set");
		}
		return name;
	}

	@Override
	public String getId() {
		if (id == null) {
			throw new UnsupportedOperationException("No id set");
		}
		return id;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public void setActive(final boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setObserver(
			final IObserver<ActionListener, ActionEvent, Object> perspectiveObserver) {
		this.perspectiveObserver = perspectiveObserver;
	}

	@Override
	public IActionListener<ActionListener, ActionEvent, Object> getActionListener() {
		return new SwingActionListener(new SwingAction(id), perspectiveObserver);
	}

	@Override
	public IPerspectiveLayout<Container, Container> getIPerspectiveLayout() {
		return perspectiveLayout;
	}

	/**
	 * register components at componentObserver
	 * 
	 * @param <M>
	 * @param components
	 */
	private <M extends ISubComponent<ActionListener, ActionEvent, Object>> void registerSubcomponents(
			final List<M> components) {
		for (final M component : components) {
			registerComponent(component, componentObserver);
		}
	}

	/**
	 * returns the message target id
	 * 
	 * @param messageId
	 * @return
	 */
	private String getTargetComponentId(final String messageId) {
		final String[] targetId = getTargetId(messageId);
		if (isFullValidId(targetId)) {
			return targetId[1];
		}
		return messageId;
	}

	/**
	 * returns the message (parent) target id
	 * 
	 * @param messageId
	 * @return
	 */
	private String getTargetParentId(final String messageId) {
		final String[] parentId = getTargetId(messageId);
		if (isFullValidId(parentId)) {
			return parentId[0];
		}
		return messageId;
	}

	/**
	 * returns target message with perspective and component name
	 * 
	 * @param messageId
	 * @return
	 */
	private String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

	/**
	 * a target id is valid, when it does contain a perspective and a component
	 * id (perspectiveId.componentId)
	 * 
	 * @param targetId
	 * @return
	 */
	private boolean isFullValidId(final String[] targetId) {
		if (targetId != null && targetId.length == 2) {
			return true;
		}

		return false;
	}

	@Override
	public List<ISubComponent<ActionListener, ActionEvent, Object>> getSubcomponents() {
		return subcomponents;
	}

	@Override
	public void setSubcomponents(
			final List<ISubComponent<ActionListener, ActionEvent, Object>> subComponents) {
		registerSubcomponents(subComponents);
	}

	private void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}
	}
}
