/**
 * 
 */
package su.cs08.classifier.server.exceptions;

/**
 * The Class MessageParsingException.
 * 
 * @author plamKaTa
 */
public class MessageParsingException extends Exception {

	/**
	 * Instantiates a new message parsing exception.
	 */
	public MessageParsingException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * The Constructor.
	 * 
	 * @param message the message
	 */
	public MessageParsingException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * The Constructor.
	 * 
	 * @param cause the cause
	 */
	public MessageParsingException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * The Constructor.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public MessageParsingException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
