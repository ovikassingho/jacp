package org.jacp.swing.rcp.observers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


import org.jacp.api.component.ISubComponent;
import org.jacp.api.observers.IComponentObserver;
import org.jacp.api.perspective.IPerspective;


/**
 * This Handler coordinates observers and components in perspectives; each added component get his own observer; handler instantiates an observer set the parent perspective an the component list
 * @author Andy Moncsek
 *
 */
public class ComponentObserverCoordinator  {
    private final List<ISubComponent<ActionListener, ActionEvent, Object>> components;
    
    private final IPerspective<ActionListener, ActionEvent, Object> perspective;
    
    public ComponentObserverCoordinator(final IPerspective<ActionListener, ActionEvent, Object> perspective,final List<ISubComponent<ActionListener, ActionEvent, Object>> components) {
	this.perspective = perspective;
	this.components = components;
    }
    
    /**
     * 
     * @param component
     */
    public void addComponent(final ISubComponent<ActionListener, ActionEvent, Object> component) {
	components.add(component);
	component.setObserver(getObserverInstance());
    }
    
    private IComponentObserver<ActionListener, ActionEvent, Object> getObserverInstance() {
	final IComponentObserver<ActionListener, ActionEvent, Object> componentObserver = new SwingComponentObserver(
		this.perspective,this.components);
	((SwingComponentObserver) componentObserver).start();
	return componentObserver;
    }
    
    public void removeComponent(
	    final ISubComponent<ActionListener, ActionEvent, Object> component) {
	component.setObserver(null);
	components.remove(component);

    }
   
}
