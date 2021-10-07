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
    private TableColumn<DiscountModel, String> colName;

    @FXML
    private TableColumn<DiscountModel, Float> colDecrease;

    @FXML
    private TableColumn<DiscountModel, String>  colDescrip;

    @FXML
    private TableColumn<DiscountModel, LocalDate> colStartDate;

    @FXML
    private TableColumn<DiscountModel, Integer> colPeriod;

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
    private DatePicker dpStart;

    @FXML
    private TextField tfPeriod;

    @FXML
    private ComboBox<DataMapping> cbStatus;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;
    @FXML
    private TextField tfDecrease;
    @FXML
    private Label lblNameError;

    @FXML
    private Label lblCodeError;

    @FXML
    private Label lblDescriptError;

    @FXML
    private Label lblDecreaseError;

    @FXML
    private Label lblPeriodError;

    @FXML
    void btnSaveAction(ActionEvent event) {
    	idDiscount = 0;
    	ucAction();
    	resetInput();
    }

    @FXML
    void btnUpdateAction(ActionEvent event) {
    	ucAction();
    	resetInput();
    	idDiscount = 0;
    }
    
    
    private void ucAction() {
    	Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		try {
			String name = tfName.getText();
			String code = tfCode.getText();
			String descript = tfDescript.getText();
			String decrease = tfDecrease.getText();
			String period = tfPeriod.getText();
			
			
			String status = cbStatus.getValue() != null ? cbStatus.getValue().key : String.valueOf(DiscountModel.DISCOUNT_DEACTIVATED);

			if(validated( name, code, descript, decrease, period)) {
				if(!decrease.isEmpty()) {
					float sup = Float.parseFloat(decrease);
					float dec = sup/100;
				
				ArrayList<DataMapping> users = new ArrayList<DataMapping>();
				users.add(DataMapping.getInstance("name", name));
				users.add(DataMapping.getInstance("code", code));
				users.add(DataMapping.getInstance("descriptions", descript));
				users.add(DataMapping.getInstance("decrease", dec+""));
				users.add(DataMapping.getInstance("period", period));
				users.add(DataMapping.getInstance("start_date", dpStart.getValue().toString()));
				users.add(DataMapping.getInstance("status", status));
				
				if(idDiscount ==0) {
					discountModel.createDiscount(users);
					Helpers.status("success");
		    	}
				else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Update Role Confirmation");
					alert.setHeaderText("Do you want to make this change?");
					alert.setX(screenBounds.getWidth() - 880);
					alert.setY(screenBounds.getHeight() - 580);
					Optional<ButtonType> option = alert.showAndWait();
					if (option.get() == ButtonType.OK) {
						discountModel.updateDiscountById(idDiscount, users);
						Helpers.status("success");
						idDiscount = 0;
					} 
					
				}
				this.parseData(null);
				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
    	
    }
    
  //validate
  	public boolean validated( String name, String code, String descript, String decrease, String period) {
  		try {
  			
  			lblNameError.setText("");
  			lblCodeError.setText("");
  			lblDecreaseError.setText("");
  			lblDescriptError.setText("");
  			lblPeriodError.setText("");
  			
  			ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
  			data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:5"));
  			data.add(new ValidationDataMapping("code", code, "lblCodeError", "required|min:5"));
  			data.add(new ValidationDataMapping("descript", descript, "lblDescriptError", "required"));
  			data.add(new ValidationDataMapping("descrease", decrease, "lblDecreaseError", "required|numeric|max:100|min:0"));
  			data.add(new ValidationDataMapping("period", period, "lblPeriodError", "required|numeric"));
  			
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
  						
  						case "lblPeriodError":
  							lblPeriodError.setText(message.value);
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
  	
    @FXML
    public void resetInput() {
    	tfName.setText("");
    	tfCode.setText("");
    	tfDescript.setText("");
    	dpStart.setValue(LocalDate.now());
    	cbStatus.getEditor().clear();
    	tfPeriod.setText("");
    	btnAdd.setDisable(false);
    	btnUpdate.setDisable(true);
    	tfDecrease.setText("");
    	cbStatus.setValue(DiscountModel.isActivated);
    	
    }
    private void checkStartDate() {
    	Connection conn = MySQLJDBC.Instance().getConn();
    	String queryUpdate = "update discounts set status = 0 where curdate()<start_date or DATE_ADD(start_date, INTERVAL period DAY)<curdate() ";
    	try{
    		PreparedStatement ps = conn.prepareStatement(queryUpdate);
        	ps.execute();
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
			colPeriod.setCellValueFactory(new PropertyValueFactory<DiscountModel, Integer>("period"));
			colDecrease.setCellValueFactory(new PropertyValueFactory<DiscountModel, Float>("decrease"));
			colCreatedAt.setCellValueFactory(new PropertyValueFactory<DiscountModel, LocalDate>("createdAt"));
			//format col
			colStatus.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(				
					cellData.getValue().getStatus() == DiscountModel.DISCOUNT_ACTIVATED ? String.valueOf(DiscountModel.isActivated) : String.valueOf(DiscountModel.isDeactivated)));
			
			//get data from db
			ResultSet discounts = discountModel.getDiscountList(conditions);
			
			while(discounts.next()) {
				discountList.add(DiscountModel.getInstance(
						discounts.getInt("id"),
						discounts.getRow(),
						discounts.getString("code"),
						discounts.getString("name"),
						discounts.getString("descriptions"),
						discounts.getDate("start_date").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
						discounts.getInt("period"),
						discounts.getDate("created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
						discounts.getInt("status"),
						discounts.getFloat("decrease"))
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
			conditions.add(CompareOperator.getInstance("code or name or DATE_FORMAT(start_date,'%d-%m-%Y')", " like ", "%"+ code + "%"));
		
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
    	
			if(event.getClickCount() > 0) {
				btnAdd.setDisable(true);
				btnUpdate.setDisable(false);
				DiscountModel item = tblDiscount.getSelectionModel().getSelectedItem();
				DataMapping status=DiscountModel.isDeactivated;
				if(item.getStatus()==1) {
					status=DiscountModel.isActivated;
				}
				idDiscount = item.getId();
				tfDecrease.setText((int) Math.round(item.getDecrease()*100)+"");
				tfName.setText(item.getName());
				tfCode.setText(item.getCode());
				tfDescript.setText(item.getDescriptions());
				tfPeriod.setText(item.getPeriod()+"");
				
				    Date date1= new SimpleDateFormat("dd-MM-yyyy").parse(item.getStart_date());
					ZoneId defaultZoneId = ZoneId.systemDefault();
					Instant instant = date1.toInstant();
					LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
				dpStart.setValue(localDate);
				cbStatus.setValue(status);
			}else {
				System.out.println("not click in table");
				idDiscount=0;
			}
		
    }
}
