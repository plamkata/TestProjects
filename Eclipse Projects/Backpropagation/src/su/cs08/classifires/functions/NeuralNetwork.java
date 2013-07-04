/**
 * 
 */
package su.cs08.classifires.functions;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

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
	 * This inner class is used as an input node of the network.
	 * Hence it should know the number of the attribute to be passed.
	 */
	protected class InputNode extends NeuralNode {

		/**
		 * The index of the attribute represented in the network by this input node.
		 */
		private int attrIndex;
		
		public InputNode(String id, int attrNumber) {
			super(id);

			this.attrIndex = attrNumber;
		}
		
		@Override
		protected double calculateValue() {
			if (currentInstance.isMissing(attrIndex)) {
				value = 0;
			} else {
				value = currentInstance.value(attrIndex);
			}
			return value;
		}
		
		@Override
		protected double calculateError() {
			error = 0;
			for (NeuralNode output : getOutputElements()) {
				error += output.getError();
			}
			return error;
		}
		
		/**
		 * The corresponding attribute number whose value is 
		 * to be processed by this node.
		 * @return the attribute number
		 */
		public int getAttrIndex() {
			return attrIndex;
		}
		
	}

	/**
	 * This inner class is used as an output node.
	 * Hence it knows the corresponding class number it represents.
	 */
	protected class OutputNode extends NeuralNode {

		/**
		 * The number of the class that is represented by this output node in the network.
		 */
		private int classIndex;

		public OutputNode(String id, int classIndex) {
			super(id);
			this.classIndex = classIndex;
		}

		/**
		 * Call this to get the output value of this node.
		 * 
		 * @return The output value, or NaN, if the value has not been
		 *         calculated.
		 */
		protected double calculateValue() {
			// node is an output.
			value = 0;
			for (int index = 0; index < getInputElements().size(); index++) {
				NeuralNode input = getInputElements().get(index);
				value += input.getValue()*getWeight(index);

			}
			if (configuration.isNumeric() && configuration.isNormalizeClass()) {
				// then scale the value;
				// this scales linearly from between -1 and 1
				int index = instances.classIndex();
				value = value*configuration.getRange(index) + configuration.getBase(index);
			}
			return value;

		}

		/**
		 * Call this to get the error value of this unit, which in this case is
		 * the difference between the predicted class, and the actual class.
		 * 
		 * @return The error value, or NaN, if the value has not been
		 *         calculated.
		 */
		@Override
		protected double calculateError() {
			if (currentInstance.classIsMissing()) {
				error = .1;
			} else if (instances.classAttribute().isNominal()) {
				if (currentInstance.classValue() == classIndex) {
					error = 1 - value;
				} else {
					error = 0 - value;
				}
			} else if (configuration.isNumeric()) {
				double error = currentInstance.classValue() - value;
				if (configuration.isNormalizeClass()) {
					double attrRange = configuration.getRange(instances.classIndex());
					error = (attrRange == 0) ? 0 : error / attrRange;
				}
			}
			return error;
		}

		/**
		 * @return link for this node.
		 */
		public int getClassIndex() {
			return classIndex;
		}

	}

	/** 
	 * The training instances. 
	 */
	private Instances instances;

	/** The current instance. */
	private Instance currentInstance;

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
		currentInstance = null;

		outputs = new ArrayList<OutputNode>();
		inputs = new ArrayList<InputNode>();
		numAttributes = 0;
		numClasses = 0;
		innerNodes = new ArrayList<NeuralNode>();
		nextId = 0;
		random = null;
		nominalToBinaryFilter = new NominalToBinary();
		
		configuration = new NetworkConfig();
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
	 */
	private void reset() {
		for (OutputNode output : outputs) {
			output.reset();
		}
	}

	/**
	 * This will calculate values for all output elements of the network. 
	 * Hence all network elements' values will be calculated (including hidden layer).
	 */
	private void calculateOutputs() {
		for (OutputNode output : outputs) {
			output.getValue();
		}
	}

	/**
	 * This will cause the error values to be calculated for all nodes. Note
	 * that the m_currentInstance is used to calculate these values. Also the
	 * output values should have been calculated first.
	 * 
	 * @return The squared error.
	 */
	private double calculateErrors() throws Exception {
		double ret = 0;
		
		// calculate errors for the network
		for (InputNode input : inputs) {
			input.getError();
		}
		
		for (OutputNode output : outputs) {
			ret += output.getError() * output.getError();
		}
		return ret;

	}

	/**
	 * This will cause the weight values to be updated based on the learning
	 * rate, momentum and the errors that have been calculated for each node.
	 * 
	 * @param learnRate
	 *            The learning rate to update with.
	 * @param momentum
	 *            The momentum to update with.
	 */
	private void updateWeights(double learnRate, double momentum) {
		for (OutputNode output : outputs) {
			output.updateWeights(learnRate, momentum);
		}
	}

	/**
	 * This creates the required input units.
	 */
	private void createInputLayer() throws Exception {
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
	private void createOutputLayer() throws Exception {
		outputs.clear();
		for (int classIndex = 0; classIndex < numClasses; classIndex++) {
			OutputNode output;
			if (configuration.isNumeric()) {
				output = new OutputNode(instances.classAttribute().name(), classIndex);
			} else {
				output = new OutputNode(instances.classAttribute().value(classIndex), classIndex);
			}
			
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
	 * @param i
	 *            The training data.
	 * @exception Exception
	 *                if can't build classification properly.
	 */
	public void buildClassifier(Instances i) throws Exception {
		if (i.checkForStringAttributes()) {
			throw new UnsupportedAttributeTypeException("Cannot handle string attributes!");
		} 
		if (i.numInstances() == 0) {
			throw new IllegalArgumentException("No training instances.");
		}
		if (i.numAttributes() == 1) {
			System.err.println("No training instances found - only classes.");
			throw new IllegalArgumentException("No training instances found - only classes.");
		}
		
		initClassifier(i);

		createInputLayer();
		createOutputLayer();
		createHiddenLayer();

		// connections done.
		double estimation = 0;
		double origRate = configuration.getLearningRate(); // only used for when reset
		
		double totalWeight = calculateTotalWeight(0);

		for (int noa = 1; noa < configuration.getTrainingTime() + 1; noa++) {
			estimation = 0;
			for (int nob = 0; nob < instances.numInstances(); nob++) {
				currentInstance = instances.instance(nob);

				if (!currentInstance.classIsMissing()) {
					// this is where the network is being trained 
					reset();
					calculateOutputs();
					double tempRate = configuration.getLearningRate() * currentInstance.weight();
					estimation += (calculateErrors() / instances.numClasses()) * currentInstance.weight();
					updateWeights(tempRate, configuration.getMomentum());
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
						throw new IllegalStateException(
								"Learning rate got too small ("	+ 
								configuration.getLearningRate() + " <= " + Utils.SMALL + 
								")!");
					
					// change the learning rate and rebuild
					configuration.setLearningRate(configuration.getLearningRate() / 2);
					buildClassifier(i);
					configuration.setLearningRate(origRate);
					instances = new Instances(instances, 0);
					return;
				}
			}
		}
		instances = new Instances(instances, 0);
	}

	/**
	 * @param i
	 * @throws Exception
	 */
	private void initClassifier(Instances i) throws Exception {
		instances = null;
		currentInstance = null;

		outputs = new ArrayList<OutputNode>();
		inputs = new ArrayList<InputNode>();
		innerNodes = new ArrayList<NeuralNode>();
		numAttributes = 0;
		numClasses = 0;

		nextId = 0;
		
		instances = new Instances(i);
		instances.deleteWithMissingClass();
		if (instances.numInstances() == 0) {
			instances = null;
			throw new IllegalArgumentException("All class values missing.");
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
	}

	/**
	 * @param numInVal
	 * @return
	 */
	private double calculateTotalWeight(int numInVal) {
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
	 * @param i
	 *            The instance to classify.
	 * @return A double array filled with the probabilities of each class type.
	 * @exception Exception
	 *                if can't classify instance.
	 */
	public double[] distributionForInstance(Instance i) throws Exception {
		if (configuration.getNominalToBinaryFilter()) {
			nominalToBinaryFilter.input(i);
			currentInstance = nominalToBinaryFilter.output();
		} else {
			currentInstance = i;
		}

		if (configuration.isNormalizeAttributes()) {
			for (int noa = 0; noa < instances.numAttributes(); noa++) {
				if (noa != instances.classIndex()) {
					double oldValue = currentInstance.value(noa);
					
					double value = configuration.normalizeAttribute(noa, oldValue);
					currentInstance.setValue(noa, value);
				}
			}
		}
		reset();

		// since all the output values are needed.
		// They are calculated manually here and the values collected.
		double[] theArray = new double[numClasses];
		for (int classIndex = 0; classIndex < numClasses; classIndex++) {
			theArray[classIndex] = outputs.get(classIndex).getValue();
		}
		if (instances.classAttribute().isNumeric()) {
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
