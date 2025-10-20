package com.dnd.ui.tooltip;

import com.dnd.DefinitionManager;

import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;

public class TooltipButton extends Button {
    private final TabPane mainTabPane;
    public TooltipButton(String text, TabPane mainTabPane) {
        super(text);
        this.mainTabPane = mainTabPane;
        assignTooltip(text);
        setupKeyListener(text);
    }

    public TooltipButton(String text, String tooltipKey, TabPane mainTabPane) {
        super(text);
        this.mainTabPane = mainTabPane;
        assignTooltip(tooltipKey);
        setupKeyListener(tooltipKey);
    }

    private void assignTooltip(String tooltipKey) {
        DefinitionManager.getInstance().assignTooltip(this, tooltipKey);
    }

    // Set up a key listener for the "T" key
    private void setupKeyListener(String text) {
        // Request focus when the mouse enters the label
        this.setOnMouseEntered(_ -> this.requestFocus());

        // Add a key listener for the "T" key
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.T) {
                DefinitionManager.getInstance().openDefinitionTab(text, mainTabPane);
            }
        });

        // Ensure the label is focusable to capture key events
        this.setFocusTraversable(true);
    }
}