package controller;

import GUI.ClientController;
import model.Client;

public class CommunicationController {
	private Client client;
	private ClientController clientController;
	
	public CommunicationController(ClientController clientController) {
		this.client = new Client();
		this.clientController = clientController;
		System.out.println(clientController.toString());
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public ClientController getClientController() {
		return clientController;
	}

	public void setClientController(ClientController clientController) {
		this.clientController = clientController;
	}
}
