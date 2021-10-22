package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.OrderDetailModel;
import models.OrderModel;
import models.OrderWaiterModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

public class WaiterOptionController implements Initializable {
	@FXML ComboBox<DataMapping> cbStatus;
	private OrderWaiterModel oModel = new OrderWaiterModel();
	
	private int orderID;
	private String orderName;
	
	@FXML
    private Label lblCode;
	private OrderWaiterController ODController;
	
	@FXML private Button btnCancel;

    @FXML
    void Action(ActionEvent event) {
    	try {
    		String st = cbStatus.getValue() != null ? cbStatus.getValue().key : null;
    		
    		ArrayList<DataMapping> option = new ArrayList<DataMapping>();
    		option.add(DataMapping.getInstance("status", st));
    		
    		Alert al = new Alert(AlertType.CONFIRMATION);
    		Optional<ButtonType> opti = al.showAndWait();
    		if(opti.get() == ButtonType.OK) {
    			oModel.updateOrder(orderID, option);
    			Helpers.status("success");
    		}
    		ODController.loadData(null);
    		this.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ObservableList<DataMapping> status = FXCollections.observableArrayList(OrderModel.isPending, OrderModel.isProcessing, OrderModel.isServed, OrderModel.isCompleted);
		cbStatus.setItems(status);
	}
    
	//load data
	public void loadDataById(OrderWaiterController ODController) {
		try {
			this.ODController = ODController;
			this.orderID = ODController.getOrderId();
			this.orderName = ODController.getOrderCode();
			lblCode.setText("Code: " +this.getOrderName());
			
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
