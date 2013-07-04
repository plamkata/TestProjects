package su.cs08.classifier.server;

/**
 * The listener interface for receiving client requests.
 * The class that is interested in processing a client
 * requests implements this interface, and the object created
 * with that class is registered with a component using the
 * component's method. When the client write occurs, that 
 * object's appropriate method is invoked.
 * 
 * @see IClientEvent
 */
public interface IClientListener {
	
	/**
	 * Start listening.
	 */
	public void startListening();
	
	/**
	 * Interrupt the current listener.
	 */
	public void interrupt();
	
	/**
	 * Wake up the current listener when it has been blocked.
	 */
	public void wakeUp();
	
	/**
	 * Gets the client.
	 * 
	 * @return the client
	 */
	public Client getClient();
	
	/**
	 * Sets the client.
	 * 
	 * @param client the new client
	 */
	public void setClient(Client client);
	
}
