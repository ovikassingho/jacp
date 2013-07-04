package org.jacp.javafx.rcp.context;

import javafx.event.Event;
import javafx.event.EventHandler;
import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.context.Context;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.action.FXActionListener;

import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: Andy Moncsek
 * Date: 24.06.13
 * Time: 21:36
 * JACP context object provides functionality to components context and basic features.
 */
public class JACPContextImpl implements JACPContext {

    protected volatile BlockingQueue<IAction<Event, Object>> globalMessageQueue;
    private String id;
    private String name;



    // TODO remove ResourceBundle from AFXComponent
    private ResourceBundle resourceBundle;

    public JACPContextImpl(final String id, final String name, BlockingQueue<IAction<Event, Object>> globalMessageQueue) {
        this.id = id;
        this.name = name;
        this.globalMessageQueue = globalMessageQueue;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
            final Object message) {
        return new FXActionListener(new FXAction(this.id, message),
                this.globalMessageQueue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
            final String targetId, final Object message) {
        return new FXActionListener(new FXAction(this.id, targetId, message, null),
                this.globalMessageQueue);
    }

    public final String getId(){
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final ResourceBundle getResourceBundle(){
        return this.resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

}
