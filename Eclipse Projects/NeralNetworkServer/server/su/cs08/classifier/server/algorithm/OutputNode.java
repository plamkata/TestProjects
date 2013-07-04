package su.cs08.classifier.server.algorithm;

import java.util.Hashtable;

import weka.core.Instance;

/**
 * This inner class is used as an output node.
 * Hence it knows the corresponding class number it represents.
 */
class OutputNode extends NeuralNode {

	/**
	 * The number of the class that is represented by this output node in the network.
	 */
	private int classIndex;
	
	private NetworkConfig configuration;

	public OutputNode(String id, int classIndex, NetworkConfig configuration) {
		super(id);
		this.classIndex = classIndex;
		this.configuration = configuration;
	}

	/**
	 * Call this to get the output value of this node.
	 * 
	 * @return The output value, or NaN, if the value has not been
	 *         calculated.
	 */
	protected double calculateValue(Instance instance, Hashtable<NeuralNode, Double> nodeValues) {
		// node is an output.
		double value = 0;
		for (int index = 0; index < getInputElements().size(); index++) {
			NeuralNode input = getInputElements().get(index);
			value += input.getValue(instance, nodeValues)*getWeight(index);

		}
		if (configuration.isNumeric() && configuration.isNormalizeClass()) {
			// then scale the value;
			// this scales linearly from between -1 and 1
			int index = instance.classIndex();
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
	protected double calculateError(Instance instance, Hashtable<NeuralNode, Double> nodeValues) {
		double error = .1;
		if (instance.classIsMissing()) {
			error = .1;
		} else if (instance.classAttribute().isNominal()) {
			if (instance.classValue() == classIndex) {
				error = 1 - getValue(instance, nodeValues);
			} else {
				error = 0 - getValue(instance, nodeValues);
			}
		} else if (configuration.isNumeric()) {
			error = instance.classValue() - getValue(instance, nodeValues);
			if (configuration.isNormalizeClass()) {
				double attrRange = configuration.getRange(instance.classIndex());
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