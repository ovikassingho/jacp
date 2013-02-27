/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [JACPModalDialog.java]
 * AHCP Project (http://jacp.googlecode.com)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 *
 ************************************************************************/
package org.jacp.javafx.rcp.components.modalDialog;

import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * The Class JACPModalDialog.
 * 
 * @author Patrick Symmangk
 * 
 */
public class JACPModalDialog extends StackPane implements IModalMessageNode {

    /** The maximum blur radius. */
    private final double MAX_BLUR = 4.0;

    /** The root. */
    private static Node root;

    /** The instance. */
    private static JACPModalDialog instance;

    private Timeline hideTimeline;
    private Timeline showTimeline;

    /**
     * Gets the single instance of JACPModalDialog.
     * 
     * @return single instance of JACPModalDialog
     */
    public static JACPModalDialog getInstance() {

        if (JACPModalDialog.instance == null) {
            throw new DialogNotInitializedException();
        }
        return JACPModalDialog.instance;
    }

    /**
     * Inits the dialog.
     * 
     * @param rootNode
     *            the root node
     */
    public synchronized static void initDialog(final Node rootNode) {
        if (JACPModalDialog.instance == null) {
            JACPModalDialog.root = rootNode;
            JACPModalDialog.instance = new JACPModalDialog();
            JACPModalDialog.instance.setId("error-dimmer");
            final Button test = new Button("close");
            test.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(final ActionEvent arg0) {
                    final GaussianBlur blur = (GaussianBlur) rootNode.getEffect();
                    blur.setRadius(0.0);
                    JACPModalDialog.instance.setVisible(false);
                }

            });
        }

    }

    /**
     * Show modal message.
     * 
     * @param message
     *            the message
     */
    public void showModalMessage(final Node message) {
        if (getHideTimeline().getStatus() == Status.RUNNING) {
            getHideTimeline().stop();
        }
        this.getChildren().clear();
        this.getChildren().add(message);
        this.setOpacity(0);
        this.setVisible(true);
        this.setCache(true);
        ((GaussianBlur) JACPModalDialog.root.getEffect()).setRadius(this.MAX_BLUR);

        getShowTimeline().play();
    }

    /**
     * Hide any modal message that is shown.
     */
    @Override
    public void hideModalMessage() {
        this.setCache(true);
        getHideTimeline().play();
        ((GaussianBlur) JACPModalDialog.root.getEffect()).setRadius(0.0);
    }

    private Timeline getHideTimeline() {
        hideTimeline = (hideTimeline == null) ? TimelineBuilder.create().keyFrames(new KeyFrame(Duration.millis(250), new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent t) {
                JACPModalDialog.this.setCache(false);
                JACPModalDialog.this.setVisible(false);
            }
        }, new KeyValue(this.opacityProperty(), 0, Interpolator.EASE_BOTH))).build() : hideTimeline;
        return hideTimeline;
    }

    private Timeline getShowTimeline() {
        showTimeline = (showTimeline == null) ? TimelineBuilder.create().keyFrames(new KeyFrame(Duration.millis(250), new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent t) {
                JACPModalDialog.this.setCache(false);
            }
        }, new KeyValue(this.opacityProperty(), 1, Interpolator.EASE_BOTH))).build() : showTimeline;
        return showTimeline;
    }

}
