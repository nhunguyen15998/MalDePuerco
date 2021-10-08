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
import utils.DataMapping;
import utils.Helpers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import db.MySQLJDBC;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.CheckBox;

public class ShiftController implements Initializable {
	private TableModel tableModel = new TableModel();
	private TablesController tableController;
	private int tableId;
	@FXML
	private Label lblTable;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnClear;
	@FXML
	private TextField tfShiftOf;
	@FXML
	private CheckBox chkShift;
	@FXML
	private Label lblTableName;
	@FXML
	private Label lblTableCode;

	@FXML
	public void btnSaveAction(ActionEvent event) {
		ArrayList<DataMapping> table = new ArrayList<DataMapping>();
		if(chkShift.isSelected()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Change Shift Confirmation");
			alert.setHeaderText("Do you change this shift?");
			Optional<ButtonType> option = alert.showAndWait();
			table.add(DataMapping.getInstance("user_id", AuthenticationModel.id+""));
			if (option.get() == ButtonType.OK) {
				tableModel.updateTableById(this.tableId, table);
				tfShiftOf.setText(getUserName(AuthenticationModel.id));
				Helpers.status("success");
			} 
		}else {
			table.add(DataMapping.getInstance("user_id",0+""));
			tableModel.updateTableById(this.tableId, table);
			tfShiftOf.setText("");
			Helpers.status("success");
		}
		tableController.parseData(null);
		this.btnCancelAction();
	}
	
	
	@FXML
	public void btnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
	@FXML
	public void clearAction() {
		ArrayList<DataMapping> table = new ArrayList<DataMapping>();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Clear Shift Confirmation");
		alert.setHeaderText("Do you want to make this change?");
		Optional<ButtonType> option = alert.showAndWait();
		table.add(DataMapping.getInstance("user_id",0+""));
		if (option.get() == ButtonType.OK) {
			tableModel.updateTableById(this.tableId, table);
			tfShiftOf.setText("");
			Helpers.status("success");
			btnCancelAction();
		} 
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		chkShift.setSelected(false);
		chkShift.setVisible(false);
		btnClear.setVisible(false);
		if(AuthenticationModel.roleName.equals("Server")) {
			chkShift.setVisible(true);
		}
		if(AuthenticationModel.roleName.equals("Super Admin")||AuthenticationModel.roleName.equals("Manager")) {
			btnClear.setVisible(true);
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
  					tfShiftOf.setText(getUserName(currentTable.getInt("user_id")));
  					if(currentTable.getInt("user_id")==AuthenticationModel.id) {
  						chkShift.setSelected(true);
  					}
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
		
	}
	private String getUserName(int id) {
  		String name="";
  		Connection conn = MySQLJDBC.Instance().getConn();
  		String sql = "select name from users where id="+id;
  		try {
  			PreparedStatement ps = conn.prepareStatement(sql);
  			ResultSet rs =ps.executeQuery();
  			if(rs.next()) {
  				name=rs.getString("name");
  			}
  		}catch (Exception e) {
			
		}
  		return name;
  	}
  	
}
