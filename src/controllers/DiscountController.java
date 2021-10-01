/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class DiscountController implements Initializable {

    @FXML
    private ImageView btnCancel;
    @FXML
    private TableView<?> tblDiscount;
    @FXML
    private TableColumn<?, ?> colNo;
    @FXML
    private TableColumn<?, ?> colCode;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colDescrip;
    @FXML
    private TableColumn<?, ?> colStartDate;
    @FXML
    private TableColumn<?, ?> colPeriod;
    @FXML
    private TableColumn<?, ?> colCreatedAt;
    @FXML
    private TableColumn<?, ?> colStatus;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfCode;
    @FXML
    private TextField tfDescript;
    @FXML
    private DatePicker dpStart;
    @FXML
    private TextField tfPeriod;
    @FXML
    private ComboBox<?> cbStatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnCancelAction(MouseEvent event) {
    }

    @FXML
    private void btnSaveAction(ActionEvent event) {
    }

    @FXML
    private void btnUpdateAction(ActionEvent event) {
    }
    
}
