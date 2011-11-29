package org.jacp.javafx2.rcp.components;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import org.jacp.api.action.IAction;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

public class DemoFX2ComponentBreadCrumb extends AFX2Component {

	private BreadcrumbBar bar;

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	private HBox createBreadCrumbBar(Node node) {
		HBox box = new HBox();
		if (bar == null)
			bar = new BreadcrumbBar();
		bar.setPath("Home/test/test");
		HBox.setHgrow(bar, Priority.ALWAYS);
		box.setMaxWidth(Double.MAX_VALUE);
		box.setId("page-toolbar");
		box.getChildren().addAll(bar);
		return box;
	}

	private ToolBar createBreadCrumbToolBar(Node node) {
		ToolBar pageToolBar = new ToolBar();
		if (bar == null)
			bar = new BreadcrumbBar();
		bar.setPath("Home/test/test");
		Region spacer = new Region();
		HBox.setHgrow(bar, Priority.ALWAYS);
		pageToolBar.setId("page-toolbar");
		pageToolBar.getItems().addAll(spacer, bar);
		GridPane.setHgrow(pageToolBar, Priority.ALWAYS);
		return pageToolBar;
	}

	@Override
	public Node postHandleAction(Node node, IAction<Event, Object> action) {
		return createBreadCrumbBar(node);
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
