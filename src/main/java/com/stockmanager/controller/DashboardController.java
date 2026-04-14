package com.stockmanager.controller;

import com.stockmanager.model.Product;
import com.stockmanager.model.StockMovement;
import com.stockmanager.service.ProductService;
import com.stockmanager.service.StockMovementService;
import com.stockmanager.service.SupplierService;

import com.stockmanager.util.DateUtils;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DashboardController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private StockMovementService stockMovementService;

    @FXML
    private BarChart<String, Number> stockBarChart;

    @FXML
    private PieChart movementPieChart;

    @FXML
    private Label productCountLabel;

    @FXML
    private Label supplierCountLabel;

    @FXML
    private Label alertCountLabel;

    @FXML
    private Label movementCountLabel;

    @FXML
    private TableColumn<StockMovement, String> recentProductColumn;

    @FXML
    private TableColumn<StockMovement, String> recentTypeColumn;

    @FXML
    private TableColumn<StockMovement, String> recentQuantityColumn;

    @FXML
    private TableColumn<StockMovement, String> recentDateColumn;

    @FXML
    private TableView<StockMovement> recentMovementsTable;

    public void initialize() {
        List<Product> products = productService.findAll();
        List<Product> lowStock = productService.findLowStockProducts();
        List<StockMovement> movements = stockMovementService.findAll();

        productCountLabel.setText(String.valueOf(products.size()));
        supplierCountLabel.setText(String.valueOf(supplierService.findAll().size()));
        alertCountLabel.setText(String.valueOf(lowStock.size()));
        movementCountLabel.setText(String.valueOf(movements.size()));

        recentProductColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getProduct() != null ?
                                cellData.getValue().getProduct().getName() : ""
                )
        );
        recentTypeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getType() != null ?
                                cellData.getValue().getType().getLabel() : ""
                )
        );
        recentQuantityColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(cellData.getValue().getQuantity())
                )
        );
        recentDateColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        DateUtils.format(cellData.getValue().getDate())
                )
        );
        recentMovementsTable.setItems(
                javafx.collections.FXCollections.observableArrayList(movements)
        );

        // Graphique stock par produit
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Stock actuel");
        for (Product product : products) {
            series.getData().add(
                    new XYChart.Data<>(product.getName(), product.getStockQuantity())
            );
        }
        stockBarChart.getData().add(series);

        // Graphique répartition mouvements
        long entries = movements.stream()
                .filter(m -> m.getType() == StockMovement.MovementType.ENTRY)
                .count();
        long outgoings = movements.stream()
                .filter(m -> m.getType() == StockMovement.MovementType.OUTGOING)
                .count();

        movementPieChart.getData().addAll(
                new PieChart.Data("Entrées", entries),
                new PieChart.Data("Sorties", outgoings)
        );
    }
}
