package controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import db.MySQLJDBC;
import utils.DataMapping;
import utils.HandleNotifications;
import utils.Helpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import models.OrderDetailModel;
import models.OrderModel;
import models.UserModel;

public class ChefOptionSController implements Initializable{
	@FXML private ComboBox<DataMapping> cbStatus;
	private UserModel userModel = new UserModel();
	private OrderDetailModel odModel = new OrderDetailModel();
	private OrderModel orderModel = new OrderModel();
	
	private int orderID;
	private String userCode;
	private String chefID;

	ChefItemController citemControl = new ChefItemController();
	OrderChefController chefControl = new OrderChefController();
	OrderWaiterDController odControl = new OrderWaiterDController();
	
	@FXML private Button btnCancel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ObservableList<DataMapping> status = FXCollections.observableArrayList(OrderDetailModel.isCooking, OrderDetailModel.isReady, OrderDetailModel.isCanceled);
		cbStatus.setItems(status);
	}
	
	@FXML void btnStatusOption(ActionEvent event1) {
		try {
			Integer statusOp = Integer.valueOf(cbStatus.getValue() != null ? cbStatus.getValue().key : null);
			
			ArrayList<DataMapping> option1 = new ArrayList<DataMapping>();
			option1.add(DataMapping.getInstance("serving_status", String.valueOf(statusOp)));
			
			Alert al1 = new Alert(AlertType.CONFIRMATION);
			Optional<ButtonType> opti1 = al1.showAndWait();
			if(opti1.get() == ButtonType.OK) {
				odModel.updateOrderDetail(orderID, option1);
				Helpers.status("success");
				ResultSet orderDetail = odModel.getById(orderID);
				orderDetail.next();
				ResultSet order = orderModel.getOrderById(orderDetail.getInt("order_id"));
				order.next();
				
				HandleNotifications.getInstance().sendMessage("SERVER#SERVER_DISH_COOKING#"+order.getInt("table_id")+"#This item(s) is being cooked!#"+order.getInt("user_id"));
				HandleNotifications.getInstance().sendMessage("CUSTOMER#CUSTOMER_DISH_COOKING#"+order.getInt("table_id")+"#This item(s) is being cooked!#"+order.getInt("user_id"));
				System.out.println("SERVER#SERVER_DISH_COOKING#"+order.getInt("table_id")+"#This item(s) is being cooked!#"+order.getInt("user_id"));
			}
			odControl.loadData(null);
			this.close();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	//load by id
	public void loadDataById(ChefItemController citemControl) {
		try {
			this.citemControl = citemControl;
			this.orderID = citemControl.getChefID();
			ResultSet rs = this.odModel.getID(orderID);
			if(rs.next()) {
				System.out.println("order_details.serving_status");
				
				//ccb
				for(DataMapping chef : cbStatus.getItems()) {
					if(chef.key != null && Integer.parseInt(chef.key) == Integer.valueOf("order_details.serving_status"));
					cbStatus.setValue(chef);
					break;
				}
			}
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
	
	@FXML void btnCancelAction(ActionEvent event) {
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

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getChefID() {
		return chefID;
	}

	public void setChefID(String chefID) {
		this.chefID = chefID;
	}
	
	
}

