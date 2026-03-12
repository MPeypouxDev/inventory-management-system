package com.stockmanager.controller;

import com.stockmanager.service.ProductService;
import com.stockmanager.model.Product;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;


@Component
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ApplicationContext springContext;

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
    private void handleAdd() { }

    @FXML
    private void handleEdit() { }

    @FXML
    private void handleDelete() { }

    @FXML
    private void handleSearch() { }

    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        categoryColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory() != null ?
                                cellData.getValue().getCategory().getName() : ""
                )
        );

        ObservableList<Product> products = FXCollections.observableArrayList(productService.findAll());
        productTable.setItems(products);
    }
}
