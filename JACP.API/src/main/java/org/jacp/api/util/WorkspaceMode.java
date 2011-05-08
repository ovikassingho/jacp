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
package org.jacp.api.util;

/**
 * define behavior of workspace; WINDOWED_PANE : each perspective is set in a
 * single subwindow; SINGLE_PANE: only one perspective at time is visible;
 * TABBED_PANE each perspective in a separate tab
 * 
 * @author Andy Moncsek
 */
public enum WorkspaceMode {
    WINDOWED_PANE, SINGLE_PANE, TABBED_PANE
}
