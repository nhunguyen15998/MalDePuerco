package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CustomerOrderController implements Initializable {
	
	//pane
	@FXML
	private AnchorPane firstPane;
	@FXML
	private ScrollPane secondScrollPane; 
	@FXML
	private Pane thirdPane;
	@FXML
	private AnchorPane fourPane;
	
	@FXML
	private AnchorPane spChildPane;
	@FXML
	private VBox vboxPane;
	
	//btn
	@FXML
	private Button btnPay;
	@FXML
	private Button btnBack;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
