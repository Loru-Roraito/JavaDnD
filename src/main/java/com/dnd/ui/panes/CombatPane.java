package com.dnd.ui.panes;

import com.dnd.ViewModel;

import javafx.scene.layout.GridPane;
import javafx.scene.control.TabPane;

public class CombatPane extends GridPane {
    private final ViewModel character;
    public CombatPane(TabPane mainTabPane, ViewModel character) {
        getStyleClass().add("grid-pane");
        this.character = character;
        
    }
}