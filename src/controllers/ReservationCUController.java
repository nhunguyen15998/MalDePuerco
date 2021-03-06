package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.Connection;

import db.MySQLJDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.AuthenticationModel;
import models.DiscountModel;
import models.ReservationModel;
import models.RolePermissionModel;
import models.TableModel;
import models.TablesReserModel;
import models.UserModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;
import javafx.scene.control.DatePicker;

public class ReservationCUController implements Initializable {
	private ReservationModel reserModel = new ReservationModel();
	private TablesReserModel tblReserModel = new TablesReserModel();
	private TableModel tableModel = new TableModel();
	private ReservationController reController = new ReservationController();
	private int reserId;
	@FXML
	private AnchorPane cuHolder;
	@FXML
	private Label lblReser;
	@FXML
	private TextField tfName;
	@FXML
	private TextField tfCode;
	@FXML
	private TextField tfEmail;
	@FXML
	private TextField tfPhone;
	@FXML
	private ComboBox<DataMapping> cbStatus;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblNameError;
	@FXML
	private Label lblEmailError;
	@FXML
	private Label lblPhoneError;
	@FXML
	private TextField tfDeposit;
	@FXML
	private TextField tfSeat;
	@FXML
	private Label lblDepoError;
	@FXML
	private Label lblSeatError;
	@FXML
	private TextField tfStart;
	@FXML
	private TextField tfEnd;
	@FXML
	private DatePicker dpDate;
    @FXML
    private ComboBox<DataMapping> cbbTable;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Label lblTableError;
    @FXML
    private TextArea tfTable;
	@FXML
	private Label lblTimeError;
	ArrayList<DataMapping> listTable = new ArrayList<DataMapping>();
	ArrayList<DataMapping> listTableAuth = new ArrayList<DataMapping>();
	private static int seats = 0;
	boolean checkTable=true;
	private int timeCancel;
	private DataMapping stt;
	private boolean checkType = true;
	private ArrayList<DataMapping> table = new ArrayList<DataMapping>();
	@FXML
	public void btnSaveAction(ActionEvent event) {
		try {
			String name = tfName.getText();
			String phone = tfPhone.getText();
			String email = tfEmail.getText();
			String start = tfStart.getText();
			String end = tfEnd.getText();
			String depo = tfDeposit.getText();
			String code = Helpers.randomCode("RS");
			String seat = tfSeat.getText();
			String date = dpDate.getValue().toString();
			String status = cbStatus.getValue() == ReservationModel.isNew ? cbStatus.getValue().key : 
				(cbStatus.getValue()  == ReservationModel.isCancelled ? cbStatus.getValue().key : 
				(cbStatus.getValue()  == ReservationModel.isConfirmed ? cbStatus.getValue().key :
				(cbStatus.getValue()  == ReservationModel.isDeposited ? cbStatus.getValue().key :
				(cbStatus.getValue()  == ReservationModel.isExpried ? cbStatus.getValue().key: String.valueOf(ReservationModel.RESER_PRESENT)))));
			if(validated( name, phone, email, seat, depo, start, end)) {
				ArrayList<DataMapping> re = new ArrayList<DataMapping>();
				re.add(DataMapping.getInstance("customer_name", name));
				re.add(DataMapping.getInstance("phone", phone));
				if(!email.isEmpty()) {
				re.add(DataMapping.getInstance("email", email));
				}
				
				re.add(DataMapping.getInstance("seats_pick", seat));
				if(!depo.isEmpty()) {
				re.add(DataMapping.getInstance("deposit", depo));
				}
				if(depo.isEmpty()) {
					re.add(DataMapping.getInstance("deposit", "0"));
					}
				re.add(DataMapping.getInstance("start_time", Helpers.formatTime(start)));
				re.add(DataMapping.getInstance("end_time", Helpers.formatTime(end)));
				re.add(DataMapping.getInstance("date_pick", date));
				re.add(DataMapping.getInstance("status", status));

				if(this.reserId == 0) {
					re.add(DataMapping.getInstance("code", code));
					reserModel.createReser(re);
					for(DataMapping item : listTable) {

						System.out.println("item insert:"+item);
						ArrayList<DataMapping> data = new ArrayList<DataMapping>();
						data.add(DataMapping.getInstance("table_id",item.key));
						data.add(DataMapping.getInstance("reservation_id", getIDReser(code)+""));
						tblReserModel.create(data);
					}
					Helpers.status("success");
					close();
					 reController.refresh();
					
					
				} else {
					System.out.println("check: "+ checkToPresent(reserId));
					Alert alert = new Alert(AlertType.ERROR);
					boolean checkPresent = (status.equals("4")&&checkToPresent(reserId));
					boolean checkCancel = (status.equals("0")&&checkToCancel(reserId));
					 if(!status.equals("4")&&!status.equals("0")) {
						 updateReser(re);
					}else if(checkPresent) {
							 updateReser(re);
						}else if(!checkPresent&&!status.equals("0")){
							alert.setHeaderText("Not at the right time to update");
							 alert.showAndWait();
							
						}
						else if(checkCancel) {
							 updateReser(re);
						}else if(!checkCancel&&!status.equals("4")) {
							
							alert.setHeaderText("Can not cancel reservation after "+timeCancel+" hours");
							 alert.showAndWait();
						}
					
					 
					
					
				}
				checkTable=true;
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void updateReser(ArrayList<DataMapping> re) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Update Reservation Confirmation");
		alert.setHeaderText("Do you want to make this change?");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK) {
			reserModel.updateReserById(this.reserId, re);
			ArrayList<CompareOperator> condiRemove = new ArrayList<CompareOperator>();
  			condiRemove.add(CompareOperator.getInstance("reservation_id", "=", getIDReser(tfCode.getText())+""));
  			this.tblReserModel.delete(condiRemove);
			for(DataMapping item : listTable) {
				System.out.println("item update:"+item);
				ArrayList<DataMapping> data = new ArrayList<DataMapping>();
				data.add(DataMapping.getInstance("table_id", item.key));
				data.add(DataMapping.getInstance("reservation_id", getIDReser(tfCode.getText())+""));
				tblReserModel.create(data);
			}
			Helpers.status("success");

			close();
			 reController.refresh();
			
		} 
	}
	private boolean checkToPresent(int id) {
		boolean check = true;
		String sql ="select date_pick, start_time,end_time from reservations where (id="+id+" and date_pick=curdate() and curtime()<start_time) or (id="+id+" and date_pick<curdate())  or (id="+id+" and date_pick>curdate()) ";

		System.out.println(sql);
		try {
			Statement st = MySQLJDBC.connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()) {
				check=false;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return check;
				
	}
	private boolean checkToCancel(int id) {
		boolean check = true;
		String sql ="select created_at, date_pick from reservations where (id="+id+" and date_add(now() , INTERVAL -"+timeCancel+" HOUR)> created_at) or (id="+id+" and created_at<now())";
		System.out.println(sql);
		try {
			Statement st = MySQLJDBC.connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()) {
				check=false;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return check;
				
	}
	private int getIDReser(String code) {
		System.out.println(code);
		int id =0;
		String sql = "select id from reservations where code='"+code+"'";
		try {
	    Statement st = MySQLJDBC.connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if(rs.next()) {
			id=rs.getInt("id");
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return id;
	}
	
	 public void close() {
			try {
				Stage stage = (Stage) btnCancel.getScene().getWindow();
				stage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	@FXML
	public void btnCancelAction(ActionEvent event) {
		try {

			seats=0;
			listTable.clear();
			listTableAuth.clear();
			this.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ObservableList<DataMapping> status=FXCollections.observableArrayList(ReservationModel.isNew,ReservationModel.isDeposited,ReservationModel.isPresent, ReservationModel.isExpried,ReservationModel.isCancelled );
		Preferences preference;
		preference = Preferences.userNodeForPackage(SettingController.class);
		timeCancel=preference.getInt("timeCancel",2);
		cbStatus.setItems(status);
		cbStatus.setValue(ReservationModel.isNew);
		dpDate.setValue(LocalDate.now());
		tfDeposit.setDisable(true);
		LocalDate maxDate = LocalDate.now();
		dpDate.setDayCellFactory(d ->
		           new DateCell() {
		               @Override public void updateItem(LocalDate item, boolean empty) {
		                   super.updateItem(item, empty);
		                   if(item.isBefore(maxDate)){
		                	   setDisable(item.isBefore(maxDate));
			                   setStyle("-fx-background-color: #eba9b4;");
		                   }
		               }});
		



	}


		
		//load data update
	  	public void loadDataUpdateById(ReservationController r) {
	  		try {
	  			
	  			this.reController = r;
	  			this.reserId = r.getReserId();
	  			lblReser.setText("Create Reservation");
	  			cbStatus.setDisable(true);
	  			cbbTable.setDisable(true);
	  			lblTableError.setText("You must fill in date,time,seats to select a table(s)");
	  			if(this.reserId != 0) {
	  				btnAdd.setDisable(true);
	  				lblReser.setText("Update Reservation");
	  				cbStatus.setDisable(false);
	  				ObservableList<DataMapping> sts = FXCollections.observableArrayList(ReservationModel.isConfirmed,ReservationModel.isDeposited,ReservationModel.isPresent,ReservationModel.isCancelled );
	  				cbStatus.setItems(sts);
	  				this.getTableList(dpDate.getValue().toString(),tfStart.getText(), tfEnd.getText()," and reservation_id!="+reserId);
	  				cbbTable.setDisable(false);
	  				lblTableError.setText("");
	  				
	  				ResultSet re = this.reserModel.getReserById(this.reserId);
	  				if(re.next()) {
	  					tfCode.setText(re.getString("code"));
	  					tfName.setText(re.getString("customer_name"));
	  					tfPhone.setText(re.getString("phone"));
	  					tfEmail.setText(re.getString("email"));
	  					tfSeat.setText(re.getString("seats_pick"));
	  					tfDeposit.setText((int)(re.getDouble("deposit"))+"");
	  					dpDate.setValue(re.getDate("date_pick").toLocalDate());
	  					tfStart.setText(Helpers.formatTime(re.getString("start_time")));
	  					tfEnd.setText(Helpers.formatTime(re.getString("end_time")));
	  					tfDeposit.setDisable(true);
	  					if((int)(re.getDouble("deposit"))>4) {
	  						tfDeposit.setDisable(false);
	  					}
	  					for(DataMapping status : cbStatus.getItems()) {
	  						if(status.key != null && Integer.parseInt(status.key) == re.getInt("status")) {
	  							cbStatus.setValue(status);
	  							stt=status;
	  							System.out.println(status);
	  							break;
	  						}
	  					}
	  					cbStatus.setDisable(false);
	  					if( re.getInt("status")==1) {
	  						cbStatus.setValue(ReservationModel.isNew);
	  						stt=ReservationModel.isNew;
	  						cbStatus.setDisable(true);
	  					}
	  					
	  				
	  		  			DataMapping content = getTableName(reserId);
	  		  			tfTable.setText(content.value);
	  		  			if(listTable.isEmpty()) {
	  		  				checkTable=false;
	  		  			}
	  					
	  					
	  				}
	  			}
	  		} catch (Exception e) {
	  			e.printStackTrace();
	  		}
	  	}
		
	  	public void loadDataPutMore(ReservationController r) {
	  		try {
	  			
	  			this.reController = r;
	  			this.reserId = r.getReserId();
	  			lblReser.setText("Create Reservation");
	  			cbStatus.setDisable(true);
	  			cbbTable.setDisable(true);
	  			lblTableError.setText("You must fill in date,time,seats to select a table(s)");
	  		
	  				
	  				ResultSet re = this.reserModel.getReserById(this.reserId);
	  				if(re.next()) {
	  					tfName.setText(re.getString("customer_name"));
	  					tfPhone.setText(re.getString("phone"));
	  					tfEmail.setText(re.getString("email"));
	  					this.reserId = 0;
	  			}
	  		} catch (Exception e) {
	  			e.printStackTrace();
	  		}
	  	}
	  	
	    private DataMapping getTableName(int id){
	    	DataMapping tablename = new DataMapping();

	    	String sql = "select tables.id,tables.name, tables.seats from tables_reservation join tables on table_id=tables.id join reservations on reservation_id=reservations.id where reservation_id="+id;
	    	System.out.println(sql);
	    	try{
	    		Statement st = MySQLJDBC.connection.createStatement();
	    	
	    	ResultSet rs = st.executeQuery(sql);
	    	while(rs.next()) {
	    		listTable.add(DataMapping.getInstance(rs.getInt("tables.id"), rs.getString("tables.name")));
	    		seats+=rs.getInt("tables.seats");
		    	tablename.value=statusSelected();
	    	}

	    	return tablename;
	    	}catch (Exception e) {
				// TODO: handle exception
	    		return null;
			}
	    	
	    }
	  //validate
		public boolean validated( String name, String phone, String email, String seat, String depo, String start, String end) {
			try {
				lblNameError.setText("");
				lblPhoneError.setText("");
				lblEmailError.setText("");
				lblSeatError.setText("");
				lblTimeError.setText("");
				String s = tfStart.getText();
		    	String e = tfEnd.getText();
		    	
				ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
				data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:5"));
				data.add(new ValidationDataMapping("phone", phone, "lblPhoneError", "required|phone|max:10"));
				data.add(new ValidationDataMapping("email", email, "lblEmailError", "email|required"));
				data.add(new ValidationDataMapping("seat", seat, "lblSeatError", "min:1|numeric|required"));
				data.add(new ValidationDataMapping("deposit", depo, "lblDepoError", "numeric|min:0"));
				data.add(new ValidationDataMapping("start", start, "lblTimeError", "time|required"));
				data.add(new ValidationDataMapping("end", end, "lblTimeError", "time|required"));
				ArrayList<DataMapping> messages = Validations.validated(data);
				if(messages.size() > 0) {
					for(DataMapping message : messages) {
						switch(message.key) {
							case "lblNameError":
								lblNameError.setText(message.value);
								break;
							case "lblPhoneError":
								lblPhoneError.setText(message.value);
								break;
							case "lblEmailError":
								lblEmailError.setText(message.value);
								break;
							case "lblSeatError":
								lblSeatError.setText(message.value);
								break;
							case "lblDepoError":
								lblDepoError.setText(message.value);
								break;
							case "lblTimeError":
								lblTimeError.setText(message.value);
							
								break;
							
							default:
								System.out.println("abcde");
						}

					}
					return false;
				}
				if(lblTimeError.getText().isEmpty()&&s.length()==5&&e.length()==5) {
			    	LocalTime time = LocalTime.parse(start);
			        LocalTime time2 = LocalTime.parse(end);
			        boolean checkTime = Validations.checkTime(time , time2, lblTimeError, "unachievable!");
			      
			        if(!checkTime||!checkTable||listTable.isEmpty()) {
			        	lblTableError.setText("please choose");
			        	return false;
			        }
			        if(!lblDepoError.getText().isEmpty()) {
			        	return false;
			        }
				}
			        if(listTable.isEmpty()) {
			        	lblTableError.setText("please choose");
			        	return false;
			        }
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
// create: show new and deposited, if textfield deposit typed => status change to Diposited and not type => New
//Update: show deposited, present, cancelled, expired
		
		//key event
	    @FXML
	    void changeStatus() { 
	    	lblDepoError.setText("");
	    	if(tfDeposit.getText().equals("0")) {
	    		if(reserId==0) {
	    		cbStatus.setValue(ReservationModel.isNew);
	    		}else {
	    			cbStatus.setValue(stt);
	    		}
	    		
	    		
	    	}else if(!tfDeposit.getText().equals("0")&&!tfDeposit.getText().isEmpty()&&depoCheck(tfDeposit.getText())){
	    		cbStatus.setValue(ReservationModel.isDeposited);
	    		
	    	}else {
	    		if(reserId==0) {
		    		cbStatus.setValue(ReservationModel.isNew);
		    		}else {
		    			cbStatus.setValue(stt);
		    		}
	    	}
	    	
	    }
	    private boolean depoCheck(String depo) {
	    	

		lblDepoError.setText("");
		    	ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
		    	data.add(new ValidationDataMapping("deposit", depo, "lblDepoError", "numeric|min:100000"));
				ArrayList<DataMapping> messages = Validations.validated(data);
		    	if(messages.size() > 0) {
					for(DataMapping message : messages) {
					switch(message.key) {
						case "lblDepoError":
							lblDepoError.setText(message.value);
							break;
						default:
							System.out.println("abcde");
					}

				}
				return false;
			}
	    	return true;
}
	    @FXML
	    void checkTime() { 
	    	checkType=false;
	    	seats=0;
	    	listTableAuth.clear();
	    	listTable.clear();
	    	tfTable.setText("");
	    	btnAdd.setDisable(false);
	    	String start = tfStart.getText();
	    	String end = tfEnd.getText();
	    	String seats = tfSeat.getText();
	    	int seat=0;
	    	if(seats.length()>0) {
	    	seat = Integer.parseInt(seats);
	    	}
	    	

    		changeStatus();
	    	lblDepoError.setText("");
    		
	    	
	    	tfDeposit.setDisable(true);
	    	if(seat>4) {
	    		tfDeposit.setDisable(false);
	    		if(tfDeposit.getText().isEmpty()) {
	    		lblDepoError.setText("You have to deposit at least 100 000 VND");
		    	}
	    		if(!tfDeposit.getText().isEmpty()) {
	    			int depo = Integer.parseInt(tfDeposit.getText());
		    		if(tfDeposit.getText().isEmpty()||depo<10) {
		    			lblDepoError.setText("You have to deposit at least 100 000 VND");
			    	}
	    		}
	    	}
	    	if(!end.equals("")&&!start.equals("")&&!(seat<=0)) {
	    	if(time(seats,start, end)&&reserId==0&&seat>0) {
	    		this.getTableList(dpDate.getValue().toString(),start, end,"");
	    		cbbTable.setDisable(false);
	    		lblTableError.setText("");
		        
	    	}
	    	if(time(seats,start, end)&&reserId!=0&&seat>0) {
	    		this.getTableList(dpDate.getValue().toString(),start, end," and reservation_id!="+reserId);
	    		cbbTable.setDisable(false);
	    		lblTableError.setText("");
	    	}
	    	
	    	}
	    	
	    	if(end.equals("")||start.equals("")||seat<=0||!lblTimeError.getText().isEmpty()) {
	    		cbbTable.setDisable(true);
	    	}

	    	
	    }
	    private boolean time(String seats,String start, String end) {
	    	

	    	lblTimeError.setText("");
	    	ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
	    	data.add(new ValidationDataMapping("start", start, "lblTimeError", "time|required"));
			data.add(new ValidationDataMapping("end", end, "lblTimeError", "time|required"));
			data.add(new ValidationDataMapping("seats", seats, "", "min:1|numeric|required"));
			ArrayList<DataMapping> messages = Validations.validated(data);
	    	if(messages.size() > 0) {
				for(DataMapping message : messages) {
					switch(message.key) {
						case "lblTimeError":
							lblTimeError.setText(message.value);
							break;
						default:
							System.out.println("abcde");
					}

				}
				return false;
			}
	    	String s = tfStart.getText();
	    	String e = tfEnd.getText();
	    	if(lblTimeError.getText().equals("")&&s.length()==5&&e.length()==5) {
	    	LocalTime time = LocalTime.parse(start);
	        LocalTime time2 = LocalTime.parse(end);
	        boolean checkTime = Validations.checkTime(time , time2, lblTimeError, "unachievable!");
	        boolean checkTimeToUpdate =true;
	        Calendar now = Calendar.getInstance();
	           int year= now.get(Calendar.YEAR);
	           int month= (now.get(Calendar.MONTH) + 1);
	           int day= + now.get(Calendar.DATE);
	        LocalDate nowLocal = LocalDate.of(year,month,day);
	        if(!checkType&&dpDate.getValue().compareTo(nowLocal)==0) {
	        	checkTimeToUpdate= Validations.checkTimeUpdate(time, lblTimeError, "unachievable!");
	        }
	        if(!checkTime||!checkTimeToUpdate) {
	        	return false;
	           
	        }
	    	}
	    	return true;
	    	
	    }
	    @FXML
	    void addTable() { 

	    	System.out.println(seats+"      "+Integer.parseInt(tfSeat.getText()));
	    	if(!(seats>=Integer.parseInt(tfSeat.getText()))) {
	    	String value = cbbTable.getValue().value;
			if(checkExists(DataMapping.getInstance(cbbTable.getValue().key, value))) {
				DataMapping list = DataMapping.getInstance(cbbTable.getValue().key, value);
				listTableAuth.add(list);
				listTable.add(returnData(list));
				tfTable.setText(statusSelected());
				
				seats+=seatsCheck(Integer.parseInt(cbbTable.getValue().key));
			}

	    	System.out.println(seats+"      "+Integer.parseInt(tfSeat.getText()));
			if(seats>=Integer.parseInt(tfSeat.getText())) {
				lblTableError.setText("had enough. Seats table:"+seats);
				btnAdd.setDisable(true);
				checkTable=true;
				
			}else {
				lblTableError.setText("please choose more. Seats table:"+seats);
				checkTable=false;
			}
					
					
	    	}else {
				lblTableError.setText("had enough. Seats table:"+seats);
				checkTable=true;
			}
	    }
	    private int seatsCheck(int id) {
	    	int seat = 0;
	    	try {
	    	ResultSet rs = tableModel.getTableById(id);
	    	if(rs.next()) {
					seat=rs.getInt("seats");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return seat;
	    }
	 //check exists value
	    private boolean checkExists(DataMapping ob) {
	    	boolean check = true;
	    	int k = 0;
	    	for(DataMapping item : listTableAuth) {
				if(item.value.equals(ob.value)) {
					k++;
				}
				if(k>0) {
					check=false;
				}
	    	}
	    	System.out.println("check: "+check);
			return check;
	    }
	    private DataMapping returnData(DataMapping ob) {
	    	DataMapping a = null;
	    	int k = 0;
	    	for(DataMapping item : table) {
				if(item.key.equals(ob.key)) {
					a=DataMapping.getInstance(item.key, item.value);
					k++;
				}
	    	}
			return a;
	    }
	    //return String add "," after result
	    private String statusSelected() {
	    	int count = 0;
	    	String value ="";
			int i = 0;
			
			for(DataMapping item : listTable) {
				value += item.value;
				i++;
				if(i < listTable.size()) {
					value += ", ";
				}
				count++;
				System.out.println("count:"+ count);
				if(count%4==0) {
					value += "\n";
				}
				
		}
    return value;
    }
	    @FXML
	    void deleteTable() { 
	    				listTableAuth.clear();
		    			listTable.clear();
						tfTable.setText("");
						seats=0;
		    	if(seats>=Integer.parseInt(tfSeat.getText())) {
					lblTableError.setText("had enough. Seats table:"+seats);
					btnAdd.setDisable(true);
					checkTable=true;
				}else {
					lblTableError.setText("please choose more. Seats table:"+seats);
					btnAdd.setDisable(false);
					checkTable=false;
				}
						
						
		    
	    }
	  
	  	@FXML
	  	public void dateAction() {
	  		seats=0;
	  		listTableAuth.clear();
	    	listTable.clear();
	    	lblTableError.setText("You must fill in date,time,seats to select a table(s)");
	    	tfTable.setText("");
	    	btnAdd.setDisable(false);
	  		if(reserId==0) {
	  		this.getTableList(dpDate.getValue().toString(),tfStart.getText(), tfEnd.getText(),"");
	  		
	  		}else {
	  			this.getTableList(dpDate.getValue().toString(),tfStart.getText(), tfEnd.getText()," and reservation_id!="+reserId);
	  		}

	    	checkTime();
	  	}
	  	//load code discount
	  		public ResultSet getTableList(String date,String st, String et, String cond) {
	  			try {
	  				
	  				ArrayList<DataMapping> options = new ArrayList<DataMapping>();
	  				String sql = "select id, name, seats from tables where id not in (select table_id from tables_reservation where status =1 "+cond+" and reservation_id in (select id from reservations where  ((start_time<='"+st+"' and '"+st+"'<end_time and end_time<='"+et+"') or ('"+st+"'<=start_time and start_time<=end_time and end_time<='"+et+"' ) or ('"+st+"'<=start_time and start_time<=end_time and end_time<='"+et+"' ) or ('"+st+"'<=start_time and start_time<'"+et+"' and '"+et+"'<=end_time) or (start_time<='"+st+"' and end_time>='"+et+"') or (start_time>='"+st+"' and end_time<'"+et+"')) and date_pick = '"+ date+"' ))";
	  				Statement ps = MySQLJDBC.connection.createStatement();
	  				System.out.println(sql);
	  				ResultSet rs = ps.executeQuery(sql);
	  				while(rs.next()) {
	  					options.add(DataMapping.getInstance(rs.getInt("id"), rs.getString("name")+"  -  seat(s): "+ rs.getInt("seats")));
	  					table.add(DataMapping.getInstance(rs.getInt("id"), rs.getString("name")));
	  				}
	  				cbbTable.getItems().setAll(options);
	  				return rs;
	  			} catch (Exception e) {
	  				e.printStackTrace();
	  				return null;
	  			}
	  			
	  		}
}
