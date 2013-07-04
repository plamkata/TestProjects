/**
 * 
 */
package com.startup.solynchron;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * @author plamKaTa
 *
 */
public class InitializationProgressException extends Exception {
	
	private static String INFO_MESSAGE = 
		"Solynchron Plug-in is not yet initialized!\n" + 
		"The requested operation will be executed after initialization.";

	/**
	 * 
	 */
	public InitializationProgressException() {
		super(INFO_MESSAGE);
	}

	/**
	 * @param message
	 */
	public InitializationProgressException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public InitializationProgressException(Throwable cause) {
		super(INFO_MESSAGE, cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InitializationProgressException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public void handle() {
		IWorkbenchWindow window = Activator.getDefault().
			getWorkbench().getActiveWorkbenchWindow();
		//Activator.log(this);
		try {
			MessageDialog.openInformation(
					window == null ? null : window.getShell(),
					"Solynchron Plug-in",
					getMessage()
					);
		} catch (Exception e) {
			// ignore
		}
	}

}
