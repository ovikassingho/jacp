package org.jacp.api.component;

/**
 * defines methods for background components
 * @author Andy Moncsek
 *
 * @param <L>
 * @param <A>
 * @param <M>
 */
public interface IBGComponent<L, A, M> extends ISubComponent<L, A, M> {
	
	
	/**
	 * returns component target id which is targeted by bg component return value; the return value will be handled like an average message and will be delivered to targeted component
	 * @return
	 */
	public abstract String getHandleComponentTarget();
	
	/**
	 * set component target id which is targeted by bg component return value; the return value will be handled like an average message and will be delivered to targeted component
	 * @param componentTargetId
	 */
	public abstract void setHandleComponentTarget(final String componentTargetId);

}
