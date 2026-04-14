package com.stockmanager.controller;

import com.stockmanager.service.ProductService;
import com.stockmanager.model.Product;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
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
    private VBox contentArea;

    @FXML
    private Label alertLabel;

    @Autowired
    private ProductService productService;

    @FXML
    private void showProducts() { loadView("/views/ProductView.fxml"); }

    @FXML
    private void showSuppliers() { loadView("/views/SupplierView.fxml"); }

    @FXML
    private void showMovements() { loadView("/views/MovementView.fxml"); }

    @FXML
    private void showDashboard() { loadView("/views/DashboardView.fxml"); }

    @FXML
    private void showCategories() { loadView("/views/CategoryView.fxml"); }

    @FXML
    private void showUsers() { loadView("/views/UserView.fxml"); }

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(springContext::getBean);
        try {
            Parent root = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);

            if (contentArea.getScene() != null) {
                Stage stage = (Stage) contentArea.getScene().getWindow();
                stage.setTitle("Gestion de Stock - " + getTitleForView(fxmlPath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTitleForView(String fxmlPath) {
        return switch (fxmlPath) {
            case "/views/ProductView.fxml" -> "Produits";
            case "/views/SupplierView.fxml" -> "Fournisseurs";
            case "/views/MovementView.fxml" -> "Mouvements";
            case "/views/CategoryView.fxml" -> "Catégories";
            case "/views/DashboardView.fxml" -> "Tableau de bord";
            case "/views/UserView.fxml" -> "Utilisateurs";
            default -> "";
        };
    }

    public void initialize() {
        List<Product> lowStock = productService.findLowStockProducts();
        if (lowStock.isEmpty()) {
            alertLabel.setText("");
        } else {
            alertLabel.setText("Attention " + lowStock.size() + " produit(s) en alerte stock !");
        }
        showDashboard();
    }
}
