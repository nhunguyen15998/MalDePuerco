package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import models.OrderModel;
import models.ReviewOrderModel;
import utils.DataMapping;

public class ReviewOrderController implements Initializable{
	public static CustomerHomeController customerHomeController = new CustomerHomeController();
	private ReviewOrderModel reviewOrderModel = new ReviewOrderModel();
	private CustomerPaymentController customerPaymentController = new CustomerPaymentController();
	private int orderId;
	private int rbSelected;
	private static ToggleGroup group;
	//xml
	@FXML
	private RadioButton rbESatisfied;
	@FXML
	private RadioButton rbSatisfied;
	@FXML
	private RadioButton rbNormal;
	@FXML
	private RadioButton rbDisappointed;
	@FXML
	private RadioButton rbEDisappointed;
	@FXML
	private TextArea taComment;
	@FXML
	private Button btnCancel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.renderRbRating();
	}
	
	//
	public int renderRbRating() {
		group = new ToggleGroup();
		rbESatisfied.setToggleGroup(group);
		rbSatisfied.setToggleGroup(group);
		rbNormal.setToggleGroup(group);
		rbDisappointed.setToggleGroup(group);
		rbEDisappointed.setToggleGroup(group);
		rbESatisfied.setOnMouseClicked(event -> {
			this.rbSelected = ReviewOrderModel.EXTREMELY_SATISFIED;
			System.out.println(this.rbSelected);
		});
		rbSatisfied.setOnMouseClicked(event -> {
			this.rbSelected = ReviewOrderModel.SATISFIED;
			System.out.println(this.rbSelected);
		});
		rbNormal.setOnMouseClicked(event -> {
			this.rbSelected = ReviewOrderModel.NORMAL;
			System.out.println(this.rbSelected);
		});
		rbDisappointed.setOnMouseClicked(event -> {
			this.rbSelected = ReviewOrderModel.DISAPPOINTED;
			System.out.println(this.rbSelected);
		});
		rbEDisappointed.setOnMouseClicked(event -> {
			this.rbSelected = ReviewOrderModel.EXTREMELY_DISAPPOINTED;
			System.out.println(this.rbSelected);
		});
		return this.rbSelected;
	}
	
	//load order
	public int loadOrder(CustomerPaymentController customerPaymentController) {
		this.customerPaymentController = customerPaymentController;
		this.customerHomeController = customerPaymentController.customerHomeController;
		return this.orderId = this.customerPaymentController.orderId;
	}
	
	//create review
	public ArrayList<DataMapping> insertReviewOrder() {
		try {
			int rate = this.renderRbRating();
			String comment = this.taComment.getText().isEmpty() ? null : this.taComment.getText();
			ArrayList<DataMapping> data = new ArrayList<DataMapping>();
			data.add(DataMapping.getInstance("order_id", String.valueOf(this.orderId)));
			data.add(DataMapping.getInstance("rate", String.valueOf(rate)));
			data.add(DataMapping.getInstance("comment", comment));
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//btnSendAction
	public int btnSendAction(){
		ArrayList<DataMapping> data = this.insertReviewOrder();
		int reviewId = this.reviewOrderModel.createReviewOrder(data);
		//close CustomerPaymentController + clear basket
		this.customerPaymentController.close();
//		CustomerHomeController.updatedList.clear();
//		CustomerHomeController.vboxOrderList.getChildren().clear();
		close();
		customerHomeController.customerMasterHolder.setDisable(false);
		return reviewId;
	}
	//btnCancelAction
	public void btnCancelAction() {
		//close CustomerPaymentController + clear basket
		this.customerPaymentController.close();
		customerHomeController.customerMasterHolder.setDisable(false);
//		CustomerPaymentController.customerHomeController.emptyOrderList();
//		OrderModel.currentOrderId = 0;
//		CustomerPaymentController.customerHomeController.lblOrderCode.setText("");
//		CustomerHomeController.updatedList.clear();
//		CustomerHomeController.vboxOrderList.getChildren().clear();
		close();
	}
	
	//close
	public void close() {
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		stage.close();
	}
}
