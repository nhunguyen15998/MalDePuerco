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
import models.TableModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

public class SettingController implements Initializable {
	TableModel tableModel= new TableModel();
	public  static String tablet_code;
	public static int tableId;
	
	@FXML
	private Label lblRole;
	@FXML
	private Label lblWarn;
	@FXML
	private Label lblStatus;
	@FXML
	private ImageView imgStatus;
	@FXML
	private ComboBox <String> cbbCode;
	@FXML
	private Label lblTableInfo;
	@FXML
	private Button btnClear;
	
	@FXML
	private Button btnSave;
	@FXML
	private Button btnClose;
	Preferences preference;
	
	
	
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
			ResultSet results = tableModel.getData(selects, conditions, null);
			if(results.next()) {
				lblTableInfo.setText("Table name: "+results.getString("name")+"\nSeats: "+ results.getInt("seats"));
				tableId=results.getInt("id");
			}
		} catch (Exception eLogin) {
			eLogin.printStackTrace();
		
		}
	}
	//load table code
		public ResultSet getTableCodeList() {
			try {
				ObservableList<String> codeOptions = FXCollections.observableArrayList();
				String[] selects = {"code"};
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("is_set", "=", String.valueOf(TableModel.NO)));
				ResultSet code = tableModel.getData(selects, conditions, null);
				while(code.next()) {
					codeOptions.add(code.getString("code"));
				}
				cbbCode.setItems(codeOptions);
				return code;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
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
	public void btnSaveAction(ActionEvent event) {
		String choose = cbbCode.getValue().toString();
		if(!choose.equals("")) {
			SettingController.tablet_code=(preference.get("cbb", cbbCode.getValue().toString()));
			preference.put("tabletCode", SettingController.tablet_code);
			 System.out.println("new:"+SettingController.tablet_code);
			 ArrayList<DataMapping> code = new ArrayList<DataMapping>();
				code.add(DataMapping.getInstance("is_set", "1"));
						tableModel.updateTableById(SettingController.tableId, code);
						updateData();
						Helpers.status("success");
						
		} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Please select table code");
				alert.showAndWait();
		}
		
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		preference = Preferences.userNodeForPackage(SettingController.class);
		SettingController.tablet_code =(preference.get("tabletCode", ""));
		System.out.println("old: "+ SettingController.tablet_code );
		cbbCode.setValue(SettingController.tablet_code );
		updateData();
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
		lblWarn.setText("You must clear to be re-selected number of this tablet");
		
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
		cbbCode.setValue("");
		getTableInfo();
	}
	
}
