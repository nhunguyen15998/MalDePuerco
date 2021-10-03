/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.RoleModel;
import utils.CompareOperator;
import utils.Helpers;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class RolesController implements Initializable {
	private RoleModel roleModel = new RoleModel();
	private int roleId;
	private String roleName;
		@FXML
	    private AnchorPane roleHolder;
		@FXML
		private AnchorPane createHolder;
	    @FXML
	    private TableView<RoleModel> tableRole;
	    @FXML
	    private TableColumn<RoleModel, Integer> colOrder;
	    @FXML
	    private TableColumn<RoleModel, Integer> colId;
	    @FXML
	    private TableColumn<RoleModel, String> colCode;
	    @FXML
	    private TableColumn<RoleModel, String> colName;
	    @FXML
	    private TableColumn<RoleModel, LocalDate> colCreatedAt;
	    @FXML
	    private TableColumn<RoleModel, String> colStatus;
	    @FXML
	    private TextField tfRole;

	 

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    	parseData(this.getFilter());
    }    
    
	//load data
	public void parseData(ArrayList<CompareOperator> conditions) {
		try {
			//create list, format date
			ObservableList<RoleModel> roleList = FXCollections.observableArrayList();
					
			//cols get from model
			colId.setCellValueFactory(new PropertyValueFactory<RoleModel, Integer>("id"));
			colOrder.setCellValueFactory(new PropertyValueFactory<RoleModel, Integer>("sequence"));
			colCode.setCellValueFactory(new PropertyValueFactory<RoleModel, String>("code"));
			colName.setCellValueFactory(new PropertyValueFactory<RoleModel, String>("name"));
			colCreatedAt.setCellValueFactory(new PropertyValueFactory<RoleModel, LocalDate>("createdAt"));
			//format col
			colStatus.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(				
					cellData.getValue().getStatus() == RoleModel.ROLE_ACTIVATED ? String.valueOf(RoleModel.isActivated) : String.valueOf(RoleModel.isDeactivated)));
			
			//get data from db
			ResultSet roles = roleModel.getRoleList(conditions);
			while(roles.next()) {
				roleList.add(RoleModel.getInstance(
					roles.getInt("id"),
					roles.getRow(),
					roles.getString("code"),
					roles.getString("name"),
					roles.getDate("created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
					roles.getInt("status"))
					);
			}
			tableRole.setItems(roleList);;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ArrayList<CompareOperator> getFilter(){
		try {
			String code = tfRole.getText();
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("code", " like ", "%"+ code + "%"));
			//conditions.add(new CompareOperators("", name, name))
			return conditions;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    @FXML
    void btnClearAction() {
    	try {
			tfRole.setText("");
			this.parseData(getFilter());
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

    @FXML
    void btnCreateAction(ActionEvent event) {
    	try {
			this.setRoleId(0);
//			if(!RoleModel.isShown) {
//				RoleModel.isShown = true;
				this.showCreateForm();
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
    }

    @FXML
    void btnDeleteAction(ActionEvent event) {

    }

    @FXML
    void btnPermissionAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateAction(ActionEvent event) {

    }

   

    @FXML
    void getTableDataByClick(MouseEvent event) {

    }
  //show form assign
  	public void showCreateForm() {
  		try {
  			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
  			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/role_cu.fxml"));
  			createHolder = root.load();
  			
  			//controller
  			RoleCUController controller = root.<RoleCUController>getController();
  			controller.loadDataUpdateById(this);
  			
  			Scene scene = new Scene(createHolder, 570,376);
  			Stage createStage = new Stage();
  			createStage.setX(screenBounds.getWidth() - 1000);
  			createStage.setY(screenBounds.getHeight() - 700);
  			createStage.initStyle(StageStyle.UNDECORATED);
  			createStage.setScene(scene);
  			createStage.show();			
  			
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
    
    //get set ID
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
