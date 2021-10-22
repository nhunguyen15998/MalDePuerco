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
import java.util.prefs.Preferences;

import app.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import models.AuthenticationModel;
import models.PermissionModel;
import models.TableModel;
import models.UserModel;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class MasterController implements Initializable {
	private UserModel userModel = new UserModel();
	private PermissionModel permissionModel = new PermissionModel();
	private CustomerHomeController customerHomeController = new CustomerHomeController();
	private CustomerOptionController customerOptionController = new CustomerOptionController();

	@FXML
	public AnchorPane anchorPane;
    @FXML
    private Label lblCurrentUser;
    @FXML
    private Label lblCurrentUserRole;
    @FXML
    private Button btnInvoices;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnServings;
    @FXML
    private Button btnOrderServer;
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
    public Pane masterHolder;
    @FXML
    private Button btnSignOut;
    @FXML
    private Button btnDashBoard;
    @FXML
    private Button btnUser;
    @FXML
    private Button btnOrderChef;

	@FXML
	public AnchorPane settingHolder;
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
		btnOrderChef.setText("  ORDERS PROCESS");
		btnOrderServer.setText("  ORDERS CONFIRM");
		if(AuthenticationModel.roleName.equals("Chef")||AuthenticationModel.roleName.equals("Server")) {
			btnOrderChef.setText("  ORDERS");
			btnOrderServer.setText("  ORDERS");
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
		
	
		//invoice
		btnInvoices.setVisible(false);
		btnInvoices.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_INVOICES") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnInvoices.setVisible(true);
			btnInvoices.setManaged(true);
		}
		//serving
		btnServings.setVisible(false);
		btnServings.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_SERVINGS") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnServings.setVisible(true);
			btnServings.setManaged(true);
		}
		//sercate
		btnServingCate.setVisible(false);
		btnServingCate.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_SERCATES") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnServingCate.setVisible(true);
			btnServingCate.setManaged(true);
		}
		//attribute
		btnAttributes.setVisible(false);
		btnAttributes.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_ATTRIBUTES") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnAttributes.setVisible(true);
			btnAttributes.setManaged(true);
		}
		btnOrderChef.setVisible(false);
		btnOrderChef.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_ORDER_CHEF") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnOrderChef.setVisible(true);
			btnOrderChef.setManaged(true);
		}
		btnOrderServer.setVisible(false);
		btnOrderServer.setManaged(false);
		
		if(AuthenticationModel.hasPermission("VIEW_ORDER_SERVER") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnOrderServer.setVisible(true);
			btnOrderServer.setManaged(true);
		}
		//setting tablet
		btnSetting.setVisible(false);
		btnSetting.setManaged(false);
		
		if(AuthenticationModel.hasPermission("SETTING_TABLET") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnSetting.setVisible(true);
			btnSetting.setManaged(true);
		}
		//serving, attribute, menu, order
		
	
			
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

        
    }    

    @FXML
    public void btnCloseAction() {
    	
    		System.exit(1);
    }
  
	public void clearSession() {
		Stage stage = SignInController.substage;
		stage.close();
	}
	
    @FXML
    private void logoutAction() {
        FXMLLoader loader = new FXMLLoader(MasterController.this.getClass().getResource("/views/home.fxml"));
                                  try{

                      			   
                                      FlowPane managerPane = loader.load();
                          			  Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                                      Stage substage = new Stage();
                                      substage.initModality(Modality.WINDOW_MODAL);
                                      substage.initStyle(StageStyle.UNDECORATED);
                                      substage.setScene(new Scene(managerPane, 1000.1, 600));
                                      substage.setX((screenBounds.getWidth() - 1000)/2);
                                      substage.setY((screenBounds.getHeight() - 600)/2);
                                      substage.show();  
                                      btnSignOut.getScene().getWindow().hide();
                                      AuthenticationModel.id = 0;
                                      AuthenticationModel.name = null;
                                      AuthenticationModel.roleName = null;
                                  }catch(IOException ex){
                                      ex.printStackTrace();
                                  }

    }
 
    
    @FXML
    private void myAccountAction() {
    	setBtnBar();
       	btnMyAccount.getStyleClass().add("btnBarFocused");
       	redirect("my-account.fxml");
    	setBtn();
    
    }

    @FXML
    private void btnDashBoardAction() {
        setBtn();
        btnDashBoard.getStyleClass().add("btnFocused");
         redirect("dashboard.fxml");
         setBtnBar();

    }

    @FXML
    private void orderServerAction() {
      setBtn();
      btnOrderServer.getStyleClass().add("btnFocused");
      setBtnBar();
      redirect("orderWaiter.fxml");      

    }
    @FXML
    private void orderChefAction() {
      setBtn();
      btnOrderChef.getStyleClass().add("btnFocused");
      setBtnBar();
      redirect("orderChef.fxml");      

    }
    @FXML
    private void servingAction() {
      setBtn();
      btnServings.getStyleClass().add("btnFocused");
      setBtnBar();
      redirect("servings.fxml");      

    }
  

    @FXML
    private void userAction() {
        redirect("user.fxml");
        setBtn();
        btnUser.getStyleClass().add("btnFocused");
        setBtnBar();

    }

    @FXML
    private void roleAction() {
        redirect("roles.fxml");
        setBtnBar();
    	btnRole.getStyleClass().add("btnBarFocused");
    	setBtn();
    }

    @FXML
    private void tableAction() {
         redirect("tables.fxml");
         setBtn();
         btnTables.getStyleClass().add("btnFocused");
         setBtnBar();

    }

    @FXML
    private void reservationAction() {
        redirect("reservation.fxml");
        setBtn();
        btnReservation.getStyleClass().add("btnFocused");
        setBtnBar();

    }

    @FXML
    private void servingCateAction() {
    	setBtn();
         btnServingCate.getStyleClass().add("btnFocused");
         setBtnBar();
         redirect("sercate.fxml");
    }

    @FXML
    private void attributeAction() {
    	setBtn();
         btnAttributes.getStyleClass().add("btnFocused");
         setBtnBar();
         redirect("attributes.fxml");
    }

    @FXML
    private void discountAction() {
         redirect("discount.fxml");
         setBtn();
         btnDiscount.getStyleClass().add("btnFocused");
         setBtnBar();

    }
    @FXML
    private void settingAction() {
    	setBtnBar();
    	setBtn();
    	try {
			//draw
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/setting.fxml"));
			settingHolder = root.load();
			//controller
			SettingController controller = root.<SettingController>getController();
			Scene scene = new Scene(settingHolder, 546, 512);
			Stage createStage = new Stage();
			createStage.setX((screenBounds.getWidth() - 546)/2);
			createStage.setY((screenBounds.getHeight() - 512)/2);
			createStage.initStyle(StageStyle.UNDECORATED);
			createStage.setScene(scene);
			createStage.show();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void invoicesAction() {
    	redirect("invoices.fxml");
    	 setBtn();
         btnInvoices.getStyleClass().add("btnFocused");
         setBtnBar();
    }

    
    private void redirect(String value){
        AnchorPane anchor;
		try {
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/"+value));
			anchor = root.load();
			masterHolder.getChildren().setAll(anchor);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void redirectOrderManagement(String value){
        AnchorPane anchor;
		try {
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/"+value));
			anchor = root.load();
			
			OrderWaiterController controller = root.getController();
			controller.parseMaster(this);//here
			
			masterHolder.getChildren().setAll(anchor);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void setBtnBar(){
    	btnSetting.getStyleClass().remove("btnBarFocused");
    	btnRole.getStyleClass().remove("btnBarFocused");
    	btnMyAccount.getStyleClass().remove("btnBarFocused");
    }
    private void setBtn() {
    	 btnInvoices.getStyleClass().remove("btnFocused");
         btnUser.getStyleClass().remove("btnFocused");
         btnOrderChef.getStyleClass().remove("btnFocused");
         btnServingCate.getStyleClass().remove("btnFocused");
         btnAttributes.getStyleClass().remove("btnFocused");
         btnOrderServer.getStyleClass().remove("btnFocused");
         btnServings.getStyleClass().remove("btnFocused");
         btnReservation.getStyleClass().remove("btnFocused");
         btnTables.getStyleClass().remove("btnFocused");
         btnDiscount.getStyleClass().remove("btnFocused");
         btnDashBoard.getStyleClass().remove("btnFocused");
    }
    
    //loadController
    public CustomerHomeController itemCustomer(CustomerHomeController customerHomeController) {
    	this.customerHomeController = customerHomeController;
    	return this.customerHomeController;
    }
    
    public CustomerOptionController itemOption(CustomerOptionController customerOptionController) {
    	this.customerOptionController = customerOptionController;
    	return this.customerOptionController;
    }
    
    //btnHomeAction
    public void btnHomeAction() {
    	Preferences preferences = Preferences.userNodeForPackage(getClass());
		TableModel.tableId = preferences.getInt("tabletId", SettingController.tableId);
    	System.out.println("MC table id "+TableModel.tableId);
    	this.customerHomeController.customerMasterHolder.setDisable(false);
    	Stage stageHome = (Stage) customerHomeController.customerMasterHolder.getScene().getWindow();
    	stageHome.show();
    	customerHomeController.loadOrderByTable();
    	Stage stageManager = (Stage) anchorPane.getScene().getWindow();
    	stageManager.close();
    	this.customerHomeController.loadMaster(this);
    }
    
    
    
}
