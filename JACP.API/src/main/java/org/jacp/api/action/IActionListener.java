/*
 * Copyright (C) 2010,2011.
 * AHCP Project (http://code.google.com/p/jacp/)
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
package org.jacp.api.action;

import java.util.EventObject;

/**
 * handles implementation specific ActionListener
 * 
 * 
 * @param <L>
 *            defines the basic listener type
 * @param <M>
 *            defines the type of message ActionEvent
 * @param <A>
 *            defines the type of ActionEvent
 * @author Andy Moncsek
 */
public interface IActionListener<L, A, M> {

    /**
     * notify component when action fired
     * 
     * @param action
     */
    void notifyComponents(final IAction<A, M> action);

    /**
     * set Action to listener
     * 
     * @param action
     */
    void setAction(final IAction<A, M> action);

    /**
     * returns the action
     * 
     * @return
     */
    IAction<A, M> getAction();

    /**
     * returns implementation specific ActionListener, all listeners must extend java.util.EventListener
     * 
     * @return
     */
    <C extends L> C getListener();
    
    /**
     * abstraction to handle actions uniform on different toolkits
     * @param arg0
     */
    void performAction(EventObject arg0);
}
