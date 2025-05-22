package com.dnd.ui.panes;

import java.util.HashMap;
import java.util.Map;

import com.dnd.TranslationManager;
import com.dnd.characters.GameCharacter;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;


public class CustomizationPane extends GridPane {
    private final Map<String, String> generationsMap = new HashMap<>();
    private final Map<String, String> healthsMap = new HashMap<>();
    public CustomizationPane(TabPane mainTabPane, AbilitiesPane abilitiesPane, HealthPane healthPane, GameCharacter character) {
        TooltipLabel generationLabel = new TooltipLabel(getTranslation("GENERATION_METHOD"), mainTabPane);
        add(generationLabel, 0, 0); // Add the label to the GridPane (Column 0, Row 0)

        ObservableList<String> generations = FXCollections.observableArrayList();
        for (String generationKey : getGroup(new String[] {"generation_methods"})) {
            String translatedGeneration = getTranslation(generationKey);
            generations.add(translatedGeneration);
            generationsMap.put(translatedGeneration, generationKey); // Map translated name to original key
        }

        TooltipComboBox<String> generationComboBox = new TooltipComboBox<>(generations, mainTabPane);
        generationComboBox.setPromptText(getTranslation("STANDARD_ARRAY"));
        add(generationComboBox, 0, 1); // Add the ComboBox to the GridPane (Column 0, Row 1)

        // Listen for ComboBox changes (Translated → English)
        generationComboBox.valueProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                String englishKey = generationsMap.get(newVal);
                if (englishKey != null && !englishKey.equals(character.getGenerationMethod().get())) {
                    character.getGenerationMethod().set(englishKey);
                }
            }
        });

        // Listen for character property changes (English → Translated)
        character.getGenerationMethod().addListener((_, _, newVal) -> {
            String translated = getTranslation(newVal);
            if (translated != null && !translated.equals(generationComboBox.getValue())) {
                generationComboBox.setValue(translated);
            }
        });

        generationComboBox.valueProperty().addListener((_, _, _) -> {
            abilitiesPane.chooseAbilitiesUI();
        });


        TooltipLabel healthLabel = new TooltipLabel(getTranslation("HEALTH_METHOD"), mainTabPane);
        add(healthLabel, 0, 2); // Add the label to the GridPane

        ObservableList<String> healths = FXCollections.observableArrayList();
        for (String healthKey : getGroup(new String[] {"health_methods"})) {
            String translatedHealth = getTranslation(healthKey);
            healths.add(translatedHealth);
            healthsMap.put(translatedHealth, healthKey); // Map translated name to original key
        }

        TooltipComboBox<String> healthComboBox = new TooltipComboBox<>(healths, mainTabPane);
        healthComboBox.setPromptText(getTranslation("MEDIUM_HP"));
        add(healthComboBox, 0, 3); // Add the ComboBox to the GridPane

        // Listen for ComboBox changes (Translated → English)
        healthComboBox.valueProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                String healthKey = healthsMap.get(newVal);
                if (healthKey != null && !healthKey.equals(character.getHealthMethod().get())) {
                    character.getHealthMethod().set(healthKey);
                }
            }
        });

        // Listen for character property changes (English → Translated)
        character.getHealthMethod().addListener((_, _, newVal) -> {
            String translated = getTranslation(newVal);
            if (translated != null && !translated.equals(healthComboBox.getValue())) {
                healthComboBox.setValue(translated);
            }
        });

        healthComboBox.valueProperty().addListener((_, _, _) -> {
            healthPane.chooseHealthUI();
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