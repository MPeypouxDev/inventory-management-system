package com.stockmanager.config;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import org.springframework.stereotype.Component;

@Component
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    // Instance statique nécessaire pour l'utilisation dans les lambdas JavaFX ou l'injection Spring n'est pas disponible
    private static GlobalExceptionHandler instance;

    public GlobalExceptionHandler() {
        instance = this;
    }

    public static void handle(Throwable throwable) {
        if (instance != null) {
            instance.uncaughtException(Thread.currentThread(), throwable);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur inatendue");
            alert.setHeaderText("Une erreur inattendue s'est produite");
            alert.setContentText(getReadableMessage(throwable));
            alert.showAndWait();
        });
        throwable.printStackTrace();
    }

    private String getReadableMessage(Throwable throwable) {
        if (throwable.getMessage() != null && !throwable.getMessage().isEmpty()) {
            return throwable.getMessage();
        }
        return "Une erreur inattendue s'est produite. Veuillez réessayer.";
    }
}
