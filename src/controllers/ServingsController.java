package controllers;

import java.awt.Button;
import java.awt.TextField;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ServingModel;
import utils.CompareOperator;
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
    private TableColumn<ServingModel, String> col_des;
    @FXML
    private TableColumn<ServingModel, Integer> col_price;
    @FXML
    private TableColumn<ServingModel, Integer> col_new;
    @FXML
    private TableColumn<ServingModel, Integer> col_best;
    @FXML
    private TableColumn<ServingModel, String> col_status;
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
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadData();
	}
	
	//load data 
	public void loadData (ArrayList<CompareOperator> conditions) {
		try {
			ObservableList<ServingModel> list = FXCollections.observableArrayList();
			
			col_no.setCellValueFactory(new PropertyValueFactory<ServingModel, Integer>("sequence"));
			col_id.setCellValueFactory(new PropertyValueFactory<ServingModel, Integer>("id"));
			col_name.setCellValueFactory(new PropertyValueFactory<ServingModel, String>("name"));
			col_cate.setCellValueFactory(new PropertyValueFactory<ServingModel, Integer>("category_id"));
			col_des.setCellValueFactory(new PropertyValueFactory<ServingModel, String>("descriptions"));
			col_price.setCellValueFactory(new PropertyValueFactory<ServingModel, Integer>("price"));
			col_new.setCellValueFactory(new PropertyValueFactory<ServingModel, Integer>("is_new"));
			col_best.setCellValueFactory(new PropertyValueFactory<ServingModel, Integer>("is_bestseller"));
			col_status.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus() == ServingModel.SERVINGS_ACTIVATED ?
					String.valueOf(ServingModel.isActivated) : String.valueOf(ServingModel.isDeactivated)));
			
			ResultSet servings = this.servingModel.getServingsList(conditions);
			int sequence = 1;
			while (servings.next()) {
				//int no, int id, String name, int category_id, String descriptions
				//int is_new, int is_bestseller, String price, int status
				list.add(new ServingModel(
						sequence,
						servings.getInt("id"),
						servings.getString("name"),
						servings.getInt("category_id"),
						servings.getString("descriptions"),
						servings.getInt("price"),
						servings.getInt("is_new"),
						servings.getInt("is_bestseller"),
						servings.getInt("status")
						));
				sequence++;
			}
			tbl_servings.setItems(list);
		} catch (Exception e) {
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
