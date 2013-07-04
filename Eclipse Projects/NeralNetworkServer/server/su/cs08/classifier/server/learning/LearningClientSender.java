/**
 * 
 */
package su.cs08.classifier.server.learning;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import su.cs08.classifier.server.Client;
import su.cs08.classifier.server.IClientSender;

/**
 * For performance reasons, in order to process requests faster,
 * we encapsulate writing messages to the client in a separate
 * thread per learning client.
 * 
 * @author plamKaTa
 */
public class LearningClientSender extends Thread implements IClientSender {
	
	/** The message queue. */
	private List<String> messageQueue;

	/** The client. */
	private Client client;
	
	/** The service. */
	private LearningService service;

    /** The writer. */
    private PrintWriter writer;
	
	/**
	 * The Constructor.
	 * 
	 * @param client the client
	 * @param queue the queue
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public LearningClientSender(Client client, LearningService queue) throws IOException {
		this.client = client;
		messageQueue = new ArrayList<String>();
		service = queue;
		
		writer = new PrintWriter(new OutputStreamWriter(
				client.getSocket().getOutputStream()));
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientSender#getClient()
	 */
	@Override
	public Client getClient() {
		return client;
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientSender#send(java.lang.String)
	 */
	@Override
	public synchronized void send(String mssg) {
		messageQueue.add(mssg);
		notify();
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientSender#setClient(su.cs08.classifier.server.Client)
	 */
	@Override
	public void setClient(Client client) {
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientSender#startSending()
	 */
	@Override
	public void startSending() {
		start();
	}

	/**
	 * Gets the next message from queue.
	 * 
	 * @return and deletes the next message from the message
	 * queue. If the queue is empty, falls in sleep until
	 * notified for message arrival by sendMessage method.
	 * 
	 * @throws InterruptedException the interrupted exception
	 */ 
    private synchronized String getNextMessageFromQueue() 
    throws InterruptedException { 
        while (messageQueue.size() == 0) wait(); 

        return messageQueue.remove(0); 
    } 

    /**
     * Sends given message to the client's socket.
     * 
     * @param aMessage the a message
     */ 
    private void sendMessageToClient(String aMessage) { 
        writer.println(aMessage); 
        writer.flush(); 
    } 
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try { 
            while (!isInterrupted()) { 
                String message = getNextMessageFromQueue(); 
                sendMessageToClient(message); 
            }
        } catch (Exception e) { 
            // Communication problem - close the channel
        }
        
        // try closing the stream
        writer.close();
 
        // Communication is broken. Interrupt both listener 
        // and sender threads 
        client.getListener().interrupt();
        service.removeClient(client);
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientSender#wakeUp()
	 */
	@Override
	public synchronized void wakeUp() {
		notify();
	}

}
