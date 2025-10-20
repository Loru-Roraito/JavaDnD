package com.dnd.ui.tooltip;

import com.dnd.DefinitionManager;

import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;

public class TooltipLabel extends Label {

    private final TabPane mainTabPane; // Reference to the TabPane where new tabs will be added

    // Constructor that uses the label's text as the tooltip key
    public TooltipLabel(String text, TabPane mainTabPane) {
        super(text); // Set the label's text
        this.mainTabPane = mainTabPane;
        assignTooltip(text);
        setupKeyListener(text);
    }

    // Constructor that uses the custom text as the tooltip key
    public TooltipLabel(String text, String tooltipKey, TabPane mainTabPane) {
        super(text); // Set the label's text
        this.mainTabPane = mainTabPane;
        assignTooltip(tooltipKey);
        setupKeyListener(tooltipKey);
    }

    public TooltipLabel(String text, TabPane mainTabPane, String tooltip) {
        super(text); // Set the label's text
        this.mainTabPane = mainTabPane;
        forceTooltip(tooltip);
        setupKeyListener(text);
    }

    private void assignTooltip(String tooltipKey) {
        DefinitionManager.getInstance().assignTooltip(this, tooltipKey);
    }

    private void forceTooltip(String tooltip) {
        DefinitionManager.getInstance().forceTooltip(this, tooltip);
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