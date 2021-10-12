package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.OrderDetailModel;
import models.OrderListModel;
import models.OrderModel;
import models.ServingCategoryModel;
import models.ServingModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

public class CustomerHomeController implements Initializable {
	private ServingCategoryModel servingCategoryModel = new ServingCategoryModel();
	private ServingModel servingModel = new ServingModel();
	private OrderModel orderModel = new OrderModel();
	private OrderDetailModel orderDetailModel = new OrderDetailModel();

//	private TableModel tableModel = ;
	public static boolean isActive = false;
	public static boolean isCreated = true; 
	
	//main category
	public static final String APPETIZERS = "Appetizers";
	public static final String SIDE_ORDERS = "Side orders";
	public static final String A_LA_CARTE = "A La Carte";
	public static final String DESSERTS = "Desserts";
	public static final String BEVERAGES = "Beverages";
	
	private String mainCategorySelected;
	private String categorySelected;
		
	public static ObservableList<OrderListModel> createdList = FXCollections.observableArrayList();
	public static ObservableList<OrderDetailModel> updatedList = FXCollections.observableArrayList();

	private OrderListModel selected;
	
	private int userId = 3;
	private double totalPlace;
	public int orderDetailId = 0;
	private String orderCode = "";
	private int servingId;
	
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
	@FXML
	private Button btnOrder;
	@FXML
	private Button btnReloadUpdated;
	@FXML
	private Label lblOrderCode;
	
	//pane
	@FXML
	public FlowPane customerMasterHolder;//main pane
	@FXML
	private AnchorPane rightHolder;//pane containing orderListPane
	private AnchorPane apCustomServing;
	//parent
	@FXML
	private AnchorPane orderListPane;//childpane of rightholder including titlePane, spOrderList, btnPlace
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblTableName;
	//1
	private Pane titlePane = new Pane();
	//2
	private ScrollPane spOrderList = new ScrollPane();//has children: vboxOrderList,
	private VBox vboxOrderList = new VBox();//has children order list
	//order list
	private Pane vboxPane = new Pane();
	private ImageView ivImageView;
	private Label lblServingName;
	private Label lblItemPrice;
	private Label lblTotalPrice;
	private TextField tfNote;
	private TextField tfQuantity;
	private Button btnDelete;
	//private ImageView ivServingStatus;
	private Label lblCreatedAt;
	//private Button statusPane;

	//3
	private Button btnPlace = new Button();//has child: lblPlaceTotal
	private Label lblPlaceTotal = new Label();
	private Button btnPay = new Button();
	private Label lblPayTotal = new Label();
	
	//hbox
	@FXML
	private HBox categoryHBox;
	
	//grid has children serving grid
	@FXML
	private GridPane servingGridPane;
	//serving grid
	private Pane servingPane;
	private ImageView servingImage;
	private Text servingName;
	private Label servingPrice;
	private Text servingStock;
	
	//search 
	@FXML
	private TextField searchBox;
	
	//--------------METHODS---------------------
	//----------initialize & search-------------
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			this.lblTableName.setText(OrderModel.tableName);
			this.vboxOrderList.getChildren().clear();
			this.addItemToOrderList();
			this.btnAllAction();
			this.btnReloadUpdated.setVisible(false);
			this.lblOrderCode.setText(null);
			//btns
			
			btnServer.setOnMouseClicked(event -> {
				
			});
			btnNoti.setOnMouseClicked(event -> {
				
			});
			btnHelp.setOnMouseClicked(event -> {
				
			});
			btnHelp.setOnMouseClicked(event -> {
				
			});
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

	//------------sidebar btn------------------------------
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
	
	//----------------------icon btn------------------------
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
	
	//-----------------draw layout & render-------------------	
	//draw hbox showing categories
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
	
	//draw gridpane showing servings
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
	
