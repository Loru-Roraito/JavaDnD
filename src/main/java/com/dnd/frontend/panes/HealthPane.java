package com.dnd.frontend.panes;

import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.ViewModel;
import com.dnd.frontend.tabs.InfoTab;
import com.dnd.frontend.tooltip.TooltipLabel;
import com.dnd.utils.observables.ObservableInteger;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class HealthPane extends GridPane {
    ViewModel character;
    TextField hpCustom = new TextField();
    Label hpMedium = new Label();
    Label hpRandom = new Label();
    public HealthPane(ViewModel character, TabPane mainTabPane, InfoTab infoTab) {
        this.character = character;
        getStyleClass().add("grid-pane");

        HBox healthBox = new HBox();
        TooltipLabel hpLabel = new TooltipLabel(getTranslation("HIT_POINTS") + ":", mainTabPane);
        healthBox.getChildren().add(hpLabel);
        add(healthBox, 0, 0, 4, 1);

        TextField currentHp = new TextField();
        healthBox.getChildren().add(currentHp);
        currentHp.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allow only digits
                currentHp.setText(oldValue); // Revert to the old value if invalid input is detected
            }
        });

        currentHp.textProperty().addListener((_, _, newVal) -> {
            if (!newVal.isEmpty()) {
                character.getCurrentHealthShown().set(newVal);
            }
        });
        currentHp.setOnAction(_ -> {
            if (currentHp.getText().isEmpty()) {
                character.getCurrentHealthShown().set("0");
            }
        });

        character.getCurrentHealthShown().addListener(_ -> {
            currentHp.setText(character.getCurrentHealthShown().get());
        });
        currentHp.setText(character.getCurrentHealthShown().get());

        // TODO: dynamic
        currentHp.setMaxWidth(60);

        Label slash = new Label("/");
        healthBox.getChildren().add(slash);
        healthBox.getChildren().add(hpCustom);
        healthBox.getChildren().add(hpMedium);
        healthBox.getChildren().add(hpRandom);

        int[] hitDies = {6, 8, 10, 12};
        HBox hitDieBox = new HBox();
        for (int i = 0; i < 4; i++) {
            int index = i;
            Button button = new Button();

            Runnable updateButtonText = () -> {
                String text = character.getAvailableHitDie(index).get() + "/" + character.getMaximumHitDie(index).get() + " d" + hitDies[index];
                button.setText(text);
                if (character.getMaximumHitDie(index).get() == 0) {
                    button.setVisible(false);
                    button.setManaged(false);
                } else {
                    button.setVisible(true);
                    button.setManaged(true);
                }
            };

            button.disableProperty().bind(character.isShortResting().not());

            button.setOnAction(_ -> {
                if (character.getAvailableHitDie(index).get() <= 0) {
                    return;
                }
                character.getAvailableHitDie(index).set(character.getAvailableHitDie(index).get() - 1);
                int result = Math.max(1, infoTab.throwDie(1, hitDies[index], character.getAbilityModifier(2).get(), false, false, 2));
                character.getCurrentHealth().set(character.getCurrentHealth().get() + result);
            });

            updateButtonText.run();
            character.getAvailableHitDie(i).addListener(_ -> updateButtonText.run());
            character.getMaximumHitDie(i).addListener(_ -> updateButtonText.run());

            hitDieBox.getChildren().add(button);
        }
        add(hitDieBox, 0, 1, 4, 1); // Span across 4 columns
        
        TooltipLabel initiativeLabel = new TooltipLabel(getTranslation("INITIATIVE_BONUS") + ":", getTranslation("INITIATIVE_BONUS"), mainTabPane);
        add(initiativeLabel, 0, 2); // Add the label to the GridPane (Column 0, Row 0)
        GridPane.setColumnSpan(initiativeLabel, 3);

        Button initiativeButton = new Button(String.valueOf(character.getInitiativeBonus().get()));
        character.getInitiativeBonus().addListener(_ -> {
            initiativeButton.setText(String.valueOf(character.getInitiativeBonus().get()));
        });
        
        // Add a listener to the button to roll
        initiativeButton.setOnAction(_ -> {
            infoTab.throwDie(1, 20, character.getInitiativeBonus().get(), character.getInvisible().get(), !character.getIncapacitated().get(), -1);
        });
        add(initiativeButton, 3, 2);
            
        TooltipLabel armorClass = new TooltipLabel("", getTranslation("ARMOR_CLASS"), mainTabPane);
        Runnable updateArmorClass = () -> {
            armorClass.textProperty().set(
                getTranslation("ARMOR_CLASS") + ": " + character.getArmorClass().get()
            );
        };
        updateArmorClass.run();
        character.getArmorClass().addListener(_ -> updateArmorClass.run());

        add(armorClass, 0, 3); // Add the label to the GridPane
        GridPane.setColumnSpan(armorClass, 4);

        TooltipLabel proficiencyBonus = new TooltipLabel("", getTranslation("PROFICIENCY_BONUS"), mainTabPane);
        Runnable updateProficiencyBonus = () -> {
            proficiencyBonus.textProperty().set(
                getTranslation("PROFICIENCY_BONUS") + ": " + character.getProficiencyBonus().get()
            );
        };
        updateProficiencyBonus.run();
        character.getProficiencyBonus().addListener(_ -> updateProficiencyBonus.run());
        
        add(proficiencyBonus, 0, 4); // Add the label to the GridPane
        GridPane.setColumnSpan(proficiencyBonus, 4);

        generateHealthUI();
    }

    private void generateHealthUI() {
        // Restrict input to positive integers
        hpCustom.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allow only digits
                hpCustom.setText(oldValue); // Revert to the old value if invalid input is detected
            }
            if (!newValue.isEmpty()) {
                character.getHealth().set(Integer.parseInt(newValue));
            } else {
                character.getHealth().set(0);
            }
        });

        character.getHealth().addListener(newVal -> {
            hpCustom.setText(String.valueOf(newVal));
        });
        hpCustom.setText(String.valueOf(character.getHealth().get()));
        hpCustom.disableProperty().bind(character.isEditing().not());

        hpMedium.setText(String.valueOf(character.getHealth().get()));
        character.getHealth().addListener(newVal -> {
            hpMedium.setText(String.valueOf(newVal));
        });

        Runnable updateRandomText = () -> {
            int level = character.getTotalLevel().get();
            String fixed = String.valueOf(character.getFixedHealth().get());
            if (level > 1) {
                for (ObservableInteger hitDie : character.getHitDies()) {
                    fixed += " + " + (level - 1) + "d" + hitDie.get();
                }
            }
            hpRandom.textProperty().set(
                fixed
            );
        };
        updateRandomText.run();
        character.getTotalLevel().addListener(_ -> updateRandomText.run());
        character.getFixedHealth().addListener(_ -> updateRandomText.run());
        for (ObservableInteger hitDie : character.getHitDies()) {
            hitDie.addListener(_ -> updateRandomText.run());
        }

        chooseHealthUI();
    }

    public void chooseHealthUI() {
        String healthType = character.getHealthMethod().get();
        
        if (healthType.equals(getTranslation("MEDIUM_HP"))) {
            hpMedium.setVisible(true);
            hpMedium.setManaged(true);
        } else {
            hpMedium.setVisible(false);
            hpMedium.setManaged(false);
        }

        if (healthType.equals(getTranslation("RANDOM"))) {
            hpRandom.setVisible(true);
            hpRandom.setManaged(true);
        } else {
            hpRandom.setVisible(false);
            hpRandom.setManaged(false);
        }

        if (healthType.equals(getTranslation("CUSTOM_M"))) {
            hpCustom.setVisible(true);
            hpCustom.setManaged(true);
        } else {
            hpCustom.setVisible(false);
            hpCustom.setManaged(false);
        }
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }
}
