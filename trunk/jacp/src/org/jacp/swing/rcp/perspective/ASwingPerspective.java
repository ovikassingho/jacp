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
import org.jacp.api.base.IPerspective;
import org.jacp.api.base.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.observers.IComponentObserver;
import org.jacp.api.observers.IObserver;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.action.SwingActionListener;
import org.jacp.swing.rcp.componentLayout.SwingPerspectiveLayout;
import org.jacp.swing.rcp.observers.SwingComponentObserver;
import org.jacp.swing.rcp.util.ComponentAddWorker;
import org.jacp.swing.rcp.util.ComponentInitWorker;
import org.jacp.swing.rcp.util.ComponentReplaceWorker;

/**
 * represents a basic swing perspective that handles subcomponents
 * 
 * @author Andy Moncsek
 */
public abstract class ASwingPerspective<T extends Container> implements
		IPerspective<Container, ActionListener, ActionEvent, Object> {

	private final List<ISubComponent<Container, ActionListener, ActionEvent, Object>> subcomponents = new CopyOnWriteArrayList<ISubComponent<Container, ActionListener, ActionEvent, Object>>();
	private IObserver<Container, ActionListener, ActionEvent, Object> perspectiveObserver;
	private final IComponentObserver<Container, ActionListener, ActionEvent, Object> componentObserver = new SwingComponentObserver(
			this);
	private final IPerspectiveLayout<T, Container> perspectiveLayout = new SwingPerspectiveLayout<T>();
	private String id;
	private String name;
	private boolean active;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void init() {
	}

	public abstract void addMenuEntries(JMenu menuBar);

	@Override
	public abstract void handleBarEntries(Container toolBar, Container bottomBar);

	@Override
	public Container handle(final IAction<ActionEvent, Object> action) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public abstract void handleInitialLayout(SwingAction action,
			SwingPerspectiveLayout<T> perspectiveLayout);

	@Override
	public void handleInitialLayout(
			final IAction<ActionEvent, Object> action,
			final IPerspectiveLayout<? extends Container, Container> perspectiveLayout) {
		handleInitialLayout((SwingAction) action,
				(SwingPerspectiveLayout<T>) perspectiveLayout);

	}

	@Override
	public void registerComponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		componentObserver.addComponent(component);
		this.subcomponents.add(component);
		component.setParentPerspective(this);

	}

	/**
	 * unregister component from current perspective TODO add to api
	 * 
	 * @param component
	 */
	private void unregisterComponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IComponentObserver<Container, ActionListener, ActionEvent, Object> handler) {
		handler.removeComponent(component);
		this.subcomponents.remove(component);
		component.setParentPerspective(null);
	}

	@Override
	public void addMenuEntries(final Container meuneBar) {
		if (meuneBar instanceof JMenu) {
			this.addMenuEntries((JMenu) meuneBar);
		}

	}

	@Override
	public void initSubcomponents(
			final IAction<ActionEvent, Object> action,
			final IPerspectiveLayout<? extends Container, Container> layout,
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		final String targetId = getTargetComponentId(action.getTargetId());
		log("3.4.4.1: subcomponent targetId: " + targetId);
		for (final ISubComponent<Container, ActionListener, ActionEvent, Object> component : perspective
				.getSubcomponents()) {
			if (component.getId().equals(targetId)) {
				log("3.4.4.2: subcomponent init with custom action");
				initSubcomonent(action, layout, component);
			} else if (component.isActive()) {
				log("3.4.4.2: subcomponent init with default action");
				initSubcomonent(new SwingAction(component.getId(), component
						.getId(), "init"), layout, component);
			} // if END

		} // for END
	}

	@Override
	public void initSubcomonent(
			final IAction<ActionEvent, Object> action,
			final IPerspectiveLayout<? extends Container, Container> layout,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		final ComponentInitWorker tmp = new ComponentInitWorker(layout
				.getTargetLayoutComponents(), component, action);
		tmp.execute();
		System.out.println("DONE EXECUTE INIT:::"+component.getName());
	}

	@Override
	public void handleAndReplaceSubcomponent(
			final IPerspectiveLayout<? extends Container, Container> layout,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		final ComponentReplaceWorker tmp = new ComponentReplaceWorker(layout
				.getTargetLayoutComponents(), component, action);
		tmp.execute();
		System.out.println("DONE EXECUTE REPLACE:::"+component.getName());

	}

	@Override
	public void addComponentUIValue(
			final Map<String, Container> targetComponents,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		final ComponentAddWorker worker = new ComponentAddWorker(
				targetComponents, component);
		worker.execute();
	}

	@Override
	public synchronized void delegateTargetChange(
			final String target,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		final String parentId = getTargetParentId(target);
		if (!this.id.equals(parentId)) {
			// unregister component in current perspective
			unregisterComponent(component, this.componentObserver);
			// delegate to perspective observer
			this.perspectiveObserver.delegateTargetChange(target, component);

		}
	}

	@Override
	public synchronized void delegateMassege(final String target,
			final IAction<ActionEvent, Object> action) {
		this.perspectiveObserver.delegateMessage(target, action);
	}

	@Override
	public synchronized void delegateComponentMassege(final String target,
			final IAction<ActionEvent, Object> action) {
		this.componentObserver.delegateMessage(target, action);
	}

	@Override
	public String getName() {
		if (this.id == null) {
			throw new UnsupportedOperationException("No name set");
		}
		return this.name;
	}

	@Override
	public String getId() {
		if (this.id == null) {
			throw new UnsupportedOperationException("No id set");
		}
		return this.id;
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
		return this.active;
	}

	@Override
	public void setObserver(
			final IObserver<Container, ActionListener, ActionEvent, Object> perspectiveObserver) {
		this.perspectiveObserver = perspectiveObserver;
	}

	@Override
	public IActionListener<ActionListener, ActionEvent, Object> getActionListener() {
		return new SwingActionListener(new SwingAction(id), perspectiveObserver);
	}

	@Override
	public IPerspectiveLayout<T, Container> getIPerspectiveLayout() {
		return this.perspectiveLayout;
	}

	/**
	 * register components at componentObserver
	 * 
	 * @param <M>
	 * @param components
	 */
	private <M extends ISubComponent<Container, ActionListener, ActionEvent, Object>> void registerSubcomponents(
			final List<M> components) {
		for (final M component : components) {
			registerComponent(component);
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
	public List<ISubComponent<Container, ActionListener, ActionEvent, Object>> getSubcomponents() {
		return subcomponents;
	}

	@Override
	public void setSubcomponents(
			final List<ISubComponent<Container, ActionListener, ActionEvent, Object>> subComponents) {
		registerSubcomponents(subComponents);
	}

	private void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}
	}
}
