package org.jacp.javafx.rcp.context;

import javafx.event.Event;
import javafx.event.EventHandler;
import org.jacp.api.context.Context;

/**
 * Created with IntelliJ IDEA.
 * User: ady
 * Date: 02.07.13
 * Time: 13:29
 * To change this template use File | Settings | File Templates.
 */
public interface JACPContext extends Context<EventHandler<Event>, Event, Object> {
}
