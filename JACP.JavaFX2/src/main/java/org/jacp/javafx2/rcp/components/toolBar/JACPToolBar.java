package org.jacp.javafx2.rcp.components.toolBar;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * The Class JACPToolBar.
 * @author Patrick Symmangk
 *
 */
public class JACPToolBar extends ToolBar implements
		ChangeListener<Orientation>, ListChangeListener<Node> {

	/** The left buttons. */
	private HBox leftButtons;

	/** The right buttons. */
	private HBox rightButtons;

	/** The horizontal tool bar. */
	private HBox horizontalToolBar;

	/** The top buttons. */
	private VBox topButtons;

	/** The bottom buttons. */
	private VBox bottomButtons;

	/** The vertical tool bar. */
	private VBox verticalToolBar;

	private final double TOOLBAR_PADDING = 20;

	/**
	 * Instantiates a new jACP tool bar.
	 */
	public JACPToolBar() {
		super();
		getStyleClass().add("jacp-toolbar");
		orientationProperty().addListener(this);
		getItems().addListener(this);
		if (getOrientation() == Orientation.VERTICAL)
			initVerticalToolBar();
		else
			initHorizontalToolBar();
	}

	/**
	 * Adds the.
	 *
	 * @param node the node
	 */
	public void add(Node node) {

		if (getOrientation() == Orientation.HORIZONTAL) {
			HBox.setMargin(node, new Insets(0, 2, 0, 2));
			leftButtons.getChildren().add(node);
		} else {
			VBox.setMargin(node, new Insets(2, 0, 2, 0));
			topButtons.getChildren().add(node);
		}

	}

	/**
	 * Adds the on end.
	 *
	 * @param node the node
	 */
	public void addOnEnd(Node node) {
		if (getOrientation() == Orientation.HORIZONTAL) {
			HBox.setMargin(node, new Insets(0, 2, 0, 2));
			rightButtons.getChildren().add(node);
		} else {
			VBox.setMargin(node, new Insets(2, 0, 2, 0));
			bottomButtons.getChildren().add(node);
		}
		bind();
	}

	/**
	 * Inits the horizontal tool bar.
	 */
	private void initHorizontalToolBar() {
		/*
		* ---------------------------------------------------------------
		* | |left hand side buttons| |spacer| |right hand side buttons| |
		* ---------------------------------------------------------------
		*/
		clear();
		// the main box for the toolbar
		// holds the lefthand side and the right hand side buttons!
		// the buttons are separated by a spacer box, that fills the remaining
		// width
		horizontalToolBar = new HBox();
		// the place for the buttons on the left hand side
		leftButtons = new HBox();
		leftButtons.setAlignment(Pos.CENTER_LEFT);
		// the spacer that fills the remaining width between the buttons
		HBox spacer = new HBox();
		rightButtons = new HBox();
		rightButtons.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		horizontalToolBar.getChildren().addAll(leftButtons, spacer,
				rightButtons);
		getItems().add(0, horizontalToolBar);
	}

	/**
	 * Inits the vertical tool bar.
	 */
	private void initVerticalToolBar() {
		/*
		* ---------------------------------------------------------------
		* | |left hand side buttons| |spacer| |right hand side buttons| |
		* ---------------------------------------------------------------
		*/
		clear();
		// the main box for the toolbar
		// holds the lefthand side and the right hand side buttons!
		// the buttons are separated by a spacer box, that fills the remaining
		// width
		verticalToolBar = new VBox();

		// the place for the buttons on the left hand side
		topButtons = new VBox();
		topButtons.setAlignment(Pos.CENTER_LEFT);
		// the spacer that fills the remaining width between the buttons
		VBox spacer = new VBox();
		bottomButtons = new VBox();
		bottomButtons.setAlignment(Pos.CENTER_RIGHT);
		VBox.setVgrow(spacer, Priority.ALWAYS);
		verticalToolBar.getChildren().addAll(topButtons, spacer, bottomButtons);
		getItems().add(0, verticalToolBar);
	}

	/* (non-Javadoc)
	 * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void changed(ObservableValue<? extends Orientation> arg0,
			Orientation oldOrientation, Orientation newOrientation) {

		if (newOrientation == Orientation.VERTICAL) {
			initVerticalToolBar();
		} else {
			initHorizontalToolBar();
		}
		unbind();

	}

	/* (non-Javadoc)
	 * @see javafx.collections.ListChangeListener#onChanged(javafx.collections.ListChangeListener.Change)
	 */
	@Override
	public void onChanged(
			javafx.collections.ListChangeListener.Change<? extends Node> arg0) {
		if (getItems().size() > 1) {
			unbind();
		}

	}

	/**
	 * Bind.
	 */
	private void bind() {
		if (getOrientation() == Orientation.HORIZONTAL) {
			if (horizontalToolBar != null) {
				horizontalToolBar.maxWidthProperty().bind(
						widthProperty().subtract(TOOLBAR_PADDING));
				horizontalToolBar.minWidthProperty().bind(
						widthProperty().subtract(TOOLBAR_PADDING));
			}
		} else {
			if (verticalToolBar != null) {
				verticalToolBar.maxHeightProperty().bind(
						heightProperty().subtract(TOOLBAR_PADDING));
				verticalToolBar.minHeightProperty().bind(
						heightProperty().subtract(TOOLBAR_PADDING));
			}
		}
	}

	/**
	 * Unbind.
	 */
	private void unbind() {
		if (getOrientation() == Orientation.HORIZONTAL) {
			if (horizontalToolBar != null) {
				horizontalToolBar.maxWidthProperty().unbind();
				horizontalToolBar.minWidthProperty().unbind();
			}
		} else {
			if (verticalToolBar != null) {
				verticalToolBar.maxHeightProperty().unbind();
				verticalToolBar.minHeightProperty().unbind();
			}
		}
	}

	/**
	 * Clear.
	 */
	private void clear() {
		if (!getItems().isEmpty()) {
			Node node = getItems().get(0);
			if (node instanceof HBox || node instanceof VBox) {
				getItems().remove(node);
			}
		}
	}

}
