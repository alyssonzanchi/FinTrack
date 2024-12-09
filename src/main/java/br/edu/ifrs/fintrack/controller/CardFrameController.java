package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.CreditCardDAO;
import br.edu.ifrs.fintrack.model.CreditCard;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.util.LoggedUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CardFrameController implements Initializable {

    @FXML
    private TableColumn<CreditCard, String> clmLimit;

    @FXML
    private TableColumn<CreditCard, String> clmName;

    @FXML
    private TableColumn<CreditCard, String> clmIcon;

    @FXML
    private TableView<CreditCard> tblList;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblName;

    @FXML
    private ImageView profileImageView;

    private final CreditCardDAO creditCardDAO = new CreditCardDAO();

    private void loadTable() {
        ObservableList<CreditCard> creditCards = FXCollections.observableArrayList(creditCardDAO.listByUser(LoggedUser.getUser().getId()));
        this.tblList.setItems(creditCards);
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        User user = LoggedUser.getUser();
        Image profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImage())));
        profileImageView.setImage(profileImage);
        lblName.setText(user.getName());
        lblEmail.setText(user.getEmail());

        this.loadTable();
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

    public void handleEditCard() {
        CreditCard creditCard = this.tblList.getSelectionModel().getSelectedItem();
        if(creditCard != null) {
            Main.addData("creditCardSelected", creditCard);
            Main.loadView("RegisterCreditCard");
        } else {
            rowNotSelected();
        }
    }

    public void handleRemoveCard() {
        CreditCard creditCard = this.tblList.getSelectionModel().getSelectedItem();
        if(creditCard != null) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Cartão");
            alert.setHeaderText("Deseja realmente apagar esse cartão?");
            var res = alert.showAndWait();

            if(res.get() == ButtonType.OK) {
                creditCardDAO.delete(creditCard.getId());
                Main.loadView("CardsFrame");
            }
        }
    }

    private void rowNotSelected() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText("Cartão não selecionado!");
        alert.setContentText("Você deve selecionar um cartão para completar essa ação!");
        alert.show();
    }
}
