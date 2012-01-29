package org.jacp.javafx2.rcp.components.optionPane;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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

	/**
	 * Gets the single instance of JACPModalDialog.
	 *
	 * @return single instance of JACPModalDialog
	 */
	public static JACPModalDialog getInstance() {

		if (instance == null)
			throw new DialogNotInitializedException();
		return instance;
	}

	/**
	 * Inits the dialog.
	 *
	 * @param rootNode the root node
	 */
	public static void initDialog(final Node rootNode) {
		if (instance == null) {
			root = rootNode;
			instance = new JACPModalDialog();
			instance.setId("error-dimmer");
			Button test = new Button("close");
			test.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					GaussianBlur blur = (GaussianBlur) rootNode.getEffect();
					blur.setRadius(0.0);
					instance.setVisible(false);
				}

			});
		}

	}

	/**
	 * Show modal message.
	 *
	 * @param message the message
	 */
	public void showModalMessage(Node message) {
		this.getChildren().clear();
		this.getChildren().add(message);
		this.setOpacity(0);
		this.setVisible(true);
		this.setCache(true);
		((GaussianBlur) root.getEffect()).setRadius(MAX_BLUR);
		TimelineBuilder
				.create()
				.keyFrames(
						new KeyFrame(Duration.millis(250),
								new EventHandler<ActionEvent>() {
									public void handle(ActionEvent t) {
										setCache(false);
									}
								}, new KeyValue(this.opacityProperty(), 1,
										Interpolator.EASE_BOTH))).build()
				.play();
	}

	/**
	 * Hide any modal message that is shown.
	 */
	public void hideModalMessage() {
		this.setCache(true);
		TimelineBuilder
				.create()
				.keyFrames(
						new KeyFrame(Duration.millis(250),
								new EventHandler<ActionEvent>() {
									public void handle(ActionEvent t) {
										setCache(false);
										setVisible(false);
										getChildren().clear();
									}
								}, new KeyValue(this.opacityProperty(), 0,
										Interpolator.EASE_BOTH))).build()
				.play();
		((GaussianBlur) root.getEffect()).setRadius(0.0);
	}

}
