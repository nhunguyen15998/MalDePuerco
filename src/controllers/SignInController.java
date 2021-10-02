/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class SignInController implements Initializable {

    @FXML
    private Pane paneLeft;
    @FXML
    private Pane paneRight;
    @FXML
    private TextField tfUsername;
    @FXML
    private Button btnSignin;
    @FXML
    private CheckBox chkRemember;
    @FXML
    private PasswordField tfPassHidden;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       tfUsername.setText("okconde");
       tfPassHidden.setText("123456");
       chkRemember.setSelected(true);
      
     
    }    

    @FXML
    private void signinAction() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/myfx/pjsem2/view/master_layout.fxml"));
                        try{
                            AnchorPane managerPane = loader.load();
                            Stage substage=new Stage();
                            substage.initModality(Modality.WINDOW_MODAL);
                            substage.setScene(new Scene(managerPane));
                            substage.show();
                            btnSignin.getScene().getWindow().hide();
                        }catch(IOException ex){
                            ex.printStackTrace();
                            
                        }
    }

    @FXML
    private void closeSignIn() {
        System.exit(0);
    }



    
}
