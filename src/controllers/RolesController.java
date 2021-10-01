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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class RolesController implements Initializable {

    @FXML
    private AnchorPane roleHolder;
    @FXML
    private TableView<?> tableRole;
    @FXML
    private TableColumn<?, ?> colOrder;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colCode;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colCreatedAt;
    @FXML
    private TableColumn<?, ?> colStatus;
    @FXML
    private TextField tfRole;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void getTableDataByClick(MouseEvent event) {
    }

    @FXML
    private void btnPermissionAction(ActionEvent event) {
    }

    @FXML
    private void btnCreateAction(ActionEvent event) {
    }

    @FXML
    private void btnDeleteAction(ActionEvent event) {
    }

    @FXML
    private void btnUpdateAction(ActionEvent event) {
    }

    @FXML
    private void btnClearAction(ActionEvent event) {
    }

    @FXML
    private void onSearch(KeyEvent event) {
    }
    
}
