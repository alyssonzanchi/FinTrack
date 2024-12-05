package br.edu.ifrs.fintrack.controller;

import br.edu.ifrs.fintrack.Main;
import br.edu.ifrs.fintrack.dao.CategoryDAO;
import br.edu.ifrs.fintrack.dao.UserDAO;
import br.edu.ifrs.fintrack.model.Category;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CategoriesFrameController {
    @FXML
    private Label lblEmail;

    @FXML
    private Label lblName;

    @FXML
    private ImageView profileImageView;

    @FXML
    private TableView<Category> tblListCosts;

    @FXML
    private TableView<Category> tblListReceipts;

    @FXML
    private TableColumn<Category, String> clmIconCosts;

    @FXML
    private TableColumn<Category, String> clmIconReceipts;

    @FXML
    private TableColumn<Category, String> clmNameCosts;

    @FXML
    private TableColumn<Category, String> clmNameReceipts;

    private final UserDAO userDAO = new UserDAO();

    private final CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> listCategoriesReceipts() {
        List<Category> allCategories = categoryDAO.list(100, 0);
        return allCategories.stream()
                .filter(category -> Objects.equals(category.getType(), "RECEITA"))
                .collect(Collectors.toList());
    }

    public List<Category> listCategoriesCosts() {
        List<Category> allCategories = categoryDAO.list(100, 0);
        return allCategories.stream()
                .filter(category -> Objects.equals(category.getType(), "DESPESA"))
                .collect(Collectors.toList());
    }

    private void loadTables() {
        ObservableList<Category> categoriesReceipts = FXCollections.observableArrayList(listCategoriesReceipts());
        ObservableList<Category> categoriesCosts = FXCollections.observableArrayList(listCategoriesCosts());
        this.tblListReceipts.setItems(categoriesReceipts);
        this.tblListCosts.setItems(categoriesCosts);
    }

    @FXML
    public void initialize() {
        int userId = Session.getInstance().getUserId();
        User user = userDAO.get(userId);
        Image profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImage())));
        profileImageView.setImage(profileImage);
        lblName.setText(user.getName());
        lblEmail.setText(user.getEmail());

        clmIconReceipts.setCellValueFactory(new PropertyValueFactory<>("icon"));
        clmNameReceipts.setCellValueFactory(new PropertyValueFactory<>("name"));

        tblListReceipts.setRowFactory(_ -> {
            TableRow<Category> row = new TableRow<>();

            row.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-padding: 10; -fx-border-insets: 5; -fx-background-insets: 5;");

            row.selectedProperty().addListener((_, _, isNowSelected) -> {
                if (isNowSelected) {
                    row.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-padding: 10;  -fx-border-insets: 5; -fx-background-insets: 5;");
                } else {
                    row.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-padding: 10; -fx-border-insets: 5; -fx-background-insets: 5;");
                }
            });

            return row;
        });

        clmIconReceipts.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(item))));
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);
                    setGraphic(imageView);
                }
            }
        });

        clmNameReceipts.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-size: 16px;");
                }
            }
        });

        clmIconCosts.setCellValueFactory(new PropertyValueFactory<>("icon"));
        clmNameCosts.setCellValueFactory(new PropertyValueFactory<>("name"));

        tblListCosts.setRowFactory(_ -> {
            TableRow<Category> row = new TableRow<>();

            row.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-padding: 10; -fx-border-insets: 5; -fx-background-insets: 5;");

            row.selectedProperty().addListener((_, _, isNowSelected) -> {
                if (isNowSelected) {
                    row.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-padding: 10;  -fx-border-insets: 5; -fx-background-insets: 5;");
                } else {
                    row.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-padding: 10; -fx-border-insets: 5; -fx-background-insets: 5;");
                }
            });

            return row;
        });

        clmIconCosts.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(item))));
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);
                    setGraphic(imageView);
                }
            }
        });

        clmNameCosts.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-size: 16px;");
                }
            }
        });

        this.loadTables();
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

    public void handleNewCategoryReceipt() {
        Main.loadView("RegisterCategoryFrame");
    }

    public void handleEditCategoryReceipt() {
        Category category = this.tblListReceipts.getSelectionModel().getSelectedItem();
        if(category != null) {
            Main.addData("categorySelected", category);
            Main.loadView("RegisterCategoryFrame");
        } else {
            rowNotSelected();
        }
    }

    public void handleRemoveCategoryReceipt() {
        Category category = this.tblListReceipts.getSelectionModel().getSelectedItem();
        if(category != null) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Categoria");
            alert.setHeaderText("Deseja realmente apagar essa categoria?");
            var res = alert.showAndWait();

            if(res.get() == ButtonType.OK) {
                categoryDAO.delete(category.getId());
                Main.loadView("CategoriesFrame");
            }
        } else {
            rowNotSelected();
        }
    }

    public void handleNewCategoryCost() {
        Main.loadView("RegisterCategoryFrame");
    }

    public void handleEditCategoryCost() {
        Category category = this.tblListCosts.getSelectionModel().getSelectedItem();
        if(category != null) {
            Main.addData("categorySelected", category);
            Main.loadView("RegisterCategoryFrame");
        } else {
            rowNotSelected();
        }
    }

    public void handleRemoveCategoryCost() {
        Category category = this.tblListCosts.getSelectionModel().getSelectedItem();
        if(category != null) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Categoria");
            alert.setHeaderText("Deseja realmente apagar essa categoria?");
            var res = alert.showAndWait();

            if(res.get() == ButtonType.OK) {
                categoryDAO.delete(category.getId());
                Main.loadView("CategoriesFrame");
            }
        } else {
            rowNotSelected();
        }
    }

    private void rowNotSelected() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText("Categoria não selecionada!");
        alert.setContentText("Você deve selecionar uma categoria para completar essa ação!");
        alert.show();
    }
}
