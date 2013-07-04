/**
 * 
 */
package su.cs08.classifires.functions;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;

/**
 * This class contains all configurations for the neural network.
 * @author plamKaTa
 */
public class NetworkConfig implements Serializable, OptionHandler {
	
	/** This flag states that the user wants the input values normalized. */
	private boolean normalizeAttributes;

	/** This is the learning rate for the network. */
	private double learningRate;

	/** This is the momentum for the network. */
	private double momentum;

	/**
	 * This flag states that the user wants the network to restart if it is
	 * found to be generating infinity or NaN for the error value. This would
	 * restart the network with the current options except that the learning
	 * rate would be smaller than before, (perhaps half of its current value).
	 * This option will not be available if the gui is chosen (if the gui is
	 * open the user can fix the network themselves, it is an architectural
	 * minefield for the network to be reset with the gui open).
	 */
	private boolean reset;

	/**
	 * This flag states that the user wants the class to be normalized while
	 * processing in the network is done. (the final answer will be in the
	 * original range regardless). This option will only be used when the class
	 * is numeric.
	 */
	private boolean normalizeClass;
	
	/** The number of epochs to train through. */
	private int trainingTime;
	
	/** The number used to seed the random number generator. */
	private long m_randomSeed;

	/** A flag to state that a nominal to binary filter should be used. */
	private boolean m_useNomToBin;
	
	/** The number to to use to quit on validation testing. */
	private int m_driftThreshold;
	
	/** A flag to say that it's a numeric class. */
	private boolean m_numeric;

	/** The ranges for all the attributes. */
	private double[] m_attributeRanges;

	/** The base values for all the attributes. */
	private double[] m_attributeBases;
	
	public NetworkConfig() {

		// setting all the options to their defaults. To completely change these
		// defaults they will also need to be changed down the bottom in the
		// set options function (the text info in the accompanying functions
		// should also be changed to reflect the new defaults
		normalizeClass = true;
		normalizeAttributes = true;

		m_numeric = false;
		m_driftThreshold = 20;
		m_useNomToBin = true;
		m_randomSeed = 0;
		trainingTime = 500;
		learningRate = .3;
		momentum = .2;
		reset = true;
	}

	public boolean isNormalizeAttributes() {
		return normalizeAttributes;
	}

	public void setNormalizeAttributes(boolean normalizeAttributes) {
		this.normalizeAttributes = normalizeAttributes;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double rate) {
		if (rate > 0 && rate <= 1) {
			learningRate = rate;
		}
	}

	public double getMomentum() {
		return momentum;
	}

	public void setMomentum(double m) {
		if (m >= 0 && m <= 1) {
			this.momentum = m;
		}
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean m_reset) {
		this.reset = m_reset;
	}

	public boolean isNormalizeClass() {
		return normalizeClass;
	}

	public void setNormalizeClass(boolean class1) {
		normalizeClass = class1;
	}
	
	/**
	 * Set the number of training epochs to perform. Must be greater than 0.
	 * 
	 * @param n
	 *            The number of epochs to train through.
	 */
	public void setTrainingTime(int n) {
		if (n > 0) {
			trainingTime = n;
		}
	}

	/**
	 * @return The number of epochs to train through.
	 */
	public int getTrainingTime() {
		return trainingTime;
	}

	/**
	 * @param f
	 *            True if a nominalToBinary filter should be used on the data.
	 */
	public void setNominalToBinaryFilter(boolean f) {
		m_useNomToBin = f;
	}

	/**
	 * @return The flag for nominal to binary filter use.
	 */
	public boolean getNominalToBinaryFilter() {
		return m_useNomToBin;
	}

	/**
	 * This seeds the random number generator, that is used when a random number
	 * is needed for the network.
	 * 
	 * @param l
	 *            The seed.
	 */
	public void setRandomSeed(long l) {
		if (l >= 0) {
			m_randomSeed = l;
		}
	}

	/**
	 * @return The seed for the random number generator.
	 */
	public long getRandomSeed() {
		return m_randomSeed;
	}
	
	/**
	 * This sets the threshold to use for when validation testing is being done.
	 * It works by ending testing once the error on the validation set has
	 * consecutively increased a certain number of times.
	 * 
	 * @param t
	 *            The threshold to use for this.
	 */
	public void setValidationThreshold(int t) {
		if (t > 0) {
			m_driftThreshold = t;
		}
	}

	/**
	 * @return The threshold used for validation testing.
	 */
	public int getValidationThreshold() {
		return m_driftThreshold;
	}

	public boolean isNumeric() {
		return m_numeric;
	}
	
