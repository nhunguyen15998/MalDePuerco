/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.stream.IntStream;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import models.AuthenticationModel;
import models.RoleModel;
import models.UserModel;
import utils.DataMapping;
import org.springframework.security.crypto.bcrypt.BCrypt;

import app.Main;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class SignInController implements Initializable {
	public CustomerHomeController customerHomeController;

	@FXML
    private AnchorPane managerPane;
	@FXML
    private Button btnBack;
    @FXML
    private Pane paneLeft;
    @FXML
    private Pane paneRight;
    @FXML
    private  TextField tfUsername =new TextField();
    @FXML
    private Button btnSignin;
    @FXML
    private CheckBox chkRemember;
    @FXML
    private  PasswordField tfPassHidden = new PasswordField();
    @FXML
    private Label lblErrorMessage;
    private UserModel userModel = new UserModel();
	public Parent root;
	public static Stage substage;
	public static String dropSession ;
	public static int checkRemember;
	Preferences preference;
	
    /**
     * Initializes the controller class.
     */
	
	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    	//remember login
    	preference = Preferences.userNodeForPackage(SignInController.class);
    	checkRemember=preference.getInt("remind", 0);
    	System.out.println("remember:"+checkRemember);
       if(preference != null ) {
    	   if(!preference.get("tfUsername", "").isEmpty() && preference.get("tfUsername", "") != null) {
	    		   tfUsername.setText(preference.get("tfUsername", ""));
	    	       tfPassHidden.setText(preference.get("tfPassHidden", ""));
 	       if(checkRemember==1) {
    	       chkRemember.setSelected(true);
    	   }else {
    		   chkRemember.setSelected(false);
    	   }
    	   }
    	   
       }
       dropSession = preference.get("session", "1");
    	 System.out.println("session:"+dropSession);
         if(dropSession.equals("0")) {
        	 deleteLoginSession();
         	}
      	
    }    
    
    public void deleteLoginSession() {
    	preference = Preferences.userNodeForPackage(SignInController.class);
    	preference.put("session", "0");
    	dropSession = preference.get("session", "1");

   	 	System.out.println("session hehe:"+dropSession);
    	tfUsername.setText("");
    	tfPassHidden.setText("");
    }
    
    @FXML
    private void signinAction() {
    	
    	lblErrorMessage.setText("");
		try {
			ResultSet sign = userModel.loginSupport(tfUsername.getText());

			
			boolean signedIn = userModel.doLogin(tfUsername.getText(), tfPassHidden.getText());
			
			System.out.println("signIn:" + signedIn);
			if(signedIn&&sign.next()) {
				System.out.println("fereee");
				//remember login
				
				if(chkRemember.isSelected()) {
					preference.put("tfUsername",tfUsername.getText());
					preference.put("tfPassHidden", tfPassHidden.getText());
					checkRemember= (preference.getInt("remember", 1));
				    preference.putInt("remind", checkRemember);
				    System.out.println("remember:"+checkRemember);
					preference.put("session", "");
				}else {
					preference.put("tfUsername","");
					preference.put("tfPassHidden", "");
					tfUsername.setText("");
					checkRemember= (preference.getInt("remember", 0));
				    preference.putInt("remind", checkRemember);
				    System.out.println("remember:"+checkRemember);
					tfPassHidden.setText("");
				}
				
				AuthenticationModel.id = sign.getInt("id");
				AuthenticationModel.name = sign.getString("name");
				loadMaster();
				
			}else {
				lblErrorMessage.setText("Invalid login. Please try again");
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    
    //load CustomerOptionController
    public CustomerHomeController itemCustomer(CustomerHomeController customerHomeController) {
    	this.customerHomeController = customerHomeController;
    	return this.customerHomeController;
    }
    
    //btnBackAction
    @FXML
    private void btnBackAction() {
    	this.customerHomeController.customerMasterHolder.setDisable(false);
    	Stage stageHome = (Stage) this.customerHomeController.customerMasterHolder.getScene().getWindow();
    	stageHome.show();
    	Stage stageManager = (Stage) btnBack.getScene().getWindow();
    	stageManager.close();
    }
  
    private void loadMaster() {
        AnchorPane managerPane;
		try {
			FXMLLoader loader = new FXMLLoader(SignInController.this.getClass().getResource("/views/master_layout.fxml"));
			managerPane = loader.load();
			 substage =new Stage();
	         substage.initModality(Modality.WINDOW_MODAL);
	         substage.initStyle(StageStyle.UNDECORATED);
	         substage.setScene(new Scene(managerPane));
	         substage.show();  
	         btnSignin.getScene().getWindow().hide();
	    	 System.out.println("ABC");
	    	 
	    	//controller
			 MasterController controller = loader.getController();
			 this.customerHomeController.customerMasterHolder.setDisable(false);
			 controller.itemCustomer(this.customerHomeController);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void closeSignIn() {
        System.exit(0);
        
    }



    
}