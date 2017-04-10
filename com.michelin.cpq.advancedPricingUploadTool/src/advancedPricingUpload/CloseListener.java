package advancedPricingUpload;

import java.awt.event.*;
import javax.swing.*;

/**
 * This class handles the close window operation
 * @author N556488 - Steven Draugel
 *
 */
public class CloseListener extends WindowAdapter {
	private String message;
	private String title;
	private Action closeAction;
	private boolean disposeOnClose = false;

	/**
	 *  Display a confirmation message upon closing the application
	 *
	 *  @param message - the confirmation message
	 *  @param title - 	 the text used for the title bar of the dialog
	 */
	public CloseListener(String message, String title) {
		this(message, title, null);
	}

	/**
	 *  Specify an Action to be invoked upon closing the application.
	 *
	 *  @param closeAction - the Action to be invoked upon closing the application
	 */
	public CloseListener(Action closeAction) {
		this(null, null, closeAction);
	}

	/**
	 *  Display a confirmation message upon closing the application and
	 *	and invoke a closing action.
	 *
	 *  @param message - 	 the confirmation message
	 *  @param title -       the text used for the title bar of the dialog
	 *  @param closeAction - the Action to be invoked upon closing the application
	 */
	public CloseListener(String message, String title, Action closeAction) {
		this.message = message;
		this.title = title;
		this.closeAction = closeAction;
	}

	/**
	 *  Set the default close operation to be dispose on close. Typically would
	 *  be used in a multi frame application. This way only when the last frame
	 *  is closed will the JVM be shut down.
	 *
	 *  @param disposeOnClose - true for DISPOSE_ON_CLOSE, false for EXIT_ON_CLOSE
	 */
	public void setDisposeOnClose(boolean disposeOnClose) {
		this.disposeOnClose = disposeOnClose;
	}

	/**
	 * Handles what to do if the window close button is pressed
	 *
	 * @param e - the window event
	 * @Override
	 */
	public void windowClosing(WindowEvent e) {
		JFrame frame = (JFrame)e.getComponent();

		//  Confirm with the user to close the frame
		if (! confirmWindowClosing(frame)) {
			frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
			return;
		}

		//  Set the close option. Setting it here allows the close action to
		//  prevent the window from closing by resetting the close option to
		//  DO_NOTHING_ON_CLOSE.
		if (disposeOnClose) {
			frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		}
		else {
			frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		}

		//  Confirmation received, so do the exit processing
		if (closeAction != null) {
			ActionEvent ae = new ActionEvent(frame, ActionEvent.ACTION_PERFORMED, "");
			closeAction.actionPerformed( ae );
		}
	}

	/**
	 * Sends the user a popup window after that press the close window button. Gives them the option
	 * to continue closing the program or instead to go back to the program.
	 * 
	 * @param frame - the window the popup will be contained in
	 * @return - a true or false boolean (close or keep window open)
	 */
	private boolean confirmWindowClosing(JFrame frame) {
		if (message == null) return true;

		int result = JOptionPane.showConfirmDialog(
			frame,
			message,
			title,
			JOptionPane.YES_NO_OPTION);

		return (result == JOptionPane.YES_OPTION) ? true : false;
	}
}
