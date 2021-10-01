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

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class TablesController implements Initializable {

    @FXML
    private TableView<?> tblTables;
    @FXML
    private TableColumn<?, ?> colNo;
    @FXML
    private TableColumn<?, ?> colID;
    @FXML
    private TableColumn<?, ?> colCode;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colSeats;
    @FXML
    private TableColumn<?, ?> colCreated;
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
