package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.AccountDAO;
import br.edu.ifrs.fintrack.dao.UserDAO;
import br.edu.ifrs.fintrack.model.Account;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AccountsFrameController implements Initializable {

    @FXML
    private TableColumn<Account, BigDecimal> clmBalance;

    @FXML
    private TableColumn<Account, String> clmIcon;

    @FXML
    private TableColumn<Account, String> clmName;

    @FXML
    private TableView<Account> tblList;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblName;

    @FXML
    private ImageView profileImageView;

    private final UserDAO userDAO = new UserDAO();

    private final AccountDAO accountDAO = new AccountDAO();

    public List<Account> listAccountsForUser() {
        List<Account> allAccounts = accountDAO.list(100, 0);
        return allAccounts.stream()
                .filter(account -> account.getUserId() == Session.getInstance().getUserId())
                .collect(Collectors.toList());
    }

    private void loadTable() {
        ObservableList<Account> accounts = FXCollections.observableArrayList(listAccountsForUser());
        this.tblList.setItems(accounts);
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        int userId = Session.getInstance().getUserId();
        User user = userDAO.get(userId);
//        Image profileImage = new Image(user.getImage());
//        profileImageView.setImage(profileImage);
        lblName.setText(user.getName());
        lblEmail.setText(user.getEmail());

        this.loadTable();

        clmIcon.setCellValueFactory(new PropertyValueFactory<>("icon"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));

        tblList.setRowFactory(_ -> {
            TableRow<Account> row = new TableRow<>();

            row.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-insets: 5; -fx-background-insets: 5;");

            row.selectedProperty().addListener((_, _, isNowSelected) -> {
                if (isNowSelected) {
                    row.setStyle("-fx-background-color: lightgray; -fx-padding: 10;  -fx-border-insets: 5; -fx-background-insets: 5;");
                } else {
                    row.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-insets: 5; -fx-background-insets: 5;");
                }
            });

            return row;
        });

        clmIcon.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(item))));
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(40);
                    setGraphic(imageView);
                }
            }
        });

        clmBalance.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("R$ " + item);
                    if (item.compareTo(BigDecimal.ZERO) < 0) {
                        setStyle("-fx-text-fill: red; -fx-font-size: 18px;");
                    } else {
                        setStyle("-fx-text-fill: green; -fx-font-size: 18px;");
                    }
                }
            }
        });

        clmName.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-size: 18px;");
                }
            }
        });
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

    public void handleNewAccount() {
        Main.loadView("RegisterAccountFrame");
    }

    public void handleEditAccount() {
        Account account = this.tblList.getSelectionModel().getSelectedItem();
        if(account != null) {
            Main.addData("accountSelected", account);
            Main.loadView("RegisterAccountFrame");
        } else {
            rowNotSelected();
        }
    }

    public void handleRemoveAccount() {
        Account account = this.tblList.getSelectionModel().getSelectedItem();
        if(account != null) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Conta");
            alert.setHeaderText("Deseja realmente apagar essa conta?");
            var res = alert.showAndWait();

            if(res.get() == ButtonType.OK) {
                accountDAO.delete(account.getId());
                Main.loadView("AccountsFrame");
            }
        } else {
            rowNotSelected();
        }
    }

    private void rowNotSelected() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText("Conta não selecionada!");
        alert.setContentText("Você deve selecionar uma conta para completar essa ação!");
        alert.show();
    }
}
