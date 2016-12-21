package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import controller.CommunicationController;

public class Client {
	private Socket clientSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	private CommunicationController communicationController;
	
	public Client() {
		this.clientSocket = new Socket();
	}
	
	public void connect(String host, int port) {
		try {
			clientSocket.connect(new InetSocketAddress(host, port));
			receive();
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
		if(clientSocket != null) {
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				Scanner scanner = new Scanner(str);
				while(scanner.hasNextLine()) {
					out.println(scanner.nextLine());
				}
				scanner.close();
			} catch (IOException e) { 
	            communicationController.getClientController().update(
	            		"# Couldn't get I/O for the connection to: " + clientSocket.getInetAddress()); 
	            e.printStackTrace(); 
	        } finally {
				out.close();
	        }
		}
	}
	
	public void receive() {
		String line = null;
		if(clientSocket != null) {
			try {
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
				while((line = in.readLine()) != null) {
					communicationController.getClientController().update(line);
				}
			} catch (IOException e) { 
				communicationController.getClientController().update(
						"# Couldn't get I/O for the connection to: " + clientSocket.getInetAddress()); 
	            e.printStackTrace(); 
	        } finally {
	        	try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}
	}
	
	public void close() {
		if (clientSocket != null) {
			try {
				clientSocket.close();
				communicationController.getClientController().update(
						"# Socket geschlossen..."); 
			} catch (IOException e) { 
				communicationController.getClientController().update(
						"# Socket nicht zu schliessen..."); 
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