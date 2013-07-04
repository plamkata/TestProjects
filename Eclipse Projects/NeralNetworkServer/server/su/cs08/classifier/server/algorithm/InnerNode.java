/**
 * 
 */
package su.cs08.classifier.server.algorithm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import weka.core.Instance;

/**
 * This class represents a node in the neural network.
 * @author plamKaTa
 */
public class InnerNode extends NeuralNode {
	
	/** This list contains the weights of the connections of this element to all its input elements. */
	private List<Double> weights;
	
	/** This list contains the changes of the corresponding weights. */
	private List<Double> weightsChanges;
	
	private Random random;
	
	/**
	 * Instantiate a new neural node.
	 * @param id The id for the node.
	 * @param r The random generator to be used for initial weights.
	 * @param f The transitional function to be used for calculations by the current node.
	 */
	public InnerNode(String id, Random r) {
		super(id);
		this.id = id;
		this.random = r;
		
		this.weights = new ArrayList<Double>();
		this.weightsChanges = new ArrayList<Double>();
		
		// initialize the first weight according to the algorithm
		this.weights.add(random.nextDouble()*.1 -.05);
		this.weightsChanges.add(.0);
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifires.functions.NeuralElement#calculateValue()
	 */
	@Override
	protected double calculateValue(Instance instance, Hashtable<NeuralNode, Double> nodeValues) {
		double value = weights.get(0);
		List<NeuralNode> inputs = getInputElements();
		for (int index = 0; index < inputs.size(); index++) {
			NeuralNode input = inputs.get(index);
			value += input.getValue(instance, nodeValues)*getWeight(index);
		}
		
		if (value > 45) {
			value = 1;
		} else if (value < -45) {
			value = 0;
		} else {
			value = 1 / (1 + Math.exp(-value));
		}
		return value;
	}
	
	/* (non-Javadoc)
	 * @see su.cs08.classifires.functions.NeuralElement#calculateError()
	 */
	@Override
	protected double calculateError(Instance instance, Hashtable<NeuralNode, Double> nodeValues) {
		double error = 0;
		for (NeuralNode output : getOutputElements()) {
			// find the index of element in output's inputElements collection 
			int inputElementIndex = output.getInputElements().indexOf(this);
			error += output.getError(instance, nodeValues)*output.getWeight(inputElementIndex);
		}
		
		error = error * getValue(instance, nodeValues) * (1 - getValue(instance, nodeValues));
		return error;
	}
	
	@Override
	public void updateWeights(Instance instance, Hashtable<NeuralNode, Double> nodeValues, double learn, double momentum) {
		if (!weightsUpdated && !Double.isNaN(error)) {
			double learnError = learn*getError(instance, nodeValues);
			
			int index = -1;
			double c = learnError*1 + momentum*getWeightChange(index);
			changeWeight(index, getWeight(index) + c);
			
			List<NeuralNode> inputs = getInputElements();
			for (index = 0; index < inputs.size(); index++) {
				c = learnError*inputs.get(index).getValue(instance, nodeValues) + momentum*getWeightChange(index);
				changeWeight(index, getWeight(index) + c);
			}
			
			// call super in order for the changes to be propagated recursively
			super.updateWeights(instance, nodeValues, learn, momentum);
		}
	}

	/* (non-Javadoc)
	 * @see su.cs08.classifires.functions.NeuralElement#getWeight(int)
	 */
	@Override
	public double getWeight(int inputElementIndex) {
		if (inputElementIndex < -1 || inputElementIndex >= getInputElements().size()) {
			return Double.NaN;
		}
		return weights.get(inputElementIndex + 1);
	}
	
	public double getWeightChange(int inputElementIndex) {
		if (inputElementIndex < -1 || inputElementIndex >= getInputElements().size()) {
			return Double.NaN;
		}
		return weightsChanges.get(inputElementIndex + 1);
	}
	
	public double changeWeight(int inputElementIndex, double newWeight) {
		if (inputElementIndex < -1 || inputElementIndex >= getInputElements().size()) {
			throw new IndexOutOfBoundsException("Index is out of bounds.");
		}
		
		Double oldWeight = weights.set(inputElementIndex + 1, newWeight); 
		weightsChanges.set(inputElementIndex + 1, oldWeight - newWeight);
		return oldWeight;
	}
	
	/**
	 * Query the weights of the connections with output elements for the current element.
	 * @return the weights of output connections.
	 */
	public Double[] getWeights() {
		return weights.toArray(new Double[]{});
	}
	
	public Double[] getWeightsChanges() {
		return weightsChanges.toArray(new Double[]{});
	}
	
	@Override
	protected boolean connectInput(NeuralNode element) {
		if (!super.connectInput(element)) {
			return false;
		}
		
		weights.add(random.nextDouble()*.1 -.05);
		weightsChanges.add(.0);
		return true;
	}

}
