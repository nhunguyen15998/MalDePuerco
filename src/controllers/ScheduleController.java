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
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ItemModel;
import models.ReservationModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;

public class ScheduleController implements Initializable {
   private static ReservationModel reserModel = new ReservationModel();
    @FXML
    private GridPane grid;
    @FXML
    private VBox vbox;

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
    private Pane paneStatus;
    @FXML
    private Label lblExpired;

    @FXML
    private Label lblCancel;
    public static int inReser=0;
    
    private int reserId;
    private List<ItemModel> item = new ArrayList<>();
    
    public static ResultSet getDataReser(String condition) throws SQLException{
    	String sql = "select *from reservations "+condition ;
       Statement stmt = MySQLJDBC.connection.createStatement();
		return stmt.executeQuery(sql);
       
	//
      
    }
    private String getTableName(int id) throws SQLException{
    	String tablename = "";
    	int count = 0;
    	String sql = "select tables.name from tables_reservation join tables on table_id=tables.id join reservations on reservation_id=reservations.id where reservation_id="+id;
    	Statement st = MySQLJDBC.connection.createStatement();
    	ResultSet rs = st.executeQuery(sql);
    	while(rs.next()) {
    		tablename+=rs.getString("tables.name")+" ";
    		count++;
    		if(count%2==0) {
    			tablename+="\n";
    		}
    	}
    	return tablename;
    	
    }
    private List<ItemModel> getData(LocalDate date, String status) throws SQLException{
    	List<ItemModel> i = new ArrayList<>();
    	ItemModel items;

    	String value =" where date_pick ='"+date+"' order by start_time asc";
    	if(!status.equals("")) {
    	 value="where date_pick ='"+date+"' and reservations.status="+status+" order by start_time asc";
    	}
    	ResultSet rs = getDataReser(value);
    	int k =0;
    	while(rs.next()){
    		items = new ItemModel();
    		items.setId(rs.getInt("id"));
    		items.setStart(Helpers.formatTime(rs.getString("start_time")));
    		items.setEnd(Helpers.formatTime(rs.getString("end_time")));
    		items.setDate(rs.getString("date_pick"));
    		items.setTable(getTableName(rs.getInt("id")));
    		items.setCusName(rs.getString("customer_name"));
    		items.setStatus(rs.getInt("status"));
    		i.add(items);
    		k++;
    		
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
			item.addAll(getData( date,status));
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
   		root.setLocation(getClass().getResource("/views/item.fxml"));
   		AnchorPane anchor = root.load();
   		ItemController itemControl = root.getController();
   		itemControl.setData(item.get(i));
   		
   		int id = item.get(i).getId();
   		int stt = item.get(i).getStatus();
   		ContextMenu contextMenu = new ContextMenu();

        MenuItem item = new MenuItem("Update this reservation");
        item.setOnAction(eh->{
       	 btnUpdateAction();
        });
        contextMenu.getItems().addAll(item);
   		anchor.setOnMouseClicked(eh->{
   			if(inReser==1&&(stt==1||stt==2||stt==3)) {
   			getIdOnRightClick(eh, id);
   			}
   		});
   		anchor.setOnContextMenuRequested(eh->{
   			if(inReser==1&&(stt==1||stt==2||stt==3)) {
   			contextMenu.show(anchor, eh.getScreenX(), eh.getScreenY());
   			}
   		});
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
   
   private void getIdOnRightClick(MouseEvent event, int id) {
	   switch(event.getButton()) {
	   case PRIMARY:
		   System.out.println("left click");
		   
		   break;
	   case SECONDARY:
		   reserId=id;
		   System.out.println("reserId: "+reserId);
		   break;
	   default:
		 break;
	   }
   }
   
   void btnUpdateAction() {
   	try {
			if(this.reserId != 0) {
					this.shoUpdateForm();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
   }
	public void shoUpdateForm() {
  		try {
  			
  			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/reservation-cu.fxml"));
  			AnchorPane createHolder =  new AnchorPane();
  			createHolder = root.load();
  			
  			//controller
  			ReservationCUController controller = root.<ReservationCUController>getController();
  			ReservationController ctr = new ReservationController();
  			ctr.setReserId(reserId);
  			controller.loadDataUpdateById(ctr);
  			Scene scene = new Scene(createHolder, 680, 532);
  			Stage createStage = new Stage();
  			createStage.initStyle(StageStyle.UNDECORATED);
  			createStage.setScene(scene);
  			createStage.show();			
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
    @FXML
    void dateChoose() {
    	
    	loadData(dpDate.getValue(),"");
    
    	
    }

    @FXML
    void tabAll() {
    	this.dateChoose();
    
    }

    @FXML
    void tabCancel(MouseEvent event) {
    	loadData(dpDate.getValue(), lblCancel.getId());
    	
    	
    }

    @FXML
    void tabConfirm(MouseEvent event) {
    	loadData(dpDate.getValue(), lblConfirm.getId());
    }

    @FXML
    void tabDepo(MouseEvent event) {
    	loadData(dpDate.getValue(), lblDepo.getId());
    }

    @FXML
    void tabExpired(MouseEvent event) {
    	loadData(dpDate.getValue(), lblExpired.getId());
    }

    @FXML
    void tabNew(MouseEvent event) {
    	loadData(dpDate.getValue(), lblNew.getId());
    }

    @FXML
    void tabPresent(MouseEvent event) {
    	loadData(dpDate.getValue(), lblPresent.getId());
    }
	public static int getInReser() {
		return inReser;
	}
	public static void setInReser(int inReser) {
		ScheduleController.inReser = inReser;
	}
	public int getReserId() {
		return reserId;
	}
	public void setReserId(int reserId) {
		this.reserId = reserId;
	}

	

}
