package controllers;

import java.awt.Component;
import models.AuthenticationModel;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.sql.ResultSet;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.AttributeModel;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;
import utils.CompareOperator;
import java.util.ArrayList;
import java.util.Optional;

public class AttributeController implements Initializable{
	AttributeModel attModel = new AttributeModel();
	
	private int attID;
	private String attName;
	
	@FXML
    private TableView<AttributeModel> tblAttribute;

	@FXML
    private TableColumn<AttributeModel, Integer> col_no;

    @FXML
    private TableColumn<AttributeModel, Integer> col_id;

    @FXML
    private TableColumn<AttributeModel, String> col_name;

    @FXML
    private TableColumn<AttributeModel, Integer> col_parent;

    @FXML
    private TableColumn<AttributeModel, LocalDate> col_created;

    @FXML
    private TextField tfFind;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnDelete;
    
    @FXML
    private TextField tfName;

    @FXML
    private ComboBox<DataMapping> cbParent;

    @FXML
    private Label lblNameError;

    @FXML
    private Label lblParentError;

    @FXML AnchorPane serattHolder;

	private MasterController masterController;
	
	@FXML private Button btnSA;
    
    @FXML
    void btnClearAction(ActionEvent event) {
    	try {
    		tfFind.setText("");
    		this.loadData(getFilter());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void btnAddAction(ActionEvent event) {
    	attID = 0;
    	udAction();
    }

    @FXML
    void btnDeleteAction(ActionEvent event) {
    	try { 
			if(this.attID != 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete Attribute Confirmation");
				alert.setHeaderText("are you sure you want delete this item?");
//				alert.setContentText("Name: ".concat(this.attName));
				
				Optional<ButtonType> options = alert.showAndWait();
				if(options.get() == ButtonType.OK) {
					this.attModel.deleteAttribute(attID);
					this.loadData(null);
				}
			}
			else {
				Helpers.status("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void btnUpdateAction(ActionEvent event) {
    	attID = 0;
    	udAction();
    }

    public boolean validated(String name) {
    	try {
    		lblNameError.setText("");
    		
    		ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
    		data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:2"));
    		
    		ArrayList<DataMapping> mess = Validations.validated(data);
    		if(mess.size() > 0) {
    			for(DataMapping message : mess) {
    				switch(message.key) {
    				case "lblNameError":
    					lblNameError.setText(message.value);
    					break;
    				default:
    					System.out.println("abcde");
    				}
    			} return false;
    		} return true;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return false;
    }
    
    private void udAction() {
    	try {
    		String name = tfName.getText();
    		String parent = cbParent.getValue() != null ? cbParent.getValue().key : null;
    		
    		if (validated(name)) {
    			ArrayList<DataMapping> attribute = new ArrayList<DataMapping>();
    			attribute.add(DataMapping.getInstance("name", name));
    			attribute.add(DataMapping.getInstance("parent_id", parent));
    			
    			if(attID == 0) {
    				attModel.createAttribute(attribute);
    				Helpers.status("success");
    			} else {
    				Alert alert = new Alert(AlertType.CONFIRMATION);
    				alert.setTitle("Update Role Connfirmation");
    				alert.setHeaderText("Do you want to make this change?");
    				Optional<ButtonType> option = alert.showAndWait();
    				if(option.get() == ButtonType.OK) {
    					attModel.updateAttribute(attID, attribute);
    					Helpers.status("success");
    					attID = 0;
    				}
    			}
    			this.loadData(null);
    			btnClearAction(null);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void onSearch() {
    	try {
    		this.loadData(getFilter());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void resetInput(ActionEvent event) {
    	btnAdd.setDisable(false);
    	btnUpdate.setDisable(true);
    	btnDelete.setDisable(true);
    	lblNameError.setText("");
    	lblParentError.setText("");
    	tfName.setText("");
    	cbParent.getEditor().clear();
    }

    public ArrayList<CompareOperator> getFilter() {
    	try {
    		String code = tfFind.getText();
    		ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
    		conditions.add(CompareOperator.getInstance("attributes.name or att.name"," like ", "%"+ code + "%"));
    		return conditions;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.loadData(null);
		this.getParentList();
		resetInput(null);
		//create
				btnAdd.setDisable(true);
				//update
				btnUpdate.setDisable(true);
				
				//delete
				btnDelete.setDisable(true);
				

				
				if(AuthenticationModel.hasPermission("CREATE_ATTRIBUTE") || AuthenticationModel.roleName.equals("Super Admin")) {
					btnAdd.setDisable(false);
				}
				
				if(AuthenticationModel.hasPermission("UPDATE_ATTRIBUTE") || AuthenticationModel.roleName.equals("Super Admin")) {
					btnUpdate.setDisable(false);
				}
				
				if(AuthenticationModel.hasPermission("DELETE_ATTRIBUTE") || AuthenticationModel.roleName.equals("Super Admin")) {
					btnDelete.setDisable(false);
				}
				
				
				btnSA.setDisable(true);
		if(AuthenticationModel.hasPermission("VIEW_SER_ATTRIBUTES") || AuthenticationModel.roleName.equals("Super Admin")) {
					btnSA.setDisable(false);
				}
	}
	
	//load data
	public void loadData(ArrayList<CompareOperator> conditions) {
		try {
			ObservableList<AttributeModel> attList = FXCollections.observableArrayList();
			//get row from model
			col_no.setCellValueFactory(new PropertyValueFactory<AttributeModel, Integer>("sequence"));
			col_id.setCellValueFactory(new PropertyValueFactory<AttributeModel, Integer>("id"));
			col_name.setCellValueFactory(new PropertyValueFactory<AttributeModel, String>("name"));
			col_parent.setCellValueFactory(new PropertyValueFactory<AttributeModel, Integer>("parent"));
			col_created.setCellValueFactory(new PropertyValueFactory<AttributeModel, LocalDate>("createdAt"));
			
			
			//get row from db
			ResultSet att = this.attModel.getAttributeList(conditions);
			while (att.next()) {
				attList.add(new AttributeModel(
						att.getRow(),
						att.getInt("id"),
						att.getString("name"),
						att.getString("parent_name"),
						att.getDate("created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy"))
						));
			}
			

			tblAttribute.setItems(attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@FXML
    void getDataRowSelected(MouseEvent event) throws ParseException{
		if(event.getClickCount() > 0 ) {
			btnAdd.setDisable(true);
			btnUpdate.setDisable(false);
			btnDelete.setDisable(false);
			
			AttributeModel item = tblAttribute.getSelectionModel().getSelectedItem();
			
			attID = item.getId();
			
			tfName.setText(item.getName());
			cbParent.setValue(DataMapping.getInstance(item.getParent_id(), item.getParent()));
		} else {
			System.out.println("not click in table");
			attID = 0;
		}
    }

	public ResultSet getParentList() {
		try {
			ArrayList<DataMapping> parentOptions = new ArrayList<DataMapping>();
			ResultSet parent = attModel.getAttributeList(null);
			while(parent.next() ) {
				parentOptions.add(DataMapping.getInstance(parent.getInt("id"), parent.getString("parent_id")));
			}
			cbParent.getItems().setAll(parentOptions);
			return parent;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void parseMaster(MasterController masterController) {
		this.masterController = masterController;
	}
	
	//show view ser att
	public void showSerAtt() {
		try {
  			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/serattributes.fxml"));
  			AnchorPane chef = root.load();
			ServingAttributeController control = root.getController();
			control.parseMaster(masterController);
			this.masterController.masterHolder.getChildren().setAll(chef);
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
	}
	
	@FXML public void btnSerAtt() {
		showSerAtt();
	}
	public int getAttID() {
		return attID;
	}

	public void setAttID(int attID) {
		this.attID = attID;
	}

	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}
	
	
}
