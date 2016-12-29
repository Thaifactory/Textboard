package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import controller.CommunicationController;

public class Client {
	private CommunicationController communicationController;
	private Socket clientSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private Listener socketListener;
	private int numberOfListener = 0;

	public Client() {
		this.clientSocket = new Socket();
	}

	public void connect(String host, int port) {
		try {
			clientSocket.connect(new InetSocketAddress(host, port));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			socketListener = new Listener(in, numberOfListener++);
			socketListener.start();
		} catch (UnknownHostException e) {
			communicationController.getClientController().update("# Don't know about host: " + host);
			e.printStackTrace();
		} catch (IOException e) {
			communicationController.getClientController().update("# Couldn't get I/O for the connection to: " + host);
			e.printStackTrace();
		}
	}

	public void send(String str) {
		if (clientSocket != null && out != null) {
			out.println(str);
			//receive();
		}
	}
	
	public void receive() {
		String responseLine;
		if (clientSocket != null && in != null) {
			try {
				while ((responseLine = in.readLine()) != null) {
					communicationController.getClientController().update("< " + responseLine);
				}
			} catch (IOException e) {
				communicationController.getClientController().update("# Couldn't get Input for the connection");
			}
		}
	}

	public void close() {
		if (out != null) {
			out.close();
			communicationController.getClientController().update("# Outputstream geschlossen...");
		}
		if (in != null) {
			try {
				in.close();
				communicationController.getClientController().update("# Inputstream geschlossen...");
			} catch (IOException e) {
				communicationController.getClientController().update("# Socket nicht zu schliessen...");
				e.printStackTrace();
			}
		}
		if (clientSocket != null) {
			try {
				clientSocket.close();
				communicationController.getClientController().update("# Socket geschlossen...");
			} catch (IOException e) {
				communicationController.getClientController().update("# Socket nicht zu schliessen...");
				e.printStackTrace();
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

	private class Listener extends Thread {
		private BufferedReader in;
		private boolean terminate;
		private int numberOfListener;

		public Listener(BufferedReader reader, int numberOfListener) {
			this.in = reader;
			this.numberOfListener = numberOfListener;
			terminate = false;
		}

		public void terminate() {
			terminate = true;
			communicationController.getClientController().update("# Terminate the Listener " + numberOfListener
					+ ".\n Please " + "close the Socket and create a new connection!");
		}

		@Override
		public void run() {
//			String responseLine;
			while (!terminate) {
				if (in != null) {
					try {
						communicationController.getClientController().update("< " + in.readLine());
//						while (in.ready()) {
//							communicationController.getClientController().update("< " + 
//									(responseLine = in.readLine()));
//						}
					} catch (IOException e) {
						communicationController.getClientController().update("# Couldn't get Input for the connection");
						terminate();
					}
				}
			}
		}
	}
}