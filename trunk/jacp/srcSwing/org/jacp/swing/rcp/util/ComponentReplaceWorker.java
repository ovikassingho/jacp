package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.jacp.api.action.IAction;
import org.jacp.api.base.IVComponent;

/**
 * Background Worker to execute components handle method to replace or add the
 * component
 * 
 * @author Andy Moncsek
 * 
 */
public class ComponentReplaceWorker extends AbstractComponentWorker<IVComponent<Container, ActionListener, ActionEvent, Object>> {
	private final Map<String, Container> targetComponents;
	private final IVComponent<Container, ActionListener, ActionEvent, Object> component;
	private final IAction<ActionEvent, Object> action;
	private volatile BlockingQueue<Boolean> lock = new ArrayBlockingQueue<Boolean>(
			1);

	public ComponentReplaceWorker(
			final Map<String, Container> targetComponents,
			final IVComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		this.targetComponents = targetComponents;
		this.component = component;
		this.action = action;
	}

	@Override
	protected IVComponent<Container, ActionListener, ActionEvent, Object> doInBackground()
			throws Exception {
		return runHandleSubcomponent(component, action);
	}

	@Override
	protected IVComponent<Container, ActionListener, ActionEvent, Object> runHandleSubcomponent(
			final IVComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		synchronized (component) {
			lock.add(true);
			while (component.hasIncomingMessage()) {
				final IAction<ActionEvent, Object> myAction = component
						.getNextIncomingMessage();
				try {
					lock.take();
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}

				log(" //1.1.1.1.1// handle replace component BEGIN: "
						+ component.getName());
				final Map<String, Container> targetComponents = this.targetComponents;
				final Container previousContainer = component.getRoot();
				final String currentTaget = component.getTarget();
				// run code
				log(" //1.1.1.1.2// handle component: " + component.getName());
				prepareAndHandleComponent(component, myAction);
				final Container parent = previousContainer.getParent();
				publish(new ChunkDTO(parent, previousContainer,
						targetComponents, currentTaget, component));

			}
			component.setBlocked(false);
			return component;

		}

	}

	@Override
	protected void process(final List<ChunkDTO> chunks) {
		// process method runs in EventDispatchThread
		for (final ChunkDTO dto : chunks) {
			final Container parent = dto.getParent();
			final IVComponent<Container, ActionListener, ActionEvent, Object> component = dto
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
			lock.add(true);
		}
	}

	@Override
	protected void done() {

	}

}
