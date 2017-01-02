package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import controller.CommunicationController;
import javafx.application.Platform;

/**
 * Handle the connection between Server and this Client.
 * @author Michael Ratke and Timon Sachweh
 *
 */
public class Client {
	private CommunicationController communicationController;
	private Socket clientSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private Listener socketListener;
	private int numberOfListener = 1;

	public Client() {
		this.clientSocket = new Socket();
	}

	/**
	 * Connect the socket of this client with the host.
	 * @param host Server which will be connect.
	 * @param port Port of the Server which will be connect.
	 */
	public void connect(String host, int port) {
		try {
			clientSocket = new Socket(host, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			socketListener = new Listener(in, numberOfListener++);
			socketListener.start();
		} catch (UnknownHostException e) {
			communicationController.getClientController().update("# Don't know about host: " + host);
		} catch (IOException e) {
			communicationController.getClientController().update("# Couldn't get I/O for the connection to: " + host);
		}
	}

	/**
	 * Send the message to the host.
	 * @param str Text or command of the message.
	 */
	public void send(String str) {
		if (clientSocket != null && out != null) {
			out.println(str);
		}
	}

	/**
	 * Close the connection with the host.
	 */
	public void close() {
		communicationController.getClientController().update("");
		
		if (out != null) {
			out.close();
			communicationController.getClientController().update("# Close outputstream...");
		}
		if (in != null) {
			try {
				in.close();
				communicationController.getClientController().update("# Close inputstream...");
			} catch (IOException e) {
				communicationController.getClientController().update("# Cound't close the inputstream...");
			}
		}
		if (clientSocket != null) {
			try {
				clientSocket.close();
				communicationController.getClientController().update("# Close socket...");
			} catch (IOException e) {
				communicationController.getClientController().update("# Cound't close the socket...");
			}
		}
	}

	public boolean isConnected() {
		return clientSocket.isConnected();
	}

	public CommunicationController getController() {
		return communicationController;
	}

	public void setController(CommunicationController controller) {
		this.communicationController = controller;
	}

	/**
	 * A thread which listen for notifications from the host
	 * @author Michael Ratke and Timon Sachweh
	 *
	 */
	private class Listener extends Thread {
		private BufferedReader in;
		private boolean terminate;

		public Listener(BufferedReader reader, int numberOfListener) {
			setName("Listener " + numberOfListener);
			this.in = reader;
			this.terminate = false;
		}

		/**
		 * Terminate this thread.
		 */
		public void terminate() {
			terminate = true;
			communicationController.getClientController().update("# Terminate the " + getName()
					+ ".\n   Please close the Socket and create a new connection!");
		}

		//@Override
		public void run() {
			String responseLine;
			while (!terminate) {
				if (in != null) {
					try {
						while ((responseLine = in.readLine()) != null) {
							final String line = responseLine;
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									communicationController.getClientController().update("< " + line);
								}
							});
						}
					} catch (IOException e) {
						communicationController.getClientController().update("# " + 
									getName() + ": Couldn't get Input for the connection");
						terminate();
					}
				} else {
					terminate();
				}
			}
		}
	}
}