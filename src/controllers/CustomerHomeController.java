package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;

public class CustomerHomeController implements Initializable {
	public static boolean isSelected = false;
	private int btnId;
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
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			//empty order list
			emptyOrderList = FXMLLoader.load(getClass().getResource("/views/emptycart.fxml"));	
			orderListPane.getChildren().setAll(emptyOrderList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//sidebarItemPressed
	public void sidebarItemPressed() {
		try {
//			if(CustomerHomeController.isSelected != true) {
//				CustomerHomeController.isSelected = true;
//				btnAll.setStyle("-fx-background-color: #EA7C69 !important;\r\n"
//						+ "	-fx-background-radius: 10px !important;\r\n"
//						+ "	-fx-border-color:  #EA7C69;\r\n"
//						+ "	-fx-border-radius: 10px;\r\n"
//						+ "	-fx-text-fill: #fff;\r\n"
//						+ "	-fx-background-size: 20px 20px; \r\n"
//						+ " -fx-background-repeat: no-repeat; \r\n"
//						+ " -fx-background-position: 25px 10px; "
//						+ " -fx-background-image: url(\"../assets/menu.png\");");
//			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
