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
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

        toggleGroupType.selectedToggleProperty().addListener((_, _, newToggle) -> {
                    if (newToggle != null) {
                        String selectedType = ((RadioButton) newToggle).getText();

                        List<Category> allCategories = categoryDAO.list(100, 0);

                        List<Category> filteredCategories = allCategories.stream()
                                .filter(category -> category.getType().equalsIgnoreCase(selectedType))
                                .toList();

                        boxCategory.getItems().clear();
                        boxCategory.getItems().addAll(filteredCategories);
                        boxCategory.setCellFactory(_ -> new ListCell<>() {
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
                        });
                    }

                    boxCategory.setButtonCell(new ListCell<>() {
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
                    });
                });

        radioInstallment.setOnAction(_ -> {
            if (radioInstallment.isSelected()) {
                boxFrequency.setDisable(false);
                lblFrequency.setDisable(false);
                txtCount.setDisable(false);
                lblCount.setDisable(false);
                txtNumber.setDisable(false);
                lblNumber.setDisable(false);
            }
        });

        radioFixed.setOnAction(_ -> {
            if (radioFixed.isSelected()) {
                boxFrequency.setDisable(false);
                lblFrequency.setDisable(false);
                txtCount.setDisable(true);
                lblCount.setDisable(true);
                txtNumber.setDisable(true);
                lblNumber.setDisable(true);
            }
        });

        checkPaid.setOnAction(_ -> {
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
        if (this.isEditing()) {
            // Edição de transação existente
            this.transaction.setName(this.lblNameTransaction.getText());
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
                        this.txtNumber.getText().isEmpty() ? null : Integer.parseInt(this.txtNumber.getText())
                );
                this.transaction.setInstallmentCount(
                        this.txtCount.getText().isEmpty() ? null : Integer.parseInt(this.txtCount.getText())
                );
            }

            this.transaction.setDueDate(this.pickDueDate.getValue());
            this.transaction.setPaymentDate(this.pickPaymentDate.getValue());
            this.transaction.setCategory(this.boxCategory.getValue());
            this.transaction.setAccount(this.boxAccount.getValue());
            this.transaction.setUser(user);

            transactionDAO.update(this.transaction);
        } else {
            // Criação de nova transação
            BigDecimal amount = new BigDecimal(this.lblAmount.getText());
            Account account = this.boxAccount.getValue();
            String type = this.radioIncome.isSelected() ? "RECEITA" : "DESPESA";
            String recurring = null;
            String fixedFrequency = null;
            String installmentFrequency = null;
            Integer installmentNumber = null;
            Integer installmentCount = null;

            if (this.radioFixed.isSelected()) {
                recurring = "FIXA";
                fixedFrequency = this.boxFrequency.getValue();
            } else if (this.radioInstallment.isSelected()) {
                recurring = "PARCELADA";
                installmentFrequency = this.boxFrequency.getValue();
                installmentNumber = this.txtNumber.getText().isEmpty() ? null : Integer.parseInt(this.txtNumber.getText());
                installmentCount = this.txtCount.getText().isEmpty() ? null : Integer.parseInt(this.txtCount.getText());
            }

            Transaction transaction = new Transaction(
                    this.lblNameTransaction.getText(),
                    type,
                    amount,
                    this.pickDueDate.getValue(),
                    this.pickPaymentDate.getValue(),
                    this.checkPaid.isSelected(),
                    null,
                    recurring,
                    fixedFrequency,
                    installmentFrequency,
                    installmentCount,
                    installmentNumber,
                    user,
                    this.boxCategory.getValue(),
                    account,
                    null,
                    null
            );

            if (recurring == null) {
                transactionDAO.insert(transaction);
            } else if ("FIXA".equals(recurring)) {
                // Inserções fixas
                LocalDate dueDate = transaction.getDueDate();
                for (int i = 0; i < 60; i++) {
                    Transaction fixedTransaction = new Transaction(
                            transaction.getName(),
                            transaction.getType(),
                            transaction.getAmount(),
                            dueDate,
                            transaction.getPaymentDate(),
                            transaction.getPaid(),
                            null,
                            recurring,
                            fixedFrequency,
                            installmentFrequency,
                            installmentCount,
                            installmentNumber,
                            transaction.getUser(),
                            transaction.getCategory(),
                            transaction.getAccount(),
                            null,
                            null
                    );
                    dueDate = dueDate.plusMonths(1); // Incrementa um mês a cada iteração
                    transactionDAO.insert(fixedTransaction);
                }
            } else if ("PARCELADA".equals(recurring)) {
                // Inserções parceladas
                LocalDate dueDate = transaction.getDueDate();
                for (int i = 1; i <= installmentCount; i++) {
                    Transaction installmentTransaction = new Transaction(
                            transaction.getName(),
                            transaction.getType(),
                            transaction.getAmount(),
                            dueDate,
                            transaction.getPaymentDate(),
                            transaction.getPaid(),
                            null,
                            recurring,
                            fixedFrequency,
                            installmentFrequency,
                            installmentCount,
                            i, // Número da parcela
                            transaction.getUser(),
                            transaction.getCategory(),
                            transaction.getAccount(),
                            null,
                            null
                    );
                    dueDate = dueDate.plusMonths(1); // Incrementa um mês a cada inserção
                    transactionDAO.insert(installmentTransaction);
                }
            }
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
                boxFrequency.setDisable(true);
                lblFrequency.setDisable(true);
                txtCount.setDisable(true);
                lblCount.setDisable(true);
                txtNumber.setDisable(true);
                lblNumber.setDisable(true);
            } else if ("FIXA".equals(transaction.getRecurring())) {
                this.radioFixed.setSelected(true);
                boxFrequency.setDisable(false);
                lblFrequency.setDisable(false);
                txtCount.setDisable(true);
                lblCount.setDisable(true);
                txtNumber.setDisable(true);
                lblNumber.setDisable(true);
            } else if ("PARCELADA".equals(transaction.getRecurring())) {
                this.radioInstallment.setSelected(true);
                boxFrequency.setDisable(false);
                lblFrequency.setDisable(false);
                txtCount.setDisable(false);
                lblCount.setDisable(false);
                txtNumber.setDisable(false);
                lblNumber.setDisable(false);
            }

            if (transaction.getInstallmentNumber() != null) {
                this.lblNumber.setText(transaction.getInstallmentNumber().toString());
            }

            if (transaction.getInstallmentCount() != null) {
                this.lblCount.setText(transaction.getInstallmentCount().toString());
            }

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
