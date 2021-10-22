package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/home.fxml")); //container
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();//get screen width, height
			Scene scene = new Scene(root, 1002, 600);
			scene.getStylesheets().add(getClass().getResource("/css/customer-home.css").toExternalForm());
			primaryStage.setX((screenBounds.getWidth() - 1000)/2);
			primaryStage.setY((screenBounds.getHeight() - 600)/2);
			primaryStage.setResizable(true);
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}