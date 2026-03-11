package com.dnd.frontend.tooltip;

import com.dnd.frontend.language.DefinitionManager;
import com.dnd.utils.items.Item;
import com.dnd.utils.items.Spell;

import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;

public class TooltipLabel extends Label {
    private final TabPane mainTabPane;
    private final Tooltip tooltip;
    private String tooltipKey;

    public TooltipLabel(Spell spell, TabPane mainTabPane) {
        super(spell.getName());
        this.mainTabPane = mainTabPane;
        tooltip = assignTooltip(spell);
        tooltipKey = spell.getName();
        setupKeyListener();
    }

    public TooltipLabel(Item item, TabPane mainTabPane) {
        super(item.getName());
        this.mainTabPane = mainTabPane;
        tooltip = assignTooltip(item);
        tooltipKey = item.getName();
        setupKeyListener();
    }

    // Constructor that uses the label's text as the tooltip key
    public TooltipLabel(String text, TabPane mainTabPane) {
        super(text); // Set the label's text
        this.mainTabPane = mainTabPane;
        tooltip = assignTooltip(text);
        tooltipKey = text;
        setupKeyListener();

        this.textProperty().addListener((_) -> {
            update(this.getText());
        });
    }

    // Constructor that uses the custom text as the tooltip key
    public TooltipLabel(String text, String tooltipKey, TabPane mainTabPane) {
        super(text); // Set the label's text
        this.mainTabPane = mainTabPane;
        tooltip = assignTooltip(tooltipKey);
        this.tooltipKey = tooltipKey;
        setupKeyListener();
    }

    public void update(String newTooltipKey) {
        tooltipKey = newTooltipKey;
        DefinitionManager.updateTooltip(this, tooltip, tooltipKey);
    }

    private Tooltip assignTooltip(String tooltipKey) {
        return DefinitionManager.assignTooltip(this, tooltipKey);
    }

    private Tooltip assignTooltip(Spell spell) {
        return DefinitionManager.assignTooltip(this, spell);
    }

    private Tooltip assignTooltip(Item item) {
        return DefinitionManager.assignTooltip(this, item);
    }

    private void setupKeyListener() {
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