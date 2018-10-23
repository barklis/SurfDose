package application;
	
import GUI.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

// Starting class
public class StartingClass extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		GUI.instance(primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}