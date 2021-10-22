package controllers;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NotificationController implements Initializable{
	
	//xml
	@FXML
	private AnchorPane apNotiAlert;
	@FXML
	private Label lblDateTime;
	@FXML
	private Text txtMessage;	
	@FXML
	private ImageView ivThumbnail;
	@FXML
	private Pane paneStatus;
	@FXML
	private Label lblStatus;
	@FXML
	private Button btnOK;
	
	private static final Integer countDown = 5;
	private Integer interval = countDown;
    private Timer timer;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		interval = countDown;
		timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	            if(interval > 0){
	                Platform.runLater(() -> btnOK.setText("OK ("+interval+")"));
	                interval--;
	            }else {
	            	close();
	                timer.cancel();
	            }
	        }
	    }, 1000,1000);
	}
	
	//load msg
	public void processMessages(String role, String type, String table, String message, String user) {
		txtMessage.setText(message);
	}
	
	//btnOKAction
	public void btnOKAction() {
		this.close();
//		NotificationController.customerHomeController.customerMasterHolder.setDisable(false);
	}
	
	public void close() {
		Platform.runLater(()-> {
			Stage stage = (Stage) btnOK.getScene().getWindow();
			stage.close();
		});
		
	}	
	
}
