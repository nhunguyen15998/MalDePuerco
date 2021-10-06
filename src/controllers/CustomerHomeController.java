package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import models.OrderListModel;
import models.ServingCategoryModel;
import models.ServingModel;
import utils.CompareOperator;

public class CustomerHomeController implements Initializable {
	private ServingCategoryModel servingCategoryModel = new ServingCategoryModel();
	private ServingModel servingModel = new ServingModel();
	public static boolean isActive = false;
	
	//main category
	public static final String APPETIZERS = "Appetizers";
	public static final String SIDE_ORDERS = "Side orders";
	public static final String A_LA_CARTE = "A La Carte";
	public static final String DESSERTS = "Desserts";
	public static final String BEVERAGES = "Beverages";
	
	private String mainCategorySelected;
	private String categorySelected;
	private int servingId;
	
	
	
	private static ObservableList<OrderListModel> list = FXCollections.observableArrayList();
	private OrderListModel selected;
	
	//sidebar btn
	@FXML
	private Button btnAll;
	@FXML
	private Button btnAppetizer;
	@FXML
	private Button btnSideOrder;
	@FXML
	private Button btnALaCarte;
	@FXML
	private Button btnDessert;
	@FXML
	private Button btnBeverage;
	
	//icon btn
	@FXML
	private Button btnServer;
	@FXML
	private Button btnNoti;
	@FXML
	private Button btnSetting;
	@FXML
	private Button btnHelp;

	//pane
	@FXML
	private FlowPane customerMasterHolder;
	//parent
	@FXML
	private AnchorPane orderListPane;
	//1
	private ScrollPane spOrderList = new ScrollPane();
	private VBox vboxOrderList = new VBox();
	//2
	private Pane titlePane = new Pane();
	//3
	private Button btnPlace = new Button();
	private Label lblPlaceTotal = new Label();

	//serving grid
	private Pane servingPane;
	private ImageView servingImage;
	private Text servingName;
	private Label servingPrice;
	private Text servingStock;
	
	//list
	private ImageView ivImageView;
	private Label lblServingName;
	private Label lblItemPrice;
	private Label lblTotalPrice;
	private TextField tfNote;
	private TextField tfQuantity;
	
	//hbox
	@FXML
	private HBox categoryHBox;
	
	//grid
	@FXML
	private GridPane servingGridPane;
	
	//search 
	@FXML
	private TextField searchBox;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			this.vboxOrderList.getChildren().clear();
//			list.add(new OrderListModel(1, "/assets/dish-2.png", "bbbbbbbbbb", "$2.3", "$4.6", "No spice", 2));
//			list.add(new OrderListModel(2, "/assets/dish-3.png", "cccccccccc", "$2.3", "$4.6", "No spice", 2));
//			list.add(new OrderListModel(3, "/assets/dish-4.png", "dddddddddd", "$2.3", "$4.6", "No spice", 2));
//			list.add(new OrderListModel(4, "/assets/dish-1.png", "eeeeeeeeee", "$2.3", "$4.6", "No spice", 2));
			this.addItemToOrderList();

			this.btnAllAction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//add to order list
	public void addItemToOrderList() {	
		if(list.size() > 0) {
			this.orderListLayout();
			vboxOrderList.getChildren().clear();
			for(OrderListModel item : list) {
				this.vboxLayout(item);
			}
		} else {
			this.emptyOrderList();
		}
	}
	
	//empty order list
	public void emptyOrderList() {
		//empty order list
		orderListPane.getChildren().removeAll(titlePane, btnPlace);	
		spOrderList.setVisible(false);
		//imageview
		Label lblEmptyOrder = new Label("Your order list is empty");
		lblEmptyOrder.setLayoutX(33);
		lblEmptyOrder.setLayoutY(280);
		lblEmptyOrder.setPrefWidth(209);
		lblEmptyOrder.setAlignment(Pos.CENTER);
		lblEmptyOrder.setTextFill(Color.web("#e87968"));
		ImageView emptyOrder = new ImageView();
		Image image = new Image(CustomerHomeController.class.getResourceAsStream("/assets/empty-cart.png"));
		emptyOrder.setImage(image);
		emptyOrder.setFitWidth(133);
		emptyOrder.setFitHeight(122);
		emptyOrder.setLayoutX(71);
		emptyOrder.setLayoutY(163);
		orderListPane.getChildren().addAll(emptyOrder, lblEmptyOrder);
	}
	
