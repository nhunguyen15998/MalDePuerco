package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import models.ServingAttributeModel;
import models.ServingModel;
import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;

public class ServingAttributeCUController implements Initializable {
	private ServingAttributeModel serattModel = new ServingAttributeModel();
	private ServingModel serModel = new ServingModel();
	private int serattId;
	public ServingAttributeController serattController;
	
	@FXML
    private ComboBox<DataMapping> cbServing;

    @FXML
    private TextField tfPrice;

    @FXML
    private TextField tfAttribute;

    @FXML
    private Label lblAttributeError;

    @FXML
    private Label lblPriceError;

    @FXML
    private Label lblServingError;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lbl;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.getServingList();
	}
	
	//validate
	public boolean validated(String serving, String attribute, String price) {
		try {
			lblServingError.setText("");
			lblAttributeError.setText("");
			lblPriceError.setText("");
			
			ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
			data.add(new ValidationDataMapping("serving", serving, "lblServingError", "required"));
			data.add(new ValidationDataMapping("attribute", attribute, "lblAttributeError", "required|string|min:10"));
			data.add(new ValidationDataMapping("price", price, "lblPriceError", "required|numberic"));
			
			ArrayList<DataMapping> mess = Validations.validated(data);
			if(mess.size() > 0) {
				for(DataMapping message : mess) {
					switch(message.key) {
						case "lblServingError":
							lblServingError.setText(message.value);
							break;
						case "lblAttributeError":
							lblAttributeError.setText(message.value);
							break;
						case "lblPriceError":
							lblPriceError.setText(message.value);
							break;
						default:
							System.out.println("validatation");
					}
				} return false;
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	//save
	@FXML
    void btnSaveAction(ActionEvent event) {
		try {
			String attribute = tfAttribute.getText();
			String price = tfPrice.getText();
			String serving = cbServing.getValue() != null ? cbServing.getValue().key : null;
			
			if(validated(serving, attribute, price)) {
				ArrayList<DataMapping> servingattribute = new ArrayList<DataMapping>();
				servingattribute.add(DataMapping.getInstance("serving_id", serving));
				servingattribute.add(DataMapping.getInstance("attribute", attribute));
				servingattribute.add(DataMapping.getInstance("price", price));
				
				if(this.serattId == 0) {
					serattModel.createSerAtt(servingattribute);
					Helpers.status("success");
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Update User Confirmation");
					alert.setHeaderText("Do you want to make this change?");
					Optional<ButtonType> option = alert.showAndWait();
					if (option.get() == ButtonType.OK) {
						serattModel.updateSerAtt(serattId, servingattribute);
						Helpers.status("success");
				}
			}
				serattController.loadData(null);
				this.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	//close
		public void close() {
			try {
				Stage stage = (Stage) btnCancel.getScene().getWindow();
				stage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	//cancel
		@FXML
	    void btnCancelAction(ActionEvent event) {
			try {
				this.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		
	//load ccb serving
	public ResultSet getServingList() {
		try {
			ArrayList<DataMapping> servingOptions = new ArrayList<DataMapping>();
			
			ResultSet serving = serModel.getServingList(null);
			while(serving.next()) {
				servingOptions.add(DataMapping.getInstance(serving.getInt("id"), serving.getString("name")));
			}
			cbServing.getItems().setAll(servingOptions);
			return serving;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//load data update
	public void loadDataUpdateById(ServingAttributeController serattController) {
		try {
			this.serattController = serattController;
			this.serattId = serattController.getServingattId();
			lbl.setText("Create Serving Attribute");
			if(this.serattId != 0) {
				lbl.setText("Update Serving Attribute");
				ResultSet currentServingAtt = this.serattModel.getSerAttById(this.serattId);
				if(currentServingAtt.next()) {
					tfAttribute.setText(currentServingAtt.getString("attribute"));
					tfPrice.setText(currentServingAtt.getString("price"));
					
					//load combobox serving
					for(DataMapping serving : cbServing.getItems()) {
						if(serving.key != null & Integer.parseInt(serving.key) == currentServingAtt.getInt("serving_id")) {
							cbServing.setValue(serving);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
