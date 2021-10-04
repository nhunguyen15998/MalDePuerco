package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import models.PermissionModel;
import models.ServingCategoryModel;
import models.ServingModel;
import utils.CompareOperator;

public class CustomerHomeController implements Initializable {
	private ServingCategoryModel servingCategoryModel = new ServingCategoryModel();
	private ServingModel servingModel = new ServingModel();
	
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
	private Button btnPlace;
	
	//pane
	@FXML
	private FlowPane customerMasterHolder;
	@FXML
	private AnchorPane orderListPane;
	@FXML
	private AnchorPane emptyOrderList;
	
	//hbox
	@FXML
	private HBox categoryHBox;
//	@FXML
//	private Pane categoryPane;
//	@FXML
//	private Pane hBoxChild;
//	@FXML
//	private ImageView categoryImage;
//	@FXML
//	private Text categoryName;
	
	//grid
	@FXML
	private GridPane servingGridPane;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			//empty order list
			emptyOrderList = FXMLLoader.load(getClass().getResource("/views/emptycart.fxml"));	
			orderListPane.getChildren().setAll(emptyOrderList);
			this.btnAllAction();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	//sidebar btn
	//btnall
	public void btnAllAction() {
		this.categoryHBox.getChildren().clear();
		this.servingGridPane.getChildren().clear();
		try {
			//category
			ArrayList<CompareOperator> subCategoryCondition = new ArrayList<CompareOperator>();
			subCategoryCondition.add(CompareOperator.getInstance("serving_categories.parent_id", "!=", String.valueOf(0)));
			ResultSet subCategories = this.servingCategoryModel.getServingCategoryList(subCategoryCondition);
			while(subCategories.next()) {
				//hbox
				Pane categoryPane = new Pane();
				Pane hBoxChild = new Pane();
				ImageView categoryImage = new ImageView();
				Text categoryName = new Text();
				this.hboxLayout(categoryPane, hBoxChild, categoryImage, categoryName);
				//set
				categoryName.setText(subCategories.getString("name"));
				Image image = new Image(subCategories.getString("thumbnail"));
				categoryImage.setImage(image);
				System.out.println(categoryName);
				//add
				categoryPane.getChildren().addAll(categoryName, categoryImage);
				hBoxChild.getChildren().addAll(categoryPane);
				this.categoryHBox.getChildren().addAll(hBoxChild);		
			}
			
			//serving
			ResultSet servings = this.servingModel.getServingList(null);
			int x = 0;
			int y = 0;
			int count = 0;
			while(servings.next()) {
				//grid
				Pane servingPane = new Pane();
				ImageView servingImage = new ImageView();
				Text servingName = new Text();
				Label servingPrice = new Label();
				Text servingStock = new Text();
				this.gridPaneLayout(servingPane, servingImage, servingName, servingPrice, servingStock);
				//set
				servingName.setText(servings.getString("name"));
				Image image = new Image(servings.getString("thumbnail"));
				servingImage.setImage(image);
				servingPrice.setText("$"+servings.getString("price"));
				servingStock.setText(servings.getString("quantity")+" bowls in stock");
				System.out.println(servingName);
				//add
				servingPane.getChildren().addAll(servingImage, servingName, servingPrice, servingStock);
				this.servingGridPane.add(servingPane, x, y);//0,0 1,0 2,0 3,0
															//1,0 1,1 2,1 3,1
															//2,0 1,2 2,2 3,2
				count++;
				x++;
				if(count%5 == 0) {
					y++;
				}

				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//btnappetizer
	public void btnAppetizerAction() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//btnsideorder
	public void btnSideOrderAction() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnalacarte
	public void btnALaCarteAction() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btndessert
	public void btnDessertAction() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//btnbeverage
	public void btnBeverageAction() {
		try {
			
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
	
	

	
	
}
