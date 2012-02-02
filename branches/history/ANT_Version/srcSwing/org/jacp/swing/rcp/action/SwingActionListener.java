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
package org.jacp.swing.rcp.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.coordinator.ICoordinator;

/**
 * 
 * @author amo
 */
public class SwingActionListener implements ActionListener,
	IActionListener<ActionListener, ActionEvent, Object> {

    private IAction<ActionEvent, Object> action;
    private final ICoordinator<ActionListener, ActionEvent, Object> observer;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public SwingActionListener(final IAction<ActionEvent, Object> action,
	    final ICoordinator<ActionListener, ActionEvent, Object> observer) {
	this.action = action;
	this.observer = observer;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	action.setActionEvent(e);
	log(" //1// message send from / to: " + action.getSourceId() + " / "
		+ action.getTargetId());
	notifyComponents(action);

    }

    @Override
    public void notifyComponents(final IAction<ActionEvent, Object> action) {
	observer.handle(action);
    }

    @Override
    public void setAction(final IAction<ActionEvent, Object> action) {
	this.action = action;
    }

    @Override
    public IAction<ActionEvent, Object> getAction() {
	return action;
    }

    @Override
    public ActionListener getListener() {
	return this;
    }

    private void log(final String message) {
	if (logger.isLoggable(Level.FINE)) {
	    logger.fine(">> " + message);
	}
    }
}
