package org.jacp.javafx2.rcp.coordinator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IBGComponent;
import org.jacp.api.coordinator.IStatelessComponentCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.javafx2.rcp.component.AStatelessComponent;
import org.jacp.javafx2.rcp.util.StateLessComponentRunWorker;

public class StatelessComponentCoordinator implements
		IStatelessComponentCoordinator<EventHandler<Event>, Event, Object> {
	public static int MAX_INCTANCE_COUNT;

	private final AtomicInteger threadCount = new AtomicInteger(0);

	private final List<IBGComponent<EventHandler<Event>, Event, Object>> componentInstances = new CopyOnWriteArrayList<IBGComponent<EventHandler<Event>, Event, Object>>();

	private IBGComponent<EventHandler<Event>, Event, Object> baseComponent;
	private final Launcher<?> launcher;
	private final ExecutorService executor = Executors
			.newFixedThreadPool(StatelessComponentCoordinator.MAX_INCTANCE_COUNT);

	public StatelessComponentCoordinator(
			final IBGComponent<EventHandler<Event>, Event, Object> baseComponent,
			final Launcher<?> launcher) {
		this.launcher = launcher;
		this.setBaseComponent(baseComponent);
	}

	static {
		final Runtime runtime = Runtime.getRuntime();
		final int nrOfProcessors = runtime.availableProcessors();
		StatelessComponentCoordinator.MAX_INCTANCE_COUNT = nrOfProcessors
				+ (nrOfProcessors / 2);
	}

	@Override
	public void incomingMessage(IAction<Event, Object> message) {
		synchronized (this.baseComponent) {
			// get active instance
			final IBGComponent<EventHandler<Event>, Event, Object> comp = this
					.getActiveComponent();
			if (comp != null) {
				if (this.componentInstances.size() < StatelessComponentCoordinator.MAX_INCTANCE_COUNT) {
					// create new instance
					this.componentInstances
							.add(this
									.getCloneBean(((AStatelessComponent) this.baseComponent)
											.getClass()));
				} // End inner if
					// run component in thread
				this.instanceRun(comp, message);
			} // End if
			else {
				// check if new instances can be created
				if (this.componentInstances.size() < StatelessComponentCoordinator.MAX_INCTANCE_COUNT) {
					this.createInstanceAndRun(message);
				} // End if
				else {
					this.seekAndPutMessage(message);
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
			final IBGComponent<EventHandler<Event>, Event, Object> comp,
			final IAction<Event, Object> message) {
		comp.setBlocked(true);
		comp.putIncomingMessage(message);
		final StateLessComponentRunWorker worker = new StateLessComponentRunWorker(
				comp);
		this.executor.submit(worker);
	}

	/**
	 * if max thread count is not reached and all available component instances
	 * are blocked create a new one, block it an run in thread
	 * 
	 * @param message
	 */
	private void createInstanceAndRun(final IAction<Event, Object> message) {
		final IBGComponent<EventHandler<Event>, Event, Object> comp = this
				.getCloneBean(((AStatelessComponent) this.baseComponent)
						.getClass());
		this.componentInstances.add(comp);
		this.instanceRun(comp, message);
	}

	@Override
	public <T extends IBGComponent<EventHandler<Event>, Event, Object>> IBGComponent<EventHandler<Event>, Event, Object> getCloneBean(
			Class<T> clazz) {
		return ((AStatelessComponent) this.baseComponent).init(this.launcher
				.getBean(clazz));
	}

	/**
	 * returns a component that is not blocked
	 * 
	 * @return
	 */
	private final IBGComponent<EventHandler<Event>, Event, Object> getActiveComponent() {
		for (int i = 0; i < this.componentInstances.size(); i++) {
			final IBGComponent<EventHandler<Event>, Event, Object> comp = this.componentInstances
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
	private final void seekAndPutMessage(final IAction<Event, Object> message) {
		// if max count reached, seek through components and add
		// message to queue of oldest component
		final Integer seek = Integer
				.valueOf(this.threadCount.incrementAndGet())
				% this.componentInstances.size();
		final IBGComponent<EventHandler<Event>, Event, Object> comp = this.componentInstances
				.get(seek);
		// put message to queue
		comp.putIncomingMessage(message);
	}

	/**
	 * returns base component instance
	 * 
	 * @return
	 */
	public final IBGComponent<EventHandler<Event>, Event, Object> getBaseComponent() {
		return this.baseComponent;
	}

	/**
	 * set base component instance
	 * 
	 * @param baseComponent
	 */
	public final void setBaseComponent(
			final IBGComponent<EventHandler<Event>, Event, Object> baseComponent) {
		this.baseComponent = baseComponent;
		this.componentInstances
				.add(this.getCloneBean(((AStatelessComponent) baseComponent)
						.getClass()));
	}

}
