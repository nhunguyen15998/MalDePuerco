package controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import models.ItemModel;
import models.ItemSupportModel;
import models.ReservationModel;
import utils.DataMapping;

public class ItemSupportController {
	@FXML
	private Label lblName;
	@FXML
	private Label lblTime;
	private ItemSupportModel item;
	public void setData(ItemSupportModel items) {
		this.item = items;
		lblTime.setText(item.getTime());
		lblName.setText(item.getTableName());
		

}
}