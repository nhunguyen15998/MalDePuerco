/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javafx.scene.control.TextField;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.security.crypto.bcrypt.BCrypt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.RoleModel;
import models.TableModel;
import models.UserModel;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class TableCUController implements Initializable {
	private TableModel tableModel = new TableModel();
	private TablesController tableController;
	private int tableId;
	   @FXML
	    private TextField tfName;

	   @FXML
	    private TextField tfSeats;

	    @FXML
	    private Label lblCodeError;

	    @FXML
	    private Label lblSeatsError;


	    @FXML
	    private Label lblTable;
	    @FXML
	    private Label lblNameError;
	    @FXML
	    private TextField tfCode;

	    @FXML
	    private ComboBox<DataMapping> cbStatus;

	    @FXML
	    private Button btnCancel;

	   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    		
		ObservableList<DataMapping> status = FXCollections.observableArrayList(TableModel.isEmpty, TableModel.isServing, TableModel.isUpcoming, TableModel.isWaiting);
		cbStatus.setItems(status);
		cbStatus.setValue(TableModel.isEmpty);
    }    
    
  //load data update
  	public void loadDataUpdateById(TablesController tbl) {
  		try {
  			this.tableController = tbl;
  			this.tableId = tbl.getTableId();
  			lblTable.setText("Create Table");
  			if(this.tableId != 0) {
  				lblTable.setText("Update Table");
  				ResultSet currentTable = this.tableModel.getTableById(this.tableId);
  				if(currentTable.next()) {
  					tfCode.setText(currentTable.getString("code"));
  					tfName.setText(currentTable.getString("name"));
  					tfSeats.setText(currentTable.getInt("seats")+"");
  					
  					
  					
  					for(DataMapping status : cbStatus.getItems()) {
  						if(status.key != null && Integer.parseInt(status.key) == currentTable.getInt("status")) {
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
	public boolean validated( String name, String code, String seats) {
		try {
			
			lblNameError.setText("");
			lblCodeError.setText("");
			lblSeatsError.setText("");
			
			
			ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
			data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:5"));
			data.add(new ValidationDataMapping("code", code, "lblCodeError", "required"));
			data.add(new ValidationDataMapping("seats", seats, "lblSeatsError", "required|min:0|numeric"));
			
			
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
						case "lblSeatsError":
							lblSeatsError.setText(message.value);
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
    void btnCancelAction() {
    	try {
			this.close();
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
    void btnSaveAction() {
		try {
			String name = tfName.getText();
			String code = tfCode.getText();
			String seats = tfSeats.getText();
			String status = cbStatus.getValue() == TableModel.isEmpty ? cbStatus.getValue().key : 
					((cbStatus.getValue()  == TableModel.isWaiting ? cbStatus.getValue().key : 
							(cbStatus.getValue()  == TableModel.isUpcoming ? cbStatus.getValue().key :String.valueOf(TableModel.isServing))));

			if(validated( name, code, seats)) {
				
				ArrayList<DataMapping> table = new ArrayList<DataMapping>();
				table.add(DataMapping.getInstance("name", name));
				table.add(DataMapping.getInstance("code", code));
				table.add(DataMapping.getInstance("seats", seats));
				table.add(DataMapping.getInstance("status", status));

				if(this.tableId == 0) {
					
					tableModel.createTable(table);
					Helpers.status("success");
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Update Table Confirmation");
					alert.setHeaderText("Do you want to make this change?");
					Optional<ButtonType> option = alert.showAndWait();
					if (option.get() == ButtonType.OK) {
						tableModel.updateTableById(this.tableId, table);
						Helpers.status("success");
					} 
					
				}
				tableController.parseData(null);
				this.close();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
