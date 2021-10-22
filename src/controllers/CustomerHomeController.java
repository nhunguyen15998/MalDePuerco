package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
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
import models.AuthenticationModel;
import models.OrderDetailModel;
import models.OrderListModel;
import models.OrderModel;
import models.ServingAttributeModel;
import models.ServingCategoryModel;
import models.ServingModel;
import models.TableModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.HandleNotifications;
import utils.Helpers;

public class CustomerHomeController implements Initializable {
	private ServingCategoryModel servingCategoryModel = new ServingCategoryModel();
	private ServingModel servingModel = new ServingModel();
	private OrderModel orderModel = new OrderModel();
	private OrderDetailModel orderDetailModel = new OrderDetailModel();
	private ServingAttributeModel servingAttributeModel = new ServingAttributeModel();
	public MasterController masterController;
	private TableModel tableModel = new TableModel();
	public static int tableId;
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
	
	public static int serverId = 0;
	public double totalPlace;
	public String orderCode = "";
	private int servingId;
	public static String tableName;
	
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
	public Label lblOrderCode;
	
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
	public ScrollPane spOrderList = new ScrollPane();//has children: vboxOrderList,
	public AnchorPane anchorPane = new AnchorPane();
	public static VBox vboxOrderList = new VBox();//has children order list
	//order list
	private ImageView ivImageView;
	private Label lblServingName;
	private Label lblItemPrice;
	private Label lblTotalPrice;
	private TextField tfNote;
	private TextField tfQuantity;
	private Button btnDelete;
	private Label lblCreatedAt;
	private Label lblAttribute;

