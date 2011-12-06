package org.jacp.javafx2.rcp.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
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
}
