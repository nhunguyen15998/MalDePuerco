package controllers;

import java.awt.Button;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ServingModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

public class ServingsController  implements Initializable {
	private ServingModel servingModel = new ServingModel();
	private int servingId;
	private String servingName;
	
	@FXML
    private TableView<ServingModel> tbl_servings;
    @FXML
    private TableColumn<ServingModel, Integer> col_no;
    @FXML
    private TableColumn<ServingModel, Integer> col_id;
    @FXML
    private TableColumn<ServingModel, String> col_name;
    @FXML
    private TableColumn<ServingModel, Integer> col_cate;
    @FXML
    private TableColumn<ServingModel, String> col_type;
    @FXML
    private TableColumn<ServingModel, String> col_des;
    @FXML
    private TableColumn<ServingModel, String> col_price;
    @FXML 
    private TableColumn<ServingModel, LocalDate> col_created; 
    @FXML
    private TableColumn<ServingModel, String> col_status;
    @FXML private TableColumn<ServingModel, Integer> colQuantity;
    @FXML private TableColumn<ServingModel, String> colPath;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private TextField tfFind;
    @FXML
    private Button btnClear;
    @FXML 
    private AnchorPane createForm;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.loadData(null);
		
		
	}

	//load data 
	public void loadData (ArrayList<CompareOperator> conditions) {
		try {
			ObservableList<ServingModel> Servingslist = FXCollections.observableArrayList();
			//get row form model
			col_no.setCellValueFactory(new PropertyValueFactory<ServingModel, Integer>("sequence"));
			col_id.setCellValueFactory(new PropertyValueFactory<ServingModel, Integer>("id"));
			col_name.setCellValueFactory(new PropertyValueFactory<ServingModel, String>("name"));
			col_cate.setCellValueFactory(new PropertyValueFactory<ServingModel, Integer>("categoryName"));
			col_type.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
					cellData.getValue().getType() == ServingModel.FOOD ? String.valueOf(ServingModel.isFood) :
						(cellData.getValue().getType() == ServingModel.HOT_DRINK ? String.valueOf(ServingModel.isHotDrink) : String.valueOf(ServingModel.isColdDrink))
					));
			col_des.setCellValueFactory(new PropertyValueFactory<ServingModel, String>("descriptions"));
			colQuantity.setCellValueFactory(new PropertyValueFactory<ServingModel, Integer>("quantity"));
			col_price.setCellValueFactory(new PropertyValueFactory<ServingModel, String>("price"));
			col_created.setCellValueFactory(new PropertyValueFactory<ServingModel, LocalDate>("createAt"));
			col_status.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(				
					cellData.getValue().getStatus() == ServingModel.SERVING_ACTIVATED ? String.valueOf(ServingModel.isActivated) : String.valueOf(ServingModel.isDeactivated)));
			colPath.setVisible(true);
			//get data form db
			ResultSet servings = this.servingModel.getServingList(conditions);
			while (servings.next()) {
				//int no, int id, String name, String categoryName, String descriptions
				//double price, String createAt, int status 
				Servingslist.add(new ServingModel(
						servings.getRow(),
						servings.getInt("id"),
						servings.getString("nameSer"), 
						servings.getString("cateName"),
						servings.getInt("type"), 
						servings.getString("descriptions"),
						servings.getInt("quantity"),
						servings.getInt("price"),
						servings.getDate("created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
						servings.getInt("status"),
						servings.getString("servings.thumbnail")
						));
			}
			tbl_servings.setItems(Servingslist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	//mouse click
	public void getTableDataByClick(MouseEvent e) {
		if (e.getClickCount() > 0) {
			ServingModel item = tbl_servings.getSelectionModel().getSelectedItem();
			if (item != null) {
				this.servingId = item.getId();
				this.servingName = item.getName();
				System.out.println(this.servingId);
			}
		}
	}

	//create 
	public void btnCreateAction() {
		try {
				this.setServingId(0);
				this.showCreateForm();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
//	//update
	public void btnUpdateAction() {
		System.out.println("update");
		try {
			if (this.servingId != 0) {
				this.showCreateForm();
			} else {
				//error
				Helpers.status("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	//delete 
	public void btnDeleteAction() {
		try {
			if (this.servingId != 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete Serving Confirmation");
				alert.setHeaderText("Are you sure want to delete this item ?");
				alert.setContentText("Name: " + this.getServingName());
				Optional<ButtonType> options = alert.showAndWait();
				if (options.get() == ButtonType.OK) {
					this.servingModel.deleteServing(this.servingId);
					this.loadData(null);
				}
			} else {
				//error
				Helpers.status("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//		//show create form
		public void showCreateForm() {
			System.out.println("create form");
			try {
				
				FXMLLoader root = new FXMLLoader(getClass().getResource("/views/servingsCU.fxml"));
				createForm = root.load();
				
				ServingsCUController controller= root.<ServingsCUController>getController();
				controller.loadDataById(this);
				
				Scene scene = new Scene(createForm, 838, 550);
				Stage createStage = new Stage();
				createStage.initStyle(StageStyle.UNDECORATED);
				createStage.setScene(scene);
				createStage.show();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
		}
		
		//filter
		public ArrayList<CompareOperator> getFilter() {
			try {
				String code = tfFind.getText();
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("name or sc.name", " like ", "%"+ code + "%"));
				return conditions;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		//on search
		public void onSearch() {
			try {
				this.loadData(getFilter());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void btnClearAction() {
			try {
				tfFind.setText("");
				this.loadData(getFilter());
			} catch (Exception e ) {
				e.printStackTrace();
			}
		}
		
		
		
	//get&set
	
	public int getServingId() {
		return servingId;
	}
	public void setServingId(int servingId) {
		this.servingId = servingId;
	}
	
	public String getServingName() {
		return servingName;
	}
	public void setServingName(String servingName) {
		this.servingName = servingName;
	}
	
}
