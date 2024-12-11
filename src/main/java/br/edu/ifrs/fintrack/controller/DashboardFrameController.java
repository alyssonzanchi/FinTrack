package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.TransactionDAO;
import br.edu.ifrs.fintrack.model.Transaction;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.util.LoggedUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.math.BigDecimal;
import java.net.URL;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardFrameController implements Initializable {
    @FXML
    public Label lblMonth;

    @FXML
    public Label lblEntryValue;

    @FXML
    public Label lblBalance;

    @FXML
    public Label lblBalanceValue;

    @FXML
    public Label lblExitValue;

    @FXML
    public BarChart<?, ?> chartCashflow;

    @FXML
    public CategoryAxis x;

    @FXML
    public NumberAxis y;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblName;

    @FXML
    private ImageView profileImageView;

    private YearMonth currentMonth;

    private final TransactionDAO transactionDAO = new TransactionDAO();

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        User user = LoggedUser.getUser();
        Image profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImage())));
        profileImageView.setImage(profileImage);
        lblName.setText(user.getName());
        lblEmail.setText(user.getEmail());

        currentMonth = YearMonth.now();
        updateMonthLabel();

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

    public void handleBackMonth() {
        currentMonth = currentMonth.minusMonths(1);
        updateMonthLabel();
    }

    public void handleNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        updateMonthLabel();
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

        updateCashflowChart();
        calculateMonthlyExpenses();
        calculateMonthlyIncome();
        calculateBalance();
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    private void updateCashflowChart() {
        List<Transaction> transactions = transactionDAO.listByUserAndDueDate(LoggedUser.getUser().getId(), currentMonth);

        BigDecimal totalEntry = transactions.stream()
                .map(Transaction::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExit = transactions.stream()
                .map(Transaction::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalEntry.subtract(totalExit);

        chartCashflow.getData().clear();

        XYChart.Series<String, Number> entryValue = new XYChart.Series<>();
        entryValue.setName("Entrada");
        entryValue.getData().add(new XYChart.Data<>("Entrada", totalEntry.doubleValue()));

        XYChart.Series<String, Number> exitValue = new XYChart.Series<>();
        exitValue.setName("Saída");
        exitValue.getData().add(new XYChart.Data<>("Saída", totalExit.doubleValue()));

        XYChart.Series<String, Number> balanceValue = new XYChart.Series<>();
        balanceValue.setName("Saldo Previsto");
        balanceValue.getData().add(new XYChart.Data<>("Saldo Previsto", balance.doubleValue()));

        chartCashflow.getData().addAll(
                (XYChart.Series) entryValue,
                (XYChart.Series) exitValue,
                (XYChart.Series) balanceValue
        );
    }

    private void calculateMonthlyExpenses() {
        List<Transaction> transactions = transactionDAO.listByUserAndDueDate(LoggedUser.getUser().getId(), currentMonth);

        BigDecimal totalExit = transactions.stream()
                .map(Transaction::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        lblExitValue.setText("R$ " + totalExit);
    }

    private void calculateMonthlyIncome() {
        List<Transaction> transactions = transactionDAO.listByUserAndDueDate(LoggedUser.getUser().getId(), currentMonth);

        BigDecimal totalEntry = transactions.stream()
                .map(Transaction::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        lblEntryValue.setText("R$ " + totalEntry);
    }

    private void calculateBalance() {
        List<Transaction> transactions = transactionDAO.listByUserAndDueDate(LoggedUser.getUser().getId(), currentMonth);

        BigDecimal totalEntry = transactions.stream()
                .map(Transaction::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExit = transactions.stream()
                .map(Transaction::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalEntry.subtract(totalExit);

        lblBalanceValue.setText("R$ " + balance);
    }
}
