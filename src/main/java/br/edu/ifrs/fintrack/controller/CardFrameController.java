package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.UserDAO;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class CardFrameController {
    @FXML
    private Label lblEmail;

    @FXML
    private Label lblName;

    @FXML
    private ImageView profileImageView;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        int userId = Session.getInstance().getUserId();
        User user = userDAO.get(userId);
        Image profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImage())));
        profileImageView.setImage(profileImage);
        lblName.setText(user.getName());
        lblEmail.setText(user.getEmail());
    }

    public void handleCards() {
        Main.loadView("CardsFrame");
    }

    public void handleCategories() {
        Main.loadView("CategoriesFrame");
    }

    public void handleAccounts() {
        Main.loadView("AccountsFrame");
    }

    public void handleDashboard() {
        Main.loadView("DashboardFrame");
    }

    public void handleTransactions() {
        Main.loadView("TransactionsFrame");
    }
}
