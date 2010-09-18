package org.jacp.swing.rcp.observers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


import org.jacp.api.component.ISubComponent;
import org.jacp.api.observers.IComponentObserver;
import org.jacp.api.perspective.IPerspective;



public class ComponentObserverHandler  {
    private final List<ISubComponent<ActionListener, ActionEvent, Object>> components;
    
    private final IPerspective<ActionListener, ActionEvent, Object> perspective;
    
    public ComponentObserverHandler(final IPerspective<ActionListener, ActionEvent, Object> perspective,final List<ISubComponent<ActionListener, ActionEvent, Object>> components) {
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
