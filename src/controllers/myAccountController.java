package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AuthenticationModel;
import models.UserModel;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class myAccountController implements Initializable{
	private UserModel userModel = new UserModel();
	@FXML
	private Label lblName;
	@FXML
	private Label lblRole;
	@FXML
	private TextField tfName;
	@FXML
	private TextField tfCode;
	@FXML
	private TextField tfEmail;
	@FXML
	private TextField tfPhone;
	@FXML
	private TextField tfUserType;
	@FXML
	private Label lblNameError;
	@FXML
	private Label lblEmailError;
	@FXML
	private Label lblPhoneError;
	@FXML
	private AnchorPane createHolder;
	private MasterController mas = new MasterController();

	// Event Listener on Button.onAction
	@FXML
	public void changePassword(ActionEvent event) {
		try {
			
			//draw
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/change-pass.fxml"));
			createHolder = root.load();
			
			//controller
			ChangePassController controller = root.<ChangePassController>getController();
			controller.loadDataUpdateById(this);
			
			Scene scene = new Scene(createHolder, 638, 370);
			Stage createStage = new Stage();
			createStage.initStyle(StageStyle.UNDECORATED);
			createStage.setScene(scene);
			createStage.show();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void close() {
		mas.btnCloseAction();
	}
	@FXML
	public void btnSaveAction(ActionEvent event) {
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		try {
			String name = tfName.getText();
			String phone = tfPhone.getText();
			String email = tfEmail.getText();
			

			if(validated( name, phone, email)) {
				ArrayList<DataMapping> users = new ArrayList<DataMapping>();
				users.add(DataMapping.getInstance("name", name));
				users.add(DataMapping.getInstance("phone", phone));
				users.add(DataMapping.getInstance("email", email));

				if(AuthenticationModel.id != 0) {
					
					
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Update user Confirmation");
					alert.setHeaderText("Do you want to make this change?");
					alert.setX(screenBounds.getWidth() - 850);
					alert.setY(screenBounds.getHeight() - 610);
					Optional<ButtonType> option = alert.showAndWait();
					if (option.get() == ButtonType.OK) {
						userModel.updateUserById(AuthenticationModel.id, users);
						Helpers.status("success");
					} 
					
				}
				this.parseData();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean validated( String name, String phone, String email) {
		try {
			
			lblNameError.setText("");
			lblPhoneError.setText("");
			lblEmailError.setText("");
			
			
			ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
			data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:5"));
			data.add(new ValidationDataMapping("phone", phone, "lblPhoneError", "required|phone"));
			data.add(new ValidationDataMapping("email", email, "lblEmailError", "required|email"));
			
			ArrayList<DataMapping> messages = Validations.validated(data);
			if(messages.size() > 0) {
				for(DataMapping message : messages) {
					switch(message.key) {
						case "lblNameError":
							lblNameError.setText(message.value);
							break;
						case "lblPhoneError":
							lblPhoneError.setText(message.value);
							break;
						case "lblEmailError":
							lblEmailError.setText(message.value);
							break;
					
						default:
							System.out.println("abcde");
					}

				}
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//load data
		public void parseData() {
			try {
				if(AuthenticationModel.id != 0) {
					//get current user info
					ResultSet user = this.userModel.getUserById(AuthenticationModel.id);
					if(user.next()) {
						tfCode.setText(user.getString("users.code"));
						tfName.setText(user.getString("user_name"));
						lblName.setText(user.getString("user_name"));
						lblRole.setText(user.getString("role_name"));
						tfUserType.setText(user.getString("role_name"));
						tfPhone.setText(user.getString("users.phone"));
						tfEmail.setText(user.getString("users.email"));
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.parseData();
	}
}
