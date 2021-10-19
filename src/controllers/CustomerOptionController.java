package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AuthenticationModel;

public class CustomerOptionController implements Initializable {
	public CustomerHomeController customerHomeController;
	public MasterController masterController;
	
	private static Stage stage;
	//xml
	@FXML
	private AnchorPane apOption;
	@FXML
	private Label lblUserName;
	@FXML
	private Label lblRole;
	@FXML
	private Label lblSignIn;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnSignIn;
	@FXML
	private Button btnDashboard;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//if signed in => btn dashboard
		if(AuthenticationModel.id != 0) {
			lblUserName.setText(AuthenticationModel.name);
			lblRole.setText(AuthenticationModel.roleName);
			apOption.getChildren().removeAll(lblSignIn, btnSignIn);
		} else {//if not signed in => btn sign in
			apOption.getChildren().removeAll(lblUserName, lblRole, btnDashboard);
			notSignIn();
		}
	}

	public void showForm(CustomerHomeController customerHomeController) {
		this.customerHomeController = customerHomeController;
		this.masterController = customerHomeController.masterController;
		this.customerHomeController.customerMasterHolder.setDisable(true);
	}
	
	//not sign in
	public void notSignIn() {
		lblSignIn.setText("You have not signed in");
		lblSignIn.setFont(Font.font("System Italic", 10));
		lblSignIn.setTextFill(Color.web("#c6c4c4"));
		lblSignIn.setLayoutX(105);
		lblSignIn.setLayoutY(156);
		
		btnSignIn.setLayoutX(110);
		btnSignIn.setLayoutY(196);
		btnSignIn.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #ea7c69; "
				+ "-fx-border-radius: 10px; -fx-background-radius: 10px;");
		btnSignIn.setTextFill(Color.WHITE);
		btnSignIn.setFont(Font.font("System", 11));
	}
	
	//btnDashboardAction
	public void btnDashboardAction() {
		if(AuthenticationModel.id != 0) {
			Stage master = (Stage) this.masterController.anchorPane.getScene().getWindow();
			master.show();
			apOption.getScene().getWindow().hide();
			this.customerHomeController.customerMasterHolder.getScene().getWindow().hide();
		}
	}

	//btnSignInAction
	public void btnSignInAction() {
		FXMLLoader root = loadView("sign_in.fxml", 797, 500);
		//controller
		SignInController controller = root.getController();
		controller.itemCustomer(this.customerHomeController);
		this.customerHomeController.customerMasterHolder.getScene().getWindow().hide();
		apOption.getScene().getWindow().hide();
	}
	
	//load view
	public FXMLLoader loadView(String path, double width, double height) {
		try {
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			AnchorPane holder;
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/"+path));
			holder = root.load();
			Scene scene = new Scene(holder, width, height);
			stage = new Stage();
			stage.setX((screenBounds.getWidth() - width)/2);
			stage.setY((screenBounds.getHeight() - height)/2);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene);
			stage.show();
			
			return root;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
