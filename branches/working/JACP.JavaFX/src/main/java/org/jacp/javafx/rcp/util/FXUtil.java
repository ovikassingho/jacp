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
package org.jacp.javafx.rcp.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.IPerspective;
import org.jacp.api.component.ISubComponent;

/**
 * Util class with helper methods
 * 
 * @author Andy Moncsek
 * 
 */
public class FXUtil {

	public static final String AFXCOMPONENT_ROOT = "root";
	public static final String ACOMPONENT_ACTIVE = "active";
	public static final String ACOMPONENT_ID = "id";
	public static final String ACOMPONENT_NAME = "name";
	public static final String ACOMPONENT_EXTARGET = "executionTarget";
	public static final String ACOMPONENT_BLOCKED = "blocked";
	public static final String ACOMPONENT_STARTED = "started";
	public static final String APERSPECTIVE_MQUEUE = "messageQueue";
	public static final String IDECLARATIVECOMPONENT_VIEW_LOCATION = "viewLocation";
	public static final String IDECLARATIVECOMPONENT_TYPE = "type";
	public static final String IDECLARATIVECOMPONENT_DOCUMENT_URL = "documentURL";
	public static final String IDECLARATIVECOMPONENT_LOCALE = "localeID";
	public static final String IDECLARATIVECOMPONENT_BUNDLE_LOCATION = "resourceBundleLocation";
	public static final String AFXPERSPECTIVE_PERSPECTIVE_LAYOUT = "perspectiveLayout";

	/**
	 * contains constant values
	 * 
	 * @author Andy Moncsek
	 * 
	 */
	public static class MessageUtil {
		public static String INIT = "init";
	}

	/**
	 * returns children of current node
	 * 
	 * @param node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static ObservableList<Node> getChildren(final Node node) {
		if (node instanceof Parent) {
			final Parent tmp = (Parent) node;
			Method protectedChildrenMethod;
			ObservableList<Node> returnValue = null;
			try {
				protectedChildrenMethod = Parent.class
						.getDeclaredMethod("getChildren");
				protectedChildrenMethod.setAccessible(true);

				returnValue = (ObservableList<Node>) protectedChildrenMethod
						.invoke(tmp);

			} catch (final NoSuchMethodException ex) {
				Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE,
						null, ex);
			} catch (final SecurityException ex) {
				Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE,
						null, ex);
			} catch (final IllegalAccessException ex) {
				Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE,
						null, ex);
			} catch (final IllegalArgumentException ex) {
				Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE,
						null, ex);
			} catch (final InvocationTargetException ex) {
				Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE,
						null, ex);
			}

			return returnValue;
		}

		return null;

	}

	public final static void setPrivateMemberValue(final Class<?> superClass,
			final Object object, final String member, final Object value) {
		try {
			final Field privateStringField = superClass
					.getDeclaredField(member);
			privateStringField.setAccessible(true);
			privateStringField.set(object, value);

		} catch (final SecurityException e) {
			Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE, null, e);
		} catch (final NoSuchFieldException e) {
			Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE, null, e);
		} catch (final IllegalArgumentException e) {
			Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE, null, e);
		} catch (final IllegalAccessException e) {
			Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * find annotated method in component and pass value 
	 * @param annotation
	 * @param component
	 * @param value
	 */
	public final static void invokeHandleMethodsByAnnotation(
			final Class annotation, final Object component, final Object ...value) {
		final Class<?> componentClass = component.getClass();
		final Method[] methods = componentClass.getMethods();
		for (final Method m : methods) {
			if (m.isAnnotationPresent(annotation)) {
				try {
					final Class<?>[] types = m.getParameterTypes();
					if(types.length==value.length){
						m.invoke(component, value);
						return;
					}					
					if(types.length>0){
						m.invoke(component, getValidParameterList(types, value));
						return;
					}				

					m.invoke(component);
					return;

				} catch (final IllegalArgumentException e) {
					throw new UnsupportedOperationException(
							"use @OnStart and @OnTeardown either with paramter extending IBaseLayout<Node> layout (like FXComponentLayout) or with no arguments  ",
							e.getCause());
				} catch (final IllegalAccessException e) {
					Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE,
							null, e);
				} catch (final InvocationTargetException e) {
					Logger.getLogger(FXUtil.class.getName()).log(Level.SEVERE,
							null, e);
				}
				break;
			}
		}
	}
	
	private static Object[] getValidParameterList(final Class<?>[] types, Object ...value) {
		final Object[] found=new Object[types.length];
		int i=0;
		for (final Class<?> t : types) {
			final Object result =findByClass(t, value);
			if (result!=null) {
				found[i]=result;
				i++;
			}
		}
		
		return found;
	}
	
	private static Object findByClass(Class<?> key, Object[] values) {
		for(Object val:values) {
			if(val!=null && val.getClass().getGenericSuperclass().equals(key) || val.getClass().equals(key)) return val;
		}
		return null;
	}

	/**
	 * returns the message (parent) target id
	 * 
	 * @param messageId
	 * @return
	 */
	public final static String getTargetParentId(final String messageId) {
		final String[] parentId = FXUtil.getTargetId(messageId);
		if (FXUtil.isFullValidId(parentId)) {
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
	private final static boolean isFullValidId(final String[] targetId) {
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
	public final static String getTargetPerspectiveId(final String messageId) {
		final String[] targetId = FXUtil.getTargetId(messageId);
		if (!FXUtil.isLocalMessage(messageId)) {
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
	public final static String getTargetComponentId(final String messageId) {
		final String[] targetId = FXUtil.getTargetId(messageId);
		if (!FXUtil.isLocalMessage(messageId)) {
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

	public static final <P extends IComponent<EventHandler<Event>, Event, Object>> P getObserveableById(
			final String id, final List<P> components) {
		synchronized (components) {
			for (int i = 0; i < components.size(); i++) {
				final P p = components.get(i);
				if (p.getId().equals(id)) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * find the parent perspective to id; should be only used when no
	 * responsible component was found
	 * 
	 * @param id
	 * @param perspectives
	 * @return
	 */
	public static final IPerspective<EventHandler<Event>, Event, Object> findRootByObserveableId(
			final String id,
			final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives) {
		synchronized (perspectives) {
			for (int i = 0; i < perspectives.size(); i++) {
				final IPerspective<EventHandler<Event>, Event, Object> p = perspectives
						.get(i);
				final List<ISubComponent<EventHandler<Event>, Event, Object>> subComponents = p
						.getSubcomponents();
				for (int j = 0; j < subComponents.size(); j++) {
					final ISubComponent<EventHandler<Event>, Event, Object> component = subComponents
							.get(j);
					if (component.getId().equals(id)) {
						return p;
					}
				}

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
