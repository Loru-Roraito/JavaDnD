package com.dnd.frontend.tooltip;

import com.dnd.frontend.language.DefinitionManager;
import com.dnd.frontend.language.TranslationManager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Bounds;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class TooltipComboBox extends ComboBox<String> {
    private final TabPane mainTabPane;
    private String hoveredItem; // Store the currently hovered item
    private ListView<String> listView;
    private PopupControl popup;
    private TooltipLabel replacementLabel; // Label to show when disabled
    private final ObservableList<String> combinedItems; // Combined list with current selection
    private final ObservableList<String> sourceItems; // Reference to original items

    public TooltipComboBox(ObservableList<String> items, TabPane mainTabPane) {
        super(FXCollections.observableArrayList());
        this.mainTabPane = mainTabPane;
        sourceItems = items;
        combinedItems = FXCollections.observableArrayList(items);

        // Create a sorted view of the items
        SortedList<String> sortedItems = new SortedList<>(combinedItems);
        setItems(sortedItems);
        
        sortedItems.setComparator((s1, s2) -> {
            // RANDOM always first
            if (s1.equals(getTranslation("RANDOM"))) return -1;
            if (s2.equals(getTranslation("RANDOM"))) return 1;
            
            // Both are special or both are regular - alphabetical
            if (s1.matches("-?\\d+(\\.\\d+)?") && s2.matches("-?\\d+(\\.\\d+)?")) {
                double num1 = Double.parseDouble(s1);
                double num2 = Double.parseDouble(s2);
                return Double.compare(num1, num2);
            }
            return s1.compareTo(s2);
        });

        createPopup();
        assignTooltip();
        setupKeyListener();
        setupComboBoxScrolling();
        setupClickHandling();
        setupDisabledListener();
        updateLabel();
    }
    
    private void updateCombinedItems() {
        String currentValue = getValue();
        ObservableList<String> newItems = FXCollections.observableArrayList(sourceItems);
        
        // Add current selection if not already in the list
        if (currentValue != null && !newItems.contains(currentValue)) {
            newItems.add(currentValue);
        }
        
        combinedItems.setAll(newItems);
    }

    public TooltipLabel getLabel() {
        return replacementLabel;
    }

    private void setupDisabledListener() {
        replacementLabel = new TooltipLabel("", mainTabPane);
        replacementLabel.getStyleClass().add("combo-box-disabled-label");
        replacementLabel.setVisible(false);

        // Listen for disabled state changes
        this.disabledProperty().addListener((_, _, isDisabled) -> {
            if (isDisabled) {
                updateLabel();
                replacementLabel.setVisible(true);
                this.setVisible(false);
                this.setManaged(false);
            } else {
                replacementLabel.setVisible(false);
                this.setVisible(true);
                this.setManaged(true);
            }
        });

        this.valueProperty().addListener((_, _, _) -> {
            updateLabel();
        });
    }

    private void updateLabel() {
        // Update label text and tooltip
        String currentValue = getValue();
        if (currentValue != null) {
            // TODO: spaces
            replacementLabel.setText("   " + currentValue);
            replacementLabel.changeTooltip(currentValue);
            replacementLabel.changeDefinition(currentValue);
        } else {
            replacementLabel.setText("");
            replacementLabel.changeTooltip("");
            replacementLabel.changeDefinition("");
        }
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
        updateCombinedItems();
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
        comboBoxTooltip.setWrapText(true);
        comboBoxTooltip.setMaxWidth(300);
        comboBoxTooltip.setShowDuration(Duration.INDEFINITE); // Stay visible while hovering
        comboBoxTooltip.setAutoFix(true); // Automatically adjust position to stay on screen
    
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
        DefinitionManager.openDefinitionTab(text, mainTabPane);
    }

    private String fetchTooltip(String key) {
        return DefinitionManager.fetchTooltip(key);
    }

    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }
}