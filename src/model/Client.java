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
	private Socket serverSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	private CommunicationController communicationController;
	
	public Client() {
		this.serverSocket = new Socket();
	}
	
	public void connect(String host, int port) {
		try {
			serverSocket.connect(new InetSocketAddress(host, port));
			out = new PrintWriter(serverSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
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
		return serverSocket.isConnected();
	}
	
	public void send(String str) {
		if(serverSocket != null && out != null) {
				Scanner scanner = new Scanner(str);
				while(scanner.hasNextLine()) {
					out.println(scanner.nextLine());
				}
				scanner.close();
		}
	}
	
	public void receive() {
		String line = null;
		if(serverSocket != null && in != null) {
			try {
				while ((line = in.readLine()) != null) {
					communicationController.getClientController().update(line);
				}
			} catch (IOException e) {
				communicationController.getClientController().update(
	        			"# Couldn't get I/O for the connection to: " + serverSocket.getInetAddress());
			}
		}
	}
	
	public void close() {
		if (serverSocket != null && out != null && in != null) {
			try {
				in.close();
				out.close();
				serverSocket.close();
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