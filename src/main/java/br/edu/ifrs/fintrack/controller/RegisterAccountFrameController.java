package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.AccountDAO;
import br.edu.ifrs.fintrack.dao.UserDAO;
import br.edu.ifrs.fintrack.model.Account;
import br.edu.ifrs.fintrack.model.Banks;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.util.Session;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.math.BigDecimal;
import java.util.Objects;

public class RegisterAccountFrameController {
    @FXML
    private ComboBox<Banks> boxIcon;

    @FXML
    private TextField lblBalance;

    @FXML
    private TextField lblNameAccount;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblName;

    @FXML
    private ImageView profileImageView;

    private final UserDAO userDAO = new UserDAO();

    private final AccountDAO accountDAO = new AccountDAO();

    private Account account;

    @FXML
    public void initialize() {
        int userId = Session.getInstance().getUserId();
        User user = userDAO.get(userId);
        Image profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImage())));
        profileImageView.setImage(profileImage);
        lblName.setText(user.getName());
        lblEmail.setText(user.getEmail());

        boxIcon.getItems().addAll(Banks.values());

        boxIcon.setCellFactory(_ -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Banks item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(item.getPath()))));
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);

                    setGraphic(imageView);
                    setText(item.getName());
                }
            }
        });

        boxIcon.setButtonCell(new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Banks item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(item.getPath()))));
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(40);

                    setGraphic(imageView);
                    setText(item.getName());
                }
            }
        });

        Platform.runLater(this::checkScreenEditing);
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

    public void handleSaveAccount() {
        if(this.isEditing()) {
            this.account.setName(this.lblNameAccount.getText());

            try {
                BigDecimal balance = new BigDecimal(this.lblBalance.getText());
                this.account.setBalance(balance);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido para o saldo: " + this.lblBalance.getText());
            }

            Banks selectedBank = this.boxIcon.getValue();
            if (selectedBank != null) {
                this.account.setIcon(selectedBank.getPath());
            }

            accountDAO.update(this.account);
        } else {
            BigDecimal balance = new BigDecimal(this.lblBalance.getText());
            Banks icon = this.boxIcon.getValue();
            User user = userDAO.get(Session.getInstance().getUserId());
            Account acc = new Account(this.lblNameAccount.getText(), balance, icon.getPath(), user);
            accountDAO.insert(acc);
        }

        Main.loadView("AccountsFrame");
    }

    @FXML
    public void handleBackPage() {
        Main.loadView("AccountsFrame");
    }

    private void checkScreenEditing() {
        this.account = Main.getAndRemoveData("accountSelected");
        if(this.isEditing()) {
            this.lblNameAccount.setText(this.account.getName());
            this.lblBalance.setText(this.account.getBalance().toString());
            this.boxIcon.setValue(Banks.fromName(this.account.getIcon()));
        }
    }
    private boolean isEditing() {
        return this.account!=null;
    }
}
