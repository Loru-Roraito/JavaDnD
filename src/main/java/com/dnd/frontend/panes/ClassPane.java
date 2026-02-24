package com.dnd.frontend.panes;

import java.util.ArrayList;
import java.util.List;

import com.dnd.frontend.ViewModel;
import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.tooltip.TooltipComboBox;
import com.dnd.frontend.tooltip.TooltipLabel;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class ClassPane extends GridPane {
    public ClassPane(ViewModel character, TabPane mainTabPane, int classIndex, List<TooltipComboBox> classComboBoxes) {
        getStyleClass().add("grid-pane");

        // Create a label as the title for the ComboBox
        TooltipLabel classeLabel = new TooltipLabel(getTranslation("CLASS"), mainTabPane);
        add(classeLabel, 0, 0); // Add the label to the GridPane (Column 0, Row 0)
        classeLabel.getStyleClass().add("bold-label"); // Add CSS class

        // Create the first ComboBox (class selection)
        TooltipComboBox classComboBox = new TooltipComboBox(character.getselectableClasses(), mainTabPane);
        classComboBox.setPromptText(getTranslation("RANDOM"));
        add(classComboBox, 0, 1);
        add(classComboBox.getLabel(), 0, 1);
        classComboBox.disableProperty().bind(character.isEditing().not());

        classComboBoxes.add(classComboBox);

        // Listen for ComboBox changes (Translated → English)
        classComboBox.valueProperty().bindBidirectional(character.getClasse(classIndex));

        // Create a label as the title for the second ComboBox
        TooltipLabel subclassLabel = new TooltipLabel(getTranslation("SUBCLASS"), mainTabPane);
        subclassLabel.getStyleClass().add("bold-label"); // Add CSS class

        // Create the second ComboBox (subclass selection)
        ObservableList<String> subclasses = FXCollections.observableArrayList();
        TooltipComboBox subclassComboBox = new TooltipComboBox(subclasses, mainTabPane);
        subclassComboBox.setPromptText(getTranslation("RANDOM"));
        
        // Listen for ComboBox changes (Translated → English)
        subclassComboBox.valueProperty().bindBidirectional(character.getSubclass(classIndex));
        subclasses.add(getTranslation("RANDOM"));
        subclassComboBox.setItems(subclasses);
        subclassComboBox.disableProperty().bind(character.isEditing().not());

        Runnable updateSubclassList = () -> {
            subclasses.setAll(getTranslation("RANDOM"));
            for (StringProperty prop : character.getSelectableSubclasses(classIndex)) {
                if (prop != null && !prop.get().isEmpty()) {
                    subclasses.add(prop.get());
                }
            }
        };

        for (StringProperty prop : character.getSelectableSubclasses(classIndex)) {
            if (prop != null) {
                prop.addListener((_, _, _) -> updateSubclassList.run());
            }
        }
        updateSubclassList.run(); // Run during build

        TooltipLabel levelLabel = new TooltipLabel(getTranslation("LEVEL"), mainTabPane);
        levelLabel.getStyleClass().add("bold-label"); // Add CSS class
        
        ObservableList<String> levels = FXCollections.observableArrayList();
        
        ComboBox<String> levelComboBox = new ComboBox<>(levels);
        levelComboBox.disableProperty().bind(character.isEditing().not());
        
        // Why am I using a runnable instead of a method? I don't remember, but shouldn't change too much
        Runnable updateSubclasses = () -> {
            // Dynamically add or remove the subclass elements
            if (subclasses.isEmpty() || subclasses.size() == 1 && subclasses.get(0).equals(getTranslation("RANDOM"))) {
                getChildren().removeAll(subclassLabel, subclassComboBox, subclassComboBox.getLabel()); // Remove from GridPane
            } else {
                if (!getChildren().contains(subclassLabel)) {
                    add(subclassLabel, 0, 2); // Add back to GridPane
                }
                if (!getChildren().contains(subclassComboBox)) {
                    add(subclassComboBox, 0, 3); // Add back to GridPane
                    add(subclassComboBox.getLabel(), 0, 3); // Add back to GridPane
                }
            }
            
            if (levels.size() <= 1) {
                getChildren().removeAll(levelLabel, levelComboBox); // Remove from GridPane
            } else {
                if (!getChildren().contains(levelLabel)) {
                    add(levelLabel, 0, 4); // Add back to GridPane
                }
                if (!getChildren().contains(levelComboBox)) {
                    add(levelComboBox, 0, 5); // Add back to GridPane
                }
            }
        };

        TooltipLabel featsLabel = new TooltipLabel(getTranslation("FEATS") + ":",getTranslation("FEATS"), mainTabPane);
        featsLabel.getStyleClass().add("bold-label"); // Add CSS class

        // Using a List instead of an array for type security (reminder for me: it means that arrays lose the info about <String>)
        int maxFeats = character.getMaxFeats();
        List<TooltipComboBox> feats = new ArrayList<>(maxFeats);
        List<TooltipComboBox> featOnes = new ArrayList<>(maxFeats);
        List<TooltipComboBox> featTwos = new ArrayList<>(maxFeats);
        boolean[] newFeat = new boolean[maxFeats];
        for (int i = 0; i < maxFeats; i++) {
            newFeat[i] = false;
        }

        Runnable updateFeatsLabel = () -> {
            if (character.getLevel(classIndex).get() >= 4 || (classIndex == 0 && !character.getBackground().get().equals(getTranslation("RANDOM")))) {
                if (!getChildren().contains(featsLabel)) {
                    add(featsLabel, 0, 6);
                }
            } else {
                getChildren().remove(featsLabel);
            }
        };

        character.getLevel(classIndex).addListener(_ -> updateFeatsLabel.run());
        character.getBackground().addListener((_, _, _) -> updateFeatsLabel.run());
        updateFeatsLabel.run();

        Runnable updateLevels = () -> {
            String classValue = character.getClasses()[classIndex].get();

            if (classValue != null && !classValue.equals(getTranslation("RANDOM")) && !classValue.equals(getTranslation("NONE"))) {
                List<String> newLevels = new ArrayList<>();
                
                newLevels.add(getTranslation("RANDOM"));

                int requiredLevels = 0; // minimum levels required by other classes
                for (int i = 0; i < character.getClasses().length; i++) {
                    if (character.getClasses()[i].get().equals(getTranslation("RANDOM")) && i != classIndex) {
                        requiredLevels += 1;
                    }
                }

                for (int i = 1; i <= 20 - character.getTotalLevel().get() + character.getLevel(classIndex).get() - requiredLevels; i++) {
                    newLevels.add(String.valueOf(i));
                }

                levels.setAll(newLevels);
            } else {
                levels.setAll(new ArrayList<>(List.of(getTranslation("RANDOM"))));
            }
                
            updateSubclasses.run();
        };

        character.getClasses()[classIndex].addListener((_, _, _) -> updateLevels.run());
        character.getTotalLevel().addListener(_ -> updateLevels.run());
        character.getLevel(classIndex).addListener(_ -> updateLevels.run());
        updateLevels.run();

        levelComboBox.valueProperty().bindBidirectional(character.getLevelShown(classIndex));

        // Done this way so that the tooltip updates when the origin feat changes (could be done by modifying the TooltipLabel class, but it seemed simpler this way... I'll be doing it in the future, for now this should work)
        if (classIndex == 0) {
            List<TooltipLabel> originFeats = new ArrayList<>(1);
            originFeats.add(new TooltipLabel(getTranslation("FEAT"), mainTabPane));
            
            Runnable updateOriginFeat = () -> {
                String background = character.getBackground().get();
                if (!background.equals(getTranslation("RANDOM"))) {
                    // TODO: spaces
                    TooltipLabel originFeat = new TooltipLabel("   " + character.getOriginFeat().get(), character.getOriginFeat().get(), mainTabPane);
                    getChildren().remove(originFeats.get(0));
                    originFeats.remove(0);
                    add(originFeat, 0, 7);
                    originFeats.add(originFeat);
                } else {
                    getChildren().remove(originFeats.get(0));
                }
                updateFeatsLabel.run();
            };

            character.getBackground().addListener((_, _, _) -> updateOriginFeat.run());
            updateOriginFeat.run(); // Run during build
        }

        for (int i = 0; i < maxFeats; i++) {
            ObservableList<String> observableArrayListOne = FXCollections.observableArrayList();
            ObservableList<String> observableArrayListTwo = FXCollections.observableArrayList();

            TooltipComboBox one = new TooltipComboBox(observableArrayListOne, mainTabPane);
            TooltipComboBox two = new TooltipComboBox(observableArrayListTwo, mainTabPane);
            
            featOnes.add(one);
            featTwos.add(two);

            Runnable disableFeats = () -> {
                if (!character.isLevelingUp().get()) {
                    if (character.isEditing().get()) {
                        one.setDisable(false);
                        two.setDisable(false);
                    } else {
                        one.setDisable(true);
                        two.setDisable(true);
                        for (int j = 0; j < newFeat.length; j++) {
                            newFeat[j] = false;
                        }
                    }
                }
            };
            character.isEditing().addListener(_ -> disableFeats.run());
            character.isLevelingUp().addListener(_ -> disableFeats.run());
            disableFeats.run();

            int index = i;

            Runnable updateFeatOne = () -> {
                String newVal = character.getFeatOne(classIndex, index).get();
                if (newVal != null && !newVal.equals(getTranslation("NONE")) && !getChildren().contains(one)) {
                    add(one, 1, 8 + index);
                    add(one.getLabel(), 1, 8 + index);
                } else if (newVal != null && newVal.equals(getTranslation("NONE")) && getChildren().contains(one)) {
                    getChildren().remove(one);
                    getChildren().remove(one.getLabel());
                }

                observableArrayListOne.clear();
                for (StringProperty featAbility : character.getFeatAbilities(classIndex, index)) {
                    String val = featAbility.get();
                    if (!val.isEmpty()) {
                        observableArrayListOne.add(val);
                    }
                }
                if (observableArrayListOne.size() > 1 && !observableArrayListOne.contains(getTranslation("RANDOM"))) {
                    observableArrayListOne.add(0, getTranslation("RANDOM"));
                } else if (observableArrayListOne.size() <= 1) {
                    observableArrayListOne.remove(getTranslation("RANDOM"));
                }
            };

            character.getFeatOne(classIndex, index).addListener((_, _, _) -> updateFeatOne.run());
            updateFeatOne.run(); // Run during build

            Runnable updateFeatTwo = () -> {
                String newVal = character.getFeatTwo(classIndex, index).get();
                if (newVal != null && !newVal.equals(getTranslation("NONE")) && !getChildren().contains(two)) {
                    add(two, 2, 8 + index);
                    add(two.getLabel(), 2, 8 + index);
                } else if (newVal != null && newVal.equals(getTranslation("NONE")) && getChildren().contains(two)) {
                    getChildren().remove(two);
                    getChildren().remove(two.getLabel());
                }

                observableArrayListTwo.clear();
                for (StringProperty featAbility : character.getFeatAbilities(classIndex, index)) {
                    String val = featAbility.get();
                    if (!val.isEmpty()) {
                        observableArrayListTwo.add(val);
                    }
                }
                if (observableArrayListTwo.size() != 1 && !observableArrayListTwo.contains(getTranslation("RANDOM"))) {
                    observableArrayListTwo.add(0, getTranslation("RANDOM"));
                } else if (observableArrayListTwo.size() <= 1) {
                    observableArrayListTwo.remove(getTranslation("RANDOM"));
                }
            };

            character.getFeatTwo(classIndex, index).addListener((_, _, _) -> updateFeatTwo.run());
            updateFeatTwo.run(); // Run during build

            one.valueProperty().bindBidirectional(character.getFeatOne(classIndex, index));
            two.valueProperty().bindBidirectional(character.getFeatTwo(classIndex, index));
        }

        Runnable updateAvailableFeats = () -> {
            int newVal = character.getAvailableFeats(classIndex).get();
            for (int i = 0; i < maxFeats; i++) {
                if (feats.get(i).getValue().equals(getTranslation("RANDOM"))) {
                    newFeat[i] = true;
                    feats.get(i).setDisable(false);
                    featOnes.get(i).setDisable(false);
                    featTwos.get(i).setDisable(false);
                }
                if (i < newVal && !getChildren().contains(feats.get(i))) {
                    add(feats.get(i), 0, 8 + i);
                    add(feats.get(i).getLabel(), 0, 8 + i);
                } else if (i >= newVal) {
                    getChildren().remove(feats.get(i));
                    getChildren().remove(feats.get(i).getLabel());
                }
            }
        };

        character.getAvailableFeats(classIndex).addListener(_ -> updateAvailableFeats.run());

        int counter = 0;
        for (TooltipComboBox feat : feats) {
            if (getChildren().contains(feat)) {
                counter ++;
                getChildren().remove(feat);
                getChildren().remove(feat.getLabel());
            }
        }
        feats.clear();

        for (int i = 0; i < maxFeats; i++) {
            TooltipComboBox comboBox = new TooltipComboBox(character.getSelectableFeats(classIndex), mainTabPane);
            feats.add(comboBox);
            if(character.isLevelingUp().get()) {
                comboBox.setDisable(true);
            }

            Runnable disableFeats = () -> {
                if (!character.isLevelingUp().get()) {
                    if (character.isEditing().get()) {
                        comboBox.setDisable(false);
                    } else {
                        comboBox.setDisable(true);
                    }
                }
            };
            character.isEditing().addListener(_ -> disableFeats.run());
            character.isLevelingUp().addListener(_ -> disableFeats.run());
            disableFeats.run();

            comboBox.setPromptText(getTranslation("RANDOM"));
            comboBox.valueProperty().bindBidirectional(character.getFeat(classIndex, i));
        }

        for (int i = 0; i < counter; i++) {
            add(feats.get(i), 0, 8 + i);
            add(feats.get(i).getLabel(), 0, 8 + i);
        }

        updateAvailableFeats.run();
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }
}
