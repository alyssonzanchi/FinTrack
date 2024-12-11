package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.AccountDAO;
import br.edu.ifrs.fintrack.dao.CategoryDAO;
import br.edu.ifrs.fintrack.dao.TransactionDAO;
import br.edu.ifrs.fintrack.model.*;
import br.edu.ifrs.fintrack.util.LoggedUser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegisterTransactionFrameController {

    @FXML
    public TextField lblNameTransaction;

    @FXML
    public TextField lblAmount;

    @FXML
    public CheckBox checkPaid;

    @FXML
    public ToggleGroup toggleGroupType;

    @FXML
    public RadioButton radioExpense;

    @FXML
    public RadioButton radioIncome;

    @FXML
    private ToggleGroup recurring;

    @FXML
    public RadioButton radioFalse;

    @FXML
    public RadioButton radioInstallment;

    @FXML
    public RadioButton radioFixed;

    @FXML
    public TextField txtNumber;

    @FXML
    public TextField txtCount;

    @FXML
    public ComboBox<String> boxFrequency;

    @FXML
    public DatePicker pickDueDate;

    @FXML
    public ComboBox<Category> boxCategory;

    @FXML
    public DatePicker pickPaymentDate;

    @FXML
    public Label lblNumber;

    @FXML
    public Label lblCount;

    @FXML
    public Label lblFrequency;

    @FXML
    public Label lblPaymentDate;

    @FXML
    private ComboBox<Account> boxAccount;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblName;

    @FXML
    private ImageView profileImageView;

    private final CategoryDAO categoryDAO = new CategoryDAO();

    private final TransactionDAO transactionDAO = new TransactionDAO();

    private Transaction transaction;

    private final AccountDAO accountDAO = new AccountDAO();

    @FXML
    public void initialize() {
        User user = LoggedUser.getUser();
        Image profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImage())));
        profileImageView.setImage(profileImage);
        lblName.setText(user.getName());
        lblEmail.setText(user.getEmail());

        toggleGroupType.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
                    if (newToggle != null) {
                        String selectedType = ((RadioButton) newToggle).getText();

                        List<Category> allCategories = categoryDAO.list(100, 0);

                        List<Category> filteredCategories = allCategories.stream()
                                .filter(category -> category.getType().equalsIgnoreCase(selectedType))
                                .collect(Collectors.toList());

                        boxCategory.getItems().clear();
                        boxCategory.getItems().addAll(filteredCategories);
                        boxCategory.setCellFactory(listView -> {
                            return new ListCell<Category>() {
                                private final ImageView imageView = new ImageView();
                                @Override
                                protected void updateItem(Category category, boolean empty) {
                                    super.updateItem(category, empty);
                                    if (empty || category == null) {
                                        setText(null);
                                        setGraphic(null);
                                    } else {
                                        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(category.getIcon()))));
                                        imageView.setFitHeight(30);
                                        imageView.setFitWidth(30);

                                        setGraphic(imageView);
                                        setText(category.getName());
                                    }
                                }
                            };
                        });
                    }
                });

        radioInstallment.setOnAction(event -> {
            if (radioInstallment.isSelected()) {
                boxFrequency.setDisable(false);
                lblFrequency.setDisable(false);
                txtCount.setDisable(false);
                lblCount.setDisable(false);
                txtNumber.setDisable(false);
                lblNumber.setDisable(false);
            }
        });

        radioFixed.setOnAction(event -> {
            if (radioFixed.isSelected()) {
                boxFrequency.setDisable(false);
                lblFrequency.setDisable(false);
                txtCount.setDisable(true);
                lblCount.setDisable(true);
                txtNumber.setDisable(true);
                lblNumber.setDisable(true);
            }
        });

        checkPaid.setOnAction(event -> {
            if (checkPaid.isSelected()) {
                pickPaymentDate.setDisable(false);
                lblPaymentDate.setDisable(false);
            } else {
                pickPaymentDate.setDisable(true);
                lblPaymentDate.setDisable(true);
            }
        });

        boxFrequency.setItems(FXCollections.observableArrayList(
                "Mensal", "Diário", "Semanal", "Trimestral", "Anual"
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

    public void handleSaveTransaction() {
        User user = LoggedUser.getUser();
        if(this.isEditing()) {
            this.transaction.setName(this.lblName.getText());
            this.transaction.setAmount(new BigDecimal(this.lblAmount.getText()));
            this.transaction.setPaid(this.checkPaid.isSelected());

            if (this.radioIncome.isSelected()) {
                this.transaction.setType("RECEITA");
            } else if (this.radioExpense.isSelected()) {
                this.transaction.setType("DESPESA");
            }

            if (this.radioFalse.isSelected()) {
                this.transaction.setRecurring(null);
            } else if (this.radioFixed.isSelected()) {
                this.transaction.setRecurring("FIXA");
                this.transaction.setFixedFrequency(this.boxFrequency.getValue());
            } else if (this.radioInstallment.isSelected()) {
                this.transaction.setRecurring("PARCELADA");
                this.transaction.setInstallmentFrequency(this.boxFrequency.getValue());
                this.transaction.setInstallmentNumber(
                        this.lblNumber.getText().isEmpty() ? null : Integer.parseInt(this.lblNumber.getText())
                );
                this.transaction.setInstallmentCount(
                        this.lblCount.getText().isEmpty() ? null : Integer.parseInt(this.lblCount.getText())
                );
            }

            this.transaction.setDueDate(this.pickDueDate.getValue());
            this.transaction.setPaymentDate(this.pickPaymentDate.getValue());
            this.transaction.setCategory(this.boxCategory.getValue());
            this.transaction.setAccount(this.boxAccount.getValue());
            this.transaction.setUser(user);

            transactionDAO.update(this.transaction);
        } else {
            BigDecimal amount = new BigDecimal(this.lblAmount.getText());
            Account account = this.boxAccount.getValue();
            String type = this.radioIncome.isSelected() ? "RECEITA" : "DESPESA";

            transaction.setName(this.lblNameTransaction.getText());
            transaction.setAmount(amount);
            transaction.setPaid(this.checkPaid.isSelected());
            transaction.setType(type);
            transaction.setDueDate(this.pickDueDate.getValue());
            transaction.setPaymentDate(this.pickPaymentDate.getValue());
            transaction.setCategory(this.boxCategory.getValue());
            transaction.setAccount(account);
            transaction.setUser(user);

            if (this.radioFixed.isSelected()) {
                transaction.setRecurring("FIXA");
                transaction.setFixedFrequency(this.boxFrequency.getValue());
            } else if (this.radioInstallment.isSelected()) {
                transaction.setRecurring("PARCELADA");
                transaction.setInstallmentFrequency(this.boxFrequency.getValue());
                transaction.setInstallmentNumber(
                        this.lblNumber.getText().isEmpty() ? null : Integer.parseInt(this.lblNumber.getText())
                );
                transaction.setInstallmentCount(
                        this.lblCount.getText().isEmpty() ? null : Integer.parseInt(this.lblCount.getText())
                );
            } else {
                transaction.setRecurring(null);
            }

            transactionDAO.insert(transaction);
        }

        Main.loadView("TransactionsFrame");
    }

    @FXML
    public void handleBackPage() {
        Main.loadView("TransactionsFrame");
    }

    private void checkScreenEditing() {
        this.transaction = Main.getAndRemoveData("transactionSelected");
        if(this.isEditing()) {
            this.lblNameTransaction.setText(this.transaction.getName());
            this.lblAmount.setText(this.transaction.getAmount().toString());
            this.checkPaid.setSelected(this.transaction.getPaid());
            if ("RECEITA".equals(transaction.getType())) {
                this.radioIncome.setSelected(true);
            } else if ("DESPESA".equals(transaction.getType())) {
                this.radioExpense.setSelected(true);
            }
            if (transaction.getRecurring() == null) {
                this.radioFalse.setSelected(true);
            } else if ("FIXA".equals(transaction.getRecurring())) {
                this.radioFixed.setSelected(true);
            } else if ("PARCELADA".equals(transaction.getRecurring())) {
                this.radioInstallment.setSelected(true);
            }
            this.lblNumber.setText(transaction.getInstallmentNumber().toString());
            this.lblCount.setText(transaction.getInstallmentCount().toString());
            if (transaction.getFixedFrequency() != null) {
                this.boxFrequency.setValue(transaction.getFixedFrequency());
            } else if (transaction.getInstallmentFrequency() != null) {
                this.boxFrequency.setValue(transaction.getInstallmentFrequency());
            } else {
                this.boxFrequency.setValue("");
            }
            this.pickDueDate.setValue(transaction.getDueDate());
            this.boxCategory.getItems().stream()
                    .filter(category -> category.equals(this.transaction.getCategory()))
                    .findFirst()
                    .ifPresent(this.boxCategory::setValue);
            this.boxAccount.getItems().stream()
                    .filter(account -> account.equals(this.transaction.getAccount()))
                    .findFirst()
                    .ifPresent(this.boxAccount::setValue);
            if (transaction.getPaymentDate() != null) {
                this.pickPaymentDate.setValue(transaction.getPaymentDate());
            }
        }
    }
    private boolean isEditing() {
        return this.transaction!=null;
    }
}
