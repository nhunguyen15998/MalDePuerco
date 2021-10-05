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
	private int tableId;
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
		TableModel.tablet_code="";
		preference.put("tabletCode", TableModel.tablet_code);
		System.out.println("new:"+TableModel.tablet_code);
		ArrayList<DataMapping> code = new ArrayList<DataMapping>();
		code.add(DataMapping.getInstance("is_set", "0"));
		tableModel.updateTableById(this.tableId, code);
		notSet();
		this.getTableCodeList();
		Helpers.status("success");
		
	}
	@FXML
	public void btnSaveAction(ActionEvent event) {
		if(cbbCode.getValue()!=null) {
			TableModel.tablet_code=(preference.get("cbb", cbbCode.getValue().toString()));
			preference.put("tabletCode", TableModel.tablet_code);
			 System.out.println("new:"+TableModel.tablet_code);
			 ArrayList<DataMapping> code = new ArrayList<DataMapping>();
				code.add(DataMapping.getInstance("is_set", "1"));
						tableModel.updateTableById(this.tableId, code);
						Helpers.status("success");
						updateData();
		} 
		
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		preference = Preferences.userNodeForPackage(SettingController.class);
		TableModel.tablet_code =(preference.get("tabletCode", ""));
		System.out.println("old: "+ TableModel.tablet_code);
		cbbCode.setValue(TableModel.tablet_code);
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
		Path imageFile = Paths.get("@../../../../../../my_java/eclipse-workspace/MalDePuerco/src/assets/tablet_yes.png");
		btnClear.setDisable(false);
		btnSave.setDisable(true);
		cbbCode.setDisable(true);
		lblWarn.setText("You must clear to be re-selected number of this tablet");
		try {
			imgStatus.setImage(new Image(imageFile.toUri().toURL().toExternalForm()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void notSet() {
		lblStatus.setText("You HAVE NOT set \n the number for this tablet");
		Path imageFile = Paths.get("@../../../../../../my_java/eclipse-workspace/MalDePuerco/src/assets/tablet_no.png");
		btnClear.setDisable(true);
		btnSave.setDisable(false);
		lblWarn.setText("");
		cbbCode.setDisable(false);
		lblTableInfo.setText("");
		try {
			cbbCode.setValue("");
			imgStatus.setImage(new Image(imageFile.toUri().toURL().toExternalForm()));
			getTableInfo();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	}
}
