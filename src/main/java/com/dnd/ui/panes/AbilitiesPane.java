package com.dnd.ui.panes;

import java.util.ArrayList;
import java.util.List;

import com.dnd.ThrowManager;
import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tabs.InfoTab;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.ui.tooltip.TooltipTitledPane;
import com.dnd.utils.ComboBoxUtils;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AbilitiesPane extends GridPane {
    private final ViewModel character;
    private final TabPane mainTabPane;
    private final InfoTab infoTab;
    private final GridPane abilitiesSection = new GridPane();
    private final String[] abilityNames = getTranslations("abilities");
    private final List<TooltipComboBox> comboBoxes = new ArrayList<>();
    private final List<Button> minuses = new ArrayList<>();
    private final List<Label> labels = new ArrayList<>();
    private final List<Button> pluses = new ArrayList<>();
    private final List<Button> buttons = new ArrayList<>();
    private final List<TextField> customs= new ArrayList<>();
    private final List<ObservableList<String>> groupItemsList;

    private final Label points = new Label();

    public AbilitiesPane(ViewModel character, TabPane mainTabPane, InfoTab infoTab) {
        getStyleClass().add("grid-pane");
        this.character = character;
        this.mainTabPane = mainTabPane;
        this.infoTab = infoTab;
        this.groupItemsList = new ArrayList<>();
        // Create sections for abilities, skills, and saving throws
        abilitiesSection.getStyleClass().add("grid-pane");
        GridPane skillsSection = new GridPane();
        skillsSection.getStyleClass().add("grid-pane");
        GridPane saveThrowsSection = new GridPane();
        saveThrowsSection.getStyleClass().add("grid-pane");

        TooltipTitledPane abilitiesPane = new TooltipTitledPane(getTranslation("ABILITIES"), abilitiesSection);
        TooltipTitledPane skillsPane = new TooltipTitledPane(getTranslation("SKILLS"), skillsSection);
        TooltipTitledPane saveThrowsPane = new TooltipTitledPane(getTranslation("SAVE_THROWS"), saveThrowsSection);
        abilitiesPane.setCollapsible(false);
        skillsPane.setCollapsible(false);
        saveThrowsPane.setCollapsible(false);

        // Set up each section
        setupAbilitiesGeneration();
        setupSkills(skillsSection);
        setupSavingThrows(saveThrowsSection);
    
        // Add sections to the main VBox
        add(abilitiesPane, 0, 0);
        add(skillsPane, 1, 0);
        add(saveThrowsPane, 0, 1);

        GridPane.setRowSpan(skillsPane, 2);
    }

    private void setupAbilitiesGeneration() {
        for (int i = 0; i < abilityNames.length; i++) {
            int index = i; // Capture the index for use in the lambda
            
            String ability = abilityNames[i];

            // Ability name
            abilitiesSection.add(new TooltipLabel(ability, mainTabPane), 0, i); // Column 0, Row i

            // Checkboxes for +1 and +2 bonuses
            CheckBox bonusOne = new CheckBox();
            CheckBox bonusTwo = new CheckBox();
            abilitiesSection.add(bonusOne, 1, i); // Column 1, Row i
            abilitiesSection.add(bonusTwo, 2, i); // Column 2, Row i

            // This needs to be before the bidirectional binding, otherwise the value will be changed before the checkbox realizes it's selecated
            bonusOne.disableProperty().bind(character.getAvailablePlusOne(index).not());
            bonusTwo.disableProperty().bind(character.getAvailablePlusTwo(index).not());

            bonusOne.visibleProperty().bind(character.isEditing());
            bonusTwo.visibleProperty().bind(character.isEditing());
            bonusOne.managedProperty().bind(character.isEditing());
            bonusTwo.managedProperty().bind(character.isEditing());

            // Bind the CheckBox states to the Character's Boolean properties
            bonusOne.selectedProperty().bindBidirectional(character.getAbilityPlusOne(index));
            bonusTwo.selectedProperty().bindBidirectional(character.getAbilityPlusTwo(index));

            bonusOne.disabledProperty().addListener((_, _, newValue) -> {
                if (newValue) {
                    bonusOne.setSelected(false); // Uncheck the box if disabled
                }
            });
            
            bonusTwo.disabledProperty().addListener((_, _, newValue) -> {
                if (newValue) {
                    bonusTwo.setSelected(false); // Uncheck the box if disabled
                }
            });

            // Final value display
            Label finalValue = new Label();
            // Bind the text of the label to the ability value and modifier
            finalValue.textProperty().bind(
                character.getAbility(index).asString().concat(" (").concat(character.getAbilityModifier(index).asString()).concat(")")
            );
            abilitiesSection.add(finalValue, 6, i);
        }

        // Generate abilities UI based on the generation type
        generateAbilitiesUI();
    }

    private void generateAbilitiesUI() {
        // Define the starting values (8, 10, 12, 13, 14, 15)
        String[] startingValues = {getTranslation("RANDOM"), "8", "10", "12", "13", "14", "15"};

        // Loop through abilities and add them to the GridPane
        for (int i = 0; i < abilityNames.length; i++) {
            int index = i; // Capture the index for use in the lambda

            // ComboBox for selecting the starting value
            ObservableList<String> baseValues = FXCollections.observableArrayList(startingValues);
            TooltipComboBox comboBox = new TooltipComboBox(baseValues, mainTabPane);

            // Add the ComboBox to the list
            comboBoxes.add(comboBox);
            groupItemsList.add(baseValues);
            comboBox.setPromptText(getTranslation("RANDOM"));

            // Bind the selected value of the ComboBox to the corresponding ability in the Character class
            comboBox.valueProperty().addListener((_, _, _) -> {
                if (character.getGenerationMethod().get().equals(getTranslation("STANDARD_ARRAY"))) {
                    // When any comboBox changes, refresh all lists
                    for (int j = 0; j < comboBoxes.size(); j++) {
                        ComboBoxUtils.updateItems(comboBoxes.get(j), comboBoxes, groupItemsList.get(j), startingValues, new String[] {getTranslation("RANDOM")});
                    }
                }
            });

            Button minus = new Button("-");
            minuses.add(minus);
            Label label = new Label();
            labels.add(label);
            Button plus = new Button("+");
            pluses.add(plus);

            label.textProperty().bind(character.getAbilityBaseShown(i));

            // Add event handlers for the buttons
            minus.setOnAction(_ -> {
                character.AbilityBaseMinus(index);
            });

            plus.setOnAction(_ -> {
                character.AbilityBasePlus(index);
            });

            // Bind the disable property of the minus button
            minus.disableProperty().bind(character.getAvailableMinus(index).not());
            plus.disableProperty().bind(character.getAvailablePlus(index).not());

            Button button = new Button();
            buttons.add(button);

            button.setText(getTranslation("RANDOM"));
            button.textProperty().bindBidirectional(character.getAbilityBaseShown(i));

            button.setOnAction(_ -> {
                String currentValue = button.getText();
                if (currentValue.equals(getTranslation("RANDOM"))) {
                    int result = rollFourDropLowest();
                    button.setText(String.valueOf(result));
                } else {
                    button.setText(getTranslation("RANDOM"));
                }
            });

            TextField custom = new TextField();
            customs.add(custom);

            // Restrict input to positive integers
            custom.textProperty().addListener((_, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) { // Allow only digits
                    custom.setText(oldValue); // Revert to the old value if invalid input is detected
                }
            });

            custom.visibleProperty().bind(character.isEditing());
            custom.managedProperty().bind(character.isEditing());
        }

        chooseAbilitiesUI();
    }
        
    public void chooseAbilitiesUI() {
        String generationType = character.getGenerationMethod().get();

        for (int i = 0; i < abilityNames.length; i++) {
            ComboBox<String> comboBox = comboBoxes.get(i);
            Button minus = minuses.get(i);
            Label label = labels.get(i);
            Button plus = pluses.get(i);
            Button button = buttons.get(i);
            TextField custom = customs.get(i);

            if (generationType.equals(getTranslation("STANDARD_ARRAY"))) {
                comboBox.valueProperty().bindBidirectional(character.getAbilityBaseShown(i));

                abilitiesSection.add(comboBox, 3, i); // Column 3, Row i
            } else {
                comboBox.valueProperty().unbindBidirectional(character.getAbilityBaseShown(i));

                abilitiesSection.getChildren().remove(comboBox);
            }

            if (generationType.equals(getTranslation("POINT_BUY"))) {
                abilitiesSection.add(minus, 3, i);
                abilitiesSection.add(label, 4, i);
                abilitiesSection.add(plus, 5, i);
            } else {
                abilitiesSection.getChildren().remove(minus);
                abilitiesSection.getChildren().remove(label);
                abilitiesSection.getChildren().remove(plus);
            }

            if (generationType.equals(getTranslation("CUSTOM_M"))) {
                abilitiesSection.add(custom, 3, i);
                custom.textProperty().bindBidirectional(character.getAbilityBaseShown(i));
            } else {
                abilitiesSection.getChildren().remove(custom);
                custom.textProperty().unbindBidirectional(character.getAbilityBaseShown(i));
                custom.textProperty().set("10"); // Clear the text field
            }

            if (generationType.equals(getTranslation("RANDOM"))) {
                button.setText(getTranslation("RANDOM"));

                abilitiesSection.add(button, 3, i); // Column 3, Row i
            } else {
                abilitiesSection.getChildren().remove(button);
            }
        }

        if (generationType.equals(getTranslation("POINT_BUY"))) {
            abilitiesSection.add(points, 0, 6);
            points.textProperty().bind(
                Bindings.concat(getTranslation("POINTS"), ": ", character.getGenerationPoints().asString())
            );
        } else {
            abilitiesSection.getChildren().remove(points);
        }
    }

    private void setupSkills(GridPane skillsArea) {
        // Fetch the skills
        String[] skillNames = getTranslations("skills");

        for (int i = 0; i < skillNames.length; i++) {
            int index = i; // Capture the index for use in the lambda

            String skill = skillNames[i];

            // Skill name
            TooltipLabel label = new TooltipLabel(skill, mainTabPane);
            skillsArea.add(label, 0, i); // Column 0, Row i

            // Checkboxes for proficiency and specialty
            CheckBox proficiency = new CheckBox();
            skillsArea.add(proficiency, 1, i); // Column 1, Row i

            proficiency.disableProperty().bind(character.getAvailableSkill(index).not().or(character.isEditing().not()));
            proficiency.selectedProperty().bindBidirectional(character.getSkillProficiency(index));
            
            // Button to roll a D20
            Button rollButton = new Button("0");
            rollButton.textProperty().bind(
                character.getSkillModifier(index).asString()
            );

            // Add a listener to the button to roll
            boolean isStealth = skill.equals(getTranslation("STEALTH"));
            rollButton.setOnAction(_ -> {
                infoTab.throwDie(1, 20, 0, character.getSkillModifier(index).get(), false,
                    character.getPoisoned().get() || (isStealth && character.getArmor().get().getStealth()),
                    character.getSkillAbilities()[index]);
            });
            skillsArea.add(rollButton, 3, i); // Column 3, Row i
        }
    }

    private void setupSavingThrows(GridPane savingThrowsArea) {
        // Define the saving throws
        String[] savingThrowNames = getTranslations("abilities");
    
        for (int i = 0; i < savingThrowNames.length; i++) {
            int index = i; // Capture the index for use in the lambda

            String savingThrow = savingThrowNames[i];
            
            // Saving throw name
            TooltipLabel label = new TooltipLabel(savingThrow, mainTabPane);
            savingThrowsArea.add(label, 0, i); // Column 0, Row i
    
            // Checkboxes for proficiency and specialty
            CheckBox proficiency = new CheckBox();
            savingThrowsArea.add(proficiency, 1, i); // Column 1, Row i

            proficiency.selectedProperty().bind(character.getSavingThrowProficiency(index));
            proficiency.setDisable(true);
    
            // Button to roll a D20
            Button rollButton = new Button("0");
            rollButton.textProperty().bind(
                character.getSavingThrowModifier(index).asString()
            );

            // Add a listener to the button to roll
            switch (index) {
                case 0 -> // Strength saving throw
                    rollButton.setOnAction(_ -> {
                        infoTab.throwDie(1, 20, 0, character.getSavingThrowModifier(index).get(), false, false,
                        character.getParalyzed().get() || character.getPetrified().get() || character.getStunned().get() || character.getUnconscious().get(),
                        index);
                    });
                case 1 -> // Dexterity saving throw
                    rollButton.setOnAction(_ -> {
                        infoTab.throwDie(1, 20, 0, character.getSavingThrowModifier(index).get(), false, false,
                        character.getParalyzed().get() || character.getPetrified().get() || character.getStunned().get() || character.getUnconscious().get() || character.getRestrained().get(),
                        index);
                    });
                default -> 
                    rollButton.setOnAction(_ -> {
                        infoTab.throwDie(1, 20, 0, character.getSavingThrowModifier(index).get(), false, false, index);
                    });
            }
            savingThrowsArea.add(rollButton, 2, index); // Column 2, Row i
        }
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String[] getTranslations(String key) {
        return TranslationManager.getInstance().getTranslations(key);
    }

    private int rollFourDropLowest() {
        return ThrowManager.getInstance().rollFourDropLowest();
    }
}