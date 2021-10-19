package controllers;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.input.MouseEvent;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.BaseModel;
import models.ServingCategoryModel;
import utils.CompareOperator;
import utils.Helpers;

public class ServingCateController implements Initializable {
	private ServingCategoryModel servingcateModel = new ServingCategoryModel();
	private int servingcateId;
	private String servingcateName;
	@FXML
    private TableView<ServingCategoryModel> tbl_sercate;

    @FXML
    private TableColumn<ServingCategoryModel, Integer> colNo;

    @FXML
    private TableColumn<ServingCategoryModel, Integer> colId;

    @FXML
    private TableColumn<ServingCategoryModel, String> colName;

    @FXML
    private TableColumn<ServingCategoryModel, Integer> colParent;
    @FXML
    private TableColumn<ServingCategoryModel, String> colStatus;
    
    @FXML 
    private TableColumn<ServingCategoryModel, LocalDate> colCreate;
    @FXML private TableColumn<ServingCategoryModel, String> colPath;
    @FXML private AnchorPane createHolder;
    @FXML private TextField tfFind;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	this.loadData(null);
    }
    
    //loadData
    public void loadData(ArrayList<CompareOperator> conditions) {
    	try {
    		ObservableList<ServingCategoryModel> list = FXCollections.observableArrayList();
    		//get row from model
    		colNo.setCellValueFactory(new PropertyValueFactory<ServingCategoryModel, Integer>("sequence"));
    		colId.setCellValueFactory(new PropertyValueFactory<ServingCategoryModel, Integer>("id"));
    		colName.setCellValueFactory(new PropertyValueFactory<ServingCategoryModel, String>("name"));
    		colParent.setCellValueFactory(new PropertyValueFactory<ServingCategoryModel, Integer>("parentName"));
    		colCreate.setCellValueFactory(new PropertyValueFactory<ServingCategoryModel, LocalDate>("createdAt"));
    		colStatus.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(				
					cellData.getValue().getStatus() == ServingCategoryModel.SERVING_CATEGORY_ACTIVATED ? String.valueOf(ServingCategoryModel.isActivated) : String.valueOf(ServingCategoryModel.isDeactivated)));
    		colPath.setVisible(true);
    		//get row from db
    		ResultSet sercate = servingcateModel.getServingCategoryList(conditions);
    		while(sercate.next()) {
    			list.add(new ServingCategoryModel(
    					sercate.getRow(),
    					sercate.getInt("id"), 
    					sercate.getString("name"),
    					sercate.getString("parentID"),
    					sercate.getDate("created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
    					sercate.getInt("status"),
    					sercate.getString("thumbnail")
    					));
    		}
    		tbl_sercate.setItems(list);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public ArrayList<CompareOperator> getFilter() {
    	try {
    		String code = tfFind.getText();
    		ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("serving_categories.name", " like ", "%"+ code + "%"));
			return conditions;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public void onSearch() {
    	try {
    		this.loadData(getFilter());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
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
    void btnCreateAction(ActionEvent event) {
    	try {
    		this.setServingcateId(0);
    		this.showCreateForm();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void btnDeleteAction(ActionEvent event) {
    	try {
    		if(this.servingcateId != 0) {
    			Alert alert = new Alert(AlertType.CONFIRMATION);
    			alert.setTitle("Delete Serving Category Confirmation");
    			alert.setHeaderText("Are you sure want to delete this item ?");
    			alert.setContentText("Name: " + this.getServingcateName());
    			Optional<ButtonType> options = alert.showAndWait();
    			if(options.get() == ButtonType.OK) {
    				this.servingcateModel.deleteServingCategory(this.servingcateId);
    				this.loadData(getFilter());
    			}
    		}
    		else {
    			Helpers.status("error");
    		}
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void btnUpdateAction(ActionEvent event) {
    	try {
    		if(this.servingcateId != 0) {
    			this.showCreateForm();
    		} else {
    			Helpers.status("error");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void onSearch(KeyEvent event) {
    	try {
    		this.loadData(getFilter());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
//	//show create form
	public void showCreateForm() {
		System.out.println("create form");
		try {
			
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/sercateCU.fxml"));
			createHolder = root.load();
			
			ServingCateCUController controller= root.<ServingCateCUController>getController();
			controller.loadDataById(this);
			
			Scene scene = new Scene(createHolder, 838, 550);
			Stage createStage = new Stage();
			createStage.initStyle(StageStyle.UNDECORATED);
			createStage.setScene(scene);
			createStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
    
	//mouse click 
	public void getTableDataByClick(MouseEvent e) {
		try {
			if(e.getClickCount() > 0) {
				ServingCategoryModel item = tbl_sercate.getSelectionModel().getSelectedItem();
				if(item != null) {
					this.servingcateId = item.getId();
					this.servingcateName = item.getName();
					System.out.println(this.servingcateId);
					System.out.println(this.servingcateName);
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
    public int getServingcateId() {
		return servingcateId;
	}
	public void setServingcateId(int servingcateId) {
		this.servingcateId = servingcateId;
	}
	
	public String getServingcateName() {
		return servingcateName;
	}
	public void setServingcateName(String servingcateName) {
		this.servingcateName = servingcateName;
	}
}
