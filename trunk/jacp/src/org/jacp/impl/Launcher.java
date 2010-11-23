package org.jacp.impl;


public interface Launcher<E> {
    public abstract E getContext();
    
    public <P> P getBean(final Class<P> clazz);
}
