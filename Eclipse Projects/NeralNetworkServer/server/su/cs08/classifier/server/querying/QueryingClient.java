/**
 * 
 */
package su.cs08.classifier.server.querying;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;

import su.cs08.classifier.server.Client;
import su.cs08.classifier.server.IClientListener;
import su.cs08.classifier.server.IClientSender;
import su.cs08.classifier.server.MessageParser;
import su.cs08.classifier.server.NeuralNetworkServer;
import su.cs08.classifier.server.exceptions.MessageParsingException;
import su.cs08.classifier.server.learning.LearningService;
import weka.core.Instance;

/**
 * This is the querying thread client handler. When the client identifies
 * itself as a querying client a client session thread is started and it is
 * ready to handle requests.
 * <p>
 * A querying request specifies comma separated values for the attributes
 * and should return all possible class values with the probability of matching
 * the given instance to the appropriate class.
 * 
 * @author plamKaTa
 */
public class QueryingClient extends Thread implements IClientListener, IClientSender {
	
	/** The client. */
	private Client client;
	
	/** The service. */
	private LearningService service;
	
	/** The reader. */
	private BufferedReader reader;
	
	/** The writer. */
	private PrintWriter writer;

	/**
	 * The Constructor.
	 * 
	 * @param client the client
	 * @param service the service
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public QueryingClient(Client client, LearningService service) throws IOException {
		super();
		this.client = client;
		this.service = service;
		
		if (service != null) {
			service.addQueryingClient(client);
		}
		
		reader = new BufferedReader(new InputStreamReader(
				client.getSocket().getInputStream()));
		
		writer = new PrintWriter(new OutputStreamWriter(
				client.getSocket().getOutputStream()));
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientListener#getClient()
	 */
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
	 * @see su.cs08.classifier.server.IClientSender#startSending()
	 */
	@Override
	public void startSending() {
		// Thread should have been already started with startListening
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifier.server.IClientSender#send(java.lang.String)
	 */
	@Override
	public void send(String mssg) {
		writer.println(mssg);
		writer.flush();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				String requestMessage = reader.readLine();
				if (requestMessage == null) break;

				String responseMessage = "";
				try {
					Instance instance = MessageParser.parseInstance(
							NeuralNetworkServer.getHeader(), new StringReader(requestMessage + ",?"));
					// wait until learning finishes
					while (service.isBusy()) wait();
					// note that distributions are calculated in this threads memory 
					// according to algorithm modification; if values were stored in each neural node
					// then we would've had serious synch. problems and we would've needed each querying 
					// client to wait for the previous request to be finished. 
					double[] probabilities = service.getClassifier().distributionForInstance(instance);
					
					// form response message containing probabilities of output classes
					StringBuffer responseBuff = new StringBuffer(instance.classAttribute().name() + ": ");
					for (int classNum = 0; classNum < instance.numClasses(); classNum++) {
						responseBuff.append(instance.classAttribute().value(classNum)
								+ "(" + probabilities[classNum] * 100 + "%) ");
					}
					
					responseMessage = responseBuff.toString();
				} catch (MessageParsingException e) {
					responseMessage = e.getMessage();
					e.printStackTrace();
				} catch (Exception e) {
					responseMessage = e.getMessage();
					e.printStackTrace();
				}
				
				send(responseMessage);
			} catch (IOException e) {
				// An error occurred - probably when trying to read/write from socket
				e.printStackTrace();
				break;
			}
		}
		
		finishConversation();
	}

	/**
	 * Finish conversation.
	 */
	private void finishConversation() {
		try {
			reader.close();
		} catch (IOException e) {
			// ignore - reader will be closed after finishing the execution of this thread
		}
		// try closing the stream
		writer.close();
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
