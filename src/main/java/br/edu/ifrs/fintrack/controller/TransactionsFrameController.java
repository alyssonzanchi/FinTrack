package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.CategoryDAO;
import br.edu.ifrs.fintrack.dao.TransactionDAO;
import br.edu.ifrs.fintrack.model.Category;
import br.edu.ifrs.fintrack.model.Transaction;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.util.LoggedUser;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    private YearMonth currentMonth;

    private void loadTable() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(transactionDAO.listByUserAndDueDate(LoggedUser.getUser().getId(), currentMonth));
        this.tblList.setItems(transactions);
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        User user = LoggedUser.getUser();
        Image profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImage())));
        profileImageView.setImage(profileImage);
        lblName.setText(user.getName());
        lblEmail.setText(user.getEmail());

        currentMonth = YearMonth.now();
        updateMonthLabel();

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
            String iconPath = transaction.getPaid() ? "/images/confirm.png" : "/images/clock.png";

            return new SimpleObjectProperty<>(new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath)))
            ));
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
                    String amountText;
                    if (item.compareTo(BigDecimal.ZERO) < 0) {
                        amountText = "R$ " + item.negate();
                        setStyle("-fx-text-fill: red; -fx-font-size: 18px;");
                    } else {
                        amountText = "R$ " + item;
                        setStyle("-fx-text-fill: green; -fx-font-size: 18px;");
                    }
                    setText(amountText);
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
        Main.loadView("RegisterTransactionFrame");
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
        currentMonth = currentMonth.minusMonths(1);
        updateMonthLabel();
        loadTable();
    }

    public void handleNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        updateMonthLabel();
        loadTable();
    }

    private void updateMonthLabel() {
        YearMonth now = YearMonth.now();
        int currentYear = now.getYear();
        int labelYear = currentMonth.getYear();


        if (labelYear == currentYear) {
            String monthName = capitalizeFirstLetter(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
            lblMonth.setText(monthName);
        } else {
            String shortMonthName = capitalizeFirstLetter(
                    currentMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault())
            );
            int yearSuffix = labelYear % 100;
            lblMonth.setText(shortMonthName + "/" + yearSuffix);
        }

        loadTable();
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
