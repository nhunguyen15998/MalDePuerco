package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.OrderDetailModel;
import models.OrderModel;
import utils.CompareOperator;
import utils.DataMapping;

public class WaiterOptionController implements Initializable {
	@FXML ComboBox<DataMapping> cbStatus;
	private OrderDetailModel odModel = new OrderDetailModel();
	
	private int orderID;
	private String orderName;
	
	@FXML
    private Label lblCode;
	private OrderWaiterDController ODDController;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ObservableList<DataMapping> status = FXCollections.observableArrayList(OrderModel.isPending, OrderModel.isProcessing, OrderModel.isServed, OrderModel.isCompleted);
		cbStatus.setItems(status);
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
