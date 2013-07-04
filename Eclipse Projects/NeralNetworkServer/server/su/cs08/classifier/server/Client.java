/**
 * 
 */
package su.cs08.classifier.server;

import java.net.Socket;

// TODO: Auto-generated Javadoc
/**
 * Represents a client connected to our server.
 * 
 * @author plamKaTa
 */
public class Client {
	
	/** The socket. */
	private Socket socket;
	
	/** The listener. */
	private IClientListener listener;
	
	/** The sender. */
	private IClientSender sender;

	/**
	 * Create a new client on the specified socket.
	 * 
	 * @param socket the socket
	 */
	public Client(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * Gets the socket.
	 * 
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Gets the listener.
	 * 
	 * @return the listener
	 */
	public IClientListener getListener() {
		return listener;
	}

	/**
	 * Sets the listener.
	 * 
	 * @param listener the new listener
	 */
	public void setListener(IClientListener listener) {
		this.listener = listener;
		if (listener != null) {
			listener.setClient(this);
		}
	}

	/**
	 * Gets the sender.
	 * 
	 * @return the sender
	 */
	public IClientSender getSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 * 
	 * @param sender the new sender
	 */
	public void setSender(IClientSender sender) {
		this.sender = sender;
		if (sender != null) {
			sender.setClient(this);
		}
	}
	
}
