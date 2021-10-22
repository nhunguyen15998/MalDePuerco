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
import javafx.scene.control.TextField;
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
import utils.ValidationDataMapping;
import utils.Validations;

public class SettingController implements Initializable {
	TableModel tableModel= new TableModel();
	public  static String tablet_code;
	public static int tableId;
	public static int timeBook;
	public static int timeCancel;
	@FXML
	private Label lblRole;
	@FXML
	private Label lblWarn;
	@FXML
	private Label lblStatus;
	@FXML
	private Label lblBookError;
	@FXML
	private Label lblCancelError;
	@FXML
	private TextField tfBook;
	@FXML
	private TextField tfCancel;
	@FXML
	private ImageView imgStatus;
	@FXML
	private ComboBox <DataMapping> cbbCode;//nhu
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
			ResultSet results = tableModel.getData(selects, conditions, null, null, null, null);
			if(results.next()) {
				lblTableInfo.setText("Table name: "+results.getString("name")+"\nSeats: "+ results.getInt("seats"));
				tableId=results.getInt("id");
				System.out.println("tableid select:"+tableId);
			}
		} catch (Exception eLogin) {
			eLogin.printStackTrace();
		
		}
	}
	//load table code
	public ResultSet getTableCodeList() {
		try {
			ObservableList<DataMapping> codeOptions = FXCollections.observableArrayList();//string ->dm nhu
			String[] selects = {"id, code"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("is_set", "=", String.valueOf(TableModel.NO)));
			ResultSet code = tableModel.getData(selects, conditions, null, null, null, null);
			while(code.next()) {
				codeOptions.add(DataMapping.getInstance(code.getString("id"), code.getString("code")));
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
		ArrayList<DataMapping> code = new ArrayList<DataMapping>();
		code.add(DataMapping.getInstance("is_set", "0"));
		tableModel.updateTableById(SettingController.tableId, code);
		SettingController.tableId=0;
		preference.putInt("tabletId", SettingController.tableId);
		System.out.println("NEW CODE:"+SettingController.tablet_code +"\nNEW ID: "+SettingController.tableId);
		notSet();
		this.getTableCodeList();
		Helpers.status("success");
		
	}
	@FXML
	public void btnSaveAction(ActionEvent event) {
		String key = cbbCode.getValue() != null ? cbbCode.getValue().key : null;
		System.out.println(SettingController.tableId);
		System.out.println("choose: "+key);
		//if reselect => choose
		if(key != null) {
			SettingController.tablet_code = cbbCode.getValue().value;
			SettingController.tableId = Integer.parseInt(key);
			preference.put("tabletCode", SettingController.tablet_code);
			preference.putInt("tabletId", SettingController.tableId);
			System.out.println("table: "+SettingController.tableId);
			System.out.println("key: "+key);
			ArrayList<DataMapping> code = new ArrayList<DataMapping>();
			code.add(DataMapping.getInstance("is_set", "1"));
			tableModel.updateTableById(SettingController.tableId, code);
			updateData();
		} 
		
		if(!tfBook.getText().isEmpty()) {
			if(validated(tfBook.getText(), "lblBookError")) {
			SettingController.timeBook = Integer.parseInt((preference.get("timeB", tfBook.getText())));
			System.out.println("Time book new: " + SettingController.timeBook );
			preference.putInt("timeBook", SettingController.timeBook);
			}
		}
		if(!tfCancel.getText().isEmpty()) {
			if(validated(tfCancel.getText(), "lblCancelError")) {
			SettingController.timeCancel = Integer.parseInt((preference.get("timeC", tfCancel.getText())));
			System.out.println("Time cancel new:" + SettingController.timeCancel );
			preference.putInt("timeCancel", SettingController.timeCancel);
			}
		}

		if(key.equals("")&&tfBook.getText().isEmpty()&&tfCancel.getText().isEmpty()){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Please fill in the input");
				alert.showAndWait();
		}
		if(validated(tfBook.getText(), "lblBookError")&&validated(tfCancel.getText(), "lblCancelError")) {
		Helpers.status("success");
		}	
	}
	
	
	private boolean validated(String time, String lbl) {
		try{
			ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
				data.add(new ValidationDataMapping("time", time, lbl, "numeric|min:0"));
				ArrayList<DataMapping> messages = Validations.validated(data);
				if(messages.size() > 0) {
					for(DataMapping message : messages) {
						switch(message.key) {
							case "lblCancelError":
								lblCancelError.setText(message.value);
								break;
							case "lblBookError":
								lblBookError.setText(message.value);
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		preference = Preferences.userNodeForPackage(SettingController.class);
		
		SettingController.tablet_code =(preference.get("tabletCode", ""));
		SettingController.tableId =preference.getInt("tabletId", 0);
		System.out.println("OLD CODE: " + SettingController.tablet_code );
		System.out.println("OLD ID: " + SettingController.tableId );
		cbbCode.setValue(DataMapping.getInstance(SettingController.tableId, SettingController.tablet_code));
		
		updateData();
		
		SettingController.timeBook= preference.getInt("timeBook",2);
		SettingController.timeCancel = preference.getInt("timeCancel", 2);
		cbbCode.setValue(DataMapping.getInstance(SettingController.tableId, SettingController.tablet_code));
		tfBook.setText(timeBook+"");
		tfCancel.setText(timeCancel+"");
		btnClear.setDisable(true);
		
		if(AuthenticationModel.hasPermission("CLEAR_TABLET") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnClear.setDisable(false);
		}
		tfBook.setDisable(true);
		tfCancel.setDisable(true);
		if(AuthenticationModel.hasPermission("SETTING_TIME") || AuthenticationModel.roleName.equals("Super Admin")) {
			tfBook.setDisable(false);
			tfCancel.setDisable(false);
		}
		this.getTableCodeList();
		this.getTableInfo();
	}
	private void updateData() {
		if(cbbCode.getValue()!=null) {
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
		cbbCode.setValue(DataMapping.getInstance(SettingController.tableId, SettingController.tablet_code));
		this.getTableInfo();
	}
	
}
