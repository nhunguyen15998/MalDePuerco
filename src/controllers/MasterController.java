/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class MasterController implements Initializable {

    @FXML
    private Label lblCurrentUser;
    @FXML
    private Label lblCurrentUserRole;
    @FXML
    private Button btnInvoices;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnMenu;
    @FXML
    private Button btnRoles;
    @FXML
    private Button btnTables;
    @FXML
    private Button btnReservation;
    @FXML
    private Button btnServingCate;
    @FXML
    private Button btnAttributes;
    @FXML
    private Button btnDiscount;
    @FXML
    private Button btnMyAccount;
    @FXML
    private Button btnRole;
    @FXML
    private Pane masterHolder;
    @FXML
    private Button btnSignOut;
    @FXML
    private Button btnDashBoard;
    @FXML
    private Button btnUser;
    @FXML
    private Button btnOrder;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnDashBoardAction();
        
    }    


    @FXML
    private void btnCloseAction() {
        System.exit(0);
    }



    @FXML
    private void logoutAction() {
        FXMLLoader loader = new FXMLLoader(MasterController.this.getClass().getResource("/com/myfx/pjsem2/view/sign_in.fxml"));
                                  try{
                                      AnchorPane managerPane = loader.load();
                                    
                                      Stage substage=new Stage();
                                      substage.initModality(Modality.WINDOW_MODAL);
                                      substage.initStyle(StageStyle.UNDECORATED);
                                      substage.setScene(new Scene(managerPane));
                                      substage.show();  
                                     btnSignOut.getScene().getWindow().hide();
                                  }catch(IOException ex){
                                      ex.printStackTrace();
                                  }
    }
    private void redirect(String value){
        AnchorPane anchor;
		try {
			anchor = FXMLLoader.load(getClass().getResource("/view/"+value));
			masterHolder.getChildren().setAll(anchor);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void btnMyAccountAction() {
    }

    @FXML
    private void btnDashBoardAction() {
         redirect("dashboard.fxml");
    }

    @FXML
    private void menuAction() {
    }

    @FXML
    private void userAction() {
        redirect("user.fxml");
    }

    @FXML
    private void roleAction() {
        redirect("roles.fxml");
    }

    @FXML
    private void orderAction() {
    }

    @FXML
    private void tableAction() {
         redirect("tables.fxml");
    }

    @FXML
    private void reservationAction() {
        redirect("reservation.fxml");
    }

    @FXML
    private void servingCateAction() {
    }

    @FXML
    private void attributeAction() {
    }

    @FXML
    private void discountAction() {
         redirect("discount.fxml");
    }

    @FXML
    private void invoicesAction() {
    }

    @FXML
    private void btnRolesAction() { //bar
    }
    
    
    
    
    
    
    
    
}
