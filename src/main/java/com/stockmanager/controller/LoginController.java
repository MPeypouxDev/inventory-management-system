package com.stockmanager.controller;

import com.stockmanager.config.SessionManager;
import com.stockmanager.service.AuthService;
import com.stockmanager.model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.io.IOException;

@Component
public class LoginController {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private ApplicationContext springContext;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Optional<User> result = authService.login(username, password);

        if (result.isPresent()) {
            sessionManager.setCurrentUser(result.get());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            try {
                Parent root = loader.load();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                errorLabel.setText("Erreur lors du chargement de l'interface");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("La connexion a échoué, veuillez vérifier les identifiants saisis");
        }
    }
}
