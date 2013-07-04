/**
 * 
 */
package su.cs08.classifier.server.learning;

import java.util.ArrayList;
import java.util.List;

import su.cs08.classifier.server.Client;
import su.cs08.classifier.server.NeuralNetworkServer;
import su.cs08.classifier.server.algorithm.NeuralNetwork;
import su.cs08.classifier.server.exceptions.QueueIsFullException;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class will asynchronously allow queuing learning instances send by
 * teaching clients until a max number of instances is reached. When this
 * upper limit is reached the supervised learning algorithm will be started
 * on the instances received so far.
 * 
 * While the algorithm is running the queue should be able to receive instances,
 * but when max number of instances is reached execution of the algorithm should
 * be postponed until previous execution finishes. This includes a notification
 * to the teaching client that his request cannot be handled at the moment.
 * 
 * @author plamKaTa
 */
public class LearningService extends Thread {
	
	/** The learning clients. */
	private List<Client> learningClients;
	
	/** The querying clients. */
	private List<Client> queryingClients;
	
	/** The max learning instances. */
	private int maxLearningInstances = 100;
	
	/** The learning instances. */
	private Instances learningInstances;
	
	/** The classifier. */
	private NeuralNetwork classifier;
	
	/** The busy. */
	private boolean busy = false;
	
	/** The flushed. */
	private boolean flushed = false;
	
	/**
	 * Create an instance queue.
	 * 
	 * @param classifier - the classifier to be learned and queried; should already
	 * have been build and should contain the header data;
	 * @param executionSize - the size for the learning queue - when size is reached
	 * learning algorithm is started
	 */
	public LearningService(NeuralNetwork classifier, int executionSize) {
		super("InstanceQueue");
		
		if (classifier == null) {
			throw new IllegalArgumentException("Classifier is null");
		}
		if (classifier.getInstances() == null) {
			throw new IllegalArgumentException("Classifier have not been build.");
		}
		this.learningClients = new ArrayList<Client>();
		this.queryingClients = new ArrayList<Client>();
		this.classifier = classifier;
		learningInstances = new Instances(NeuralNetworkServer.getHeader());
		if (executionSize > 0) {
			maxLearningInstances = executionSize;
		}
	}
	
	/**
	 * Checks if is busy.
	 * 
	 * @return true, if is busy
	 */
	public boolean isBusy() {
		return busy;
	}
	
	/**
	 * Gets the classifier.
	 * 
	 * @return the classifier
	 */
	public Classifier getClassifier() {
		return classifier;
	}
	
	/**
	 * Gets the instances.
	 * 
	 * @return the instances
	 */
	public Instances getInstances() {
		return learningInstances;
	}
	
	/**
	 * Flush all instances currently in queue, no matter how many they are,
	 * triggering execution of the learning algorithm.
	 */
	public synchronized void flush() {
		if (!flushed) {
			flushed = true;
			notify();
		}
	}
	
	/**
	 * Adds a querying client to be waited when learning algorithm is running
	 * and notified when algorithm finishes execution.
	 * 
	 * @param client the querying client
	 */
	public synchronized void addQueryingClient(Client client) {
		if (!queryingClients.contains(client)) {
			queryingClients.add(client);
		}
	}
	
	/**
	 * Allows unregistering of a client from receiving error/notification messages
	 * about executions of the learning algorithm performed by this service.
	 * 
	 * @param client the client to be unregistered
	 */
	public synchronized void removeClient(Client client) {
		if (learningClients.contains(client)) {
			learningClients.remove(client);
		}
		if (queryingClients.contains(client)) {
			queryingClients.remove(client);
		}
	}
	
	/**
	 * Add an already evaluated learning instance to the learning service.
	 * When number of learning instances reaches maxLearningInstances number
	 * neural network's learning algorithm will be executed.
	 * <p>
	 * Note that many clients are allowed to provide learning instances and those
	 * that provided instances for the current execution of the learning algorithm
	 * should be informed about its success or failure.
	 * 
	 * @param client the client, who provides the learning instance
	 * @param learningInstance the learning instance itself
	 * 
	 * @throws QueueIsFullException if the space of the queue is exhausted wile the algorithm is
	 * running and no further algorithm execution is possible at the moment; hence after the
	 * current execution finishes a new execution will be triggered and space will be freed
	 */
	public synchronized void addInstance(Client client, Instance learningInstance) 
	throws QueueIsFullException {
		// fail if algorithm is running and learning queue is full
		if (learningInstances.numInstances() >= maxLearningInstances) {
			throw new QueueIsFullException(
					"Learning algorithm is running and there is no more space in queue." +
					"Plase try sending your request after a while. (errno=552)");
		}
		
		if (!learningClients.contains(client)) {
			learningClients.add(client);
		}
		learningInstances.add(learningInstance);
		notify();
	}
	
	/**
	 * Gets the instances for execution.
	 * 
	 * @return the instances for execution
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	private synchronized Instances getInstancesForExecution() 
	throws InterruptedException {
		while (learningInstances.numInstances() < maxLearningInstances) {
			wait();
			
			if (flushed) {
				flushed = false;
				break;
			}
		}
		Instances instances = new Instances(learningInstances);
		learningInstances.delete();
		return instances;
	}
	
	/**
	 * Send message to clients.
	 * 
	 * @param mssg the mssg
	 */
	private void sendMessageToClients(String mssg) {
		for (Client client : learningClients) {
			client.getSender().send(mssg);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			while (true) {
				Instances instances = getInstancesForExecution();
				busy = true;
				try {
					classifier.learnClassifier(instances);
					sendMessageToClients("Learning cycle finished successfully.");
				} catch (Exception e) {
					// TODO messages are not send to clients?
					sendMessageToClients("Learning cycle have failed. " +
							"See error log for more details. " +
							"Reason: " + e.getMessage());
					// log the server error for this specific learning
					e.printStackTrace();
				}
				learningClients.clear();
				busy = false;
				// notify querying clients
				for (Client client : queryingClients) {
					client.getListener().wakeUp();
					client.getSender().wakeUp();
				}
			}
		} catch (InterruptedException e) {
			// thread is interrupted - stop execution
		}
	}

}
