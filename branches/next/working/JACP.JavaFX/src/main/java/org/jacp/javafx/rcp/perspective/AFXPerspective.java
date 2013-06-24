/************************************************************************
 *
 * Copyright (C) 2010 - 2012
 *
 * [AFX2Perspective.java]
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

package org.jacp.javafx.rcp.perspective;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import org.jacp.api.action.IAction;
import org.jacp.api.action.IDelegateDTO;
import org.jacp.api.annotations.CallbackComponent;
import org.jacp.api.annotations.Component;
import org.jacp.api.annotations.DeclarativeComponent;
import org.jacp.api.component.IPerspectiveView;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.handler.IComponentHandler;
import org.jacp.api.util.UIType;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.component.AComponent;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.component.ASubComponent;
import org.jacp.javafx.rcp.componentLayout.PerspectiveLayout;
import org.jacp.javafx.rcp.coordinator.FXComponentCoordinator;
import org.jacp.javafx.rcp.util.ComponentRegistry;
import org.jacp.javafx.rcp.util.FXUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * represents a basic javafx2 perspective that handles subcomponents,
 * perspectives are not handled in thread so avoid long running tasks in
 * perspectives.
 *
 * @author Andy Moncsek
 */
public abstract class AFXPerspective extends AComponent implements
        IPerspectiveView<Node, EventHandler<Event>, Event, Object>,
        Initializable {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private List<ISubComponent<EventHandler<Event>, Event, Object>> subcomponents;
    private IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> componentHandler;
    private BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue;
    private BlockingQueue<IDelegateDTO<Event, Object>> messageDelegateQueue;
    private IComponentCoordinator<EventHandler<Event>, Event, Object> componentCoordinator;
    private String viewLocation;
    private URL documentURL;
    private ResourceBundle resourceBundle;
    private IPerspectiveLayout<Node, Node> perspectiveLayout;
    private UIType type = UIType.PROGRAMMATIC;
    private String localeID = "";
    private String resourceBundleLocation = "";
    private final Object lock = new Object();

    @Override
    public final void init(
            final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue,
            final BlockingQueue<IDelegateDTO<Event, Object>> messageDelegateQueue,
            final BlockingQueue<IAction<Event, Object>> globalMessageQueue) {
        this.componentDelegateQueue = componentDelegateQueue;
        this.messageDelegateQueue = messageDelegateQueue;
        this.globalMessageQueue = globalMessageQueue;

    }

    @Override
    public final <C> C handle(final IAction<Event, Object> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postInit(
            final IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> componentHandler) {
        // init component handler
        this.componentHandler = componentHandler;
        this.componentCoordinator = new FXComponentCoordinator();
        ((FXComponentCoordinator) this.componentCoordinator).start();
        this.componentCoordinator.setComponentHandler(this.componentHandler);
        this.componentCoordinator
                .setMessageDelegateQueue(this.messageDelegateQueue);
        this.componentCoordinator.setParentId(this.getId());
        if (this.subcomponents != null) this.registerSubcomponents(this.subcomponents);
    }

    /**
     * Handle perspective method to initialize the perspective and the layout.
     *
     * @param action            ; the action triggering the method
     * @param perspectiveLayout ,  the layout handler defining the perspective
     */
    protected abstract void handlePerspective(IAction<Event, Object> action,
                                              final PerspectiveLayout perspectiveLayout);

    @Override
    public void handlePerspective(final IAction<Event, Object> action) {
        this.handlePerspective(action,
                (PerspectiveLayout) this.perspectiveLayout);

    }

    @Override
    public final void registerComponent(
            final ISubComponent<EventHandler<Event>, Event, Object> component) {
        synchronized (lock) {
            this.handleMetaAnnotation(component);
            this.log("register component: " + component.getId());
            component.initEnv(this.getId(),
                    this.componentCoordinator.getMessageQueue());
            ComponentRegistry.registerComponent(component);
            if (!this.subcomponents.contains(component)) {
                this.subcomponents.add(component);
            }
        }

    }

    /**
     * Set meta attributes defined in annotations.
     *
     * @param component ; the component containing metadata.
     */
    private void handleMetaAnnotation(final ISubComponent<EventHandler<Event>, Event, Object> component) {
        final Component componentAnnotation = component.getClass().getAnnotation(Component.class);
        if (componentAnnotation != null && component instanceof AFXComponent) {
            handleComponentAnnotation(component, componentAnnotation);
            this.log("register component with annotations : " + componentAnnotation.id());
            return;
        }
        final CallbackComponent callbackAnnotation = component.getClass().getAnnotation(CallbackComponent.class);
        if (callbackAnnotation != null) {
            handleCallbackAnnotation(component, callbackAnnotation);
            this.log("register CallbackComponent with annotations : " + callbackAnnotation.id());
            return;
        }
        final DeclarativeComponent declarativeComponent = component.getClass()
                .getAnnotation(DeclarativeComponent.class);
        if (declarativeComponent != null && component instanceof AFXComponent) {
            handleDeclarativeComponentAnnotation(component, declarativeComponent);
            this.log("register component with annotations : " + declarativeComponent.id());
        }

    }

    private void handleDeclarativeComponentAnnotation(final ISubComponent<EventHandler<Event>, Event, Object> component, final DeclarativeComponent declarativeComponent) {
        handleBaseAttributes(AComponent.class, component, declarativeComponent.id(), declarativeComponent.active(),
                declarativeComponent.name());
        handleDeclarativeComponentAnnotations(declarativeComponent, (AFXComponent) component);
    }

    private void handleCallbackAnnotation(final ISubComponent<EventHandler<Event>, Event, Object> component, final CallbackComponent callbackAnnotation) {
        handleBaseAttributes(AComponent.class, component, callbackAnnotation.id(), callbackAnnotation.active(),
                callbackAnnotation.name());
    }

    private void handleComponentAnnotation(final ISubComponent<EventHandler<Event>, Event, Object> component, final Component componentAnnotation) {
        handleBaseAttributes(AComponent.class, component, componentAnnotation.id(), componentAnnotation.active(),
                componentAnnotation.name());
        handleComponentAnnotation(componentAnnotation, (AFXComponent) component);
    }

    /**
     * set component members
     *
     * @param componentAnnotation
     * @param component
     */
    private void handleComponentAnnotation(final Component componentAnnotation, final AFXComponent component) {
        setExecutionTarget(component, componentAnnotation.defaultExecutionTarget());
        setLocale(component, componentAnnotation.localeID());
        setRessourceBundleLocation(component, componentAnnotation.resourceBundleLocation());
        this.log("register component with annotations : " + componentAnnotation.id());
    }

    /**
     * set declarative component members
     *
     * @param declarativeComponent
     * @param component
     */
    private void handleDeclarativeComponentAnnotations(final DeclarativeComponent declarativeComponent, final AFXComponent component) {
        setExecutionTarget(component, declarativeComponent.defaultExecutionTarget());
        FXUtil.setPrivateMemberValue(AFXComponent.class, component, FXUtil.IDECLARATIVECOMPONENT_VIEW_LOCATION,
                declarativeComponent.viewLocation());
        FXUtil.setPrivateMemberValue(AFXComponent.class, component, FXUtil.IDECLARATIVECOMPONENT_TYPE,
                UIType.DECLARATIVE);
        setLocale(component, declarativeComponent.localeID());
        setRessourceBundleLocation(component, declarativeComponent.resourceBundleLocation());
    }

    private void setRessourceBundleLocation(final AFXComponent component, String bundleLocation) {
        if (component.getResourceBundleLocation() != null)
            FXUtil.setPrivateMemberValue(AFXComponent.class, component, FXUtil.IDECLARATIVECOMPONENT_BUNDLE_LOCATION,
                    bundleLocation);
    }

    private void setLocale(final AFXComponent component, String locale) {
        if (component.getLocaleID() != null)
            FXUtil.setPrivateMemberValue(AFXComponent.class, component, FXUtil.IDECLARATIVECOMPONENT_LOCALE,
                    locale);
    }

    private void setExecutionTarget(final AFXComponent component, String value) {
        if (component.getExecutionTarget().length() <= 1)
            FXUtil.setPrivateMemberValue(ASubComponent.class, component, FXUtil.ACOMPONENT_EXTARGET,
                    value);
    }

    /**
     * set base component members
     *
     * @param clazz
     * @param component
     * @param id
     * @param active
     * @param name
     */
    private void handleBaseAttributes(Class<?> clazz,
                                      final ISubComponent<EventHandler<Event>, Event, Object> component, final String id, final boolean active,
                                      final String name) {
        if (id != null) FXUtil.setPrivateMemberValue(clazz, component, FXUtil.ACOMPONENT_ID, id);
        FXUtil.setPrivateMemberValue(clazz, component, FXUtil.ACOMPONENT_ACTIVE, active);
        if (name != null) FXUtil.setPrivateMemberValue(clazz, component, FXUtil.ACOMPONENT_NAME, name);
    }

    @Override
    public final void unregisterComponent(
            final ISubComponent<EventHandler<Event>, Event, Object> component) {
        synchronized (lock) {
            this.log("unregister component: " + component.getId());
            component.initEnv(null, null);
            ComponentRegistry.removeComponent(component);
            if (this.subcomponents.contains(component)) {
                this.subcomponents.remove(component);
            }
        }
    }

    @Override
    public final void initComponents(final IAction<Event, Object> action) {
        final String targetId = FXUtil.getTargetComponentId(action
                .getTargetId());
        this.log("3.4.4.1: subcomponent targetId: " + targetId);
        final List<ISubComponent<EventHandler<Event>, Event, Object>> components = this
                .getSubcomponents();
        if (components == null) return;
        components.parallelStream().forEach(component -> {
            if (component.getId().equals(targetId)) {
                this.log("3.4.4.2: subcomponent init with custom action");
                this.componentHandler.initComponent(action, component);
            } // else END
            else if (component.isActive() && !component.isStarted()) {
                this.log("3.4.4.2: subcomponent init with default action");
                this.componentHandler.initComponent(
                        new FXAction(component.getId(), component.getId(),
                                "init", null), component);
            } // if END
        });
    }

    @Override
    public final void setSubcomponents(
            final List<ISubComponent<EventHandler<Event>, Event, Object>> subComponents) {
        this.subcomponents = subComponents;

    }

    private void log(final String message) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine(">> " + message);
        }
    }

    /**
     * Register components at componentHandler.
     *
     * @param <M>
     * @param components
     */
    private <M extends ISubComponent<EventHandler<Event>, Event, Object>> void registerSubcomponents(
            final List<M> components) {
        components.forEach(this::registerComponent);
    }

    @Override
    public List<ISubComponent<EventHandler<Event>, Event, Object>> getSubcomponents() {
        return this.subcomponents;
    }

    @Override
    public final IPerspectiveLayout<? extends Node, Node> getIPerspectiveLayout() {
        return this.perspectiveLayout;
    }

    @Override
    public final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> getComponentDelegateQueue() {
        return this.componentDelegateQueue;
    }

    @Override
    public final BlockingQueue<IDelegateDTO<Event, Object>> getMessageDelegateQueue() {
        return this.messageDelegateQueue;
    }

    @Override
    public final BlockingQueue<IAction<Event, Object>> getComponentsMessageQueue() {
        return this.componentCoordinator.getMessageQueue();

    }

    @Override
    public final IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> getComponentHandler() {
        return this.componentHandler;
    }

    @Override
    public final String getViewLocation() {
        if (type.equals(UIType.PROGRAMMATIC))
            throw new UnsupportedOperationException("Only supported when @DeclarativeComponent annotation is used");
        return this.viewLocation;
    }

    @Override
    public final void setViewLocation(String documentURL) {
        super.checkPolicy(this.viewLocation, "Do Not Set document manually");
        this.viewLocation = documentURL;
        this.type = UIType.DECLARATIVE;
    }

    @Override
    public final void initialize(URL url, ResourceBundle resourceBundle) {
        this.documentURL = url;
        this.resourceBundle = resourceBundle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final URL getDocumentURL() {
        if (type.equals(UIType.PROGRAMMATIC))
            throw new UnsupportedOperationException("Only supported when @DeclarativeComponent annotation is used");
        return documentURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final UIType getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getLocaleID() {
        return localeID;
    }

    public final void setLocaleID(String localeID) {
        super.checkPolicy(this.localeID, "Do Not Set document manually");
        this.localeID = localeID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getResourceBundleLocation() {
        return resourceBundleLocation;
    }

    public final void setResourceBundleLocation(String resourceBundleLocation) {
        super.checkPolicy(this.resourceBundleLocation, "Do Not Set document manually");
        this.resourceBundleLocation = resourceBundleLocation;
    }

}