	/**
	 * Returns an enumeration describing the available options.
	 * 
	 * @return an enumeration of all the available options.
	 */
	public Enumeration<Option> listOptions() {

		Vector<Option> newVector = new Vector<Option>(14);

		newVector.addElement(new Option(
				"\tLearning Rate for the backpropagation algorithm.\n"
						+ "\t(Value should be between 0 - 1, Default = 0.3).",
				"L", 1, "-L <learning rate>"));
		newVector.addElement(new Option(
				"\tMomentum Rate for the backpropagation algorithm.\n"
						+ "\t(Value should be between 0 - 1, Default = 0.2).",
				"M", 1, "-M <momentum>"));
		newVector
				.addElement(new Option("\tNumber of epochs to train through.\n"
						+ "\t(Default = 500).", "N", 1, "-N <number of epochs>"));
		newVector
				.addElement(new Option(
						"\tPercentage size of validation set to use to terminate"
								+ " training (if this is non zero it can pre-empt num of epochs.\n"
								+ "\t(Value should be between 0 - 100, Default = 0).",
						"V", 1, "-V <percentage size of validation set>"));
		newVector
				.addElement(new Option(
						"\tThe value used to seed the random number generator"
								+ "\t(Value should be >= 0 and and a long, Default = 0).",
						"S", 1, "-S <seed>"));
		newVector.addElement(new Option(
				"\tThe consequetive number of errors allowed for validation"
						+ " testing before the netwrok terminates."
						+ "\t(Value should be > 0, Default = 20).", "E", 1,
				"-E <threshold for number of consequetive errors>"));
		newVector.addElement(new Option(
				"\tAutocreation of the network connections will NOT be done.\n"
						+ "\t(This will be ignored if -G is NOT set)", "A", 0,
				"-A"));
		newVector.addElement(new Option(
				"\tA NominalToBinary filter will NOT automatically be used.\n"
						+ "\t(Set this to not use a NominalToBinary filter).",
				"B", 0, "-B"));
		newVector
				.addElement(new Option(
						"\tThe hidden layers to be created for the network.\n"
								+ "\t(Value should be a list of comma seperated Natural numbers"
								+ " or the letters 'a' = (attribs + classes) / 2, 'i'"
								+ " = attribs, 'o' = classes, 't' = attribs .+ classes)"
								+ " For wildcard values" + ",Default = a).",
						"H", 1,
						"-H <comma seperated numbers for nodes on each layer>"));
		newVector
				.addElement(new Option(
						"\tNormalizing a numeric class will NOT be done.\n"
								+ "\t(Set this to not normalize the class if it's numeric).",
						"C", 0, "-C"));
		newVector.addElement(new Option(
				"\tNormalizing the attributes will NOT be done.\n"
						+ "\t(Set this to not normalize the attributes).", "I",
				0, "-I"));
		newVector.addElement(new Option(
				"\tReseting the network will NOT be allowed.\n"
						+ "\t(Set this to not allow the network to reset).",
				"R", 0, "-R"));
		newVector.addElement(new Option("\tLearning rate decay will occur.\n"
				+ "\t(Set this to cause the learning rate to decay).", "D", 0,
				"-D"));

		return newVector.elements();
	}

