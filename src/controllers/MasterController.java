/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AuthenticationModel;
import models.PermissionModel;
import models.UserModel;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class MasterController implements Initializable {
	private UserModel userModel = new UserModel();
	private PermissionModel permissionModel = new PermissionModel();
	
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

	@FXML
	private AnchorPane settingHolder;
    @FXML
    private Button btnSetting;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnDashBoardAction();
        
        try {
		lblCurrentUser.setText(AuthenticationModel.name);
		lblCurrentUserRole.setText(AuthenticationModel.roleName);
		ResultSet currentUser = userModel.getUserById(AuthenticationModel.id);
		if(!currentUser.next()) {
			System.exit(1);
		}
		AuthenticationModel.roleName = currentUser.getString("role_name");
		AuthenticationModel.name = currentUser.getString("user_name");
		lblCurrentUser.setText(AuthenticationModel.name);
		lblCurrentUserRole.setText(AuthenticationModel.roleName);
		
		
		//load permission
		ResultSet permissions = permissionModel.getByUserId(AuthenticationModel.id);
		ArrayList<PermissionModel> currentPermissions = new ArrayList<PermissionModel>();
		while(permissions.next()) {
			PermissionModel permission = new PermissionModel();
			permission.setId(permissions.getInt("permission_id"));
			permission.setPermissionCode(permissions.getString("permissions.code"));
			permission.setName(permissions.getString("permission_name"));
			currentPermissions.add(permission);
		}
		AuthenticationModel.permissions = currentPermissions;
		btnRole.setVisible(false);
		btnRole.setManaged(false);
//		
		if( AuthenticationModel.roleName.equals("Super Admin")) {
			btnRole.setVisible(true);
			btnRole.setManaged(true);
		
		}
		
		//user
		btnUser.setVisible(false);
		btnUser.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_USERS") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnUser.setVisible(true);
			btnUser.setManaged(true);
		}
		
		//tables
		btnTables.setVisible(false);
		btnTables.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_TABLES") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnTables.setVisible(true);
			btnTables.setManaged(true);
		}
		
	
		
		//reservation
		btnReservation.setVisible(false);
		btnReservation.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_RESERVATIONS") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnReservation.setVisible(true);
			btnReservation.setManaged(true);
		}
		
		//discount
		btnDiscount.setVisible(false);
		btnDiscount.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_DISCOUNTS") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnDiscount.setVisible(true);
			btnDiscount.setManaged(true);
		}
		
	/*
		//invoice
		btnInvoices.setVisible(false);
		btnInvoices.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_INVOICE") || AuthenticationModel.roleName.equals("Admin")) {
			btnInvoices.setVisible(true);
			btnInvoices.setManaged(true);
		}
		*/
		
	
			
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

        
    }    


    @FXML
    private void btnCloseAction() {
        System.exit(0);
    }



    @FXML
    private void logoutAction() {
        FXMLLoader loader = new FXMLLoader(MasterController.this.getClass().getResource("/views/sign_in.fxml"));
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
 
    
    @FXML
    private void myAccountAction() {
       	btnMyAccount.getStyleClass().add("btnBarFocused");
    	btnSetting.getStyleClass().remove("btnBarFocused");
    	btnRole.getStyleClass().remove("btnBarFocused");
    	redirect("my-account.fxml");
    
    }

    @FXML
    private void btnDashBoardAction() {
        btnDashBoard.getStyleClass().add("btnFocused");
         redirect("dashboard.fxml");
         btnUser.getStyleClass().remove("btnFocused");
         btnMenu.getStyleClass().remove("btnFocused");
         btnRole.getStyleClass().remove("btnFocused");
         btnServingCate.getStyleClass().remove("btnFocused");
         btnAttributes.getStyleClass().remove("btnFocused");
         btnInvoices.getStyleClass().remove("btnFocused");
         btnOrder.getStyleClass().remove("btnFocused");
         btnReservation.getStyleClass().remove("btnFocused");
         btnTables.getStyleClass().remove("btnFocused");
         btnDiscount.getStyleClass().remove("btnFocused");
    }

    @FXML
    private void menuAction() {
    	btnMenu.getStyleClass();
      btnMenu.getStyleClass().add("btnFocused");
      btnUser.getStyleClass().remove("btnFocused");
      btnServingCate.getStyleClass().remove("btnFocused");
      btnAttributes.getStyleClass().remove("btnFocused");
      btnInvoices.getStyleClass().remove("btnFocused");
      btnOrder.getStyleClass().remove("btnFocused");
      btnReservation.getStyleClass().remove("btnFocused");
      btnTables.getStyleClass().remove("btnFocused");
      btnDiscount.getStyleClass().remove("btnFocused");
      btnDashBoard.getStyleClass().remove("btnFocused");
    }

    @FXML
    private void userAction() {
        redirect("user.fxml");
        btnUser.getStyleClass().add("btnFocused");
        btnMenu.getStyleClass().remove("btnFocused");
        btnServingCate.getStyleClass().remove("btnFocused");
        btnAttributes.getStyleClass().remove("btnFocused");
        btnInvoices.getStyleClass().remove("btnFocused");
        btnOrder.getStyleClass().remove("btnFocused");
        btnReservation.getStyleClass().remove("btnFocused");
        btnTables.getStyleClass().remove("btnFocused");
        btnDiscount.getStyleClass().remove("btnFocused");
        btnDashBoard.getStyleClass().remove("btnFocused");
    }

    @FXML
    private void roleAction() {
        redirect("roles.fxml");
    	btnRole.getStyleClass().add("btnBarFocused");
    	btnSetting.getStyleClass().remove("btnBarFocused");
    	btnMyAccount.getStyleClass().remove("btnBarFocused");
    }

    @FXML
    private void orderAction() {
          btnOrder.getStyleClass().add("btnFocused");
          btnUser.getStyleClass().remove("btnFocused");
          btnMenu.getStyleClass().remove("btnFocused");
          btnServingCate.getStyleClass().remove("btnFocused");
          btnAttributes.getStyleClass().remove("btnFocused");
          btnInvoices.getStyleClass().remove("btnFocused");
          btnReservation.getStyleClass().remove("btnFocused");
          btnTables.getStyleClass().remove("btnFocused");
          btnDiscount.getStyleClass().remove("btnFocused");
          btnDashBoard.getStyleClass().remove("btnFocused");
    }

    @FXML
    private void tableAction() {
         redirect("tables.fxml");
         btnTables.getStyleClass().add("btnFocused");
         btnUser.getStyleClass().remove("btnFocused");
         btnMenu.getStyleClass().remove("btnFocused");
         btnServingCate.getStyleClass().remove("btnFocused");
         btnAttributes.getStyleClass().remove("btnFocused");
         btnInvoices.getStyleClass().remove("btnFocused");
         btnOrder.getStyleClass().remove("btnFocused");
         btnReservation.getStyleClass().remove("btnFocused");
         btnDiscount.getStyleClass().remove("btnFocused");
         btnDashBoard.getStyleClass().remove("btnFocused");
    }

    @FXML
    private void reservationAction() {
        redirect("reservation.fxml");
        btnReservation.getStyleClass().add("btnFocused");
        btnUser.getStyleClass().remove("btnFocused");
        btnMenu.getStyleClass().remove("btnFocused");
        btnServingCate.getStyleClass().remove("btnFocused");
        btnAttributes.getStyleClass().remove("btnFocused");
        btnInvoices.getStyleClass().remove("btnFocused");
        btnOrder.getStyleClass().remove("btnFocused");
        btnTables.getStyleClass().remove("btnFocused");
        btnDiscount.getStyleClass().remove("btnFocused");
        btnDashBoard.getStyleClass().remove("btnFocused");
    }

    @FXML
    private void servingCateAction() {
         btnServingCate.getStyleClass().add("btnFocused");
         btnUser.getStyleClass().remove("btnFocused");
         btnMenu.getStyleClass().remove("btnFocused");
         btnAttributes.getStyleClass().remove("btnFocused");
         btnInvoices.getStyleClass().remove("btnFocused");
         btnOrder.getStyleClass().remove("btnFocused");
         btnReservation.getStyleClass().remove("btnFocused");
         btnTables.getStyleClass().remove("btnFocused");
         btnDiscount.getStyleClass().remove("btnFocused");
         btnDashBoard.getStyleClass().remove("btnFocused");
    }

    @FXML
    private void attributeAction() {
         btnAttributes.getStyleClass().add("btnFocused");
         btnUser.getStyleClass().remove("btnFocused");
         btnMenu.getStyleClass().remove("btnFocused");
         btnServingCate.getStyleClass().remove("btnFocused");
         btnInvoices.getStyleClass().remove("btnFocused");
         btnOrder.getStyleClass().remove("btnFocused");
         btnReservation.getStyleClass().remove("btnFocused");
         btnTables.getStyleClass().remove("btnFocused");
         btnDiscount.getStyleClass().remove("btnFocused");
         btnDashBoard.getStyleClass().remove("btnFocused");
    }

    @FXML
    private void discountAction() {
         redirect("discount.fxml");
         btnDiscount.getStyleClass().add("btnFocused");
         btnUser.getStyleClass().remove("btnFocused");
         btnMenu.getStyleClass().remove("btnFocused");
         btnServingCate.getStyleClass().remove("btnFocused");
         btnAttributes.getStyleClass().remove("btnFocused");
         btnInvoices.getStyleClass().remove("btnFocused");
         btnOrder.getStyleClass().remove("btnFocused");
         btnReservation.getStyleClass().remove("btnFocused");
         btnTables.getStyleClass().remove("btnFocused");
         btnDashBoard.getStyleClass().remove("btnFocused");
    }
    @FXML
    private void settingAction() {
    	btnRole.getStyleClass().remove("btnBarFocused");
    	btnMyAccount.getStyleClass().remove("btnBarFocused");
    	try {
			//draw
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/setting.fxml"));
			settingHolder = root.load();
			
			//controller
			SettingController controller = root.<SettingController>getController();
			
			Scene scene = new Scene(settingHolder, 546, 512);
			Stage createStage = new Stage();
			createStage.setX(screenBounds.getWidth() - 1000);
			createStage.setY(screenBounds.getHeight() - 750);
			createStage.initStyle(StageStyle.UNDECORATED);
			createStage.setScene(scene);
			createStage.show();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void invoicesAction() {
         btnInvoices.getStyleClass().add("btnFocused");
         btnUser.getStyleClass().remove("btnFocused");
         btnMenu.getStyleClass().remove("btnFocused");
         btnServingCate.getStyleClass().remove("btnFocused");
         btnAttributes.getStyleClass().remove("btnFocused");
         btnOrder.getStyleClass().remove("btnFocused");
         btnReservation.getStyleClass().remove("btnFocused");
         btnTables.getStyleClass().remove("btnFocused");
         btnDiscount.getStyleClass().remove("btnFocused");
         btnDashBoard.getStyleClass().remove("btnFocused");
    }

    
    private void redirect(String value){
        AnchorPane anchor;
		try {
			anchor = FXMLLoader.load(getClass().getResource("/views/"+value));
			masterHolder.getChildren().setAll(anchor);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    
    
    
    
    
}
