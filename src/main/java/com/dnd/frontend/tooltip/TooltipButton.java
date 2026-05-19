package com.dnd.frontend.tooltip;

import com.dnd.frontend.language.DefinitionManager;

import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;

public class TooltipButton extends Button {
    private final TabPane mainTabPane;
    private final Tooltip tooltip;
    private String tooltipKey;
    public TooltipButton(String text, TabPane mainTabPane) {
        super(text);
        this.mainTabPane = mainTabPane;
        tooltip = assignTooltip(text);
        tooltipKey = text;
        setupKeyListener(text);

        this.textProperty().addListener((_) -> {
            update(this.getText());
        });
    }

    public void update(String newTooltipKey) {
        tooltipKey = newTooltipKey;
        DefinitionManager.updateTooltip(this, tooltip, tooltipKey);
    }

    public TooltipButton(String text, String tooltipKey, TabPane mainTabPane) {
        super(text);
        this.mainTabPane = mainTabPane;
        this.tooltip = assignTooltip(tooltipKey);
        this.tooltipKey = tooltipKey;
        setupKeyListener(tooltipKey);
    }

    private Tooltip assignTooltip(String tooltipKey) {
        return DefinitionManager.assignTooltip(this, tooltipKey);
    }

    // Set up a key listener for the "T" key
    private void setupKeyListener(String text) {
        this.setOnMouseEntered(_ -> {
            if (!FrozenTooltipManager.isFrozen().get()) {
                this.requestFocus();
            }
        });

        // Add a key listener for the "T" key to freeze the tooltip in place
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.T) {
                FrozenTooltipManager.freeze(tooltip, this, mainTabPane);
            } else if (event.getCode() == KeyCode.F) {
                DefinitionManager.openDefinitionTab(tooltipKey, mainTabPane);
            }
        });

        // Ensure the label is focusable to capture key events
        this.setFocusTraversable(true);
    }
}