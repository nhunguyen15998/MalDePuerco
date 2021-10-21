/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.mysql.cj.xdevapi.PreparableStatement;

import db.MySQLJDBC;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import models.AuthenticationModel;
import models.DiscountModel;
import models.RoleModel;
import models.UserModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class DiscountController implements Initializable {
	private DiscountModel discountModel = new DiscountModel();

	private int idDiscount;
    @FXML
    private TableView<DiscountModel> tblDiscount;

    @FXML
    private TableColumn<DiscountModel, Integer> colNo;

    @FXML
    private TableColumn<DiscountModel, String>  colCode;
    @FXML
    private TableColumn<DiscountModel, Integer>  ID;
    @FXML
    private TableColumn<DiscountModel, Double>  colTotal;
    @FXML
    private TableColumn<DiscountModel, String> colName;

    @FXML
    private TableColumn<DiscountModel, Float> colDecrease;

    @FXML
    private TableColumn<DiscountModel, String>  colDescrip;

    @FXML
    private TableColumn<DiscountModel, LocalDate> colStartDate;

    @FXML
    private TableColumn<DiscountModel, LocalDate> colEndDate;

    @FXML
    private TableColumn<DiscountModel, LocalDate>  colCreatedAt;

    @FXML
    private TableColumn<DiscountModel, String>  colStatus;

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfDiscount;

    @FXML
    private TextField tfCode;
  
    @FXML
    private TextField tfDescript;
    @FXML
    private TextField tfTotal;

    @FXML
    private DatePicker dpStart;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private ComboBox<DataMapping> cbStatus;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private TextField tfDecrease;
    @FXML
    private Label lblNameError;
    @FXML
    private Label lblTotalError;

    @FXML
    private Label lblCodeError;

    @FXML
    private Label lblDescriptError;

    @FXML
    private Label lblDecreaseError;

    @FXML
    private Label lblDateError;

    @FXML
    void btnSaveAction(ActionEvent event) {
    	idDiscount = 0;
    	ucAction();
    }

    @FXML
    void btnUpdateAction(ActionEvent event) {
    	ucAction();
    	idDiscount = 0;
    }
    
    
    private void ucAction() {
		try {
			String name = tfName.getText();
			String code = tfCode.getText();
			String descript = tfDescript.getText();
			String decrease = tfDecrease.getText();
			String total = tfTotal.getText();
			
			
			String status = cbStatus.getValue() != null ? cbStatus.getValue().key : String.valueOf(DiscountModel.DISCOUNT_DEACTIVATED);

			if(validated( name, code, total,descript, decrease, dpStart.getValue(),dpEnd.getValue())) {
				if(!decrease.isEmpty()) {
					float sup = Float.parseFloat(decrease);
					float dec = sup/100;
				
				ArrayList<DataMapping> users = new ArrayList<DataMapping>();
				users.add(DataMapping.getInstance("name", name));
				users.add(DataMapping.getInstance("code", code));
				users.add(DataMapping.getInstance("descriptions", descript));
				users.add(DataMapping.getInstance("decrease", dec+""));
				users.add(DataMapping.getInstance("start_date", dpStart.getValue().toString()));
				users.add(DataMapping.getInstance("end_date", dpEnd.getValue().toString()));
				users.add(DataMapping.getInstance("status", status));
				users.add(DataMapping.getInstance("order_total", total));
				if(idDiscount ==0) {
					discountModel.createDiscount(users);
					Helpers.status("success");
		    	}
				else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Update Role Confirmation");
					alert.setHeaderText("Do you want to make this change?");
					Optional<ButtonType> option = alert.showAndWait();
					if (option.get() == ButtonType.OK) {
						discountModel.updateDiscountById(idDiscount, users);
						Helpers.status("success");
						idDiscount = 0;
					} 
					
				}
				resetInput();
				this.parseData(null);
				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
    	
    }
    
  //validate
  	public boolean validated( String name, String code, String total,String descript, String decrease, LocalDate start, LocalDate end) {
  		try {
  			
  			lblNameError.setText("");
  			lblCodeError.setText("");
  			lblDecreaseError.setText("");
  			lblDescriptError.setText("");
  			lblDateError.setText("");
  			lblTotalError.setText("");
  			java.sql.Date date1 = java.sql.Date.valueOf(start);
            java.sql.Date date2 = java.sql.Date.valueOf(end);
            boolean codeDup;
            boolean checkDate=true;
			if(idDiscount!=0) {
			codeDup =  Validations.checkDup("code", "discounts", " id !="+idDiscount+" and ", tfCode.getText(), lblCodeError, "This code already exists");
			}else {
			codeDup =  Validations.checkDup("code", "discounts", " ", tfCode.getText(), lblCodeError, "This code already exists");
			checkDate = Validations.checkDate(date1, date2, lblDateError, "Unavailable");
			
			}
            
  			ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
  			data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:5"));
  			data.add(new ValidationDataMapping("code", code, "lblCodeError", "required|min:5"));
  			data.add(new ValidationDataMapping("descript", descript, "lblDescriptError", "required"));
  			data.add(new ValidationDataMapping("descrease", decrease, "lblDecreaseError", "required|numeric|max:100|min:0"));
  			data.add(new ValidationDataMapping("total", total, "lblTotalError", "numeric|min:0"));
  			ArrayList<DataMapping> messages = Validations.validated(data);
  			if(messages.size() > 0) {
  				for(DataMapping message : messages) {
  					switch(message.key) {
  						case "lblNameError":
  							lblNameError.setText(message.value);
  							break;
  						case "lblCodeError":
  							lblCodeError.setText(message.value);
  							break;
  						case "lblDecreaseError":
  							lblDecreaseError.setText(message.value);
  							break;
  						case "lblDescriptError":
  							lblDescriptError.setText(message.value);
  							break;
  						case "lblTotalError":
  							lblTotalError.setText(message.value);
  							break;
  						default:
  							System.out.println("abcde");
  					}
  					
  				}
  				return false;
  			}
  			if(!checkDate||!codeDup) {
					return false;
				}else {

  			return true;
				}
  		} catch (Exception e) {
  			e.printStackTrace();
  			return false;
  		}
  	}
  	
    @FXML
    public void resetInput() {
    	tfName.setText("");
    	tfCode.setText("");
    	tfDescript.setText("");
    	dpStart.setValue(LocalDate.now());
    	cbStatus.getEditor().clear();
    	dpEnd.setValue(LocalDate.now());
    	btnAdd.setDisable(false);
    	btnUpdate.setDisable(true);
    	tfDecrease.setText("");
    	cbStatus.setValue(DiscountModel.isActivated);
    	dpStart.setDisable(false);
		dpEnd.setDisable(false);
		tfDecrease.setDisable(false);
		tfTotal.setText("");
    	
    }
    private void checkStartDate() {
    	String sql = "update discounts set status = 0 where curdate()<start_date or end_date<curdate()" ;
        try {
        	Statement stmt = MySQLJDBC.connection.createStatement();
    	   stmt.execute(sql);
    	}catch(SQLException  ex) {
    		ex.printStackTrace();
    	}
    	
    }
	public void parseData(ArrayList<CompareOperator> conditions) {

		checkStartDate();
		try {
			//create list, format date
			ObservableList<DiscountModel> discountList = FXCollections.observableArrayList();
					
			//cols get from model
			ID.setCellValueFactory(new PropertyValueFactory<DiscountModel, Integer>("id"));
			colNo.setCellValueFactory(new PropertyValueFactory<DiscountModel, Integer>("sequence"));
			colCode.setCellValueFactory(new PropertyValueFactory<DiscountModel, String>("code"));
			colName.setCellValueFactory(new PropertyValueFactory<DiscountModel, String>("name"));
			colDescrip.setCellValueFactory(new PropertyValueFactory<DiscountModel, String>("descriptions"));
			colStartDate.setCellValueFactory(new PropertyValueFactory<DiscountModel, LocalDate>("start_date"));
			colEndDate.setCellValueFactory(new PropertyValueFactory<DiscountModel, LocalDate>("end_date"));
			colTotal.setCellValueFactory(new PropertyValueFactory<DiscountModel, Double>("orderTotal"));
			colDecrease.setCellValueFactory(new PropertyValueFactory<DiscountModel, Float>("decrease"));
			colCreatedAt.setCellValueFactory(new PropertyValueFactory<DiscountModel, LocalDate>("createdAt"));
			//format col
			colStatus.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(				
					cellData.getValue().getStatus() == DiscountModel.DISCOUNT_ACTIVATED ? String.valueOf(DiscountModel.isActivated) : String.valueOf(DiscountModel.isDeactivated)));
			
			//get data from db
			ResultSet discounts = discountModel.getDiscountList(conditions);
			
			while(discounts.next()) {
				discountList.add(new DiscountModel(
						discounts.getInt("id"),
						discounts.getRow(),
						discounts.getString("code"),
						discounts.getString("name"),
						discounts.getString("descriptions"),
						discounts.getDate("start_date").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
						discounts.getDate("end_date").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
						discounts.getDate("created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
						discounts.getInt("status"),
						discounts.getFloat("decrease"),
						discounts.getDouble("order_total"))
					);
			}
			tblDiscount.setItems(discountList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public ArrayList<CompareOperator> getFilter(){
		
		try {
			String code = tfDiscount.getText();
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("code  like '%"+ code + "%' or name like '%"+ code + "%' or DATE_FORMAT(start_date,'%d-%m-%Y')", " like ", "%"+ code + "%"));
		
			return conditions;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		this.parseData(getFilter());
		
		resetInput();

		ObservableList<DataMapping> status = FXCollections.observableArrayList(DiscountModel.isActivated, DiscountModel.isDeactivated);
		cbStatus.setItems(status);
		//create
		btnAdd.setDisable(true);
		
		//update
		btnUpdate.setDisable(true);
		
		//delete
		btnDelete.setDisable(true);
		

		
		if(AuthenticationModel.hasPermission("CREATE_DISCOUNT") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnAdd.setDisable(false);
		}
		
		if(AuthenticationModel.hasPermission("UPDATE_DISCOUNT") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnUpdate.setDisable(false);
		}
		
		if(AuthenticationModel.hasPermission("DELETE_DISCOUNT") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnDelete.setDisable(false);
		}
		
	}

    @FXML
    void btnClearAction(ActionEvent event) {
    	try {
			tfDiscount.setText("");
			this.parseData(getFilter());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }


    @FXML
    void onSearch(KeyEvent event) {
    	try {
			this.parseData(getFilter());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    @FXML
    void getDataRowSelected(MouseEvent event) throws ParseException {
    		dpStart.setDisable(false);
    		dpEnd.setDisable(false);
    		tfDecrease.setDisable(false);
			if(event.getClickCount() > 0) {
				btnAdd.setDisable(true);
				btnUpdate.setDisable(false);
				DiscountModel item = tblDiscount.getSelectionModel().getSelectedItem();
				DataMapping status=DiscountModel.isDeactivated;
				if(item.getStatus()==1) {
					status=DiscountModel.isActivated;
				}
				idDiscount = item.getId();
				btnDelete.setDisable(true);
				if(!checkToDelete(idDiscount)) {
					btnDelete.setDisable(true);
				}
				tfDecrease.setText((int) Math.round(item.getDecrease()*100)+"");
				tfTotal.setText((int) item.getOrderTotal()+"");
				tfName.setText(item.getName());
				tfCode.setText(item.getCode());
				tfDescript.setText(item.getDescriptions());
					LocalDate now = LocalDate.now();
				    Date date1= new SimpleDateFormat("dd-MM-yyyy").parse(item.getStart_date());
				    Date date2= new SimpleDateFormat("dd-MM-yyyy").parse(item.getEnd_date());
					ZoneId defaultZoneId = ZoneId.systemDefault();
					Instant instant = date1.toInstant();
					Instant instant2 = date2.toInstant();
					LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
					LocalDate localDate2 = instant2.atZone(defaultZoneId).toLocalDate();
				dpStart.setValue(localDate);
				dpEnd.setValue(localDate2);
				cbStatus.setValue(status);
				if(now.compareTo(localDate)>0) {
					dpStart.setDisable(true);
		    		dpEnd.setDisable(true);
		    		tfDecrease.setDisable(true);
				}
			}else {
				System.out.println("not click in table");
				idDiscount=0;
			}
		
    }

    @FXML
    public void btnDeleteAction() {
    	try {
			if(this.idDiscount != 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete User Confirmation");
				alert.setHeaderText("Are you sure you want to delete this item ?");
				
				Optional<ButtonType> options = alert.showAndWait();
				if(options.get() == ButtonType.OK) {
					this.discountModel.deleteDiscountById(idDiscount);
					this.parseData(getFilter());
					idDiscount=0;
				}
			} else {
				Helpers.status("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    
    private boolean checkToDelete(int id) {
    	boolean check=true;
    	
    	String query = "select end_date from discounts where id="+id+" and end_date<curdate()";
    	try{
    		Statement ps = MySQLJDBC.connection.createStatement();
        	ResultSet rs = ps.executeQuery(query);
        	if(rs.next()) {
        		check=false;
        	}
    	}catch(SQLException  ex) {
    		ex.printStackTrace();
    	}
    	
    	return check;
    }
}
