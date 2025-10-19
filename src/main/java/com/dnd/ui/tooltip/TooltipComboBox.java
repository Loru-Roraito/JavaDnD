package com.dnd.ui.tooltip;

import com.dnd.DefinitionManager;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
public class TooltipComboBox extends ComboBox<String> {
    private final TabPane mainTabPane;
    private String hoveredItem; // Store the currently hovered item
    private ListView<String> listView;
    private PopupControl popup;

    public TooltipComboBox(ObservableList<String> items, TabPane mainTabPane) {
        super(items);
        this.mainTabPane = mainTabPane;
        createPopup();
        assignTooltip();
        setupKeyListener();
        setupComboBoxScrolling();
        setupClickHandling();
    }

    public TooltipComboBox(TabPane mainTabPane) {
        super();
        this.mainTabPane = mainTabPane;
        createPopup();
        assignTooltip();
        setupKeyListener();
        setupComboBoxScrolling();
        setupClickHandling();
    }

    private void createPopup() {
        // Create the ListView that will be in the popup
        listView = new ListView<>(getItems());
        updateListViewSize();
        
        // Apply ComboBox popup CSS classes
        listView.getStyleClass().add("list-view");
        
        // Create custom cells with tooltips
        listView.setCellFactory(_ -> new ListCell<String>() {
            private final Tooltip tooltip = new Tooltip();

            {
                // Apply ComboBox list-cell styling
                this.getStyleClass().add("list-cell");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item);
                    tooltip.setText(fetchTooltip(item));
                    if (!tooltip.getText().isEmpty()) {
                        setTooltip(tooltip);
                    }
                }
            }

            {   // Event to track the hovered item
                this.setOnMouseEntered(_ -> hoveredItem = getItem());
                
                // Handle mouse clicks on cells
                this.setOnMouseClicked(event -> {
                    if (!isEmpty() && getItem() != null) {
                        setValue(getItem()); // Set the value (even if it's the same)
                        hide(); // Always close the popup
                        event.consume(); // Prevent event from bubbling up
                    }
                });
            }
        });

        // Create the popup
        popup = new PopupControl() {
            {
                // Create a container that will hold the ListView
                StackPane container = new StackPane();
                container.getStyleClass().add("combo-box-popup");
                container.getChildren().add(listView);
                
                // Set the container as the scene root
                getScene().setRoot(container);
                setAutoFix(true);
                setAutoHide(true);
                setHideOnEscape(true);
            }
        };
    }

    private double calculateCellHeight() {
        // Use system default font size or a reasonable default
        double fontSize = javafx.scene.text.Font.getDefault().getSize();
        return fontSize * 1.5 + 13; // 1.5x font size + 13px padding TODO: need to remove this part somehow
    }

    private void updateListViewSize() {
        Platform.runLater(() -> {
            try {
                int itemCount = getItems().size();
                int maxRows = Math.min(itemCount, 10);

                double cellHeight = calculateCellHeight();
                double padding = 2;
                double totalHeight = (maxRows * cellHeight) + padding;
                
                listView.setPrefHeight(totalHeight);
                listView.setMinHeight(totalHeight);
                listView.setMaxHeight(totalHeight);
                listView.setPrefWidth(this.getWidth());
                listView.setMinWidth(this.getWidth());
                listView.setMaxWidth(this.getWidth());
                listView.refresh(); // Force refresh after size change
            } catch (Exception e) {
                System.err.println("Error updating ListView size: " + e.getMessage());
            }
        });
    }

    @Override
    public void show() {
        updateListViewSize(); // Ensure size is updated before showing
        if (!popup.isShowing()) {
            Platform.runLater(() -> {
                if (!popup.isShowing()) { // Double-check in the runLater                    
                    // Get the bounds of this ComboBox
                    Bounds bounds = this.localToScreen(this.getBoundsInLocal());
                    popup.show(this, bounds.getMinX(), bounds.getMaxY());
                    scrollToSelectedItem();
                }
            });
        }
    }

    @Override
    public void hide() {
        popup.hide();
    }
    
    private void setupClickHandling() {
        this.setOnMouseClicked(event -> {
            if (popup.isShowing()) {
                hide();
            } else {
                show();
            }
            event.consume(); // Prevent event from bubbling up
        });
    }

    private void scrollToSelectedItem() {
        Platform.runLater(() -> {
            try {
                if (getValue() != null && listView.getItems() != null && !listView.getItems().isEmpty()) {
                    int selectedIndex = listView.getItems().indexOf(getValue());
                    if (selectedIndex >= 0 && selectedIndex < listView.getItems().size()) {
                        if (selectedIndex >= 5) {
                            listView.scrollTo(Math.max(0, selectedIndex - 3));
                        }
                        listView.getSelectionModel().select(selectedIndex);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error scrolling to selected item: " + e.getMessage());
            }
        });
    }

    private void setupComboBoxScrolling() {
        this.setOnShowing(_ -> scrollToSelectedItem());
    }

    // Direct access to ListView - no casting needed!
    public ListView<String> getListView() {
        return listView;
    }

    private void assignTooltip() {
        Tooltip comboBoxTooltip = new Tooltip();
        Tooltip.install(this, comboBoxTooltip);
        
        if (comboBoxTooltip.getText().isEmpty()) {
            Tooltip.uninstall(this, comboBoxTooltip);
        }
    
        this.valueProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                String tooltipText = fetchTooltip(newValue);
                comboBoxTooltip.setText(tooltipText.isEmpty() ? "" : tooltipText);
                Tooltip.install(this, comboBoxTooltip);
            }
            if (comboBoxTooltip.getText().isEmpty()) {
                Tooltip.uninstall(this, comboBoxTooltip);
            }
        });
    }

    private void setupKeyListener() {
        // Request focus when the mouse enters
        this.setOnMouseEntered(_ -> this.requestFocus());

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.T) {
                if (hoveredItem != null) {
                    openDefinitionTab(hoveredItem); // Use hovered item, not selected one
                }
            }
        });

        this.setFocusTraversable(true);
    }

    private void openDefinitionTab(String text) {
        DefinitionManager.getInstance().openDefinitionTab(text, mainTabPane);
    }

    private String fetchTooltip(String key) {
        return DefinitionManager.getInstance().fetchTooltip(key);
    }
}