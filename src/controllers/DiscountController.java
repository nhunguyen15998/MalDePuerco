/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import models.DiscountModel;
import models.RoleModel;
import models.UserModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class DiscountController implements Initializable {
	private DiscountModel discountModel = new DiscountModel();
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
    void btnSaveAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateAction(ActionEvent event) {

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
    	cbStatus.setValue(DiscountModel.isActivated);
    	
    }

	public void parseData(ArrayList<CompareOperator> conditions) {
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
			tblDiscount.setItems(discountList);;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<CompareOperator> getFilter(){
		try {
			String code = tfCode.getText();
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("code or name or start_date", " like ", "%"+ code + "%"));
			//conditions.add(new CompareOperators("", name, name))
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
				DataMapping status=item.isDeactivated;
				if(item.getStatus()==1) {
					status=item.isActivated;
				}
				tfDecrease.setText(item.getDecrease()*100+"");
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
			}
		
    }
}
