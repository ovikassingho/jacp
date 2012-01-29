package org.jacp.javafx2.rcp.components.optionPane;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The Class JACPoptionDialogV2.
 *
 * @author Patrick Symmangk
 */
public class JACPOptionPane extends VBox implements EventHandler<MouseEvent> {

	/** Drag offsets for window dragging. */
	private String message;

	/** The title. */
	private String title;

	/** The default button. */
	private JACPDialogButton defaultButton;

	/** The ok button. */
	private Button okButton;

	/** The cancel button. */
	private Button cancelButton;

	/** The yes button. */
	private Button yesButton;

	/** The no button. */
	private Button noButton;

	/** The bottom bar. */
	private HBox bottomBar;

	/** The BUTTO n_ size. */
	private final int BUTTON_SIZE = 74;

	/** The buttons. */
	private List<Button> buttons;

	/** The explanation. */
	private Text explanation;

	/** The title label. */
	private Label titleLabel;

	/** The top box. */
	private HBox topBox;

	/** The button styles. */
	private ObservableList<String> buttonStyles;

	/**
	 * Instantiates a new jAC poption dialog v2.
	 *
	 * @param title the title
	 * @param message the message
	 * @param defaultButton the default button
	 */
	public JACPOptionPane(final String title, final String message,
			JACPDialogButton defaultButton) {
		this.message = message;
		this.title = title;
		this.defaultButton = defaultButton;
		this.initDialog();
	}

	/**
	 * Inits the dialog.
	 */
	private void initDialog() {
		buttons = new ArrayList<Button>();

		setId("ProxyDialog");
		setSpacing(10);
		setMaxSize(430, USE_PREF_SIZE);
		// block mouse clicks
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent t) {
				t.consume();
			}
		});

		explanation = new Text(message);
		explanation.setWrappingWidth(400);
		explanation.getStyleClass().add("jacp-option-pane-explanation");

		BorderPane explPane = new BorderPane();
		VBox.setMargin(explPane, new Insets(5, 5, 5, 5));
		explPane.setCenter(explanation);
		BorderPane.setMargin(explanation, new Insets(5, 5, 5, 5));

		topBox = new HBox();
		// topBox.setAlignment(Pos.TOP_RIGHT);
		topBox.setAlignment(Pos.TOP_LEFT);
		Button defaultClose = new Button("x");
		defaultClose.setOnMouseClicked(this);
		defaultClose.setId("jacp-option-pane-close");
		topBox.getChildren().add(defaultClose);
		VBox.setVgrow(topBox, Priority.ALWAYS);
		
		getChildren().add(topBox);

		// create title
		titleLabel = new Label(title);
		titleLabel.getStyleClass().add("jacp-option-pane-title");
		titleLabel.setId("title");
		titleLabel.setMinHeight(22);
		titleLabel.setPrefHeight(22);
		titleLabel.setMaxWidth(Double.MAX_VALUE);
		titleLabel.setAlignment(Pos.CENTER);
		
		getChildren().add(titleLabel);

		bottomBar = new HBox(0);
		bottomBar.setAlignment(Pos.BASELINE_RIGHT);

		VBox.setMargin(bottomBar, new Insets(20, 5, 5, 5));
		getStyleClass().add("proxy-pane");
		setEffect(new DropShadow());
		getChildren().addAll(explPane, bottomBar);
	}

	/**
	 * Sets the on ok action.
	 *
	 * @param onOK the new on ok action
	 */
	public void setOnOkAction(EventHandler<ActionEvent> onOK) {
		if (okButton == null) {
			okButton = createButton(JACPDialogButton.OK);
		}
		this.setAction(okButton, onOK);
	}

	/**
	 * Sets the on cancel action.
	 *
	 * @param onCancel the new on cancel action
	 */
	public void setOnCancelAction(EventHandler<ActionEvent> onCancel) {
		if (cancelButton == null) {
			cancelButton = createButton(JACPDialogButton.CANCEL);
		}
		this.setAction(cancelButton, onCancel);
	}

	/**
	 * Sets the on yes action.
	 *
	 * @param onYes the new on yes action
	 */
	public void setOnYesAction(EventHandler<ActionEvent> onYes) {
		if (yesButton == null) {
			yesButton = createButton(JACPDialogButton.YES);
		}
		this.setAction(yesButton, onYes);
	}

	/**
	 * Sets the on no action.
	 *
	 * @param onNo the new on no action
	 */
	public void setOnNoAction(EventHandler<ActionEvent> onNo) {
		if (noButton == null) {
			noButton = createButton(JACPDialogButton.NO);
		}
		this.setAction(noButton, onNo);
	}

	/**
	 * Sets the action.
	 *
	 * @param button the button
	 * @param handler the handler
	 */
	private void setAction(Button button, EventHandler<ActionEvent> handler) {
		if (button != null)
			button.setOnAction(handler);
	}

	/**
	 * Creates the button.
	 *
	 * @param button the button
	 * @return the button
	 */
	private Button createButton(JACPDialogButton button) {
		Button but = new Button(button.getLabel());
		but.setId(button.getLabel().toLowerCase() + "Button");
		if (defaultButton != null && button.getId() == defaultButton.getId()) {
			but.setDefaultButton(true);
			but.requestFocus();
		}
		but.setMinWidth(BUTTON_SIZE);
		but.setPrefWidth(BUTTON_SIZE);
		but.setOnMouseClicked(this);
		HBox.setMargin(but, new Insets(0, 8, 0, 0));
		bottomBar.getChildren().add(but);
		buttons.add(but);
		but.getStyleClass().add("jacp-option-pane-button");
		return but;
	}

	/* (non-Javadoc)
	 * @see javafx.event.EventHandler#handle(javafx.event.Event)
	 */
	@Override
	public void handle(MouseEvent arg0) {
		JACPModalDialog.getInstance().hideModalMessage();
	}

	/**
	 * Show dialog.
	 */
	public void showDialog() {
		JACPModalDialog.getInstance().showModalMessage(this);
	}

	/**
	 * Show dialog.
	 *
	 * @param node the node
	 */
	public void showDialog(Node node) {
		JACPModalDialog.getInstance().showModalMessage(node);
	}

	/**
	 * Sets the default close button orientation.
	 *
	 * @param pos the new default close button orientation
	 */
	public void setDefaultCloseButtonOrientation(Pos pos) {
		topBox.setAlignment(pos);
	}
}
