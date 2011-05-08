/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jacp.swing.rcp.handler;

import javax.swing.JOptionPane;

import com.apple.mrj.MRJAboutHandler;
import com.apple.mrj.MRJPrefsHandler;
import com.apple.mrj.MRJQuitHandler;

/**
 * 
 * @author amo
 */
public class MacOSXController implements MRJAboutHandler, MRJQuitHandler,
	MRJPrefsHandler {

    @Override
    public void handleAbout() {
	JOptionPane.showMessageDialog(null, "about", "about",
		JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void handlePrefs() throws IllegalStateException {
	JOptionPane.showMessageDialog(null, "prefs", "prefs",
		JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void handleQuit() throws IllegalStateException {
	JOptionPane.showMessageDialog(null, "quit", "quit",
		JOptionPane.INFORMATION_MESSAGE);
	// handle exit here
	System.exit(0);
    }

}