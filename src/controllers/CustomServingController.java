package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.SizeSequence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.OrderDetailModel;
import models.OrderListModel;
import models.ServingAttributeModel;
import models.ServingModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;


public class CustomServingController implements Initializable {
	private static CustomerHomeController customerHomeController = new CustomerHomeController();
	private ServingModel servingModel = new ServingModel();
	private ServingAttributeModel servingAttributeModel = new ServingAttributeModel();
	private OrderDetailModel orderDetailModel;

	private int servingId;
	private int qty = 1;
	private double price = 0;
	//xml
	@FXML
	private AnchorPane apCustomServing;
	@FXML
	private Text txtServingName;
	@FXML
	private ImageView ivThumbnail;
	@FXML
	private Label lblPrice;
	@FXML
	private ComboBox<DataMapping> cbSize;
	@FXML
	private Label lblSize;
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
	private ComboBox<DataMapping> cbIce;
	@FXML
	private Label lblIce;
	@FXML
	private ComboBox<DataMapping> cbSugar;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnDone;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//load sugar
		ObservableList<DataMapping> sugars = FXCollections.observableArrayList(OrderDetailModel.isSugarFree, 
			OrderDetailModel.isSugar25, OrderDetailModel.isSugar50, OrderDetailModel.isSugar75, OrderDetailModel.isSugar100);
		cbSugar.setItems(sugars);
		
		//load ice
		ObservableList<DataMapping> ices = FXCollections.observableArrayList(OrderDetailModel.isIceFree, 
				OrderDetailModel.isIce25, OrderDetailModel.isIce50, OrderDetailModel.isIce75, OrderDetailModel.isIce100);
		cbIce.setItems(ices);
		
		tfNote.setPromptText("Leave a note");
		tfQuantity.setText("1");
		tfNote.setStyle("-fx-text-fill: white; -fx-background-color: #3B3F40");
		tfQuantity.setStyle("-fx-text-fill: white; -fx-background-color: #3B3F40");
		cbSize.setStyle("-fx-text-fill: white; -fx-background-color: #3B3F40");
		cbIce.setStyle("-fx-text-fill: white; -fx-background-color: #3B3F40");
		cbSugar.setStyle("-fx-text-fill: white; -fx-background-color: #3B3F40");
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
				//attributes
				ArrayList<DataMapping> attributes = this.mapAttributeIdWithName(this.servingId);
				this.mapAttributeNameWithValue(this.servingId, attributes);

				if(serving.getInt("servings.type") != ServingModel.FOOD) {
					System.out.print("cold");
					if(serving.getInt("servings.type") == ServingModel.HOT_DRINK) {
						System.out.print("hot");
						paneSugarIce.getChildren().removeAll(lblIce, cbIce);
						cbSugar.setPrefWidth(262);
					} 
				} else {
					System.out.print("food");
					apCustomServing.getChildren().remove(paneSugarIce);
				}
				
