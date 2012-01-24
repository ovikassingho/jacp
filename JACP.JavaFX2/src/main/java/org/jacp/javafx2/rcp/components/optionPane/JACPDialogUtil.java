package org.jacp.javafx2.rcp.components.optionPane;

/**
 * The Class JACPDialogUtil.
 * @author Patrick Symmangk
 *
 */
public class JACPDialogUtil {

	/**
	 * Creates the v2 dialog.
	 *
	 * @param title the title
	 * @param message the message
	 * @param defaultButton the default button
	 * @return the jAC poption dialog v2
	 */
	public static JACPOptionPane createOptionPane(final String title,
			final String message, JACPDialogButton defaultButton) {
		return new JACPOptionPane(title, message, defaultButton);
	}

	public static JACPOptionPane createOptionPane(final String title,
			final String message) {
		return new JACPOptionPane(title, message, null);
	}

}
