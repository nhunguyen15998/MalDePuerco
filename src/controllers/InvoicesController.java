package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.InvoiceModel;
import models.ReservationModel;
import utils.CompareOperator;
import utils.Helpers;
import javafx.scene.input.KeyEvent;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class InvoicesController implements Initializable {
	private InvoiceModel invoiceModel = new InvoiceModel();
	private int invoiceId;
	
	@FXML
	private TableView<InvoiceModel> tableInvoices;
	@FXML
	private TableColumn<InvoiceModel, Integer> colOrder;
	@FXML
	private TableColumn<InvoiceModel, Integer> colId;
	@FXML
	private TableColumn<InvoiceModel, String> colCode;
	@FXML
	private TableColumn<InvoiceModel, String> colOrderCode;
	@FXML
	private TableColumn<InvoiceModel, Double> colTotal;
	@FXML
	private TableColumn<InvoiceModel, String> colPayment;
	@FXML
	private TableColumn<InvoiceModel, String> colStatus;
	@FXML
	private TableColumn<InvoiceModel, LocalDate> colCreatedAt;
	@FXML
	private Button btnView;
	@FXML
	private Button btnDelete;
	@FXML
	private TextField tfInvoice;
	@FXML
	private AnchorPane viewDetails;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		parseData(null);	
	}
	
	//load data
  	public void parseData(ArrayList<CompareOperator> conditions) {
  		try {
   			ObservableList<InvoiceModel> invoiceList = FXCollections.observableArrayList();				
  			//cols get from model
  			colId.setCellValueFactory(new PropertyValueFactory<InvoiceModel, Integer>("id"));
  			colOrder.setCellValueFactory(new PropertyValueFactory<InvoiceModel, Integer>("sequence"));
  			colCode.setCellValueFactory(new PropertyValueFactory<InvoiceModel, String>("invoiceCode"));
  			colOrderCode.setCellValueFactory(new PropertyValueFactory<InvoiceModel, String>("orderCode"));
  			colTotal.setCellValueFactory(new PropertyValueFactory<InvoiceModel, Double>("totalOrder"));
  			colPayment.setCellValueFactory(new PropertyValueFactory<InvoiceModel, String>("paymentMethod"));
  			colCreatedAt.setCellValueFactory(new PropertyValueFactory<InvoiceModel, LocalDate>("createdAt"));
  			//format col
  			colStatus.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPaymentStatus() == InvoiceModel.PAID ? 
  							String.valueOf(InvoiceModel.isPaid) : String.valueOf(InvoiceModel.isUnpaid)));
  			
  			ResultSet invoices = this.invoiceModel.getInvoiceList(conditions);
  			while(invoices.next()) {
  				invoiceList.add(new InvoiceModel(
  					invoices.getInt("id"),
  					invoices.getRow(),
  					invoices.getString("code"),
  					invoices.getString("orders.code"),
  					invoices.getDouble("orders.total_amount"),
  					invoices.getString("payment_method.name"),
  					invoices.getInt("payment_status"),
  					invoices.getDate("created_at").toLocalDate().format(Helpers.formatDate("dd-MM-yyyy"))));
  			}
  			
  			tableInvoices.setItems(invoiceList);
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
		
	//on mouse click
	public void getTableDataByClick(MouseEvent e) {
		try {
			if(e.getClickCount() > 0) {
				InvoiceModel item = tableInvoices.getSelectionModel().getSelectedItem();
				if(item != null) {
					this.invoiceId = item.getId();
					System.out.println(this.invoiceId);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
	
	
	
	
	
	
	@FXML
	public void viewAction(ActionEvent event) {
		showFormDetails();
		
	}
	
	
	
	
	public void showFormDetails() {
  		try {
  			
  			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/invoice-details.fxml"));
  			viewDetails = root.load();
  			
  			//controller
  			InvoiceDetailsController controller = root.<InvoiceDetailsController>getController();
  		//	controller.loadDataUpdateById(this);
  			
  			Scene scene = new Scene(viewDetails, 600, 600);
  			Stage createStage = new Stage();
  			createStage.initStyle(StageStyle.UNDECORATED);
  			createStage.setScene(scene);
  			createStage.show();			
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
	
	//search
//	public ArrayList<CompareOperator> getFilter(){
//		try {
//			String code = tfInvoice.getText();
//			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
//			conditions.add(CompareOperator.getInstance("orders.code", " like ", "%"+ code + "%"));
//			return conditions;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	//delete
	@FXML
	public void btnDeleteAction() {
		try {
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			if(this.invoiceId != 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete Customer Confirmation");
				alert.setHeaderText("Are you sure you want to delete this item ?");
				alert.setContentText("Name: " + this.invoiceId);
				alert.setX(screenBounds.getWidth() - 726);
				alert.setX(screenBounds.getHeight() - 410);
				
				Optional<ButtonType> options = alert.showAndWait();
				if(options.get() == ButtonType.OK) {
					this.invoiceModel.deleteInvoice(this.invoiceId);
					this.parseData(null);
				}

			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Please select a row");
				alert.setX(screenBounds.getWidth() - 726);
				alert.setY(screenBounds.getHeight() - 410);
				alert.showAndWait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//clear
	@FXML
	public void btnClearAction() {
		try {
			tfInvoice.setText("");
			this.parseData(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//search
	public void onSearch() {
//		try {
//			this.parseData(getFilter());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	@FXML
	public void printAction(ActionEvent event) {
		// TODO Autogenerated
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}








}
