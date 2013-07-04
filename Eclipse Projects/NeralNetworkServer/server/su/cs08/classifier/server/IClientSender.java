package su.cs08.classifier.server;

/**
 * The Interface IClientSender is responsible for sending 
 * messages to the client in an asynchronous manner.
 */
public interface IClientSender {
	
	/**
	 * Start sending.
	 */
	public void startSending();
	
	/**
	 * Interrupt the sender.
	 */
	public void interrupt();
	
	/**
	 * Wake up the sender if it has been blocked.
	 */
	public void wakeUp();
	
	/**
	 * Send the given message to the client.
	 * The client sender is responsible for asynchronous management. 
	 * 
	 * @param mssg the mssg
	 */
	public void send(String mssg);
	
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
