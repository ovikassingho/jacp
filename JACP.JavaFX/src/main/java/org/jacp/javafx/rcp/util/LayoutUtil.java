package org.jacp.javafx.rcp.util;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * @author Patrick Symmangk
 * 
 */
public class LayoutUtil {

    public static class GridPaneUtil {

        public static void setFullGrow(Priority priority, Node... nodes) {
            for (Node node : nodes) {
                if (node != null) {
                    GridPane.setVgrow(node, priority);
                    GridPane.setHgrow(node, priority);
                }
            }
        }

    }

}
