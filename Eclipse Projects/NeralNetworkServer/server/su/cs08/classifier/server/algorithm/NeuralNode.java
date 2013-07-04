/**
 * 
 */
package su.cs08.classifier.server.algorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import weka.core.Instance;

/**
 * This class represents an abstract neural element in a neural network.
 * 
 * @author plamKaTa
 */
public abstract class NeuralNode implements Serializable {

	// bitwise flags for the types of unit.

	/** These are the input elements of the current element. */
	private List<NeuralNode> inputElements;

	/** These are the output elements of the current element. */
	private List<NeuralNode> outputElements;

	/** The output value for the current element (NaN if not calculated). */
	protected double value;

	/** The error value for the current element (NaN if not calculated). */
	protected double error;

	/** Determine if the weights have been updated (default false). */
	protected boolean weightsUpdated;

	/** The unique string identifier of the current element. */
	protected String id;

	/**
	 * Constructor of a neural element.
	 * 
	 * @param id
	 */
	public NeuralNode(String id) {
		this.id = id;
		inputElements = new ArrayList<NeuralNode>();
		outputElements = new ArrayList<NeuralNode>();
		error = Double.NaN;
		weightsUpdated = false;
	}

	public String getId() {
		return id;
	}

	public List<NeuralNode> getInputElements() {
		return inputElements;
	}

	public List<NeuralNode> getOutputElements() {
		return outputElements;
	}

	/**
	 * Returns the value of the current element.
	 * If the value is not calculated it will be first calculated.
	 * @param instance the current instance that is passed through the network
	 * @param nodeValues calculated
	 * @return the value of the element
	 */
	public double getValue(Instance instance, Hashtable<NeuralNode, Double> nodeValues) {
		if (!nodeValues.containsKey(this)) {
			Double value = calculateValue(instance, nodeValues);
			nodeValues.put(this, value);
		}
		return nodeValues.get(this);
	}

	/**
	 * Returns the error of the current element.
	 * If the value have not been calculated an {@link IllegalStateException} will be thrown.
	 * If the error have not been calculated it will be first calculated.
	 * @param instance the current instance that is passed through the network
	 * @param nodeValues the collection of values calculated so far
	 * @return the error of the element
	 * @throws IllegalStateException if value have not been calculated yet
	 */
	public double getError(Instance instance, Hashtable<NeuralNode, Double> nodeValues) {
		if (!nodeValues.containsKey(this)) {
			throw new IllegalStateException("Value should have been initialised in order to calculate the error.");
		}
		if (Double.isNaN(error)) {
			error = calculateError(instance, nodeValues);
		}
		return error;
	}
	
	/**
	 * Calculate the value for the current element.
	 * Note that getValue will automatically call this function
	 * in order to lazily initialize the value of the element.
	 * @param instance the current instance that is passed through the network
	 * @param nodeValues the node values calculated so far
	 * @return the value of the element.
	 */
	protected abstract double calculateValue(Instance instance, Hashtable<NeuralNode, Double> nodeValues);
	
	/**
	 * Calculate the error of the current element.
	 * Note that getError will automatically call this function
	 * in order to lazily initialize the error of the element.
	 * @param instance the current instance that is passed through the network
	 * @param nodeValues the node values calculated so far
	 * @return the error of the element
	 */
	protected abstract double calculateError(Instance instance, Hashtable<NeuralNode, Double> nodeValues);
	
	/**
	  * Call this function to update the weight values at this element.
	  * After the weights have been updated at this element, All the
	  * input connections will then be called from this to have their
	  * weights updated.
	 * @param instance the current instance that is passed through the network
	 * @param nodeValues the node values calculated so far
	 * @param learn The learning Rate to use.
	 * @param momentum The momentum to use.
	  */
	 public void updateWeights(Instance instance, Hashtable<NeuralNode, Double> nodeValues, double learn, double momentum) {
		 if (!weightsUpdated) {
			 for (NeuralNode element : inputElements) {
				 element.updateWeights(instance, nodeValues, learn, momentum);
			 }
			 weightsUpdated = true;
		 }
	 }
	
	/**
	 * This function will reset the current element and all its input elements
	 * in initial state as if no calculations were performed. The current element
	 * will be ready for the next teaching example.
	 * @param nodeValues the values for all neural nodes for the current running 
	 * instance of the algorithm, which have been calculated so far
	 */
	public void reset(Hashtable<NeuralNode, Double> nodeValues) {
		nodeValues.remove(this);
		error = Double.NaN;
		weightsUpdated = false;
		for (NeuralNode input : inputElements) {
			input.reset(nodeValues);
		}
	}
	
	/**
	 * Returns the weight of the connection for the corresponding input 
	 * element index.
	 * @param inputElementIndex the index of the input element
	 * @return the weight of the connection for the corresponding input element index.
	 */
	public double getWeight(int inputElementIndex) {
		return 1;
	}
	
	/**
	 * Returns the change in the weight of the connection for the corresponding 
	 * input element index.
	 * @param inputElementIndex the index of the input element
	 * @return the change in weight of the connection for the corresponding input element index.
	 */
	public double getWeightChange(int inputElementIndex) {
		return 0;
	}
	
	/**
	 * Changes the weight of the corresponding input connection on the specified 
	 * input element index. Implementations should update the corresponding weight change
	 * in order to be able to achieve certain optimizations.  
	 * @param inputElementIndex the index of the input element connection to be updated
	 * @param newValue the new weight of the connection
	 * @return the old weight of the connection
	 */
	public double changeWeight(int inputElementIndex, double newValue) {
		return 1;
	}

	/**
	 * Returns the weight of the connection for the corresponding input element.
	 * @param inputElement the input element connection
	 * @return the weight of the connection for the corresponding input element.
	 */
	public double getWeight(NeuralNode inputElement) {
		int index = inputElements.indexOf(inputElement);
		if (index == -1) {
			throw new IllegalArgumentException("Given argument is not an output element.");
		} else {
			return getWeight(index);
		}
	}

	public boolean areWeightsUpdated() {
		return weightsUpdated;
	}

	/**
	 * This will connect the specified element to be an input to this element.
	 * 
	 * @param element
	 *            The element.
	 * @return True if the connection was made, false otherwise.
	 */
	protected boolean connectInput(NeuralNode element) {
		if (inputElements.contains(element)) {
			return false;
		} else {
			inputElements.add(element);
			return true;
		}
	}

	/**
	 * This will connect the specified element to be an output to this element.
	 * 
	 * @param element
	 *            The element.
	 * @return True if the connection was made, false otherwise.
	 */
	protected boolean connectOutput(NeuralNode element) {
		if (outputElements.contains(element)) {
			return false;
		} else {
			outputElements.add(element);
			return true;
		}
	}
	
	public static boolean connect(NeuralNode a, NeuralNode b) {
		if (a == null || b == null || a == b) {
			return false;
		}

		if (!a.connectOutput(b)) return false;
		if (!b.connectInput(a)) return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ID: " + id + " error: " + error + 
			" inputs: " + inputElements.size() + " outputs:" + outputElements.size() + ";";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof NeuralNode)) {
			return false;
		}
		
		NeuralNode otherNode = (NeuralNode) other;
		if (this.getId() == null && otherNode.getId() == null) {
			// compare by reference
			return super.equals(other);
		} else if (this.getId() != null && otherNode.getId() != null) {
			return this.getId().equals(otherNode.getId());
		} else {
			return super.equals(other);
		}
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		if (getId() != null) {
			hash = getId().hashCode()^37;
		} else {
			hash = super.hashCode();
		}
		return hash;
	}

}
