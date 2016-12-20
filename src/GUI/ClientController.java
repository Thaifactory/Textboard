package GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;

public class ClientController {

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private Button connectButton;

    @FXML
    private Circle statusCircle;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private TextField dateTextField;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private Button sendButton;

    @FXML
    void connectAction(ActionEvent event) {

    }

    @FXML
    void convertToTimestampAction(ActionEvent event) {

    }

    @FXML
    void keyPressedAction(KeyEvent event) {

    }

}
