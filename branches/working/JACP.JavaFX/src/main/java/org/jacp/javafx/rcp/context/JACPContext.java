package org.jacp.javafx.rcp.context;

import javafx.event.Event;
import javafx.event.EventHandler;
import org.jacp.api.action.IActionListener;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.action.FXActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Andy Moncsek
 * Date: 24.06.13
 * Time: 21:36
 * JACP context object provides functionality to components context and basic features.
 */
public class JACPContext {

    private Context context;

    public JACPContext(final String id, final String name) {
        context = new Context(id,name) ;
    }

    /**
     * Returns a local message listener to send messages to the component itself.
     * @param message
     * @return
     */
    public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
            final Object message) {
        return context.getActionListener(message);
    }

    /**
     * Returns a global message listener to
     * @param targetId
     * @param message
     * @return
     */
    public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
            final String targetId, final Object message) {
        return context.getActionListener(targetId,message);
    }

    public final String getId(){
        return context.getId();
    }
}
