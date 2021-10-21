package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.stage.Window;
import org.springframework.security.crypto.bcrypt.BCrypt;

import app.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.image.ImageView;

import javafx.scene.control.PasswordField;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AuthenticationModel;
import models.UserModel;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;
import javafx.scene.input.KeyEvent;

public class ChangePassController implements Initializable{
	private String phone;
	@FXML
	private Label lblReser;
	@FXML
	private Label lblName;
	@FXML
	private Label lblRole;
	@FXML
	private TextField tfOld;
	@FXML
	private TextField tfNew;
	@FXML
	private Label lblOldPass;
	@FXML
	private Label lblNewPass;
	@FXML
	private TextField tfConfirm;
	@FXML
	private Label lblConfirmPass;
	@FXML
	private Button btnCancel;
	@FXML
	private PasswordField tfOldHidden;
	@FXML
	private ImageView closeOld;
	@FXML
	private ImageView openOld;
	@FXML
	private ImageView openNew;
	@FXML
	private ImageView openConfirm;
	@FXML
	private PasswordField tfNewHidden;
	@FXML
	private ImageView closeNew;
	@FXML
	private PasswordField tfConfirmHidden;
	@FXML
	private ImageView closeConfirm;
	@FXML
	private AnchorPane createHolder;
	private UserModel userModel = new UserModel();
	private myAccountController myAcc = new myAccountController();
	private SignInController session = new SignInController();
	@FXML
	public void btnSaveAction(ActionEvent event) {
		
		if(!validated()){
			lblOldPass.setText("Incorrect password");
		}
		if(lblConfirmPass.getText().equals("not match")) {
			System.out.println("no");
		}
		else if(validated()){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Update User Confirmation");
		alert.setHeaderText("Do you want to make this change?");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK) {
			ArrayList<DataMapping> data = new ArrayList<DataMapping>();
			String passHased = BCrypt.hashpw(tfNewHidden.getText(), BCrypt.gensalt(10));
			data.add(DataMapping.getInstance("password", passHased));
			userModel.updateUserById( AuthenticationModel.id,data);
			Helpers.status("success");
			btnCancelAction();
			alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Please login again");
			alert.initStyle(StageStyle.UNDECORATED);
			Optional<ButtonType> option1 = alert.showAndWait();
			if (option1.get() == ButtonType.OK) {
			    Platform.runLater( () -> new Main().start( new Stage() ) );
			    Preferences preference = Preferences.userNodeForPackage(SignInController.class);
			    preference.putInt("remind", 0);
			    session.deleteLoginSession();
			    close();
			    
				

			}
		}
		}
	}
	private void close() {
		myAcc.close();
	}
	
	private boolean validated() {
		boolean check = userModel.doLogin(phone, tfOld.getText());
		return check;
	}
	@FXML
	public void btnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
	@FXML
	public void close(MouseEvent event) {
		if(event.getSource()==closeOld){
			supportOpen(tfOldHidden,openOld);
			supportClose(tfOld, closeOld);
			tfOldHidden.setText(tfOld.getText());
		}
		if(event.getSource()==closeNew){
			supportOpen(tfNewHidden,openNew);
			supportClose(tfNew, closeNew);
			tfNewHidden.setText(tfNew.getText());
		}
		if(event.getSource()==closeConfirm){
			supportOpen(tfConfirmHidden,openConfirm);
			supportClose(tfConfirm, closeConfirm);
			tfConfirmHidden.setText(tfConfirm.getText());
		}
	}
	@FXML
	public void open(MouseEvent event) {
		
		if(event.getSource()==openOld){
			supportOpen(tfOld, closeOld);
			supportClose(tfOldHidden,openOld);
			tfOld.setText(tfOldHidden.getText());
		}
		if(event.getSource()==openNew) {
			supportOpen(tfNew, closeNew);
			supportClose(tfNewHidden,openNew);
			tfNew.setText(tfNewHidden.getText());
		}
		if(event.getSource()==openConfirm) {
			supportOpen(tfConfirm, closeConfirm);
			supportClose(tfConfirmHidden,openConfirm);
			tfConfirm.setText(tfConfirmHidden.getText());
		}
	}
	public void loadDataUpdateById(myAccountController m) {
		ResultSet user = this.userModel.getUserById(AuthenticationModel.id);
		try {
			if(user.next()) {
				lblName.setText(user.getString("user_name"));
				lblRole.setText(user.getString("role_name"));
				phone=user.getString("phone");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    @FXML
    void check(KeyEvent event) {
    	tfConfirm.setDisable(true);
		tfConfirmHidden.setDisable(true);
		openConfirm.setDisable(true);
    	if(validatedNew(tfNew.getText())||validatedNew(tfNewHidden.getText())) {
    		tfConfirm.setDisable(false);
    		tfConfirmHidden.setDisable(false);
    		openConfirm.setDisable(false);
    		lblConfirmPass.setText("");
    	}

    }
    @FXML
    void supportOld(KeyEvent event) {
    	if(event.getSource()==tfOld) {
    		tfOldHidden.setText(tfOld.getText());
    	}
    	if(event.getSource()==tfOldHidden) {
    		tfOld.setText(tfOldHidden.getText());
    	}
    }
    @FXML
    void match(KeyEvent evt) {
    	lblConfirmPass.setText("not match");
    	if(evt.getSource()==tfConfirm&&(tfConfirm.getText().equals(tfNew.getText())||tfConfirm.getText().equals(tfNewHidden.getText()))) {
    		lblConfirmPass.setText("match!");
    	}
    	if(evt.getSource()==tfConfirmHidden&&(tfConfirmHidden.getText().equals(tfNew.getText())||tfConfirmHidden.getText().equals(tfNewHidden.getText()))) {
    		lblConfirmPass.setText("match!");
    	}
    	

    }
	public boolean validatedNew( String newPass) {
		try {
			lblNewPass.setText("");
			ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
			data.add(new ValidationDataMapping("new pass", newPass, "lblNewPass", "required|string|min:5"));
			ArrayList<DataMapping> messages = Validations.validated(data);
			if(messages.size() > 0) {
				for(DataMapping message : messages) {
					switch(message.key) {
						case "lblNewPass":
							lblNewPass.setText(message.value);
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
	private void supportOpen(TextField tf, ImageView img) {
		tf.setVisible(true);
		img.setVisible(true);
	}
	private void supportClose(TextField tf, ImageView img) {
		tf.setVisible(false);
		img.setVisible(false);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		tfConfirmHidden.setDisable(true);
		openConfirm.setDisable(true);
		supportClose(tfConfirm, closeConfirm);
	}

}
