/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ahcp.api.base;

/**
 * define behaviour of workspace; WINDOWED_PAIN : each perspective is set in a
 * single subwindow; SINGLE_PAIN: only one perspective at time is visible;
 * TABBED_PAIN each perspective in a separate tab
 * 
 * @author Andy Moncsek
 */
public enum WorkspaceMode {
	WINDOWED_PAIN, SINGLE_PAIN, TABBED_PAIN
}
