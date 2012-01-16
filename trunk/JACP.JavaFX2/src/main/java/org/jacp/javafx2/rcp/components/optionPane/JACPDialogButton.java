package org.jacp.javafx2.rcp.components.optionPane;

/**
 * The Enum JACPDialogButton.
 *
 * @author psy
 */
public enum JACPDialogButton {

	/** The OK. */
	OK(1, "OK"), /** The CANCEL. */
	CANCEL(2, "Cancel"), /** The YES. */
	YES(3, "Yes"), /** The NO. */
	NO(4, "No");

	/** The id. */
	private int id;

	/** The label. */
	private String label;

	/**
	 * Instantiates a new jACP dialog button.
	 *
	 * @param id the id
	 * @param label the label
	 */
	JACPDialogButton(int id, String label) {
		this.id = id;
		this.label = label;
	}

	/**
	 * From id.
	 *
	 * @param id the id
	 * @return the jACP dialog button
	 */
	public static JACPDialogButton fromId(final int id) {
		JACPDialogButton currentButton = JACPDialogButton.OK;
		switch (id) {
		case 1:
			currentButton = JACPDialogButton.OK;
			break;
		case 2:
			currentButton = JACPDialogButton.CANCEL;
			break;
		case 3:
			currentButton = JACPDialogButton.YES;
			break;
		case 4:
			currentButton = JACPDialogButton.NO;
			break;
		default:
			currentButton = JACPDialogButton.OK;
			break;
		}
		return currentButton;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 *
	 * @param label the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

}
