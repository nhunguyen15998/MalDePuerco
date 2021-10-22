package controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class InvoiceDetailsController {
	@FXML
	private Label lblUser;
	@FXML
	private TableView tableInvoices;
	@FXML
	private TableColumn colOrder;
	@FXML
	private TableColumn colId;
	@FXML
	private TableColumn colSerName;
	@FXML
	private TableColumn colSize;
	@FXML
	private TableColumn colQuantity;
	@FXML
	private TableColumn colPrice;
	@FXML
	private TableColumn colTotal;
	@FXML
	private Label lblOrderCode;
	@FXML
	private Label lblTotal;
	@FXML
	private Label lblDate;
	@FXML
	private Button btnCancel;


	@FXML
	public void btnCancelAction(ActionEvent event) {
		// TODO Autogenerated
		btnCancel.getScene().getWindow().hide();
	}
}