	//3
	private Button btnPlace = new Button();//has child: lblPlaceTotal
	private Label lblPlaceTotal = new Label();
	public Button btnPay = new Button();
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
	public void initialize(URL arg0, ResourceBundle arg1){
		this.vboxOrderList.getChildren().clear();
		this.addItemToOrderList();
		this.btnAllAction();
		MasterController.customerHomeController = this;
		loadOrderByTable();
		// connect to socket
		try {
			HandleNotifications.currentRole = AuthenticationModel.roleCode == null? "CUSTOMER": AuthenticationModel.roleCode;
			HandleNotifications.currentTable = String.valueOf(tableId);
			HandleNotifications.currentUser = AuthenticationModel.id;
			HandleNotifications.getInstance().handleReceivedMessage();//
			//this.customerMasterHolder.setVisible(false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		
		try {
			
						
			//btns
			
			btnServer.setOnMouseClicked(event -> {
				
			});
			btnHelp.setOnMouseClicked(event -> {
				
			});

			//load server
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//load order by table
	public int loadOrderByTable() {
		Preferences preferences = Preferences.userNodeForPackage(getClass());
		TableModel.tableId = preferences.getInt("tabletId", SettingController.tableId);
		System.out.println("home table id "+TableModel.tableId);
		if(TableModel.tableId != 0) { 
			String name = getTableName(TableModel.tableId);
			this.tableName = name;
			String code = this.loadLatestUnpaidOrder();
			if(OrderModel.currentOrderId != 0) {
				this.lblOrderCode.setText("#"+code);
			} else {
				this.lblOrderCode.setText("");
			}
		} else {
			this.lblTableName.setText("");
			this.lblOrderCode.setText("");
		}
		this.tableId = TableModel.tableId;
		return this.tableId;

	}
	
	//load latest unpaid order - get updated data -> add to updated list, draw updated list -> render
	public String loadLatestUnpaidOrder() {
		System.out.println("vorrr r ne");
		try {
			ArrayList<CompareOperator> unpaidOrder =  new ArrayList<CompareOperator>();
			unpaidOrder.add(CompareOperator.getInstance("orders.table_id", "=", String.valueOf(TableModel.tableId)));//TableModel.tableId
			unpaidOrder.add(CompareOperator.getInstance("orders.status", "!=", String.valueOf(OrderModel.COMPLETED)));
			String orderBys = "order_details.id desc";
			this.renderUpdatedOrderList(unpaidOrder, orderBys);
			System.out.println(">>> OrderId:"+OrderModel.currentOrderId);
			ResultSet code = this.orderModel.getOrderById(OrderModel.currentOrderId);
			while(code.next()) {
				this.orderCode = code.getString("code");
			}
			return this.orderCode;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//get table name
	public String getTableName(int id) {
		try {
			ResultSet table = this.tableModel.getTableById(id);
			while(table.next()) {
				lblTableName.setText(table.getString("name"));
				serverId = table.getInt("user_id");
			}
			return this.tableName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
		setButtonStyle();
		btnAll.getStyleClass().add("btnHomeFocused");
		btnAll.getStyleClass().add("btnAllFocused");

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
		this.mainCategorySelected = CustomerHomeController.APPETIZERS;
		this.categorySelected = "";
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		setButtonStyle();
		btnAppetizer.getStyleClass().add("btnHomeFocused");
		btnAppetizer.getStyleClass().add("btnAppetizerFocused");

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
		this.mainCategorySelected = CustomerHomeController.SIDE_ORDERS;
		this.categorySelected = "";
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		setButtonStyle();
		btnSideOrder.getStyleClass().add("btnHomeFocused");
		btnSideOrder.getStyleClass().add("btnSideOrderFocused");

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
		this.mainCategorySelected = CustomerHomeController.A_LA_CARTE;
		this.categorySelected = "";
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		setButtonStyle();
		btnALaCarte.getStyleClass().add("btnHomeFocused");
		btnALaCarte.getStyleClass().add("btnALaCarteFocused");

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
		setButtonStyle();
		btnDessert.getStyleClass().add("btnHomeFocused");
		btnDessert.getStyleClass().add("btnDessertFocused");

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
		setButtonStyle();
		btnBeverage.getStyleClass().add("btnHomeFocused");
		btnBeverage.getStyleClass().add("btnBeverageFocused");

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
		setButtonStyle();

		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//load master
	public MasterController loadMaster(MasterController masterController) {
		this.masterController = masterController;
		return this.masterController;
	}
	
	//btnsetting
	public void btnSettingAction() { 
		setButtonStyle();
		try {
			AnchorPane customerHolder;
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/login-exit.fxml")); 
			customerHolder = root.load();
			
			//controller
			CustomerOptionController controller = root.getController();
			controller.showForm(this);
			
			
			Scene scene = new Scene(customerHolder, 307, 349);
			Stage stage = new Stage();
			stage.setX((screenBounds.getWidth() - 307)/2);
			stage.setY((screenBounds.getHeight() - 349)/2);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene);
			stage.show();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnnoti
	@FXML
	public void btnNotiAction() {
		setButtonStyle();
	}
	
	//btnserver
	@FXML
	public void btnServerAction() {
		setButtonStyle();
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
			servingGridPane.getStylesheets().add(getClass().getResource("/css/home-component.css").toExternalForm());
			
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
			servingPrice.setFont(Font.font("System Bold", 12));
			servingPrice.setLayoutX(34);
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
				addItem.getStyleClass().add("btnAdd");
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
				servingPrice.setText(Helpers.formatNumber(null).format(price)+" vnd");
				int qty = servings.getInt("quantity");
				servingStock.setText(qty + " item(s) in stock");
				//add
				//onclick to add				 
				addItem.setOnMouseClicked(event -> {
					//draw custom serving
					this.servingId = id;
					this.customServingLayout(id);
				});
				servingPane.getChildren().addAll(prodId, servingImage, servingName, servingPrice, servingStock, addItem);
				this.servingGridPane.add(servingPane, x, y);
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
	
	public int totalServingQuantity(int id) {
		try {
			ArrayList<CompareOperator> condition  = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));//servingid
			ResultSet qties = this.servingAttributeModel.getStock(condition);
			int qty = 0;
			while(qties.next()) {
				qty = qties.getInt("qty");
			}
			return qty;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
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
		anchorPane = new AnchorPane();
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
			
			lblCreatedAt.setLayoutX(16);
			lblCreatedAt.setPrefWidth(100);
			lblCreatedAt.setLayoutY(87);
			lblCreatedAt.setTextFill(Color.WHITE);
			lblCreatedAt.setFont(Font.font("System Italic", 9));
			
			lblTotalPrice.setLayoutX(223);
			lblTotalPrice.setLayoutY(19);
			lblTotalPrice.setTextFill(Color.web("#ea7c69"));
			lblTotalPrice.setFont(Font.font("System Bold", 14));

			lblAttribute = new Label();
			lblAttribute.setLayoutX(83);
			lblAttribute.setPrefWidth(140);
			lblAttribute.setLayoutY(28);
			lblAttribute.setFont(Font.font("System Bold", 9));
			lblAttribute.setTextFill(Color.web("#dfe5e7c2"));

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
			
			//Pane
			Pane vboxPane = new Pane();
			vboxPane.setPrefSize(275, 100);
			
			if(CustomerHomeController.isCreated) {
				vboxPane.getChildren().addAll(ivImageView, lblServingName, lblItemPrice, lblTotalPrice, 
												   tfNote, tfQuantity, btnDelete, lblAttribute);
			} else {
				CustomerHomeController.isCreated = false;
				tfNote.setDisable(true);
				tfQuantity.setDisable(true);
				//set icon status
				ObservableList<Node> nodes = FXCollections.observableArrayList(ivImageView, lblServingName, lblItemPrice, lblTotalPrice, 
						tfNote, tfQuantity, statusPane, lblCreatedAt, lblAttribute);
				switch(detail.getServingStatus()) {
					case OrderDetailModel.PENDING:
						ivServingStatus.setImage(new Image(getClass().getResource("/assets/sandclock.png").toExternalForm()));
						statusPane.setStyle("-fx-background-color: #F23E5C !important; -fx-background-radius: 5px");
						tfNote.setDisable(false);
						tfQuantity.setDisable(false);
						vboxPane.getChildren().addAll(ivImageView, lblServingName, lblItemPrice, lblTotalPrice, 
								   tfNote, tfQuantity, btnDelete, lblAttribute);
						break;
					case OrderDetailModel.COOKING:
						ivServingStatus.setImage(new Image(getClass().getResource("/assets/cooking.png").toExternalForm()));
						statusPane.setStyle("-fx-background-color: #ff2351 !important; -fx-background-radius: 5px");
						vboxPane.getChildren().addAll(nodes);
						break;
					case OrderDetailModel.READY:
						ivServingStatus.setImage(new Image(getClass().getResource("/assets/ready.png").toExternalForm()));
						statusPane.setStyle("-fx-background-color: #34a853 !important; -fx-background-radius: 5px");
						vboxPane.getChildren().addAll(nodes);
						break;
					case OrderDetailModel.SERVING:
						ivServingStatus.setImage(new Image(getClass().getResource("/assets/serving.png").toExternalForm()));
						statusPane.setStyle("-fx-background-color: #6F71C2 !important; -fx-background-radius: 5px");
						vboxPane.getChildren().addAll(nodes);
						break;
					case OrderDetailModel.SERVED:
						ivServingStatus.setImage(new Image(getClass().getResource("/assets/served.png").toExternalForm()));
						statusPane.setStyle("-fx-background-color: #F07613 !important; -fx-background-radius: 5px");
						vboxPane.getChildren().addAll(nodes);
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
		String sugar = "", ice = "";
		if(!item.getSugar().isEmpty() && !item.getIce().isEmpty()) {
			sugar = "S:"+item.getSugar();
			ice = "I:"+ item.getIce();
		}
		lblAttribute.setText(item.getSize() + " " + sugar + " " + ice);
		lblServingName.setText(item.getServingName());
		lblItemPrice.setText(Helpers.formatNumber(null).format(item.getItemPrice()));
		lblTotalPrice.setText(Helpers.formatNumber(null).format(item.getTotalPrice()));
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
			this.btnPayAction();
		});
		//pane
		this.titlePaneSPOrderLayout();
		orderListPane.getChildren().addAll(titlePane, spOrderList, btnPay);
	}
	
	//set content of vboxchildpane - update
	public void renderVBoxPaneWithSetUpdatedContent(OrderDetailModel item) {
		CustomerHomeController.isCreated = false;
		this.vboxLayout(null, item);
		String sugar = "S:"+item.getSugar();
		String ice = "I:"+ item.getIce();
		if(item.getSugar() == null && item.getIce() == null) {
			sugar = ""; ice = "";
		}
		lblAttribute.setText(item.getSize() + " " + sugar + " " + ice);				
		lblCreatedAt.setText(item.getCreatedAt());
		lblServingName.setText(item.getServingName());
		lblItemPrice.setText(Helpers.formatNumber(null).format(item.getPrice()));
		lblTotalPrice.setText(Helpers.formatNumber(null).format(item.getTotal()));
		tfNote.setText(item.getServingNote());
		tfQuantity.setText(String.valueOf(item.getQuantity()));
		ivImageView.setImage(new Image(CustomerHomeController.class.getResourceAsStream(item.getThumbnail())));
		this.lblPayTotal.setText(Helpers.formatNumber(null).format(this.totalPlace)+"vnd");
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
			this.totalPlace = total;
			lblPlaceTotal.setText(Helpers.formatNumber(null).format(total)+"vnd");
		} else {
			this.emptyOrderList();
		}
	}
	
	//load updated 
	public ArrayList<CompareOperator> loadUpdatedData() {
		ArrayList<CompareOperator> updated = new ArrayList<CompareOperator>();
		updated.add(CompareOperator.getInstance("orders.table_id", "=", String.valueOf(TableModel.tableId)));
		updated.add(CompareOperator.getInstance("orders.id", "=", String.valueOf(OrderModel.currentOrderId)));
		return updated;
	}
	
	//render update order list
	public int getUpdated(ArrayList<CompareOperator> updated, String orderBys) {
		try {
			int orderId = 0;
			ResultSet orderDetails = this.orderDetailModel.getOrderDetailList(updated, orderBys);
			while(orderDetails.next()) {
				this.orderCode = orderDetails.getString("orders.code");
				this.totalPlace = orderDetails.getDouble("orders.total_amount");
				orderId = orderDetails.getInt("orders.id");
				System.out.println("kkk "+this.orderCode);

				updatedList.add(new OrderDetailModel(
						orderDetails.getInt("order_details.serving_id"),
						orderDetails.getString("servings.thumbnail"),
						orderDetails.getString("servings.name"),
						orderDetails.getDouble("order_details.price"),
						orderDetails.getDouble("order_details.total"),
						orderDetails.getString("order_details.serving_note"),
						orderDetails.getInt("order_details.quantity"),
						orderDetails.getInt("order_details.serving_status"),
						orderDetails.getString("order_details.created_at"),
						orderDetails.getString("order_details.ice") != null ? orderDetails.getString("order_details.ice") : null,
						orderDetails.getString("order_details.sugar") != null ? orderDetails.getString("order_details.sugar") : null,
						orderDetails.getString("order_details.size")
				));
			}
			this.lblOrderCode.setText("#"+this.orderCode);
			System.out.println(">>>after updated: "+this.totalPlace);
			return orderId;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//render update
	public int renderUpdatedOrderList(ArrayList<CompareOperator> updated, String orderBys) {
		try {
			//get order detail from db
			OrderModel.currentOrderId = this.getUpdated(updated, orderBys);
			System.out.println("mmm "+updatedList.size());
			if(updatedList.size() > 0) {
				this.updateOrderListLayout();
				vboxOrderList.getChildren().clear();
				for(OrderDetailModel item : updatedList) {
					
					this.renderVBoxPaneWithSetUpdatedContent(item);//servingname, thumbnail, quantity, total, serving note, 											//sevingstatus, createdat, servingid, price
				}
				CustomerHomeController.updatedList.clear();
			} else {
				this.emptyOrderList();
				this.lblOrderCode.setText("");
			}	
			return OrderModel.currentOrderId;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	//delete from order list
	public void deleteItemOrder(OrderListModel item) {
		System.out.println(createdList.indexOf(item));
		createdList.remove(item);
		this.addItemToOrderList();
	}
				
	//click btn place
	public int btnPlaceAction() {
		try {
			ArrayList<DataMapping> orderData = new ArrayList<DataMapping>();
			orderData.add(DataMapping.getInstance("code", "OC"+Helpers.randomString(6)));
			orderData.add(DataMapping.getInstance("table_id", String.valueOf(TableModel.tableId)));
			orderData.add(DataMapping.getInstance("user_id", String.valueOf(serverId)));

			orderData.add(DataMapping.getInstance("total_amount", String.valueOf(Math.round(this.totalPlace))));
			OrderModel.currentOrderId = this.orderModel.createOrder(orderData);
			this.totalPlace = 0;
			int result = this.insertOrderDetails();
			if(result != 0) {
				createdList.clear();
				this.emptyOrderList();
			}
			CustomerHomeController.isCreated = false;
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("orders.id", "=", String.valueOf( OrderModel.currentOrderId)));
			this.renderUpdatedOrderList(condition, null);
			HandleNotifications.getInstance().sendMessage("SERVER#SERVER_NEW_ORDER#"+tableId+"#New order received!#"+serverId);
			System.out.println("SERVER#SERVER_NEW_ORDER#"+tableId+"#New order received!#"+serverId);
			return OrderModel.currentOrderId;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//order detail
	public int insertOrderDetails() {
		try {
			int result = 0; 
			ArrayList<DataMapping> orderDetailData = new ArrayList<DataMapping>();
			for(OrderListModel item : createdList) {
				orderDetailData.add(DataMapping.getInstance("order_id", String.valueOf(OrderModel.currentOrderId)));
				orderDetailData.add(DataMapping.getInstance("serving_id", String.valueOf(item.getServingId())));
				orderDetailData.add(DataMapping.getInstance("serving_note", item.getNote().isEmpty() ? null : item.getNote()));
				orderDetailData.add(DataMapping.getInstance("serving_status", String.valueOf(OrderDetailModel.READY)));
				orderDetailData.add(DataMapping.getInstance("size", item.getSize()));
				orderDetailData.add(DataMapping.getInstance("sugar", item.getSugar() != "" ? item.getSugar() : null));
				orderDetailData.add(DataMapping.getInstance("ice", item.getIce() != "" ? item.getIce() : null));
				orderDetailData.add(DataMapping.getInstance("quantity", String.valueOf(item.getQuantity())));
				orderDetailData.add(DataMapping.getInstance("price", String.valueOf(item.getItemPrice())));
				orderDetailData.add(DataMapping.getInstance("total", String.valueOf(item.getTotalPrice())));
				this.totalPlace+=item.getTotalPrice();
				result = this.orderDetailModel.createOrderDetail(orderDetailData);
				System.out.println("SUM: "+this.totalPlace);
				this.updateOrderTotal(item);
				orderDetailData.clear();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update order total
	public void updateOrderTotal(OrderListModel item) {
		ArrayList<DataMapping> updateTotalOrder = new ArrayList<DataMapping>();
		updateTotalOrder.add(DataMapping.getInstance("total_amount", String.valueOf(this.totalPlace)));
		this.orderModel.updateOrder(OrderModel.currentOrderId, updateTotalOrder);
	}

	//load data from orderdetail by order id
//	public void btnReloadUpdatedAction(){
//		try {
//			//has order id
//			if(OrderModel.currentOrderId != 0) {
//				this.renderUpdatedOrderList(null, null);
//			} else {
//				this.btnReloadUpdated.setDisable(true);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
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
	
	//btnPayAction
	public void btnPayAction() {
		try {
			if(OrderModel.currentOrderId != 0) {
				FlowPane requestPayment;
				Rectangle2D screenBounds = Screen.getPrimary().getBounds();
				FXMLLoader root = new FXMLLoader(getClass().getResource("/views/request-payment.fxml"));
				requestPayment = root.load();

				//controller
				CustomerPaymentController controller = root.getController();
				controller.loadDataToRequestPayment(this);
				
				Scene scene = new Scene(requestPayment, 600, 400);
				Stage stage = new Stage();
				stage.setX((screenBounds.getWidth() - 600)/2);
				stage.setY((screenBounds.getHeight() - 400)/2);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.setScene(scene);
				stage.show();
			}
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
	
	
	public void handleMessage() {
		try {
			AnchorPane apNotiAlert;
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/notification-alert.fxml"));
			apNotiAlert = root.load();

			//controller
			NotificationController controller = root.getController();
			controller.initialize(null, null);
			
			Scene scene = new Scene(apNotiAlert, 340, 460);
			Stage stage = new Stage();
			stage.initStyle(StageStyle.TRANSPARENT);
			scene.setFill(Color.TRANSPARENT);
			stage.setX((screenBounds.getWidth() - 340)/2);
			stage.setY((screenBounds.getHeight() - 460)/2);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setButtonStyle() {
		//========= btn home=========
	    btnAll.getStyleClass().remove("btnHomeFocused");
	    btnAll.getStyleClass().remove("btnAllFocused");
	    
	    btnAppetizer.getStyleClass().remove("btnHomeFocused");
	    btnAppetizer.getStyleClass().remove("btnAppetizerFocused");
	    
	    btnSideOrder.getStyleClass().remove("btnHomeFocused");
	    btnSideOrder.getStyleClass().remove("btnSideOrderFocused");
	    
	   	btnALaCarte.getStyleClass().remove("btnHomeFocused");
	   	btnALaCarte.getStyleClass().remove("btnALaCarteFocused");
	   	
	   	btnDessert.getStyleClass().remove("btnHomeFocused");
	   	btnDessert.getStyleClass().remove("btnDessertFocused");
        
        btnBeverage.getStyleClass().remove("btnHomeFocused");
        btnBeverage.getStyleClass().remove("btnBeverageFocused");
        
        //========== btn other==========
        btnOrder.getStyleClass().remove("btnOtherFocused");
        btnOrder.getStyleClass().remove("btnOrderFocused");
        
        btnHelp.getStyleClass().remove("btnOtherFocused");
        btnHelp.getStyleClass().remove("btnHelpFocused");
        
        btnSetting.getStyleClass().remove("btnOtherFocused");
        btnSetting.getStyleClass().remove("btnSettingFocused");
        
        btnNoti.getStyleClass().remove("btnOtherFocused");
        btnNoti.getStyleClass().remove("btnNotiFocused");
        
        btnServer.getStyleClass().remove("btnOtherFocused");
        btnServer.getStyleClass().remove("btnServerFocused");
   }

}