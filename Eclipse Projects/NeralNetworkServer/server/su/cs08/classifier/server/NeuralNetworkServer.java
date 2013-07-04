/**
 * 
 */
package su.cs08.classifier.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import su.cs08.classifier.server.algorithm.NeuralNetwork;
import su.cs08.classifier.server.learning.LearningClientListener;
import su.cs08.classifier.server.learning.LearningClientSender;
import su.cs08.classifier.server.learning.LearningService;
import su.cs08.classifier.server.querying.QueryingClient;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * This is the entry point for the neural network server. This class is responsible
 * for reading the initial configuration, building and configuring the neural network
 * itself and accepting client requests according to the client types. Querying requests
 * are accepted in separate thread on the port specified for querying. Learning requests
 * are accepted also in separate thread on a different port specified for learning.
 * 
 * @author plamKaTa
 */
public class NeuralNetworkServer {
	
	/** The Constant LEARNING_SERVER_PORT. */
	public static final int LEARNING_SERVER_PORT = 5524;
	
	/** The Constant QUERYING_SERVER_PORT. */
	public static final int QUERYING_SERVER_PORT = 6733;
	
	/** The Constant LEARNING_QUEUE_SIZE. */
	public static final int LEARNING_QUEUE_SIZE = 20;
	
	/** The learning server socket. */
	private static ServerSocket learningServerSocket;
	
	/** The querying server socket. */
	private static ServerSocket queryingServerSocket;
	
	/** The header. */
	private static Instances header;

	/**
	 * Gets the header.
	 * 
	 * @param cfgFile the path to the configuration file
	 * 
	 * @return the header
	 */
	public static Instances getHeader(String cfgFile) {
		if (header == null) {
			// preserve only header information here
			header = readHeader(cfgFile);
		}
		return new Instances(header);
	}
	
	public static Instances getHeader() {
		if (header == null) {
			throw new IllegalStateException("Header have not been initialised.");
		}
		return new Instances(header);
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		startListening(LEARNING_SERVER_PORT, false);
		startListening(QUERYING_SERVER_PORT, true);
		NeuralNetwork network = createNetwork(args);
		
		final LearningService service = new LearningService(network, LEARNING_QUEUE_SIZE);
		service.start();
		
		startLearning(service);
		startQuerying(service);
	}

	/**
	 * Start querying.
	 * 
	 * @param service the service
	 */
	private static void startQuerying(final LearningService service) {
		Thread queryingThread = new Thread("QueryingServer") {
			
			@Override
			public void run() {
				// start listening for client requests
				while (!isInterrupted()) { 
		            try { 
		                Socket socket = queryingServerSocket.accept(); 
						startClientSession(socket, "query", service);
		            } catch (IOException ioe) { 
		                ioe.printStackTrace();
		            } 
		        }
			}
		};
		queryingThread.start();
	}

	/**
	 * Start learning.
	 * 
	 * @param service the service
	 */
	private static void startLearning(final LearningService service) {
		Thread learningThread = new Thread("LearningServer") {
			
			@Override
			public void run() {
				// start listening for client requests
				while (!isInterrupted()) { 
		            try { 
		                Socket socket = learningServerSocket.accept(); 
						startClientSession(socket, "learn", service);
		            } catch (IOException ioe) { 
		                ioe.printStackTrace();
		            } 
		        }
			}
		};
		learningThread.start();
	}

