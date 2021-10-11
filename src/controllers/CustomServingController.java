package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.SizeSequence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.ServingAttributeModel;
import models.ServingModel;
import utils.CompareOperator;
import utils.DataMapping;


public class CustomServingController implements Initializable {
	private static CustomerHomeController customerHomeController = new CustomerHomeController();
	private ServingModel servingModel = new ServingModel();
	private ServingAttributeModel servingAttributeModel = new ServingAttributeModel();

	private int servingId;
	
	//xml
	@FXML
	private AnchorPane apCustomServing;
	@FXML
	private Label lblServingName;
	@FXML
	private ImageView ivThumbnail;
	@FXML
	private Label lblPrice;
	@FXML
	private ComboBox<DataMapping> cbSize;
	@FXML
	private TextField tfNote;
	@FXML
	private TextField tfQuantity;
	@FXML
	private Label lblTotal;
	@FXML
	private Button btnPlus;
	@FXML
	private Button btnMinus;
	@FXML
	private Label lblStockQuantity;
	@FXML
	private Pane paneSugarIce;
	@FXML
	private ComboBox cbIce;
	@FXML
	private ComboBox cbSugar;
	@FXML
	private Button btnCancel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//if serving type = drink => paneSugar
		//load size
		//cbSize.setItems(sizes);
		
	}
	
	//load data
	public void loadDataToCustomServing(int id, CustomerHomeController customerHomeController) {
		try {
			CustomServingController.customerHomeController = customerHomeController;
			CustomServingController.customerHomeController.customerMasterHolder.setDisable(true);
			this.servingId = customerHomeController.getServingId();
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(this.servingId)));
			ResultSet serving = this.servingModel.getServingList(servingCondition);
			while(serving.next()) {
				lblServingName.setText(serving.getString("servings.name"));
				ivThumbnail.setImage(new Image(getClass().getResourceAsStream(serving.getString("servings.thumbnail"))));
				lblStockQuantity.setText(serving.getInt("servings.quantity") + "item(s) available");
				ObservableList<DataMapping> sizes = FXCollections.observableArrayList();
//				while(serving.getInt("serving_attributes.serving_id") == this.servingId) {
//					int attribute = serving.getInt("serving_attributes.attribute");
//					sizes.add(DataMapping.getInstance(attribute, attribute == 0 ? String.valueOf(ServingAttributeModel.isSizeS) : 
//													 (attribute == 1 ? String.valueOf(ServingAttributeModel.isSizeM) : 
//													 (attribute == 2 ? String.valueOf(ServingAttributeModel.isSizeL) :
//													 (attribute == 3 ? String.valueOf(ServingAttributeModel.OnePerson) :
//													 (attribute == 4 ? String.valueOf(ServingAttributeModel.TwoPeople) : 
//														 			   String.valueOf(ServingAttributeModel.FourPeople)))))));
//				lblPrice.setText("$" + serving.getDouble("price_of_item_with_attribute"));

			
					lblPrice.setText("$" + serving.getDouble("servings.price"));

				

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//btnPlusAction
	public void btnPlusAction() {
		
	}
	
	//btnMinusAction
	public void btnMinusAction() {
		
	}
	
	//btnDoneAction
	public void btnDoneAction() {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//btnCancelAction
	public void btnCancelAction() {
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		CustomServingController.customerHomeController.customerMasterHolder.setDisable(false);
		stage.close();
	}
	
	//close
	public void close() {
	
	}
}
