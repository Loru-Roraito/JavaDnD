package com.dnd.ui.tooltip;

import com.dnd.DefinitionManager;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class TooltipTitledPane extends TitledPane {

    private final Label titleLabel; // Label for the title

    // Constructor that accepts the title text and tooltip key
    public TooltipTitledPane(String title, Node content) {
        super(null, content);
        this.titleLabel = new Label(title); // Create a label for the title
        assignTooltip(title); // Assign the tooltip to the title
        this.setGraphic(titleLabel); // Set the label as the title of the TitledPane
    }

    // Assign a tooltip to the title label
    private void assignTooltip(String tooltipKey) {
        DefinitionManager.getInstance().assignTooltip(titleLabel, tooltipKey);
    }
}