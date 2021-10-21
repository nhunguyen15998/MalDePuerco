package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import utils.CompareOperator;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.OrderDetailModel;
import models.OrderModel;
import utils.Helpers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class OrderWaiterDController implements Initializable{
	private OrderDetailModel odModel = new OrderDetailModel();
	
	public int odID;
	private String odCode;
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
    private TableColumn<OrderDetailModel, String> col_status;
    
    @FXML private Button btnCancel;
    
    @FXML private Label lblCode;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.loadData(null);
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
			col_status.setCellValueFactory(cellData -> new ReadOnlyStringWrapper (
					cellData.getValue().getServingStatus() == OrderDetailModel.PENDING ? String.valueOf(OrderDetailModel.isPending) : 
						(cellData.getValue().getServingStatus() == OrderDetailModel.COOKING ? String.valueOf(OrderDetailModel.isCooking) :
							(cellData.getValue().getServingStatus() == OrderDetailModel.READY ? String.valueOf(OrderDetailModel.isReady) :
								(cellData.getValue().getServingStatus() == OrderDetailModel.SERVING ? String.valueOf(OrderDetailModel.isServing) :
									(cellData.getValue().getServingStatus() == OrderDetailModel.SERVED ? String.valueOf(OrderDetailModel.isServed) :
										String.valueOf(OrderDetailModel.isCanceled)
											)
										)
									)
								)
					));
			
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
						rsList.getInt("serving_status")
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
			System.out.println("code:"+this.odID);
			this.odCode = this.orderController.getOrderCode();
			
			lblCode.setText("Code:" + this.odCode);
			
			ArrayList<CompareOperator> code = new ArrayList<CompareOperator>();
			code.add(CompareOperator.getInstance("orders.id", "=", String.valueOf(this.odID)));
			this.loadData(code);
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
	
		@FXML private void handleStatusOption(ActionEvent event) {
			String status = col_status.getTypeSelector();
			if(status == null) {
				System.out.println("select row");
				return ;
			}
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/optionWaiter.fxml"));
	    		Parent root = loader.load();
				Stage stage = new Stage();
				stage.setScene(new Scene(root));
				stage.show();
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

	public String getOdCode() {
		return odCode;
	}

	public void setOdCode(String odCode) {
		this.odCode = odCode;
	}

	public String getOdName() {
		return odName;
	}

	public void setOdName(String odName) {
		this.odName = odName;
	}
	
	
}
