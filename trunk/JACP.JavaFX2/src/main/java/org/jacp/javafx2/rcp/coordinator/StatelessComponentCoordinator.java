package org.jacp.javafx2.rcp.coordinator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IBGComponent;
import org.jacp.api.coordinator.IStatelessComponentCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.javafx2.rcp.component.AStatelessComponent;
import org.jacp.javafx2.rcp.util.StateLessComponentRunWorker;

public class StatelessComponentCoordinator
		implements
		IStatelessComponentCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> {
	public static int MAX_INCTANCE_COUNT;

	private final AtomicInteger threadCount = new AtomicInteger(0);

	private final List<IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object>> componentInstances = new CopyOnWriteArrayList<IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object>>();

	private IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> baseComponent;
	private final Launcher<?> launcher;
	private ExecutorService executor = Executors
			.newFixedThreadPool(MAX_INCTANCE_COUNT);

	public StatelessComponentCoordinator(
			final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> baseComponent,
			final Launcher<?> launcher) {
		this.launcher = launcher;
		setBaseComponent(baseComponent);
	}

	static {
		final Runtime runtime = Runtime.getRuntime();
		final int nrOfProcessors = runtime.availableProcessors();
		MAX_INCTANCE_COUNT = nrOfProcessors + (nrOfProcessors / 2);
	}

	@Override
	public void incomingMessage(IAction<ActionEvent, Object> message) {
		synchronized (baseComponent) {
			// get active instance
			final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> comp = getActiveComponent();
			if (comp != null) {
				if (componentInstances.size() < MAX_INCTANCE_COUNT) {
					// create new instance
					componentInstances
							.add(getCloneBean(((AStatelessComponent) baseComponent)
									.getClass()));
				} // End inner if
					// run component in thread
				instanceRun(comp, message);
			} // End if
			else {
				// check if new instances can be created
				if (componentInstances.size() < MAX_INCTANCE_COUNT) {
					createInstanceAndRun(message);
				} // End if
				else {
					seekAndPutMessage(message);
				} // End else
			} // End else

		} // End synchronized
	}

	/**
	 * block component, put message to component's queue and run in thread
	 * 
	 * @param comp
	 * @param message
	 */
	private final void instanceRun(
			final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> comp,
			final IAction<ActionEvent, Object> message) {
		comp.setBlocked(true);
		comp.putIncomingMessage(message);
		final StateLessComponentRunWorker worker = new StateLessComponentRunWorker(
				comp);
		executor.submit(worker);
	}

	/**
	 * if max thread count is not reached and all available component instances
	 * are blocked create a new one, block it an run in thread
	 * 
	 * @param message
	 */
	private void createInstanceAndRun(final IAction<ActionEvent, Object> message) {
		final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> comp = getCloneBean(((AStatelessComponent) baseComponent)
				.getClass());
		componentInstances.add(comp);
		instanceRun(comp, message);
	}

	@Override
	public <T extends IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object>> IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> getCloneBean(
			Class<T> clazz) {
		return ((AStatelessComponent) baseComponent).init(launcher
				.getBean(clazz));
	}

	/**
	 * returns a component that is not blocked
	 * 
	 * @return
	 */
	private final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> getActiveComponent() {
		for (int i = 0; i < componentInstances.size(); i++) {
			final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> comp = componentInstances
					.get(i);
			if (!comp.isBlocked()) {
				return comp;
			} // End if
		} // End for

		return null;
	}

	/**
	 * seek to first running component in instance list and add message to queue
	 * of selected component
	 * 
	 * @param message
	 */
	private final void seekAndPutMessage(
			final IAction<ActionEvent, Object> message) {
		// if max count reached, seek through components and add
		// message to queue of oldest component
		final Integer seek = Integer.valueOf(threadCount.incrementAndGet())
				% componentInstances.size();
		final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> comp = componentInstances
				.get(seek);
		// put message to queue
		comp.putIncomingMessage(message);
	}

	/**
	 * returns base component instance
	 * 
	 * @return
	 */
	public final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> getBaseComponent() {
		return baseComponent;
	}

	/**
	 * set base component instance
	 * 
	 * @param baseComponent
	 */
	public final void setBaseComponent(
			final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> baseComponent) {
		this.baseComponent = baseComponent;
		componentInstances
				.add(getCloneBean(((AStatelessComponent) baseComponent)
						.getClass()));
	}

}
