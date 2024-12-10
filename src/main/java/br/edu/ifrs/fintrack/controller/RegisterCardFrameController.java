package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.AccountDAO;
import br.edu.ifrs.fintrack.dao.CreditCardDAO;
import br.edu.ifrs.fintrack.model.Account;
import br.edu.ifrs.fintrack.model.CreditCard;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.util.LoggedUser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class RegisterCardFrameController {
    @FXML
    private ComboBox<Account> boxAccount;

    @FXML
    private ComboBox<String> boxClosing;

    @FXML
    private ComboBox<String> boxPayment;

    @FXML
    private TextField lblLimit;

    @FXML
    private TextField lblNameCard;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblName;

    @FXML
    private ImageView profileImageView;

    private final CreditCardDAO creditCardDAO = new CreditCardDAO();

    private CreditCard creditCard;

    private final AccountDAO accountDAO = new AccountDAO();

    @FXML
    public void initialize() {
        User user = LoggedUser.getUser();
        Image profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImage())));
        profileImageView.setImage(profileImage);
        lblName.setText(user.getName());
        lblEmail.setText(user.getEmail());

        boxClosing.setItems(FXCollections.observableArrayList(
                "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12", "13", "14", "15", "16", "17", "18",
                "19", "20", "21", "22", "23", "24", "25", "26", "27",
                "28", "29", "30", "31"
        ));

        boxPayment.setItems(FXCollections.observableArrayList(
                "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12", "13", "14", "15", "16", "17", "18",
                "19", "20", "21", "22", "23", "24", "25", "26", "27",
                "28", "29", "30", "31"
        ));

        List<Account> accounts = accountDAO.listByUser(LoggedUser.getUser().getId());

        boxAccount.getItems().addAll(accounts);

        boxAccount.setCellFactory(_ -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(item.getIcon()))));
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);

                    setGraphic(imageView);
                    setText(item.getName());
                }
            }
        });

        boxAccount.setButtonCell(new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(item.getIcon()))));
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);

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

    public void handleSaveCard() {
        if(this.isEditing()) {
            this.creditCard.setName(this.lblNameCard.getText());
            this.creditCard.setLimit(new BigDecimal(this.lblLimit.getText()));
            this.creditCard.setIcon(boxAccount.getValue().getIcon());
            this.creditCard.setClosing(Integer.parseInt(this.boxClosing.getValue()));
            this.creditCard.setPayment(Integer.parseInt(this.boxPayment.getValue()));
            this.creditCard.setAccount(boxAccount.getValue());

            creditCardDAO.update(this.creditCard);
        } else {
            BigDecimal limit = new BigDecimal(this.lblLimit.getText());
            Account account = this.boxAccount.getValue();
            User user = LoggedUser.getUser();
            int closing = Integer.parseInt(this.boxClosing.getValue());
            int payment = Integer.parseInt(this.boxPayment.getValue());
            CreditCard creditCard = new CreditCard(this.lblNameCard.getText(), account.getIcon(), limit, closing, payment, user, account);
            creditCardDAO.insert(creditCard);
        }

        Main.loadView("CardsFrame");
    }

    @FXML
    public void handleBackPage() {
        Main.loadView("CardsFrame");
    }

    private void checkScreenEditing() {
        this.creditCard = Main.getAndRemoveData("cardSelected");
        if(this.isEditing()) {
            this.lblNameCard.setText(this.creditCard.getName());
            this.lblLimit.setText(this.creditCard.getLimit().toString());
            this.boxAccount.getItems().stream()
                    .filter(account -> account.equals(this.creditCard.getAccount()))
                    .findFirst()
                    .ifPresent(this.boxAccount::setValue);
            this.boxClosing.setValue(String.valueOf(this.creditCard.getClosing()));
            this.boxPayment.setValue(String.valueOf(this.creditCard.getPayment()));
        }
    }
    private boolean isEditing() {
        return this.creditCard!=null;
    }
}
