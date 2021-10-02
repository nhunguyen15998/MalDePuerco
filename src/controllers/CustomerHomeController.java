package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;

public class CustomerHomeController implements Initializable{

	
	//btn
	@FXML
	private Button btnAll;
	@FXML
	private Button btnDessert;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	//sidebarItemPressed
	public void sidebarItemPressed() {
		try {
			btnAll.setStyle("-fx-background-color:  #ff2351 !important;\r\n"
					+ "	-fx-background-radius: 10px !important;\r\n"
					+ "	-fx-border-color:  #ff2351;\r\n"
					+ "	-fx-border-radius: 10px;\r\n"
					+ "	-fx-text-fill: #fff;\r\n"
					+ "	-fx-background-size: 20px 20px; \r\n"
					+ " -fx-background-repeat: no-repeat; \r\n"
					+ " -fx-background-position: 25px 10px; "
					+ " -fx-background-image: url(\"../assets/menu.png\");");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
