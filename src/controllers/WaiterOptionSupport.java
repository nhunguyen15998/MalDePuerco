package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.zxing.Result;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.OrderDetailModel;
import models.OrderModel;
import utils.DataMapping;
import utils.HandleNotifications;
import utils.Helpers;

public class WaiterOptionSupport implements Initializable{
	@FXML ComboBox<DataMapping> cbStatus;
	private OrderDetailModel odModel = new OrderDetailModel();
	private int orderID;
	private String orderName;
	
	@FXML
    private Label lblCode;
	private OrderWaiterDController ODDController;
	
	@FXML private Button btnCancel;

    @FXML
    void Action(ActionEvent event) {
    	try {
    		String st = cbStatus.getValue() != null ? cbStatus.getValue().key : null;
    		
    		ArrayList<DataMapping> option = new ArrayList<DataMapping>();
    		option.add(DataMapping.getInstance("serving_status", st));
    		
    		Alert al = new Alert(AlertType.CONFIRMATION);
    		Optional<ButtonType> opti = al.showAndWait();
    		if(opti.get() == ButtonType.OK) {
    			odModel.updateOrderDetail(orderID, option);
    			Helpers.status("success");
    		}
    		ODDController.refreshAction(event);
    		this.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ObservableList<DataMapping> status = FXCollections.observableArrayList(OrderDetailModel.isServing, OrderDetailModel.isServed);
		cbStatus.setItems(status);
		System.out.println("status");
	}
    
	//load data
	public void loadDataById(OrderWaiterDController ODController) {
		try {
			this.ODDController = ODController;
			this.orderID = ODController.getOdID();
			this.orderName = ODController.getOdCode();
			lblCode.setText("Code: " +this.getOrderName());
			
			ResultSet rs = this.odModel.getID(orderID);
			if(rs.next()) {
				for(DataMapping status : cbStatus.getItems()) {
					if(status.key != null & Integer.parseInt(status.key) == rs.getInt("order_details.serving_status"));
					cbStatus.setValue(status);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @FXML
    void btnCancelAction(ActionEvent event) {
    	try {
    		this.close();
    		System.out.println("close");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void close() {
    	try {
    		Stage stage = (Stage) btnCancel.getScene().getWindow();
    		stage.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
}
