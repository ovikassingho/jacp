package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import org.jacp.api.component.IVComponent;

public final class ChunkDTO {
    private final Container parent;
	private final Map<String, Container> targetComponents;
	private final String currentTaget;
	private final IVComponent<Container, ActionListener, ActionEvent, Object> component;
	private final Container previousContainer;

	public ChunkDTO(
		final Container parent,
		final Container previousContainer,
		final Map<String, Container> targetComponents,
		final String currentTaget,
		final IVComponent<Container, ActionListener, ActionEvent, Object> component) {
	    this.parent = parent;
	    this.targetComponents = targetComponents;
	    this.currentTaget = currentTaget;
	    this.component = component;
	    this.previousContainer = previousContainer;
	}

	public Container getParent() {
	    return parent;
	}

	public Map<String, Container> getTargetComponents() {
	    return targetComponents;
	}

	public String getCurrentTaget() {
	    return currentTaget;
	}

	public IVComponent<Container, ActionListener, ActionEvent, Object> getComponent() {
	    return component;
	}

	public Container getPreviousContainer() {
	    return previousContainer;
	}
}
