/**
 * 
 */
package su.cs08.classifier.server;

import java.io.IOException;
import java.io.Reader;

import su.cs08.classifier.server.exceptions.MessageParsingException;
import weka.core.Instance;
import weka.core.Instances;

/**
 * The Class MessageParser is used for parsing client messages in an appropriate format.
 * 
 * @author plamKaTa
 */
public class MessageParser {
	
	/**
	 * Parses a string message send from client to either a learning instance
	 * or querying instance depending on number of values specified in string.
	 * <p>
	 * Message format should be comma separated value list of values for particular
	 * properties for the learning instances.
	 * 
	 * @param reader the message to be parsed
	 * @param header the header
	 * 
	 * @return an instance representation of the message
	 * 
	 * @throws MessageParsingException if there is a problem with the format of the message
	 * @throws IOException if there is a problem reading the instance
	 */
	public static Instance parseInstance(Instances header, Reader reader) 
	throws MessageParsingException, IOException {
		if (header == null) {
			throw new IllegalArgumentException("Header should have been parsed.");
		}
		try {
			header.readInstance(reader);
		} catch (Exception e) {
			throw new MessageParsingException(e.getMessage(), e);
		}
		int index = header.numInstances() - 1;
		if (index == -1) {
			throw new MessageParsingException("Instance was not successfuly parsed.");
		}
		Instance lastInstance = header.instance(index);
		if (lastInstance == null || !header.checkInstance(lastInstance)) {
			throw new MessageParsingException("Instance is not compatible with expected format.");
		}		
		header.delete(index);
		return lastInstance;
	}

}
