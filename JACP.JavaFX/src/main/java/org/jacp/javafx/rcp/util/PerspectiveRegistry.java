package org.jacp.javafx.rcp.util;

import javafx.event.Event;
import javafx.event.EventHandler;
import org.jacp.api.component.IPerspective;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created with IntelliJ IDEA.
 * User: Andy
 * Date: 28.05.13
 * Time: 21:13
 * Global registry with references to all perspectives
 */
public class PerspectiveRegistry {
    private static volatile List<IPerspective<EventHandler<Event>, Event, Object>> perspectives = new CopyOnWriteArrayList<IPerspective<EventHandler<Event>, Event, Object>>();
    private static volatile ReadWriteLock lock = new ReentrantReadWriteLock();
    private static AtomicReference<String> currentVisiblePerspectiveId = new AtomicReference<>();

    /**
     * Set a new perspective id and returns the current id.
     * @param id
     * @return
     */
    public static String getAndSetCurrentVisiblePerspective(final String id){
        final String current = currentVisiblePerspectiveId.get();
        currentVisiblePerspectiveId.set(id);
        return current;
    }


    /**
     * Registers a component.
     *
     * @param component
     */
    public static void registerPerspective(
            final IPerspective<EventHandler<Event>, Event, Object> component) {
        lock.writeLock().lock();
        try{
            if (!perspectives.contains(component))
                perspectives.add(component);
        }finally{
            lock.writeLock().unlock();
        }

    }

    /**
     * Removes component from registry.
     *
     * @param component
     */
    public static void removePerspective(
            final IPerspective<EventHandler<Event>, Event, Object> component) {
        lock.writeLock().lock();
        try{
            if (perspectives.contains(component))
                perspectives.remove(component);
        }finally{
            lock.writeLock().unlock();
        }

    }

    /**
     * Returns a component by component id
     *
     * @param targetId
     * @return
     */
    public static IPerspective<EventHandler<Event>, Event, Object> findPerspectiveById(
            final String targetId) {
        lock.readLock().lock();
        try{
            return FXUtil.getObserveableById(FXUtil.getTargetComponentId(targetId),
                    perspectives);
        }finally{
            lock.readLock().unlock();
        }

    }
    /**
     * Returns the a component by class.
     * @param clazz
     * @return
     */
    public static IPerspective<EventHandler<Event>, Event, Object> findPerspectiveByClass(final Class<?> clazz) {
        lock.readLock().lock();
        try{
            for(final IPerspective<EventHandler<Event>, Event, Object> comp : perspectives) {
                if(comp.getClass().isAssignableFrom(clazz))return comp;
            }
            return null;
        }finally{
            lock.readLock().unlock();
        }
    }

    /**
     * applies a simple predicate to all perspectives
     * @param predicate
     */
    public static void applyToPerspectives(Predicate predicate) {
        lock.writeLock().lock();
        try{
            for(final IPerspective<EventHandler<Event>, Event, Object> p : perspectives) {
                    predicate.apply(p);
            }
        }finally{
            lock.writeLock().unlock();
        }
    }
}
