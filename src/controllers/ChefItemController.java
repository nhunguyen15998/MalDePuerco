package controllers;

import java.awt.MenuItem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ChefItemModel;
import models.OrderDetailModel;
import utils.DataMapping;
import utils.Helpers;

public class ChefItemController {
	private int chefID;
	private String table;
	private String orderCode;
	
	@FXML
    private AnchorPane anchor;

    @FXML
    private Label lblCode;

    @FXML
    private Label lblSerName;

    @FXML
    private Label lblSize;

    @FXML
    private Label lblQuantity;

    @FXML
    private Label lblCreate;

    @FXML
    private Label lblUser;

    @FXML
    private Label lblStatus;
    
    @FXML
    private Label lblChef;
    @FXML private Label lblNote;
    
    private ChefItemModel item;
    
    @FXML private MenuItem chef;
    
    @FXML private AnchorPane optionHolder;
    @FXML private AnchorPane Holder;
    
    public void setData(ChefItemModel items) {
    	this.item = items;
    	lblCode.setText(item.getOderCode());
    	lblSerName.setText(item.getServingName());
    	lblNote.setText(item.getNote());
    	lblSize.setText(item.getSize());
    	lblQuantity.setText(item.getQuantity());
    	lblCreate.setText(item.getCreatedAt());
    	lblUser.setText(String.valueOf(item.getUserCode()));
    	//pending, cooking, ready, serving, served, canceled
    	DataMapping status = OrderDetailModel.isPending;
    	
    	if(items.getStatus() == 1) {
    		status = OrderDetailModel.isCooking;
    		lblStatus.getStyleClass().add("Cooking");
    		anchor.getStyleClass().add("btnCooking");
    	} else if(items.getStatus() == 2) {
    		status = OrderDetailModel.isReady;
    		lblStatus.getStyleClass().add("Ready");
    		anchor.getStyleClass().add("btnReady");
    	} else if(items.getStatus() == 3) {
    		status = OrderDetailModel.isServing;
    		lblStatus.getStyleClass().add("Serving");
    		anchor.getStyleClass().add("btnServing");
    	} else if(items.getStatus() == 4){
    		status = OrderDetailModel.isServed;
    		lblStatus.getStyleClass().add("Served");
    		anchor.getStyleClass().add("btnServed");
    	} else if(items.getStatus() == 5) {
    		status = OrderDetailModel.isCanceled;
    		lblStatus.getStyleClass().add("Canceled");
    		anchor.getStyleClass().add("btnCanceled");
    	} else {
    		lblStatus.getStyleClass().add("Pending");
    		anchor.getStyleClass().add("btnPending");
    	}
    	lblStatus.setText(status+"");
    }
    
    @FXML
    private void handleChefOption(ActionEvent event) {
    	System.out.println("Chef Optio n");
    	try {
    		FXMLLoader root = new FXMLLoader(getClass().getResource("/views/optionChef.fxml"));
    		optionHolder = root.load();
    		
    		ChefOptionController control = root.<ChefOptionController>getController();
    		control.loadDataById(this);
    		
    		Scene scene = new Scene(optionHolder, 260, 140);
    		Stage optionStage = new Stage();
    		optionStage.initStyle(StageStyle.UNDECORATED);
    		optionStage.setScene(scene);
    		optionStage.show(); 
    	} catch (Exception e) {
    		e.printStackTrace();
    	    System.out.println(e);
    	}
    }
    
    @FXML private void handleStatusOption(ActionEvent event) {
    	System.out.println("Status Option");
    	try {
    		FXMLLoader root = new FXMLLoader(getClass().getResource("/views/optionChefS.fxml"));
    		Holder = root.load();
    		
    		ChefOptionSController control = root.<ChefOptionSController>getController();
    		control.loadDataById(this);
    		
    		Scene scene = new Scene(Holder, 260, 140);
    		Stage optionStage = new Stage();
    		optionStage.initStyle(StageStyle.UNDECORATED);
    		optionStage.setScene(scene);
    		optionStage.show();
    	} catch (Exception e) {
    		e.printStackTrace();
    	    System.out.println(e);
    	}
    }

    public void btnClick(MouseEvent event, int id) {
		switch (event.getButton()) {
		case PRIMARY:
			break;
		case SECONDARY:
			chefID = id;
			break;
		  default:
			break;
		}
	}
    
	public int getChefID() {
		return chefID;
	}


	public void setChefID(int chefID) {
		this.chefID = chefID;
	}


	public String getTable() {
		return table;
	}


	public void setTable(String table) {
		this.table = table;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
    
    
}
