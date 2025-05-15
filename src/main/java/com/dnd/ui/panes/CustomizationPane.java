package com.dnd.ui.panes;

import com.dnd.TranslationManager;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.characters.GameCharacter;

import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class CustomizationPane extends GridPane {
    public CustomizationPane(TabPane mainTabPane, AbilitiesPane abilitiesPane, GameCharacter character) {
        TooltipLabel generationLabel = new TooltipLabel(getTranslation("GENERATION_METHOD"), mainTabPane);
        add(generationLabel, 0, 0); // Add the label to the GridPane (Column 0, Row 0)

        TooltipComboBox<String> generationComboBox = new TooltipComboBox<>(mainTabPane);
        generationComboBox.getItems().addAll(getTranslation("CUSTOM_M"), getTranslation("RANDOM"), getTranslation("POINT_BUY"), getTranslation("STANDARD_ARRAY"));
        generationComboBox.setPromptText(getTranslation("STANDARD_ARRAY"));
        add(generationComboBox, 0, 1); // Add the ComboBox to the GridPane (Column 0, Row 1)

        generationComboBox.valueProperty().bindBidirectional(character.getGenerationMethod());

        generationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            abilitiesPane.chooseAbilitiesUI();
        });
    }   

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }
}