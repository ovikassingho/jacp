package org.jacp.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the meta attributes for a callback component
 * 
 * @author Andy Moncsek
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CallbackComponent {
	/**
	 * the components name
	 * 
	 * @return
	 */
	public String name();

	/**
	 * the component id
	 * 
	 * @return
	 */
	public String id();

	/**
	 * the active state at start time
	 * 
	 * @return
	 */
	public boolean active() default true;
}
