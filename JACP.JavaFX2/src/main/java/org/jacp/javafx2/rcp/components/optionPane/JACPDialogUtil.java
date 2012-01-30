package org.jacp.javafx2.rcp.components.optionPane;

/**
 * The Class JACPDialogUtil.
 * @author Patrick Symmangk
 *
 */
public class JACPDialogUtil {

	/**
	 * Creates the option pane.
	 *
	 * @param title the title
	 * @param message the message
	 * @return the jACP option pane
	 */
	public static JACPOptionPane createOptionPane(final String title,
			final String message) {
		return new JACPOptionPane(title, message);
	}

}
