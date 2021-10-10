package controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import models.ItemModel;
import models.ReservationModel;
import utils.DataMapping;

public class ItemController {
	@FXML
	private Label lblTime;
	@FXML
	private Label lblTable;
	@FXML
	private Label lblCusName;
	@FXML
	private Label lblStatus;
	@FXML
	private AnchorPane anchor;
	private ItemModel item;
	public void setData(ItemModel items) {
		this.item = items;
		lblTime.setText(item.getStart()+" - "+items.getEnd());
		lblTable.setText(item.getTable()+"");
		lblCusName.setText(items.getCusName());
		DataMapping status=ReservationModel.isCancelled;
		if(items.getStatus()==1) {
			status=ReservationModel.isNew;
			lblStatus.getStyleClass().add("new");
			anchor.getStyleClass().add("hboxNew");
			
		}else if(items.getStatus()==2) {
			status=ReservationModel.isConfirmed;
			lblStatus.getStyleClass().add("confirm");
			anchor.getStyleClass().add("hboxConfirm");
		}else if(items.getStatus()==3) {
			status=ReservationModel.isDeposited;
			lblStatus.getStyleClass().add("deposit");
			anchor.getStyleClass().add("hboxDeposit");
		}else if(items.getStatus()==4) {
			status=ReservationModel.isPresent;
			lblStatus.getStyleClass().add("present");
			anchor.getStyleClass().add("hboxPresent");
		}else if(items.getStatus()==5) {
			status=ReservationModel.isExpried;
			lblStatus.getStyleClass().add("expired");
			anchor.getStyleClass().add("hboxExpired");
		}
		lblStatus.setText(status+"");
	}

}
