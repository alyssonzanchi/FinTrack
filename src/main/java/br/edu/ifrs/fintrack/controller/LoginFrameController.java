package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.util.EmailUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class LoginFrameController {
    
    @FXML
    private TextField inputEmail;

    @FXML
    private PasswordField inputPassword;

    @FXML
    public void initialize() {}

    @FXML
    void handleRegister(ActionEvent event) {
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

        Main.loadView("RegisterFrame", email, password);

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}