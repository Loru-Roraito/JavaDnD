package com.dnd.ui.panes;

import java.util.HashMap;
import java.util.Map;

import com.dnd.TranslationManager;
import com.dnd.characters.Character;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class ClassPane extends GridPane {
    private final Map<String, String> classMap = new HashMap<>();
    public ClassPane(Character character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");

        // Create a label as the title for the ComboBox
        TooltipLabel classeLabel = new TooltipLabel(getTranslation("CLASS"), mainTabPane);
        add(classeLabel, 0, 0); // Add the label to the GridPane (Column 0, Row 0)
        
        // Populate the class list and translation map
        ObservableList<String> classes = FXCollections.observableArrayList();
        for (String classKey : getGroup(new String[] {"CLASSES"})) {
            String translatedClass = getTranslation(classKey);
            classes.add(translatedClass);
            classMap.put(translatedClass, classKey); // Map translated name to original key
        }

        classes.add(0, getTranslation("RANDOM"));

        // Create the first ComboBox (class selection)
        TooltipComboBox<String> classComboBox = new TooltipComboBox<>(classes, mainTabPane);
        classComboBox.setPromptText(getTranslation("RANDOM"));
        add(classComboBox, 0, 1);

        // Bind the ComboBox's selected value to the Character's classe property
        classComboBox.valueProperty().bindBidirectional(character.getClasse());

        // Create a label as the title for the second ComboBox
        TooltipLabel subclassLabel = new TooltipLabel(getTranslation("SUBCLASS"), mainTabPane);
        add(subclassLabel, 0, 2); // Add the label to the GridPane (Column 0, Row 2)

        // Create the second ComboBox (subclass selection)
        ObservableList<String> subclasses = FXCollections.observableArrayList();
        TooltipComboBox<String> subclassComboBox = new TooltipComboBox<>(subclasses, mainTabPane);
        subclassComboBox.setPromptText(getTranslation("RANDOM"));
        add(subclassComboBox, 0, 3);
        
        // Bind the ComboBox's selected value to the Character's subclass property
        subclassComboBox.valueProperty().bindBidirectional(character.getSubclass());

        TooltipLabel levelLabel = new TooltipLabel(getTranslation("LEVEL"), mainTabPane);
        add(levelLabel, 0, 4); // Add the label to the GridPane
        
        ObservableList<String> levels = FXCollections.observableArrayList();
        
        ComboBox<String> levelComboBox = new ComboBox<>(levels);
        levelComboBox.setPromptText(getTranslation("RANDOM"));
        add(levelComboBox, 0, 5);

        levelComboBox.valueProperty().bindBidirectional(character.getLevelProperty());

        // Update the subclasses based on the selected class
        classComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Get the original key for the selected class
            String classKey = classMap.get(newValue);
            if (classKey != null) {
                subclasses.setAll(getTranslation("RANDOM"));
                subclassComboBox.valueProperty().set(getTranslation("RANDOM"));
                // Fetch the subclasses for the selected class
                String[] subclassArray = getGroupTranslations(new String[]{"CLASSES", classKey, "SUBCLASSES"});
                if (subclassArray.length > 0) {
                    subclasses.addAll(subclassArray);
                } else {
                    subclasses.clear(); // Clear if no subclasses are found
                }

                if (newValue.equals(getTranslation("RANDOM")) || newValue.equals(getTranslation("NONE_F"))) {
                    levels.clear();
                } else if (levels.isEmpty()) {
                    // Add "RANDOM" as the first item
                    levels.add(getTranslation("RANDOM"));
                    levelComboBox.valueProperty().set(getTranslation("RANDOM"));
            
                    // Add levels from 1 to 20
                    for (int i = 1; i <= 20; i++) {
                        levels.add(String.valueOf(i));
                    }
                }
            } else {
                subclasses.clear();
                levels.clear();
            }

            // Dynamically add or remove the subclass elements
            if (subclasses.isEmpty()) {
                getChildren().removeAll(subclassLabel, subclassComboBox); // Remove from GridPane
            } else {
                if (!getChildren().contains(subclassLabel)) {
                    add(subclassLabel, 0, 2); // Add back to GridPane
                }
                if (!getChildren().contains(subclassComboBox)) {
                    add(subclassComboBox, 0, 3); // Add back to GridPane
                }
            }
            
            if (levels.isEmpty()) {
                getChildren().removeAll(levelLabel, levelComboBox); // Remove from GridPane
            } else {
                if (!getChildren().contains(levelLabel)) {
                    add(levelLabel, 0, 4); // Add back to GridPane
                }
                if (!getChildren().contains(levelComboBox)) {
                    add(levelComboBox, 0, 5); // Add back to GridPane
                }
            }
        });

        getChildren().removeAll(subclassLabel, subclassComboBox, levelLabel, levelComboBox); // Initially remove specific elements
    }    

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String[] getGroup(String[] key) {
        return TranslationManager.getInstance().getGroup(key);
    }

    private String[] getGroupTranslations(String[] key) {
        return TranslationManager.getInstance().getGroupTranslations(key);
    }
}
