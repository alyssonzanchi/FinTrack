package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.util.EmailUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFrameController {
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private TextField inputEmail;

    @FXML
    private PasswordField inputPassword;

    @FXML
    private Label lblPassword;

    @FXML
    public void initialize() {
        btnRegister.setOnAction(_ -> registerUser());
    }

    void registerUser() {
        String email = inputEmail.getText();
        String password = inputPassword.getText();

        if(email.isEmpty() || password.isEmpty()) {
            showAlert("Por favor, preencha todos os campos.");
            return;
        }

        if(!EmailUtils.isValidEmail(email)) {
            showAlert("Este e-mail é inválido");
            return;
        }

        if(password.length() < 8) {
            showAlert("A senha deve ter pelo menos 8 caracteres.");
            return;
        }

        openHomeScreen(email, password);
    }

    private void openHomeScreen(String email, String password) {
        String registerFrame = "/br/edu/ifrs/fintrack/views/RegisterFrame.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(registerFrame));
            Parent root = loader.load();

            Scene registerScene = new Scene(root);
            Stage registerStage = new Stage();
            registerStage.setScene(registerScene);
            registerStage.setTitle("FinTrack - Sistema de Gestão Financeira");
            registerStage.show();

            Stage currentStage = (Stage) btnRegister.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showAlert("Não foi possível abrir a tela de registro: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}