package su.cs08.classifier.server.algorithm;

import java.util.Hashtable;

import weka.core.Instance;

/**
 * This class is used as an input node of the network.
 * Hence it should know the number of the attribute to be passed.
 */
public class InputNode extends NeuralNode {

	/**
	 * The index of the attribute represented in the network by this input node.
	 */
	private int attrIndex;
	
	public InputNode(String id, int attrNumber) {
		super(id);

		this.attrIndex = attrNumber;
	}
	
	@Override
	protected double calculateValue(Instance instance, Hashtable<NeuralNode, Double> nodeValues) {
		double value;
		if (instance.isMissing(attrIndex)) {
			value = 0;
		} else {
			value = instance.value(attrIndex);
		}
		return value;
	}
	
	@Override
	protected double calculateError(Instance instance, Hashtable<NeuralNode, Double> nodeValues) {
		error = 0;
		for (NeuralNode output : getOutputElements()) {
			error += output.getError(instance, nodeValues);
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