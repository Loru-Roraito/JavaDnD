package com.dnd.ui.panes;

import java.util.ArrayList;
import java.util.List;

import com.dnd.ThrowManager;
import com.dnd.TranslationManager;
import com.dnd.characters.GameCharacter;
import com.dnd.ui.tabs.InfoTab;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.ui.tooltip.TooltipTitledPane;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AbilitiesPane extends GridPane {
    private final GameCharacter character;
    private final TabPane mainTabPane;
    private final InfoTab infoTab;
    private final GridPane abilitiesSection = new GridPane();
    private final String[] abilityNames = getTranslations("ABILITIES");
    private final List<ComboBox<String>> comboBoxes = new ArrayList<>();
    private final List<Button> minuses = new ArrayList<>();
    private final List<Label> labels = new ArrayList<>();
    private final List<Button> pluses = new ArrayList<>();
    private final List<Button> buttons = new ArrayList<>();
    private final List<TextField> customs= new ArrayList<>();
    private final List<ChangeListener<String>> comboToCharListeners = new ArrayList<>();
    private final List<ChangeListener<String>> charToComboListeners = new ArrayList<>();
    private final List<ChangeListener<String>> buttonToCharListeners = new ArrayList<>();
    private final List<ChangeListener<String>> charToButtonListeners = new ArrayList<>();

    private final Label points = new Label();

    public AbilitiesPane(GameCharacter character, TabPane mainTabPane, InfoTab infoTab) {
        getStyleClass().add("grid-pane");
        this.character = character;
        this.mainTabPane = mainTabPane;
        this.infoTab = infoTab;
        // Create sections for abilities, skills, and saving throws
        abilitiesSection.getStyleClass().add("grid-pane");
        GridPane skillsSection = new GridPane();
        skillsSection.getStyleClass().add("grid-pane");
        GridPane saveThrowsSection = new GridPane();
        saveThrowsSection.getStyleClass().add("grid-pane");

        TooltipTitledPane abilitiesPane = new TooltipTitledPane(getTranslation("ABILITIES"), abilitiesSection);
        TooltipTitledPane skillsPane = new TooltipTitledPane(getTranslation("SKILLS"), skillsSection);
        TooltipTitledPane saveThrowsPane = new TooltipTitledPane(getTranslation("SAVE_THROWS"), saveThrowsSection);
    
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
            ChangeListener<String> fill = (_, _, _) -> {};
            comboToCharListeners.add(fill);
            charToComboListeners.add(fill);
            buttonToCharListeners.add(fill);
            charToButtonListeners.add(fill);
            int index = i; // Capture the index for use in the lambda
            
            String ability = abilityNames[i];

            // Ability name
            abilitiesSection.add(new TooltipLabel(ability, mainTabPane), 0, i); // Column 0, Row i

            // Checkboxes for +1 and +2 bonuses
            CheckBox bonus1 = new CheckBox();
            CheckBox bonus2 = new CheckBox();
            abilitiesSection.add(bonus1, 1, i); // Column 1, Row i
            abilitiesSection.add(bonus2, 2, i); // Column 2, Row i

            // Bind the CheckBox states to the Character's boolean properties
            bonus1.selectedProperty().bindBidirectional(character.getAbilityPlusOne(index));
            bonus2.selectedProperty().bindBidirectional(character.getAbilityPlusTwo(index));

            // Add listeners to the CheckBoxes to modify the ability value
            bonus1.selectedProperty().addListener((_, _, newValue) -> {
                if (newValue) {
                    character.addAbilityBonus(index, 1);  // Add +1
                } else {
                    character.addAbilityBonus(index, -1);  // Remove +1
                }
            });
            
            bonus2.selectedProperty().addListener((_, _, newValue) -> {
                if (newValue) {
                    character.addAbilityBonus(index, 2);  // Add +2
                } else {
                    character.addAbilityBonus(index, -2);  // Remove +2
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
        chooseAbilitiesUI(); // Default to standard array
    }

    private void generateAbilitiesUI() {
        // Define the starting values (8, 10, 12, 13, 14, 15)
        String[] startingValues = {getTranslation("RANDOM"), "8", "10", "12", "13", "14", "15"};

        // Loop through abilities and add them to the GridPane
        for (int i = 0; i < abilityNames.length; i++) {
            int index = i; // Capture the index for use in the lambda

            // ComboBox for selecting the starting value
            ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(startingValues));
                
            // Add the ComboBox to the list
            comboBoxes.add(comboBox);
            comboBox.setPromptText(getTranslation("RANDOM"));

            // Bind the selected value of the ComboBox to the corresponding ability in the Character class
            comboBox.valueProperty().addListener((_, oldValue, newValue) -> {
                if (newValue != null && oldValue != null && !oldValue.equals(getTranslation("RANDOM"))) {
                    // Add the old value back to all other ComboBoxes
                    for (int j = 0; j < comboBoxes.size(); j++) {
                        if (j != index) {
                            comboBoxes.get(j).getItems().add(oldValue);
        
                            // Sort the items to restore the original order
                            FXCollections.sort(comboBoxes.get(j).getItems(), (a, b) -> {
                                if (a.equals(getTranslation("RANDOM"))) return -1; // Keep "Random" at the top
                                if (b.equals(getTranslation("RANDOM"))) return 1;
                                return Integer.compare(Integer.parseInt(a), Integer.parseInt(b));
                            });
                        }
                    }
                }

                if (newValue != null && !newValue.equals(getTranslation("RANDOM"))) {
                    // Remove the new value from all other ComboBoxes
                    for (int j = 0; j < comboBoxes.size(); j++) {
                        if (j != index) {
                            comboBoxes.get(j).getItems().remove(newValue);
                        }
                    }
                }
            });

            Button minus = new Button("-");
            minuses.add(minus);
            Label label = new Label();
            labels.add(label);
            Button plus = new Button("+");
            pluses.add(plus);

            // Add event handlers for the buttons
            minus.setOnAction(_ -> {
                int currentValue = character.getAbilityBase(index).get(); // Get the current value from the character
                int currentPoints = character.getGenerationPoints().get(); // Get the current generation points
                int cost = getPointCost(currentValue);
                character.setGenerationPoints(currentPoints + cost); // Refund points
                character.setAbilityBase(index, currentValue - 1); // Update the character's base stat
            });

            plus.setOnAction(_ -> {
                int currentValue = character.getAbilityBase(index).get(); // Get the current value from the character
                int currentPoints = character.getGenerationPoints().get(); // Get the current generation points
                int cost = getPointCost(currentValue + 1);
                character.setGenerationPoints(currentPoints - cost); // Deduct points
                character.setAbilityBase(index, currentValue + 1); // Update the character's base stat
            });

            // Bind the disable property of the minus button
            minus.disableProperty().bind(
                character.getAbilityBase(index).lessThanOrEqualTo(8) // Disable if the value is <= 8
            );

            // Bind the disable property of the plus button
            plus.disableProperty().bind(
                character.getAbilityBase(index).greaterThanOrEqualTo(15) // Disable if the value is >= 15
                .or(Bindings.createBooleanBinding(
                    () -> character.getGenerationPoints().get() < getPointCost(character.getAbilityBase(index).get() + 1),
                    character.getGenerationPoints(), // Observe generation points
                    character.getAbilityBase(index)  // Observe ability base value
                ))
            );

            Button button = new Button();
            buttons.add(button);

            button.setText(getTranslation("RANDOM"));

            button.setOnAction(_ -> {
                String currentValue = character.getAbilityBaseProperty(index).get(); // Get the current value from the character
                if (currentValue.equals("RANDOM")) {
                    int result = rollFourDropLowest();
                    character.setAbilityBase(index, result);
                } else {
                    character.setAbilityBaseProperty(index, "RANDOM");
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
        }
    } 

    private int rollFourDropLowest() {
        // Roll 4 dice
        int[] rolls = new int[4];
        for (int i = 0; i < 4; i++) {
            rolls[i] = throwDice(1, 6, 0, 0, false, false); // Roll a d6
        }
        
        // Find the lowest value
        int lowest = 6;
        for (int roll : rolls) {
            if (roll < lowest) {
                lowest = roll;
            }
        }
    
        // Calculate the sum of the remaining three rolls
        int sum = 0;
        for (int roll : rolls) {
            sum += roll;
        }
        sum -= lowest; // Subtract the lowest roll
    
        return sum;
    }

    private int getPointCost(int score) {
        if (score <= 13) {
            return 1; // Cost is 1 point for scores 8–13
        } else {
            return 2; // Cost is 2 points for scores 14–15
        }
    }
        
    public void chooseAbilitiesUI() {
        String generationType = character.getGenerationMethod().get(); // Get the current generation type

        for (int i = 0; i < abilityNames.length; i++) {
            ComboBox<String> comboBox = comboBoxes.get(i);
            Button minus = minuses.get(i);
            Label label = labels.get(i);
            Button plus = pluses.get(i);
            Button button = buttons.get(i);
            TextField custom = customs.get(i);
            int index = i; // Capture the index for use in the lambda

            if (generationType.equals("STANDARD_ARRAY")) {
                character.resetAbilityBase(i); // Reset the ability bases to their default values

                // ComboBox → Character property
                ChangeListener<String> comboToChar = (_, _, newVal) -> {
                    if (newVal == null) return;
                    if (newVal.equals(getTranslation("RANDOM"))) {
                        character.getAbilityBaseProperty(index).set("RANDOM");
                    } else {
                        character.getAbilityBaseProperty(index).set(newVal);
                    }
                };
                comboBox.valueProperty().addListener(comboToChar);
                comboToCharListeners.set(i, comboToChar);

                // Character property → ComboBox
                ChangeListener<String> charToCombo = (_, _, newVal) -> {
                    if (newVal == null) return;
                    if (newVal.equalsIgnoreCase("RANDOM")) {
                        if (!getTranslation("RANDOM").equals(comboBox.getValue())) {
                            comboBox.setValue(getTranslation("RANDOM"));
                        }
                    } else {
                        if (!newVal.equals(comboBox.getValue())) {
                            comboBox.setValue(newVal);
                        }
                    }
                };
                character.getAbilityBaseProperty(i).addListener(charToCombo);
                charToComboListeners.set(i, charToCombo);

                abilitiesSection.add(comboBox, 3, i); // Column 3, Row i
            } else {
                comboBox.valueProperty().removeListener(comboToCharListeners.get(i));
                character.getAbilityBaseProperty(i).removeListener(charToComboListeners.get(i));

                abilitiesSection.getChildren().remove(comboBox);
            }

            if (generationType.equals("POINT_BUY")) {
                character.setAbilityBase(i, 8);

                label.textProperty().bindBidirectional(character.getAbilityBaseProperty(i));
                abilitiesSection.add(minus, 3, i);
                abilitiesSection.add(label, 4, i);
                abilitiesSection.add(plus, 5, i);
            } else {
                label.textProperty().unbindBidirectional(character.getAbilityBaseProperty(i));
                abilitiesSection.getChildren().remove(minus);
                abilitiesSection.getChildren().remove(label);
                abilitiesSection.getChildren().remove(plus);
            }

            if (generationType.equals("CUSTOM_M")) {
                abilitiesSection.add(custom, 3, i);
                custom.textProperty().bindBidirectional(character.getAbilityBaseProperty(i));
            } else {
                abilitiesSection.getChildren().remove(custom);
                custom.textProperty().unbindBidirectional(character.getAbilityBaseProperty(i));
                custom.textProperty().set("10"); // Clear the text field
            }

            if (generationType.equals("RANDOM")) {
                // button → Character property
                ChangeListener<String> buttonToChar = (_, _, newVal) -> {
                    if (newVal == null) return;
                    if (newVal.equals(getTranslation("RANDOM"))) {
                        character.getAbilityBaseProperty(index).set("RANDOM");
                    } else {
                        character.getAbilityBaseProperty(index).set(newVal);
                    }
                };
                button.textProperty().addListener(buttonToChar);
                buttonToCharListeners.set(i, buttonToChar);

                // Character property → button
                ChangeListener<String> charToButton = (_, _, newVal) -> {
                    if (newVal == null) return;
                    if (newVal.equalsIgnoreCase("RANDOM")) {
                        if (!getTranslation("RANDOM").equals(button.textProperty().getValue())) {
                            button.textProperty().setValue(getTranslation("RANDOM"));
                        }
                    } else {
                        if (!newVal.equals(button.textProperty().getValue())) {
                            button.textProperty().setValue(newVal);
                        }
                    }
                };
                character.getAbilityBaseProperty(i).addListener(charToButton);
                charToButtonListeners.set(i, charToButton);
                
                character.resetAbilityBase(i); // Reset the ability bases to their default values

                abilitiesSection.add(button, 3, i); // Column 3, Row i
            } else {
                button.textProperty().removeListener(buttonToCharListeners.get(i));
                character.getAbilityBaseProperty(i).removeListener(charToButtonListeners.get(i));

                abilitiesSection.getChildren().remove(button);
            }
        }

        if (generationType.equals("POINT_BUY")) {
            abilitiesSection.add(points, 0, 6);
            points.textProperty().bind(
                Bindings.concat(getTranslation("POINTS"), ": ", character.getGenerationPoints().asString())
            );
        } else {
            abilitiesSection.getChildren().remove(points);
            character.setGenerationPoints(27);
        }
    }

    private void setupSkills(GridPane skillsArea) {
        // Fetch the skills
        String[] skillNames = getTranslations("SKILLS");

        for (int i = 0; i < skillNames.length; i++) {
            int index = i; // Capture the index for use in the lambda

            String skill = skillNames[i];

            // Skill name
            TooltipLabel label = new TooltipLabel(skill, mainTabPane);
            skillsArea.add(label, 0, i); // Column 0, Row i

            // Checkboxes for proficiency and specialty
            CheckBox proficiency = new CheckBox();
            skillsArea.add(proficiency, 1, i); // Column 1, Row i

            proficiency.selectedProperty().bindBidirectional(character.getSkillProficiency(index));

            // Skill value display
            Label value = new Label();
            value.textProperty().bind(
                character.getSkillModifier(index).asString()
            );
            skillsArea.add(value, 3, i); // Column 3, Row i
            
            // Button to roll a D20
            Button rollButton = new Button(getTranslation("THROW"));

            // Add a listener to the button to roll
            rollButton.setOnAction(_ -> {
                infoTab.throwDie(1, 20, 0, character.getSkillModifier(index).get(), false, false);
            });
            skillsArea.add(rollButton, 4, i); // Column 4, Row i
        }
    }

    private void setupSavingThrows(GridPane savingThrowsArea) {
        // Define the saving throws
        String[] savingThrowNames = getTranslations("ABILITIES");
    
        for (int i = 0; i < savingThrowNames.length; i++) {
            int index = i; // Capture the index for use in the lambda

            String savingThrow = savingThrowNames[i];
            
            // Saving throw name
            TooltipLabel label = new TooltipLabel(savingThrow, mainTabPane);
            savingThrowsArea.add(label, 0, i); // Column 0, Row i
    
            // Checkboxes for proficiency and specialty
            CheckBox proficiency = new CheckBox();
            savingThrowsArea.add(proficiency, 1, i); // Column 1, Row i

            proficiency.selectedProperty().bindBidirectional(character.getSavingThrowProficiency(index));

            // Final value display
            Label value = new Label();
            // Bind the text of the label to the ability value and modifier
            value.textProperty().bind(
                character.getSavingThrowModifier(index).asString()
            );
            savingThrowsArea.add(value, 2, i); // Column 2, Row i
    
            // Button to roll a D20
            Button rollButton = new Button(getTranslation("THROW"));

            // Add a listener to the button to roll
            rollButton.setOnAction(_ -> {
                infoTab.throwDie(1, 20, 0, character.getSavingThrowModifier(index).get(), false, false);
            });
            savingThrowsArea.add(rollButton, 3, i); // Column 3, Row i
        }
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String[] getTranslations(String key) {
        return TranslationManager.getInstance().getTranslations(key);
    }

    // Method to update the die result
    public int throwDice(int times, int size, int base, int bonus, boolean advantage, boolean disadvantage) {
        return ThrowManager.getInstance().ThrowDice(times, size, base, bonus, advantage, disadvantage);
    }
}