/**
 * 
 */
package su.cs08.classifier.server.algorithm;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.UnsupportedAttributeTypeException;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToBinary;

/**
 * @author plamKaTa
 * 
 */
public class NeuralNetwork extends Classifier implements OptionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8730624609300819163L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			System.out.println(Evaluation.evaluateModel(new NeuralNetwork(), args));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	/** 
	 * The training instances. 
	 */
	private Instances instances;

	/** The output nodes. */
	private List<OutputNode> outputs;

	/** The input nodes. */
	private List<InputNode> inputs;

	/** All the inner nodes in the network. */
	private List<NeuralNode> innerNodes;

	/** The number of classes. */
	private int numClasses = 0;

	/** The number of attributes. */
	private int numAttributes = 0;

	/** The next id number available for default naming. */
	private int nextId;

	/** The actual random number generator. */
	private Random random;

	/** The actual filter. */
	private NominalToBinary nominalToBinaryFilter;

	private NetworkConfig configuration;

	/**
	 * The constructor.
	 */
	public NeuralNetwork() {
		instances = null;

		numAttributes = 0;
		numClasses = 0;
		
		outputs = new ArrayList<OutputNode>();
		inputs = new ArrayList<InputNode>();
		innerNodes = new ArrayList<NeuralNode>();
		
		nextId = 0;
		random = null;
		nominalToBinaryFilter = new NominalToBinary();
		
		configuration = new NetworkConfig();
	}
	
	public Instances getInstances() {
		return instances;
	}

	/**
	 * Call this function to place a node into the network list.
	 * 
	 * @param node
	 *            The node to place in the list.
	 */
	private void addNode(NeuralNode node) {
		innerNodes.add(node);
	}

	/**
	 * Call this function to remove the passed node from the list. This will
	 * only remove the node if it is in the neural nodes list.
	 * 
	 * @param node The neuralConnection to remove.
	 * @return True if removed false if not (because it wasn't there).
	 */
	private boolean removeNode(NeuralNode node) {
		return innerNodes.remove(node);
	}

	/**
	 * this will reset all the nodes in the network.
	 * @param nodeValues TODO
	 */
	private void reset(Hashtable<NeuralNode, Double> nodeValues) {
		for (OutputNode output : outputs) {
			output.reset(nodeValues);
		}
	}

	/**
	 * This will calculate values for all output elements of the network. 
	 * Hence all network elements' values will be calculated (including hidden layer).
	 * @param nodeValues calculated output values for nodes should be stored in this collection 
	 */
	private void calculateOutputs(Instance instance, Hashtable<NeuralNode, Double> nodeValues) {
		for (OutputNode output : outputs) {
			output.getValue(instance, nodeValues);
		}
	}

	/**
	 * This will cause the error values to be calculated for all nodes. Note
	 * that the m_currentInstance is used to calculate these values. Also the
	 * output values should have been calculated first.
	 * 
	 * @return The squared error.
	 */
	private double calculateErrors(Instance instance, Hashtable<NeuralNode, Double> nodeValues) throws Exception {
		double ret = 0;
		
		// calculate errors for the network
		for (InputNode input : inputs) {
			input.getError(instance, nodeValues);
		}
		
		for (OutputNode output : outputs) {
			double error = output.getError(instance, nodeValues);
			ret += error * error;
		}
		return ret;

	}

	/**
	 * This will cause the weight values to be updated based on the learning
	 * rate, momentum and the errors that have been calculated for each node.
	 * @param nodeValues the values af the nodes calculated so far
	 * @param learnRate
	 *            The learning rate to update with.
	 * @param momentum
	 *            The momentum to update with.
	 */
	private void updateWeights(Hashtable<NeuralNode, Double> nodeValues, double learnRate, double momentum) {
		for (OutputNode output : outputs) {
			output.updateWeights(null, nodeValues, learnRate, momentum);
		}
	}

	/**
	 * This creates the required input units.
	 */
	private void createInputLayer(Instances instances) throws Exception {
		inputs = new ArrayList<InputNode>();
		int now = 0;
		for (int attrNum = 0; attrNum < numAttributes + 1; attrNum++) {
			if (instances.classIndex() != attrNum) {
				InputNode input = new InputNode(instances.attribute(attrNum).name(), attrNum);
				inputs.add(attrNum - now, input);
			} else {
				now = 1;
			}
		}

	}

	/**
	 * This creates the required output units.
	 */
	private void createOutputLayer(Instances instances) throws Exception {
		outputs.clear();
		for (int classIndex = 0; classIndex < numClasses; classIndex++) {
			String name;
			if (configuration.isNumeric()) {
				name = instances.classAttribute().name();
			} else {
				name = instances.classAttribute().value(classIndex);
			}
			OutputNode output = new OutputNode(name, classIndex, configuration);
			
			InnerNode temp = new InnerNode(String.valueOf(nextId), random);
			nextId++;
			addNode(temp);
			NeuralNode.connect(temp, output);
			
			outputs.add(output);
		}

	}

	/**
	 * Call this function to automatically generate the hidden units
	 */
	private void createHiddenLayer() {
		int layers = 1;
		
		int hiddenLayerSize = 0; 
		int prevLayerSize = numAttributes;
		
		for (int layer = 0; layer < layers; layer++) {
			hiddenLayerSize = (numAttributes + numClasses) / 2;
			
			for (int nob = 0; nob < hiddenLayerSize; nob++) {
				InnerNode temp = new InnerNode(String.valueOf(nextId), random);
				nextId++;
				addNode(temp);
				if (layer > 0) {
					// then do connections
					for (int noc = innerNodes.size() - nob - 1 - prevLayerSize; noc < innerNodes.size() - nob - 1; noc++) {
						NeuralNode.connect(innerNodes.get(noc), temp);
					}
				}
			}
			
			prevLayerSize = hiddenLayerSize;
		}

		if (hiddenLayerSize == 0) {
			for (InputNode input : inputs) {
				for (NeuralNode node : innerNodes) {
					NeuralNode.connect(input, node);
				}
			}
		} else {
			for (NeuralNode input : inputs) {
				for (int nob = numClasses; nob < numClasses + hiddenLayerSize; nob++) {
					NeuralNode.connect(input, innerNodes.get(nob));
				}
			}
			for (int noa = innerNodes.size() - prevLayerSize; noa < innerNodes.size(); noa++) {
				for (int nob = 0; nob < numClasses; nob++) {
					NeuralNode.connect(innerNodes.get(noa), innerNodes.get(nob));
				}
			}
		}

	}

	/**
	 * Call this function to build and train a neural network for the training
	 * data provided.
	 * 
	 * @param instances
	 *            The training data.
	 * @exception Exception
	 *                if can't build classification properly.
	 */
	public void buildClassifier(Instances instances) throws Exception {
		if (instances.checkForStringAttributes()) {
			throw new UnsupportedAttributeTypeException("Cannot handle string attributes!");
		} 
		if (instances.numInstances() == 0) {
			//throw new IllegalArgumentException("No training instances.");
		}
		if (instances.numAttributes() == 1) {
			System.err.println("No training instances found - only classes.");
			throw new IllegalArgumentException("No training instances found - only classes.");
		}
		
		instances = initClassifier(instances);

		createInputLayer(instances);
		createOutputLayer(instances);
		createHiddenLayer();

		// connections done.
		// learnClassifier(instances);
	}
	
	/**
	 * @param i
	 * @throws Exception
	 */
	private Instances initClassifier(Instances i) throws Exception {
		instances = null;

		//nextId = 0;
		
		instances = new Instances(i);
		instances.deleteWithMissingClass();
		if (instances.numInstances() == 0) {
			//instances = null;
			//throw new IllegalArgumentException("All class values missing.");
		}
		random = new Random(configuration.getRandomSeed());
		instances.randomize(random);

		if (configuration.getNominalToBinaryFilter()) {
			nominalToBinaryFilter = new NominalToBinary();
			nominalToBinaryFilter.setInputFormat(instances);
			instances = Filter.useFilter(instances, nominalToBinaryFilter);
		}
		numAttributes = instances.numAttributes() - 1;
		numClasses = instances.numClasses();

		configuration.setClassType(instances);
		
		return instances;
	}

	/**
	 * @param i
	 * @throws Exception
	 */
	public void learnClassifier(Instances i) throws Exception {
		i = initClassifier(i);
		
		double estimation = 0;
		double origRate = configuration.getLearningRate(); // only used for when reset
		
		double totalWeight = calculateTotalWeight(i, 0);

		Hashtable<NeuralNode, Double> nodeValues = new Hashtable<NeuralNode, Double>();
		for (int noa = 1; noa < configuration.getTrainingTime() + 1; noa++) {
			estimation = 0;
			for (int nob = 0; nob < i.numInstances(); nob++) {
				Instance instance = i.instance(nob);

				if (!instance.classIsMissing()) {
					// this is where the network is being trained 
					reset(nodeValues);
					calculateOutputs(instance, nodeValues);
					
					double tempRate = configuration.getLearningRate() * instance.weight();
					double errors = calculateErrors(instance, nodeValues);
					estimation += (errors / i.numClasses()) * instance.weight();
					
					updateWeights(nodeValues, tempRate, configuration.getMomentum());
				}
			}
			
			// after the current training check for overflows and potentially restart the training
			estimation /= totalWeight;
			if (Double.isInfinite(estimation) || Double.isNaN(estimation)) {
				if (!configuration.isReset()) {
					instances = null;
					throw new Exception("Network cannot train. Try restarting with a"
									+ " smaller learning rate.");
				} else {
					// reset the network if possible
					if (configuration.getLearningRate() <= Utils.SMALL)
						throw new Exception(
								"Learning rate got too small ("	+ 
								configuration.getLearningRate() + " <= " + Utils.SMALL + 
								")!");
					
					// change the learning rate and rebuild
					configuration.setLearningRate(configuration.getLearningRate() / 2);
					learnClassifier(i);
					configuration.setLearningRate(origRate);
					instances = new Instances(i, 0);
					return;
				}
			}
		}
		instances = new Instances(i, 0);
	}

	/**
	 * @param numInVal
	 * @return
	 */
	private double calculateTotalWeight(Instances instances, int numInVal) {
		double totalWeight = 0;
		for (int noa = numInVal; noa < instances.numInstances(); noa++) {
			if (!instances.instance(noa).classIsMissing()) {
				totalWeight += instances.instance(noa).weight();
			}
		}
		return totalWeight;
	}

	/**
	 * Call this function to predict the class of an instance once a
	 * classification model has been built with the buildClassifier call.
	 * 
	 * @param instance
	 *            The instance to classify.
	 * @return A double array filled with the probabilities of each class type.
	 * @exception Exception
	 *                if can't classify instance.
	 */
	public double[] distributionForInstance(Instance instance) throws Exception {
		if (configuration.getNominalToBinaryFilter()) {
			nominalToBinaryFilter.input(instance);
			instance = nominalToBinaryFilter.output();
		}

		if (configuration.isNormalizeAttributes()) {
			for (int noa = 0; noa < instance.numAttributes(); noa++) {
				if (noa != instance.classIndex()) {
					double oldValue = instance.value(noa);
					
					double value = configuration.normalizeAttribute(noa, oldValue);
					instance.setValue(noa, value);
				}
			}
		}
		
		// since node values are stored in current thread memory and not in neural network's instance memory, 
		// hence that this code is enabled for concurrent execution - i.e. many threads will be able to
		// query the network for instance classification
		Hashtable<NeuralNode, Double> nodeValues = new Hashtable<NeuralNode, Double>();
		reset(nodeValues);

		// since all the output values are needed.
		// They are calculated manually here and the values collected.
		double[] theArray = new double[numClasses];
		for (int classIndex = 0; classIndex < numClasses; classIndex++) {
			theArray[classIndex] = outputs.get(classIndex).getValue(instance, nodeValues);
		}
		if (instance.classAttribute().isNumeric()) {
			return theArray;
		}

		// now normalize the array
		double count = 0;
		for (int noa = 0; noa < numClasses; noa++) {
			count += theArray[noa];
		}
		if (count <= 0) {
			return null;
		}
		for (int noa = 0; noa < numClasses; noa++) {
			theArray[noa] /= count;
		}
		return theArray;
	}
	
	@Override
	public Enumeration listOptions() {
		return configuration.listOptions();
	}
	
	@Override
	public String[] getOptions() {
		return configuration.getOptions();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		configuration.setOptions(options);
	}
	
	/**
	 * @return string describing the model.
	 */
	public String toString() {
		StringBuffer model = new StringBuffer();
		for (NeuralNode node : innerNodes) {
			model.append("Node " + node.getId() + "\n    Inputs    Weights\n");
			model.append("    Threshold    " + node.getWeight(-1) + "\n");
			List<NeuralNode> inputs = node.getInputElements();
			for (int inNum = 0; inNum < inputs.size(); inNum++) {
				NeuralNode input = inputs.get(inNum);
				if (input instanceof InputNode) {
					model.append("    Attrib "
							+ instances.attribute(
									((InputNode) input).getAttrIndex()).name() + "    " + node.getWeight(inNum) + "\n");
				} else {
					model.append("    Node " + input.getId() + "    " + node.getWeight(inNum) + "\n");
				}
			}
		}
		// now put in the ends
		for (OutputNode output : outputs) {
			model.append("Class "
					+ instances.classAttribute().value(output.getClassIndex()) + "\n    Input\n");
			for (NeuralNode input : output.getInputElements()) {
				if (input instanceof InputNode) {
					model.append("    Attrib "
							+ instances.attribute(((OutputNode) input).getClassIndex()).name() + "\n");
				} else {
					model.append("    Node " + input.getId() + "\n");
				}
			}
		}
		return model.toString();
	}
	
}
