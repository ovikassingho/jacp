package org.jacp.api.coordinator;


import org.jacp.api.action.IAction;
import org.jacp.api.component.IBGComponent;

/**
 * handles instances of a state less component; delegates message to a non
 * blocked component instance or if all components are blocked message is
 * delegated to queue in one of existing instances
 * 
 * @author Andy Moncsek
 * 
 * @param <L>
 * @param <A>
 * @param <M>
 */
public interface IStatelessComponentCoordinator<L, A, M> {
    /**
     * handles incoming message to managed state less component
     * 
     * @param message
     */
    public abstract void incomingMessage(final IAction<A, M> message);

    /**
     * returns a new instance of managed state less component
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    public abstract <T extends IBGComponent<L, A, M>> IBGComponent<L, A, M> getCloneBean(final Class<T> clazz);


}