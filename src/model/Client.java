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

	public Client() {
		this.clientSocket = new Socket();
	}

	public void connect(String host, int port) {
		try {
			clientSocket.connect(new InetSocketAddress(host, port));
			
		} catch (UnknownHostException e) {
			communicationController.getClientController().update(
					"# Don't know about host: " + host);
			e.printStackTrace();
		} catch (IOException e) {
			communicationController.getClientController().update(
					"# Couldn't get I/O for the connection to: " + host);
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return clientSocket.isConnected();
	}

	public void send(String str) {
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			communicationController.getClientController().update(
					"# Couldn't get I/O for the connection" + clientSocket.getInetAddress());
			e.printStackTrace();
		}
		
		if (clientSocket != null && out != null && in != null) {
			out.println(str);
			String responseLine;
			try {
				while((responseLine = in.readLine()) != null) {
					communicationController.getClientController().update("< " + responseLine);
				}
			} catch (IOException e) {
				communicationController.getClientController().update(
						"# Couldn't get I/O for the connection to: " + clientSocket.getInetAddress());
	        } finally {
	        	try {
					in.close();
				} catch (IOException e) {
					communicationController.getClientController().update(
							"# Couldn't close the Inputstream");
					e.printStackTrace();
				}
	        	out.close();
	        }
		}
	}

	public void close() {
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

	public CommunicationController getController() {
		return communicationController;
	}

	public void setController(CommunicationController controller) {
		this.communicationController = controller;
	}
}