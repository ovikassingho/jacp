package org.jacp.swing.rcp.coordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IBGComponent;
import org.jacp.api.coordinator.IStatelessComponentCoordinator;
import org.jacp.swing.rcp.util.StateLessComponentRunWorker;

/**
 * controls instantiation of state less component clones; each put to a new
 * instance until max count is reached, if no instance is unblocked message is
 * put to queue of first instance thread
 * 
 * @author Andy Moncsek
 * 
 */
public class StatelessComponentCoordinator implements
	IStatelessComponentCoordinator<ActionListener, ActionEvent, Object> {

    public static final int MAX_INCTANCE_COUNT = 10;

    private AtomicInteger threadCount = new AtomicInteger(0);

    private final List<IBGComponent<ActionListener, ActionEvent, Object>> componentInstances = new CopyOnWriteArrayList<IBGComponent<ActionListener, ActionEvent, Object>>();

    private IBGComponent<ActionListener, ActionEvent, Object> baseComponent;

    public StatelessComponentCoordinator(
	    IBGComponent<ActionListener, ActionEvent, Object> baseComponent) {
	setBaseComponent(baseComponent);
    }

    public StatelessComponentCoordinator() {

    }

    

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jacp.swing.rcp.coordinator.IStatelessComponentCoordinator#incomingMessage
     * (org.jacp.api.action.IAction)
     */
    @Override
    public void incomingMessage(IAction<ActionEvent, Object> message) {
	synchronized (baseComponent) {

	    // get active instance
	    IBGComponent<ActionListener, ActionEvent, Object> comp = getActiveComponent();
	    if (comp != null) {
		
		if (componentInstances.size() < MAX_INCTANCE_COUNT) {
		 // create new instance
		    componentInstances.add(this.baseComponent.getNewInstance());
		}
		// run component in thread
		comp.putIncomingMessage(message);
		StateLessComponentRunWorker worker = new StateLessComponentRunWorker(
			comp);
		worker.run();
	    } else {
		// check if new instances can be created
		if (componentInstances.size() < MAX_INCTANCE_COUNT) {
		    comp = this.baseComponent.getNewInstance();
		    componentInstances.add(comp);
		    // run component in thread
		    comp.putIncomingMessage(message);
		    StateLessComponentRunWorker worker = new StateLessComponentRunWorker(
			    comp);
		    worker.run();
		} else {
		    // if max count reached, seek through components and add
		    // message to queue of oldest component
		    Integer seek = Integer.valueOf(threadCount
			    .incrementAndGet()) % componentInstances.size();
		    comp = componentInstances.get(seek);
		    // put message to queue
		    comp.putIncomingMessage(message);
		}
	    }
	    // if instance is NULL get first element in list and put message to
	    // queue (an increment position counter (counter = counter mod
	    // instances.size()))

	}
    }

    private IBGComponent<ActionListener, ActionEvent, Object> getActiveComponent() {
	for (IBGComponent<ActionListener, ActionEvent, Object> comp : componentInstances) {
	    if (!comp.isBlocked())
		return comp;
	}

	return null;
    }

    public void flushInstances() {

    }

    public void handleWorkerCall() {

    }

    public void handleWorkerCallToPostbox() {

    }

    public IBGComponent<ActionListener, ActionEvent, Object> getBaseComponent() {
	return baseComponent;
    }

    public void setBaseComponent(
	    IBGComponent<ActionListener, ActionEvent, Object> baseComponent) {
	this.baseComponent = baseComponent;
	componentInstances.add(baseComponent.getNewInstance());
    }
    
    private void log(final String message) {
	if (logger.isLoggable(Level.FINE)) {
	    logger.fine(">> " + message);
	}
    }
}
