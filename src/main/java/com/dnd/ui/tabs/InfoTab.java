package com.dnd.ui.tabs;

import com.dnd.ThrowManager;
import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.panes.AbilitiesPane;
import com.dnd.ui.panes.ClassPane;
import com.dnd.ui.panes.CustomizationPane;
import com.dnd.ui.panes.EquipmentPane;
import com.dnd.ui.panes.HealthPane;
import com.dnd.ui.panes.ParametersPane;
import com.dnd.ui.panes.ProficienciesPane;
import com.dnd.ui.panes.SystemPane;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.ui.tooltip.TooltipTitledPane;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class InfoTab extends Tab {
    // Create a GridPane
    private final GridPane gridPane = new GridPane(); // Class-level field for the GridPane
    private final Label dieResultLabel; // Shared label for die result
        
    public InfoTab(ViewModel character, TabPane mainTabPane){
        setText(getTranslation("INFO"));

        // Add a style class to the GridPane
        gridPane.getStyleClass().add("grid-pane");

        // Add subareas to the grid
        AbilitiesPane abilitiesPane = new AbilitiesPane(character, mainTabPane, this);
        addTitledPane("ABILITIES_AND_SKILLS", abilitiesPane, 0, 0, 4, 1);
        HealthPane healthPane = new HealthPane(character, mainTabPane, this);
        addTitledPane("HEALTH", healthPane, 0, 1, 1, 1);
        addTitledPane("PARAMETERS", new ParametersPane(character, mainTabPane), 0, 2, 3, 1);
        addTitledPane("CLASS", new ClassPane(character, mainTabPane), 3, 1, 2, 2);
        addTitledPane("PROFICIENCIES", new ProficienciesPane(character, gridPane, mainTabPane), 0, 3, 3, 1);
        addTitledPane("EQUIPMENT", new EquipmentPane(), 3, 3, 2, 1);
        addTitledPane("SYSTEM", new SystemPane(), 4, 0, 1, 1);
        addTitledPane("CUSTOMIZATION", new CustomizationPane(mainTabPane, abilitiesPane, healthPane, character), 2, 1, 1, 1);

        // Initialize the die result label
        dieResultLabel = new TooltipLabel(getTranslation("DIE"), mainTabPane); // Default text
        gridPane.add(dieResultLabel, 1, 1);

        // Set the GridPane as the content of the tab
        setContent(gridPane);
    }

    private void addTitledPane(String title, GridPane pane, int row, int column, int rowSpan, int columnSpan) {
        // Wrap the pane in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        TooltipTitledPane titledPane = new TooltipTitledPane(getTranslation(title), scrollPane);
        gridPane.add(titledPane, column, row);
        GridPane.setRowSpan(titledPane, rowSpan);
        GridPane.setColumnSpan(titledPane, columnSpan);
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    // Method to update the die result
    public void throwDie(int times, int size, int base, int bonus, boolean advantage, boolean disadvantage) {
        dieResultLabel.setText(String.valueOf(ThrowManager.getInstance().ThrowDice(times, size, base, bonus, advantage, disadvantage)));
    }
}