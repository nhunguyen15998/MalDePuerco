package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.AttributeModel;
import models.OrderDetailModel;
import models.OrderListModel;
import models.OrderModel;
import models.ServingAttributeModel;
import models.ServingModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;


public class CustomServingController implements Initializable {
	private static CustomerHomeController customerHomeController = new CustomerHomeController();
	private ServingModel servingModel = new ServingModel();
	private ServingAttributeModel servingAttributeModel = new ServingAttributeModel();
	private AttributeModel attributeModel = new AttributeModel();
	private OrderDetailModel orderDetailModel;

	private int servingId;
	private int qty = 1;
	private double price;
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
		this.apCustomServing.getStylesheets().add(getClass().getResource("/css/home-component.css").toExternalForm());
		
		//load sugar
		ObservableList<DataMapping> sugars = this.retrievesSugarIce(AttributeModel.SUGAR);
		cbSugar.setItems(sugars);
		cbSugar.getSelectionModel().selectFirst();
		//load ice
		ObservableList<DataMapping> ices = this.retrievesSugarIce(AttributeModel.ICE);
		cbIce.setItems(ices);
		cbIce.getSelectionModel().selectFirst();
		
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
			this.servingId = id;
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(this.servingId)));
			ResultSet serving = this.servingModel.getServingList(servingCondition);
			while(serving.next()) {
				//attributes
				this.retrievesServingAttributes(this.servingId);
				System.out.println("here:"+this.price);
				if(serving.getInt("servings.type") == ServingModel.FOOD) {
					cbSugar.getSelectionModel().clearSelection();
					cbIce.getSelectionModel().clearSelection();
					apCustomServing.getChildren().remove(paneSugarIce);
				}
				int serId = serving.getInt("id");
				String thumbnail = serving.getString("servings.thumbnail");
				String name = serving.getString("servings.name");
				double price = this.price != 0 ? this.price : serving.getDouble("servings.price");
				int stock = serving.getInt("servings.quantity");
				double total = Double.parseDouble(tfQuantity.getText())*price;
				txtServingName.setText(name);
				ivThumbnail.setImage(new Image(getClass().getResourceAsStream(thumbnail)));
				lblStockQuantity.setText(stock + " item(s) available");
				lblPrice.setText(Helpers.formatNumber(null).format(price)+"vnd");
				lblTotal.setText(Helpers.formatNumber(null).format(total)+"vnd");
				System.out.println("price:" + price);
				System.out.println("total:" + this.price);
				System.out.println("total:" + total);
				
				tfQuantity.setOnKeyPressed(event -> {
					this.changeQuantityOnEnter(event, stock, price);
				});
				btnDone.setOnMouseClicked(event -> {
					this.btnDoneAction(id, serId, thumbnail, name);
					this.addMoreItemToOrderList();
				});	
				btnPlus.setOnMouseClicked(event -> {
					if(this.qty < stock) {
						this.qty++; 
						this.changeQuantity(price);
					}
				});
				btnMinus.setOnMouseClicked(event -> {
					if(this.qty > 1) {
						this.qty--;
						this.changeQuantity(price);
					} 
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//panel confirm when add more item to order list
	public void confirmAddMoreAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Order Confirmation");
		alert.setHeaderText("Add this item to your order?");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK) {
			int result = CustomServingController.customerHomeController.insertOrderDetails();
			if(result != 0) {
				CustomerHomeController.createdList.clear();
				CustomerHomeController.updatedList.clear();
				String orderBy = "order_details.id desc";
				CustomServingController.customerHomeController.renderUpdatedOrderList
				(CustomServingController.customerHomeController.loadUpdatedData(), orderBy);
			}
		} 
	}
	
	//btndone when has orderid
	public void addMoreItemToOrderList() {
		if(OrderModel.currentOrderId == 0) {
			CustomServingController.customerHomeController.addItemToOrderList();
		} else {
			this.confirmAddMoreAlert();
		}				
		this.btnCancelAction();
	}

	//qty
	public int loadServingQuantity(int id) {
		try {
			ArrayList<CompareOperator> condition  = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));//servingid
			ResultSet qties = this.servingAttributeModel.getServingAttributeList(condition);
			int qty = 0;
			while(qties.next()) {
				qty = qties.getInt("serving_attributes.quantity");
			}
			return qty;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void changeQuantityOnEnter(KeyEvent event, int stock, double price) {
		this.qty = Integer.parseInt(!tfQuantity.getText().isEmpty() ? tfQuantity.getText() : "1");
		if(event.getCode().equals(KeyCode.ENTER)) {
			tfQuantity.setText(qty > 0 ? String.valueOf(qty) : "1");
			if(qty > stock) {
				qty = stock;
				tfQuantity.setText(String.valueOf(qty));
			}
        }
		double total = this.price != 0 ? this.price * qty: price * qty;
		lblTotal.setText(Helpers.formatNumber(null).format(total)+"vnd");
		System.out.println("price:" + price);
		System.out.println("tfqty:" + this.price);
	}
	
	//btn- btn+
	public void changeQuantity(double price) {
		tfQuantity.setText(String.valueOf(this.qty));
		double total = this.price != 0 ? this.price * qty: price * qty;
		lblTotal.setText(Helpers.formatNumber(null).format(total)+"vnd");						
		System.out.println("price:" + price);
		System.out.println("btn-:" + this.price);
	}
	
	//btnDoneAction
	public void btnDoneAction(int id, int serId, String thumbnail, String name) {
		try {
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));
			ResultSet serving = this.servingModel.getServingList(servingCondition);
			
			while(serving.next()) {
				String size = "", sugar = "", ice = "";
				if(serving.getInt("servings.type") == ServingModel.DRINK) {
					sugar = cbSugar.getValue().value;
					ice = cbIce.getValue().value;
				}
				size = cbSize.getValue().value;
				String note = tfNote.getText().isEmpty() ? "" : tfNote.getText();
				
				boolean isExisted = false;
				for(OrderListModel item : CustomerHomeController.createdList) {
					if(item.getServingId() == id) {
						System.out.println("size:"+item.getSize());
						System.out.println(size);

						if(!item.getSize().equals(size)) {
							isExisted = false;
						}else {
							//size==
							if(item.getSugar() == "" || sugar.equals(item.getSugar()) && ice.equals(item.getIce())) {
								System.out.println("i:"+sugar.equals(item.getSugar()));

								int indexItem = CustomerHomeController.createdList.indexOf(item);
								OrderListModel currentItem = CustomerHomeController.createdList.get(indexItem); 
								currentItem.setQuantity(currentItem.getQuantity() + this.qty);
								currentItem.setTotalPrice(Double.parseDouble(Helpers.formatNumber(null).format(this.price * currentItem.getQuantity())));
								currentItem.setNote(note);
								isExisted = true;
								break;
							} else {
								isExisted = false;
							}
						}

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
							this.qty,
							size,
							sugar,
							ice
					));
				}	
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnCancelAction
	@FXML
	public void btnCancelAction() {
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		CustomServingController.customerHomeController.customerMasterHolder.setDisable(false);
		stage.close();
	}
	
	//sugar-ice
	public ObservableList<DataMapping> retrievesSugarIce(int parentId){//AttributeModel.SUGAR
		try {
			ArrayList<CompareOperator> sugarCondition = new ArrayList<CompareOperator>();
			sugarCondition.add(CompareOperator.getInstance("attributes.parent_id", "=", String.valueOf(parentId)));
			ResultSet parent = this.attributeModel.getAttributeList(sugarCondition);
			ObservableList<DataMapping> parents = FXCollections.observableArrayList();
			while(parent.next()) {
				parents.add(DataMapping.getInstance(parent.getInt("attributes.id"), parent.getString("attributes.name")));
			}
			return parents;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//attribute -size -portion
	public void retrievesServingAttributes(int id){
		try {
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));
			ResultSet sizes = this.servingAttributeModel.getServingAttributeList(servingCondition);
			ObservableList<DataMapping> attributes = FXCollections.observableArrayList();
			while(sizes.next()) {
				int servingAttributeId = sizes.getInt("serving_attributes.id");
				String attributeName = sizes.getString("attribute_name");
				attributes.add(DataMapping.getInstance(servingAttributeId, attributeName));
			}
			System.out.println("attri "+attributes);
			cbSize.getItems().setAll(attributes);
			cbSize.getSelectionModel().selectFirst();
			String defKey = cbSize.getSelectionModel().getSelectedItem().key;
			System.out.println("defKey: "+defKey);
			this.price = this.getPriceByAttributeId(id, defKey);
			lblPrice.setText(Helpers.formatNumber(null).format(this.price)+"vnd");
			System.out.println("first cbsize:" + this.price);
			
			cbSize.setOnAction(event -> {
				String itemSelected = cbSize.getSelectionModel().getSelectedItem().key;
				this.price = this.getPriceByAttributeId(id, itemSelected);
				lblPrice.setText(Helpers.formatNumber(null).format(this.price)+"vnd");
				lblTotal.setText(Helpers.formatNumber(null).format(this.price*Double.parseDouble(tfQuantity.getText()))+"vnd");
				System.out.println("click cbsize:" + this.price);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getPriceByAttributeId(int id, String itemId) {
		//price
		double attributePrice = 0;
		ArrayList<CompareOperator> attributeCondition = new ArrayList<CompareOperator>();
		attributeCondition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));
		attributeCondition.add(CompareOperator.getInstance("serving_attributes.id", "=", itemId));
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
