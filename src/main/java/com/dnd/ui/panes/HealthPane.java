package com.dnd.ui.panes;

import com.dnd.characters.GameCharacter;

import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class HealthPane extends GridPane {
    public HealthPane(GameCharacter character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");
    }
}
