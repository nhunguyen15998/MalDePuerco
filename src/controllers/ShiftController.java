package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import models.AuthenticationModel;
import models.DiscountModel;
import models.TableModel;
import models.UserModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import db.MySQLJDBC;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class ShiftController implements Initializable {
	private TableModel tableModel = new TableModel();
	private UserModel userModel = new UserModel();
	private TablesController tableController;
	private int tableId;
	@FXML
	private Label lblTable;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnClear;
	@FXML
	private ComboBox<DataMapping> cbbServer;
	@FXML
	private Label lblSupport;
	@FXML
	private Label lblUserCode;
	
	@FXML
	private Label lblTableName;
	@FXML
	private Label lblTableCode;

	@FXML
	public void btnSaveAction(ActionEvent event) {
		ArrayList<DataMapping> table = new ArrayList<DataMapping>();
		if(cbbServer.getValue()!=null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Change Shift Confirmation");
			alert.setHeaderText("Do you change this shift?");
			Optional<ButtonType> option = alert.showAndWait();
			table.add(DataMapping.getInstance("user_id",cbbServer.getValue().key));
			if (option.get() == ButtonType.OK) {
				tableModel.updateTableById(this.tableId, table);
				Helpers.status("success");
				tableController.parseData(null);
				this.btnCancelAction();
			} 
		}else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Change Shift Confirmation");
			alert.setHeaderText("Do you change this shift?");
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() == ButtonType.OK) {
				this.clearAction();
				Helpers.status("success");
				btnCancelAction();
			} 
		}
		
		
	}
	
	
	@FXML
	public void btnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		this.getUserList();
		btnClear.setDisable(true);
	}
	
	@FXML
	void showInfo() {
		if(cbbServer.getValue()!=null) {
	    btnClear.setDisable(false);
		int value =  Integer.parseInt(cbbServer.getValue() != null ? cbbServer.getValue().key : "");
		lblSupport.setVisible(true);
		lblUserCode.setText(getById("code", value));
		}else {
			lblSupport.setVisible(false);
			lblUserCode.setText("");
		}
	}
	public void loadDataUpdateById(TablesController tbl, TableView tw) {
		try {
  			this.tableController = tbl;
  			this.tableId = tbl.getTableId();
  				ResultSet currentTable = this.tableModel.getTableById(this.tableId);
  				if(currentTable.next()) {
  					lblTableName.setText(currentTable.getString("name"));
  					lblTableCode.setText(currentTable.getString("code"));
  					for(DataMapping cb : cbbServer.getItems()) {
						if(cb.key != null && Integer.parseInt(cb.key) == currentTable.getInt("user_id")) {
							cbbServer.setValue(cb);
							break;
						}
					}
  					if(cbbServer.getValue() != null) {
  					showInfo();
  					}
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
		
	}
	@FXML
	public void clearAction() {
		ArrayList<DataMapping> table = new ArrayList<DataMapping>();
		table.add(DataMapping.getInstance("user_id",0+""));
		tableModel.updateTableById(this.tableId, table);
		cbbServer.setValue(null);
	}
	private String getById(String ob,int id) {
  		String value="";
  		
  		String sql = "select "+ob+" from users where id="+id;
  		try {
  			Statement ps = MySQLJDBC.connection.createStatement();
  			ResultSet rs =ps.executeQuery(sql);
  			if(rs.next()) {
  				value=rs.getString(ob);
  			}
  		}catch (Exception e) {
			
		}
  		return value;
  	}
	
	public ResultSet getUserList() {
		try {
			ArrayList<DataMapping> userTypeOptions = new ArrayList<DataMapping>();

			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("roles.name", "=", "Server"));
			
			ResultSet userTypes = userModel.getUserList(conditions);
			while(userTypes.next()) {
				userTypeOptions.add(DataMapping.getInstance(userTypes.getInt("id"), userTypes.getString("user_name")));
				
			}
			cbbServer.getItems().setAll(userTypeOptions);
			return userTypes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
