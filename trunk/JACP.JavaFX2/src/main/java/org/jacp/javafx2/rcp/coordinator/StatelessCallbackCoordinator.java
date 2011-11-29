package org.jacp.javafx2.rcp.coordinator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.api.coordinator.IStatelessComponentCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.javafx2.rcp.component.AStatelessCallbackComponent;
import org.jacp.javafx2.rcp.util.StateLessComponentRunWorker;

public class StatelessCallbackCoordinator implements
		IStatelessComponentCoordinator<EventHandler<Event>, Event, Object> {
	public static int MAX_INCTANCE_COUNT;

	private final AtomicInteger threadCount = new AtomicInteger(0);

	private final List<ICallbackComponent<EventHandler<Event>, Event, Object>> componentInstances = new CopyOnWriteArrayList<ICallbackComponent<EventHandler<Event>, Event, Object>>();

	private ICallbackComponent<EventHandler<Event>, Event, Object> baseComponent;
	private final Launcher<?> launcher;
	private final ExecutorService executor = Executors
			.newFixedThreadPool(StatelessCallbackCoordinator.MAX_INCTANCE_COUNT + 10);

	public StatelessCallbackCoordinator(
			final ICallbackComponent<EventHandler<Event>, Event, Object> baseComponent,
			final Launcher<?> launcher) {
		this.launcher = launcher;
		this.setBaseComponent(baseComponent);
	}

	static {
		final Runtime runtime = Runtime.getRuntime();
		final int nrOfProcessors = runtime.availableProcessors();
		StatelessCallbackCoordinator.MAX_INCTANCE_COUNT = nrOfProcessors
				+ (nrOfProcessors / 2);
	}

	@Override
	public void incomingMessage(IAction<Event, Object> message) {
		synchronized (this.baseComponent) {
			// get active instance
			final ICallbackComponent<EventHandler<Event>, Event, Object> comp = this
					.getActiveComponent();
			if (comp != null) {
				if (this.componentInstances.size() < StatelessCallbackCoordinator.MAX_INCTANCE_COUNT) {
					// create new instance
					this.componentInstances
							.add(this
									.getCloneBean(((AStatelessCallbackComponent) this.baseComponent)
											.getClass()));
				} // End inner if
					// run component in thread
				this.instanceRun(comp, message);
			} // End if
			else {
				// check if new instances can be created
				if (this.componentInstances.size() < StatelessCallbackCoordinator.MAX_INCTANCE_COUNT) {
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
			final ICallbackComponent<EventHandler<Event>, Event, Object> comp,
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
		final ICallbackComponent<EventHandler<Event>, Event, Object> comp = this
				.getCloneBean(((AStatelessCallbackComponent) this.baseComponent)
						.getClass());
		this.componentInstances.add(comp);
		this.instanceRun(comp, message);
	}

	@Override
	public <T extends ICallbackComponent<EventHandler<Event>, Event, Object>> ICallbackComponent<EventHandler<Event>, Event, Object> getCloneBean(
			Class<T> clazz) {
		return ((AStatelessCallbackComponent) this.baseComponent)
				.init(this.launcher.getBean(clazz));
	}

	/**
	 * Returns a component instance that is currently not blocked.
	 * 
	 * @return
	 */
	private final ICallbackComponent<EventHandler<Event>, Event, Object> getActiveComponent() {
		for (int i = 0; i < this.componentInstances.size(); i++) {
			final ICallbackComponent<EventHandler<Event>, Event, Object> comp = this.componentInstances
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
	private void seekAndPutMessage(final IAction<Event, Object> message) {
		// if max count reached, seek through components and add
		// message to queue of oldest component
		final ICallbackComponent<EventHandler<Event>, Event, Object> comp = this.componentInstances
				.get(this.getSeekValue());
		// put message to queue
		comp.putIncomingMessage(message);
	}

	private int getSeekValue() {
		final int seek = this.threadCount.incrementAndGet()
				% this.componentInstances.size();
		this.threadCount.set(seek);
		return seek;
	}

	/**
	 * returns base component instance
	 * 
	 * @return
	 */
	public final ICallbackComponent<EventHandler<Event>, Event, Object> getBaseComponent() {
		return this.baseComponent;
	}

	/**
	 * set base component instance
	 * 
	 * @param baseComponent
	 */
	public final void setBaseComponent(
			final ICallbackComponent<EventHandler<Event>, Event, Object> baseComponent) {
		this.baseComponent = baseComponent;
		this.componentInstances.add(this
				.getCloneBean(((AStatelessCallbackComponent) baseComponent)
						.getClass()));
	}

}
