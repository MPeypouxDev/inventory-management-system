package com.stockmanager.controller;

import com.stockmanager.service.ProductService;
import com.stockmanager.model.Product;
import com.stockmanager.model.StockMovement;
import com.stockmanager.service.StockMovementService;
import com.stockmanager.service.SupplierService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

@Component
public class MainController {

    @Autowired
    private ApplicationContext springContext;

    @FXML
    private TableColumn<StockMovement, String> recentProductColumn;

    @FXML
    private TableColumn<StockMovement, String> recentTypeColumn;

    @FXML
    private TableColumn<StockMovement, String> recentQuantityColumn;

    @FXML
    private TableColumn<StockMovement, String> recentDateColumn;

    @FXML
    private Label productCountLabel;

    @FXML
    private Label supplierCountLabel;

    @FXML
    private Label alertCountLabel;

    @FXML
    private Label movementCountLabel;

    @FXML
    private TableView<StockMovement> recentMovementsTable;

    @FXML
    private VBox contentArea;

    @FXML
    private Label alertLabel;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private StockMovementService stockMovementService;

    @FXML
    private void showProducts() { loadView("/views/ProductView.fxml"); }

    @FXML
    private void showSuppliers() { loadView("/views/SupplierView.fxml"); }

    @FXML
    private void showMovements() { loadView("/views/MovementView.fxml"); }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 300));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath) {
        Stage stage = (Stage) contentArea.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(springContext::getBean);
        try {
            Parent root = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        List<Product> lowStock = productService.findLowStockProducts();
        if (lowStock.isEmpty()) {
            alertLabel.setText("");
        } else {
            alertLabel.setText("Attention " + lowStock.size() + " produit(s) en alerte stock !");
        }

        productCountLabel.setText(String.valueOf(productService.findAll().size()));
        supplierCountLabel.setText(String.valueOf(supplierService.findAll().size()));
        alertCountLabel.setText(String.valueOf(lowStock.size()));
        movementCountLabel.setText(String.valueOf(stockMovementService.findAll().size()));

        List<StockMovement> movements = stockMovementService.findAll();

        recentProductColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getProduct() != null ?
                                cellData.getValue().getProduct().getName() : ""
                )
        );

        recentTypeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getType() != null ?
                                cellData.getValue().getType().name() : ""
                )
        );

        recentQuantityColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(cellData.getValue().getQuantity())
                )
        );

        recentDateColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDate() != null ?
                                cellData.getValue().toString() : ""
                )
        );

        recentMovementsTable.setItems(
                javafx.collections.FXCollections.observableArrayList(movements)
        );
    }
}
