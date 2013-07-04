/**
 * 
 */
package su.cs08.classifier.server.learning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import su.cs08.classifier.server.Client;
import su.cs08.classifier.server.IClientListener;
import su.cs08.classifier.server.MessageParser;
import su.cs08.classifier.server.NeuralNetworkServer;
import su.cs08.classifier.server.exceptions.MessageParsingException;
import su.cs08.classifier.server.exceptions.QueueIsFullException;
import weka.core.Instance;

/**
 * The Class LearningClientListener.
 * 
 * @author plamKaTa
 */
public class LearningClientListener extends Thread implements IClientListener {
	
	/** The client. */
	private Client client;
	
	/** The service. */
	private LearningService service;
	
	/** The reader. */
	private BufferedReader reader;
	
	/**
	 * Instantiates a new learning client listener.
	 * 
	 * @param client the client
	 * @param service the service
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public LearningClientListener(Client client, LearningService service) throws IOException {
		super();
		
		this.client = client;
		this.service = service;
		
		reader = new BufferedReader(new InputStreamReader(
				client.getSocket().getInputStream()));
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientListener#getClient()
	 */
	@Override
	public Client getClient() {
		return client;
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientListener#setClient(su.cs08.classifier.server.Client)
	 */
	@Override
	public void setClient(Client client) {
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientListener#startListening()
	 */
	@Override
	public void startListening() {
		start();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try { 
            while (!isInterrupted()) { 
                String message = reader.readLine(); 
                if (message == null) break; 
                if (message.equals("flush")) {
                	service.flush();
                } else {
					try {
						Instance instance = MessageParser.parseInstance(
								NeuralNetworkServer.getHeader(), new StringReader(message));
						service.addInstance(client, instance);
					} catch (QueueIsFullException e) {
						client.getSender().send(e.getMessage());
						e.printStackTrace();
					} catch (MessageParsingException e) {
						client.getSender().send(e.getMessage());
						e.printStackTrace();
					}
				}
            } 
        } catch (IOException ioex) { 
            // Problem reading from socket (broken connection) 
        } 

        // try closing the reader
        try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        // Communication is broken. Interrupt both listener and 
        // sender threads 
        client.getSender().interrupt();
        service.removeClient(client);
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientListener#wakeUp()
	 */
	@Override
	public synchronized void wakeUp() {
		notify();
	}

}
