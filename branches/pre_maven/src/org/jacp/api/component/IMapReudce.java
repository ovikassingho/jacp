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
package org.jacp.api.component;

import org.jacp.api.action.IAction;

/**
 * defines the interface for a map/reduce instance
 * 
 * @author Andy Moncsek
 * 
 * @param <L>
 * @param <A>
 * @param <M>
 */
public interface IMapReudce<L, A, M> extends IComponent<L, A, M> {
    /**
     * the reduce method
     */
    public abstract <C> C reduce(final IAction<A, M> action);
    /**
     * the handle method
     * @param <C>
     * @param action
     * @return
     */
    public abstract <C> C map(final IAction<A, M> action);
}
