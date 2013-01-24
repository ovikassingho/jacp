/************************************************************************
 * 
 * Copyright (C) 2010 - 2013
 *
 * [JACPManagedDialog.java]
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
package org.jacp.javafx.rcp.components.managedDialog;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.annotations.Dialog;
import org.jacp.api.annotations.Resource;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.dialog.Scope;
import org.jacp.api.launcher.Launcher;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.component.ASubComponent;
import org.jacp.javafx.rcp.util.ComponentRegistry;
import org.jacp.javafx.rcp.util.FXUtil;

/**
 * The JACPManagedDialog handles creation of managed dialog components. A
 * managed Dialog is part of an UIComponent, it has always a UIComponent as a
 * parent element (normally the caller component), it has full access to DI
 * injection (services, etc.) and it can be used as managed node or JACPModal
 * dialog.
 * 
 * @author Andy Moncsek
 * 
 */
public class JACPManagedDialog {
	/**
	 * the reference to DI container
	 */
	private static Launcher<?> launcher;

	/**
	 * The running instance.
	 */
	private static JACPManagedDialog instance;

	/**
	 * initialize the JACPManaged dialog.
	 * 
	 * @param launcher
	 */
	public static void initManagedDialog(Launcher<?> launcher) {
		if (JACPManagedDialog.instance == null) {
			JACPManagedDialog.launcher = launcher;
			JACPManagedDialog.instance = new JACPManagedDialog();
		}
	}

	/**
	 * Returns an instance of JACPManagedDialog, to create managed dialogs.
	 * 
	 * @return the instance
	 */
	public static JACPManagedDialog getInstance() {
		if (JACPManagedDialog.instance == null)
			throw new ManagedDialogNotInitializedException();
		return JACPManagedDialog.instance;
	}

	/**
	 * Creates a managed dialog.
	 * 
	 * @param type
	 * @return a managed dialog handler see {@link ManagedDialogHandler}
	 */
	public <T> ManagedDialogHandler<T> getManagedDialog(Class<? extends T> type) {
		@SuppressWarnings("restriction")
		String callerClassName = sun.reflect.Reflection.getCallerClass(2)
				.getName();
		final Dialog dialogAnnotation = type.getAnnotation(Dialog.class);
		if (dialogAnnotation == null)
			throw new ManagedDialogAnnotationMissingException();
		final String id = dialogAnnotation.id();
		final Scope scope = dialogAnnotation.scope();
		final T bean = launcher.registerAndGetBean(type, id, scope);
		final String resourceBundleLocation = dialogAnnotation
				.resourceBundleLocation();
		final String localeID = dialogAnnotation.localeID();
		final ResourceBundle bundle = FXUtil.getBundle(resourceBundleLocation,
				localeID);
		try {
			checkMemberAnnotations(bean, bundle, callerClassName);
		} catch (IllegalArgumentException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (bean instanceof Node)
			return new ManagedDialogHandler<T>(bean, (Node) bean, id);

		return createFXMLDialog(dialogAnnotation, id, scope, bean, bundle);

	}

	private <T> ManagedDialogHandler<T> createFXMLDialog(
			final Dialog dialogAnnotation, final String id, final Scope scope,
			final T bean, final ResourceBundle bundle) {
		final String viewLocation = dialogAnnotation.viewLocation();
		if (viewLocation == null)
			throw new ManagedDialogAnnotationFXMLMissingException();
		final URL url = getClass().getResource(viewLocation);
		return new ManagedDialogHandler<T>(bean,
				FXUtil.loadFXMLandSetController(bean, bundle, url), id);
	}

	private <T> void checkMemberAnnotations(final T bean,
			final ResourceBundle bundle, final String callerClassName)
			throws IllegalArgumentException, IllegalAccessException,
			ClassNotFoundException {
		for (final Field field : bean.getClass().getDeclaredFields()) {
			final Resource resource = field.getAnnotation(Resource.class);
			if (resource == null)
				continue;
			if (bundle!=null && field.getType().isAssignableFrom(bundle.getClass())
					&& bundle != null) {
				field.setAccessible(true);
				field.set(bean, bundle);
			} else if (ASubComponent.class.isAssignableFrom(field.getType())
					|| AFXComponent.class.isAssignableFrom(field.getType())) {
				handleParentComponentAnnotation(bean, field, resource,
						callerClassName);
			}

		}
	}

	private <T> void handleParentComponentAnnotation(final T bean,
			final Field field, final Resource resource,
			final String callerClassName) throws ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException {
		final String parentId = resource.parentId();
		ISubComponent<EventHandler<Event>, Event, Object> comp = null;
		if (parentId.isEmpty()) {
			comp = ComponentRegistry.findComponentByClass(Class
					.forName(callerClassName));
		} else {
			comp = ComponentRegistry.findComponentById(parentId);
		}
		if (comp == null)
			throw new IllegalArgumentException("component could not be foud");
		field.setAccessible(true);
		field.set(bean, comp);
	}

}
