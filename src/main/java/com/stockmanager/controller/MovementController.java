package com.stockmanager.controller;

import com.stockmanager.model.StockMovement;
import com.stockmanager.model.Product;
import com.stockmanager.service.ProductService;
import com.stockmanager.service.StockMovementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MovementController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StockMovementService stockMovementService;

    @Autowired
    private ApplicationContext springContext;

    @FXML
    TableView<StockMovement> stockMovementTable;

    @FXML
    TextField searchField;

    @FXML
    private TableColumn<StockMovement, Integer> idColumn;

    @FXML
    private TableColumn<StockMovement, String> productColumn;

    @FXML
    private TableColumn<StockMovement, String> typeColumn;

    @FXML
    private TableColumn<StockMovement, Integer> quantityColumn;

    @FXML
    private TableColumn<StockMovement, LocalDateTime> dateColumn;

    @FXML
    private TableColumn<StockMovement, String> reasonColumn;

    @FXML
    private void handleAdd() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un mouvement");
        dialog.setHeaderText("Nouveau mouvement");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField quantityField = new TextField();
        TextField reasonField = new TextField();
        ComboBox<Product> productCombo = new ComboBox<>();
        ComboBox<StockMovement.MovementType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(StockMovement.MovementType.values());
        productCombo.getItems().addAll(productService.findAll());

        productCombo.setConverter(new javafx.util.StringConverter<Product>() {
            @Override
            public String toString(Product product) { return product != null ? product.getName() : ""; }

            @Override
            public Product fromString(String string) { return null; }
        });

        grid.add(new Label("Produit: "), 0, 0);
        grid.add(productCombo, 1, 0);
        grid.add(new Label("Type : "), 0, 1);
        grid.add(typeCombo, 1, 1);
        grid.add(new Label("Quantité :"), 0, 2);
        grid.add(quantityField, 1, 2);
        grid.add(new Label("Date du mouvement :"), 0, 3);
        grid.add(new Label("Raison du mouvement :"),0, 4);
        grid.add(reasonField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                StockMovement stockMovement = new StockMovement();
                stockMovement.setProduct(productCombo.getValue());
                stockMovement.setType(typeCombo.getValue());
                stockMovement.setQuantity(Integer.parseInt(quantityField.getText()));
                stockMovement.setDate(LocalDateTime.now());
                stockMovement.setReason(reasonField.getText());

                stockMovementService.addMovement(stockMovement);
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        ObservableList<StockMovement> stockMovements = FXCollections.observableArrayList(stockMovementService.findAll());
        stockMovementTable.setItems(stockMovements);
    }

    @FXML
    private void handleSearch() { }

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productColumn.setCellValueFactory(stockMovementStringCellDataFeatures ->
                new javafx.beans.property.SimpleStringProperty(
                        stockMovementStringCellDataFeatures.getValue().getProduct() != null ?
                                stockMovementStringCellDataFeatures.getValue().getProduct().getName() : ""
                )
        );
        typeColumn.setCellValueFactory(stockMovementStringCellDataFeatures ->
                new javafx.beans.property.SimpleStringProperty(
                        stockMovementStringCellDataFeatures.getValue().getType() != null ?
                                stockMovementStringCellDataFeatures.getValue().getType().name() : ""
                )
                );
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

        ObservableList<StockMovement> stockMovements = FXCollections.observableArrayList(stockMovementService.findAll());
        stockMovementTable.setItems(stockMovements);
    }
}
