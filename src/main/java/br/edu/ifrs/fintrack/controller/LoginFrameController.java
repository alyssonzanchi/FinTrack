package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.UserDAO;
import br.edu.ifrs.fintrack.exception.DatabaseConnectionException;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.util.EmailUtils;
import br.edu.ifrs.fintrack.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.util.List;
import java.util.Optional;

public class LoginFrameController {
    
    @FXML
    private TextField inputEmail;

    @FXML
    private PasswordField inputPassword;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {}

    @FXML
    void handleRegister() {
        String email = inputEmail.getText();
        String password = inputPassword.getText();

        if(email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, null, "Por favor, preencha todos os campos.");
            return;
        }

        if(!EmailUtils.isValidEmail(email)) {
            showAlert(Alert.AlertType.INFORMATION, null, "Este e-mail é inválido");
            return;
        }

        if(password.length() < 8) {
            showAlert(Alert.AlertType.INFORMATION, null, "A senha deve ter pelo menos 8 caracteres.");
            return;
        }

        Main.loadView("RegisterFrame", email, password);

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleLogin() {
        try {
            String email = inputEmail.getText();
            String password = inputPassword.getText();

            List<User> users = userDAO.list(Integer.MAX_VALUE, 0);
            Optional<User> userOpt = users.stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst();

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.validatePassword(password)) {
                    Session.getInstance().setUserId(user.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Login realizado", "Usuário autenticado com sucesso!");
                    Main.loadView("HomeFrame");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro de autenticação", "Email ou senha incorretos.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro de autenticação", "Usuário não encontrado.");
            }
        } catch (DatabaseConnectionException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de conexão", "Erro ao conectar com o banco de dados. Tente novamente.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
        }
    }
}