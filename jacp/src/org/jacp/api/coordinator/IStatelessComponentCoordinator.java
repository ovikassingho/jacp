package org.jacp.api.coordinator;


import org.jacp.api.action.IAction;

/**
 * handles instances of a state less component; delegates message to a non blocked component instance or if all components are blocked message is delegated to queue in one of existing instances
 * @author Andy Moncsek
 *
 * @param <L>
 * @param <A>
 * @param <M>
 */
public interface IStatelessComponentCoordinator<L, A, M> {

    public abstract void incomingMessage(IAction<A, M> message);

}