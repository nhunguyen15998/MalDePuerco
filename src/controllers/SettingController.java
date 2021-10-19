package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import models.AuthenticationModel;
import models.TableModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

public class SettingController implements Initializable {
	TableModel tableModel= new TableModel();
	public static String tablet_code;
	public static int tableId;
	public static String tableName = "";
	
	@FXML
	private Label lblRole;
	@FXML
	private Label lblWarn;
	@FXML
	private Label lblStatus;
	@FXML
	private ImageView imgStatus;
	@FXML
	private ComboBox <DataMapping> cbbCode;
	@FXML
	private Label lblTableInfo;
	@FXML
	private Button btnClear;
	
	@FXML
	private Button btnSave;
	@FXML
	private Button btnClose;
	public static Preferences preference;
	
	
	
	@FXML
	public void close() {
		btnClose.getScene().getWindow().hide();
	}
	
	@FXML
	public void getTableInfo() {
		try {
			
			String[] selects = {"id","seats", "name"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("code", "=", cbbCode.getValue().toString()));
			ResultSet results = tableModel.getTableList(conditions);
			if(results.next()) {
				lblTableInfo.setText("Table name: "+results.getString("name")+"\nSeats: "+ results.getInt("seats"));
			}
		} catch (Exception eLogin) {
			eLogin.printStackTrace();
		
		}
	}
	//load table code
		public void getTableCodeList() {
			try {
				ObservableList<DataMapping> codeOptions = FXCollections.observableArrayList();
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("is_set", "=", String.valueOf(TableModel.NO)));
				ResultSet code = tableModel.getTableList(conditions);
				while(code.next()) {
					codeOptions.add(DataMapping.getInstance(code.getString("id"), code.getString("code")));
				}
				cbbCode.setItems(codeOptions);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	@FXML
	public void btnClearAction(ActionEvent event) {
		SettingController.tablet_code="";
		preference.put("tabletCode", SettingController.tablet_code);
		System.out.println("new:"+SettingController.tablet_code);
		ArrayList<DataMapping> code = new ArrayList<DataMapping>();
		code.add(DataMapping.getInstance("is_set", "0"));
		tableModel.updateTableById(SettingController.tableId, code);
		notSet();
		this.getTableCodeList();
		Helpers.status("success");
		
	}
	@FXML
	public void btnSaveAction() {
		SettingController.tableId = Integer.parseInt(cbbCode.getSelectionModel().getSelectedItem().key);
		if(SettingController.tableId != 0) {
			SettingController.tablet_code=(preference.get("cbb", cbbCode.getValue().toString()));
			preference.put("tabletCode", SettingController.tablet_code);
			SettingController.tableId= (preference.getInt("tableId", tableId));
			preference.putInt("tableId", SettingController.tableId);
			System.out.println("new:"+SettingController.tablet_code+ "\n Table id: "+SettingController.tableId);			 ArrayList<DataMapping> code = new ArrayList<DataMapping>();
				code.add(DataMapping.getInstance("is_set", String.valueOf(TableModel.YES)));
						tableModel.updateTableById(SettingController.tableId, code);
						updateData();
						Helpers.status("success");
		} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Please select a table");
				alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		preference = Preferences.userNodeForPackage(SettingController.class);
		SettingController.tablet_code =(preference.get("tabletCode", ""));
		SettingController.tableId =preference.getInt("tabletId", 0);
		System.out.println("old: "+ SettingController.tablet_code +"\n Table id: "+SettingController.tableId );
		cbbCode.setValue(DataMapping.getInstance(SettingController.tableId, SettingController.tablet_code ));
		updateData();
		//invoice
		btnClear.setVisible(false);
		btnClear.setManaged(false);
		
		if(AuthenticationModel.hasPermission("CLEAR_TABLET") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnClear.setVisible(true);
			btnClear.setManaged(true);
		}
		this.getTableCodeList();
		this.getTableInfo();
		
	}
	private void updateData() {
		if(!cbbCode.getValue().toString().isEmpty()) {
			isSet();
		}else {
			notSet();
		}
	}
	private void isSet() {
		lblStatus.setText("You HAVE set \n the number for this tablet");
		Image im = new Image("/assets/tablet_yes.png");  
        imgStatus.setImage(im);
	    btnClear.setDisable(false);
		btnSave.setDisable(true);
		cbbCode.setDisable(true);
		lblWarn.setText("You must clear to re-select number of this tablet");
		
	}
	private void notSet() {
		lblStatus.setText("You HAVE NOT set \n the number for this tablet");
		Image im = new Image("/assets/tablet_no.png");  
        imgStatus.setImage(im);
		btnClear.setDisable(true);
		btnSave.setDisable(false);
		lblWarn.setText("");
		cbbCode.setDisable(false);
		lblTableInfo.setText("");
		cbbCode.setValue(DataMapping.getInstance(0, ""));
		getTableInfo();
	}
	
}