	//render serving panes
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
				servingStock.setText(servings.getInt("quantity") + " item(s) in stock");
				//add
				//onclick to add				 
				addItem.setOnMouseClicked(event -> {
					//draw custom serving
					this.servingId = id;
					this.customServingLayout(id);
//					//CustomerHomeController.isCreated = true;
//					boolean isExisted = false;
//					for(OrderListModel item : createdList) {
//						if(item.getServingId() == id) {
//							int indexItem = createdList.indexOf(item);
//							OrderListModel currentItem = createdList.get(indexItem);
//							currentItem.setQuantity(currentItem.getQuantity()+1);
//							currentItem.setTotalPrice(currentItem.getItemPrice()*currentItem.getQuantity());
//							isExisted = true;
//						} 	
//					}
//					if(!isExisted) {
//						createdList.add(new OrderListModel(id, 
//								thumbnail, 
//								name, 
//								price, 
//								price, 
//								note, 
//								quantity));	
//					}	
//
//					this.addItemToOrderList();
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
	
	//---start order layout---
	//draw empty order list
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
		
	//draw title pane, scroll pane containing dishes - used in create, update, payment view
	public void titlePaneSPOrderLayout() {
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
		//sp
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
		anchorPane.getChildren().add(vboxOrderList);
		spOrderList.setContent(anchorPane);
	}
	
	//draw vbox - used in create, update, payment view
	public void vboxLayout(OrderListModel item, OrderDetailModel detail) {
		try {			
			//new
			ivImageView = new ImageView();
			lblServingName = new Label();
			lblItemPrice = new Label();
			lblTotalPrice = new Label();
			tfNote = new TextField();
			tfQuantity = new TextField();
			btnDelete = new Button();
			ImageView ivServingStatus = new ImageView();
			lblCreatedAt = new Label();
			Button statusPane = new Button();

			
			//ImageView 
			ivImageView.setFitWidth(36);
			ivImageView.setFitHeight(36);
			ivImageView.setLayoutX(10);
			ivImageView.setLayoutY(9);
			
			statusPane.setPrefSize(30, 30);
			statusPane.setLayoutX(231);
			statusPane.setLayoutY(51);
			ivServingStatus.setFitHeight(15);
			ivServingStatus.setFitWidth(15);
			ivServingStatus.setLayoutX(8);
			ivServingStatus.setLayoutY(8);
			
			//Label 
			lblServingName.setPrefSize(139, 20);
			lblServingName.setLayoutX(48);
			lblServingName.setLayoutY(9);
			lblServingName.setTextFill(Color.WHITE);
			lblServingName.setFont(Font.font("System Bold", 9));
			lblItemPrice.setLayoutX(48);
			lblItemPrice.setLayoutY(28);
			lblItemPrice.setTextFill(Color.web("#ea7c69"));
			lblItemPrice.setFont(Font.font("System Bold", 9));
			
			lblCreatedAt.setLayoutX(100);
			lblCreatedAt.setPrefWidth(100);
			lblCreatedAt.setLayoutY(28);
			lblCreatedAt.setTextFill(Color.WHITE);
			lblCreatedAt.setFont(Font.font("System Italic", 9));
			
			lblTotalPrice.setLayoutX(230);
			lblTotalPrice.setLayoutY(19);
			lblTotalPrice.setTextFill(Color.web("#ea7c69"));
			lblTotalPrice.setFont(Font.font("System Bold", 14));
			//TextField 
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
						item.setTotalPrice(qty*item.getItemPrice());
					} else {
						this.deleteItemOrder(item);
					}
					this.addItemToOrderList();
		        }
			});
			btnDelete.setStyle("-fx-background-color: #2B2B2B; -fx-background-radius: 5px; "
									+ "-fx-border-color: #EA7C69; -fx-border-radius: 5px");
			btnDelete.setPrefSize(30, 30);
			btnDelete.setLayoutX(231);
			btnDelete.setLayoutY(51);
			btnDelete.setOnMouseClicked(event -> {
				System.out.println(item);
				this.deleteItemOrder(item);
			});
			ImageView deleteIcon = new ImageView(getClass().getResource("/assets/delete-o.png").toExternalForm());
			deleteIcon.setFitHeight(15);
			deleteIcon.setFitWidth(15);
			btnDelete.setGraphic(deleteIcon);
			
//			Button btnRefresh = new Button("reload");
//			btnRefresh.setOnMouseClicked(event -> {
//				CustomerHomeController.updatedList.clear();
//				this.getUpdated();
//			});
			//Pane
			Pane vboxPane = new Pane();
			vboxPane.setPrefSize(275, 90);
			
