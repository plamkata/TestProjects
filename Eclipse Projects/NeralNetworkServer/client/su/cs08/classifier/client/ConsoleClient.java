/**
 * 
 */
package su.cs08.classifier.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


/**
 * This is the general console client that supports asynchronous interaction 
 * from console to socket and from socket to console. The general options supported
 * on start up are:
 * -host=<Host Name>
 * -port=<Port Number>
 * -csv=<CSV File Path>
 * 
 * While interacting real-time with the server the client may disconnect by writing: exit.
 * This will generally closes the socket on the client and wait for the server to close his
 * writing stream.
 * 
 * @author plamKaTa
 * 
 */
public class ConsoleClient {

	private static String serverHostname = "localhost";

	private static int serverPort = 6733;

	private static BufferedReader mSocketReader;

	private static PrintWriter mSocketWriter;

	/**
	 * Starts appropriate IO transmitter threads.
	 * @param args
	 */
	public static void main(String[] args) {
		// read server host and port from args.
		String host = getOption("host", args);
		if (host != null) serverHostname = host;
		
		String port = getOption("port", args);
		if (port != null) serverPort = Integer.parseInt(port);
		
		InputStream inputStream = System.in;
		String filename = getOption("csv", args);
		if (filename != null) {
			try {
				inputStream = new FileInputStream(filename);
			} catch (FileNotFoundException e) {
				System.err.println("Cannot find the file specified: " + filename);
				e.printStackTrace();
				System.exit(-1);
			}
		}

		// Connect to the server
		try {
			Socket socket = new Socket(serverHostname, serverPort);
			mSocketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			mSocketWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.println("Connected to server " +	serverHostname + ":" + serverPort);
		} catch (IOException ioe) {
			System.err.println("Can not connect to " + serverHostname + ":" + serverPort);
			ioe.printStackTrace();
			System.exit(-1);
		}
		
		startTransfering(inputStream);
	}
	
	/**
	 * @param inputStream
	 */
	private static void startTransfering(InputStream inputStream) {
		// Start socket --> console transmitter thread
		PrintWriter consoleWriter = new PrintWriter(System.out);
		IOTransmitter socketToConsoleTransmitter = new IOTransmitter(mSocketReader, consoleWriter);
		socketToConsoleTransmitter.setDaemon(false);
		
		// Start console --> socket transmitter thread
		// TODO: this is a performance killer - try using pipes/scanner instead
		// BufferedReader consoleReader = new BufferedReader(new InputStreamReader(inputStream));
		Scanner consoleScanner = new Scanner(inputStream);
		IOTransmitter consoleToSocketTransmitter = new IOTransmitter(consoleScanner, mSocketWriter);
		consoleToSocketTransmitter.setDaemon(false);
		
		socketToConsoleTransmitter.setOposite(consoleToSocketTransmitter);
		consoleToSocketTransmitter.setOposite(socketToConsoleTransmitter);
		
		socketToConsoleTransmitter.start();
		consoleToSocketTransmitter.start();
	}
	
	private static String getOption(String optName, String[] options) {
		String optValue = null;
		for (int num = 0; num < options.length; num++) {
			if (options[num].contains(optName)) {
				int index = options[num].indexOf('=');
				if (index + 1 < options[num].length()) {
					optValue = options[num].substring(index + 1);
					break;
				}
			}
		}
		return optValue;
	}
}
