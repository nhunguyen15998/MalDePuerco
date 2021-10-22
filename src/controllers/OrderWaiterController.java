package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.OrderDetailModel;
import models.OrderModel;
import models.OrderWaiterModel;
import utils.CompareOperator;
import utils.Helpers;

public class OrderWaiterController implements Initializable {
	private MasterController masterController;
	private OrderWaiterModel orderModel = new OrderWaiterModel();
	private int orderId;
	private String orderCode;
	private int status;
	private String orderName;
	
	@FXML private AnchorPane odHolder;
	@FXML private AnchorPane optionHolder;

    @FXML
    private AnchorPane anchorOrder;

    @FXML
    private TableView<OrderWaiterModel> tblOrder;

    @FXML
    private TableColumn<OrderWaiterModel, Integer> col_id;

    @FXML
    private TableColumn<OrderWaiterModel, Integer> col_no;

    @FXML
    private TableColumn<OrderWaiterModel, String> col_code;

    @FXML
    private TableColumn<OrderWaiterModel, Integer> col_table;

    @FXML
    private TableColumn<OrderWaiterModel, Integer> col_res;
    
    @FXML 
    private TableColumn<OrderWaiterModel, String> col_tip;

    @FXML
    private TableColumn<OrderWaiterModel, String> col_total;

    @FXML
    private TableColumn<OrderWaiterModel, String> col_pay;

    @FXML 
    private TableColumn<OrderWaiterModel, Integer> col_quantity;

    @FXML
    private TableColumn<OrderWaiterModel, String> col_status;

    @FXML 
    private TableColumn<OrderWaiterModel, Integer> col_user;
    
    @FXML
    private Button btnConfirm;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MasterController.orderWaiterController = this;
		// TODO Auto-generated method stub
		this.loadData(null);
	}

    //load data
	//id, sequence, code, table, res, tip, total, pay, created, status
	public void loadData(ArrayList<CompareOperator> conditions) {
		System.out.println("metqua");
		try {
			ObservableList<OrderWaiterModel> orderList = FXCollections.observableArrayList();
			
			col_id.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, Integer>("id"));
			col_no.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, Integer>("sequence"));
			col_code.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, String>("code"));
			col_table.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, Integer>("tableID"));
			col_res.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, Integer>("reservationID"));
			col_tip.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, String>("tip"));
			col_total.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, String>("total_amount"));
			col_pay.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, String>("paymentID"));
			col_quantity.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, Integer>("quantity"));
			col_user.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, Integer>("userCode"));
			col_status.setCellValueFactory(cellData -> new ReadOnlyStringWrapper (
					cellData.getValue().getStatus() == OrderWaiterModel.PENDING ? String.valueOf(OrderWaiterModel.isPending) : 
						(cellData.getValue().getStatus() == OrderWaiterModel.PROCESSING ? String.valueOf(OrderWaiterModel.isProcessing) :
							(cellData.getValue().getStatus() == OrderWaiterModel.SERVED ? String.valueOf(OrderWaiterModel.isServed) :
								String.valueOf(OrderWaiterModel.isCompleted)
									)
								)
					));
			
			/*(cellData -> new ReadOnlyStringWrapper(
					cellData.getValue().getStatus() == OrderWaiterModel.PENDING ? String.valueOf(OrderWaiterModel.isPending) :
						(cellData.getValue().getStatus() == OrderWaiterModel.PROCESSING ? String.valueOf(OrderWaiterModel.isProcessing) :
							(cellData.getValue().getStatus() == OrderWaiterModel.SERVED ? String.valueOf(OrderWaiterModel.isServed) :
								String.valueOf(OrderWaiterModel.isCompleted)
								))))));*/
			
			ResultSet item = this.orderModel.getOrderList(conditions, null);
			while(item.next()) {
				orderList.add(new OrderWaiterModel(
						item.getInt("id"),
						item.getRow(),
						item.getString("code"),
						item.getString("tblNameID"),
						item.getString("reservationCode"),
						item.getInt("tip"),
						item.getInt("total_amount"),
						item.getString("payment_id"),
						item.getInt("orderQuantity"),
						item.getInt("status"),
						item.getString("userID")
						));
				
			}
 			
			tblOrder.setItems(orderList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//on mouse click
	public void getTableByClick(MouseEvent e) {
		try {
			if(e.getClickCount() > 0) {
				OrderWaiterModel item = tblOrder.getSelectionModel().getSelectedItem();
				if(item != null) {
					this.orderCode = item.getCode();
					this.orderId = item.getId();
					System.out.println(this.orderCode);
					System.out.println(this.orderId);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
	
	//show order details
	public void showODForm() {
		try {
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/orderWaiterDetails.fxml"));
			odHolder = root.load();
			
			//controller
			OrderWaiterDController controller = root.getController();
			controller.getOrderCode(this);
			
			//scene
			Scene scene = new Scene(odHolder, 838, 550);
			Stage primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showOption() {
		try {
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/optionWaiter.fxml"));
			optionHolder = root.load();
			
			WaiterOptionController Wcontroller = root.<WaiterOptionController>getController();
			Wcontroller.loadDataById(this);
			
			Scene scene = new Scene(optionHolder, 260, 140);
			Stage optionHolder = new Stage();
			optionHolder.initStyle(StageStyle.UNDECORATED);
			optionHolder.setScene(scene);
			optionHolder.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	@FXML void btnOption(ActionEvent event) {
		try {
			if(this.orderId != 0 ) {
				this.showOption();
			} else {
				Helpers.status("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 @FXML
	    void btnOrderDetail(ActionEvent event) {
			 try {
					if(this.orderId != 0 && this.status != 3) {
						if(OrderDetailModel.isShown != true) {
							OrderDetailModel.isShown = true;
							this.showODForm();
						} else {
							Alert al = new Alert(AlertType.WARNING);
							al.setHeaderText(null);
							al.setContentText("Order completed");
							al.showAndWait();
							
						}
					} else {
						System.out.println("select a row");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	    }
	
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	//btnChefViewAction
	@FXML
	public void btnChefViewAction() {
		showChefView();
	}
	
	//parse master
	public void parseMaster(MasterController masterController) {
		this.masterController = masterController;
	}
	
	//show form assign
  	public void showChefView() {
  		try {
  			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/orderChef.fxml"));
  			AnchorPane chef = root.load();
			OrderChefController controller = root.getController();
			controller.masterController = this.masterController;		
			this.masterController.masterHolder.getChildren().setAll(chef);
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
}