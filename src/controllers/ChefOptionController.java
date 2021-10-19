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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import models.OrderDetailModel;
import models.UserModel;

public class ChefOptionController implements Initializable{
	@FXML
    private ComboBox<DataMapping> cbChef;
	private UserModel userModel = new UserModel();
	private OrderDetailModel odModel = new OrderDetailModel();
	
	private int orderID;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.getChefList();
	}
	
	public ResultSet getChefList() {
		try {
			ArrayList<DataMapping> chefList = new ArrayList<DataMapping>();
			ResultSet chef = userModel.getChefById(0);
			while(chef.next()) {
				chefList.add(DataMapping.getInstance(chef.getInt("role_id"), chef.getString("name")));
			}
			cbChef.getItems().setAll(chefList);
			return chef;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@FXML void btnChefOption(ActionEvent event) {
		try {
			String chefOp = cbChef.getValue() != null ? cbChef.getValue().key : null;
			
			ArrayList<DataMapping> option = new ArrayList<DataMapping>();
			option.add(DataMapping.getInstance("user_id", chefOp));
			
			Alert al = new Alert(AlertType.CONFIRMATION);
			Optional<ButtonType> opti = al.showAndWait();
			if(opti.get() == ButtonType.OK) {
				odModel.updateOrderDetail(orderID, option);
			}
			
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
	
	
}
