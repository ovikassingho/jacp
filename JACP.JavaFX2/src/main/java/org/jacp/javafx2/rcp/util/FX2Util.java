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
}
