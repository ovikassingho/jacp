package org.jacp.javafx2.rcp.components.optionPane;

/**
 * The Class DialogNotInitializedException.
 *
 * @author Patrick Symmangk
 */
public class DialogNotInitializedException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -343178301862250777L;

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return "Initialize JACPDialog before using it -> Call initJACPDialog()";
	}

}
