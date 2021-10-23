package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.AttributeModel;
import models.OrderDetailModel;
import utils.DataMapping;
import utils.Helpers;

public class OrderWaiterDUController implements Initializable{
	private OrderWaiterDController odControl = new OrderWaiterDController();
	private int oduID;
	private String oduName;
	
	private OrderDetailModel odModel = new OrderDetailModel();
	private AttributeModel attModel = new AttributeModel();
	
	@FXML
    private AnchorPane createHolder;

    @FXML private ComboBox<DataMapping> cbSize;

    @FXML
    private TextField tfQuan;

    @FXML
    private Button btnCancel;
    
    @FXML private TextArea tfNote;
    
    @FXML private Label lblCode;
    
    //load ccb
    public ResultSet getAttrList() {
    	try {
    		ArrayList<DataMapping> cb = new ArrayList<DataMapping>();
    		
    		ResultSet att = attModel.getAttributeList(null);
    		while(att.next()) {
    			cb.add(DataMapping.getInstance(att.getInt("id"), att.getString("name")));
    		}
    		cbSize.getItems().setAll(cb);
    		return att;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    @FXML
    void btnCancelAction(ActionEvent event) {
    	try {
    		this.close();
    		System.out.println("close");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void btnOKAction(ActionEvent event) {
    	try {
    		String quantity = tfQuan.getText();
    		String note = tfNote.getText();
    		String size = cbSize.getValue() != null ? cbSize.getValue().key : null;
    		
    		ArrayList<DataMapping> odu = new ArrayList<DataMapping>();
    		odu.add(DataMapping.getInstance("quantity", quantity));
    		odu.add(DataMapping.getInstance("size", size));
    		odu.add(DataMapping.getInstance("serving_note", note));
    		
    		if(this.oduID != 0) {
    			odModel.updateOrderDetail(oduID, odu);
    			Helpers.status("success");
    		}
    		this.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void close() {
		try {
			Stage stage = (Stage) btnCancel.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		odControl.setStatus(0);
		this.getAttrList();
	}
	
	//load data by id
	public void loadDataById(OrderWaiterDController odControl) {
		System.out.println("update");
		try {
			this.odControl = odControl;
			this.oduID = odControl.getOdID();
			
			ResultSet rs = this.odModel.getById(this.oduID);
			if(rs.next()) {
				tfQuan.setText(rs.getString("order_details.quantity"));
				tfNote.setText(rs.getString("order_details.serving_note"));
				
				for(DataMapping size : cbSize.getItems()) {
					if(size.key != null & Integer.parseInt(size.key) == Integer.valueOf(rs.getString("size")));
					cbSize.setValue(size);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getOduID() {
		return oduID;
	}

	public void setOduID(int oduID) {
		this.oduID = oduID;
	}

	public String getOduName() {
		return oduName;
	}

	public void setOduName(String oduName) {
		this.oduName = oduName;
	}
	
}
