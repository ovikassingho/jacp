package org.jacp.swing.rcp.util;


import java.awt.Color;
import java.awt.FlowLayout;

import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.jgoodies.forms.factories.Borders;

public class OSXToolBar extends JPanel {
 

    /**
     * 
     */
    private static final long serialVersionUID = 7030069330633050293L;
    public static final Color OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR =
            new Color(64, 64, 64);
    public static final Color OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR =
            new Color(135, 135, 135);    

    public OSXToolBar() {
        // make the component transparent
        setOpaque(false);
        // create an empty border around the panel
        // note the border below is created using JGoodies Forms
        setBorder(Borders.createEmptyBorder("3dlu, 3dlu, 1dlu, 3dlu"));
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 5,5));
    }

    @Override
    public Border getBorder() {
	    Window  window = SwingUtilities.getWindowAncestor(this);
        return window != null && window.isFocused()
                ? BorderFactory.createMatteBorder(0,0,1,0,
                        OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR)
                : BorderFactory.createMatteBorder(0,0,1,0,
                       OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR);
    }
}
