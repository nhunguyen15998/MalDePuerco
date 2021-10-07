package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.MySQLJDBC;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;

import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import models.ItemModel;
import models.ReservationModel;
import models.ScheduleModel;
import utils.Helpers;

public class ScheduleController implements Initializable {
   private ReservationModel reserModel = new ReservationModel();
	private String date;
    @FXML
    private GridPane grid;

    @FXML
    private DatePicker dpDate;

    @FXML
    private Label all;

    @FXML
    private Label lblNew;

    @FXML
    private Label lblConfirm;

    @FXML
    private Label lblDepo;

    @FXML
    private Label lblPresent;

    @FXML
    private Label lblExpired;

    @FXML
    private Label tabCancel;
    private List<ItemModel> item = new ArrayList<>();
    
    
    private List<ItemModel> getData() throws SQLException{
    	List<ItemModel> i = new ArrayList<>();
    	ItemModel items;
    	
    	ResultSet rs = reserModel.getReserList(null);
    	
    	int k =0;
    	while(rs.next()){
    		
    		items = new ItemModel();
    		items.setStart(Helpers.formatTime(rs.getString("start_time")));
    		items.setEnd(Helpers.formatTime(rs.getString("end_time")));
    		items.setDate(rs.getString("date_pick"));
    		items.setCusName(rs.getString("customer_name"));
    		items.setStatus(rs.getInt("status"));
    		i.add(items);
    		k++;
    		System.out.println(k);
    		
    	}
    	return i;
    }
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
    	try {
			item.addAll(getData());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	int col = 0;
    	int row =0;
    	try {
    	for(int i =0;i<item.size(); i++) {
    		FXMLLoader root = new FXMLLoader();
    		root.setLocation(getClass().getResource("/views/item.fxml"));
    		AnchorPane anchor = root.load();
    		ItemController itemControl = root.getController();
    		itemControl.setData(item.get(i));
    		if(col==1) {
    			col=0;
    			row++;
    		}
    		grid.add(anchor, col, row++);
    		GridPane.setMargin(anchor, new Insets(0,0,2,0));
    		
    	}
    	}catch (Exception e) {
			// TODO: handle exception
		}
	}
    
    @FXML
    void dateChoose(ActionEvent event) {

    }

    @FXML
    void tabAll(MouseEvent event) {

    }

    @FXML
    void tabCancel(MouseEvent event) {

    }

    @FXML
    void tabConfirm(MouseEvent event) {

    }

    @FXML
    void tabDepo(MouseEvent event) {

    }

    @FXML
    void tabExpired(MouseEvent event) {

    }

    @FXML
    void tabNew(MouseEvent event) {

    }

    @FXML
    void tabPresent(MouseEvent event) {

    }

    private void handleLabel(MouseEvent event) { 
        Label label = (Label) event.getSource();
        String labelText = label.getText();
        System.out.println("Mouse click on label: "+labelText);
    }


	
	

}
