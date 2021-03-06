package controllers;

import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.sql.ResultSet;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ServingAttributeModel;
import models.ServingModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;

public class ServingAttributeController implements Initializable{
	private ServingAttributeModel serattModel = new ServingAttributeModel();
	private int servingattId;
	private String servingattName;
	
	@FXML
    private TableView<ServingAttributeModel> tblAttribute;

    @FXML
    private TableColumn<ServingAttributeModel, Integer> colNo;

    @FXML
    private TableColumn<ServingAttributeModel, Integer> colId;

    @FXML
    private TableColumn<ServingAttributeModel, Integer> colServing;

    @FXML
    private TableColumn<ServingAttributeModel, Integer> colAttribute;
    
    @FXML private TableColumn<ServingAttributeModel, Integer> colQuantity;

    @FXML
    private TableColumn<ServingAttributeModel, String> colPrice;

    @FXML
    private TableColumn<ServingAttributeModel, LocalDate> colCreated;
    private ServingModel servingModel = new ServingModel();
	
	@FXML private Button btnSave;
	@FXML private Button btnUpdate;
	@FXML private Button btnDelete;
	@FXML private TextField tfFind;
	
	@FXML private AnchorPane createHolder;
	private MasterController masterController;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		this.loadData(getFilter());
	}
	
	//load data
	public void loadData(ArrayList<CompareOperator> conditions) {
		try {
			ObservableList<ServingAttributeModel> serattList = FXCollections.observableArrayList();
			
			//get row form model
			colNo.setCellValueFactory(new PropertyValueFactory<ServingAttributeModel, Integer>("sequence"));
			colId.setCellValueFactory(new PropertyValueFactory<ServingAttributeModel, Integer>("id"));
			colServing.setCellValueFactory(new PropertyValueFactory<ServingAttributeModel, Integer>("servingName"));
			colAttribute.setCellValueFactory(new PropertyValueFactory<ServingAttributeModel, Integer>("attribute"));
			colQuantity.setCellValueFactory(new PropertyValueFactory<ServingAttributeModel, Integer>("quantity"));
			colPrice.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Helpers.formatNumber(null).format(cellData.getValue().getPrice())));
			colCreated.setCellValueFactory(new PropertyValueFactory<ServingAttributeModel, LocalDate>("createdAt"));
			
			//get data form db
			ResultSet servingatt = this.serattModel.getSerAttList(conditions);
			while (servingatt.next()) {
				//int no, int id, String serving_id, String attribute
				//double price, String created_at
				serattList.add(new ServingAttributeModel(
						servingatt.getRow(), 
						servingatt.getInt("id"),
						servingatt.getString("servingID"),
						servingatt.getString("attName"),
						servingatt.getInt("quantity"), 
						servingatt.getInt("price"), 
						servingatt.getDate("created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy"))
						));
			}
			tblAttribute.setItems(serattList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//mouse click
		public void getTableDataByClick(MouseEvent e) {
			try {
				if(e.getClickCount() > 0) {
					ServingAttributeModel item = tblAttribute.getSelectionModel().getSelectedItem();
					if(item != null) {
						this.servingattId = item.getId();
						this.servingattName = item.getServingName();
						System.out.println(this.servingattId);
						System.out.println(this.servingattName);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	
		//create
		public void btnCreateAction() {
			try {
				this.setServingattId(0);
				this.showCreateForm();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//update
		public void btnUpdateAction() {
			try {
				if(this.servingattId != 0) {
					this.showCreateForm();
				} else {
					Helpers.status("error");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//delete
		public void btnDeleteAction() {
			try {
				if(this.servingattId != 0) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Delete Serving Attribute Confirmation");
					alert.setHeaderText("are you sure you want delete this item?");
					alert.setContentText("Name: ".concat(this.servingattName));
					
					Optional<ButtonType> options = alert.showAndWait();
					if(options.get() == ButtonType.OK) {
						this.serattModel.deleteSerAtt(this.servingattId);
						this.loadData(getFilter());
					}
				}
				else {
					Helpers.status("error");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//show form create
		public void showCreateForm() {
			try {
				FXMLLoader root = new FXMLLoader(getClass().getResource("/views/serattributesCU.fxml"));
				createHolder = root.load();
				
				//controller
				ServingAttributeCUController controller = root.<ServingAttributeCUController>getController();
				controller.loadDataUpdateById(this);
				
				Scene scene = new Scene(createHolder, 838, 550);
				Stage createStage = new Stage();
				createStage.initStyle(StageStyle.UNDECORATED);
				createStage.setScene(scene);
				createStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//get filter
		public ArrayList<CompareOperator> getFilter() {
			try {
				String code = tfFind.getText();
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("servings.name"," like ", "%"+ code + "%"));
				return conditions;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		//search
		public void onSearch() {
			try {
				this.loadData(getFilter());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//clear action
		public void btnClearAction() {
			try {
				tfFind.setText("");
				this.loadData(getFilter());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//parse master
	  	public MasterController parseMaster(MasterController masterController) {
	  		this.masterController = masterController;
	  		return this.masterController;
	  	}
		
	//get&set
	public int getServingattId() {
		return servingattId;
	}

	public void setServingattId(int servingattId) {
		this.servingattId = servingattId;
	}
	public String getSetvingattName() {
		return servingattName;
	}
	public void setServingattName(String servingattName) {
		this.servingattName = servingattName;
	}

}