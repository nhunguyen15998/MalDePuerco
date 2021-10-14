package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CustomerOptionController implements Initializable {
	private CustomerHomeController customerHomeController = new CustomerHomeController();
	//xml
	@FXML
	private AnchorPane apOption;
	@FXML
	private Label lblSignIn;
	@FXML
	private Button btnCancel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void showForm(CustomerHomeController customerHomeController) {
		this.customerHomeController = customerHomeController;
		this.customerHomeController.customerMasterHolder.setDisable(true);
	}
	
	//btnSignInAction
	public void btnSignInAction() {
		
	}
	
	//btnExitAppAction
	public void btnExitAppAction() {
		System.exit(0);
	}
	
	//btnCancelAction
	public void btnCancelAction() {
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		this.customerHomeController.customerMasterHolder.setDisable(false);
		stage.close();
	}
	
	
}
