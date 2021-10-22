package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import db.MySQLJDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ChefItemModel;
import models.OrderDetailModel;
import utils.Helpers;


public class OrderChefController implements Initializable {
	private static OrderDetailModel odetailModel = new OrderDetailModel();
	public MasterController masterController;
	private ChefItemController chefItem = new ChefItemController();
	
	private int chefID;
	@FXML
    private Button btnPen;

    @FXML
    private Button btnCook;

    @FXML
    private Button btnReady;

    @FXML
    private Button btnServing;
    
    @FXML
    private Button btnServed;

    @FXML
    private Button btnCanceled;

    @FXML
    private Pane pane=new Pane();

    @FXML
    private VBox vbox;

    @FXML
    private GridPane grid=new GridPane();
    
    private List<ChefItemModel> item = new ArrayList<>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public static ResultSet getDataOrderDetails(String condition) throws SQLException {
		String sql = "select order_details.id, orders.code as orderCode, servings.name as serName, size,"
				+ " order_details.serving_status, order_details.quantity, time(order_details.created_at) as time, users.name as userName, order_details.serving_note from order_details"
				+ " LEFT JOIN orders on orders.id = order_details.order_id LEFT JOIN servings on servings.id = order_details.serving_id"
				+ " LEFT JOIN users on users.id = order_details.user_id"+condition;
		Statement stm = MySQLJDBC.connection.createStatement();
			return stm.executeQuery(sql);
	}
	
	private List<ChefItemModel> getData(String status) throws SQLException {
		List<ChefItemModel> i = new ArrayList<>();
		ChefItemModel items;
		String value ="";
		if(!status.equals("")) {
		value =" where order_details.serving_status="+status+"";
		}
		
		ResultSet rs = getDataOrderDetails(value);
		int k = 0;
		while(rs.next()) {
			items = new ChefItemModel();
			items.setId(rs.getInt("order_details.id"));
			items.setOderCode(rs.getString("orderCode"));
			items.setServingName(rs.getString("serName"));
			items.setNote(rs.getString("order_details.serving_note"));
			items.setSize(rs.getString("order_details.size"));
			items.setQuantity("x" + rs.getInt("order_details.quantity"));
			items.setCreatedAt(rs.getString("time"));
			items.setUserCode(rs.getString("userName"));
			items.setStatus(rs.getInt("order_details.serving_status"));
			i.add(items);
			
			k++;
		}
		
		return i;
	}
	
	//load data
	public void loadData(String status) {
		grid.getChildren().clear();
		item.removeAll(item);
		try {
			item.addAll(getData(status));
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		int col = 0;
		int row = 0;
		try {
			System.out.println("size: " +item.size());
			if(item.size() == 0) {
				FXMLLoader root = new FXMLLoader();
				root.setLocation(getClass().getResource("/views/orderChef-Support.fxml"));
				AnchorPane anchor = root.load();
				pane.getChildren().setAll(anchor);
			} else {
				pane.getChildren().clear();
				pane.getChildren().setAll(vbox);
			}
			for(int i = 0; i < item.size(); i++) {
				FXMLLoader root = new FXMLLoader();
				root.setLocation(getClass().getResource("/views/chef.fxml"));
				AnchorPane pane = root.load();
				ChefItemController chefItemControl = root.getController();
				chefItemControl.setData(item.get(i));
				int id = item.get(i).getId();
				pane.setOnMouseClicked(eh -> {
					chefItemControl.btnClick(eh, id);
				});
				if(col==1) {
					col=0;
					row++;
				}
				grid.add(pane,  col,  row++);
				GridPane.setMargin(pane, new Insets(0,0,2,0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML void refreshAction(ActionEvent event) {
		this.loadData("");
	}
	
	@FXML void tabPending(MouseEvent event) {
		loadData(btnPen.getId());
	}
	
	@FXML void tabCooking(MouseEvent event) {
		loadData(btnCook.getId());
	}
	
	@FXML void tabReady(MouseEvent event) {
		loadData(btnReady.getId());
	}
	
	@FXML void tabServing(MouseEvent event) {
		loadData(btnServing.getId());
	}
	
	@FXML void tabServed(MouseEvent event) {
		loadData(btnServed.getId());
	}
	
	@FXML void tabCanceled(MouseEvent event) {
		loadData(btnCanceled.getId());
	}
	
	//btnServerViewAction
	@FXML
	public void btnServerViewAction() {
		showServerView();
	}
	
	//show form assign
  	public void showServerView() {
  		try {
  			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/orderWaiter.fxml"));
  			AnchorPane server = root.load();
  			
  			OrderWaiterController controller = root.getController();
			controller.parseMaster(this.masterController);
			this.masterController.masterHolder.getChildren().setAll(server);			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
  	
  	//parse master
  	public MasterController parseMaster(MasterController masterController) {
  		this.masterController = masterController;
  		return this.masterController;
  	}

	public int getChefID() {
		return chefID;
	}

	public void setChefID(int chefID) {
		this.chefID = chefID;
	}
	
	
}

