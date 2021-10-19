package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.DiscountModel;
import models.OrderListModel;
import models.OrderModel;
import models.PaymentMethodModel;
import models.ReservationModel;
import payments.momo.mservice.pay.Pay;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

public class CustomerPaymentController implements Initializable {
	public static CustomerHomeController customerHomeController = new CustomerHomeController();
	private DiscountModel discountModel = new DiscountModel();
	private PaymentMethodModel paymentMethodModel = new PaymentMethodModel();
	private ReservationModel reservationModel = new ReservationModel();
	private static double subtotal;
	private double total;
	private float discount = 0;
	private int tableId;
	private double tip = 0;
	private double deposit = 0;
	private String selectedPayment = "";
	private boolean paymentConfirmed = false;
	
	public int orderId;
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
	private TextField tfPhone;
	@FXML
	private Label lblDiscount;
	@FXML
	private Label lblDeposit;
	@FXML
	private Label lblTotal;
	@FXML
	private ComboBox<DataMapping> cbDiscount;
	@FXML
	private CheckBox chbDeposit;	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//set table code
		this.lblTableCode.setText(SettingController.tableName);
		this.tableId = SettingController.tableId;
		//radio
		this.renderPaymentMethodGrid();
		//cancel
		btnCancel.setOnMouseClicked(event -> {
			this.btnCancelAction();
		});
		//phone
		tfPhone.setDisable(true);
		//tip
		this.tfTip.setOnKeyPressed(event -> {
			if(event.getCode().equals(KeyCode.ENTER)) {
				this.tipAmount();
			}
		});
	}
	
	//tip
	public double tipAmount() {
		String tip = tfTip.getText().isEmpty() ? "0" : tfTip.getText();
		this.tip = Double.parseDouble(Helpers.formatNumber(null).format(Double.parseDouble(tip)));
		this.totalAmount(subtotal, this.tip, deposit, discount);
		System.out.println("total: "+this.total);	
		return this.tip;
	}
	
	//sum total
	public double totalAmount(double subtotal, double tip, double deposit, double discount) {
		double total = 0;
		total = subtotal + tip - deposit - discount;
		this.total = Double.parseDouble(Helpers.formatNumber(null).format(total));
		this.lblTotal.setText("$"+this.total);
		return this.total;
	}
	
	//load data from home
	public double loadDataToRequestPayment(CustomerHomeController customerHomeController) {
		CustomerPaymentController.customerHomeController = customerHomeController;
		this.setPaymentComponent();
		this.orderId = OrderModel.currentOrderId;
		//getsubtotal
		this.subtotal = CustomerPaymentController.customerHomeController.totalPlace;
		this.lblSubtotal.setText("$"+this.subtotal);
		//set cb discount
		ObservableList<DataMapping> discounts = this.loadDiscounts(this.subtotal);
		this.cbDiscount.setItems(discounts);
		cbDiscount.setOnAction(e -> {
			this.getComboboxItemByClicked();
		});
		this.total = CustomerPaymentController.subtotal;
		this.lblTotal.setText("$"+this.total);
		return this.total;
	}
	
	//set components
	public void setPaymentComponent() {
		//disable main flowpane
		CustomerPaymentController.customerHomeController.customerMasterHolder.setDisable(true);
		//set lbl order code
		this.lblOrderCode.setText("Code: #" + CustomerPaymentController.customerHomeController.orderCode);
		//get vbox order
		this.apChildPane.getChildren().setAll(CustomerHomeController.vboxOrderList);
		//btnpay invisible
		CustomerPaymentController.customerHomeController.btnPay.setVisible(false);
	}

	//load cbdiscount - subtotal >= order_total => load to cbb
	public ObservableList<DataMapping> loadDiscounts(double subtotal) {
		try {
			ArrayList<CompareOperator> discountCondition = new ArrayList<CompareOperator>();
			discountCondition.add(CompareOperator.getInstance("discounts.status", "=", String.valueOf(DiscountModel.DISCOUNT_ACTIVATED)));
			System.out.println("subtotal: "+CustomerPaymentController.subtotal);
			discountCondition.add(CompareOperator.getInstance("discounts.order_total", "<=", String.valueOf(subtotal)));
			ObservableList<DataMapping> data = FXCollections.observableArrayList();
			ResultSet discounts = this.discountModel.getDiscountList(discountCondition);
			while(discounts.next()) {
				data.add(DataMapping.getInstance(String.valueOf(discounts.getFloat("discounts.decrease")), 
						discounts.getString("discounts.code")));
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//click cb item
	public float getComboboxItemByClicked() {
		DataMapping item = cbDiscount.getSelectionModel().getSelectedItem();
		if(item != null) {
			float discount = Float.parseFloat(item.key);
			this.discount = Float.parseFloat(Helpers.formatNumber(null).format(discount*this.subtotal));
			this.lblDiscount.setText("($"+this.discount+")");
			this.totalAmount(subtotal, tip, deposit, this.discount);
			System.out.println("key: "+item.key);	
		}
		return this.discount;
	}
	
	//chbDepositChecked
	public void chbDepositChecked() {
		try {
			if(chbDeposit.isSelected()) {
				tfPhone.setDisable(false);
				tfPhone.setOnKeyPressed(event -> {
					if(event.getCode().equals(KeyCode.ENTER)) {
						String phone = tfPhone.getText();
						if(!phone.isEmpty()) {
							double deposit = this.loadReservation(phone);
							System.out.println("deposit::"+deposit);
							if(deposit != 0) {
								this.deposit = deposit;
								this.lblDeposit.setText("($"+this.deposit+")");
								this.totalAmount(subtotal, tip, this.deposit, discount);
								System.out.println("total: "+this.total);	
							} else {
								this.lblDeposit.setText("0");
								this.deposit = 0;
								this.totalAmount(subtotal, tip, deposit, discount);
							}
						} else {
							this.lblDeposit.setText("0");
							this.deposit = 0;
							this.totalAmount(subtotal, tip, deposit, discount);
						}						
					}
				});
			} else {
				tfPhone.setText("");
				deposit = 0;
				this.lblDeposit.setText("0");
				this.totalAmount(subtotal, tip, deposit, discount);
				tfPhone.setDisable(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//load reservation
	public double loadReservation(String phone) {
		try {
			double deposit = 0;
			LocalDate currentDate  = LocalDate.now();
			ArrayList<CompareOperator> reservation = new ArrayList<CompareOperator>();
			reservation.add(CompareOperator.getInstance("tables.id", "=", String.valueOf(this.tableId)));
			reservation.add(CompareOperator.getInstance("reservations.phone", "=", phone));
			reservation.add(CompareOperator.getInstance("reservations.date_pick", "=", String.valueOf(currentDate)));
			reservation.add(CompareOperator.getInstance("reservations.status", "=", String.valueOf(ReservationModel.RESER_PRESENT)));
			ResultSet validReserved = this.reservationModel.getReserList(reservation);
			while(validReserved.next()) {
				deposit = validReserved.getInt("reservations.deposit");
			} 
			return deposit;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//load payment method
	public ArrayList<DataMapping> loadPaymentMethods() {
		try {
			ArrayList<DataMapping> methods = new ArrayList<DataMapping>();
			ResultSet payments = this.paymentMethodModel.getPaymentMethod(null);
			while(payments.next()) {
				methods.add(DataMapping.getInstance(payments.getInt("id"), payments.getString("name")));
			}
			return methods;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//render payment method gridpane
	public ToggleGroup renderPaymentMethodGrid() {
		ToggleGroup group = new ToggleGroup();
		this.gpPaymentMethod.getChildren().clear();
		ArrayList<DataMapping> methods = this.loadPaymentMethods();
		int x = 0, y = 0;
		int count = 0;
		for(DataMapping method : methods) {  
			count++;
			this.rbPaymentMethod = new RadioButton(); //remember
			rbPaymentMethod.setText(method.value);
			rbPaymentMethod.setToggleGroup(group);//method.key
			if(count == 1) {
				rbPaymentMethod.setSelected(true);
			}
			rbPaymentMethod.setOnMouseClicked(event -> {
				selectedPayment = method.value;
			});
			
			//grid
			rbPaymentMethod.setStyle("-fx-text-fill: white;");
			this.gpPaymentMethod.add(rbPaymentMethod, x, y); 		  
			y++;	
		}
		return group;
	}
	
	//btnPayAction
	public void btnPayAction() {
		try {
			System.out.println("payment "+selectedPayment);
			//cash -> change to view pending , send noti to server -> server respond
			//-> server confirm payment -> view payment success -> clear order basket
			String path = "cash-payment-request.fxml";
			if(selectedPayment.equals("Cash")) { //->send noti to server
				this.loadView(path, 358, 272);
				this.fpRequestPayment.setDisable(true);				
			}
			
			//momo -> change to momo view -> scan qr -> momo sucess -> view payment success, noti server
			//->server confirm -> clear order basket
			if(selectedPayment.equals("Momo")) {
//				Pay abc = new Pay();
//				abc.payAction();
			}
			
			this.paymentConfirmed = true; 
			if(this.paymentConfirmed = true) { //noti server 
				path = "payment-success.fxml";
				FXMLLoader root = this.loadView(path, 690, 361);
				//controller review
				ReviewOrderController controller = root.getController();
				controller.loadOrder(this);
				//clear
			} else {
				path = "payment-failure.fxml";
				this.loadView(path, 690, 361);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//load view
	public FXMLLoader loadView(String path, double width, double height) {
		try {
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			AnchorPane payHolder;
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/"+path));
			payHolder = root.load();
			Scene scene = new Scene(payHolder, width, height);
			Stage stage = new Stage();
			stage.setX((screenBounds.getWidth() - width)/2);
			stage.setY((screenBounds.getHeight() - height)/2);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene);
			stage.show();
			
			return root;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	//btnClearAllAction
	public double btnClearAllAction() {
		if(chbDeposit.isSelected()) {
			tfPhone.setText("");
			this.lblDeposit.setText("0");
		}
		cbDiscount.getSelectionModel().clearSelection();
		cbDiscount.setPromptText("Discount");
		this.discount = 0;
		this.deposit = 0;
		this.lblDiscount.setText("0");
		double total = this.totalAmount(subtotal, tip, this.deposit, this.discount);
		return total;
	}
	
	//btnCancelAction
	public void btnCancelAction() {	
		CustomerPaymentController.customerHomeController.anchorPane.getChildren().setAll(CustomerHomeController.vboxOrderList);
		CustomerPaymentController.customerHomeController.customerMasterHolder.setDisable(false);
		CustomerPaymentController.customerHomeController.btnPay.setVisible(true);
		close();
	}
	
	//close
	public void close() {
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		stage.close();
	}
	
	
}