				int serId = serving.getInt("id");
				String thumbnail = serving.getString("servings.thumbnail");
				String name = serving.getString("servings.name");
				double price = serving.getDouble("servings.price");
				int stock = serving.getInt("stock_quantity");
				txtServingName.setText(name);
				ivThumbnail.setImage(new Image(getClass().getResourceAsStream(thumbnail)));
				lblStockQuantity.setText(stock + " item(s) available");
				lblPrice.setText("$" + price);
				lblTotal.setText("$" + (1*price));
				tfQuantity.setOnKeyPressed(event -> {
					this.qty = Integer.parseInt(!tfQuantity.getText().isEmpty() ? tfQuantity.getText() : "1");
					System.out.println(qty);
					if(event.getCode().equals(KeyCode.ENTER)) {
						tfQuantity.setText(qty > 0 ? String.valueOf(qty) : "1");
						if(qty > stock) {
							qty = stock;
							tfQuantity.setText(String.valueOf(qty));
						}
			        }
					double total = Double.parseDouble(Helpers.formatNumber(null).format(price * qty));
					lblTotal.setText("$" + total);
				});
				btnDone.setOnMouseClicked(event -> {
					String note = tfNote.getText().isEmpty() ? "" : tfNote.getText();
					boolean isExisted = false;
					for(OrderListModel item : CustomerHomeController.createdList) {
						if(item.getServingId() == id) {
							int indexItem = CustomerHomeController.createdList.indexOf(item);
							OrderListModel currentItem = CustomerHomeController.createdList.get(indexItem);
							currentItem.setQuantity(currentItem.getQuantity() + this.qty);
							currentItem.setTotalPrice(Double.parseDouble(Helpers.formatNumber(null).format(currentItem.getItemPrice() * currentItem.getQuantity())));
							currentItem.setNote(note);
							//sugar/ice/size
							isExisted = true;
						} 	
					}
					if(!isExisted) {
						CustomerHomeController.createdList.add(new OrderListModel(
								serId, 
								thumbnail, 
								name, 
								price, 
								price*this.qty, 
								note, 
								//sugar/ice/size
								this.qty));	
					}	
					System.out.print(CustomerHomeController.createdList);
					CustomServingController.customerHomeController.addItemToOrderList();
					this.btnCancelAction();
				});	
				btnPlus.setOnMouseClicked(event -> {
					if(this.qty < stock) {
						this.qty++; 
						tfQuantity.setText(String.valueOf(this.qty));
						lblTotal.setText("$"+Double.parseDouble(Helpers.formatNumber(null).format(this.qty*price)));
					}
				});
				btnMinus.setOnMouseClicked(event -> {
					if(this.qty > 1) {
						this.qty--;
						tfQuantity.setText(String.valueOf(this.qty));
						lblTotal.setText("$"+Double.parseDouble(Helpers.formatNumber(null).format(this.qty*price)));
					} 
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//btnDoneAction
	public void btnDoneAction(int id) {
		try {
			//if has order id
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnCancelAction
	public void btnCancelAction() {
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		CustomServingController.customerHomeController.customerMasterHolder.setDisable(false);
		stage.close();
	}
	
	//load size
	public ArrayList<DataMapping> mapAttributeIdWithName(int id) {
		try {
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));
			ResultSet sizes = this.servingAttributeModel.getServingAttributeList(servingCondition);
			ArrayList<DataMapping> attributes = new ArrayList<DataMapping>();
			while(sizes.next()) {
				int attributeId = sizes.getInt("serving_attributes.id");
				int attribute = sizes.getInt("serving_attributes.attribute");
				attributes.add(DataMapping.getInstance(attributeId, String.valueOf(attribute)));
			}
			return attributes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void mapAttributeNameWithValue(int id, ArrayList<DataMapping> attributes) {//servingId, array
		try {
			ArrayList<DataMapping> sizeValues = new ArrayList<DataMapping>();
			if(attributes.size() > 0) {
				for(DataMapping item : attributes) {
	
					//load to cbb
					int key = Integer.parseInt(item.value);
					switch (key) {
						case ServingAttributeModel.SIZE_S: 
							sizeValues.add(ServingAttributeModel.isSizeS);
							break;
						case ServingAttributeModel.SIZE_M: 
							sizeValues.add(ServingAttributeModel.isSizeM);
							break;
						case ServingAttributeModel.SIZE_L: 
							sizeValues.add(ServingAttributeModel.isSizeL);
							break;
						case ServingAttributeModel.ONE_PERSON: 
							sizeValues.add(ServingAttributeModel.OnePerson);
							break;
						case ServingAttributeModel.TWO_PEOPLE: 
							sizeValues.add(ServingAttributeModel.TwoPeople);
							break;
						case ServingAttributeModel.FOUR_PEOPLE: 
							sizeValues.add(ServingAttributeModel.FourPeople);
							break;
						
						default:
							throw new IllegalArgumentException("Unexpected value: " + key);
					}
				}
				for(DataMapping it : sizeValues) {
					cbSize.getItems().add(it);
				}
				cbSize.setOnAction(event -> {
					DataMapping item = cbSize.getSelectionModel().getSelectedItem();
					this.price = this.getPriceByAttributeId(id, item);
					lblPrice.setText("$" + this.price);
					
				});
			} else {
				apCustomServing.getChildren().removeAll(lblSize, cbSize);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public double getPriceByAttributeId(int id, DataMapping item) {
		//price
		double attributePrice = 0;
		ArrayList<CompareOperator> attributeCondition = new ArrayList<CompareOperator>();
		attributeCondition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));
		attributeCondition.add(CompareOperator.getInstance("serving_attributes.attribute", "=", item.key));
		ResultSet prices = this.servingAttributeModel.getServingAttributeList(attributeCondition);
		try {
			while(prices.next()) {
				attributePrice = prices.getDouble("price_of_item_with_attribute");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(attributePrice);
		return attributePrice;
	}
	
	
	
	
	
	
	
}
