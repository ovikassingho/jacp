package org.jacp.project.JACP.Util.util;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.jgoodies.forms.factories.Borders;

public class OSXToolBar extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 7030069330633050293L;
    private static final Color OS_X_BOTTOM_BAR_ACTIVE_TOP_COLOR = UIManager.getColor("SplitPane.background");
    private static final Color OS_X_BOTTOM_BAR_ACTIVE_BOTTOM_COLOR = UIManager.getColor("SplitPane.shadow");
    
    private static final Color OS_X_BOTTOM_BAR_INACTIVE_TOP_COLOR = UIManager.getColor("SplitPane.shadow");
    private static final Color OS_X_BOTTOM_BAR_INACTIVE_BOTTOM_COLOR = UIManager.getColor("SplitPane.shadow");
    
    
    private static final Color OS_X_BOTTOM_BAR_BORDER_HIGHLIGHT_COLOR = UIManager.getColor("SplitPane.background");
    private static final Color OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR = UIManager.getColor("SplitPane.shadow");
    private static final Color OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR = UIManager.getColor("SplitPane.background");

    public OSXToolBar() {
	// make the component transparent
	setOpaque(false);
	// create an empty border around the panel
	// note the border below is created using JGoodies Forms
	setBorder(Borders.createEmptyBorder("3dlu, 3dlu, 1dlu, 3dlu"));
	setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    }

    public OSXToolBar(final int hint) {
	// make the component transparent
	setOpaque(false);
	// create an empty border around the panel
	// note the border below is created using JGoodies Forms
	setBorder(Borders.createEmptyBorder("3dlu, 3dlu, 1dlu, 3dlu"));
	setLayout(new FlowLayout(hint, 5, 5));
    }
    
   /* @Override
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
    }*/

    @Override
    public Border getBorder() {
	final Window window = SwingUtilities.getWindowAncestor(this);
	return window != null && window.isFocused() ? BorderFactory
		.createMatteBorder(0, 0, 1, 0,
			OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR)
		: BorderFactory.createMatteBorder(0, 0, 1, 0,
			OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR);
    }
}
