package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.OrderDetailModel;
import models.OrderModel;
import utils.CompareOperator;
import utils.Helpers;
import javafx.collections.ObservableList;

public class OrderWaiterDController implements Initializable{
	private OrderModel orderModel = new OrderModel();
	private OrderDetailModel odModel = new OrderDetailModel();
	
	public int odID;
	private String odName;
	
	private OrderWaiterController orderController;
	
	@FXML
    private TableView<OrderDetailModel> tblOrderD;

    @FXML
    private TableColumn<OrderDetailModel, Integer> col_id;

    @FXML
    private TableColumn<OrderDetailModel, Integer> col_no;

    @FXML
    private TableColumn<OrderDetailModel, String> col_code;

    @FXML
    private TableColumn<OrderDetailModel, Integer> col_serving;

    @FXML
    private TableColumn<OrderDetailModel, String> col_size;

    @FXML
    private TableColumn<OrderDetailModel, Integer> col_quantity;

    @FXML
    private TableColumn<OrderDetailModel, String> col_price;

    @FXML
    private TableColumn<OrderDetailModel, String> col_total;

    @FXML
    private TableColumn<OrderDetailModel, Integer> col_user;

    @FXML
    private TableColumn<OrderDetailModel, LocalDate> col_created;

    @FXML
    private TableColumn<OrderDetailModel, Integer> col_status;
    
    @FXML private Button btnCancel;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	//load data
	public void loadData(ArrayList<CompareOperator> conditions) {
		try {
			ObservableList<OrderDetailModel> odList = FXCollections.observableArrayList();
			
			//from model
			col_id.setCellValueFactory(new PropertyValueFactory<OrderDetailModel, Integer>("id"));
			col_no.setCellValueFactory(new PropertyValueFactory<OrderDetailModel, Integer>("sequence"));
			col_code.setCellValueFactory(new PropertyValueFactory<OrderDetailModel, String>("orderCode"));
			col_serving.setCellValueFactory(new PropertyValueFactory<OrderDetailModel, Integer>("servingName"));
			col_size.setCellValueFactory(new PropertyValueFactory<OrderDetailModel, String>("size"));
			col_quantity.setCellValueFactory(new PropertyValueFactory<OrderDetailModel, Integer>("quantity"));
			col_price.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Helpers.formatNumber(null).format(cellData.getValue().getPrice())));
			col_total.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Helpers.formatNumber(null).format(cellData.getValue().getTotal())));
			col_user.setCellValueFactory(new PropertyValueFactory<OrderDetailModel, Integer>("userCode"));
			col_created.setCellValueFactory(new PropertyValueFactory<OrderDetailModel, LocalDate>("createdAt"));
			col_status.setCellValueFactory(new PropertyValueFactory<OrderDetailModel, Integer>("status"));
			
			ResultSet rsList = this.odModel.getOrderDetailListD(conditions);
			while(rsList.next()) {
				odList.add(new OrderDetailModel(
						rsList.getInt("id"),
						rsList.getRow(),
						rsList.getString("code"), 
						rsList.getString("serName"), 
						rsList.getString("size"), 
						rsList.getInt("quantity"),
						rsList.getInt("price"),
						rsList.getInt("total"),
						rsList.getString("uCode"),
						rsList.getString("time"), 
						rsList.getInt("status")
						));
			}
			tblOrderD.setItems(odList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//get order code
	public void getOrderCode(OrderWaiterController orderController) {
		try {
			this.orderController = orderController;
			this.odID = this.orderController.getOrderId();
			System.out.println(this.odID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//cancel
		public void btnCancelAction() {
			try {
				this.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void close() {
			try {
				OrderDetailModel.isShown = false;
				Stage stage = (Stage) btnCancel.getScene().getWindow();
				stage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	//get & set
	
	public int getOdID() {
		return odID;
	}

	public void setOdID(int odID) {
		this.odID = odID;
	}

	public String getOdName() {
		return odName;
	}

	public void setOdName(String odName) {
		this.odName = odName;
	}
	
	
}
