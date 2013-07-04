package su.cs08.classifier.server.exceptions;

/**
 * The Class QueueIsFullException.
 */
public class QueueIsFullException extends Exception {
	
	/**
	 * Instantiates a new queue is full exception.
	 * 
	 * @param mssg the mssg
	 */
	public QueueIsFullException(String mssg) {
		super(mssg);
	}

}
