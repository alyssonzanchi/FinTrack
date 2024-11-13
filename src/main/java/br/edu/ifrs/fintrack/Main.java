package br.edu.ifrs.fintrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        String loginFrame = "/br/edu/ifrs/fintrack/views/LoginFrame.fxml";

        Image icon = new Image(getClass().getResourceAsStream("/images/icon.jpeg"));
        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource(loginFrame));
            Scene scene = new Scene(root);
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("FinTrack - Sistema de Gestão Financeira");
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