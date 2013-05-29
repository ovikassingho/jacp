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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
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
    //private static Comparator<String> COMPARE_IGNORE = (String s1, String s2) -> s1.compareToIgnoreCase(s2);

    /**
     * contains constant values
     *
     * @author Andy Moncsek
     */
    public static class MessageUtil {
        public static final String INIT = "init";
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

    public static void setPrivateMemberValue(final Class<?> superClass,
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    /**
     * find annotated method in component and pass value
     * @param annotation
     * @param component
     * @param value
     */
    public static void invokeHandleMethodsByAnnotation(
            final Class annotation, final Object component,
            final Object... value) {
        final Class<?> componentClass = component.getClass();
        final Method[] methods = componentClass.getMethods();
        for (final Method m : methods) {
            if (m.isAnnotationPresent(annotation)) {
                try {
                    final Class<?>[] types = m.getParameterTypes();
                    if (types.length == value.length) {
                        m.invoke(component, value);
                        return;
                    }
                    if (types.length > 0) {
                        m.invoke(component, getValidParameterList(types, value));
                        return;
                    }

                    m.invoke(component);
                    return;

                } catch (final IllegalArgumentException e) {
                    throw new UnsupportedOperationException(
                            "use @PostConstruct and @OnTeardown either with paramter extending IBaseLayout<Node> layout (like FXComponentLayout) or with no arguments  ",
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

    private static Object[] getValidParameterList(final Class<?>[] types,
                                                  Object... value) {
        final Object[] found = new Object[types.length];
        int i = 0;
        for (final Class<?> t : types) {
            final Object result = findByClass(t, value);
            if (result != null) {
                found[i] = result;
                i++;
            }
        }

        return found;
    }

    private static Object findByClass(Class<?> key, Object[] values) {
        if (key == null)
            return null;
        for (Object val : values) {
            if (val == null)
                return null;
            final Class<?> clazz = val.getClass();
            if (clazz == null)
                return null;
            if (clazz.getGenericSuperclass().equals(key) || clazz.equals(key))
                return val;
        }
        return null;
    }

    public static Locale getCorrectLocale(final String localeID) {
        Locale locale = Locale.getDefault();
        if (localeID != null && localeID.length() > 1) {
            if (localeID.contains("_")) {
                String[] loc = localeID.split("_");
                locale = new Locale(loc[0], loc[1]);
            } else {
                locale = new Locale(localeID);
            }

        }
        return locale;
    }

    /**
     * Returns the resourceBundle
     *
     * @param resourceBundleLocation
     * @param localeID
     * @return
     */
    public static ResourceBundle getBundle(String resourceBundleLocation,
                                           final String localeID) {
        if (resourceBundleLocation == null
                || resourceBundleLocation.length() <= 1)
            return null;
        return ResourceBundle.getBundle(resourceBundleLocation,
                FXUtil.getCorrectLocale(localeID));
    }

    /**
     * Loads the FXML document provided by viewLocation-
     *
     * @param bean   the controller
     * @param bundle the ressource bundle
     * @param url    the fxml url
     * @return The components root Node.
     */
    // TODO merge with loadFXMLandSetController in FXComponentInit
    public static <T> Node loadFXMLandSetController(final T bean,
                                                    final ResourceBundle bundle, final URL url) {
        final FXMLLoader fxmlLoader = new FXMLLoader();
        if (bundle != null) {
            fxmlLoader.setResources(bundle);
        }
        fxmlLoader.setLocation(url);
        fxmlLoader.setController(bean);
        try {
            return (Node) fxmlLoader.load();
        } catch (IOException e) {
            throw new MissingResourceException(
                    "fxml file not found --  place in resource folder and reference like this: viewLocation = \"/myUIFile.fxml\"",
                    url.getPath(), "");
        }
    }

    /**
     * returns the message (parent) target id
     *
     * @param messageId
     * @return
     */
    public static String getTargetParentId(final String messageId) {
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
    private static boolean isFullValidId(final String[] targetId) {
        return targetId != null && targetId.length == 2;

    }

    /**
     * returns the message target perspective id
     *
     * @param messageId
     * @return
     */
    public static String getTargetPerspectiveId(final String messageId) {
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
    public static String getTargetComponentId(final String messageId) {
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
    public static boolean isLocalMessage(final String messageId) {
        return !messageId.contains(".");
    }

    /**
     * returns target message with perspective and component name as array
     *
     * @param messageId
     * @return
     */
    protected static String[] getTargetId(final String messageId) {
        return messageId.split("\\.");
    }

    /**
     * Returns a component by id from a provided component list
     *
     * @param id
     * @param components
     * @param <P>
     * @return
     */
    public static <P extends IComponent<EventHandler<Event>, Event, Object>> P getObserveableById(
            final String id, final List<P> components) {
        final Optional<P> filter = components.parallelStream().filter(c -> c.getId().equals(id)).findFirst();
        if (filter.isPresent()) return filter.get();
        return null;
    }


    /**
     * find the parent perspective to id; should be only used when no
     * responsible component was found , TODO migrate to Java 8
     *
     * @param id
     * @param perspectives
     * @return
     */
    public static IPerspective<EventHandler<Event>, Event, Object> findRootByObserveableId(
            final String id,
            final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives) {
        final Optional<IPerspective<EventHandler<Event>, Event, Object>> result = perspectives.
                parallelStream().
                filter(perspective ->
                        perspective.getSubcomponents().
                                parallelStream().map(ISubComponent::getId).
                                anyMatch(cId -> cId.equals(id))).findFirst();
        if (result.isPresent()) return result.get();
        return null;
    }

    /**
     * returns cloned action with valid message TODO add to interface
     *
     * @param action
     * @param message
     * @return
     */
    public static IAction<Event, Object> getValidAction(
            final IAction<Event, Object> action, final String target,
            final Object message) {
        final IAction<Event, Object> actionClone = action.clone();
        actionClone.addMessage(target, message);
        return actionClone;
    }

}
