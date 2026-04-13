package com.stockmanager.controller;

import com.stockmanager.config.GlobalExceptionHandler;
import com.stockmanager.model.User;
import com.stockmanager.service.AuthService;
import com.stockmanager.service.UserService;

import com.stockmanager.util.ToastNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @FXML
    TableView<User> userTable;

    @FXML
    TextField searchField;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private void handleAdd() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un utilisateur");
        dialog.setHeaderText("Nouveau utilisateur");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        ComboBox<User.Role> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll(User.Role.values());

        grid.add(new Label("Nom d'utilisateur :"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Mot de passe :"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Rôle :"), 0, 2);
        grid.add(roleCombo, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (usernameField.getText().isEmpty() ||
                        passwordField.getText().isEmpty() ||
                        roleCombo.getValue() == null) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Champs requis manquants");
                        alert.setContentText("Veuillez remplir tous les champs obligatoires : Nom d'utilisateur, Rôle.");
                        alert.showAndWait();
                        return;
                    }

                    User user = new User();
                    user.setUsername(usernameField.getText());
                    user.setPassword(authService.hashPassword(passwordField.getText()));
                    user.setRole(roleCombo.getValue());

                    userService.save(user);
                    refreshTable();
                    Stage stage = (Stage) userTable.getScene().getWindow();
                    ToastNotification.show(stage, "Utilisateur ajouté avec succès !");
                } catch (Exception e) {
                    GlobalExceptionHandler.handle(e);
                }
            }
        });
    }

    private void refreshTable() {
        ObservableList<User> users = FXCollections.observableArrayList(userService.findAll());
        userTable.setItems(users);
    }

    @FXML
    private void handleEdit() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner un utilisateur à modifier");
            alert.showAndWait();
        } else {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Modifier un utilisateur");
            dialog.setHeaderText("Modifier : " + selected.getUsername());

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField usernameField = new TextField(selected.getUsername());
            ComboBox<User.Role> roleCombo = new ComboBox<>();
            roleCombo.getItems().addAll(User.Role.values());

            grid.add(new Label("Nom d'utilisateur :"), 0, 0);
            grid.add(usernameField, 1, 0);
            grid.add(new Label("Rôle :"), 0, 1);
            grid.add(roleCombo, 1, 1);

            roleCombo.setValue(selected.getRole());

            dialog.getDialogPane().setContent(grid);

            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        if (usernameField.getText().isEmpty() ||
                            roleCombo.getValue() == null) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Champs requis manquants");
                            alert.setContentText("Veuillez remplir tous les champs obligatoires : Nom d'utilisateur, Rôle.");
                            alert.showAndWait();
                            return;
                        }

                        selected.setUsername(usernameField.getText());
                        selected.setRole(roleCombo.getValue());

                        userService.save(selected);
                        refreshTable();
                        Stage stage = (Stage) userTable.getScene().getWindow();
                        ToastNotification.show(stage, "Utilisateur modifié avec succès !");
                    } catch (Exception e) {
                        GlobalExceptionHandler.handle(e);
                    }
                }
            });
        }
    }

    @FXML
    private void handleDelete() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner un utilisateur");
            alert.showAndWait();
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setContentText("Voulez-vous vraiment supprimer " + selected.getUsername() + " ?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        userService.delete(selected.getId());
                        refreshTable();
                    } catch (Exception e) {
                        GlobalExceptionHandler.handle(e);
                    }
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
            ObservableList<User> results = FXCollections.observableArrayList(
                    userService.searchByName(query)
            );
            userTable.setItems(results);
        }
    }

    @FXML
    private void handleHistory() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner un utilisateur");
            alert.showAndWait();
        } else {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Historique de " + selected.getUsername());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            TableView<User> table = new TableView<>();
            table.setPrefWidth(600);
            table.setPrefHeight(400);
        }
    }

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        ObservableList<User> users = FXCollections.observableArrayList(userService.findAll());
        userTable.setItems(users);
    }
}
