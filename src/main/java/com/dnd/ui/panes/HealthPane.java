package com.dnd.ui.panes;

import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tabs.InfoTab;
import com.dnd.ui.tooltip.TooltipLabel;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class HealthPane extends GridPane {
    ViewModel character;
    TextField hpCustom = new TextField();
    Label hpMedium = new Label();
    Button hpRandom = new Button();
    public HealthPane(ViewModel character, TabPane mainTabPane, InfoTab infoTab) {
        this.character = character;
        getStyleClass().add("grid-pane");

        TooltipLabel hpLabel = new TooltipLabel(getTranslation("HIT_POINTS") + ":", mainTabPane);
        add(hpLabel, 0, 0); // Add the label to the GridPane (Column 0, Row 0)
        
        TooltipLabel initiativeLabel = new TooltipLabel("", getTranslation("INITIATIVE_BONUS"), mainTabPane);
        initiativeLabel.textProperty().bind(
            Bindings.concat(
                getTranslation("INITIATIVE_BONUS"),
                ": ",
                character.getInitiativeBonus().asString()
            )
        );
        add(initiativeLabel, 0, 1); // Add the label to the GridPane (Column 0, Row 0)
        GridPane.setColumnSpan(initiativeLabel, 3);

        Button initiativeButton = new Button(getTranslation("THROW"));
        
        // Add a listener to the button to roll
        initiativeButton.setOnAction(_ -> {
            infoTab.throwDie(1, 20, 0, character.getInitiativeBonus().get(), false, false);
        });
        add(initiativeButton, 3, 1);
            
        TooltipLabel armorClass = new TooltipLabel("", getTranslation("ARMOR_CLASS"), mainTabPane);
        armorClass.textProperty().bind(
            Bindings.concat(
                getTranslation("ARMOR_CLASS"),
                ": ",
                character.getArmorClass().asString()
            )
        );
        add(armorClass, 0, 2); // Add the label to the GridPane
        GridPane.setColumnSpan(armorClass, 4);

        TooltipLabel proficiencyBonus = new TooltipLabel("", getTranslation("PROFICIENCY_BONUS"), mainTabPane);
        proficiencyBonus.textProperty().bind(
            Bindings.concat(
                getTranslation("PROFICIENCY_BONUS"),
                ": ",
                character.getProficiencyBonus().asString()
            )
        );
        add(proficiencyBonus, 0, 3); // Add the label to the GridPane
        GridPane.setColumnSpan(proficiencyBonus, 4);

        generateHealthUI();
    }

    private void generateHealthUI() {
        // Restrict input to positive integers
        hpCustom.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allow only digits
                hpCustom.setText(oldValue); // Revert to the old value if invalid input is detected
            }
        });

        hpMedium.textProperty().bind(
            character.getHealth().asString()
        );

        chooseHealthUI();
    }

    public void chooseHealthUI() {
        String healthType = character.getHealthMethod().get();
        
        if (healthType.equals(getTranslation("MEDIUM_HP"))) {
            add(hpMedium, 2, 0);
        } else {
            getChildren().remove(hpMedium);
        }

        if (healthType.equals(getTranslation("RANDOM"))) {
            add(hpRandom, 2, 0);
        } else {
            getChildren().remove(hpRandom);
        }

        if (healthType.equals(getTranslation("CUSTOM_M"))) {
            add(hpCustom, 2, 0);
        } else {
            getChildren().remove(hpCustom);
        }
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }
}
