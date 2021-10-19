package controllers;

import java.awt.MenuItem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.ChefItemModel;
import models.OrderDetailModel;
import utils.DataMapping;

public class ChefItemController {
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
    
    private ChefItemModel item;
    
    @FXML private MenuItem chef;
    
    public void setData(ChefItemModel items) {
    	this.item = items;
    	lblCode.setText(item.getOderCode());
    	lblSerName.setText(item.getServingName());
    	lblSize.setText(item.getSize());
    	lblQuantity.setText(item.getQuantity());
    	lblCreate.setText(item.getCreatedAt());
    	lblUser.setText(item.getUserCode());
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
    	String chef = lblUser.getTypeSelector();
    	if(chef == null) {
    		System.out.println("select row");
    		return;
    	}
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/optionChef.fxml"));
    		Parent root = loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
}
