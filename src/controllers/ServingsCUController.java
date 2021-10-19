package controllers;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.ServingCategoryModel;
import models.ServingModel;
import utils.DataMapping;
import utils.Helpers;
import utils.ValidationDataMapping;
import utils.Validations;

public class ServingsCUController implements Initializable{
	private ServingModel serModel = new ServingModel();
	public ServingsController serController;
	private int servingID;
	private ServingCategoryModel sercateModel = new ServingCategoryModel();
	
	@FXML
    private Label lblServing;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfDes;

    @FXML
    private TextField tfPrice;

    @FXML
    private ComboBox<DataMapping> cbCate;

    @FXML
    private ComboBox<DataMapping> cbStatus;
    
    @FXML private TextField tfQuantity;
    @FXML Label lblQuantityError;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lblNameError;

    @FXML
    private Label lblCategoryError;

    @FXML
    private Label lblDesError;

    @FXML
    private Label lblPriceError;
    
    @FXML private TextField tfImage;
    
    @FXML
    private ComboBox<DataMapping> cbType;

    @FXML
    private Label lblTypeError;
    
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
	
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			getCateList();
			ObservableList<DataMapping> status = FXCollections.observableArrayList(ServingModel.isActivated, ServingModel.isDeactivated);
			cbStatus.setItems(status);
			
			ObservableList<DataMapping> type = FXCollections.observableArrayList(ServingModel.isFood, ServingModel.isHotDrink, ServingModel.isColdDrink);
			cbType.setItems(type);
			
			fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif")
					);
		}
		
		//validate
		public boolean validated(String name, String category, String descriptions, String price, String quantity, String type) {
			try {
				lblNameError.setText("");
				lblCategoryError.setText("");
				lblDesError.setText("");
				lblPriceError.setText("");
				lblQuantityError.setText("");
				lblTypeError.setText("");
				
				ArrayList<ValidationDataMapping> data = new ArrayList<ValidationDataMapping>();
				data.add(new ValidationDataMapping("name", name, "lblNameError", "required|string|min:5"));
				data.add(new ValidationDataMapping("category", category, "lblCategoryError", "required"));
				data.add(new ValidationDataMapping("descriptions", descriptions, "lblDesError", "required|string|min:20"));
				data.add(new ValidationDataMapping("price", price, "lblPriceError", "required"));
				data.add(new ValidationDataMapping("quantity", quantity, "lblQuantityError", "required|numberic"));
				data.add(new ValidationDataMapping("type", type, "lblTypeError", "required"));
				
				ArrayList<DataMapping> message = Validations.validated(data);
				if(message.size() > 0) {
					for(DataMapping mess : message) {
						switch(mess.key) {
						case "lblNameError":
							lblNameError.setText(mess.value);
							break;
						case "lblCategoryError":
							lblCategoryError.setText(mess.value);
							break;
						case "lblDesError":
							lblDesError.setText(mess.value);
							break;
						case "lblPriceError":
							lblPriceError.setText(mess.value);
							break;
						case "lblQuantityError":
							lblQuantityError.setText(mess.value);
							break;
						default:
							System.out.println("message");
						}
					}
					return false;
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		//upload
		public void upload() throws FileNotFoundException {
			stage = new Stage();
			fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("IMAGE FILES", "*.jpg", "*.png", "*.gif")
					);
			file = fileChooser.showOpenDialog(stage);    
			if(file != null) {
				path = file.getPath();
				System.out.println("path:" +path);
				Image im = new Image(new FileInputStream(path), 200, 170, false, false);
				imageView.setImage(im);
				} 
			else {
				System.out.println("error path");
			}
		}
		
		
		
		//save
		public void btnSaveAction() {
			try {
				String name = tfName.getText();
				String cate = cbCate.getValue() != null ? cbCate.getValue().key : null;
				String des = tfDes.getText();
				String price = tfPrice.getText();
				String quantity = tfQuantity.getText();
				String type = cbType.getValue() != null ? cbType.getValue().key : null;
				
				if(validated(name, cate, des, price, quantity, type)) {
					ArrayList<DataMapping> servings = new ArrayList<DataMapping>();
					servings.add(DataMapping.getInstance("name", name));
					servings.add(DataMapping.getInstance("category_id", cate));
					servings.add(DataMapping.getInstance("descriptions", des));
					servings.add(DataMapping.getInstance("price", price));
					servings.add(DataMapping.getInstance("quantity", quantity));
					servings.add(DataMapping.getInstance("thumbnail", path));
					servings.add(DataMapping.getInstance("type", type));
					
					
					if(this.servingID == 0) {
						serModel.createServing(servings);
						Helpers.status("success");
					} else {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Update User Confirmation");
						alert.setHeaderText("Do you want to make this change?");
						Optional<ButtonType> option = alert.showAndWait();
						if (option.get() == ButtonType.OK) {
							serModel.updateServing(this.servingID, servings);
							Helpers.status("success");

						}
					}
					serController.loadData(null);
					this.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//load cate 
		public ResultSet getCateList() {
			try {
				ArrayList<DataMapping> cateOptions = new ArrayList<DataMapping>();
				ResultSet category = sercateModel.getServingCategoryList(null);
				while(category.next()) {
					cateOptions.add(DataMapping.getInstance(category.getInt("id"), category.getString("name")));
				}
				cbCate.getItems().setAll(cateOptions);
				return category;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		//cancel
		public void btnCancelAction() {
			try {
				this.close();
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
		
		//load data
		public void loadDataById(ServingsController serController) {
			try {
				this.serController = serController;
				this.servingID = serController.getServingId();
				lblServing.setText("Create Serving");
				if(this.servingID != 0) {
					lblServing.setText("Update Serving");
					ResultSet currentSer = this.serModel.getSerById(this.servingID);
					ArrayList<DataMapping> re = new ArrayList<DataMapping>();
					re.add(DataMapping.getInstance("path", path));
					if(currentSer.next()) {
						tfName.setText(currentSer.getString("name"));
						tfDes.setText(currentSer.getString("descriptions"));
						tfPrice.setText(currentSer.getString("price"));
						tfQuantity.setText(currentSer.getString("quantity"));
						
						//load ccb cate, status
						for(DataMapping cate : cbCate.getItems()) {
							if(cate.key != null && Integer.parseInt(cate.key) == currentSer.getInt("category_id")) {
								cbCate.setValue(cate);
								break;
							}
						}
						
						for(DataMapping type : cbType.getItems()) {
							if(type.key != null && Integer.parseInt(type.key) == currentSer.getInt("type")) {
								cbType.setValue(type);
								break;
							}
						}
						
						for(DataMapping status : cbStatus.getItems()) {
							if(status.key != null && Integer.parseInt(status.key) == currentSer.getInt("status")) {
								cbStatus.setValue(status);
								break;
							}
						}
						
						String paths = currentSer.getString("thumbnail").replaceAll("'\'", "/");
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
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
}
