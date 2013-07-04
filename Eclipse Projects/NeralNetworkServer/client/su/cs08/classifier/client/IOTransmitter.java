/**
 * 
 */
package su.cs08.classifier.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Transmits data from the specified reader to the specified writer
 * when it is available. Achieves asynchronous read/write.
 * <p>
 * The reader may alternatively be changed with a scanner for better
 * asynchronous performance.
 * <p>
 * The current IOTransmitter keeps track of his opposite IOTransmitter and 
 * in case of error, exit or EOF manages interruption of both transmitters
 * or appropriate interaction.
 *  
 * @author plamKaTa
 * 
 */
public class IOTransmitter extends Thread {
	
	private BufferedReader mReader;
	
	private Scanner mScanner;
	

	private PrintWriter mWriter;
	
	
	private IOTransmitter opposite;

	/**
	 * Construct an IO transmitter.
	 * @param aReader
	 * @param aWriter
	 */
	public IOTransmitter(BufferedReader aReader, PrintWriter aWriter) {
		mReader = aReader;
		mWriter = aWriter;
	}
	
	/**
	 * Construct an IO transmitter.
	 * @param aScanner
	 * @param aWriter
	 */
	public IOTransmitter(Scanner aScanner, PrintWriter aWriter) {
		mScanner = aScanner;
		mWriter = aWriter;
	}
	
	/**
	 * Sets the opposite transmitter to manage.
	 * @param oposite - the opposite transmitter
	 */
	public void setOposite(IOTransmitter oposite) {
		this.opposite = oposite;
	}
	
	/**
	 * Wait the current transmitter for millis.
	 * @param millis number of milliseconds
	 */
	public synchronized void postpone(long millis) {
		try {
			wait(millis);
		} catch (InterruptedException e) {
			// continue
		}
	}

	/**
	 * Until interrupted reads a text line from the reader
	 * and sends it to the writer.
	 */
	public void run() {
		try {
			while (!isInterrupted()) {
				String data = readLine();
				if (data != null) {
					if (data.contains("errno=552")) {
						if (opposite != null) {
							opposite.postpone(10000);
						}
					} else if (data.equals("exit")) {
						opposite.interrupt();
						// now unblock opposite by closing output stream to server
						// this way the server will close his output stream to opposite
						// and hence an IOException will be thrown at opposite's cycle
						mWriter.close();
						break;
					}
					mWriter.println(data);
					mWriter.flush();
				} else {
					// send a flush request to server in order to trigger execution
					mWriter.println("flush");
					mWriter.flush();
					
					// now interrupt the opposite and wait opposite for handling server's response
					opposite.interrupt();
					opposite.join();
					
					mWriter.close();
					break;
				}
			}
		} catch (IOException ioe) {
			System.err.println("Lost connection to server.");
			System.exit(-1);
		} catch (InterruptedException e) {
			// let this tread die
		}
	}
	
	private synchronized String readLine() throws IOException {
		if (mReader != null) {
			return mReader.readLine();
		} else if (mScanner != null) {
			try {
				return mScanner.nextLine();
			} catch (NoSuchElementException e) {
				return null;
				//throw new IOException("Reached end of file.", e);
			}
		} else {
			throw new IOException("No reader available.");
		}
	}
}
