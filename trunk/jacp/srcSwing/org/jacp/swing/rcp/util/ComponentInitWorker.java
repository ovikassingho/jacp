package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IVComponent;

/**
 * Background Worker to execute components; handle method to init component
 * 
 * @author Andy Moncsek
 * 
 */
public class ComponentInitWorker
	extends
	AbstractComponentWorker<IVComponent<Container, ActionListener, ActionEvent, Object>> {
    private final Map<String, Container> targetComponents;
    private final IVComponent<Container, ActionListener, ActionEvent, Object> component;
    private final IAction<ActionEvent, Object> action;

    public ComponentInitWorker(
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
	synchronized (component) {
	    component.setBlocked(true);
	    log("3.4.4.2.1: subcomponent handle init START: "
		    + component.getName());
	    final Container editorComponent = component.handle(action);
	    component.setRoot(editorComponent);
	    editorComponent.setVisible(true);
	    editorComponent.setEnabled(true);
	    log("3.4.4.2.2: subcomponent handle init get valid container: "
		    + component.getName());
	    final Container validContainer = getValidContainerById(
		    targetComponents, component.getExecutionTarget());
	    log("3.4.4.2.3: subcomponent handle init add component by type: "
		    + component.getName());
	    addComponentByType(validContainer, component);
	    log("3.4.4.2.4: subcomponent handle init END: "
		    + component.getName());
	    return component;
	}

	// return runHandleSubcomponent(editor, action);
    }

    @Override
    protected IVComponent<Container, ActionListener, ActionEvent, Object> runHandleSubcomponent(
	    final IVComponent<Container, ActionListener, ActionEvent, Object> component,
	    final IAction<ActionEvent, Object> action) {
	synchronized (component) {
	    log("3.4.4.2.1: subcomponent handle init START: "
		    + component.getName());
	    final Container editorComponent = component.handle(action);
	    component.setRoot(editorComponent);
	    editorComponent.setVisible(true);
	    editorComponent.setEnabled(true);
	    log("3.4.4.2.2: subcomponent handle init get valid container: "
		    + component.getName());
	    final Container validContainer = getValidContainerById(
		    targetComponents, component.getExecutionTarget());
	    log("3.4.4.2.3: subcomponent handle init add component by type: "
		    + component.getName());
	    addComponentByType(validContainer, component);
	    log("3.4.4.2.4: subcomponent handle init END: "
		    + component.getName());
	}
	return component;
    }

    @Override
    public void done() {
	component.setBlocked(false);
    }

}
