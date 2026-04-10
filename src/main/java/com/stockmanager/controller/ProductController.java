package com.stockmanager.controller;

import com.stockmanager.model.StockMovement;
import com.stockmanager.service.CategoryService;
import com.stockmanager.service.ExportService;
import com.stockmanager.service.ProductService;
import com.stockmanager.model.Product;
import com.stockmanager.model.Category;
import com.stockmanager.service.StockMovementService;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;


@Component
public class ProductController {

    @Autowired
    private ApplicationContext springContext;

    @Autowired
    private ProductService productService;

    @Autowired
    private StockMovementService stockMovementService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExportService exportService;

    @FXML
    TableView<Product> productTable;

    @FXML
    TextField searchField;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Integer> stockColumn;

    @FXML
    private TableColumn<Product, Integer> priceColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private void handleAdd() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un produit");
        dialog.setHeaderText("Nouveau produit");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        TextField purchasePriceField = new TextField();
        TextField salePriceField = new TextField();
        TextField stockField = new TextField();
        TextField thresholdField = new TextField();
        ComboBox<Category> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(categoryService.findAll());

        categoryCombo.setConverter(new javafx.util.StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getName() : "";
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });

        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description :"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Catégorie :"), 0, 6);
        grid.add(categoryCombo, 1, 6);
        grid.add(new Label("Prix d'achat :"), 0, 2);
        grid.add(purchasePriceField, 1, 2);
        grid.add(new Label("Prix de vente :"), 0, 3);
        grid.add(salePriceField, 1, 3);
        grid.add(new Label("Stock initial :"), 0, 4);
        grid.add(stockField, 1, 4);
        grid.add(new Label("Seuil d'alerte :"), 0, 5);
        grid.add(thresholdField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                if (nameField.getText().isEmpty() ||
                        stockField.getText().isEmpty() ||
                        salePriceField.getText().isEmpty() ||
                        thresholdField.getText().isEmpty() ||
                        categoryCombo.getValue() == null) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Champs requis manquants");
                    alert.setContentText("Veuillez remplir tous les champs obligatoires : Nom, Stock, Prix de vente, Seuil d'alerte et Catégorie.");
                    alert.showAndWait();
                    return;
                }


                Product product = new Product();
                product.setName(nameField.getText());
                product.setDescription(descriptionField.getText());
                product.setCategory(categoryCombo.getValue());
                product.setPurchasePrice(Double.parseDouble(purchasePriceField.getText()));
                product.setSalePrice(Double.parseDouble(salePriceField.getText()));
                product.setStockQuantity(Integer.parseInt(stockField.getText()));
                product.setAlertThreshold(Integer.parseInt(thresholdField.getText()));

                productService.save(product);
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        ObservableList<Product> products = FXCollections.observableArrayList(productService.findAll());
        productTable.setItems(products);
    }

    @FXML
    private void handleEdit() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner un produit à modifier");
            alert.showAndWait();
        } else {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Modifier un produit");
            dialog.setHeaderText("Modifier : " + selected.getName());

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nameField = new TextField(selected.getName());
            TextField descriptionField = new TextField(selected.getDescription());
            TextField purchasePriceField = new TextField(String.valueOf(selected.getPurchasePrice()));
            TextField salePriceField =  new TextField(String.valueOf(selected.getSalePrice()));
            TextField stockField = new TextField(String.valueOf(String.valueOf(selected.getStockQuantity())));
            TextField thresholdField = new TextField(String.valueOf(selected.getAlertThreshold()));

            ComboBox<Category> categoryCombo = new ComboBox<>();
            categoryCombo.getItems().addAll(categoryService.findAll());
            categoryCombo.setValue(selected.getCategory());
            categoryCombo.setConverter(new javafx.util.StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category != null ? category.getName() : "";
                }
                @Override public Category fromString(String string) { return null; }
            });

            grid.add(new Label("Nom :"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Description :"), 0, 1);
            grid.add(descriptionField, 1, 1);
            grid.add(new Label("Prix d'achat :"), 0, 2);
            grid.add(purchasePriceField, 1, 2);
            grid.add(new Label("Prix de vente :"), 0, 3);
            grid.add(salePriceField, 1, 3);
            grid.add(new Label("Stock :"), 0, 4);
            grid.add(stockField, 1, 4);
            grid.add(new Label("Seuil d'alerte :"), 0, 5);
            grid.add(thresholdField, 1, 5);
            grid.add(new Label("Catégorie :"), 0, 6);
            grid.add(categoryCombo, 1, 6);

            dialog.getDialogPane().setContent(grid);

            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    if (nameField.getText().isEmpty() ||
                            stockField.getText().isEmpty() ||
                            salePriceField.getText().isEmpty() ||
                            thresholdField.getText().isEmpty() ||
                            categoryCombo.getValue() == null) {

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Champs requis manquants");
                        alert.setContentText("Veuillez remplir tous les champs obligatoires : Nom, Stock, Prix de vente, Seuil d'alerte et Catégorie.");
                        alert.showAndWait();
                        return;
                    }

                    selected.setName(nameField.getText());
                    selected.setDescription(descriptionField.getText());
                    selected.setCategory(categoryCombo.getValue());
                    selected.setPurchasePrice(Double.parseDouble(purchasePriceField.getText()));
                    selected.setSalePrice(Double.parseDouble(salePriceField.getText()));
                    selected.setStockQuantity(Integer.parseInt(stockField.getText()));
                    selected.setAlertThreshold(Integer.parseInt(thresholdField.getText()));

                    productService.save(selected);
                    refreshTable();
                }
            });
        }
    }

    @FXML
    private void handleDelete() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner un produit");
            alert.showAndWait();
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setContentText("Voulez-vous vraiment supprimer " + selected.getName() + " ?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    productService.delete(selected.getId());
                    refreshTable();
                }
            });
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.isEmpty()) {
            refreshTable();
        } else {
            ObservableList<Product> results = FXCollections.observableArrayList(
                    productService.searchByName(query)
            );
            productTable.setItems(results);
        }
    }

    @FXML
    private void handleHistory() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner un produit");
            alert.showAndWait();
        } else {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Historique de " + selected.getName());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            TableView<StockMovement> table = new TableView<>();
            table.setPrefWidth(600);
            table.setPrefHeight(400);

            TableColumn<StockMovement, String> typeCol = new TableColumn<>("Type");
            typeCol.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().getType().getLabel()
                    )
            );

            TableColumn<StockMovement, String> quantityCol = new TableColumn<>("Quantité");
            quantityCol.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            String.valueOf(cellData.getValue().getQuantity())
                    )
            );

            TableColumn<StockMovement, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().getDate() != null ?
                                    cellData.getValue().getDate().toString() : ""
                    )
            );

            TableColumn<StockMovement, String> reasonCol = new TableColumn<>("Raison");
            reasonCol.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().getReason()
                    )
            );

            table.getColumns().addAll(typeCol, quantityCol, dateCol, reasonCol);

            table.setItems(FXCollections.observableArrayList(
                    stockMovementService.findByProduct(selected.getId())
            ));

            dialog.getDialogPane().setContent(table);
            dialog.showAndWait();
        }
    }

    @FXML
    private void handleExportExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer l'export Excel");
        fileChooser.setInitialFileName("produits.xlsx");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier Excel", "*.xlsx")
        );

        Stage stage = (Stage) productTable.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                exportService.exportProductsToExcel(file.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export réussi");
                alert.setContentText("Le fichier a été exporté avec succès !");
                alert.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'export");
                alert.setContentText("Erreur lors de 'lexport :" + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        categoryColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory() != null ?
                                cellData.getValue().getCategory().getName() : ""
                )
        );

        productTable.setRowFactory(tv -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                if (product == null || empty) {
                    setStyle("");
                } else if (product.getStockQuantity() <= product.getAlertThreshold()) {
                    setStyle("-fx-background-color: #ffcccc");
                } else {
                    setStyle("");
                }
            }
        });

        ObservableList<Product> products = FXCollections.observableArrayList(productService.findAll());
        productTable.setItems(products);
    }
}
