package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import org.jacp.api.action.IAction;
import org.jacp.api.base.ISubComponent;

/**
 * Background Worker to execute components handle method to replace or add the
 * component
 * 
 * @author Andy Moncsek
 * 
 */
public class ComponentReplaceWorker extends AbstractComponentWorker {
	private final Map<String, Container> targetComponents;
	private final ISubComponent<Container, ActionListener, ActionEvent, Object> component;
	private final IAction<ActionEvent, Object> action;

	private volatile boolean block = false;

	public ComponentReplaceWorker(
			final Map<String, Container> targetComponents,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		this.targetComponents = targetComponents;
		this.component = component;
		this.action = action;
	}

	@Override
	protected ISubComponent<Container, ActionListener, ActionEvent, Object> doInBackground()
			throws Exception {
		return runHandleSubcomponent(component, action);
	}

	@Override
	protected ISubComponent<Container, ActionListener, ActionEvent, Object> runHandleSubcomponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		synchronized (component) {
			while (component.hasIncomingMessage()) {
				final IAction<ActionEvent, Object> myAction = component
						.getNextIncomingMessage();
				while (block) {
					// wait for finish in EventDispatchThread
				}

				log(" //1.1.1.1.1// handle replace component BEGIN: "
						+ component.getName());
				final Map<String, Container> targetComponents = this.targetComponents;
				final Container previousContainer = component.getRoot();
				final String currentTaget = component.getTarget();
				// run code
				log(" //1.1.1.1.2// handle component: " + component.getName());
				// final ChunkDTO dto = new ChunkDTO(parent,previousContainer,
				// targetComponents, currentTaget, component);
				prepareAndHandleComponent(component, myAction);
				final Container parent = previousContainer.getParent();
				block = true;
				publish(new ChunkDTO(parent, previousContainer,
						targetComponents, currentTaget, component));

			}
			component.setBlocked(false);
			return component;

		}

	}

	@Override
	protected void process(final List<ChunkDTO> chunks) {
		for (final ChunkDTO dto : chunks) {
			final Container parent = dto.getParent();
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component = dto
					.getComponent();
			final Container previousContainer = dto.getPreviousContainer();
			final String currentTaget = dto.getCurrentTaget();
			if (!currentTaget.equals(component.getTarget())
					|| !previousContainer.equals(component.getRoot())) {
				// remove old view
				log(" //1.1.1.1.3// handle old component remove: "
						+ component.getName());
				handleOldComponentRemove(parent, previousContainer);
				// add new view
				log(" //1.1.1.1.4// handle new component insert: "
						+ component.getName());
				handleNewComponentValue(component, targetComponents, parent,
						currentTaget);
			}
			block = false;
		}
	}

	@Override
	protected void done() {

	}

}
