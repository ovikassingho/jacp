package org.jacp.swing.rcp.observers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IBGComponent;
/**
 * controls instantiation of state less component clones; each  put to a new instance until max count is reached, if no instance is unblocked message is put to queue of first instance thread
 * @author Andy Moncsek
 *
 */
public class StatelessComponentCoordinator {

    public static final int MAX_INCTANCE_COUNT = 10;
    
    private AtomicInteger threadCount=new AtomicInteger(0);

    private final List<IBGComponent<ActionListener, ActionEvent, Object>> componentInstances = new CopyOnWriteArrayList<IBGComponent<ActionListener, ActionEvent, Object>>();
    
    private IBGComponent<ActionListener, ActionEvent, Object> baseComponent;

    public void register(
	    final IBGComponent<ActionListener, ActionEvent, Object> component) {
	this.componentInstances.add(component);
    }

    public void incomingMessage(IAction<ActionEvent, Object> message) {
	if(!baseComponent.isBlocked()) {
	    // handle component worker
	} else {
	    // get active instance
	    IBGComponent<ActionListener, ActionEvent, Object> comp= getActiveComponent();
	    if(comp!=null) {
		 // handle component worker
	    } else {
		// check if new instances can be created
		if(componentInstances.size()<MAX_INCTANCE_COUNT) {
		    comp = this.baseComponent.getNewInstance();
		    componentInstances.add(comp);
		} else {
		    // if max count reached, seek through components and add message to queue of oldest component
		    Integer seek = Integer.valueOf(threadCount.incrementAndGet()) %componentInstances.size();
		    comp = componentInstances.get(seek);
		   // put message to queue
		    comp.putIncomingMessage(message);
		}
	    }
	    // if instance is NULL get first element in list and put message to queue (an increment position counter (counter = counter mod instances.size()))
	}
/*	int count = 0;
	IBGComponent<ActionListener, ActionEvent, Object> instanceTemp = null;
	for (final IBGComponent<ActionListener, ActionEvent, Object> instance : componentInstances) {
	    count++;
	    if (instance.isBlocked()) {
		if (componentInstances.size() <= MAX_INCTANCE_COUNT
			|| count <= MAX_INCTANCE_COUNT) {

		} else {
		    handleWorkerCallToPostbox();
		    break;
		}
	    } else {
		// Initializes worker thread with current instance
		handleWorkerCall();
		break;
	    }

	}
	if(instanceTemp!=null) {
	    componentInstances.add(instanceTemp);
	}*/
    }
    
    private IBGComponent<ActionListener, ActionEvent, Object> getActiveComponent() {
	for(IBGComponent<ActionListener, ActionEvent, Object> comp: componentInstances) {
	    if(!comp.isBlocked())return comp;
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

}
