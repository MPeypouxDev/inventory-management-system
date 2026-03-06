package com.stockmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@Component
public class MainController {

    @Autowired
    private ApplicationContext springContext;

    @FXML
    private VBox contentArea;

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
}
