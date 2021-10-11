package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.OrderWaiterModel;
import utils.CompareOperator;
import utils.Helpers;

public class OrderWaiterController implements Initializable {
	private OrderWaiterModel orderModel = new OrderWaiterModel();
	
	@FXML
    private Label label1; //orde

    @FXML
    private AnchorPane image1;

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
    
    @FXML private TableColumn<OrderWaiterModel, String> col_tip;

    @FXML
    private TableColumn<OrderWaiterModel, String> col_total;

    @FXML
    private TableColumn<OrderWaiterModel, String> col_pay;

    @FXML
    private TableColumn<OrderWaiterModel, LocalDate> col_created;

    @FXML
    private TableColumn<OrderWaiterModel, Integer> col_status;

    @FXML
    private Button btnConfirm;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.loadData(null);
	}

    //load data
	//id, sequence, code, table, res, tip, total, pay, created, status
	public void loadData(ArrayList<CompareOperator> conditions) {
		btnConfirm.setVisible(true);
		try {
			ObservableList<OrderWaiterModel> orderList = FXCollections.observableArrayList();
			col_id.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, Integer>("id"));
			col_no.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, Integer>("sequence"));
			col_code.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, String>("code"));
			col_table.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, Integer>("tableID"));
			col_res.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, Integer>("reservationID"));
			col_tip.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Helpers.formatNumber(null).format(cellData.getValue().getTip())));
			col_total.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Helpers.formatNumber(null).format(cellData.getValue().getTotal_amount())));
			col_pay.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, String>("paymentID"));
			col_created.setCellValueFactory(new PropertyValueFactory<OrderWaiterModel, LocalDate>("createdAt"));
			col_status.setCellValueFactory(cellData -> new ReadOnlyStringWrapper (
					cellData.getValue().getStatus() == OrderWaiterModel.PENDING_ORDER ? String.valueOf(OrderWaiterModel.isPending) :
						(cellData.getValue().getStatus() == OrderWaiterModel.PROCESSING_ORDER ? String.valueOf(OrderWaiterModel.isProcessing) :
							(cellData.getValue().getStatus() == OrderWaiterModel.SERVED_ORDER ? String.valueOf(OrderWaiterModel.isServed) :
								String.valueOf(OrderWaiterModel.isCompleted)
					))))));
			
			//row from db
			ResultSet orderwaiter = orderModel.getOrderList(conditions);
			while(orderwaiter.next()) {
				boolean add = orderList.add(orderModel.getInstance(
						orderwaiter.getInt("id"),
						orderwaiter.getRow(),
						orderwaiter.getString("code"),
						orderwaiter.getString("tblNameID"),
						orderwaiter.getString("reservationCode"),
						orderwaiter.getInt("tip"),
						orderwaiter.getInt("total_amount"),
						orderwaiter.getString("payment_id"),
						orderwaiter.getDate("created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy"))),
						orderwaiter.getInt("status"));
			}
			tblOrder.setItems(orderList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
