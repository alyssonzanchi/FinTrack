package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.AccountDAO;
import br.edu.ifrs.fintrack.dao.CategoryDAO;
import br.edu.ifrs.fintrack.dao.TransactionDAO;
import br.edu.ifrs.fintrack.model.Category;
import br.edu.ifrs.fintrack.model.Transaction;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.util.LoggedUser;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TransactionsFrameController implements Initializable {

    @FXML
    private Label lblMonth;

    @FXML
    private TableColumn<Transaction, BigDecimal> clmAmount;

    @FXML
    private TableColumn<Transaction, String> clmName;

    @FXML
    private TableColumn<Transaction, ImageView> clmIcon;

    @FXML
    private TableView<Transaction> tblList;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblName;

    @FXML
    private ImageView profileImageView;

    private final TransactionDAO transactionDAO = new TransactionDAO();

    private void loadTable() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(transactionDAO.listByUser(LoggedUser.getUser().getId()));
        this.tblList.setItems(transactions);
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        User user = LoggedUser.getUser();
        Image profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImage())));
        profileImageView.setImage(profileImage);
        lblName.setText(user.getName());
        lblEmail.setText(user.getEmail());

        this.loadTable();

        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tblList.setRowFactory(_ -> {
            TableRow<Transaction> row = new TableRow<>();

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

        clmIcon.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            CategoryDAO categoryDAO = new CategoryDAO();

            Category category = categoryDAO.get(transaction.getCategory().getId());

            if (category != null) {
                return new SimpleObjectProperty<>(new ImageView(
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream(category.getIcon())))
                ));
            }
            return new SimpleObjectProperty<>(null);
        });

        clmIcon.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(ImageView icon, boolean empty) {
                super.updateItem(icon, empty);

                if (empty || icon == null) {
                    setGraphic(null);
                } else {
                    icon.setFitHeight(40);
                    icon.setFitWidth(40);
                    setGraphic(icon);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        clmAmount.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("R$ " + item);
                    setStyle("-fx-text-fill: black; -fx-font-size: 18px;");
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
                    setAlignment(Pos.CENTER_LEFT);
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

    public void handleNewCard() {
        Main.loadView("RegisterCardFrame");
    }

    public void handleEditTransaction() {
        Transaction transaction = this.tblList.getSelectionModel().getSelectedItem();
        if(transaction != null) {
            Main.addData("transactionSelected", transaction);
            Main.loadView("RegisterTransactionFrame");
        } else {
            rowNotSelected();
        }
    }

    public void handleRemoveTransaction() {
        Transaction transaction = this.tblList.getSelectionModel().getSelectedItem();
        if(transaction != null) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Transação");
            alert.setHeaderText("Deseja realmente apagar essa transação?");
            var res = alert.showAndWait();

            if(res.get() == ButtonType.OK) {
                transactionDAO.delete(transaction.getId());
                Main.loadView("TransactionsFrame");
            }
        }
    }

    private void rowNotSelected() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText("Transação não selecionada!");
        alert.setContentText("Você deve selecionar uma transação para completar essa ação!");
        alert.show();
    }

    public void handleBackMonth() {
    }

    public void handleNextMonth() {
    }
}
