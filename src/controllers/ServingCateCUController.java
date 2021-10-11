package controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.ServingCategoryModel;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ServingCateCUController implements Initializable{
		private ServingCategoryModel sercateModel = new ServingCategoryModel();
		private ServingCateController sercateController;
		private int sercateId;
	
	 	@FXML
	    private TextField tfName;

	    @FXML
	    private ComboBox<DataMapping> cbParent;

	    @FXML
	    private ComboBox<DataMapping> cbStatus;

	    @FXML
	    private Button btnCancel;

	    @FXML
	    private Label lblNameError;

	    @FXML private Label lbl;
	    @FXML
	    private ImageView imageView;
	    private Image image;
	    private static String path = "";
	    private FileChooser fileChooser;
	    private File file;
	    private Desktop desktop = Desktop.getDesktop();
	    
	    @FXML private AnchorPane anchorPane;
	    private Stage stage;
	    private FileInputStream fis;
	    
	    //validated
	    public boolean validated(String name) {
	    	try {
	    		lblNameError.setText("");
	    		ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
	    		data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:5"));
	    		
	    		ArrayList<DataMapping> mess = Validations.validated(data);
	    		if(mess.size() > 0) {
	    			for(DataMapping message : mess) {
	    				switch(message.key) {
	    					case "lblNameError":
	    						lblNameError.setText(message.key);
	    						break;
	    					default:
	    						System.out.println("validation");
	    				}
	    			} return false;
	    		}
	    		return true;
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		return false;
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
	    void btnSaveAction(ActionEvent event) {
	    	try {
	    		String name = tfName.getText();
	    		String parent = cbParent.getValue() != null ? cbParent.getValue().key : null;
	    		String status = cbStatus.getValue() != null ? cbStatus.getValue().key : String.valueOf(ServingCategoryModel.SERVING_CATEGORY_ACTIVATED);
	    		
	    		if(validated(name)) {
	    			ArrayList<DataMapping> servingcate = new ArrayList<DataMapping>();
	    			servingcate.add(DataMapping.getInstance("name", name));
	    			servingcate.add(DataMapping.getInstance("parent_id", parent));
	    			servingcate.add(DataMapping.getInstance("status", status));
	    			servingcate.add(DataMapping.getInstance("thumbnail", path));
	    			if(sercateId == 0) {
	    				sercateModel.createServingCategory(servingcate);
	    				Helpers.status("success");
	    			} else {
	    				Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Update User Confirmation");
						alert.setHeaderText("Do you want to make this change?");
						Optional<ButtonType> option = alert.showAndWait();
						if (option.get() == ButtonType.OK) {
							sercateModel.updateServingCategory(sercateId, servingcate);
							Helpers.status("success");
						}
	    			}
	    			sercateController.loadData(null);
	    			this.close();
	    		}
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			
			this.getParentList();
			ObservableList<DataMapping> status = FXCollections.observableArrayList(ServingCategoryModel.isActivated, ServingCategoryModel.isDeactivated);
			cbStatus.setItems(status);
			
			fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif")
					);
		}
		
		//upload
				public void upload() throws FileNotFoundException {
//					stage = (Stage) anchorPane.getScene().getWindow();
//					file = fileChooser.showOpenDialog(stage);    
//					if(file != null) {
//						System.out.println(""+file.getAbsolutePath());
//						try {
//							image = new Image(file.getAbsoluteFile().toURL().toString(), 200, 170, false, false);
//							imageView.setImage(image);
//						} catch (MalformedURLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
					stage = new Stage();
					fileChooser = new FileChooser();
					fileChooser.getExtensionFilters().addAll(
							new FileChooser.ExtensionFilter("IMAGE FILES", "*.jpg", "*.png", "*.gif")
							);
					file = fileChooser.showOpenDialog(stage);
					String newPath = "/asset/";
					File directory = new File(newPath);
					if(file != null) {
						
						System.out.println("path:"+directory);
						Image im = new Image(new FileInputStream(newPath), 200, 170, false, false);
						imageView.setImage(im);
					} else {
						System.out.println("error");
					}
				}
		
		//load ccb parent
		public ResultSet getParentList() {
			try {
				ArrayList<DataMapping> parentOptions = new ArrayList<DataMapping>();
				ResultSet parent = sercateModel.getServingCategoryList(null);
				while(parent.next()) {
					parentOptions.add(DataMapping.getInstance(parent.getInt("id"), parent.getString("name")));
				}
				cbParent.getItems().setAll(parentOptions);
				return parent;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		//load data
		public void loadDataById(ServingCateController sercateController) {
			try {
				this.sercateController = sercateController;
				this.sercateId = sercateController.getServingcateId();
				lbl.setText("Create Serving Category");
				System.out.println("id: "+sercateId);
				if(this.sercateId != 0) {
					lbl.setText("Update Serving Category");
					ResultSet currentSC = this.sercateModel.getSerCateById(sercateId);
					if(currentSC.next()) {
						System.out.print(currentSC.getString("name"));
						tfName.setText(currentSC.getString("name"));
						
						//load ccb parent, status
						for(DataMapping cParent :  cbParent.getItems()) {
							if(cParent.key != null && Integer.parseInt(cParent.key) == currentSC.getInt("parent_id"));
							cbParent.setValue(cParent);
							break;
						}
						
						for(DataMapping status : cbStatus.getItems()) {
							if(status.key != null && Integer.parseInt(status.key) == currentSC.getInt("status")) {
								cbStatus.setValue(status);
								break;
							}
						}//m len db i
						String paths = currentSC.getString("thumbnail").replaceAll("'\'", "/");
						System.out.println("path "+paths);
						if(paths == null || paths.isEmpty()) {
							Image img = new Image("com/myfx/demofx1/asset/user_image.png");
							imageView.setImage(img);
						} else {
							path = paths;
							Image im = new Image(new FileInputStream(path), 200, 170, false, false);
							imageView.setImage(im);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
