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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
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

    AccountDAO accountDAO = new AccountDAO();

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
