package com.dnd.ui.tabs;

import java.util.function.UnaryOperator;

import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipLabel;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

public class ExtraTab extends Tab{
    public ExtraTab(ViewModel character, TabPane mainTabPane){
        setText(getTranslation("EXTRA"));
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid-pane");
        
        TooltipLabel heightLabel = new TooltipLabel(getTranslation("HEIGHT"), mainTabPane);
        gridPane.add(heightLabel, 0, 0); // Add the label to the GridPane

        // This doesn't work with feet. Could be changed. Or you could use real measurement units.
        TextField height = new TextField();

        // TextFormatter to allow only numbers with up to 2 decimal digits, not starting with '.'
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            // Regex: optional digits, optional (dot and up to 2 digits), but not starting with dot
            if (newText.matches("^\\d+(\\.\\d{0,2})?$") || newText.isEmpty()) { // Allow empty input or it won't allow to delete the first digit
                return change;
            }
            return null;
        };
        height.setTextFormatter(new TextFormatter<>(filter));

        gridPane.add(height, 0, 1); // Add the label to the GridPane
        height.textProperty().bindBidirectional(character.getHeight());
        
        TooltipLabel weightLabel = new TooltipLabel(getTranslation("WEIGHT"), mainTabPane);
        gridPane.add(weightLabel, 0, 2); // Add the label to the GridPane

        TextField weight = new TextField();
        weight.setTextFormatter(new TextFormatter<>(filter));

        gridPane.add(weight, 0, 3); // Add the label to the GridPane
        weight.textProperty().bindBidirectional(character.getWeight());

        // Set the GridPane as the content of the tab
        setContent(gridPane);
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }
}