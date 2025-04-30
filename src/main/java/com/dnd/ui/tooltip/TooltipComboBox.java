package com.dnd.ui.tooltip;

import com.dnd.DefinitionManager;
import com.dnd.TooltipManager;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;

public class TooltipComboBox<T> extends ComboBox<T> {

    private final TabPane mainTabPane;
    private T hoveredItem; // Store the currently hovered item

    public TooltipComboBox(ObservableList<T> items, TabPane mainTabPane) {
        super(items);
        this.mainTabPane = mainTabPane;
        assignTooltip();
        setupKeyListener();
    }

    public TooltipComboBox(TabPane mainTabPane) {
        super();
        this.mainTabPane = mainTabPane;
        assignTooltip();
        setupKeyListener();
    }

    private void assignTooltip() {
        Tooltip comboBoxTooltip = new Tooltip();
        Tooltip.install(this, comboBoxTooltip);
        
        if (comboBoxTooltip.getText().isEmpty()) {
            Tooltip.uninstall(this, comboBoxTooltip);
        }

        this.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String tooltipText = fetchTooltip(newValue.toString());
                comboBoxTooltip.setText(tooltipText.isEmpty() ? "" : tooltipText);
                Tooltip.install(this, comboBoxTooltip);
            }
            if (comboBoxTooltip.getText().isEmpty()) {
                Tooltip.uninstall(this, comboBoxTooltip);
            }
        });

        this.setCellFactory(lv -> new ListCell<>() {
            private final Tooltip tooltip = new Tooltip();

            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item.toString());
                    tooltip.setText(fetchTooltip(item.toString()));
                    if (!tooltip.getText().isEmpty()) {
                        setTooltip(tooltip);
                    }
                }
            }

            {   // Event to track the hovered item
                this.setOnMouseEntered(event -> hoveredItem = getItem());
            }
        });
    }

    private void setupKeyListener() {
        // Request focus when the mouse enters
        this.setOnMouseEntered(event -> this.requestFocus());

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.T) {
                if (hoveredItem != null) {
                    openDefinitionTab(hoveredItem.toString()); // Use hovered item, not selected one
                }
            }
        });

        this.setFocusTraversable(true);
    }

    private void openDefinitionTab(String text) {
        DefinitionManager.getInstance().openDefinitionTab(text, mainTabPane);
    }

    private String fetchTooltip(String key) {
        return TooltipManager.getInstance().fetchTooltip(key);
    }
}