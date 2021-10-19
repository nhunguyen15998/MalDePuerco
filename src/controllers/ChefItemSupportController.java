package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.ChefItemModel;
import models.OrderDetailModel;

public class ChefItemSupportController {

    @FXML
    private Label lblCode;

    @FXML
    private Label lblStatus;
    private ChefItemModel item;
    
    public void setData(ChefItemModel items) {
    	this.item = items;
    	lblCode.setText(item.getOderCode());
    	if(item.getStatus() == 0) {
    		lblStatus.getStyleClass().add("Pending");
    	} else if(items.getStatus() == 1) {
    		lblStatus.getStyleClass().add("Cooking");
    	} else if(items.getStatus() == 2) {
    		lblStatus.getStyleClass().add("Ready");
    	} else if(items.getStatus() == 3) {
    		lblStatus.getStyleClass().add("Serving");
    	} else if(items.getStatus() == 4){
    		lblStatus.getStyleClass().add("Served");
    	} else {
    		lblStatus.getStyleClass().add("Canceled");
    	} 
    }

}
