/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [FX2Util.java]
 * AHCP Project (http://jacp.googlecode.com)
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
 *
 *
 ************************************************************************/
package org.jacp.javafx2.rcp.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * Util class with helper methods
 * 
 * @author Andy Moncsek
 * 
 */
public class FX2Util {
	
	/**
	 * contains constant values
	 * @author Andy Moncsek
	 *
	 */
	public static class MessageUtil{
		public static String INIT ="init";
	}
	

	/**
	 * returns children of current node
	 * 
	 * @param node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ObservableList<Node> getChildren(final Node node) {
		if (node instanceof Parent) {
			final Parent tmp = (Parent) node;
			Method protectedChildrenMethod;
			ObservableList<Node> returnValue = null;
			try {
				protectedChildrenMethod = Parent.class.getDeclaredMethod(
						"getChildren", null);
				protectedChildrenMethod.setAccessible(true);

				returnValue = (ObservableList<Node>) protectedChildrenMethod
						.invoke(tmp, null);

			} catch (final NoSuchMethodException ex) {
				Logger.getLogger(FX2Util.class.getName()).log(Level.SEVERE,
						null, ex);
			} catch (final SecurityException ex) {
				Logger.getLogger(FX2Util.class.getName()).log(Level.SEVERE,
						null, ex);
			} catch (final IllegalAccessException ex) {
				Logger.getLogger(FX2Util.class.getName()).log(Level.SEVERE,
						null, ex);
			} catch (final IllegalArgumentException ex) {
				Logger.getLogger(FX2Util.class.getName()).log(Level.SEVERE,
						null, ex);
			} catch (final InvocationTargetException ex) {
				Logger.getLogger(FX2Util.class.getName()).log(Level.SEVERE,
						null, ex);
			}

			return returnValue;
		}

		return null;

	}
	
	/**
	 * returns the message (parent) target id
	 * 
	 * @param messageId
	 * @return
	 */
	public static String getTargetParentId(final String messageId) {
		final String[] parentId = getTargetId(messageId);
		if (isFullValidId(parentId)) {
			return parentId[0];
		}
		return messageId;
	}
	
	/**
	 * a target id is valid, when it does contain a perspective and a component
	 * id (perspectiveId.componentId)
	 * 
	 * @param targetId
	 * @return
	 */
	private static boolean isFullValidId(final String[] targetId) {
		if (targetId != null && targetId.length == 2) {
			return true;
		}

		return false;
	}
	
	/**
	 * returns the message target perspective id
	 * 
	 * @param messageId
	 * @return
	 */
	public static final String getTargetPerspectiveId(final String messageId) {
		final String[] targetId = getTargetId(messageId);
		if (!isLocalMessage(messageId)) {
			return targetId[0];
		}
		return messageId;
	}
	
	/**
	 * returns the message target component id
	 * 
	 * @param messageId
	 * @return
	 */
	public static final String getTargetComponentId(final String messageId) {
		final String[] targetId = getTargetId(messageId);
		if (!isLocalMessage(messageId)) {
			return targetId[1];
		}
		return messageId;
	}
	
	/**
	 * when id has no separator it is a local message
	 * 
	 * @param messageId
	 * @return
	 */
	public static final boolean isLocalMessage(final String messageId) {
		return !messageId.contains(".");
	}

	/**
	 * returns target message with perspective and component name as array
	 * 
	 * @param messageId
	 * @return
	 */
	protected static final String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}
	
	public static <P extends IComponent<EventHandler<Event>, Event, Object>> P getObserveableById(
			final String id, final List<P> components) {
		for (int i = 0; i < components.size(); i++) {
			final P p = components.get(i);
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * returns cloned action with valid message TODO add to interface
	 * 
	 * @param action
	 * @param message
	 * @return
	 */
	public static final IAction<Event, Object> getValidAction(
			final IAction<Event, Object> action, final String target,
			final Object message) {
		final IAction<Event, Object> actionClone = action.clone();
		actionClone.addMessage(target, message);
		return actionClone;
	}

}
