package com.stockmanager.controller;

import com.stockmanager.config.GlobalExceptionHandler;
import com.stockmanager.service.CategoryService;
import com.stockmanager.model.Category;
import com.stockmanager.util.ToastNotification;
import com.stockmanager.util.Pagination;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;

@Component
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ApplicationContext springContext;

    @FXML
    TableView<Category> categoryTable;

    @FXML
    TextField searchField;

    @FXML
    private TableColumn<Category, Integer> idColumn;

    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TableColumn<Category, String> descriptionColumn;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private Label pageLabel;

    @FXML
    private void handleAdd() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une catégorie");
        dialog.setHeaderText("Nouvelle catégorie");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField descriptionField = new TextField();

        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description :"), 0, 1);
        grid.add(descriptionField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent( response -> {
            if (response == ButtonType.OK) {

                try {
                    if (nameField.getText().isEmpty() ||
                            descriptionField.getText().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Champs requis manquants");
                        alert.setContentText("Veuillez remplir tous les champs obligatoires : Nom, Description.");
                        alert.showAndWait();
                        return;
                    }

                    Category category = new Category();
                    category.setName(nameField.getText());
                    category.setDescription(descriptionField.getText());

                    categoryService.save(category);
                    refreshTable();
                    Stage stage = (Stage) categoryTable.getScene().getWindow();
                    ToastNotification.show(stage, "Catégorie ajoutée avec succès !");
                } catch (Exception e) {
                    GlobalExceptionHandler.handle(e);
                }
            }
        });
    }

    private void refreshTable() {
        updateTable(categoryService.findAll());
    }

    @FXML
    private void handleEdit() {
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner une catégorie à modifier");
            alert.showAndWait();
        } else {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Modifier une catégorie");
            dialog.setHeaderText("Modifier : " + selected.getName());

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nameField = new TextField(selected.getName());
            TextField descriptionField = new TextField(selected.getDescription());

            grid.add(new Label("Nom :"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Description :"), 0, 1);
            grid.add(descriptionField, 1, 1);

            dialog.getDialogPane().setContent(grid);

            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    try {
                        if (nameField.getText().isEmpty() ||
                                descriptionField.getText().isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Champs requis manquants");
                            alert.setContentText("Veuillez remplir tous les champs obligatoires: Nom, Description");
                            alert.showAndWait();
                            return;
                        }

                        selected.setName(nameField.getText());
                        selected.setDescription(descriptionField.getText());

                        categoryService.save(selected);
                        refreshTable();
                        Stage stage = (Stage) categoryTable.getScene().getWindow();
                        ToastNotification.show(stage, "Catégorie modifiée avec succès !");
                    } catch (Exception e) {
                        GlobalExceptionHandler.handle(e);
                    }
                }
            });
        }
    }

    @FXML
    private void handleDelete() {
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez sélectionner une catégorie");
            alert.showAndWait();
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setContentText("Voulez-vous vraiment supprimer " + selected.getName());
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    try {
                        categoryService.delete(selected.getId());
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
            ObservableList<Category> results = FXCollections.observableArrayList(
                    categoryService.searchByName(query)
            );
            categoryTable.setItems(results);
        }
    }

    @FXML
    private void handleNextPage() {
        List<Category> all = categoryService.findAll();
        pagination.nextPage(all);
        updateTable(all);
    }

    @FXML
    private void handlePreviousPage() {
        List<Category> all = categoryService.findAll();
        pagination.previousPage();
        updateTable(all);
    }

    private void updateTable(List<Category> allCategories) {
        categoryTable.setItems(FXCollections.observableArrayList(
                pagination.getPage(allCategories)
        ));
        pageLabel.setText("Page " + (pagination.getCurrentPage() + 1) +
                " / " + pagination.getTotalPages(allCategories));
        prevButton.setDisable(!pagination.hasPrevious());
        nextButton.setDisable(!pagination.hasNext(allCategories));
    }

    private final Pagination<Category> pagination = new Pagination<>(10);

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        updateTable(categoryService.findAll());
    }
}
