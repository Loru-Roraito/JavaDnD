package com.dnd.ui.tooltip;

import com.dnd.DefinitionManager;
import com.dnd.items.Spell;

import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;

public class TooltipLabel extends Label {
    private final TabPane mainTabPane;
    // Constructor that uses the label's text as the tooltip key
    public TooltipLabel(Spell spell, TabPane mainTabPane) {
        super(spell.getName()); // Set the label's text
        this.mainTabPane = mainTabPane;
        assignTooltip(spell);
        Runnable openTab = () -> {
            DefinitionManager.getInstance().openDefinitionTab(spell, mainTabPane);
        };
        setupKeyListener(openTab);
    }

    // Constructor that uses the label's text as the tooltip key
    public TooltipLabel(String text, TabPane mainTabPane) {
        super(text); // Set the label's text
        this.mainTabPane = mainTabPane;
        assignTooltip(text);
        Runnable openTab = () -> {
            DefinitionManager.getInstance().openDefinitionTab(text, mainTabPane);
        };
        setupKeyListener(openTab);
    }

    // Constructor that uses the custom text as the tooltip key
    public TooltipLabel(String text, String tooltipKey, TabPane mainTabPane) {
        super(text); // Set the label's text
        this.mainTabPane = mainTabPane;
        assignTooltip(tooltipKey);
        Runnable openTab = () -> {
            DefinitionManager.getInstance().openDefinitionTab(text, mainTabPane);
        };
        setupKeyListener(openTab);
    }

    private void assignTooltip(String tooltipKey) {
        DefinitionManager.getInstance().assignTooltip(this, tooltipKey);
    }

    public void changeTooltip(String tooltipKey) {
        DefinitionManager.getInstance().assignTooltip(this, tooltipKey);
    }

    private void assignTooltip(Spell spell) {
        DefinitionManager.getInstance().assignTooltip(this, spell);
    }

    // Set up a key listener for the "T" key
    private void setupKeyListener(Runnable openTab) {
        // Request focus when the mouse enters the label
        this.setOnMouseEntered(_ -> this.requestFocus());

        // Add a key listener for the "T" key
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.T) {
                openTab.run();
            }
        });

        // Ensure the label is focusable to capture key events
        this.setFocusTraversable(true);
    }

    public void changeDefinition(String text) {
        Runnable newRunnable = () -> {
            DefinitionManager.getInstance().openDefinitionTab(text, mainTabPane);
        };
        setupKeyListener(newRunnable);
    }
}