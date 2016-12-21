package GUI;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.sun.jmx.snmp.Timestamp;

import controller.CommunicationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ClientController {

    @FXML private TextField ipTextField;
    @FXML private TextField portTextField;
    @FXML private Button connectButton;
    @FXML private Circle statusCircle;
    @FXML private TextArea outputTextArea;
    @FXML private TextField dateTextField;
    @FXML private TextArea inputTextArea;
    @FXML private Button sendButton;
    
    private CommunicationController communicationController;
    private boolean connected = false;

    @FXML
    void initialize() {
    	sendButton.requestFocus();
    	checkStatus();	
    }
    
    @FXML
    void connectAction(ActionEvent event) {
    	if(connected) {
    		communicationController.getClient().close();
    		connected = false;
    		checkStatus();
    	} else {
    		int port = Integer.parseInt(portTextField.getText());

        	print("# Connect Port: " + port);
        	communicationController.getClient().connect(ipTextField.getText(), port);
        	
        	try {
        		connected = communicationController.getClient().isConnected();
        	} catch(NullPointerException e) {
        		print("# Socket is closed!");
        		connected = false;
        	}
        	print("# Is connected: " + connected);
        	
        	checkStatus();
        	inputTextArea.requestFocus();
    	}
    }    

    @FXML
    void convertToTimestampAction(ActionEvent event) {
    	inputTextArea.setText(inputTextArea.getText() + convert());
    	dateTextField.clear();
    }

    @FXML
    void keyPressedAction(KeyEvent event) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		print("\n");
    	}
    }
    
    @FXML
    void sendAction(ActionEvent event) {
    	print("> " + inputTextArea.getText());
    	communicationController.getClient().send(inputTextArea.getText());
    	communicationController.getClient().receive();
    	inputTextArea.clear();
    }
    
    public void setCommunicationController(CommunicationController controller) {
    	this.communicationController = controller;
    }
    
    private void print(String text) {
    	outputTextArea.setText(outputTextArea.getText() + "\n" + text);
    }
    
    private void checkStatus() {
    	if(connected) {
    		statusCircle.setFill(Color.GREEN);
    		ipTextField.setDisable(true);
    		portTextField.setDisable(true);
    		connectButton.setText("Trennen");
    	} else {
    		statusCircle.setFill(Color.RED);
    		ipTextField.setDisable(false);
    		portTextField.setDisable(false);
    		connectButton.setText("Verbinden");
    	}
    }
    
    private long convert() {
    	DateFormat dt = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss", Locale.GERMANY);
    	Date date = null;
    	try {
    		date = dt.parse(dateTextField.getText());
    	} catch(Exception e) {
    		print(e.getMessage());
    	}
    	Timestamp timestamp = new Timestamp(date.getTime());
    	return timestamp.getDateTime();
    }

    public void update(String text) {
    	print(text);
    }
}
