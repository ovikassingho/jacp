package org.jacp.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the meta attributes for a callback component.
 * 
 * @author Andy Moncsek
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CallbackComponent {
	/**
	 * The components name.
	 * 
	 * @return
	 */
	String name();

	/**
	 * The component id.
	 * 
	 * @return
	 */
	String id();

	/**
	 * The active state at start time.
	 * 
	 * @return
	 */
	boolean active() default true;
}
