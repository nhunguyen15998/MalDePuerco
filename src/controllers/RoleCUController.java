/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.RoleModel;
import utils.DataMapping;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import utils.ValidationDataMapping;
import utils.Validations;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class RoleCUController implements Initializable {
	private RoleModel roleModel = new RoleModel();
	private RolesController roleController;
	private int roleId;
	   @FXML
	    private Button btnCancel;

	    @FXML
	    private TextField tfName;

	    @FXML
	    private TextField tfCode;

	    @FXML
	    private ComboBox<DataMapping> cbStatus;
	    @FXML
	    private Label lblNameError;	    
	    @FXML
	    private Label lblRole;

	   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    	ObservableList<DataMapping> status = FXCollections.observableArrayList(RoleModel.isActivated, RoleModel.isDeactivated);
		cbStatus.setItems(status);
    }
  //validate
  	public boolean validated(String code, String name) {
  		try {
  			lblNameError.setText("");
  			
  			ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
  			data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:5"));
  			
  			ArrayList<DataMapping> messages = Validations.validated(data);
  			if(messages.size() > 0) {
  				for(DataMapping message : messages) {
  					switch(message.key) {
  						case "lblNameError":
  							lblNameError.setText(message.value);
  							break;
  						default:
  							System.out.println("abcde");
  					}
  				return false;
  				}
  			}
  			return true;
  		} catch (Exception e) {
  			e.printStackTrace();
  			return false;
  		}
  	}

  	public void loadDataUpdateById(RolesController roleController) {
		try {
			this.roleController = roleController;
			this.roleId = roleController.getRoleId();
			lblRole.setText("Create Role");
			if(this.roleId != 0) {
				lblRole.setText("Update Role");
				ResultSet currentRole = this.roleModel.getRoleById(this.roleId);
				if(currentRole.next()) {
					tfCode.setText(currentRole.getString("code"));
					tfName.setText(currentRole.getString("name"));
					
					for(DataMapping status : cbStatus.getItems()) {
						if(status.key != null && Integer.parseInt(status.key) == currentRole.getInt("status")) {
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
    @FXML
    void btnCancelAction() {

    }

    @FXML
    void btnSaveAction() {

    }
    
}