			if(CustomerHomeController.isCreated) {
				vboxPane.getChildren().addAll(ivImageView, lblServingName, lblItemPrice, lblTotalPrice, 
												   tfNote, tfQuantity, btnDelete);
			} else {
				CustomerHomeController.isCreated = false;
				tfNote.setDisable(true);
				tfQuantity.setDisable(true);
				//set icon status
				
				switch(detail.getServingStatus()) {
					case OrderDetailModel.PENDING:
						ivServingStatus.setImage(new Image(getClass().getResource("/assets/sandclock.png").toExternalForm()));
						statusPane.setStyle("-fx-background-color: #F23E5C !important; -fx-background-radius: 5px");
						tfNote.setDisable(false);
						tfQuantity.setDisable(false);
						vboxPane.getChildren().addAll(ivImageView, lblServingName, lblItemPrice, lblTotalPrice, 
								   tfNote, tfQuantity, btnDelete);
						break;
					case OrderDetailModel.COOKING:
						ivServingStatus.setImage(new Image(getClass().getResource("/assets/cooking.png").toExternalForm()));
						statusPane.setStyle("-fx-background-color: #ff2351 !important; -fx-background-radius: 5px");
						vboxPane.getChildren().addAll(ivImageView, lblServingName, lblItemPrice, lblTotalPrice, 
								tfNote, tfQuantity, statusPane, lblCreatedAt);
						break;
					case OrderDetailModel.READY:
						ivServingStatus.setImage(new Image(getClass().getResource("/assets/ready.png").toExternalForm()));
						statusPane.setStyle("-fx-background-color: #34a853 !important; -fx-background-radius: 5px");
						vboxPane.getChildren().addAll(ivImageView, lblServingName, lblItemPrice, lblTotalPrice, 
								tfNote, tfQuantity, statusPane, lblCreatedAt);
						break;
					case OrderDetailModel.SERVING:
						ivServingStatus.setImage(new Image(getClass().getResource("/assets/serving.png").toExternalForm()));
						statusPane.setStyle("-fx-background-color: #6F71C2 !important; -fx-background-radius: 5px");
						vboxPane.getChildren().addAll(ivImageView, lblServingName, lblItemPrice, lblTotalPrice, 
								tfNote, tfQuantity, statusPane, lblCreatedAt);
						break;
					case OrderDetailModel.SERVED:
						ivServingStatus.setImage(new Image(getClass().getResource("/assets/served.png").toExternalForm()));
						statusPane.setStyle("-fx-background-color: #F07613 !important; -fx-background-radius: 5px");
						vboxPane.getChildren().addAll(ivImageView, lblServingName, lblItemPrice, lblTotalPrice, 
								tfNote, tfQuantity, statusPane, lblCreatedAt);
						break;
				}
				statusPane.setGraphic(ivServingStatus);

			}
			
