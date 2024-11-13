package br.edu.ifrs.fintrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        String loginFrame = "/br/edu/ifrs/fintrack/views/LoginFrame.fxml";
        try {
            VBox root = FXMLLoader.load(getClass().getResource(loginFrame));
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Hello!");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}