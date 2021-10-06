package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.UserModel;
import utils.CompareOperator;
import utils.Helpers;

public class UserController  implements Initializable {
	private UserModel userModel = new UserModel();
	private int userId;
	private String userName;

	@FXML
	private AnchorPane createHolder;
	
	@FXML
	private TextField tfUser;
	@FXML
	private Button btnCreate;
	@FXML
	private Button btnUpdate;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnAttendance;
	
	//table, cols
	@FXML
	private TableView<UserModel> tableUser;
	@FXML
	private TableColumn<UserModel, Integer> colOrder;
	@FXML
	private TableColumn<UserModel, Integer> colId;
	@FXML
	private TableColumn<UserModel, String> colCode;
	@FXML
	private TableColumn<UserModel, String> colName;
	@FXML
	private TableColumn<UserModel, String> colEmail;
	@FXML
	private TableColumn<UserModel, String> colPhone;
	@FXML
	private TableColumn<UserModel, String> colUserType;
	@FXML
	private TableColumn<UserModel, LocalDate> colCreatedAt;
	@FXML
	private TableColumn<UserModel, String> colStatus;
	/**
     * Initializes the controller class.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//role
		//create
//		btnCreate.setVisible(false);
//		btnCreate.setManaged(false);
//		
//		//update
//		btnUpdate.setVisible(false);
//		btnUpdate.setManaged(false);
//		
//		//delete
//		btnDelete.setVisible(false);
//		btnDelete.setManaged(false);
//		
//		//attendance
//		btnAttendance.setVisible(false);
//		btnAttendance.setManaged(false);
		
//		if(AuthenticationModel.hasPermission("CREATE_USER") || AuthenticationModel.roleName.equals("Admin")) {
//			btnCreate.setVisible(true);
//			btnCreate.setManaged(true);
//		}
//		
//		if(AuthenticationModel.hasPermission("UPDATE_USER") || AuthenticationModel.roleName.equals("Admin")) {
//			btnUpdate.setVisible(true);
//			btnUpdate.setManaged(true);
//		}
//		
//		if(AuthenticationModel.hasPermission("DELETE_USER") || AuthenticationModel.roleName.equals("Admin")) {
//			btnDelete.setVisible(true);
//			btnDelete.setManaged(true);
//		}
//		
//		if(AuthenticationModel.hasPermission("CHECK_ATTENDANCE") || AuthenticationModel.roleName.equals("Admin")) {
//			btnAttendance.setVisible(true);
//			btnAttendance.setManaged(true);
//		}
		//load data
		this.parseData(null);
	}
	
	//load data
	public void parseData(ArrayList<CompareOperator> conditions) {
		try {
			//create list, format date
			ObservableList<UserModel> userList = FXCollections.observableArrayList();
					
			//cols get from model
			colId.setCellValueFactory(new PropertyValueFactory<UserModel, Integer>("id"));
			colId.setVisible(false); //hide id
			colOrder.setCellValueFactory(new PropertyValueFactory<UserModel, Integer>("sequence"));
			colCode.setCellValueFactory(new PropertyValueFactory<UserModel, String>("code"));
			colName.setCellValueFactory(new PropertyValueFactory<UserModel, String>("name"));
			colEmail.setCellValueFactory(new PropertyValueFactory<UserModel, String>("email"));
			colPhone.setCellValueFactory(new PropertyValueFactory<UserModel, String>("phone"));
			colUserType.setCellValueFactory(new PropertyValueFactory<UserModel, String>("role"));
			colCreatedAt.setCellValueFactory(new PropertyValueFactory<UserModel, LocalDate>("createdAt"));
			//format col
			colStatus.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(				
					cellData.getValue().getStatus() == UserModel.USER_ACTIVATED ? String.valueOf(UserModel.isActivated) : String.valueOf(UserModel.isDeactivated)));
			
			//get data from db
			ResultSet users = userModel.getUserList(conditions);
		
			while(users.next()) {
				userList.add(new UserModel(
					users.getInt("users.id"),
					users.getRow(),
					users.getString("users.code"),
					users.getString("user_name"),
					users.getString("email"),
					users.getString("phone"),
					users.getString("role_name"),
					users.getDate("users.created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy")),
					users.getInt("status"))
					);
			}
			tableUser.setItems(userList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//on mouse click
	public void getTableDataByClick(MouseEvent e) {
		try {
			if(e.getClickCount() > 0) {
				UserModel item = tableUser.getSelectionModel().getSelectedItem();
				if(item != null) {
					this.userId = item.getId();
					this.userName = item.getName();
					System.out.println(this.userId);
					System.out.println(this.userName);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
	
	//create
	public void btnCreateAction() {
		try {
			this.setUserId(0);
//			if(!UserModel.isShown) {
//				UserModel.isShown = true;
				this.showCreateForm();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//update
	public void btnUpdateAction() {
		try {
			
			if(this.userId != 0) {
//				if(!UserModel.isShown) {
//					UserModel.isShown = true;
					this.showCreateForm();
			//	}
			} else {
				//panel
				Helpers.status("error");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//delete
	public void btnDeleteAction() {
		try {
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			if(this.userId != 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete User Confirmation");
				alert.setHeaderText("Are you sure you want to delete this item ?");
				alert.setContentText("Name: ".concat(this.userName));
				alert.setX(screenBounds.getWidth() - 900);
				alert.setY(screenBounds.getHeight() - 610);
				
				Optional<ButtonType> options = alert.showAndWait();
				if(options.get() == ButtonType.OK) {
					this.userModel.deleteUserById(this.userId);
					this.parseData(getFilter());
				}
				
			} else {
				Helpers.status("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//show form create
	public void showCreateForm() {
		try {
			
			//draw
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/userUC.fxml"));
			createHolder = root.load();
			
			//controller
			UserCUController controller = root.<UserCUController>getController();
			controller.loadDataUpdateById(this);
			
			Scene scene = new Scene(createHolder, 610, 512);
			Stage createStage = new Stage();
			createStage.setX(screenBounds.getWidth() - 1050);
			createStage.setY(screenBounds.getHeight() - 750);
			createStage.initStyle(StageStyle.UNDECORATED);
			createStage.setScene(scene);
			createStage.show();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//search
	public ArrayList<CompareOperator> getFilter(){
		try {
			String code = tfUser.getText();
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("users.code", " like ", "%"+ code + "%"));
			//conditions.add(new CompareOperators("", name, name))
			return conditions;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void onSearch() {
		try {
			this.parseData(getFilter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void btnClearAction() {
		try {
			tfUser.setText("");
			this.parseData(getFilter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	//get & set
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}
