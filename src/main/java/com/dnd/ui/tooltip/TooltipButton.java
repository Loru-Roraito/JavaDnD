package com.dnd.ui.tooltip;

import com.dnd.DefinitionManager;

import javafx.scene.control.Button;

public class TooltipButton extends Button {
    public TooltipButton(String text) {
        super(text);
        assignTooltip(text);
    }

    public TooltipButton(String text, String tooltipKey) {
        super(text);
        assignTooltip(tooltipKey);
    }

    private void assignTooltip(String tooltipKey) {
        DefinitionManager.getInstance().assignTooltip(this, tooltipKey);
    }
}