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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.PermissionModel;
import models.RoleModel;
import models.RolePermissionModel;
import utils.CompareOperator;
import utils.DataMapping;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class PermissionController implements Initializable {
	private PermissionModel permissionModel = new PermissionModel();
	private RoleModel roleModel = new RoleModel();
	public RolesController roleController;
	private int roleId;
	private RolePermissionModel roleHasPermissionModel = new RolePermissionModel();
	@FXML
	private GridPane permissionPane;
	@FXML
	private Label lblName;
	@FXML
	private Label lblCode;
	@FXML
	private Button btnCancel;
	
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

  //load permission
  	public void loadPermission() {
  		try {
  			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
  			conditions.add(CompareOperator.getInstance("roles.id", "=", String.valueOf(this.roleId)));
  			ResultSet permissions = this.permissionModel.getPermissionList(conditions);
  			ArrayList<PermissionModel> perDatas = new ArrayList<PermissionModel>();
  			while(permissions.next()) {
  				perDatas.add(new PermissionModel(permissions.getString("permission_id"), permissions.getString("permission_name")));
  			}
  			
  			String[] selects = {"*"};
  			ResultSet list = this.permissionModel.getData(selects, null, null, null, null, null);
  			int x = 0;
  			int y = 0;
  			int count = 0;
  			while(list.next()) {
  				String permissionName = list.getString("permissions.name");
  				CheckBox checkBox = new CheckBox(permissionName); 
  				for (PermissionModel permissionModel : perDatas) {
  					if(permissionName.equals(permissionModel.getName())){
  						checkBox.setSelected(true);
  					}
  				}
  				permissionPane.add(checkBox, x, y);
  				count++;
  				if(count%2 == 0) {
  					y++;
  				}
  				x++;
  				if(x>1) {
  					x = 0;
  				}
  				
  				
  			}
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
  	
  	//load data update
  	public void loadDataUpdateById(RolesController roleController) {
  		try {
  			this.roleController = roleController;
  			this.roleId = roleController.getRoleId();
  			if(this.roleId != 0) {
  				ResultSet currentRole = this.roleModel.getRoleById(this.roleId);
  				if(currentRole.next()) {
  					lblCode.setText(currentRole.getString("code"));
  					lblName.setText(currentRole.getString("name"));					
  				}
  				loadPermission();
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
  	
  	//btnCancel
  	public void btnCancelAction() {
  		try {
  			this.close();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
  	
  	//btnClose
  	public void close() {
  		try {
  			PermissionModel.isShown = false;
  			Stage stage = (Stage) btnCancel.getScene().getWindow();
  			stage.close();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
  	
  	//sve
  	public void btnSaveAction() {
  		try {
  			ArrayList<CompareOperator> condiRemove = new ArrayList<CompareOperator>();
  			condiRemove.add(CompareOperator.getInstance("role_id", "=", String.valueOf(this.roleId)));
  			this.roleHasPermissionModel.delete(condiRemove);
  			
  			
  			for (Node node : permissionPane.getChildren()) {
  		        CheckBox cb = (CheckBox) node;
  		        if(cb.isSelected()) {
  		        	ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
  		        	conditions.add(CompareOperator.getInstance("name", "=", cb.getText()));
  		        	String[] selects = {"*"};
  					ResultSet permissions = this.permissionModel.getData(selects, conditions, null, null, null, null);
  					while(permissions.next()) {
  						ArrayList<DataMapping> data = new ArrayList<DataMapping>();
  						data.add(DataMapping.getInstance("role_id", String.valueOf(this.roleId)));
  						data.add(DataMapping.getInstance("permission_id", String.valueOf(permissions.getInt("id"))));
  						this.roleHasPermissionModel.create(data);
  					}
  		        }
  		    }
  			this.close();
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
    @FXML
     public void removeAll() {
    	for (Node node : permissionPane.getChildren()) {
      	    if (node instanceof CheckBox) {
      	        ((CheckBox) node).setSelected(false);
      	    }
      	}
    }

    @FXML
    public void selectAll() {
    	for (Node node : permissionPane.getChildren()) {
      	    if (node instanceof CheckBox) {
      	        ((CheckBox) node).setSelected(true);
      	    }
      	}
    }
  
    
}
