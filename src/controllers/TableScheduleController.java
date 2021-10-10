package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.MySQLJDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;

import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.ItemModel;
import models.ItemSupportModel;
import models.ReservationModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

public class TableScheduleController implements Initializable {
   private static ReservationModel reserModel = new ReservationModel();
    @FXML
    private GridPane grid;
    @FXML
    private VBox vbox;

    @FXML
    private DatePicker dpDate;

   
    @FXML
    private Pane paneStatus;

    private List<ItemSupportModel> item = new ArrayList<>();
    
    public static ResultSet getDataReser() throws SQLException{
    	String sql = "select id, name from tables" ;
       Statement stmt = MySQLJDBC.connection.createStatement();
		return stmt.executeQuery(sql);
       
	//
      
    }
    private String getTime(int id,LocalDate date) throws SQLException{
    	String time = "";
    	int length =0;
    	String sql = "select tables.name, start_time, end_time from tables_reservation t join reservations r on reservation_id=r.id join tables on table_id=tables.id where r.date_pick ='"+date+"' and t.status = 1 and table_id="+id+" order by start_time asc";
    	Statement st = MySQLJDBC.connection.createStatement();
    	ResultSet rs = st.executeQuery(sql);
    	while(rs.next()) { 
    		time+="("+Helpers.formatTime(rs.getString("start_time"))+"-"+Helpers.formatTime(rs.getString("end_time"))+") ";
    		length+=time.length();
    		if(length>=27) {
    			time+="\n";
    		}
    		
    	}
    	System.out.println(time);
    	return time;
    	
    }
    private List<ItemSupportModel> getData() throws SQLException{
    	List<ItemSupportModel> i = new ArrayList<>();
    	ItemSupportModel items;

    	ResultSet rs = getDataReser();
    	while(rs.next()){
    		
    		items = new ItemSupportModel();
    		items.setTableName(rs.getString("name"));
    		items.setTime(getTime(rs.getInt("id"),dpDate.getValue()));
    		i.add(items);
    		
    	}
    	return i;
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
    	dpDate.setValue(LocalDate.now());
    	this.dateChoose();
	}
    //load data
    
   private void loadData(LocalDate date,String status) {

	   grid.getChildren().clear();
	   item.removeAll(item);
	   try {
			item.addAll(getData());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	   	int col = 0;
	   	int row =0;
	   	try {
	   		System.out.println("size: "+item.size());
	   		if(item.size()==0) {
	   			FXMLLoader root = new FXMLLoader();
	   	   		root.setLocation(getClass().getResource("/views/schedule-support.fxml"));
	   	   		AnchorPane anchor = root.load();
	   	   		paneStatus.getChildren().setAll(anchor);
	   		}else {
	   			paneStatus.getChildren().clear();
	   			paneStatus.getChildren().setAll(vbox);
	   		}
	   	for(int i =0;i<item.size(); i++) {
   		FXMLLoader root = new FXMLLoader();
   		root.setLocation(getClass().getResource("/views/item-support.fxml"));
   		AnchorPane anchor = root.load();
   		ItemSupportController itemControl = root.getController();
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
    void dateChoose() {
    	
    	loadData(dpDate.getValue(),"");
    
    	
    }

  

}
