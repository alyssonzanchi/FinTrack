package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.UserDAO;
import br.edu.ifrs.fintrack.exception.DatabaseConnectionException;
import br.edu.ifrs.fintrack.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class RegisterFrameController {

    @FXML
    public TextField txtName;

    @FXML
    public DatePicker Birthday;

    @FXML
    private Label lblChangeImage;

    @FXML
    private ImageView profileImageView;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        File file = new File("profile_images/user.png");
        Image defaultImage = new Image(file.toURI().toString());
        profileImageView.setImage(defaultImage);
    }

    public void setData(String email, String password) {
        txtEmail.setText(email);
        txtPassword.setText(password);
    }

    public void handleChangeImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione uma Imagem");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(lblChangeImage.getScene().getWindow());

        if (selectedFile != null) {
            try {
                File targetDirectory = new File("profile_images");

                Path targetPath = Path.of(targetDirectory.getAbsolutePath(), selectedFile.getName());
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image(targetPath.toUri().toString());
                profileImageView.setImage(image);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleRegister() {
        try {
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            String name = txtName.getText();
            LocalDate dateOfBirth = Birthday.getValue();
            String fullPath = profileImageView.getImage().getUrl();
            String image = fullPath.substring(fullPath.lastIndexOf("/") + 1);

            User user = new User(email, password, name, dateOfBirth, image);

            System.out.println(user.getPassword());

            boolean isInserted = userDAO.insert(user);

            if (isInserted) {
                showAlert(Alert.AlertType.INFORMATION, "Cadastro realizado", "Usuário cadastrado com sucesso!");
                Main.loadView("HomeFrame");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro no cadastro", "Não foi possível cadastrar o usuário.");
            }
        } catch (DatabaseConnectionException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de conexão", "Erro ao conectar com o banco de dados. Tente novamente.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

    public void handleBackLogin() {
        Main.loadView("LoginFrame");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}