	/**
	 * Starts listening at the specified port.
	 * 
	 * @param port the port
	 * @param querying the querying
	 */
	private static void startListening(int port, boolean querying) {
		String action = "listening";
		try {
			if (querying) {
				action = "querying";
				queryingServerSocket = new ServerSocket(port);
			} else {
				action = "learning";
				learningServerSocket = new ServerSocket(port);
			}
			System.out.println("Neural network server started " + action + " on port: " + port);
		} catch (IOException e) {
			System.out.println("Cannot start " + action + " on port: " + port);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() {
        closerServerSocket(NeuralNetworkServer.queryingServerSocket);
        closerServerSocket(NeuralNetworkServer.learningServerSocket);
    }

	/**
	 * Closer server socket.
	 * 
	 * @param serverSocket the server socket
	 */
	private void closerServerSocket(ServerSocket serverSocket) {
		if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverSocket = null;
        }
	}

	/**
	 * Creates a neural network and sets its options by the given arguments.
	 * Then tries to read header information from config.arff and if successful
	 * builds the classifier so that it is ready to be iteratively learned.
	 * 
	 * @param options options for the neural network
	 * 
	 * @return the neural network itself
	 */
	private static NeuralNetwork createNetwork(String[] options) {
		NeuralNetwork network = new NeuralNetwork();
		try {
			network.setOptions(options);
		} catch (Exception e) {
			// ignore
		}
		
		// build classifier with header data
		try {
			String cfgPath = getOption("arff", options);
			if (cfgPath == null) {
				System.out.println("Please specify neural network config file: -arff=<FILE_PATH>." );
				System.exit(-1);
			}
			network.buildClassifier(getHeader(cfgPath));
		} catch (Exception e) {
			System.out.println("Classifier cannot be build.");
			e.printStackTrace();
			System.exit(-1);
		}
		return network;
	}

	/**
	 * Tries to read the header elements for the classifier from config.arff.
	 * Header elements should specify the properties of the classifier and the
	 * output class property.
	 * <p>
	 * If the file is not found or reading produces an error reason will be logged
	 * and execution will b terminated.
	 * 
	 * @param cfgFile the path for the configuration file
	 * 
	 * @return the header instances
	 */
	private static Instances readHeader(String cfgFile) {
		Instances header = null;
		try {
			BufferedReader headReader = new BufferedReader(new FileReader(cfgFile));
			header = new Instances(headReader);
			header.setClassIndex(header.numAttributes() - 1);
		} catch (FileNotFoundException e) {
			// configuration file is not found
			System.out.println("Classifier's configuration file is not found or cannot be read.");
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Classifier's configuration file cannot be read.");
			e.printStackTrace();
			System.exit(-1);
		}
		return header;
	}
	
	/**
	 * Starts a client session according to the client type.
	 * 
	 * @param socket the socket to read client messages from
	 * @param clientType the type of the client: query, learn
	 * @param service the learning service
	 * 
	 * @throws IOException if there is a problem reading or writing to the socket
	 */
	private static void startClientSession(Socket socket, String clientType, LearningService service) 
	throws IOException {
		Client client = new Client(socket);
		
		IClientListener listener = null;
		IClientSender sender = null;
		if (clientType.startsWith("learn")) {
			listener = new LearningClientListener(client, service);
			sender = new LearningClientSender(client, service);
		} else if (clientType.startsWith("query")) {
			// start querying thread handler 
			QueryingClient qryClient = new QueryingClient(client, service);
			listener = qryClient;
			sender = qryClient;
		}
		
		if (listener != null && sender != null) {
			// we may register client handler threads in order to manage them (interrupt)
			// when system resources are exhausted
			client.setListener(listener);
			client.setSender(sender);
			
			listener.startListening();
			sender.startSending();
			
			sendWellkomeMessage(client, clientType);
		}
	}

	/**
	 * Send wellkome message.
	 * 
	 * @param client the client
	 * @param clientType the client type
	 */
	private static void sendWellkomeMessage(Client client, String clientType) {
		String welcomeMessage = "Please specify following attributes: \n";
		
		int numAttributes = header.numAttributes();
		if (clientType.equals("query")) numAttributes--;
		
		StringBuffer attributes = new StringBuffer();
		for (int attrIndex = 0; attrIndex < numAttributes; attrIndex++) {
			Attribute attribute = header.attribute(attrIndex);
			String attrName = attribute.toString().replaceFirst("@attribute", "");
			if (attrIndex == numAttributes - 1) {
				attributes.append(attrName);
			} else {
				attributes.append(attrName + ",");
			}
		}
		welcomeMessage += attributes.toString();
		client.getSender().send(welcomeMessage);
	}
	
	private static String getOption(String optName, String[] options) {
		String optValue = null;
		for (int num = 0; num < options.length; num++) {
			if (options[num].contains(optName)) {
				int index = options[num].indexOf('=');
				if (index + 1 < options[num].length()) {
					optValue = options[num].substring(index + 1);
					break;
				}
			}
		}
		return optValue;
	}

}
