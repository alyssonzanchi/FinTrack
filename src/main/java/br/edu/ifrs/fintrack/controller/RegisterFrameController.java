package br.edu.ifrs.fintrack.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class RegisterFrameController {

    @FXML
    private ImageView profileImageView;

    @FXML
    private Button selectImageButton;

    @FXML
    public void initialize() {
        File file = new File("profile_images/user.png");
        Image defaultImage = new Image(file.toURI().toString());
        profileImageView.setImage(defaultImage);

        selectImageButton.setOnAction(_ -> selectImage());
    }

    public void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione uma Imagem");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());

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

    public void handleChangeImage(MouseEvent mouseEvent) {
    }
}