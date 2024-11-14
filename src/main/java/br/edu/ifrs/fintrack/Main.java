package br.edu.ifrs.fintrack;

import br.edu.ifrs.fintrack.controller.RegisterFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        loadView("LoginFrame");
    }

    public static void loadView(String view) {
        String frame = "/br/edu/ifrs/fintrack/views/" + view + ".fxml";

        Image icon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/images/icon.jpeg")));

        try {
            AnchorPane root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(frame)));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.getIcons().add(icon);
            stage.setTitle("FinTrack - Sistema de Gestão Financeira");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadView(String view, String email, String password) {
        String frame = "/br/edu/ifrs/fintrack/views/" + view + ".fxml";
        Image icon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/images/icon.jpeg")));

        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(frame)));
            AnchorPane root = loader.load();

            if (loader.getController() instanceof RegisterFrameController) {
                RegisterFrameController registerController = loader.getController();
                registerController.setData(email, password);
            }

            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.getIcons().add(icon);
            stage.setTitle("FinTrack - Sistema de Gestão Financeira");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}