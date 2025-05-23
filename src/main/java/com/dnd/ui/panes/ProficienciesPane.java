package com.dnd.ui.panes;

import com.dnd.MiscsManager;
import com.dnd.ViewModel;

import javafx.beans.property.StringProperty;
import javafx.collections.MapChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ProficienciesPane extends GridPane {
    GridPane gridPane;
    public ProficienciesPane(ViewModel character, GridPane gridPane) {
        this.gridPane = gridPane;
        getStyleClass().add("grid-pane");

        VBox passivesBox = new VBox();

        // Initial population
        updatePassivesBox(passivesBox, character);

        // Listen for changes in the ObservableMap and update the VBox accordingly
        character.getPassives().addListener((MapChangeListener<StringProperty, Integer>) _ -> {
            updatePassivesBox(passivesBox, character);
        });

        add(passivesBox, 0, 0);
    }

    private void updatePassivesBox(VBox box, ViewModel character) {
        box.getChildren().clear();
        for (StringProperty property : character.getPassivesNames()) {
            Label label = new Label(property.get());
            label.setWrapText(true); // Enable text wrapping
            label.setMaxWidth(gridPane.widthProperty().get() * 0.2);  // Set your desired max width (adjust as needed)
            label.textProperty().set(fetchMisc(property.get()));
            box.getChildren().add(label);
        }
    }

    // Helper method to get translations
    private String fetchMisc(String key) {
        return MiscsManager.getInstance().fetchMisc(key);
    }
}