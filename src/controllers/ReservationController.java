/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;

import db.MySQLJDBC;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AuthenticationModel;
import models.DiscountModel;
import models.ReservationModel;
import models.TableModel;
import models.UserModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;
import utils.JoinCondition;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class ReservationController implements Initializable {
	private ReservationModel res = new ReservationModel();
	private int reserId;
	private String reserName;
    @FXML
    private TableView<ReservationModel> tblReser= new TableView();

    @FXML
    private TableColumn<ReservationModel, Integer> colNo = new TableColumn();

    @FXML
    private TableColumn<ReservationModel, Integer> colID = new TableColumn();

    @FXML
    private TableColumn<ReservationModel, String> colCode = new TableColumn();

    @FXML
    private TableColumn<ReservationModel, String> colCusName  = new TableColumn<ReservationModel, String>();

    @FXML
    private TableColumn<ReservationModel, String> colPhone  = new TableColumn<ReservationModel, String>();

    @FXML
    private TableColumn<ReservationModel, String> colEmail  = new TableColumn<ReservationModel, String>();

    @FXML
    private TableColumn<ReservationModel, Integer> colSeats  = new TableColumn<ReservationModel, Integer>(); 

    @FXML
    private TableColumn<ReservationModel, LocalDate> colDate  = new TableColumn<ReservationModel, LocalDate>();

    @FXML
    private TableColumn<ReservationModel, LocalDate> colStart= new TableColumn<ReservationModel, LocalDate>();

    @FXML
    private TableColumn<ReservationModel, LocalDate> colEnd = new TableColumn<ReservationModel, LocalDate>();

    @FXML
    private TableColumn<ReservationModel, Double> colDeposit= new TableColumn<ReservationModel, Double>();

    @FXML
    private TableColumn<ReservationModel, LocalDate> colCreated = new TableColumn<ReservationModel, LocalDate>();

    @FXML
    private TableColumn<ReservationModel, String> colStatus = new TableColumn<ReservationModel, String>();

    @FXML
    private TextField tfTable = new TextField();

	@FXML
	private AnchorPane createHolder = new AnchorPane();
    @FXML
    private DatePicker dpTo = new DatePicker();

    @FXML
    private DatePicker dpFrom = new DatePicker();

    @FXML
    private RadioButton rdNew = new RadioButton();

    @FXML
    private RadioButton rdConfirm= new RadioButton();

    @FXML
    private RadioButton rdDeposit= new RadioButton();

    @FXML
    private RadioButton rdPresent= new RadioButton();

    @FXML
    private RadioButton rdExpried= new RadioButton();

    @FXML
    private RadioButton rdCancel= new RadioButton();

    @FXML
    private Pane paneDetails= new Pane();

    @FXML
    private Label lblcode1 = new Label();

    @FXML
    private Label lblCode = new Label();

    @FXML
    private Label lblCusName= new Label();

    @FXML
    private Label lblSeats= new Label();

    @FXML
    private Label lblCreated= new Label();
    @FXML
    private Label lblTime= new Label();

    @FXML
    private Label lblPhone= new Label();

    @FXML
    private Label lblEmail= new Label();

    @FXML
    private Label lblDeposit= new Label();


    @FXML
    private Label lblStatus= new Label();
    @FXML
    private Label lblTablePick= new Label();
   
    @FXML
    private Pane paneSchedule = new Pane();
    @FXML
    private Button btnTablesSche = new Button();
    @FXML
    private Button btnReserSche= new Button();
    @FXML
    private Button btnDelete= new Button();
    @FXML
    private Button btnPut= new Button();
    @FXML
    private Button btnCreate= new Button();
    @FXML
    private Button btnUpdate= new Button();
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
  		updateStatus();
  		LocalDate now = LocalDate.now();
    	dpFrom.setValue(now.withDayOfMonth(1));
    	dpTo.setValue(now.withDayOfMonth(now.lengthOfMonth()));
		btnReserSche.setVisible(false);
		btnTablesSche.setVisible(true);
    	this.parseData(null);
		this.loadSchedule("schedule.fxml");
		//create
		btnCreate.setDisable(true);
		//btnPut
		btnPut.setDisable(true);
		//update
		btnUpdate.setDisable(true);
		
		//delete
		btnDelete.setDisable(true);
		

		
		if(AuthenticationModel.hasPermission("CREATE_RESERVATION") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnCreate.setDisable(false);
		}
		
		if(AuthenticationModel.hasPermission("UPDATE_RESERVATION") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnUpdate.setDisable(false);
		}
		
		if(AuthenticationModel.hasPermission("DELETE_RESERVATION") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnDelete.setDisable(false);
		}
    }  
 
    
  //load data
  	public void parseData(ArrayList<CompareOperator> conditions) {
  		paneDetails.setVisible(false);
    	
  		try {
  			//create list, format date

  			ObservableList<ReservationModel> reserList = FXCollections.observableArrayList();
  		//get data from db
  	  		
  			ResultSet r = res.getReserList(conditions);
  			while(r.next()) {
  				reserList.add(new ReservationModel(
  					r.getInt("id"),
  					r.getRow(),
  					r.getDouble("deposit"),
  					r.getInt("status"),
  					r.getInt("seats_pick"),
  					r.getString("code"),
  					r.getString("customer_name"),
  					r.getString("phone"), 
  					r.getString("email"),
  					Helpers.formatTime(r.getString("start_time")),
  					Helpers.formatTime(r.getString("end_time")),
  					r.getDate("date_pick").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
  					r.getString("reservations.created_at"))
  					);
  			}
  			
  			tblReser.setItems(reserList);
  			//cols get from model
  			colID.setCellValueFactory(new PropertyValueFactory<ReservationModel, Integer>("id"));
  			colNo.setCellValueFactory(new PropertyValueFactory<ReservationModel, Integer>("no"));
  			colCode.setCellValueFactory(new PropertyValueFactory<ReservationModel, String>("code"));
  			colCusName.setCellValueFactory(new PropertyValueFactory<ReservationModel, String>("name"));
  			colEmail.setCellValueFactory(new PropertyValueFactory<ReservationModel, String>("email"));
  			colPhone.setCellValueFactory(new PropertyValueFactory<ReservationModel, String>("phone"));
  			colSeats.setCellValueFactory(new PropertyValueFactory<ReservationModel, Integer>("seats"));
  			colDeposit.setCellValueFactory(new PropertyValueFactory<ReservationModel, Double>("deposit"));
  			colCreated.setCellValueFactory(new PropertyValueFactory<ReservationModel, LocalDate>("createdAt"));
  			colDate.setCellValueFactory(new PropertyValueFactory<ReservationModel, LocalDate>("date_pick"));
  			colStart.setCellValueFactory(new PropertyValueFactory<ReservationModel, LocalDate>("start_time"));
  			colEnd.setCellValueFactory(new PropertyValueFactory<ReservationModel, LocalDate>("end_time"));
  			//format col
  			colStatus.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
  					cellData.getValue().getStatus() == ReservationModel.RESER_NEW ? String.valueOf(ReservationModel.isNew) :
  						(cellData.getValue().getStatus() == ReservationModel.RESER_CANCELLED  ? String.valueOf(ReservationModel.isCancelled):
  							(cellData.getValue().getStatus() == ReservationModel.RESER_PRESENT ? String.valueOf(ReservationModel.isPresent ):
  							(cellData.getValue().getStatus() == ReservationModel.RESER_CONFIRMED ? String.valueOf(ReservationModel.isConfirmed ):
  								(cellData.getValue().getStatus() == ReservationModel.RESER_DEPOSITED ? String.valueOf(ReservationModel.isDeposited ):String.valueOf(ReservationModel.isExpried)
  							))))));
  			
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
    
    @FXML
    void btnClearAction() {
    	try {
			tfTable.setText("");
			this.parseData(getFilter());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    @FXML
    public void refresh() {
	 System.out.println("--------------------------");
	 this.updateStatus();
	 this.parseData(null);
	 this.loadSchedule("schedule.fxml");
	 reserId=0;
 }
    @FXML
    public void btnCreateAction() {
    	try {
			this.setReserId(0);
//			if(!UserModel.isShown) {
//				UserModel.isShown = true;
				this.showCreateForm();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
  //show form create
  	public void showCreateForm() {
  		try {
  			
  			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/reservation-cu.fxml"));
  			createHolder = root.load();
  			
  			//controller
  			ReservationCUController controller = root.<ReservationCUController>getController();
  			controller.loadDataUpdateById(this);
  			
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
  	void putMore() {
  			try {
  			if(reserId!=0) {
  			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/reservation-cu.fxml"));
  			createHolder = root.load();
  			
  			//controller
  			ReservationCUController controller = root.<ReservationCUController>getController();
  			controller.loadDataPutMore(this);
  			
  			Scene scene = new Scene(createHolder, 680, 532);
  			Stage createStage = new Stage();
  			createStage.initStyle(StageStyle.UNDECORATED);
  			createStage.setScene(scene);
  			createStage.show();			
  			}else {
  				Helpers.status("error");
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}


	@FXML
    void btnDeleteAction() {
    	try {
			if(this.reserId != 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete Reservation Confirmation");
				alert.setHeaderText("Are you sure you want to delete this item ?");
				
				Optional<ButtonType> options = alert.showAndWait();
				if(options.get() == ButtonType.OK) {
					this.res.deleteReserById(this.reserId);
					this.parseData(getFilter());
				}
				
			} else {
				Helpers.status("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void btnUpdateAction() {
    	try {
			
			if(this.reserId != 0) {
					this.showCreateForm();
			} else {
				
				Helpers.status("error");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    public void findOnClick() {
    	String New="empty", conf="empty", depo="empty", pre="empty", cancel="empty", expired="empty";
    	if(rdConfirm.isSelected()) {
    		conf =rdConfirm.getId();
    	}
    	if(rdNew.isSelected()) {
    		New =rdNew.getId();
    	}
    	if(rdDeposit.isSelected()) {
    		depo = rdDeposit.getId();
    	}
    	if(rdPresent.isSelected()) {
    		pre = rdPresent.getId();
    	}
    	if(rdCancel.isSelected()) {
    		cancel = rdCancel.getId();
    	}
    	if(rdExpried.isSelected()) {
    		expired =rdExpried.getId();
    	}
    	List<String> array = Arrays.asList(New, expired, conf, depo, pre, cancel);
    	ArrayList<String> a = new ArrayList<String>();
    	a.addAll(array);
    	ArrayList<CompareOperator> cond = new ArrayList<CompareOperator>();
		
    	if(checkSelected(a)) {
    		cond.add(CompareOperator.getInstance("date_pick", " >= ", dpFrom.getValue().toString()));
    		cond.add(CompareOperator.getInstance("("+statusSelected(a)+") and date_pick ", "<=", dpTo.getValue().toString()));
    		
    	}
    	else {
    		cond.add(CompareOperator.getInstance("date_pick", " >= ", dpFrom.getValue().toString()));
    		cond.add(CompareOperator.getInstance("date_pick ", "<=", dpTo.getValue().toString()));
    	}
    	this.parseData(cond);
    	
    	

    }
    private boolean checkSelected(ArrayList<String> array) {
    	boolean check = false;
    	int k = 0;
    	for(String item : array) {
			if(!item.equals("empty")) {
				k++;
			}
			if(k>0) {
				check=true;
			}
    	}
    	System.out.println("check: "+check);
		return check;
    }
    private String statusSelected(ArrayList<String> array) {
	    	String value ="";
	    	int k = 0;
	    	for(String item : array) {
				if(!item.equals("empty")) {
					k++;
				}
	    	}
			
			int i = 0;
			for(String item : array) {
				if(!item.equals("empty")) {
				value += "reservations.status="+item;
				i++;
				if(i < k) {
					value += " or ";
				}
				
				}
		}
    return value;
    }
    
 
    @FXML
    public void getRowSelected(MouseEvent event) {
    	btnDelete.setDisable(true);
		paneDetails.setVisible(true);
		if(AuthenticationModel.hasPermission("CREATE_RESERVATION") || AuthenticationModel.roleName.equals("Super Admin")) {
			btnPut.setDisable(false);
		}
		
    	try {
			if(event.getClickCount() > 0) {
				ReservationModel item = tblReser.getSelectionModel().getSelectedItem();
				if(item != null) {
					DataMapping status=ReservationModel.isCancelled;
					if(item.getStatus()==1) {
						status=ReservationModel.isNew;
					}else if(item.getStatus()==2) {
						status=ReservationModel.isConfirmed;
					}else if(item.getStatus()==3) {
						status=ReservationModel.isDeposited;
					}else if(item.getStatus()==4) {
						status=ReservationModel.isPresent;
					}else if(item.getStatus()==5) {
						status=ReservationModel.isExpried;
					}
					btnUpdate.setDisable(true);
					if(item.getStatus()!=4&&item.getStatus()!=5&&item.getStatus()!=0&&AuthenticationModel.hasPermission("UPDATE_RESERVATION")) {
						btnUpdate.setDisable(false);
					}
					lblCode.setText("Code: \t"+item.getCode());
					lblCusName.setText("Customer name: "+item.getName());
					lblSeats.setText("Seat(s) pick: "+item.getSeats());
					lblTime.setText("Date: \t"+item.getDate_pick()+" --- "+item.getStart_time()+" - "+item.getEnd_time());
					lblPhone.setText("Phone: \t"+item.getPhone());
					lblEmail.setText("Email: \t"+item.getEmail());
					lblDeposit.setText("Deposit: "+item.getDeposit());
					lblStatus.setText("Status: "+status);
					lblCreated.setText("Created at: "+ item.getCreatedAt());
					this.reserId = item.getId();
					this.reserName = item.getName();
					String tablePick = "";
					String sql = "select name from tables where id in (select table_id from tables_reservation where reservation_id="+item.getId()+")";
					Statement st = MySQLJDBC.connection.createStatement();
					ResultSet rs = st.executeQuery(sql);
					while(rs.next()) {
						tablePick+= rs.getString("name")+"  ";
						
					}
					lblTablePick.setText("Table(s) pick: "+tablePick);
					
				}
				
				
				if((checkToDelete(item.getId())&&AuthenticationModel.hasPermission("CREATE_RESERVATION")) || AuthenticationModel.roleName.equals("Super Admin")) {
					btnDelete.setDisable(false);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
    }

    @FXML
    void onSearch(KeyEvent event) {
    	try {
			this.parseData(getFilter());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    //update status New to Confirmed after 30 minutes
    public void updateStatus() {
    	String sql = "update reservations set status = 2 where status = 1 and DATE_ADD(created_at, INTERVAL 30 MINUTE)<=now()" ;
    	String sql2 = "update reservations set status = 5 where (status = 2 or status=3 or status =1 ) and  ((DATE_ADD(curtime(), INTERVAL -30 MINUTE)>= start_time  and date_pick<=curdate()) or (date_pick<curdate()))" ;
    	String sql3= "update tables_reservation set status = 0 where reservation_id in (select id from reservations where status = 5 or status = 0 or (status = 4 and date_pick<curdate()))";
    	System.out.println(sql2);
        try {
        	Statement stmt = MySQLJDBC.connection.createStatement();
    	   stmt.execute(sql);
    	   Statement stmt2 = MySQLJDBC.connection.createStatement();
    	   stmt2.execute(sql2);
    	   Statement stmt3 = MySQLJDBC.connection.createStatement();
    	   stmt3.execute(sql3);
    	}catch(SQLException  ex) {
    		ex.printStackTrace();
    	}
    	
    }
    
    
	//search
	public ArrayList<CompareOperator> getFilter(){
		try {
			String code = tfTable.getText();
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("customer_name", " like ", "%"+ code + "%"));
			return conditions;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@FXML
	 void showPane(javafx.event.ActionEvent evt) {
		if(evt.getSource()==btnReserSche) {
			loadSchedule("schedule.fxml");
			btnReserSche.setVisible(false);
			btnTablesSche.setVisible(true);
		}
		if(evt.getSource()==btnTablesSche) {
			loadSchedule("tables-schedule.fxml");
			btnReserSche.setVisible(true);
			btnTablesSche.setVisible(false);
		}
	}
	   //load schedule
    public void loadSchedule(String path) {
    	 AnchorPane anchor = new AnchorPane();
    	 ScheduleController.inReser=1;
   		try {
   			anchor = FXMLLoader.load(getClass().getResource("/views/"+path));
   			paneSchedule.getChildren().setAll(anchor);
   		} catch (IOException e) {
   			e.printStackTrace();
   		}
    }
    
    private boolean checkToDelete(int id) {
    	boolean check=false;
    	
    	String query = "select id, status from reservations where id ="+id+" and (status=5 or status =0)";
    	try{
    		Statement ps = MySQLJDBC.connection.createStatement();
        	ResultSet rs = ps.executeQuery(query);
        	if(rs.next()) {
        		check=true;
        	}
    	}catch(SQLException  ex) {
    		ex.printStackTrace();
    	}
    	
    	return check;
    }
    @FXML
    void resetInput() {
    	paneDetails.setVisible(false);
    	rdCancel.setSelected(false);
    	rdConfirm.setSelected(false);
    	rdNew.setSelected(false);
    	rdExpried.setSelected(false);
    	rdDeposit.setSelected(false);
    	rdPresent.setSelected(false);
    	LocalDate now = LocalDate.now();
    	dpFrom.setValue(now.withDayOfMonth(1));
    	dpTo.setValue(now.withDayOfMonth(now.lengthOfMonth()));
    	paneDetails.setVisible(false);
    	rdNew.setSelected(false);
       	rdCancel.setSelected(false);
       	rdConfirm.setSelected(false);
       	rdDeposit.setSelected(false);
       	rdExpried.setSelected(false);
       	rdPresent.setSelected(false);
       	this.findOnClick();
    	
    }


	public int getReserId() {
		return reserId;
	}


	public void setReserId(int reserId) {
		this.reserId = reserId;
	}


	public String getReserName() {
		return reserName;
	}


	public void setReserName(String reserName) {
		this.reserName = reserName;
	}
    
    
}
