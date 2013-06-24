package org.jacp.javafx.rcp.context;

import javafx.event.Event;
import javafx.event.EventHandler;
import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.util.UIType;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.action.FXActionListener;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Andy Moncsek
 * Date: 24.06.13
 * Time: 20:41
 * The Context allows to get access to components basic features and members like id, message listener end other stuff.
 */
public class Context {
    private volatile String executionTarget = "";


    private volatile BlockingQueue<IAction<Event, Object>> incomingMessage = new ArrayBlockingQueue<>(
            1000);
    private String id;
    private String name;

    private volatile String parentId;
    private String localeID="";
    private String resourceBundleLocation="";
    private String viewLocation;

    private final Semaphore lock = new Semaphore(1);
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private volatile boolean active = false;
    private volatile boolean started = false;

    protected volatile BlockingQueue<IAction<Event, Object>> globalMessageQueue;


    private URL documentURL;
    private ResourceBundle resourceBundle;
    private UIType type = UIType.PROGRAMMATIC;

    public Context(final String id, final String name) {
               this.id = id;
        this.name = name;
    }

    public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
            final Object message) {
        return new FXActionListener(new FXAction(this.id, message),
                this.globalMessageQueue);
    }

    public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
            final String targetId, final Object message) {
        return new FXActionListener(new FXAction(this.id, targetId, message, null),
                this.globalMessageQueue);
    }

    /**
     * {@inheritDoc}
     */
    public final String getId() {
        if (this.id == null) {
            throw new UnsupportedOperationException("No id set");
        }
        return this.id;

    }

    public final boolean isActive() {
        return this.active;
    }

    public final void setActive(final boolean active) {
        this.active = active;

    }

    public final boolean isStarted() {
        return started;
    }

    public final String getName() {
        if (this.name == null) {
            throw new UnsupportedOperationException("No name set");
        }
        return this.name;
    }


    public int compareTo(String o) {
        return this.getId().compareTo(o);
    }

    public final void initEnv(final String parentId,
                              final BlockingQueue<IAction<Event, Object>> messageQueue) {
        this.parentId = parentId;
        this.globalMessageQueue = messageQueue;

    }


    public final String getExecutionTarget() {
        return this.executionTarget;
    }


    public final void setExecutionTarget(final String target) {
        this.executionTarget = target;

    }


    public final boolean hasIncomingMessage() {
        return !this.incomingMessage.isEmpty();
    }


    public final void putIncomingMessage(final IAction<Event, Object> action) {
        try {
            this.incomingMessage.put(action);
        } catch (final InterruptedException e) {
            logger.info("massage put failed:");
        }

    }

    public final IAction<Event, Object> getNextIncomingMessage() {
        if (this.hasIncomingMessage()) {
            try {
                return this.incomingMessage.take();
            } catch (final InterruptedException e) {
                logger.info("massage take failed:");
            }
        }
        return null;
    }

    public final boolean isBlocked() {
        return lock.availablePermits() == 0;
    }

    public final void lock() {
        try {
            lock.acquire();
        } catch (InterruptedException e) {
            logger.info("lock interrupted.");
        }
    }

    public final void release() {
        lock.release();
    }


    public final String getParentId() {
        return this.parentId;
    }

    /**
     * {@inheritDoc}
     */
    public final String getViewLocation() {
        if(type.equals(UIType.PROGRAMMATIC))throw new UnsupportedOperationException("Only supported when @DeclarativeComponent annotation is used");
        return viewLocation;
    }
    /**
     * {@inheritDoc}
     */
    public final void setViewLocation(String document){
        this.viewLocation = document;
        this.type = UIType.DECLARATIVE;
    }

    public final void initialize(URL url, ResourceBundle resourceBundle) {
        this.documentURL = url;
        this.resourceBundle = resourceBundle;
    }

    /**
     * {@inheritDoc}
     */
    public final URL getDocumentURL() {
        if(type.equals(UIType.PROGRAMMATIC))throw new UnsupportedOperationException("Only supported when @DeclarativeComponent annotation is used");
        return documentURL;
    }

    /**
     * {@inheritDoc}
     */
    public final ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
    /**
     * {@inheritDoc}
     */
    public final UIType getType() {
        return type;
    }
    /**
     * {@inheritDoc}
     */
    public String getLocaleID() {
        return localeID;
    }

    public void setLocaleID(String localeID) {
        this.localeID = localeID;
    }
    /**
     * {@inheritDoc}
     */
    public String getResourceBundleLocation() {
        return resourceBundleLocation;
    }

    public void setResourceBundleLocation(String resourceBundleLocation) {
        this.resourceBundleLocation = resourceBundleLocation;
    }
}
