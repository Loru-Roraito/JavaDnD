package com.dnd.ui.panes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.utils.ComboBoxUtils;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.collections.ListChangeListener;

public class ClassPane extends GridPane {
    private final List<ObservableList<String>> baseValuesList = new ArrayList<>();
    private final Map<ComboBox<String>, ChangeListener<String>> featListeners = new HashMap<>();
    public ClassPane(ViewModel character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");

        // Create a label as the title for the ComboBox
        TooltipLabel classeLabel = new TooltipLabel(getTranslation("CLASS"), mainTabPane);
        add(classeLabel, 0, 0); // Add the label to the GridPane (Column 0, Row 0)
        
        // Populate the class list and translation map
        ObservableList<String> classes = FXCollections.observableArrayList();
        for (String classKey : getGroup(new String[] {"classes"})) {
            classes.add(getTranslation(classKey));
        }
        classes.add(0, getTranslation("RANDOM"));

        // Create the first ComboBox (class selection)
        TooltipComboBox classComboBox = new TooltipComboBox(classes, mainTabPane);
        classComboBox.setPromptText(getTranslation("RANDOM"));
        add(classComboBox, 0, 1);

        // Listen for ComboBox changes (Translated → English)
        classComboBox.valueProperty().bindBidirectional(character.getClasse());

        // Create a label as the title for the second ComboBox
        TooltipLabel subclassLabel = new TooltipLabel(getTranslation("SUBCLASS"), mainTabPane);

        // Create the second ComboBox (subclass selection)
        ObservableList<String> subclasses = FXCollections.observableArrayList();
        TooltipComboBox subclassComboBox = new TooltipComboBox(subclasses, mainTabPane);
        subclassComboBox.setPromptText(getTranslation("RANDOM"));
        
        // Listen for ComboBox changes (Translated → English)
        subclassComboBox.valueProperty().bindBidirectional(character.getSubclass());
        subclasses.add(getTranslation("RANDOM"));
        subclassComboBox.setItems(subclasses);

        Runnable updateSubclassList = () -> {
            subclasses.clear();
            subclasses.add(getTranslation("RANDOM"));
            for (StringProperty prop : character.getAvailableSubclasses()) {
                if (prop != null && !prop.get().isEmpty()) {
                    subclasses.add(prop.get());
                }
            }
        };

        for (StringProperty prop : character.getAvailableSubclasses()) {
            if (prop != null) {
                prop.addListener((_, _, _) -> updateSubclassList.run());
            }
        }
        updateSubclassList.run(); // Run during build

        TooltipLabel levelLabel = new TooltipLabel(getTranslation("LEVEL"), mainTabPane);
        
        ObservableList<String> levels = FXCollections.observableArrayList();
        
        ComboBox<String> levelComboBox = new ComboBox<>(levels);
        
        // Why am I using a runnable instead of a method? I don't remember, but shouldn't change too much
        Runnable updateSubclasses = () -> {
            // Dynamically add or remove the subclass elements
            if (subclasses.isEmpty() || subclasses.size() == 1 && subclasses.get(0).equals(getTranslation("RANDOM"))) {
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
        };

        TooltipLabel featsLabel = new TooltipLabel(getTranslation("FEATS"), mainTabPane);

        // Using a List instead of an array for type security (reminder for me: it means that arrays lose the info about <String>)
        int maxFeats = character.getMaxFeats();
        List<ComboBox<String>> feats = new ArrayList<>(maxFeats);

        Runnable updateFeatsLabel = () -> {
            if (character.getLevel().get() >= 4 || !character.getBackground().get().equals(getTranslation("RANDOM"))) {
                if (!getChildren().contains(featsLabel)) {
                    add(featsLabel, 0, 6);
                }
            } else {
                getChildren().remove(featsLabel);
            }
        };

        character.getLevel().addListener((_, _, _) -> updateFeatsLabel.run());
        updateFeatsLabel.run();

        Runnable updateLevels = () -> {
            String classValue = classComboBox.getValue();
            if (classValue != null && !classValue.equals(getTranslation("RANDOM")) 
                && !classValue.equals(getTranslation("NONE_F"))) {
                if (levels.isEmpty()) {
                    levels.add(getTranslation("RANDOM"));
                    levelComboBox.valueProperty().set(getTranslation("RANDOM"));
                    for (int i = 1; i <= 20; i++) {
                        levels.add(String.valueOf(i));
                    }
                }
            } else {
                levels.clear();
            }
            updateSubclasses.run();
        };

        classComboBox.valueProperty().addListener((_, _, _) -> updateLevels.run());
        levelComboBox.valueProperty().addListener((_, _, _) -> updateSubclasses.run());
        updateLevels.run();

        levelComboBox.valueProperty().bindBidirectional(character.getLevelShown());

        // Done this way so that the tooltip updates when the origin feat changes (could be done by modifying the TooltipLabel class, but it seemed simpler this way... I'll be doing it in the future, for now this should work)
        List<TooltipLabel> originFeats = new ArrayList<>(1);
        originFeats.add(new TooltipLabel(getTranslation("FEAT"), mainTabPane));
        
        Runnable updateOriginFeat = () -> {
            String background = character.getBackground().get();
            if (!background.equals(getTranslation("RANDOM"))) {
                TooltipLabel originFeat = new TooltipLabel(character.getOriginFeat().get(), mainTabPane);
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

        Runnable updateAvailableFeats = () -> {
            int newVal = character.getAvailableFeats().get();
            for (int i = 0; i < maxFeats; i++) {
                if (i < newVal && !getChildren().contains(feats.get(i))) {
                    add(feats.get(i), 0, 8 + i);
                } else if (i >= newVal) {
                    getChildren().remove(feats.get(i));
                }
            }
        };

        character.getAvailableFeats().addListener((_, _, _) -> updateAvailableFeats.run());

        Runnable updateFeats = () -> {
            int counter = 0;
            for (ComboBox<String> feat : feats) {
                ChangeListener<String> listener = featListeners.remove(feat);
                if (listener != null) {
                    feat.valueProperty().removeListener(listener);
                }
                if (getChildren().contains(feat)) {
                    counter ++;
                    getChildren().remove(feat);
                }
            }
            featListeners.clear();
            baseValuesList.clear();
            feats.clear();

            String[] repeatableFeats = getRepeatableFeatsTranslated();
            String[] repeatableFeatsWithRandom = new String[repeatableFeats.length + 1];
            repeatableFeatsWithRandom[0] = getTranslation("RANDOM");
            System.arraycopy(repeatableFeats, 0, repeatableFeatsWithRandom, 1, repeatableFeats.length);

            ObservableList<StringProperty> baseValues = character.getSelectableFeats();
            String[] baseArray = new String[baseValues.size()];
            for (int i = 0; i < baseValues.size(); i++) {
                baseArray[i] = baseValues.get(i).get();
            }

            for (int i = 0; i < maxFeats; i++) {
                ObservableList<String> observableArrayList = FXCollections.observableArrayList(baseArray);

                TooltipComboBox comboBox = new TooltipComboBox(observableArrayList, mainTabPane);
                baseValuesList.add(observableArrayList);
                feats.add(comboBox);

                comboBox.setPromptText(getTranslation("RANDOM"));
                comboBox.valueProperty().bindBidirectional(character.getFeat(i));

                ChangeListener<String> featListener = (_, _, _) -> {
                    for (int j = 0; j < feats.size(); j++) {
                        ComboBoxUtils.updateItems(feats.get(j), feats, baseValuesList.get(j), baseArray, repeatableFeatsWithRandom);
                    }
                };
                comboBox.valueProperty().addListener(featListener);
                
                featListeners.put(comboBox, featListener);
            }
            
            for (int j = 0; j < feats.size(); j++) {
                ComboBoxUtils.updateItems(feats.get(j), feats, baseValuesList.get(j), baseArray, repeatableFeatsWithRandom);
            }

            for (int i = 0; i < counter; i++) {
                add(feats.get(i), 0, 8 + i);
            }

            updateAvailableFeats.run();
        };

        character.getSelectableFeats().addListener((ListChangeListener<StringProperty>) _ -> {
            Platform.runLater(updateFeats);
        });
        updateFeats.run();

        for (int i = 0; i < maxFeats; i++) {
            ObservableList<String> observableArrayListOne = FXCollections.observableArrayList();
            ObservableList<String> observableArrayListTwo = FXCollections.observableArrayList();

            TooltipComboBox one = new TooltipComboBox(observableArrayListOne, mainTabPane);
            TooltipComboBox two = new TooltipComboBox(observableArrayListTwo, mainTabPane);
            int index = i;

            Runnable updateFeatOne = () -> {
                String newVal = character.getFeatOne(index).get();
                if (newVal != null && !newVal.equals(getTranslation("NONE_M")) && !getChildren().contains(one)) {
                    add(one, 1, 8 + index);
                } else if (newVal != null && newVal.equals(getTranslation("NONE_M")) && getChildren().contains(one)) {
                    getChildren().remove(one);
                }
            };

            character.getFeatOne(i).addListener((_, _, _) -> updateFeatOne.run());
            updateFeatOne.run(); // Run during build

            Runnable updateFeatAbilitiesOne = () -> {
                observableArrayListOne.clear();
                for (StringProperty featAbility : character.getFeatAbilities(index)) {
                    String val = featAbility.get();
                    if (!val.isEmpty()) {
                        observableArrayListOne.add(val);
                    }
                }
                if (observableArrayListOne.size() > 1 && !observableArrayListOne.contains(getTranslation("RANDOM"))) {
                    observableArrayListOne.add(0, getTranslation("RANDOM"));
                    one.setDisable(false);
                } else if (observableArrayListOne.size() <= 1) {
                    observableArrayListOne.remove(getTranslation("RANDOM"));
                    one.setDisable(true);
                }
            };

            for (StringProperty featAbility : character.getFeatAbilities(index)) {
                featAbility.addListener((_, _, _) -> updateFeatAbilitiesOne.run());
            }
            updateFeatAbilitiesOne.run(); // Run during build

            Runnable updateFeatTwo = () -> {
                String newVal = character.getFeatTwo(index).get();
                if (newVal != null && !newVal.equals(getTranslation("NONE_M")) && !getChildren().contains(two)) {
                    add(two, 2, 8 + index);
                } else if (newVal != null && newVal.equals(getTranslation("NONE_M")) && getChildren().contains(two)) {
                    getChildren().remove(two);
                }
            };

            character.getFeatTwo(i).addListener((_, _, _) -> updateFeatTwo.run());
            updateFeatTwo.run(); // Run during build

            Runnable updateFeatAbilitiesTwo = () -> {
                observableArrayListTwo.clear();
                for (StringProperty featAbility : character.getFeatAbilities(index)) {
                    String val = featAbility.get();
                    if (!val.isEmpty()) {
                        observableArrayListTwo.add(val);
                    }
                }
                if (observableArrayListTwo.size() != 1 && !observableArrayListTwo.contains(getTranslation("RANDOM"))) {
                    observableArrayListTwo.add(0, getTranslation("RANDOM"));
                    two.setDisable(false);
                } else if (observableArrayListTwo.size() <= 1) {
                    observableArrayListTwo.remove(getTranslation("RANDOM"));
                    two.setDisable(true);
                }
            };

            for (StringProperty featAbility : character.getFeatAbilities(index)) {
                featAbility.addListener((_, _, _) -> updateFeatAbilitiesTwo.run());
            }
            updateFeatAbilitiesTwo.run(); // Run during build

            one.valueProperty().bindBidirectional(character.getFeatOne(i));
            two.valueProperty().bindBidirectional(character.getFeatTwo(i));
        }
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String[] getGroup(String[] key) {
        return TranslationManager.getInstance().getGroup(key);
    }

    private String[] getRepeatableFeatsTranslated() {
        return TranslationManager.getInstance().getRepeatableFeatsTranslated();
    }
}
