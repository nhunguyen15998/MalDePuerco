package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.OrderDetailModel;
import models.OrderModel;
import models.OrderWaiterModel;
import utils.CompareOperator;
import utils.DataMapping;

public class WaiterOptionController implements Initializable {
	@FXML ComboBox<DataMapping> cbStatus;
	private OrderDetailModel odModel = new OrderDetailModel();
	private OrderWaiterModel oModel = new OrderWaiterModel();
	
	private int orderID;
	private String orderName;
	
	@FXML
    private Label lblCode;
	private OrderWaiterDController ODDController;
	private OrderWaiterController ODController;
	
	@FXML private Button btnCancel;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ObservableList<DataMapping> status = FXCollections.observableArrayList(OrderModel.isPending, OrderModel.isProcessing, OrderModel.isServed, OrderModel.isCompleted);
		cbStatus.setItems(status);
	}
	
	public void close() {
    	try {
    		Stage stage = (Stage) btnCancel.getScene().getWindow();
    		stage.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
	//load data
	public void loadDataById(OrderWaiterController ODController) {
		try {
			this.ODController = ODController;
			this.orderID = ODController.getOrderId();
			
			ResultSet rs = this.oModel.getOrderById(orderID);
			if(rs.next()) {
				for(DataMapping status : cbStatus.getItems()) {
					if(status.key != null & Integer.parseInt(status.key) == rs.getInt("status"));
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
