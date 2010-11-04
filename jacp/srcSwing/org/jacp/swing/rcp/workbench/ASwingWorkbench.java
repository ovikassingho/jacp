/*
 * Copyright (C) 2010,2011.
 * AHCP Project
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.jacp.swing.rcp.workbench;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IRootComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.component.ASwingComponent;
import org.jacp.swing.rcp.componentLayout.SwingWorkbenchLayout;
import org.jacp.swing.rcp.coordinator.SwingPerspectiveCoordinator;
import org.jacp.swing.rcp.handler.MacOSXController;
import org.jacp.swing.rcp.perspective.ASwingPerspective;

import com.apple.mrj.MRJApplicationUtils;

/**
 * represents the basic swing workbench instance; handles perspectives and
 * components;
 * 
 * @author Andy Moncsek
 */
public abstract class ASwingWorkbench extends JFrame
	implements
	IWorkbench<LayoutManager2, Container, ActionListener, ActionEvent, Object>,
	IRootComponent<IPerspective<ActionListener, ActionEvent, Object>, IPerspectiveCoordinator<ActionListener, ActionEvent, Object>,IAction<ActionEvent, Object>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1740398352308498810L;
    private JMenu menu;
    private List<IPerspective<ActionListener, ActionEvent, Object>> perspectives;
    private final IPerspectiveCoordinator<ActionListener, ActionEvent, Object> perspectiveObserver = new SwingPerspectiveCoordinator(
	    this);
    private final Dimension screenSize = Toolkit.getDefaultToolkit()
	    .getScreenSize();
    private final int inset = 50;
    private final IWorkbenchLayout<LayoutManager2> layout = new SwingWorkbenchLayout();

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ASwingWorkbench(final String name) {
	super(name);

    }

    @Override
    public Container init() {
	log("1: init workbench");
	// init user defined workspace
	this.handleInitialLayout(new SwingAction("TODO", "init"), layout);
	final Container contentPane = getContentPane();
	setBasicLayout(contentPane);
	log("3: handle initialisation sequence");
	handleInitialisationSequence();

	return contentPane;
    }

    /**
     * handles sequence for workbench size, menu bar, tool bar and perspective
     * initialisation
     */
    private void handleInitialisationSequence() {
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		setBounds(inset, inset, screenSize.width - inset * 2,
			screenSize.height - inset * 2);
		// start perspective Observer worker thread
		// TODO create status daemon which observes thread component on
		// failure and restarts if needed!!
		((SwingPerspectiveCoordinator) perspectiveObserver).start();
		// init size
		initWorkbenchSize();
		// init menu instance#
		log("3.1: workbench menu");
		initMenuBar();
		// init toolbar instance
		log("3.2: workbench tool bars");
		initToolBars();
		// handle perspectives
		log("3.3: workbench init perspectives");
		initComponents(null);
		// handle workspce bar entries
		log("3.4: workbench handle bar entries");
		handleBarEntries(layout.getToolBars());
		// handle default and defined workspace menu entries
		log("3.5: workbench init menu");
		initWorkbenchMenu();

	    }
	});
    }

    /**
     * set basic layout manager for workspace
     * 
     * @param contentPane
     */
    private void setBasicLayout(final Container contentPane) {
	// set layout manager
	contentPane.setLayout(layout.getLayoutManager() != null ? layout
		.getLayoutManager() : new BorderLayout());
    }

    @Override
    public void handleInitialLayout(final IAction<ActionEvent, Object> action,
	    final IWorkbenchLayout<LayoutManager2> layout) {
	handleInitialLayout((SwingAction) action, (SwingWorkbenchLayout) layout);

    }

    /**
     * define basic workbench layout and define in layout object
     * 
     * @param action
     * @param layout
     */
    public abstract void handleInitialLayout(SwingAction action,
	    SwingWorkbenchLayout layout);

    @Override
    public void registerComponent(
	    final IPerspective<ActionListener, ActionEvent, Object> component,
	    final IPerspectiveCoordinator<ActionListener, ActionEvent, Object> handler) {
	component.init();
	handler.addPerspective(component);

    }

    @Override
    public void unregisterComponent(
	    final IPerspective<ActionListener, ActionEvent, Object> component,
	    final IPerspectiveCoordinator<ActionListener, ActionEvent, Object> handler) {
	handler.removePerspective(component);

    }

    @Override
    public void initComponent(final IAction<ActionEvent, Object> action,
	    final IPerspective<ActionListener, ActionEvent, Object> perspective) {
	final IPerspectiveLayout<? extends Container, Container> perspectiveLayout = ((ASwingPerspective) perspective)
		.getIPerspectiveLayout();
	log("3.4.3: perspective handle init");
	handlePerspectiveInitMethod(action, perspective);
	log("3.4.4: perspective init subcomponents");
	perspective.initComponents(action);
	log("3.4.5: perspective init bar entries");
	addPerspectiveBarEntries(perspective);

	switch (layout.getWorkspaceMode()) {
	case SINGLE_PANE:
	    log("3.4.6: perspective init SINGLE_PANE");
	    initPerspectiveInStackMode(perspectiveLayout);
	    break;
	case TABBED_PANE:
	    log("3.4.6: perspective init TABBED_PANE");
	    initPerspectivesInTabbedMode(perspectiveLayout,
		    perspective.getName());
	    break;
	default:
	    log("3.4.6: perspective init WINDOW_PANE");
	    initPerspectiveInWindowMode(perspectiveLayout,
		    perspective.getName());
	}
    }

    @Override
    public void handleAndReplaceComponent(final IAction<ActionEvent, Object> action,
	    final IPerspective<ActionListener, ActionEvent, Object> perspective) {
	final IPerspectiveLayout<Container, Container> perspectiveLayout = ((ASwingPerspective) perspective)
		.getIPerspectiveLayout();
	// backup old component
	final Container componentOld = perspectiveLayout.getRootComponent();
	perspective.handlePerspective(action);
	final Container componentNew = getLayoutComponentFromPerspectiveLayout(
		perspectiveLayout, componentOld.getPreferredSize());
	final Container parent = getParentAndReplace(componentOld, componentNew);
	// set already active editors to new component
	reassignSubcomponents(perspectiveLayout, perspective);
	invalidateHost(parent);
    }

    /**
     * disable all components in workspace; for use in
     * initPerspectiveInSingleMode
     */
    @Override
    public void disableComponents() {
	// do not disable tool bar entries!!
	final Collection<Container> toolBars = this.layout.getToolBars()
		.values();
	for (final Component comp : getContentPane().getComponents()) {
	    if (!toolBars.contains(comp)) {
		comp.setVisible(false);
	    }
	}

    }

    /**
     * enable all components in workspace; for use in
     * initPerspectiveInWindowMode
     */
    @Override
    public void enableComponents() {
	for (final Component comp : getContentPane().getComponents()) {
	    comp.setVisible(true);
	}
    }

    @Override
    public void initWorkbenchMenu() {
	final JMenuBar menuBar = getJMenuBar();
	menuBar.add(getDefaultMenu());
	final JMenu mymenu = this.handleMenuEntries(getDefaultMenu());
	if (mymenu != null) {
	    menuBar.add(mymenu);
	}
	addDefaultMenuEntries();
    }

    @Override
    public Container handleMenuEntries(final Container menue) {
	if (menue instanceof JMenu) {
	    return this.handleMenuEntries((JMenu) menue);
	}
	return null;
    }

    public abstract JMenu handleMenuEntries(JMenu menuBar);

    @Override
    public void initMenuBar() {
	setJMenuBar(new JMenuBar());
    }

    private void initToolBars() {
	final Iterator<Entry<Layout, Container>> it = layout.getToolBars()
		.entrySet().iterator();
	while (it.hasNext()) {
	    Entry<Layout, Container> entry = it.next();
	    getContentPane().add(entry.getValue(), entry.getKey().getLayout());
	}

    }

    /**
     * creates basic menu entry for perspective
     * 
     * @param perspective
     */
    private void createPerspectiveMenue(
	    final IPerspective<ActionListener, ActionEvent, Object> perspective) {
	if (perspective instanceof ASwingPerspective) {
	    final JMenu tmp = new JMenu(perspective.getName());
	    ((ASwingPerspective) perspective).handleMenuEntries(tmp);
	    if (getJMenuBar() != null
		    && tmp.getPopupMenu().getComponents().length > 0) {
		getJMenuBar().add(tmp);
	    }
	}
    }

    /**
     * handles initialization of custom tool/bottom- bar entries
     * 
     * @param perspective
     */
    private void addPerspectiveBarEntries(
	    final IPerspective<ActionListener, ActionEvent, Object> perspective) {
	if (perspective instanceof ASwingPerspective) {
	    ((ASwingPerspective) perspective).handleBarEntries(this.layout
		    .getToolBars());
	}
    }

    @Override
    public JMenu getDefaultMenu() {
	if (menu == null) {
	    menu = new JMenu("Options");
	}
	return menu;
    }

    @SuppressWarnings("deprecation")
    private void addDefaultMenuEntries() {

	final boolean isMacOS = System.getProperty("mrj.version") != null;
	if (isMacOS) {
	    final MacOSXController macController = new MacOSXController();
	    MRJApplicationUtils.registerAboutHandler(macController);
	    MRJApplicationUtils.registerPrefsHandler(macController);
	    MRJApplicationUtils.registerQuitHandler(macController);
	}

    }

    /**
     * set perspectives to workbench
     * 
     * @param perspectives
     */
    @Override
    public void setPerspectives(
	    final List<IPerspective<ActionListener, ActionEvent, Object>> perspectives) {
	this.perspectives = perspectives;
    }

    /**
     * get perspectives in workbench
     * 
     * @return
     */
    @Override
    public List<IPerspective<ActionListener, ActionEvent, Object>> getPerspectives() {
	return perspectives;
    }

    @Override
    public IWorkbenchLayout<LayoutManager2> getWorkbenchLayout() {
	return layout;
    }

    /**
     * perspectives are a set in workbench. Each perspective contains a Layout
     * object (container like splitPane), a view container and a
     * editorContainer. The initPerspective method should stick these three
     * parts together so they can be add to the workbench
     */
    @Override
    public void initComponents(final IAction<ActionEvent, Object> action) {
	for (final IPerspective<ActionListener, ActionEvent, Object> perspective : getPerspectives()) {
	    log("3.4.1: register component: " + perspective.getName());
	    registerComponent(perspective, perspectiveObserver);
	    // TODO what if component removed an initialized later again?
	    log("3.4.2: create perspective menu");
	    createPerspectiveMenue(perspective);
	    if (perspective.isActive()) {
		SwingUtilities.invokeLater(new Runnable() {
		    @Override
		    public void run() {
			initComponent( new SwingAction(
				perspective.getId(), perspective.getId(),
				"init"),perspective);

		    }
		}); // SWING UTILS END
	    }

	}
    }

    /**
     * add all active subcomponents to replaced perspective
     * 
     * @param layout
     * @param perspective
     */
    private void reassignSubcomponents(
	    final IPerspectiveLayout<? extends Container, Container> layout,
	    final IPerspective<ActionListener, ActionEvent, Object> perspective) {
	for (final ISubComponent<ActionListener, ActionEvent, Object> editor : perspective
		.getSubcomponents()) {
	    if (editor instanceof ASwingComponent) {
		final Container editorComponent = ((ASwingComponent) editor)
			.getRoot();
		if (editorComponent != null) {
		    editorComponent.setVisible(true);
		    editorComponent.setEnabled(true);
		    addComponentByType(layout, ((ASwingComponent) editor));
		}
	    }
	}
    }

    /**
     * takes the root component from old container, removes old container and
     * add the new one
     * 
     * @param oldComp
     * @param newComp
     * @return
     */
    private Container getParentAndReplace(final Container oldComp,
	    final Container newComp) {
	final Container parent = oldComp.getParent();
	parent.remove(oldComp);
	parent.add(newComp);
	parent.setVisible(true);
	return parent;
    }

    /**
     * initialize perspective in window mode; creates an internal frame and add
     * perspective
     * 
     * @param layout
     * @param name
     */
    private void initPerspectiveInWindowMode(
	    final IPerspectiveLayout<? extends Container, Container> layout,
	    final String name) {
	enableComponents();
	final JInternalFrame internalFrame = getActiveInternalFrame(name);
	final JPanel frame = getActivePanel(getPanelDimension(this.layout));
	final Container comp = getLayoutComponentFromPerspectiveLayout(layout,
		new Dimension(this.layout.getWorkbenchSize().getX() - 30,
			this.layout.getWorkbenchSize().getY() - 20));
	frame.add(comp);
	internalFrame.setContentPane(frame);
	getContentPane().add(internalFrame);
	invalidateHost(this);
    }

    /**
     * initialize perspective in tabbed mode; creates an tab an add perspective
     * 
     * @param layout
     */
    private void initPerspectivesInTabbedMode(
	    final IPerspectiveLayout<? extends Container, Container> layout,
	    final String name) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * initialize perspective in stacked mode; creates an panel an add
     * perspective
     * 
     * @param layout
     */
    private void initPerspectiveInStackMode(
	    final IPerspectiveLayout<? extends Container, Container> layout) {
	disableComponents();
	final JComponent wrapper = new JPanel(true);
	final Container comp = layout.getRootComponent();
	comp.setVisible(true);
	getContentPane().add(wrapper.add(layout.getRootComponent()));
	invalidateHost(getContentPane());
    }

    /**
     * returns panel size in window mode
     * 
     * @param layout
     * @return
     */
    private Dimension getPanelDimension(
	    final IWorkbenchLayout<LayoutManager2> layout) {
	return new Dimension(layout.getWorkbenchSize().getX() - 15, layout
		.getWorkbenchSize().getY() - 15);
    }

    /**
     * handles initialization with correct action; perspective can be
     * initialized at application start or when called by an other component
     * 
     * @param action
     * @param perspectiveLayout
     * @param perspective
     */
    private void handlePerspectiveInitMethod(
	    final IAction<ActionEvent, Object> action,
	    final IPerspective<ActionListener, ActionEvent, Object> perspective) {
	if (getTargetPerspectiveId(action.getTargetId()).equals(
		perspective.getId())) {
	    log("3.4.3.1: perspective handle with custom action");
	    perspective.handlePerspective(action);
	} else {
	    log("3.4.3.1: perspective handle with default >>init<< action");
	    perspective.handlePerspective(new SwingAction(perspective.getId(),
		    perspective.getId(), "init"));
	}
    }

    /**
     * find valid target and add type specific new ui component
     * 
     * @param layout
     * @param editor
     */
    private void addComponentByType(
	    final IPerspectiveLayout<? extends Container, Container> layout,
	    final IVComponent<Container, ActionListener, ActionEvent, Object> editor) {
	final Container validContainer = layout.getTargetLayoutComponents()
		.get(editor.getExecutionTarget());
	if (validContainer instanceof JScrollPane) {
	    ((JScrollPane) validContainer).setViewportView(editor.getRoot());
	} else {
	    validContainer.add(editor.getName(), editor.getRoot());
	}
	validContainer.setEnabled(true);
	invalidateHost(validContainer);

    }

    /**
     * returns active internal frame for swing window mode
     * 
     * @param name
     * @return
     */
    private JInternalFrame getActiveInternalFrame(final String name) {
	final JInternalFrame internalFrame = new JInternalFrame(name, true,
		true, true, true);
	try {
	    internalFrame.setSelected(true);
	} catch (final PropertyVetoException ex) {
	    Logger.getLogger(ASwingWorkbench.class.getName()).log(Level.SEVERE,
		    null, ex);
	}
	internalFrame.setSize(layout.getWorkbenchSize().getX() - 10, layout
		.getWorkbenchSize().getY() - 10);
	internalFrame.setVisible(true);
	return internalFrame;
    }

    /**
     * create an active JPanel
     * 
     * @param dimension
     * @return
     */
    private JPanel getActivePanel(final Dimension dimension) {
	final JPanel frame = new JPanel();
	frame.setSize(dimension);
	return frame;
    }

    /**
     * get perspectives ui root container
     * 
     * @param layout
     * @param dimension
     * @return
     */
    private Container getLayoutComponentFromPerspectiveLayout(
	    final IPerspectiveLayout<? extends Container, Container> layout,
	    final Dimension dimension) {
	final Container comp = layout.getRootComponent();
	comp.setVisible(true);
	comp.setPreferredSize(dimension);
	return comp;
    }

    /**
     * invalidate swing host after changes
     * 
     * @param host
     */
    private void invalidateHost(final Container host) {
	if (host instanceof JComponent) {
	    ((JComponent) host).revalidate();
	} else {
	    host.invalidate();
	}
	host.repaint();
    }

    /**
     * set initial workbench size
     */
    private void initWorkbenchSize() {
	this.setSize(layout.getWorkbenchSize().getX(), layout
		.getWorkbenchSize().getY());
    }

    /**
     * returns the message target id
     * 
     * @param messageId
     * @return
     */
    private String getTargetPerspectiveId(final String messageId) {
	final String[] targetId = getTargetId(messageId);
	if (checkValidComponentId(targetId)) {
	    return targetId[0];
	}
	return messageId;
    }

    private String[] getTargetId(final String messageId) {
	return messageId.split("\\.");
    }

    private boolean checkValidComponentId(final String[] targetId) {
	if (targetId != null && targetId.length == 2) {
	    return true;
	}

	return false;
    }

    private void log(final String message) {
	if (logger.isLoggable(Level.FINE)) {
	    logger.fine(">> " + message);
	}
    }

}
