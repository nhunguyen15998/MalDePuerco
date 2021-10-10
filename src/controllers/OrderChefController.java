package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import db.MySQLJDBC;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class OrderChefController implements Initializable {

	@FXML
    private ListView<String> listView;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Connection conDB = MySQLJDBC.Instance().getConn();
		
		try {
			Statement stm = MySQLJDBC.connection.createStatement();
			String query = "select no, (select code from orders where order_id = orders.id) as orderID, (select name from servings where serving_id = servings.id) as servingID, quantity from order_details";
			
			ResultSet queryOutput = stm.executeQuery(query);
			while(queryOutput.next()) {
				int no = queryOutput.getRow();
				String code = queryOutput.getString( "orderID");
				String name = queryOutput.getString( "servingID");
				int quantity = queryOutput.getInt( "quantity");
				String listOut = code + " \"" + name + " \"" ;
				
				listView.getItems().add(listOut);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
