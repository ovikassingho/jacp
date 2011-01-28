package org.jacp.swing.rcp.util;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.SystemColor;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class OSXBottomBarPanel extends JPanel {

	/**
     * 
     */
	private static final long serialVersionUID = -200207223959401946L;
	
/*	 private static final Color OS_X_BOTTOM_BAR_ACTIVE_TOP_COLOR = new Color( 0xbbbbbb);
	 private static final Color OS_X_BOTTOM_BAR_ACTIVE_BOTTOM_COLOR	  = new Color( 0x969696); 
	 private static final Color	  OS_X_BOTTOM_BAR_INACTIVE_TOP_COLOR = new Color( 0xe3e3e3);
	 private static	  final Color OS_X_BOTTOM_BAR_INACTIVE_BOTTOM_COLOR = new Color( 0xcfcfcf);
	  private static final Color OS_X_BOTTOM_BAR_BORDER_HIGHLIGHT_COLOR = new	  Color( 0xd8d8d8); 
	  private static final Color	  OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR = new Color( 64, 64, 64);
	  private static final Color OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR =	  new Color( 135, 135, 135);*/
	 

	private static final Color OS_X_BOTTOM_BAR_ACTIVE_TOP_COLOR = UIManager.getColor("SplitPane.shadow");
	private static final Color OS_X_BOTTOM_BAR_ACTIVE_BOTTOM_COLOR = new Color(10,10,10);//UIManager.getColor("SplitPane.shadow");
	
	private static final Color OS_X_BOTTOM_BAR_INACTIVE_TOP_COLOR = UIManager.getColor("SplitPane.shadow");
	private static final Color OS_X_BOTTOM_BAR_INACTIVE_BOTTOM_COLOR = new Color(90,90,90);
	
	
	private static final Color OS_X_BOTTOM_BAR_BORDER_HIGHLIGHT_COLOR = UIManager.getColor("SplitPane.background");
	private static final Color OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR = UIManager.getColor("SplitPane.shadow");
	private static final Color OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR = UIManager.getColor("SplitPane.background");

	public OSXBottomBarPanel() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
				OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR));
	}

	public OSXBottomBarPanel(final int hint) {
		setLayout(new FlowLayout(hint, 5, 5));
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
				OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR));
	}

	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D graphics = (Graphics2D) g.create();

		final Window window = SwingUtilities.getWindowAncestor(this);
		final boolean hasFoucs = window != null && window.isFocused();

		final Color topColor = hasFoucs ? OS_X_BOTTOM_BAR_ACTIVE_TOP_COLOR
				: OS_X_BOTTOM_BAR_INACTIVE_TOP_COLOR;
		final Color bottomColor = hasFoucs ? OS_X_BOTTOM_BAR_ACTIVE_BOTTOM_COLOR
				: OS_X_BOTTOM_BAR_INACTIVE_BOTTOM_COLOR;

		final Paint paint = new GradientPaint(0, 0, topColor, 0, getHeight(),
				bottomColor);

		graphics.setPaint(paint);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		graphics.dispose();
		System.out.println("repaint");
	}
	

	@Override
	public Border getBorder() {
		final Window window = SwingUtilities.getWindowAncestor(this);
		final Border outterBorder = window != null && window.isFocused() ? BorderFactory
				.createMatteBorder(1, 0, 0, 0,
						OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR)
				: BorderFactory.createMatteBorder(1, 0, 0, 0,
						OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR);
		final Border innerBorder = BorderFactory.createMatteBorder(1, 0, 0, 0,
				OS_X_BOTTOM_BAR_BORDER_HIGHLIGHT_COLOR);
		return BorderFactory.createCompoundBorder(outterBorder, innerBorder);
	}

}
