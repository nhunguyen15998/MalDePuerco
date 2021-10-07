/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.security.crypto.bcrypt.BCrypt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.AuthenticationModel;
import models.RoleModel;
import models.UserModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class UserCUController implements Initializable {

	private UserModel userModel = new UserModel();
	public UserController userController;
	private int userId;
	
	@FXML
	private Label lblUser;
	@FXML
	private TextField tfName;
	@FXML
	private Label lblNameError;
	@FXML
	private TextField tfPhone;

	@FXML
	private TextField tfCode;
	@FXML
	private Label lblPhoneError;
	@FXML
	private TextField tfEmail;
	@FXML
	private Label lblEmailError;
	@FXML
	private TextField tfPassword;
	@FXML
	private Label lblPasswordError;

	@FXML
	private Button btnCancel;
	@FXML
	private ComboBox<DataMapping> cbGender;
	@FXML
	private Label lblBranchError;
	@FXML
	private ComboBox<DataMapping> cbUserType;
	@FXML
	private Label lblUserTypeError;
	@FXML
	private ComboBox<DataMapping> cbStatus;
	private RoleModel roleModel = new RoleModel();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.getUserList();
		
		
		
		ObservableList<DataMapping> status = FXCollections.observableArrayList(UserModel.isActivated, UserModel.isDeactivated);
		cbStatus.setItems(status);
		
	}
	

	//validate
	public boolean validated( String name, String phone, String email, String password, String role) {
		try {
			
			lblNameError.setText("");
			lblPhoneError.setText("");
			lblEmailError.setText("");
			lblPasswordError.setText("");
			lblUserTypeError.setText("");
			
			ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
			data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:5"));
			data.add(new ValidationDataMapping("phone", phone, "lblPhoneError", "required|phone"));
			data.add(new ValidationDataMapping("email", email, "lblEmailError", "required|email"));
			data.add(new ValidationDataMapping("password", password, "lblPasswordError", "min:5|string"));
			data.add(new ValidationDataMapping("role", role, "lblUserTypeError", "required"));
			
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
						case "lblPasswordError":
							lblPasswordError.setText(message.value);
							break;
						
						case "lblUserTypeError":
							lblUserTypeError.setText(message.value);
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
	
	//btnSave
	public void btnSaveAction() {
		try {
			String name = tfName.getText();
			String phone = tfPhone.getText();
			String email = tfEmail.getText();
			String pass = tfPassword.getText();
			String code = Helpers.randomCode("UC");
			String role = cbUserType.getValue() != null ? cbUserType.getValue().key: null;
			String status = cbStatus.getValue() != null ? cbStatus.getValue().key : String.valueOf(UserModel.USER_DEACTIVATED);

			if(validated( name, phone, email, pass, role)) {
				String passHased = BCrypt.hashpw(pass, BCrypt.gensalt(10));
				ArrayList<DataMapping> users = new ArrayList<DataMapping>();
				users.add(DataMapping.getInstance("name", name));
				users.add(DataMapping.getInstance("phone", phone));
				users.add(DataMapping.getInstance("email", email));
				users.add(DataMapping.getInstance("role_id", role));
				users.add(DataMapping.getInstance("status", status));

				if(this.userId == 0) {
					users.add(DataMapping.getInstance("code", code));
					users.add(DataMapping.getInstance("password", passHased));
					userModel.createUser(users);
					Helpers.status("success");
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Update User Confirmation");
					alert.setHeaderText("Do you want to make this change?");
					Optional<ButtonType> option = alert.showAndWait();
					if (option.get() == ButtonType.OK) {
						userModel.updateUserById(this.userId, users);
						Helpers.status("success");
					} 
					
				}
				userController.parseData(null);
				this.close();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//btnCancel
	public void btnCancelAction() {
		try {
			this.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnClose
	public void close() {
		try {
			Stage stage = (Stage) btnCancel.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//load user type
	public ResultSet getUserList() {
		try {
			ArrayList<DataMapping> userTypeOptions = new ArrayList<DataMapping>();

			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			if(AuthenticationModel.roleName.equals("Manager")) {
			conditions.add(CompareOperator.getInstance("name", "!=", "Super Admin"));
			}
			ResultSet userTypes = roleModel.getRoleList(conditions);
			while(userTypes.next()) {
				userTypeOptions.add(DataMapping.getInstance(userTypes.getInt("id"), userTypes.getString("name")));
				
			}
			cbUserType.getItems().setAll(userTypeOptions);
			return userTypes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	//load data update
	public void loadDataUpdateById(UserController userController) {
		try {
			this.userController = userController;
			this.userId = userController.getUserId();
			lblUser.setText("Create User");
			if(this.userId != 0) {
				tfPassword.setText("000000");
				lblUser.setText("Update User");
				tfPassword.setDisable(true);
				ResultSet currentUser = this.userModel.getUserById(this.userId);
				if(currentUser.next()) {
					tfCode.setText(currentUser.getString("code"));
					tfName.setText(currentUser.getString("name"));
					tfPhone.setText(currentUser.getString("phone"));
					tfEmail.setText(currentUser.getString("email"));
					
					//load combobox selected values
					
					
					for(DataMapping userType : cbUserType.getItems()) {
						if(userType.key != null && Integer.parseInt(userType.key) == currentUser.getInt("role_id")) {
							cbUserType.setValue(userType);
							break;
						}
					}
					
					for(DataMapping status : cbStatus.getItems()) {
						if(status.key != null && Integer.parseInt(status.key) == currentUser.getInt("status")) {
							cbStatus.setValue(status);
							break;
						}
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
}
