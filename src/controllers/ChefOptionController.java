package controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import db.MySQLJDBC;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import models.OrderDetailModel;
import models.UserModel;

public class ChefOptionController implements Initializable{
	@FXML
    private ComboBox<DataMapping> cbChef;
	@FXML private ComboBox<DataMapping> cbStatus;
	private UserModel userModel = new UserModel();
	private OrderDetailModel odModel = new OrderDetailModel();
	
	private int orderID;
	private String orderCode;
	private String chefID;

	ChefItemController citemControl = new ChefItemController();
	OrderChefController chefControl = new OrderChefController();
	OrderWaiterDController odControl = new OrderWaiterDController();
	
	@FXML private Button btnCancel;
	@FXML Label lblCode;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.getChefList();
	}
	
	public ResultSet getChefList() {
		try {
			ArrayList<DataMapping> chefList = new ArrayList<DataMapping>();

			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("roles.code", "=","CHEF"));
			ResultSet chef = userModel.getUserList(conditions);
			while(chef.next()) {
				chefList.add(DataMapping.getInstance(chef.getInt("id"), chef.getString("user_name")));
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
				Helpers.status("success");
			}

			chefControl.refreshAction(event);
			this.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	//load by id
	public void loadDataById(ChefItemController citemControl) {
		try {
			this.citemControl = citemControl;
			this.orderID = citemControl.getChefID();
			this.orderCode = citemControl.getOrderCode();
			
			ResultSet rs = this.odModel.getById(orderID);
			if(rs.next()) {
				System.out.println(rs.getInt("order_details.user_id"));
				for(DataMapping chef : cbChef.getItems()) {
					if(chef.key != null && Integer.parseInt(chef.key) == rs.getInt("order_details.user_id")) {
						cbChef.setValue(chef);
						break;
					}
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
		return orderCode;
	}

	public void setUserCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getChefID() {
		return chefID;
	}

	public void setChefID(String chefID) {
		this.chefID = chefID;
	}
	
	
}
