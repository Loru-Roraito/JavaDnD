package com.dnd.frontend.tooltip;

import com.dnd.frontend.language.DefinitionManager;
import com.dnd.utils.items.Item;
import com.dnd.utils.items.Spell;

import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class TooltipLabel extends Label {
    private final TabPane mainTabPane;
    // Constructor that uses the label's text as the tooltip key
    public TooltipLabel(Spell spell, TabPane mainTabPane) {
        super(spell.getName()); // Set the label's text
        this.mainTabPane = mainTabPane;
        assignTooltip(spell);
        Runnable openTab = () -> {
            DefinitionManager.openDefinitionTab(spell, mainTabPane);
        };
        setupKeyListener(openTab);
    }

    public TooltipLabel(Item item, TabPane mainTabPane) {
        super(item.getName()); // Set the label's text
        this.mainTabPane = mainTabPane;
        assignTooltip(item);
        Runnable openTab = () -> {
            DefinitionManager.openDefinitionTab(item, mainTabPane);
        };
        setupKeyListener(openTab);
    }

    // Constructor that uses the label's text as the tooltip key
    public TooltipLabel(String text, TabPane mainTabPane) {
        super(text); // Set the label's text
        this.mainTabPane = mainTabPane;
        assignTooltip(text);
        Runnable openTab = () -> {
            DefinitionManager.openDefinitionTab(text, mainTabPane);
        };
        setupKeyListener(openTab);
    }

    // Constructor that uses the custom text as the tooltip key
    public TooltipLabel(String text, String tooltipKey, TabPane mainTabPane) {
        super(text); // Set the label's text
        this.mainTabPane = mainTabPane;
        assignTooltip(tooltipKey);
        Runnable openTab = () -> {
            DefinitionManager.openDefinitionTab(text, mainTabPane);
        };
        setupKeyListener(openTab);
    }

    private void assignTooltip(String tooltipKey) {
        DefinitionManager.assignTooltip(this, tooltipKey);
    }

    public void changeTooltip(String tooltipKey) {
        DefinitionManager.assignTooltip(this, tooltipKey);
    }

    private void assignTooltip(Spell spell) {
        DefinitionManager.assignTooltip(this, spell);
    }

    private void assignTooltip(Item item) {
        DefinitionManager.assignTooltip(this, item);
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
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                openTab.run();
            }
        });

        // Ensure the label is focusable to capture key events
        this.setFocusTraversable(true);
    }

    public void changeDefinition(String text) {
        Runnable newRunnable = () -> {
            DefinitionManager.openDefinitionTab(text, mainTabPane);
        };
        setupKeyListener(newRunnable);
    }
}