			vboxOrderList.getChildren().addAll(vboxPane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//draw create order list pane
	public void createOrderListLayout() {
		orderListPane.getChildren().clear();
		//btn + label
		btnPlace.setText("Place");
		btnPlace.setTextFill(Color.WHITE);
		btnPlace.setFont(Font.font("System Bold", 14));
		btnPlace.setPrefSize(260, 36);
		btnPlace.setLayoutX(8);
		btnPlace.setLayoutY(478);
		btnPlace.setStyle("-fx-border-color: #EA7C69; -fx-background-radius: 10px; "
							+ "-fx-background-color: #2B2B2B; -fx-border-radius: 10px");
		btnPlace.setContentDisplay(ContentDisplay.RIGHT);
		lblPlaceTotal.setTextFill(Color.WHITE);
		lblPlaceTotal.setFont(Font.font("System Bold", 14));		
		btnPlace.setGraphic(lblPlaceTotal);
		btnPlace.setOnMouseClicked(event -> {
			this.btnPlaceAction();
		});		
		//pane
		this.titlePaneSPOrderLayout();
		orderListPane.getChildren().addAll(titlePane, spOrderList, btnPlace);
	}
		
	//set content of vboxchildpane - create
	public void renderVBoxPaneWithSetCreatedContent(OrderListModel item) {
		CustomerHomeController.isCreated = true;
		this.vboxLayout(item, null);
		lblServingName.setText(item.getServingName());
		lblItemPrice.setText("$"+ Helpers.formatNumber(null).format(item.getItemPrice()));
		lblTotalPrice.setText("$"+Helpers.formatNumber(null).format(item.getTotalPrice()));
		tfNote.setText(item.getNote());
		tfQuantity.setText(String.valueOf(item.getQuantity()));
		ivImageView.setImage(new Image(CustomerHomeController.class.getResourceAsStream(item.getThumbnail())));
	}
	
	//draw update order list pane
	public void updateOrderListLayout() {
		orderListPane.getChildren().clear();
		//btn + label
		btnPay.setText("Pay");
		btnPay.setTextFill(Color.WHITE);
		btnPay.setFont(Font.font("System Bold", 14));
		btnPay.setPrefSize(260, 36);
		btnPay.setLayoutX(8);
		btnPay.setLayoutY(478);
		btnPay.setStyle("-fx-border-color: #EA7C69; -fx-background-radius: 10px; "
							+ "-fx-background-color: #2B2B2B; -fx-border-radius: 10px");
		btnPay.setContentDisplay(ContentDisplay.RIGHT);
		lblPayTotal.setTextFill(Color.WHITE);
		lblPayTotal.setFont(Font.font("System Bold", 14));		
		btnPay.setGraphic(lblPayTotal);
		btnPay.setOnMouseClicked(event -> {
			this.btnPlaceAction();
		});
		//pane
		this.titlePaneSPOrderLayout();
		orderListPane.getChildren().addAll(titlePane, spOrderList, btnPay);
	}
	
	//set content of vboxchildpane - update
	public void renderVBoxPaneWithSetUpdatedContent(OrderDetailModel item) {
		CustomerHomeController.isCreated = false;
		this.vboxLayout(null, item);
		lblCreatedAt.setText(item.getCreatedAt());
		lblServingName.setText(item.getServingName());
		lblItemPrice.setText("$"+ Helpers.formatNumber(null).format(item.getPrice()));
		lblTotalPrice.setText("$"+Helpers.formatNumber(null).format(item.getTotal()));
		tfNote.setText(item.getServingNote());
		tfQuantity.setText(String.valueOf(item.getQuantity()));
		ivImageView.setImage(new Image(CustomerHomeController.class.getResourceAsStream(item.getThumbnail())));
	}
	
	
	//---end order layout---
	
	//-----------------working with order----------------------
	//add to order list - create
	public void addItemToOrderList() {
		if(createdList.size() > 0) {
			
			if(CustomerHomeController.isCreated) {
				System.out.println("second click");
				this.createOrderListLayout();
				vboxOrderList.getChildren().clear();
			}
			double total = 0;
			for(OrderListModel item : createdList) {
				this.renderVBoxPaneWithSetCreatedContent(item);
				total += (item.getItemPrice()*item.getQuantity());
			}			
			this.totalPlace = Double.parseDouble(Helpers.formatNumber(null).format(total));
			lblPlaceTotal.setText("$"+Helpers.formatNumber(null).format(total));
		} else {
			this.emptyOrderList();
		}
	}
	
	//render update order list
	public void getUpdated() {
		try {
			ArrayList<CompareOperator> updated = new ArrayList<CompareOperator>();
			updated.add(CompareOperator.getInstance("orders.table_id", "=", String.valueOf(OrderModel.tableId)));
			updated.add(CompareOperator.getInstance("orders.id", "=", String.valueOf(OrderModel.currentOrderId)));
			ResultSet orderDetails = this.orderDetailModel.getOrderDetailList(updated);
			while(orderDetails.next()) {
				this.orderCode = orderDetails.getString("orders.code");
				updatedList.add(new OrderDetailModel(
						orderDetails.getInt("order_details.serving_id"),
						orderDetails.getString("servings.thumbnail"),
						orderDetails.getString("servings.name"),
						orderDetails.getDouble("order_details.price"),
						orderDetails.getDouble("order_details.total"),
						orderDetails.getString("order_details.serving_note"),
						orderDetails.getInt("order_details.quantity"),
						orderDetails.getInt("order_details.serving_status"),
						orderDetails.getString("order_details.created_at")
					
				));
			}
			this.lblOrderCode.setText(this.orderCode);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void renderUpdatedOrderList() {
		try {
			//get order detail from db
			this.getUpdated();
			if(updatedList.size() > 0) {
				this.updateOrderListLayout();
				vboxOrderList.getChildren().clear();
				for(OrderDetailModel item : updatedList) {
					this.renderVBoxPaneWithSetUpdatedContent(item);//servingname, thumbnail, quantity, total, serving note, 
					   											   //sevingstatus, createdat, servingid, price
				}
			} else {
				this.emptyOrderList();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//delete from order list
	public void deleteItemOrder(OrderListModel item) {
		System.out.println(createdList.indexOf(item));
		createdList.remove(item);
		this.addItemToOrderList();
	}
				
	//click btn place
	public void btnPlaceAction() {
		try {
			ArrayList<DataMapping> orderData = new ArrayList<DataMapping>();
			orderData.add(DataMapping.getInstance("code", "OC"+Helpers.randomString(6)));
			orderData.add(DataMapping.getInstance("table_id", String.valueOf(OrderModel.tableId)));
			orderData.add(DataMapping.getInstance("user_id", String.valueOf(this.userId)));
			orderData.add(DataMapping.getInstance("total_amount", String.valueOf(this.totalPlace)));
			OrderModel.currentOrderId = this.orderModel.createOrder(orderData);
			ArrayList<DataMapping> orderDetailData = new ArrayList<DataMapping>();
			int result = 0;
			for(OrderListModel item : createdList) {
				orderDetailData.add(DataMapping.getInstance("order_id", String.valueOf(OrderModel.currentOrderId)));
				orderDetailData.add(DataMapping.getInstance("serving_id", String.valueOf(item.getServingId())));
				orderDetailData.add(DataMapping.getInstance("serving_note", item.getNote().isEmpty() ? null : item.getNote()));
				orderDetailData.add(DataMapping.getInstance("serving_status", String.valueOf(OrderDetailModel.READY)));
				orderDetailData.add(DataMapping.getInstance("quantity", String.valueOf(item.getQuantity())));
				orderDetailData.add(DataMapping.getInstance("price", String.valueOf(Helpers.formatNumber(null).format(item.getItemPrice()))));
				orderDetailData.add(DataMapping.getInstance("total", String.valueOf(Helpers.formatNumber(null).format(item.getTotalPrice()))));
				result = this.orderDetailModel.createOrderDetail(orderDetailData);
				orderDetailData.clear();
			}
			if(result != 0) {
				createdList.clear();
				this.emptyOrderList();
			}
			CustomerHomeController.isCreated = false;
			this.renderUpdatedOrderList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//load data from orderdetail by order id
	public void btnReloadUpdatedAction(){
		try {
			//has order id
			if(OrderModel.currentOrderId != 0) {
				this.renderUpdatedOrderList();
			} else {
				this.btnReloadUpdated.setDisable(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//draw custom serving
	public void customServingLayout(int id) {
		try {
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			FXMLLoader apCustomServing = new FXMLLoader(getClass().getResource("/views/custom-serving.fxml"));
			this.apCustomServing = apCustomServing.load();
			
			//controller
			CustomServingController controller = apCustomServing.getController();
			controller.loadDataToCustomServing(id, this);
			
			Scene scene = new Scene(this.apCustomServing, 307, 396);
			Stage stage = new Stage();
			stage.setX((screenBounds.getWidth() - 307)/2);
			stage.setY((screenBounds.getHeight() - 396)/2);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene);
			stage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//get
	public int getServingId() {
		return servingId;
	}

	public void setServingId(int servingId) {
		this.servingId = servingId;
	}

}
