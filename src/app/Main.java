package app;

import java.util.ArrayList;

import db.MySQLJDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ServingCategoryModel;
import utils.DataMapping;

public class Main extends Application{
//
//	public static void main(String[] args) {
//		launch(args);
//	}
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/orderWaiter.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setWidth(850.0);
			primaryStage.setHeight(600.0);
			primaryStage.setResizable(true);
			primaryStage.setScene(scene);
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			//primaryStage.toFront();
//			primaryStage.setFullScreen(true);
//			primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
