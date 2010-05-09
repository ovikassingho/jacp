/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ahcp.swing.rcp.workbench;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.ahcp.api.action.IAction;
import org.ahcp.api.base.IEditor;
import org.ahcp.api.base.IPerspective;
import org.ahcp.api.base.IRootComponent;
import org.ahcp.api.base.IWorkbench;
import org.ahcp.api.componentLayout.IPerspectiveLayout;
import org.ahcp.api.componentLayout.IWorkbenchLayout;
import org.ahcp.api.observers.IPerspectiveObserver;
import org.ahcp.swing.rcp.action.SwingAction;
import org.ahcp.swing.rcp.componentLayout.SwingWorkbenchLayout;
import org.ahcp.swing.rcp.handler.MacOSXController;
import org.ahcp.swing.rcp.observers.SwingPerspectiveObserver;

import com.apple.mrj.MRJApplicationUtils;

/**
 * 
 * @author amo
 */
public abstract class ASwingWorkbench extends JFrame
		implements
		IWorkbench<Container, LayoutManager2, ActionListener, ActionEvent, Object>,
		IRootComponent<IPerspective<Container, ActionListener, ActionEvent, Object>, IPerspectiveObserver<Container, ActionListener, ActionEvent, Object>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1740398352308498810L;
	private JMenu menu;
	private Container toolbar;
	private Container bottombar;
	private List<IPerspective<Container, ActionListener, ActionEvent, Object>> perspectives;
	private final IPerspectiveObserver<Container, ActionListener, ActionEvent, Object> perspectiveObserver = new SwingPerspectiveObserver(
			this);
	private final Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();
	private final int inset = 50;
	private final IWorkbenchLayout<LayoutManager2> layout = new SwingWorkbenchLayout();

	public ASwingWorkbench(final String name) {
		super(name);

	}

	@Override
	public Container init() {
		// init user defined worspace
		this.handleInitialLayout(new SwingAction("TODO", "init"), layout);
		// define basic content pane
		switch (layout.getWorkspaceMode()) {
		case WINDOWED_PAIN:
			final JDesktopPane desktop = new JDesktopPane();
			setContentPane(desktop);
			break;
		case TABBED_PAIN:
			final JTabbedPane desktopTabs = new JTabbedPane();
			setContentPane(desktopTabs);
			break;
		default:
			;
		}
		final Container contentPane = getContentPane();
		// set layout manager
		contentPane.setLayout(layout.getLayoutManager() != null ? layout
				.getLayoutManager() : new BorderLayout());
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setVisible(true);

				setBounds(inset, inset, screenSize.width - inset * 2,
						screenSize.height - inset * 2);
				initWorkbenchSize();
				// init menue instance
				initMenuBar();
				// init toolbar instance
				initToolBar();
				// init bottom bar instance
				initBottomBar();
				// handle perspectives
				initPerspectives();
				// handle workspce bar entries
				handleBarEntries(getToolBar(), getBottomBar());
				// handle default and defined workspace menu entries
				initWorkbenchMenu();

			}
		});
		return contentPane;
	}

	@Override
	public void handleInitialLayout(final IAction<Object, ActionEvent> action,
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

	/**
	 * perspectives are a set in workbench. Each perspective contains a Layout
	 * object (container like splitPane), a view container and a
	 * editorContainer. The initPerspective method should stick these three
	 * parts together so they can be add to the workbench
	 */
	private void initPerspectives() {
		for (final IPerspective<Container, ActionListener, ActionEvent, Object> perspective : getPerspectives()) {
			registerComponent(perspective, perspectiveObserver);
			// TODO what if component removed an initilaized later again?
			createPerspectiveMenue(perspective);
			if (perspective.isActive()) {
				final Thread worker = new Thread() {
					@Override
					public void run() {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								initPerspective(perspective, new SwingAction(
										perspective.getId(), perspective
												.getId(), "init"));

							}
						}); // SWING UTILS END
					} // run end
				}; // thred END
				worker.start();

			}

		}
	}

	@Override
	public void registerComponent(
			final IPerspective<Container, ActionListener, ActionEvent, Object> component,
			final IPerspectiveObserver<Container, ActionListener, ActionEvent, Object> handler) {
		handler.addPerspective(component);

	}

	@Override
	public void initPerspective(
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective,
			final IAction<Object, ActionEvent> action) {
		// TODO when initPerspective is called explicit by a component message,
		// manage to pass the massage at initilaisation process and activate
		// components if nedded
		final IPerspectiveLayout<? extends Container, Container> perspectiveLayout = perspective
				.getIPerspectiveLayout();

		handlePerspectiveInitMethod(action, perspectiveLayout, perspective);
		perspective.initSubcomponents(action, perspectiveLayout, perspective);
		addPerspectiveBarEntries(perspective);

		switch (layout.getWorkspaceMode()) {
		case SINGLE_PAIN:
			initPerspectiveInStackMode(perspectiveLayout);
			break;
		case TABBED_PAIN:
			initPerspectivesInTabbedMode(perspectiveLayout, perspective
					.getName());
			break;
		default:
			initPerspectiveInWindowMode(perspectiveLayout, perspective
					.getName());
		}
	}

	private void handlePerspectiveInitMethod(
			final IAction<Object, ActionEvent> action,
			final IPerspectiveLayout<? extends Container, Container> perspectiveLayout,
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		if (getTargetPerspectiveId(action.getTargetId()).equals(
				perspective.getId())) {
			perspective.handleInitialLayout(action, perspectiveLayout);
		} else {
			perspective.handleInitialLayout(new SwingAction(
					perspective.getId(), perspective.getId(), "init"),
					perspectiveLayout);
		}
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

	@Override
	public void replacePerspective(
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective,
			final IAction<Object, ActionEvent> action) {
		final IPerspectiveLayout<? extends Container, Container> perspectiveLayout = perspective
				.getIPerspectiveLayout();
		// backup old component
		final Container componentOld = perspectiveLayout
				.getRootLayoutComponent();
		perspective.handleInitialLayout(action, perspectiveLayout);
		final Container componentNew = getLayoutComponentFromPerspectiveLayout(
				perspectiveLayout, componentOld.getPreferredSize());
		final Container parent = getParentAndReplace(componentOld, componentNew);
		// set already active editors to new component
		reassignSubcomponents(perspectiveLayout, perspective);
		invalidateHost(parent);
	}

	/**
	 * add all active subcomponents to replaced perspective
	 * 
	 * @param layout
	 * @param perspective
	 */
	private void reassignSubcomponents(
			final IPerspectiveLayout<? extends Container, Container> layout,
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		for (final IEditor<Container, ActionListener, ActionEvent, Object> editor : perspective
				.getEditors()) {
			final Container editorComponent = editor.getRoot();
			if (editorComponent != null) {
				editorComponent.setVisible(true);
				editorComponent.setEnabled(true);
				addComponentByType(layout, editor);
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
		final JPanel frame = getActivePanel(new Dimension(this.layout
				.getWorkbenchSize().getX() - 15, this.layout.getWorkbenchSize()
				.getY() - 15));
		final Container comp = getLayoutComponentFromPerspectiveLayout(layout,
				new Dimension(this.layout.getWorkbenchSize().getX() - 30,
						this.layout.getWorkbenchSize().getY() - 20));
		frame.add(comp);
		internalFrame.setContentPane(frame);
		getContentPane().add(internalFrame);
		invalidateHost(this);
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
	 * get perspectives root container
	 * 
	 * @param layout
	 * @param dimension
	 * @return
	 */
	private Container getLayoutComponentFromPerspectiveLayout(
			final IPerspectiveLayout<? extends Container, Container> layout,
			final Dimension dimension) {
		final Container comp = layout.getRootLayoutComponent();
		comp.setVisible(true);
		comp.setPreferredSize(dimension);
		return comp;
	}

	/**
	 * initialize perspective in tabed mode; creates an tab an add perspective
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
		final Container comp = layout.getRootLayoutComponent();
		comp.setVisible(true);
		getContentPane().add(wrapper.add(layout.getRootLayoutComponent()));
		invalidateHost(getContentPane());
	}

	/**
	 * disable all components in workspace; for use in
	 * initPerspectiveInSingleMode
	 */
	@Override
	public void disableComponents() {
		final Component dummy = new JComponent() {
			private static final long serialVersionUID = 7974987150114729381L;
		};
		for (final Component comp : getContentPane().getComponents()) {
			if (!comp.equals(toolbar != null ? toolbar : dummy)
					& !comp.equals(bottombar != null ? bottombar : dummy)) {
				comp.setVisible(false);
			}
		}

	}

	/**
	 * anable all components in workspace; for use in
	 * initPerspectiveInWindowMode
	 */
	@Override
	public void enableComponents() {
		for (final Component comp : getContentPane().getComponents()) {
			comp.setVisible(true);
		}
	}

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

	private void invalidateHost(final Container host) {
		if (host instanceof JComponent) {
			((JComponent) host).revalidate();
		} else {
			host.invalidate();
		}
		host.repaint();
	}

	/**
	 * find valid target and add type specific new component
	 * 
	 * @param layout
	 * @param editor
	 */
	private void addComponentByType(
			final IPerspectiveLayout<? extends Container, Container> layout,
			final IEditor<Container, ActionListener, ActionEvent, Object> editor) {
		final Container validContainer = layout.getTargetLayoutComponents()
				.get(editor.getTarget());
		if (validContainer instanceof JScrollPane) {
			((JScrollPane) validContainer).setViewportView(editor.getRoot());
		} else {
			validContainer.add(editor.getName(), editor.getRoot());
		}
		validContainer.setEnabled(true);
		invalidateHost(validContainer);

	}

	private void initWorkbenchSize() {
		this.setSize(layout.getWorkbenchSize().getX(), layout
				.getWorkbenchSize().getY());
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

	@Override
	public void initToolBar() {
		if (layout.isToolbarEnabled()) {
			getContentPane().add(getToolBar(),
					layout.getToolBarLayout().getLayout());
		}

	}

	@Override
	public void initBottomBar() {
		if (layout.isBottomBarEnabled()) {
			getContentPane().add(getBottomBar(),
					layout.getBottomBarLayout().getLayout());
		}
	}

	/**
	 * creates basic menu entry for perspective
	 * 
	 * @param perspective
	 */
	private void createPerspectiveMenue(
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		final JMenu tmp = new JMenu(perspective.getName());
		perspective.addMenuEntries(tmp);
		if (getJMenuBar() != null
				&& tmp.getPopupMenu().getComponents().length > 0) {
			getJMenuBar().add(tmp);
		}
	}

	/**
	 * handles initialization of custom tool/bottom- bar entries
	 * 
	 * @param perspective
	 */
	private void addPerspectiveBarEntries(
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		perspective.handleBarEntries(getToolBar(), getBottomBar());
	}

	@Override
	public JMenu getDefaultMenu() {
		if (menu == null) {
			menu = new JMenu("Options");
		}
		return menu;
	}

	private void addDefaultMenuEntries() {

		final boolean isMacOS = System.getProperty("mrj.version") != null;
		if (isMacOS) {
			final MacOSXController macController = new MacOSXController();
			MRJApplicationUtils.registerAboutHandler(macController);
			MRJApplicationUtils.registerPrefsHandler(macController);
			MRJApplicationUtils.registerQuitHandler(macController);
		}

		/**
		 * JMenuItem quitItem = new JMenuItem("Quit"); quitItem.setAccelerator(
		 * KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET,
		 * (java.awt.event.InputEvent.SHIFT_MASK |
		 * (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
		 * quitItem.addActionListener(new ActionListener() {
		 * 
		 * public void actionPerformed(ActionEvent e) {
		 * System.out.println("Quit"); System.exit(0); } });
		 * 
		 * ((JMenu) getDefaultMenu()).add(quitItem);
		 **/
	}

	@Override
	public Container getToolBar() {
		if (toolbar == null && layout.isToolbarEnabled()) {
			toolbar = new JToolBar();
		}
		return toolbar;
	}

	@Override
	public Container getBottomBar() {
		if (bottombar == null && layout.isBottomBarEnabled()) {
			bottombar = new JToolBar();
		}
		return bottombar;
	}

	/**
	 * set perspectives to workbench
	 * 
	 * @param perspectives
	 */
	@Override
	public void setPerspectives(
			final List<IPerspective<Container, ActionListener, ActionEvent, Object>> perspectives) {
		this.perspectives = perspectives;
	}

	/**
	 * get perspectives in workbench
	 * 
	 * @return
	 */
	@Override
	public List<IPerspective<Container, ActionListener, ActionEvent, Object>> getPerspectives() {
		return perspectives;
	}

	@Override
	public IWorkbenchLayout<LayoutManager2> getWorkbenchLayout() {
		return layout;
	}

}
