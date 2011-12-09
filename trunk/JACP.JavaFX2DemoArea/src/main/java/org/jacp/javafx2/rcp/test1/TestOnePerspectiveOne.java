package org.jacp.javafx2.rcp.test1;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;

public class TestOnePerspectiveOne extends AFX2Perspective {
	Button button = new Button("switch");
	Button buttonRight = new Button("switch");
	GridPane mainLeft = new GridPane();
	GridPane mainRight = new GridPane();
	String colorLeft = "-fx-background-color:#E0F8E0;";
	String colorRight = "-fx-background-color:#F7BE81;";
	String descriptionText = "Description: A single perspective with two buttons, each of them creates a message to parent perspective to switch colors. \n";
	String description = "public class TestOnePerspectiveOne extends AFX2Perspective {\n\tButton buttonLeft = new Button(\"switch\");" +
			"\n\tString colorLeft = \"-fx-background-color:#E0F8E0;\"" +
			"\n\tpublic void onStartPerspective(FX2ComponentLayout layout) { }" +
			"\n\tpublic void onTearDownPerspective(FX2ComponentLayout layout) { }" +
			"\n\tpublic void handlePerspective(IAction<Event, Object> action, FX2PerspectiveLayout perspectiveLayout) {\n" +
			"\t\tif (!action.getLastMessage().equals(\"switch\")) {\n" +
			"\t\t\tBorderPane root = new BorderPane();\n" +
			"\t\t\tSplitPane splitPane = new SplitPane();\n"+
			"\t\t\tmainLeft.setStyle(colorLeft);\n"+
			"\t\t\tIActionListener<EventHandler<Event>, Event, Object> listenerLeft = getActionListener();\n"+
			"\t\t\tlistenerLeft.getAction().setMessage(\"switch\");\n"+
			"\t\t\tbuttonLeft.setOnMouseClicked(listenerLeft);\n"+
			"\t\t\tmainLeft.add(buttonLeft, 0, 1);\n"+
			"\t\t\tsplitPane.getItems().addAll(mainLeft, mainRight);\n"+
			"\t\t\troot.setTop(splitPane);\n"+
			"\t\t\tperspectiveLayout.registerRootComponent(root);\n"+
			"\t\t}else {\n"+
			"\t\t\tString tmp = colorLeft;\n"+
			"\t\t\tcolorLeft = colorRight;\n"+
			"\t\t\tcolorRight = tmp;\n"+
			"\t\t\tmainLeft.setStyle(colorLeft);\n"+
			"\t\t\tmainRight.setStyle(colorRight);\n\t\t\t}\n\t}\n}";

	@Override
	public void onStartPerspective(FX2ComponentLayout layout) {

	}

	@Override
	public void onTearDownPerspective(FX2ComponentLayout layout) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlePerspective(IAction<Event, Object> action,
			FX2PerspectiveLayout perspectiveLayout) {
		if (!action.getLastMessage().equals("switch")) {
			BorderPane root = new BorderPane();
			SplitPane splitPane = new SplitPane();

			mainLeft.setStyle(colorLeft);
			Pane leftPane = new Pane();
			leftPane.setMinSize(512, 300);
			button.setOnMouseClicked(getListener(null, "switch"));
			mainLeft.add(leftPane, 0, 1);
			leftPane.getChildren().add(button);

			mainRight.setStyle(colorRight);
			Pane rightPane = new Pane();
			rightPane.setMinSize(512, 300);
			buttonRight.setOnMouseClicked(getListener(null, "switch"));
			mainRight.add(rightPane, 0, 1);
			rightPane.getChildren().add(buttonRight);
			splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			splitPane.setDividerPosition(0, 0.25f);
			splitPane.getItems().addAll(mainLeft, mainRight);
			splitPane.setId("page-splitpane");

			ScrollPane scrollPaneBottom = new ScrollPane();
			VBox paneBottom = new VBox();
			Text title = new Text("Description: ");
			title.setFont(Font.font("Amble CN", FontWeight.BOLD, 14));
			Text textDesc = new Text(descriptionText);
			textDesc.setFont(Font.font("Amble CN", FontWeight.MEDIUM, 14));
			Text text = new Text(description);
			text.setFont(Font.font("Amble CN", FontWeight.LIGHT, 14));
			paneBottom.setMinSize(1024, 660);
			paneBottom.setPadding(new Insets(30, 30, 30, 30));
			paneBottom.getChildren().add(title);
			paneBottom.getChildren().add(textDesc);
			paneBottom.getChildren().add(text);
			scrollPaneBottom.setContent(paneBottom);
			scrollPaneBottom.setMinSize(1024, 660);
			scrollPaneBottom.setStyle("-fx-background-color:#CEE3F6;");
			root.setBottom(scrollPaneBottom);

			root.setTop(splitPane);

			perspectiveLayout.registerRootComponent(root);
		} else {
			String tmp = colorLeft;
			colorLeft = colorRight;
			colorRight = tmp;
			mainLeft.setStyle(colorLeft);
			mainRight.setStyle(colorRight);
		}

	}

	private EventHandler<? super MouseEvent> getListener(final String id,
			final String message) {
		final IActionListener<EventHandler<Event>, Event, Object> listener = getActionListener();
		if (id != null) {
			listener.getAction().addMessage(id, message);
		} else {
			listener.getAction().setMessage(message);
		}
		return (EventHandler<? super MouseEvent>) listener;
	}

}
