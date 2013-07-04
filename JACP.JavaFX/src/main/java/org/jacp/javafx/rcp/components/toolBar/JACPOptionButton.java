/************************************************************************
 *
 * Copyright (C) 2010 - 2012
 *
 * [JACPToolBar.java]
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
package org.jacp.javafx.rcp.components.toolBar;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.CSSUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class JACPOptionButton.
 *
 * @author Patrick Symmangk
 */
public class JACPOptionButton extends Button {

    private Pane glassPane;

    private VBox hoverMenu;

    private VBox options;

    public JACPOptionButton(String label, final FXComponentLayout layout) {
        super(label);
        this.glassPane  = layout.getGlassPane();
        initAction();
        initLayout();
    }

    private void initLayout(){
        this.options    = new VBox();
        this.hoverMenu  = new VBox();
        Pane arrow = new Pane();
        arrow.setMinHeight(5);
        arrow.setMaxWidth(10);
        hoverMenu.setAlignment(Pos.CENTER);
        CSSUtil.addCSSClass("top-arrow", arrow);
        CSSUtil.setBackgroundColor(options, "#333333");
        this.hoverMenu.getChildren().setAll(arrow, options);
        this.glassPane.getChildren().setAll(hoverMenu);
        this.glassPane.setMaxWidth(options.getWidth());
        this.glassPane.setMaxHeight(options.getHeight());
    }

    private void initAction() {
        this.setOnAction((EventHandler<ActionEvent>)(actionEvent)-> {
                double testX = getBoundsInParent().getMinX() + getParent().getBoundsInParent().getMinX();
                double target = testX - (options.getWidth() - getWidth()) / 2;
                double move = target - glassPane.getBoundsInParent().getMinX();

                glassPane.setTranslateX(move);
                glassPane.setTranslateY(-(glassPane.getBoundsInParent().getMinY() - getBoundsInParent().getMaxY()));
                glassPane.setVisible(!glassPane.isVisible());

        });
    }

    public void addOption(Button option) {
        option.setMaxWidth(Integer.MAX_VALUE);
        options.getChildren().add(option);
        VBox.setMargin(option, new Insets(5,5,5,5));
    }


    public void hideOptions(){
        this.glassPane.setVisible(false);
    }



}
