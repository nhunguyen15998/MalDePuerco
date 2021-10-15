package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.OrderModel;
import utils.DataMapping;

public class CustomerPaymentController implements Initializable {
	private static CustomerHomeController customerHomeController = new CustomerHomeController();
	private double subtotal;
	
	//xml
	@FXML
	private FlowPane fpRequestPayment;
	@FXML
	private Label lblTableCode;
	@FXML
	private Label lblOrderCode;
	@FXML
	private AnchorPane apChildPane;
	@FXML
	private GridPane gpPaymentMethod;
	@FXML
	private RadioButton rbPaymentMethod;
	@FXML
	private Button btnPay;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblSubtotal;
	@FXML
	private TextField tfTip;
	@FXML
	private Label lblDiscount;
	@FXML
	private Label lblDeposit;
	@FXML
	private Label lblTotal;
	@FXML
	private ComboBox<DataMapping> cbDiscount;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.lblTableCode.setText(OrderModel.tableName);
	}
	
	//load data
	public void loadDataToRequestPayment(CustomerHomeController customerHomeController) {
		CustomerPaymentController.customerHomeController = customerHomeController;
		//disable main flowpane
		CustomerPaymentController.customerHomeController.customerMasterHolder.setDisable(true);
		//set lbl order code
		this.lblOrderCode.setText("Code: #" + CustomerPaymentController.customerHomeController.orderCode);
		//get vbox order
		this.apChildPane.getChildren().setAll(CustomerHomeController.vboxOrderList);
		//btnpay invisible
		CustomerPaymentController.customerHomeController.btnPay.setVisible(false);
		//getsubtotal
		this.subtotal = CustomerPaymentController.customerHomeController.totalPlace;
		this.lblSubtotal.setText("$"+this.subtotal);
		
	}

	//btnCancelAction
	public void btnCancelAction() {
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		CustomerPaymentController.customerHomeController.anchorPane.getChildren().setAll(CustomerHomeController.vboxOrderList);
		CustomerPaymentController.customerHomeController.customerMasterHolder.setDisable(false);
		CustomerPaymentController.customerHomeController.btnPay.setVisible(true);
		stage.close();
	}
}
