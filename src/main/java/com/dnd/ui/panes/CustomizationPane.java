package com.dnd.ui.panes;

import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;


public class CustomizationPane extends GridPane {
    public CustomizationPane(TabPane mainTabPane, AbilitiesPane abilitiesPane, HealthPane healthPane, ViewModel character) {
        TooltipLabel generationLabel = new TooltipLabel(getTranslation("GENERATION_METHOD"), mainTabPane);
        add(generationLabel, 0, 0); // Add the label to the GridPane (Column 0, Row 0)

        ObservableList<String> generations = FXCollections.observableArrayList();
        for (String generationKey : getGroup(new String[] {"generation_methods"})) {
            generations.add(getTranslation(generationKey));
        }

        TooltipComboBox<String> generationComboBox = new TooltipComboBox<>(generations, mainTabPane);
        generationComboBox.setPromptText(getTranslation("STANDARD_ARRAY"));
        add(generationComboBox, 0, 1); // Add the ComboBox to the GridPane (Column 0, Row 1)

        // Listen for ComboBox changes
        generationComboBox.valueProperty().bindBidirectional(character.getGenerationMethod());

        generationComboBox.valueProperty().addListener((_, oldVal, newVal) -> {
            if (oldVal == null || !oldVal.equals(newVal)){
                abilitiesPane.chooseAbilitiesUI();
            }
        });

        TooltipLabel healthLabel = new TooltipLabel(getTranslation("HEALTH_METHOD"), mainTabPane);
        add(healthLabel, 0, 2); // Add the label to the GridPane

        ObservableList<String> healths = FXCollections.observableArrayList();
        for (String healthKey : getGroup(new String[] {"health_methods"})) {
            healths.add(getTranslation(healthKey));
        }

        TooltipComboBox<String> healthComboBox = new TooltipComboBox<>(healths, mainTabPane);
        healthComboBox.setPromptText(getTranslation("MEDIUM_HP"));
        add(healthComboBox, 0, 3); // Add the ComboBox to the GridPane

        // Listen for ComboBox changes
        healthComboBox.valueProperty().bindBidirectional(character.getHealthMethod());

        healthComboBox.valueProperty().addListener((_, oldVal, newVal) -> {
            if (oldVal == null || !oldVal.equals(newVal)){
                healthPane.chooseHealthUI();
            }
        });
    }   

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String[] getGroup(String[] key) {
        return TranslationManager.getInstance().getGroup(key);
    }
}