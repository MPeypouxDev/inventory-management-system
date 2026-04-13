package com.stockmanager.controller;

import com.stockmanager.config.GlobalExceptionHandler;
import com.stockmanager.service.SupplierService;
import com.stockmanager.model.Supplier;
import com.stockmanager.util.ToastNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ApplicationContext springContext;

    @FXML
    TableView<Supplier> supplierTable;

    @FXML
    TextField searchField;

    @FXML
    private TableColumn<Supplier, Integer> idColumn;

    @FXML
    private TableColumn<Supplier, String> nameColumn;

    @FXML
    private TableColumn<Supplier, String> contactColumn;

    @FXML
    private TableColumn<Supplier, String> phoneColumn;

    @FXML
    private TableColumn<Supplier, String> mailColumn;

    @FXML
    private TableColumn<Supplier, String> addressColumn;

    @FXML
    private void handleAdd() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un fournisseur");
        dialog.setHeaderText("Nouveau fournisseur");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField contactField = new TextField();
        TextField phoneField = new TextField();
        TextField mailField = new TextField();
        TextField addressField = new TextField();

        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Contact :"), 0, 1);
        grid.add(contactField, 1, 1);
        grid.add(new Label("Téléphone :"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Email :"), 0, 3);
        grid.add(mailField, 1, 3);
        grid.add(new Label("Adresse :"), 0, 4);
        grid.add(addressField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                try {
                    if (nameField.getText().isEmpty() ||
                            contactField.getText().isEmpty() ||
                            mailField.getText().isEmpty()) {

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Champs requis manquants");
                        alert.setContentText("Veuillez remplir tous les champs obligatoires : Nom, Contact, Email.");
                        alert.showAndWait();
                        return;
                    }

                    Supplier supplier = new Supplier();
                    supplier.setName(nameField.getText());
                    supplier.setContact(contactField.getText());
                    supplier.setTelephone(phoneField.getText());
                    supplier.setEmail(mailField.getText());
                    supplier.setAddress(addressField.getText());

                    supplierService.save(supplier);
                    refreshTable();
                    Stage stage = (Stage) supplierTable.getScene().getWindow();
                    ToastNotification.show(stage, "Fournisseur ajouté avec succès !");
                } catch (Exception e) {
                    GlobalExceptionHandler.handle(e);
                }
            }
        });
    }

    private void refreshTable() {
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList(supplierService.findAll());
        supplierTable.setItems(suppliers);
    }

    @FXML
    private void handleEdit() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner le fournisseur à modifier");
            alert.showAndWait();
        } else {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Modifier un fournisseur");
            dialog.setHeaderText("Modifier : " + selected.getName());

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nameField = new TextField(selected.getName());
            TextField contactField = new TextField(selected.getContact());
            TextField phoneField = new TextField(selected.getTelephone());
            TextField mailField = new TextField(selected.getEmail());
            TextField addressField = new TextField(selected.getAddress());

            grid.add(new Label("Nom :"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Contact :"), 0, 1);
            grid.add(contactField, 1, 1);
            grid.add(new Label("Téléphone :"), 0, 2);
            grid.add(phoneField, 1, 2);
            grid.add(new Label("Email :"), 0, 3);
            grid.add(mailField, 1, 3);
            grid.add(new Label("Adresse :"), 0, 4);
            grid.add(addressField, 1, 4);

            dialog.getDialogPane().setContent(grid);

            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    try {
                        if (nameField.getText().isEmpty() ||
                                contactField.getText().isEmpty() ||
                                mailField.getText().isEmpty()) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Champs requis manquants");
                            alert.setContentText("Veuillez remplir tous les champs obligatoires : Nom, Contact, Email.");
                            alert.showAndWait();
                            return;
                        }

                        selected.setName(nameField.getText());
                        selected.setContact(contactField.getText());
                        selected.setTelephone(phoneField.getText());
                        selected.setEmail(mailField.getText());
                        selected.setAddress(addressField.getText());

                        supplierService.save(selected);
                        refreshTable();
                        Stage stage = (Stage) supplierTable.getScene().getWindow();
                        ToastNotification.show(stage, "Fournisseur modifié avec succès !");
                    } catch (Exception e) {
                        GlobalExceptionHandler.handle(e);
                    }
                }
            });
        }
    }

    @FXML
    private void handleDelete() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner un fournisseur");
            alert.showAndWait();
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setContentText("Voulez-vous vraiment supprimer " + selected.getName() + " ?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    try {
                        supplierService.delete(selected.getId());
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
            ObservableList<Supplier> results = FXCollections.observableArrayList(
                    supplierService.searchByName(query)
            );
            supplierTable.setItems(results);
        }
    }

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        ObservableList<Supplier> suppliers = FXCollections.observableArrayList(supplierService.findAll());
        supplierTable.setItems(suppliers);
    }
}
