package com.stockmanager.controller;

import com.stockmanager.config.GlobalExceptionHandler;
import com.stockmanager.model.StockMovement;
import com.stockmanager.model.Product;
import com.stockmanager.service.ProductService;
import com.stockmanager.service.StockMovementService;
import com.stockmanager.service.ExportService;
import com.stockmanager.util.DateUtils;
import com.stockmanager.util.Pagination;

import com.stockmanager.util.ToastNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MovementController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StockMovementService stockMovementService;

    @Autowired
    private ExportService exportService;

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
    private TableColumn<StockMovement, String> dateColumn;

    @FXML
    private TableColumn<StockMovement, String> reasonColumn;

    @FXML
    private TableColumn<StockMovement, String> userColumn;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private Label pageLabel;

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
        typeCombo.setConverter(new javafx.util.StringConverter<StockMovement.MovementType>() {
            @Override
            public String toString(StockMovement.MovementType type) {
                return type != null ? type.getLabel() : "";
            }

            @Override
            public StockMovement.MovementType fromString(String string) {
                return null;
            }
        });
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

                try {
                    if (productCombo.getValue() == null ||
                            typeCombo.getValue() == null ||
                            quantityField.getText().isEmpty()) {

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Champs requis manquants");
                        alert.setContentText("Veuillez remplir tous les champs obligatoires : Produit, Type et Quantité.");
                        alert.showAndWait();
                        return;
                    }


                    StockMovement stockMovement = new StockMovement();
                    stockMovement.setProduct(productCombo.getValue());
                    stockMovement.setType(typeCombo.getValue());
                    stockMovement.setQuantity(Integer.parseInt(quantityField.getText()));
                    stockMovement.setDate(LocalDateTime.now());
                    stockMovement.setReason(reasonField.getText());

                    stockMovementService.addMovement(stockMovement);
                    refreshTable();
                    Stage stage = (Stage) stockMovementTable.getScene().getWindow();
                    ToastNotification.show(stage, "Mouvement ajouté avec succès !");
                } catch (Exception e) {
                    GlobalExceptionHandler.handle(e);
                }
            }
        });
    }

    private void refreshTable() {
        updateTable(stockMovementService.findAll());
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.isEmpty()) {
            refreshTable();
        } else {
            ObservableList<StockMovement> results = FXCollections.observableArrayList(
                    stockMovementService.searchByProductName(query)
            );
            stockMovementTable.setItems(results);
        }
    }

    @FXML
    private void handleExportPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer l'export PDF");
        fileChooser.setInitialFileName("mouvements.pdf");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf")
        );

        Stage stage = (Stage) stockMovementTable.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                exportService.exportMovementsToPdf(file.getAbsolutePath());
                ToastNotification.show(stage, "Export PDF réussi !");
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'export");
                alert.setContentText("Erreur lors de l'export :" + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleNextPage() {
        List<StockMovement> all = stockMovementService.findAll();
        pagination.nextPage(all);
        updateTable(all);
    }

    @FXML
    private void handlePreviousPage() {
        List<StockMovement> all = stockMovementService.findAll();
        pagination.previousPage();
        updateTable(all);
    }

    private void updateTable(List<StockMovement> allMovements) {
        stockMovementTable.setItems(FXCollections.observableArrayList(
                pagination.getPage(allMovements)
        ));
        pageLabel.setText("Page " + (pagination.getCurrentPage() + 1) +
                " / " + pagination.getTotalPages(allMovements));
        prevButton.setDisable(!pagination.hasPrevious());
        nextButton.setDisable(!pagination.hasNext(allMovements));
    }

    private final Pagination<StockMovement> pagination = new Pagination<>(10);

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
                                stockMovementStringCellDataFeatures.getValue().getType().getLabel() : ""
                )
                );
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        DateUtils.format(cellData.getValue().getDate())
                )
        );
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        userColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getUser() != null ?
                                cellData.getValue().getUser().getUsername() : ""
                )
        );

        updateTable(stockMovementService.findAll());
    }
}