	/**
	 * Parses a given list of options. Valid options are:
	 * <p>
	 * 
	 * -L num <br>
	 * Set the learning rate. (default 0.3)
	 * <p>
	 * 
	 * -M num <br>
	 * Set the momentum (default 0.2)
	 * <p>
	 * 
	 * -N num <br>
	 * Set the number of epochs to train through. (default 500)
	 * <p>
	 * 
	 * -V num <br>
	 * Set the percentage size of the validation set from the training to use.
	 * (default 0 (no validation set is used, instead num of epochs is used)
	 * <p>
	 * 
	 * -S num <br>
	 * Set the seed for the random number generator. (default 0)
	 * <p>
	 * 
	 * -E num <br>
	 * Set the threshold for the number of consequetive errors allowed during
	 * validation testing. (default 20)
	 * <p>
	 * 
	 * -A <br>
	 * Do not automatically create the connections in the net.
	 * 
	 * -B <br>
	 * Do Not automatically Preprocess the instances with a nominal to binary
	 * filter.
	 * <p>
	 * 
	 * -H str <br>
	 * Set the number of nodes to be used on each layer. Each number represents
	 * its own layer and the num of nodes on that layer. Each number should be
	 * comma seperated. There are also the wildcards 'a', 'i', 'o', 't' (default
	 * 4)
	 * <p>
	 * 
	 * -C <br>
	 * Do not automatically Normalize the class if it's numeric.
	 * <p>
	 * 
	 * -I <br>
	 * Do not automatically Normalize the attributes.
	 * <p>
	 * 
	 * -R <br>
	 * Do not allow the network to be automatically reset.
	 * <p>
	 * 
	 * -D <br>
	 * Cause the learning rate to decay as training is done.
	 * <p>
	 * 
	 * @param options
	 *            the list of options as an array of strings
	 * @exception Exception
	 *                if an option is not supported
	 */
	public void setOptions(String[] options) throws Exception {
		// the defaults can be found here!!!!
		String learningString = Utils.getOption('L', options);
		if (learningString.length() != 0) {
			setLearningRate((new Double(learningString)).doubleValue());
		} else {
			setLearningRate(0.3);
		}
		String momentumString = Utils.getOption('M', options);
		if (momentumString.length() != 0) {
			setMomentum((new Double(momentumString)).doubleValue());
		} else {
			setMomentum(0.2);
		}
		String epochsString = Utils.getOption('N', options);
		if (epochsString.length() != 0) {
			setTrainingTime(Integer.parseInt(epochsString));
		} else {
			setTrainingTime(500);
		}
		String seedString = Utils.getOption('S', options);
		if (seedString.length() != 0) {
			setRandomSeed(Long.parseLong(seedString));
		} else {
			setRandomSeed(0);
		}
		String thresholdString = Utils.getOption('E', options);
		if (thresholdString.length() != 0) {
			setValidationThreshold(Integer.parseInt(thresholdString));
		} else {
			setValidationThreshold(20);
		}
		// small note. since the gui is the only option that can change the
		// other
		// options this should be set first to allow the other options to set
		// properly
		if (Utils.getFlag('B', options)) {
			setNominalToBinaryFilter(false);
		} else {
			setNominalToBinaryFilter(true);
		}
		if (Utils.getFlag('C', options)) {
			setNormalizeClass(false);
		} else {
			setNormalizeClass(true);
		}
		if (Utils.getFlag('I', options)) {
			setNormalizeAttributes(false);
		} else {
			setNormalizeAttributes(true);
		}
		if (Utils.getFlag('R', options)) {
			setReset(false);
		} else {
			setReset(true);
		}

		Utils.checkForRemainingOptions(options);
	}

	/**
	 * Gets the current settings of NeuralNet.
	 * 
	 * @return an array of strings suitable for passing to setOptions()
	 */
	public String[] getOptions() {

		String[] options = new String[21];
		int current = 0;
		options[current++] = "-L";
		options[current++] = "" + getLearningRate();
		options[current++] = "-M";
		options[current++] = "" + getMomentum();
		options[current++] = "-N";
		options[current++] = "" + getTrainingTime();
		options[current++] = "-S";
		options[current++] = "" + getRandomSeed();
		options[current++] = "-E";
		options[current++] = "" + getValidationThreshold();
		options[current++] = "-H";
		if (!getNominalToBinaryFilter()) {
			options[current++] = "-B";
		}
		if (!isNormalizeClass()) {
			options[current++] = "-C";
		}
		if (!isNormalizeAttributes()) {
			options[current++] = "-I";
		}
		if (!isReset()) {
			options[current++] = "-R";
		}

		while (current < options.length) {
			options[current++] = "";
		}
		return options;
	}
	
	/**
	 * This function sets what the m_numeric flag to represent the passed class
	 * it also performs the normalization of the attributes if applicable and
	 * sets up the info to normalize the class. (note that regardless of the
	 * options it will fill an array with the range and base, set to normalize
	 * all attributes and the class to be between -1 and 1)
	 * 
	 * @param inst
	 *            the instances.
	 * @return The modified instances. This needs to be done. If the attributes
	 *         are normalized then deep copies will be made of all the instances
	 *         which will need to be passed back out.
	 */
	public Instances setClassType(Instances inst) throws Exception {
		if (inst != null) {
			// x bounds
			double min = Double.POSITIVE_INFINITY;
			double max = Double.NEGATIVE_INFINITY;
			m_attributeRanges = new double[inst.numAttributes()];
			m_attributeBases = new double[inst.numAttributes()];
			for (int attrNum = 0; attrNum < inst.numAttributes(); attrNum++) {
				min = Double.POSITIVE_INFINITY;
				max = Double.NEGATIVE_INFINITY;
				for (int i = 0; i < inst.numInstances(); i++) {
					if (!inst.instance(i).isMissing(attrNum)) {
						double value = inst.instance(i).value(attrNum);
						if (value < min) {
							min = value;
						}
						if (value > max) {
							max = value;
						}
					}
				}

				m_attributeRanges[attrNum] = (max - min) / 2;
				m_attributeBases[attrNum] = (max + min) / 2;
				
				if (attrNum != inst.classIndex() && isNormalizeAttributes()) {
					for (int i = 0; i < inst.numInstances(); i++) {
						double diff = inst.instance(i).value(attrNum) - m_attributeBases[attrNum];
						if (m_attributeRanges[attrNum] != 0) {
							inst.instance(i).setValue(attrNum, diff / m_attributeRanges[attrNum]);
						} else {
							inst.instance(i).setValue(attrNum, diff);
						}
					}
				}
			}
			if (inst.classAttribute().isNumeric()) {
				m_numeric = true;
			} else {
				m_numeric = false;
			}
		}
		return inst;
	}
	

