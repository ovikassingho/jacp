package org.jacp.api.component;

import org.jacp.impl.Launcher;
/**
 * represents a state less backgound component
 * @author Andy Moncsek
 *
 * @param <L>
 * @param <A>
 * @param <M>
 */
public interface IStateLessBGComponent<L, A, M> extends IBGComponent<L, A, M> {
    public abstract void setLauncher(final Launcher<?> launcher);
}
