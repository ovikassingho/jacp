package org.jacp.swing.rcp.coordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IBGComponent;
import org.jacp.api.coordinator.IStatelessComponentCoordinator;
import org.jacp.impl.AHCPLauncher;
import org.jacp.swing.rcp.component.AStatelessComponent;
import org.jacp.swing.rcp.util.StateLessComponentRunWorker;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * controls instantiation of state less component clones; each put to a new
 * instance until max count is reached, if no instance is unblocked message is
 * put to queue of first instance thread
 * 
 * @author Andy Moncsek
 * 
 */
@ManagedResource(objectName = "org.jacp:name=StatelessComponentCoordinator", description = "a state ful swing component")
public class StatelessComponentCoordinator implements
	IStatelessComponentCoordinator<ActionListener, ActionEvent, Object> {

    public static final int MAX_INCTANCE_COUNT = 10;

    private AtomicInteger threadCount = new AtomicInteger(0);

    private final List<IBGComponent<ActionListener, ActionEvent, Object>> componentInstances = new CopyOnWriteArrayList<IBGComponent<ActionListener, ActionEvent, Object>>();

    private IBGComponent<ActionListener, ActionEvent, Object> baseComponent;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

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
		    componentInstances.add(getBean((Class<? extends IBGComponent<ActionListener, ActionEvent, Object>>) this.baseComponent.getClass()));
		}
		// run component in thread
		comp.setBlocked(true);
		comp.putIncomingMessage(message);
		StateLessComponentRunWorker worker = new StateLessComponentRunWorker(
			comp);
		worker.execute();
	    } else {
		// check if new instances can be created
		if (componentInstances.size() < MAX_INCTANCE_COUNT) {
		    comp = getBean((Class<? extends IBGComponent<ActionListener, ActionEvent, Object>>) this.baseComponent.getClass());
		    componentInstances.add(comp);
		    // run component in thread
		    comp.setBlocked(true);
		    comp.putIncomingMessage(message);
		    StateLessComponentRunWorker worker = new StateLessComponentRunWorker(
			    comp);
		    worker.execute();
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
    
    private IBGComponent<ActionListener, ActionEvent, Object> getBean(Class<? extends IBGComponent<ActionListener, ActionEvent, Object>> class1) {
	ClassPathXmlApplicationContext context = AHCPLauncher.getContext();
	 String[] name = context.getBeanNamesForType(class1);
	 if(name.length>0) {
	    return  ((AStatelessComponent)baseComponent).init((IBGComponent<ActionListener, ActionEvent, Object>) context.getBean(name[0]));
	 }
	return null;
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
	componentInstances.add(getBean((Class<? extends IBGComponent<ActionListener, ActionEvent, Object>>) this.baseComponent.getClass()));
    }

    private void log(final String message) {
	if (logger.isLoggable(Level.FINE)) {
	    logger.fine(">> " + message);
	}
    }
    @ManagedAttribute
    protected List<IBGComponent<ActionListener, ActionEvent, Object>> getComponentInstances() {
        return componentInstances;
    }
}
