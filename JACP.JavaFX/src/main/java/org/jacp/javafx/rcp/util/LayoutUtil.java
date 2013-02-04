package org.jacp.javafx.rcp.util;

import java.util.Arrays;
import java.util.Collection;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

        
        public static void setHGrow(Priority priority, Node... nodes) {
            for (Node node : nodes) {
                if (node != null) {
                    GridPane.setHgrow(node, priority);
                }
            }
        }
        
        
        public static void setVGrow(Priority priority, Node... nodes) {
            for (Node node : nodes) {
                if (node != null) {
                    GridPane.setVgrow(node, priority);
                }
            }
        }
        
    }

    public static class HBoxUtil {

        public static void setHGrow(Priority priority, Node... nodes) {
            for (Node node : nodes) {
                if (node != null) {
                    HBox.setHgrow(node, priority);
                }
            }
        }

        public static void setMargin(Insets insets, Node... nodes) {
            setMargin(insets, Arrays.asList(nodes));

        }

        public static void setMargin(Insets insets, Collection<Node> nodes) {
            for (Node node : nodes) {
                if (node != null) {
                    HBox.setMargin(node, insets);
                }
            }
        }

    }
}
