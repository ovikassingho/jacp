package org.jacp.javafx.rcp.util;

/**
 * Created with IntelliJ IDEA.
 * User: Andy Moncsek
 * Date: 28.05.13
 * Time: 21:17
 * Simple predicate to apply functions in registry
 */
public interface Predicate {
    <T> void apply(T t);
}
