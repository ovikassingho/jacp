package org.jacp.javafx2.rcp.components.optionPane;

/**
 * The Class JACPDialogUtil.
 * @author Patrick Symmangk
 *
 */
public class JACPDialogUtil {

	/**
	 * Creates the dialog.
	 *
	 * @param title the title
	 * @param message the message
	 * @param defaultButton the default button
	 * @param buttons the buttons
	 * @return the jACP option dialog
	 */
	public static JACPOptionDialog createDialog(final String title,
			final String message, JACPDialogButton defaultButton,
			JACPDialogButton... buttons) {
		return new JACPOptionDialog(title, message, defaultButton, buttons);

	}

	/**
	 * Creates the v2 dialog.
	 *
	 * @param title the title
	 * @param message the message
	 * @param defaultButton the default button
	 * @return the jAC poption dialog v2
	 */
	public static JACPoptionDialogV2 createV2Dialog(final String title,
			final String message, JACPDialogButton defaultButton) {
		return new JACPoptionDialogV2(title, message, defaultButton);
	}

}
