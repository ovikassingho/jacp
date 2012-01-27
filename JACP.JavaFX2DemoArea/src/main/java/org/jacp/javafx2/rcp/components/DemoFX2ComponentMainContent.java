package org.jacp.javafx2.rcp.components;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jacp.api.action.IAction;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

public class DemoFX2ComponentMainContent extends AFX2Component {

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node postHandleAction(Node node, IAction<Event, Object> action) {
		// Pane group = new Pane();
		// FlowPane mainContent = new FlowPane(Orientation.VERTICAL);
		final TilePane mainContent = new TilePane();
		mainContent.setPrefRows(3);
		// Pane mainContent = new Pane();
		mainContent.setPadding(new Insets(30));
		mainContent.setOnScroll(new EventHandler<ScrollEvent>() {

			 @Override public void handle(ScrollEvent event) {
				ObservableList<Node> children = mainContent.getChildren();
				for(Node view:children) {
					if(view instanceof ImageView) {
						ImageView tmp = (ImageView) view ;
						// TODO add animation
						tmp.setFitWidth(tmp.getFitWidth() + event.getDeltaY());
						tmp.setFitHeight(tmp.getFitHeight() + event.getDeltaY());
					}
					
				}
		           
		        }
		});

		int x = 0;
		while (x < 20) {
			Image image =new Image(DemoFX2ComponentMainContent.class.getResource(
					"/mad.jpg").getFile());
			final ImageView view = new ImageView(image);
			view.setFitHeight(80);
			view.setFitWidth(80);
			
			
			view.setEffect(new DropShadow());
			
//			Rectangle r = new Rectangle(80, 80);
//			r.setFill(Color.LIGHTGRAY);
//			r.getStyleClass().add("main-container");
//			r.setEffect(new DropShadow());
			TilePane.setMargin(view, new Insets(10));

			mainContent.getChildren().add(view);
			x++;
		}

		// mainContent.setStyle("-fx-background-color: blue;");
		// GridPane.setVgrow(group, Priority.ALWAYS);
		// GridPane.setHgrow(group, Priority.ALWAYS);
		// group.getChildren().add(mainContent);
		return mainContent;
	}

	@Override
	public void onStartComponent(FX2ComponentLayout layout) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTearDownComponent(FX2ComponentLayout layout) {
		// TODO Auto-generated method stub

	}

}
