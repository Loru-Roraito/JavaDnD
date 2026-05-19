package com.dnd.frontend.tooltip;

import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import com.dnd.frontend.language.DefinitionManager;
import com.dnd.frontend.language.TranslationManager;

import javafx.collections.ObservableList;

public class TooltipListView extends ListView<String> {
    private final TabPane mainTabPane;
    private String hoveredItem;
    private final Tooltip tooltip;

    public TooltipListView(ObservableList<String> items, TabPane mainTabPane) {
        super(items);
        this.mainTabPane = mainTabPane;

        tooltip = assignTooltip();
        setupKeyListener();

        this.setCellFactory(_ -> new ListCell<String>() {
             @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isBlank()) {
                    setText(null);
                } else {
                    setText(getTranslation(item));
                }
            }

            {
                this.setOnMouseEntered(_ -> {
                    hoveredItem = getItem();
                    DefinitionManager.updateTooltip(this, tooltip, getTranslation(hoveredItem));
                });
            }
        });
    }

    private void setupKeyListener() {
        // Request focus when the mouse enters
        this.setOnMouseEntered(_ -> {
            if (!FrozenTooltipManager.isFrozen().get()) {
                this.requestFocus();
            }
        });
        
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.T) {
                FrozenTooltipManager.freeze(tooltip, this, mainTabPane);
            } else if (event.getCode() == KeyCode.F) {
                openDefinitionTab(getTranslation(hoveredItem));
            }
        });

        this.setFocusTraversable(true);
    }

    private Tooltip assignTooltip() {
        return DefinitionManager.assignTooltip(this, "");
    }

    private void openDefinitionTab(String text) {
        DefinitionManager.openDefinitionTab(text, mainTabPane);
    }
    
    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }
}
