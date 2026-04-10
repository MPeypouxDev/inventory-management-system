package com.stockmanager.config;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import org.springframework.stereotype.Component;

@Component
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

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
            alert.setTitle("Erreur innatendue");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText(getReadableMessage(throwable));
            alert.showAndWait();
        });
        throwable.printStackTrace();
    }

    private String getReadableMessage(Throwable throwable) {
        if (throwable.getMessage() != null && !throwable.getMessage().isEmpty()) {
            return throwable.getMessage();
        }
        return "Une erreur innatendue s'est produite. Veuillez réessayer.";
    }
}
