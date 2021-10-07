package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.security.crypto.bcrypt.BCrypt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.AuthenticationModel;
import models.DiscountModel;
import models.ReservationModel;
import models.TableModel;
import models.UserModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;
import javafx.scene.control.DatePicker;

public class ReservationCUController implements Initializable {
	private ReservationModel reserModel = new ReservationModel();

	private DiscountModel disModel = new DiscountModel();
	public ReservationController reController;
	private int reserId;
	@FXML
	private AnchorPane cuHolder;
	@FXML
	private Label lblReser;
	@FXML
	private TextField tfName;
	@FXML
	private TextField tfCode;
	@FXML
	private TextField tfEmail;
	@FXML
	private TextField tfPhone;
	@FXML
	private ComboBox<DataMapping> cbbDiscount;
	@FXML
	private ComboBox<DataMapping> cbStatus;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblNameError;
	@FXML
	private Label lblEmailError;
	@FXML
	private Label lblPhoneError;
	@FXML
	private TextField tfDeposit;
	@FXML
	private TextField tfSeat;
	@FXML
	private Label lblDepoError;
	@FXML
	private Label lblSeatError;
	@FXML
	private TextField tfStart;
	@FXML
	private TextField tfEnd;
	@FXML
	private DatePicker dpDate;
	@FXML
	private Label lblTimeError;
	@FXML
	public void btnSaveAction(ActionEvent event) {
		try {
			String name = tfName.getText();
			String phone = tfPhone.getText();
			String email = tfEmail.getText();
			String start = tfStart.getText();
			String end = tfEnd.getText();
			String depo = tfDeposit.getText();
			int discount = discountId(cbbDiscount.getValue()!= null ? cbbDiscount.getValue().toString() : "");
			String code = Helpers.randomCode("RS");
			String seat = tfSeat.getText();
			String date = dpDate.getValue().toString();
			String status = cbStatus.getValue() == ReservationModel.isNew ? cbStatus.getValue().key : 
				(cbStatus.getValue()  == ReservationModel.isCancelled ? cbStatus.getValue().key : 
				(cbStatus.getValue()  == ReservationModel.isConfirmed ? cbStatus.getValue().key :
				(cbStatus.getValue()  == ReservationModel.isDeposited ? cbStatus.getValue().key :
				(cbStatus.getValue()  == ReservationModel.isExpried ? cbStatus.getValue().key: String.valueOf(ReservationModel.RESER_PRESENT)))));
			if(validated( name, phone, email, seat, depo, start, end)) {
				ArrayList<DataMapping> re = new ArrayList<DataMapping>();
				re.add(DataMapping.getInstance("customer_name", name));
				re.add(DataMapping.getInstance("phone", phone));
				if(!email.isEmpty()) {
				re.add(DataMapping.getInstance("email", email));
				}
				
				re.add(DataMapping.getInstance("seats_pick", seat));
				if(!depo.isEmpty()) {
				re.add(DataMapping.getInstance("deposit", depo));
				}
				if(depo.isEmpty()) {
					re.add(DataMapping.getInstance("deposit", "0"));
					}
				re.add(DataMapping.getInstance("start_time", start));
				re.add(DataMapping.getInstance("end_time", end));
				re.add(DataMapping.getInstance("date_pick", date));
				if(discount>=0) {
				re.add(DataMapping.getInstance("discount_id", discount+""));
				}
				re.add(DataMapping.getInstance("status", status));

				if(this.reserId == 0) {
					re.add(DataMapping.getInstance("code", code));
					reserModel.createReser(re);
					Helpers.status("success");
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Update User Confirmation");
					alert.setHeaderText("Do you want to make this change?");
					Optional<ButtonType> option = alert.showAndWait();
					if (option.get() == ButtonType.OK) {
						reserModel.updateReserById(this.reserId, re);
						Helpers.status("success");
					} 
					
				}
				reController.parseData(null);
				this.close();
				
				
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
	@FXML
	public void btnCancelAction(ActionEvent event) {
		try {
			this.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ObservableList<DataMapping> status = FXCollections.observableArrayList(ReservationModel.isNew,ReservationModel.isCancelled, ReservationModel.isConfirmed,
		ReservationModel.isDeposited, ReservationModel.isExpried, ReservationModel.isPresent);
		cbStatus.setItems(status);
		cbStatus.setValue(ReservationModel.isNew);
		this.getDecreaseList();
		dpDate.setValue(LocalDate.now());
		
	}

	//load cbb decrease
		public ResultSet getDecreaseList() {
			try {
				ArrayList<DataMapping> code = new ArrayList<DataMapping>();

				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("status", "=", ""+DiscountModel.DISCOUNT_ACTIVATED));
				ResultSet discount = disModel.getDiscountList(conditions);
				while(discount.next()) {
					code.add(DataMapping.getInstance(discount.getInt("id"), discount.getString("code")));
					
				}
				cbbDiscount.getItems().setAll(code);
				return discount;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		//load data update
	  	public void loadDataUpdateById(ReservationController r) {
	  		try {
	  		
	  			this.reController = r;
	  			this.reserId = r.getReserId();
	  			lblReser.setText("Create Reservation");
	  			if(this.reserId != 0) {
	  				lblReser.setText("Update Reservatiob");
	  				ResultSet re = this.reserModel.getReserById(this.reserId);
	  				if(re.next()) {
	  					tfCode.setText(re.getString("code"));
	  					tfName.setText(re.getString("customer_name"));
	  					tfPhone.setText(re.getString("phone"));
	  					tfEmail.setText(re.getString("email"));
	  					tfSeat.setText(re.getString("seats_pick"));
	  					tfDeposit.setText(re.getString("deposit"));
	  					tfStart.setText(re.getString("start_time"));
	  					tfEnd.setText(re.getString("end_time"));
	  					cbbDiscount.setValue(discountCode(re.getInt("discount_id")));
	  					for(DataMapping status : cbStatus.getItems()) {
	  						if(status.key != null && Integer.parseInt(status.key) == re.getInt("status")) {
	  							cbStatus.setValue(status);
	  							break;
	  						}
	  					}
	  					
	  					
	  					
	  				}
	  			}
	  		} catch (Exception e) {
	  			e.printStackTrace();
	  		}
	  	}
	  	
	  	
	  //validate
		public boolean validated( String name, String phone, String email, String seat, String depo, String start, String end) {
			try {
				
				lblNameError.setText("");
				lblPhoneError.setText("");
				lblEmailError.setText("");
				lblSeatError.setText("");
				lblDepoError.setText("");
				lblTimeError.setText("");
				ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
				data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:5"));
				data.add(new ValidationDataMapping("phone", phone, "lblPhoneError", "required|phone"));
				data.add(new ValidationDataMapping("email", email, "lblEmailError", "email|required"));
				data.add(new ValidationDataMapping("seat", seat, "lblSeatError", "min:0|numeric|required"));
				data.add(new ValidationDataMapping("deposit", depo, "lblDepoError", "numeric|min:0"));
				data.add(new ValidationDataMapping("start", start, "lblTimeError", "time|required"));
				data.add(new ValidationDataMapping("end", end, "lblTimeError", "time|required"));
				ArrayList<DataMapping> messages = Validations.validated(data);
				if(messages.size() > 0) {
					for(DataMapping message : messages) {
						switch(message.key) {
							case "lblNameError":
								lblNameError.setText(message.value);
								break;
							case "lblPhoneError":
								lblPhoneError.setText(message.value);
								break;
							case "lblEmailError":
								lblEmailError.setText(message.value);
								break;
							case "lblSeatError":
								lblSeatError.setText(message.value);
								break;
							case "lblDepoError":
								lblDepoError.setText(message.value);
								break;
							case "lblTimeError":
								lblTimeError.setText(message.value);
							
								break;
							
							default:
								System.out.println("abcde");
						}

					}
					return false;
				}

				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		 
		
	  	public DataMapping discountCode(int id) {
	  		DataMapping code = null;
	  		DiscountModel dis = new DiscountModel();
	  		try {
				String[] selects = {"code, id"};
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("id", "=", id+""));
				ResultSet rs = dis.getData(selects, conditions, null);
				if(rs.next()) {
					code=DataMapping.getInstance(rs.getInt("id"), rs.getString("code"));
				}
			} catch (Exception eLogin) {
				eLogin.printStackTrace();
				return null;
			}
	  		return code;
	  	}
	  	public int discountId(String code) {
	  		int id = 0;
	  		DiscountModel dis = new DiscountModel();
	  		try {
				String[] selects = {"id"};
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("code", "=", code));
				ResultSet rs = dis.getData(selects, conditions, null);
				if(rs.next()) {
					id=rs.getInt("id");
				}else {
					return id;
				}
			} catch (Exception eLogin) {
				eLogin.printStackTrace();
			}
	  		return id;
	  	}
}
