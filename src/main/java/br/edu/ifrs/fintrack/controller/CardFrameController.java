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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CardFrameController implements Initializable {

    @FXML
    private TableColumn<CreditCard, BigDecimal> clmLimit;

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

        clmIcon.setCellValueFactory(new PropertyValueFactory<>("icon"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmLimit.setCellValueFactory(new PropertyValueFactory<>("limit"));

        tblList.setRowFactory(_ -> {
            TableRow<CreditCard> row = new TableRow<>();

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
                    setAlignment(Pos.CENTER);
                }
            }
        });

        clmLimit.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    String limitText = "Limite";
                    setText(limitText + "\nR$ " + item);
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

    public void handleEditCard() {
        CreditCard creditCard = this.tblList.getSelectionModel().getSelectedItem();
        if(creditCard != null) {
            Main.addData("cardSelected", creditCard);
            Main.loadView("RegisterCardFrame");
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
