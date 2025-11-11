package com.dnd.utils;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Popup;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class DieToast {
    private static Popup currentPopup = null;

    public static void show(String message, Stage stage) {
        show(message, stage, 3000); // Default 3 seconds
    }

    public static void show(String message, Stage stage, long duration) {
        // Hide current toast if one exists
        if (currentPopup != null && currentPopup.isShowing()) {
            // Stop any ongoing animations
            currentPopup.hide();
        }
        
        Popup popup = new Popup();
        currentPopup = popup;
        
        Label label = new Label(message);
        label.getStyleClass().add("toast");
        
        StackPane pane = new StackPane(label);
        pane.setAlignment(Pos.CENTER);
        popup.getContent().add(pane);
        
        // Show popup in center of screen
        Scene scene = stage.getScene();
        double centerX = stage.getX() + scene.getWidth() / 2;
        double centerY = stage.getY() + scene.getHeight() / 2;

        popup.show(stage, centerX, centerY);

        // Center the popup after it's shown (to account for its size)
        popup.setX(centerX - label.getWidth() / 2);
        popup.setY(centerY - label.getHeight() / 2);
        
        // Fade in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        
        // Wait, then fade out and close
        PauseTransition pause = new PauseTransition(Duration.millis(duration));
        
        pause.setOnFinished(_ -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), pane);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(_ -> {
                popup.hide();
                if (currentPopup == popup) {
                    currentPopup = null;
                }
            });
            fadeOut.play();
        });
        pause.play();
    }
}