/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;

import db.MySQLJDBC;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AuthenticationModel;
import models.RoleModel;
import models.TableModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class TablesController implements Initializable {
	private TableModel tableModel = new TableModel();
	private int tableId;
	private String tableName;
    @FXML
    private TableView<TableModel> tblTables;

	@FXML
	private AnchorPane createHolder;
    @FXML
    private TableColumn<TableModel, Integer> colNo;

    @FXML
    private TableColumn<TableModel, Integer> colID;

    @FXML
    private TableColumn<TableModel, String> colCode;

    @FXML
    private TableColumn<TableModel, String> colName;

    @FXML
    private TableColumn<TableModel, String> colShift;

    @FXML
    private TableColumn<TableModel, Integer> colUserID;
    @FXML
    private TableColumn<TableModel, Integer> colSeats;

    @FXML
    private TableColumn<TableModel, LocalDate> colCreated;

    @FXML
    private TableColumn<TableModel, String> colSet;

    @FXML
    private TableColumn<TableModel, String> colStatus;

    @FXML
    private TextField tfTable;

    @FXML
    private Button btnChange;
    @FXML
    private Button btnOrder;
    @FXML
    private CheckBox chkShift;
    @FXML
    private Pane paneSchedule;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    	updateStatus();
    	this.parseData(getFilter());
    	//discount
    			btnChange.setVisible(false);
    			
    			if(AuthenticationModel.hasPermission("CHANGE_SHIFT") || AuthenticationModel.roleName.equals("Super Admin")) {
    				btnChange.setVisible(true);
    			
    			}
    			chkShift.setVisible(false);
    			if( AuthenticationModel.roleName.equals("Server")) {
    				chkShift.setVisible(true);
    			
    			}

      			this.loadSchedule();
    }    


    //load schedule
    private void loadSchedule() {
    	 AnchorPane anchor;
  		try {
  			anchor = FXMLLoader.load(getClass().getResource("/views/schedule.fxml"));
  			paneSchedule.getChildren().setAll(anchor);
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
    }
    @FXML
    public void checkShift() {

    	if(chkShift.isSelected()) {
    		ArrayList<CompareOperator> cond = new ArrayList<CompareOperator>();
			cond.add(CompareOperator.getInstance("user_id", " = ", String.valueOf(AuthenticationModel.id)));
			parseData(cond);
    	}else {

    		parseData(getFilter());
    	}
    	
    }
    //update status New to Confirmed after 30 minutes
    public void updateStatus() {
    	String sql = "select tables.id, table_id, reservation_id, date_pick, r.status,start_time from tables_reservation t join tables  on tables.id= t.table_id join reservations r on r.id=t.reservation_id "
    			+ "where r.status = 4 and curdate()=date_pick and curtime()>=start_time" ;
    	String sql2 = "select tables.id, table_id, reservation_id, date_pick, r.status,start_time from tables_reservation t join tables  on tables.id= t.table_id join reservations r on r.id=t.reservation_id where (r.status = 1 or r.status = 2 or r.status = 3) and curdate()=date_pick and start_time<=curtime() and curtime()<=date_add(start_time, INTERVAL 30 MINUTE)" ;
    	
        try {
        	Statement stmt = MySQLJDBC.connection.createStatement();
    	   	ResultSet rs = stmt.executeQuery(sql);
    	   	while(rs.next()) {
    	   		int id = rs.getInt("tables.id");
    	   		ArrayList<DataMapping> table = new ArrayList<DataMapping>();
				table.add(DataMapping.getInstance("status", String.valueOf(TableModel.TABLE_PLACED)));
				tableModel.updateTableById(id, table);
    	   	}
    	   Statement stmt2 = MySQLJDBC.connection.createStatement();
    		ResultSet rs1 = stmt2.executeQuery(sql2);
    	   	while(rs1.next()) {
    	   		int id = rs1.getInt("tables.id");
    	   		ArrayList<DataMapping> table = new ArrayList<DataMapping>();
				table.add(DataMapping.getInstance("status", String.valueOf(TableModel.TABLE_WAITING)));
				tableModel.updateTableById(id, table);
    	   	}
    	}catch(SQLException  ex) {
    		ex.printStackTrace();
    	}
    	
    }
  //load data
  	public void parseData(ArrayList<CompareOperator> conditions) {
  		try {
  			//create list, format date
  			ObservableList<TableModel> tableList = FXCollections.observableArrayList();
  					
  			//cols get from model
  			colID.setCellValueFactory(new PropertyValueFactory<TableModel, Integer>("id"));
  			colUserID.setCellValueFactory(new PropertyValueFactory<TableModel, Integer>("user_id"));
  			colNo.setCellValueFactory(new PropertyValueFactory<TableModel, Integer>("sequence"));
  			colCode.setCellValueFactory(new PropertyValueFactory<TableModel, String>("code"));
  			colName.setCellValueFactory(new PropertyValueFactory<TableModel, String>("name"));
  			colSet.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(				
					cellData.getValue().getIs_set() == TableModel.YES ? String.valueOf(TableModel.isSet) : String.valueOf(TableModel.isNotSet)));
  			colSeats.setCellValueFactory(new PropertyValueFactory<TableModel, Integer>("seats"));
  			colShift.setCellValueFactory(new PropertyValueFactory<TableModel, String>("username"));
  			colCreated.setCellValueFactory(new PropertyValueFactory<TableModel, LocalDate>("createdAt"));
  			//format col
  			colStatus.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(				
  					cellData.getValue().getStatus() == TableModel.TABLE_EMPTY ? String.valueOf(TableModel.isEmpty) : 
  						((cellData.getValue().getStatus() == TableModel.TABLE_SERVING ? String.valueOf(TableModel.isServing) : 
  							(cellData.getValue().getStatus() == TableModel.TABLE_WAITING ? String.valueOf(TableModel.isWaiting):String.valueOf(TableModel.isPlaced))))));
  			
  			//get data from db
  			
  			ResultSet table = tableModel.getTableList(conditions);
  			while(table.next()) {
  				tableList.add(TableModel.getInstance(
  						table.getInt("id"),
  						table.getRow(),
  						table.getString("tables.code"),
  						table.getString("tables.name"),
  						table.getInt("seats"),
  						table.getDate("created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
  						table.getInt("status"),
  						table.getInt("is_set"),
  						getUserName(table.getInt("user_id")),
  						table.getInt("user_id"))
  					);
  			}
  			tblTables.setItems(tableList);
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		
  	}
  	private String getUserName(int id) {
  		String name="";
  		String sql = "select name from users where id="+id;
  		try {
  			Statement ps = MySQLJDBC.connection.createStatement();
  			ResultSet rs =ps.executeQuery(sql);
  			if(rs.next()) {
  				name=rs.getString("name");
  			}
  		}catch (Exception e) {
			
		}
  		return name;
  	}
  	
  	public ArrayList<CompareOperator> getFilter(){
		try {
			String code = tfTable.getText();
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("tables.code or tables.name or seats", " like ", "%"+ code + "%"));
			//conditions.add(new CompareOperators("", name, name))
			return conditions;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    @FXML
    void btnClearAction(ActionEvent event) {
    	try {
			tfTable.setText("");
			this.parseData(getFilter());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    @FXML
    void getTableDataByClick(MouseEvent event) {
    	try {
			if(event.getClickCount() > 0) {
				TableModel item = tblTables.getSelectionModel().getSelectedItem();
				if(item != null) {
					this.tableId = item.getId();
					this.tableName = item.getName();
					System.out.println(this.tableId);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
    }

    @FXML
    void btnCreateAction(ActionEvent event) {
    	try {
			this.setTableId(0);
//			if(!RoleModel.isShown) {
//				RoleModel.isShown = true;
				this.showCreateForm();
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    //show uc form
  	public void showCreateForm() {
  		try {
  			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/table-cu.fxml"));
  			createHolder = root.load();
  			
  			//controller
  			TableCUController controller = root.<TableCUController>getController();
  			controller.loadDataUpdateById(this);
  			
  			Scene scene = new Scene(createHolder, 603,417);
  			Stage createStage = new Stage();
  			createStage.initStyle(StageStyle.UNDECORATED);
  			createStage.setScene(scene);
  			createStage.show();			
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}

    @FXML
    void btnDeleteAction(ActionEvent event) {
    	try {
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			if(this.tableId != 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete table Confirmation");
				alert.setHeaderText("Are you sure you want to delete this item ?");
				alert.setContentText("Name: ".concat(this.tableName));
				alert.setX(screenBounds.getWidth() - 900);
				alert.setY(screenBounds.getHeight() - 610);
				
				Optional<ButtonType> options = alert.showAndWait();
				if(options.get() == ButtonType.OK) {
					this.tableModel.deleteTableById(tableId);
					this.parseData(getFilter());
				}
				
			} else {
				Helpers.status("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void btnMakeOrder(ActionEvent event) {

    }
    @FXML
    void btnChangeShift() {
		try {
			if(this.tableId != 0) {
					this.showFormChange();
			} else {
				Helpers.status("error");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void showFormChange() {
    	try {
  			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/change-shift.fxml"));
  			createHolder = root.load();
  			
  			//controller
  			ShiftController controller = root.<ShiftController>getController();
  			controller.loadDataUpdateById(this, tblTables);
  			
  			Scene scene = new Scene(createHolder, 522,327);
  			Stage createStage = new Stage();
  			createStage.initStyle(StageStyle.UNDECORATED);
  			createStage.setScene(scene);
  			createStage.show();			
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
		
	}


	@FXML
    void btnUpdateAction(ActionEvent event) {
    		try {
			
			if(this.tableId != 0) {
					this.showCreateForm();
			} else {
				//panel
				Helpers.status("error");
			}

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


	public TableModel getTableModel() {
		return tableModel;
	}


	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}


	public int getTableId() {
		return tableId;
	}


	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
  

}
