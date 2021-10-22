package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import utils.CompareOperator;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.OrderDetailModel;
import models.OrderModel;
import utils.Helpers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class OrderWaiterDController implements Initializable{
	private OrderDetailModel odModel = new OrderDetailModel();
	
	public int odID;
	public String odCode;
	public String odName;
	private int status;
	
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
    
    @FXML private TableColumn<OrderDetailModel, String> colNote;
    
    @FXML private Button btnCancel;
    
    @FXML private Button btnUpdate;
    @FXML private Button btnDel;
    
    @FXML private Label lblCode;

    @FXML AnchorPane createHolder;
    @FXML AnchorPane option;
    
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
			colNote.setCellValueFactory(new PropertyValueFactory<OrderDetailModel, String>("servingNote"));
			
			ResultSet rsList = this.odModel.getOrderDetailListD(conditions);
			while(rsList.next()) {
				odList.add(new OrderDetailModel(
						rsList.getInt("id"),
						rsList.getRow(),
						rsList.getString("orders.code"), 
						rsList.getString("serName"), 
						rsList.getString("size"), 
						rsList.getInt("quantity"),
						rsList.getInt("price"),
						rsList.getInt("total"),
						rsList.getString("uCode"),
						rsList.getString("time"), 
						rsList.getInt("serving_status"),
						rsList.getString("serving_note")
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
			this.odName = this.orderController.getOrderName();
			this.status = this.orderController.getStatus();
			
			lblCode.setText("Code: " + this.odCode);
			
			ArrayList<CompareOperator> code = new ArrayList<CompareOperator>();
			code.add(CompareOperator.getInstance("orders.id", "=", String.valueOf(this.odID)));
			this.loadData(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML void refreshAction(ActionEvent event) {
		this.getOrderCode(orderController);
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
		
		//update from
		public void showUpdateFrom() {
			System.out.println("Update " + this.odCode);
			try {
				FXMLLoader root = new FXMLLoader(getClass().getResource("/views/orderDetailsUpdate.fxml"));
				createHolder = root.load();
				
				OrderWaiterDUController DUcontroller = root.<OrderWaiterDUController>getController();
				DUcontroller.loadDataById(this);
				
				Scene scene = new Scene(createHolder, 600, 400);
				Stage create = new Stage();
				create.initStyle(StageStyle.UNDECORATED);
				create.setScene(scene);
				create.show();
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
		}
		
		public void showOption() {
			try {
				FXMLLoader root = new FXMLLoader(getClass().getResource("/views/optionWaiter-Support.fxml"));
				option = root.load();
				
				WaiterOptionSupport control = root.<WaiterOptionSupport>getController();
				control.loadDataById(this);
				
				Scene scene = new Scene(option, 260, 140);
				Stage option = new Stage();
				option.initStyle(StageStyle.UNDECORATED);
				option.setScene(scene);
				option.show();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
		}
		
		@FXML void btnOption(ActionEvent event) {
			try {
				if(this.odID != 0) {
					this.showOption();
				} else {
					Helpers.status("error");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@FXML
	    void btnUpdateAction(ActionEvent event) {
			try {
				if(this.odID != 0 && status == 0) {
					this.showUpdateFrom();
				} else {
					Alert al = new Alert(AlertType.WARNING);
					al.setHeaderText(null);
					al.setContentText("Cannot be edited");
					al.showAndWait();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		
		@FXML void btnDelete(ActionEvent event) {
			try {
				
				if(odID != 0 && status == 0) {
					Alert al = new Alert(AlertType.CONFIRMATION);
					al.setHeaderText("Are you sure you want delete this item? ");
					
					Optional<ButtonType> options = al.showAndWait();
					if(options.get() == ButtonType.OK) {
						this.odModel.deleteOrderDetail(odID);
						this.getOrderCode(orderController);
					}
				} else {
					Alert al = new Alert(AlertType.WARNING);
					al.setHeaderText(null);
					al.setContentText("Cannot be edited");
					al.showAndWait();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@FXML void getDataByClick(MouseEvent event) {
			if(event.getClickCount() > 0) {
				OrderDetailModel item = tblOrderD.getSelectionModel().getSelectedItem();
				if(item != null) {
					this.odID = item.getId();
					this.odName = item.getServingName();
					System.out.println(this.odID);
				}
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
