/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import db.MySQLJDBC;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class TestController implements Initializable {

    @FXML
    private Button open;
    @FXML
    private ImageView imgo;
    @FXML
    private Button close;
    @FXML
    private ImageView imgc;
    @FXML
    private VBox pane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         open.setVisible(true);
            open.setManaged(true);
            close.setVisible(false);
            close.setManaged(false);
            pane.setTranslateX(-223);
        
    }    

    @FXML
    private void ok() {
        Connection conn= MySQLJDBC.Instance().getConn();
        String sql="select comment from contacts";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
           ResultSet rs = ps.executeQuery();
            System.out.println(sql);
           while(rs.next()){
               System.out.println(rs.getString("comment"));
           }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void closeMenu(ActionEvent event) {
       
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(pane);
        slide.setToX(-223);
        slide.play();
        pane.setTranslateX(0);
        slide.setOnFinished((t) -> {
              open.setVisible(true);
            open.setManaged(true);
            close.setVisible(false);
            close.setManaged(false);
        });
    }

    @FXML
    private void openMenu(ActionEvent event) {
         
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(pane);
        slide.setToX(0);
        slide.play();
        pane.setTranslateX(-223);
        slide.setOnFinished((t) -> {
            open.setVisible(false);
            open.setManaged(false);
            close.setVisible(true);
            close.setManaged(true);
           
        });
    }
    
}