	/**
	 * Calculates the normalized value of an attribute at the specified index 
	 * having the old value of the attribute. 
	 * @param attIndex the index of the attribute
	 * @param oldValue the old value of the attribute
	 * @return the new normalized value
	 */
	public double normalizeAttribute(int attIndex, double oldValue) {
		double value;
		if (m_attributeRanges[attIndex] != 0) {
			value = (oldValue - m_attributeBases[attIndex]) / m_attributeRanges[attIndex];
		} else {
			value = oldValue - m_attributeBases[attIndex];
		}
		return value;
	}
	
	public double getRange(int attrIndex) {
		if (attrIndex < 0 || attrIndex >= m_attributeRanges.length) {
			throw new IllegalArgumentException("Attribute index is out of bounds.");
		}
		return m_attributeRanges[attrIndex];
	}
	
	public double getBase(int attrIndex) {
		if (attrIndex < 0 || attrIndex >= m_attributeBases.length) {
			throw new IllegalArgumentException("Attribute index is out of bounds.");
		}
		return m_attributeBases[attrIndex];
	}
	
	/**
	 * This will return a string describing the classifier.
	 * 
	 * @return The string.
	 */
	public String globalInfo() {

		return "This neural network uses backpropagation to train.";
	}

	/**
	 * @return a string to describe the learning rate option.
	 */
	public String learningRateTipText() {
		return "The amount the" + " weights are updated.";
	}

	/**
	 * @return a string to describe the momentum option.
	 */
	public String momentumTipText() {
		return "Momentum applied to the weights during updating.";
	}

	/**
	 * @return a string to describe the random seed option.
	 */
	public String randomSeedTipText() {
		return "Seed used to initialise the random number generator."
				+ "Random numbers are used for setting the initial weights of the"
				+ " connections betweem nodes, and also for shuffling the training data.";
	}

	/**
	 * @return a string to describe the validation threshold option.
	 */
	public String validationThresholdTipText() {
		return "Used to terminate validation testing."
				+ "The value here dictates how many times in a row the validation set"
				+ " error can get worse before training is terminated.";
	}

	/**
	 * @return a string to describe the learning rate option.
	 */
	public String trainingTimeTipText() {
		return "The number of epochs to train through."
				+ " If the validation set is non-zero then it can terminate the network"
				+ " early";
	}

	/**
	 * @return a string to describe the nominal to binary option.
	 */
	public String nominalToBinaryFilterTipText() {
		return "This will preprocess the instances with the filter."
				+ " This could help improve performance if there are nominal attributes"
				+ " in the data.";
	}

	/**
	 * @return a string to describe the hidden layers in the network.
	 */
	public String hiddenLayersTipText() {
		return "This defines the hidden layers of the neural network."
				+ " This is a list of positive whole numbers. 1 for each hidden layer."
				+ " Comma seperated. To have no hidden layers put a single 0 here."
				+ " This will only be used if autobuild is set. There are also wildcard"
				+ " values 'a' = (attribs + classes) / 2, 'i' = attribs, 'o' = classes"
				+ " , 't' = attribs + classes.";
	}

	/**
	 * @return a string to describe the nominal to binary option.
	 */
	public String normalizeNumericClassTipText() {
		return "This will normalize the class if it's numeric."
				+ " This could help improve performance of the network, It normalizes"
				+ " the class to be between -1 and 1. Note that this is only internally"
				+ ", the output will be scaled back to the original range.";
	}

	/**
	 * @return a string to describe the nominal to binary option.
	 */
	public String normalizeAttributesTipText() {
		return "This will normalize the attributes."
				+ " This could help improve performance of the network."
				+ " This is not reliant on the class being numeric. This will also"
				+ " normalize nominal attributes as well (after they have been run"
				+ " through the nominal to binary filter if that is in use) so that the"
				+ " nominal values are between -1 and 1";
	}

	/**
	 * @return a string to describe the Reset option.
	 */
	public String resetTipText() {
		return "This will allow the network to reset with a lower learning rate."
				+ " If the network diverges from the answer this will automatically"
				+ " reset the network with a lower learning rate and begin training"
				+ " again. This option is only available if the gui is not set. Note"
				+ " that if the network diverges but isn't allowed to reset it will"
				+ " fail the training process and return an error message.";
	}

}
