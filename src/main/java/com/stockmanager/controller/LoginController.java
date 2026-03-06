package com.stockmanager.controller;

import com.stockmanager.service.AuthService;
import com.stockmanager.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginController {

    @Autowired
    private AuthService authService;

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
            // Navigate
        } else {
            errorLabel.setText("La connexion a échoué, veuillez vérifier les identifiants saisis");
        }
    }
}