	//draw hbox
	public void hboxLayout(Pane categoryPane, Pane hBoxChild, ImageView categoryImage, Text categoryName) {
		try {
			//categoryPane
			categoryPane.setPrefSize(70, 84);
			categoryPane.setLayoutX(9);
			categoryPane.setLayoutY(3);
			categoryPane.setStyle("-fx-background-radius: 10px; -fx-background-color: #2B2B2B; "
									+ "-fx-border-radius: 10px; -fx-border-color: #2B2B2B;");
			//hBoxChild
			hBoxChild.setPrefSize(87, 90);
			//categoryImage
			categoryImage.setFitHeight(36);
			categoryImage.setFitWidth(43);
			categoryImage.setLayoutX(14);
			categoryImage.setLayoutY(14);
			categoryImage.setPickOnBounds(true);
			categoryImage.setPreserveRatio(true);
			//categoryName
			categoryName.setFill(Color.WHITE);
			categoryName.setLayoutX(-1);
			categoryName.setLayoutY(71);
			categoryName.setStrokeType(StrokeType.OUTSIDE);
			categoryName.setStrokeWidth(0);
			categoryName.setTextAlignment(TextAlignment.CENTER);
			categoryName.setWrappingWidth(73);
			categoryName.setFont(Font.font("System Bold", 9));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//draw gridpane
	public void gridPaneLayout(Pane servingPane, ImageView servingImage, Text servingName, Label servingPrice, Text servingStock) {
		try {
			servingGridPane.getStylesheets().add(getClass().getResource("/css/gridpane.css").toExternalForm());
			
			//servingpane
			servingPane.setPrefWidth(130);
			servingPane.setPrefHeight(150);
			servingPane.setMaxHeight(150);
			servingPane.setMaxWidth(130);
			servingPane.setStyle("-fx-background-radius: 10px; -fx-background-color: #2B2B2B; "
								+ "-fx-border-radius: 10px; -fx-border-color: #2B2B2B;");
			GridPane.setHalignment(servingPane, HPos.CENTER);
			GridPane.setValignment(servingPane, VPos.TOP);
			//servingimage
			servingImage.setFitHeight(65);
			servingImage.setFitWidth(73);
			servingImage.setLayoutX(29);
			servingImage.setLayoutY(12);
			servingImage.setPickOnBounds(true);
			servingImage.setPreserveRatio(true);
			//servingname
			servingName.setFill(Color.WHITE);
			servingName.setLayoutX(10);
			servingName.setLayoutY(87);
			servingName.setStrokeType(StrokeType.OUTSIDE);
			servingName.setStrokeWidth(0);
			servingName.setTextAlignment(TextAlignment.CENTER);
			servingName.setWrappingWidth(110);
			servingName.setFont(Font.font("System Bold", 9));
			//servingprice
			servingPrice.setFont(Font.font("System Bold", 14));
			servingPrice.setLayoutX(49);
			servingPrice.setLayoutY(105);
			servingPrice.setTextFill(Color.web("#ea7c69"));
			//servingstock
			servingStock.setFill(Color.web("#dfe5e7c2"));
			servingStock.setFont(Font.font("System Bold", 9));
			servingStock.setLayoutX(11);
			servingStock.setLayoutY(133);
			servingStock.setStrokeType(StrokeType.OUTSIDE);
			servingStock.setStrokeWidth(0);
			servingStock.setTextAlignment(TextAlignment.CENTER);
			servingStock.setWrappingWidth(110);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//draw vbox
	public void vboxLayout(OrderListModel item) {
		try {
			//Pane
			Pane vboxPane = new Pane();
			vboxPane.setPrefWidth(275);
			vboxPane.setPrefHeight(90);
			//ImageView 
			ivImageView = new ImageView();
			Image image = new Image(CustomerHomeController.class.getResourceAsStream(item.getThumbnail()));
			ivImageView.setImage(image);
			ivImageView.setFitWidth(36);
			ivImageView.setFitHeight(36);
			ivImageView.setLayoutX(10);
			ivImageView.setLayoutY(9);
			//Label 
			lblServingName = new Label(item.getServingName());
			lblServingName.setPrefSize(139, 20);
			lblServingName.setLayoutX(48);
			lblServingName.setLayoutY(9);
			lblServingName.setTextFill(Color.WHITE);
			lblServingName.setFont(Font.font("System Bold", 9));
			lblItemPrice = new Label("$"+item.getItemPrice());
			lblItemPrice.setLayoutX(48);
			lblItemPrice.setLayoutY(28);
			lblItemPrice.setTextFill(Color.web("#ea7c69"));
			lblItemPrice.setFont(Font.font("System Bold", 9));
			lblTotalPrice = new Label("$"+(item.getItemPrice()*item.getQuantity()));
			lblTotalPrice.setLayoutX(230);
			lblTotalPrice.setLayoutY(19);
			lblTotalPrice.setTextFill(Color.web("#ea7c69"));
			lblTotalPrice.setFont(Font.font("System Bold", 14));
			//TextField 
			tfNote = new TextField(item.getNote());
			tfNote.setPrefSize(205, 30);
			tfNote.setLayoutX(16);
			tfNote.setLayoutY(51);
			tfNote.setStyle("-fx-background-color: #3B3F40; -fx-text-fill: white;");
			tfNote.setPromptText("Leave a note");
			tfNote.setOnKeyPressed(event -> {
				if(event.getCode().equals(KeyCode.ENTER)) {
					item.setNote(tfNote.getText());
				}
			});
			tfQuantity = new TextField(String.valueOf(item.getQuantity()));
			tfQuantity.setPrefSize(30, 30);
			tfQuantity.setLayoutX(190);
			tfQuantity.setLayoutY(13);
			tfQuantity.setStyle("-fx-background-color: #3B3F40; -fx-text-fill: white;");
			tfQuantity.setOnKeyPressed(event -> {
				int qty = Integer.parseInt(!tfQuantity.getText().isEmpty() ? tfQuantity.getText() : "1");
				System.out.println(qty);
				if(event.getCode().equals(KeyCode.ENTER)) {
					if(qty > 0) {
						item.setQuantity(qty);
						tfQuantity.setText(String.valueOf(qty));
					} else {
						this.deleteItemOrder(item);
					}
		        }
			});
			Button btnDeleteNote = new Button();
			btnDeleteNote.setStyle("-fx-background-color: #2B2B2B; -fx-background-radius: 5px; "
									+ "-fx-border-color: #EA7C69; -fx-border-radius: 5px");
			btnDeleteNote.setPrefSize(30, 30);
			btnDeleteNote.setLayoutX(231);
			btnDeleteNote.setLayoutY(51);
			btnDeleteNote.setOnMouseClicked(event -> {
				System.out.println(item);
				this.deleteItemOrder(item);
			});
			ImageView deleteIcon = new ImageView(getClass().getResource("/assets/delete-o.png").toExternalForm());
			deleteIcon.setFitHeight(15);
			deleteIcon.setFitWidth(15);
			btnDeleteNote.setGraphic(deleteIcon);
			vboxPane.getChildren().addAll(ivImageView, lblServingName, lblItemPrice, lblTotalPrice, 
											   tfNote, tfQuantity, btnDeleteNote);
			vboxOrderList.getChildren().addAll(vboxPane);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//delete list item
	public void deleteItemOrder(OrderListModel item) {
		System.out.println(list.indexOf(item));
		list.remove(item);
		this.addItemToOrderList();
	}
	
	//render
	public void renderGrid(ArrayList<CompareOperator> subCategoryCondition, ArrayList<CompareOperator> servingCondition) {
		try {
			ResultSet subCategories = this.servingCategoryModel.getServingCategoryList(subCategoryCondition);
			while(subCategories.next()) {
				//hbox
				Pane categoryPane = new Pane();
				Pane hBoxChild = new Pane();
				ImageView categoryImage = new ImageView();
				Text categoryName = new Text();
				Text categoryId = new Text();
				this.hboxLayout(categoryPane, hBoxChild, categoryImage, categoryName);
				//set
				categoryId.setText(subCategories.getString("serving_categories.id"));
				categoryId.setVisible(false);
				categoryName.setText(subCategories.getString("name"));
				Image image = new Image(subCategories.getString("thumbnail"));
				categoryImage.setImage(image);
				//add
				categoryPane.getChildren().addAll(categoryName, categoryImage, categoryId);
				categoryPane.setOnMouseClicked(event ->  {
					Text catID = (Text) categoryPane.getChildren().get(2);
					this.categorySelected = catID.getText();
					this.categoryHBox.getChildren().clear();
					this.servingGridPane.getChildren().clear();
					ArrayList<CompareOperator> ssubCategoryCondition = new ArrayList<CompareOperator>();
					System.out.println(this.mainCategorySelected);
					if(!this.mainCategorySelected.isEmpty()) {
						ssubCategoryCondition.add(CompareOperator.getInstance("sc.name", "=", this.mainCategorySelected));
					}else {
						ssubCategoryCondition.add(CompareOperator.getInstance("serving_categories.parent_id", "!=", "0"));
					}
					
					ArrayList<CompareOperator> sservingCondition = this.getFilter();
					sservingCondition.add(CompareOperator.getInstance("sc.id", "=", this.categorySelected));
					this.renderGrid(ssubCategoryCondition, sservingCondition);
					
				});
				
				hBoxChild.getChildren().addAll(categoryPane);
				this.categoryHBox.getChildren().addAll(hBoxChild);		
			}
			
			//serving
			ResultSet servings = this.servingModel.getServingList(servingCondition);
			int x = 0;
			int y = 0;
			int count = 0;
			while(servings.next()) {
				//grid
				servingPane = new Pane();
				servingImage = new ImageView();
				servingName = new Text();
				servingPrice = new Label();
				servingStock = new Text();
				Button addItem = new Button("Add");
				Label prodId = new Label();
				this.gridPaneLayout(servingPane, servingImage, servingName, servingPrice, servingStock);
				//set
				int id = servings.getInt("id");
				String thumbnail = servings.getString("thumbnail");
				String name = servings.getString("name");
				double price = servings.getDouble("price");
				String note = "";
				int quantity = 1;
				prodId.setText(String.valueOf(id));
				servingName.setText(name);
				Image image = new Image(thumbnail);
				servingImage.setImage(image);
				servingPrice.setText("$"+price);
				servingStock.setText(servings.getInt("quantity") + " bowls in stock");
				//add
				//onclick to add				 
				addItem.setOnMouseClicked(event -> {
					boolean isExisted = false;
					for(OrderListModel item : list) {
						if(item.getServingId() == id) {
							int indexItem = list.indexOf(item);
							OrderListModel currentItem = list.get(indexItem);
							currentItem.setQuantity(currentItem.getQuantity()+1);
							currentItem.setTotalPrice(currentItem.getItemPrice()*currentItem.getQuantity());
							isExisted = true;
						} 	
					}
					if(!isExisted) {
						list.add(new OrderListModel(id, 
								thumbnail, 
								name, 
								price, 
								price, 
								note, 
								quantity));	
					}	
					this.addItemToOrderList();
				});
				servingPane.getChildren().addAll(prodId, servingImage, servingName, servingPrice, servingStock, addItem);
				this.servingGridPane.add(servingPane, x, y);//0,0 1,0 2,0 3,0
															//0,1 1,0 1,1 2,1 
															//0,2 2,0 1,2 2,2 
				count++;
				x++;
				if(count % 4 == 0) {
					y++;
					if(x > 1) {
						x = 0;
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//filter
	public ArrayList<CompareOperator> getFilter(){
		ArrayList<CompareOperator> filter = new ArrayList<CompareOperator>();
		String name = this.searchBox.getText();
		if(!name.isEmpty()) {
			filter.add(CompareOperator.getInstance("servings.name", "like", "%"+name+"%"));
		}		
		return filter;
	}
	
	//on search
	public void onSearch() {
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		ArrayList<CompareOperator> ssubCategoryCondition = new ArrayList<CompareOperator>();
		System.out.println(this.mainCategorySelected);
		if(!this.mainCategorySelected.isEmpty()) {
			ssubCategoryCondition.add(CompareOperator.getInstance("sc.name", "=", this.mainCategorySelected));
		}else {
			ssubCategoryCondition.add(CompareOperator.getInstance("serving_categories.parent_id", "!=", "0"));
		}
		
		ArrayList<CompareOperator> sservingCondition = this.getFilter();
		if(!this.categorySelected.isEmpty()) {
			sservingCondition.add(CompareOperator.getInstance("sc.id", "=", this.categorySelected));
		}
		this.renderGrid(ssubCategoryCondition, sservingCondition);
	}	

	
	//------------sidebar btn--------------------
	//btnall
	public void btnAllAction() {
		this.mainCategorySelected = "";
		this.categorySelected = "";
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		try {
			//category
			ArrayList<CompareOperator> subCategoryCondition = new ArrayList<CompareOperator>();
			subCategoryCondition.add(CompareOperator.getInstance("serving_categories.parent_id", "!=", String.valueOf(0)));
			this.renderGrid(subCategoryCondition, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//btnappetizer
	public void btnAppetizerAction() {
		this.mainCategorySelected = this.APPETIZERS;
		this.categorySelected = "";
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		try {
			//category
			ArrayList<CompareOperator> subCategoryCondition = new ArrayList<CompareOperator>();
			subCategoryCondition.add(CompareOperator.getInstance("sc.name", "=", CustomerHomeController.APPETIZERS));
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("scs.name", "=", CustomerHomeController.APPETIZERS));
			this.renderGrid(subCategoryCondition, servingCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//btnsideorder
	public void btnSideOrderAction() {
		this.mainCategorySelected = this.SIDE_ORDERS;
		this.categorySelected = "";
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		try {
			ArrayList<CompareOperator> subCategoryCondition = new ArrayList<CompareOperator>();
			subCategoryCondition.add(CompareOperator.getInstance("sc.name", "=", CustomerHomeController.SIDE_ORDERS));
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("scs.name", "=", CustomerHomeController.SIDE_ORDERS));
			this.renderGrid(subCategoryCondition, servingCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnalacarte
	public void btnALaCarteAction() {
		this.mainCategorySelected = this.A_LA_CARTE;
		this.categorySelected = "";
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		try {
			ArrayList<CompareOperator> subCategoryCondition = new ArrayList<CompareOperator>();
			subCategoryCondition.add(CompareOperator.getInstance("sc.name", "=", CustomerHomeController.A_LA_CARTE));
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("scs.name", "=", CustomerHomeController.A_LA_CARTE));
			this.renderGrid(subCategoryCondition, servingCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btndessert
	public void btnDessertAction() {
		this.mainCategorySelected = this.DESSERTS;
		this.categorySelected = "";
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		try {
			ArrayList<CompareOperator> subCategoryCondition = new ArrayList<CompareOperator>();
			subCategoryCondition.add(CompareOperator.getInstance("sc.name", "=", CustomerHomeController.DESSERTS));
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("scs.name", "=", CustomerHomeController.DESSERTS));
			this.renderGrid(subCategoryCondition, servingCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnbeverage
	public void btnBeverageAction() {
		this.mainCategorySelected = this.BEVERAGES;
		this.categorySelected = "";
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		try {
			ArrayList<CompareOperator> subCategoryCondition = new ArrayList<CompareOperator>();
			subCategoryCondition.add(CompareOperator.getInstance("sc.name", "=", CustomerHomeController.BEVERAGES));
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("scs.name", "=", CustomerHomeController.BEVERAGES));
			this.renderGrid(subCategoryCondition, servingCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnhelp
	public void btnHelpAction() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnsetting
	public void btnSettingAction() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnnoti
	public void btnNotiAction() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnserver
	public void btnServerAction() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnorder
	public void btnOrderAction() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//category
	
	//draw order list pane
	public void orderListLayout() {
		orderListPane.getChildren().clear();
		//pane
		titlePane = new Pane();
		titlePane.setPrefSize(260, 30);
		titlePane.setLayoutX(6);
		titlePane.setLayoutY(0);
		DropShadow ds = new DropShadow();
        ds.setOffsetY(1.0);
        ds.setHeight(1);
        ds.setColor(Color.web("#513e3e"));
		titlePane.setEffect(ds);
		//label
		Label item = new Label("Item");
		item.setLayoutX(16);
		item.setLayoutY(4);
		item.setTextFill(Color.WHITE);
		item.setFont(Font.font("System Bold", 9));
		Label qty = new Label("Qty");
		qty.setLayoutX(189);
		qty.setLayoutY(4);
		qty.setTextFill(Color.WHITE);
		qty.setFont(Font.font("System Bold", 9));
		Label total = new Label("Total");
		total.setLayoutX(231);
		total.setLayoutY(4);
		total.setTextFill(Color.WHITE);
		total.setFont(Font.font("System Bold", 9));
		titlePane.getChildren().addAll(item, qty, total);	
		
		//btn + label
		btnPlace.setText("Place");
		btnPlace.setTextFill(Color.WHITE);
		btnPlace.setFont(Font.font("System Bold", 14));
		btnPlace.setPrefSize(260, 36);
		btnPlace.setLayoutX(8);
		btnPlace.setLayoutY(478);
		btnPlace.setStyle("-fx-border-color: #EA7C69; -fx-background-radius: 10px; "
							+ "-fx-background-color: #2B2B2B; -fx-border-radius: 10px");
		lblPlaceTotal.setTextFill(Color.WHITE);
		lblPlaceTotal.setFont(Font.font("System Bold", 14));
		lblPlaceTotal.setText("");
		btnPlace.setGraphic(lblPlaceTotal);
		//scroll+anchorpane+vbox+pane
		spOrderList = new ScrollPane();
		spOrderList.setHbarPolicy(ScrollBarPolicy.NEVER);
		spOrderList.setVbarPolicy(ScrollBarPolicy.NEVER);
		spOrderList.setLayoutX(0);
		spOrderList.setLayoutY(28);
		spOrderList.setPrefSize(275, 440);
		spOrderList.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #2B2B2B");
		AnchorPane anchorPane = new AnchorPane();
		anchorPane.setPrefSize(272, 703);
		anchorPane.setStyle("-fx-background-color: #2B2B2B;");
		
		vboxOrderList.setStyle("-fx-background-color: #2B2B2B;");
		vboxOrderList.setPrefSize(272, 711);
		Pane vboxPane = new Pane();
		vboxPane.setPrefSize(275, 90);
		vboxOrderList.getChildren().add(vboxPane);
		anchorPane.getChildren().add(vboxOrderList);
		spOrderList.setContent(anchorPane);
		orderListPane.getChildren().addAll(titlePane, spOrderList, btnPlace);
		
	}
	
}
