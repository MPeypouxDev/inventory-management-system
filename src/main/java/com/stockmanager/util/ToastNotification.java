package com.stockmanager.util;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ToastNotification {

    public static void show(Stage stage, String message) {
        Platform.runLater(() -> {
            Popup popup = new Popup();

            Label label = new Label(message);
            label.setStyle(
                    "-fx-background-color: #2ecc71;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 12px 20px;" +
                    "-fx-background-radius: 5;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;"
            );

            StackPane pane = new StackPane(label);
            popup.getContent().add(pane);

            double x = stage.getX() + stage.getWidth() / 2 - 100;
            double y = stage.getY() + stage.getHeight() - 80;
            popup.show(stage, x, y);

            FadeTransition fade = new FadeTransition(Duration.seconds(2), pane);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setDelay(Duration.seconds(1.5));
            fade.setOnFinished(e -> popup.hide());
            fade.play();
        });
    }
}
