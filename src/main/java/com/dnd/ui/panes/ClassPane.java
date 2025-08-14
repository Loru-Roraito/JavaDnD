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

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.beans.value.ChangeListener;

public class ClassPane extends GridPane {
    private final List<ObservableList<String>> baseValuesList = new ArrayList<>();
    private final Map<TooltipComboBox<String>, ChangeListener<String>> featListeners = new HashMap<>();
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
        TooltipComboBox<String> classComboBox = new TooltipComboBox<>(classes, mainTabPane);
        classComboBox.setPromptText(getTranslation("RANDOM"));
        add(classComboBox, 0, 1);

        // Listen for ComboBox changes (Translated → English)
        classComboBox.valueProperty().bindBidirectional(character.getClasse());

        // Create a label as the title for the second ComboBox
        TooltipLabel subclassLabel = new TooltipLabel(getTranslation("SUBCLASS"), mainTabPane);

        // Create the second ComboBox (subclass selection)
        ObservableList<String> subclasses = FXCollections.observableArrayList();
        TooltipComboBox<String> subclassComboBox = new TooltipComboBox<>(subclasses, mainTabPane);
        subclassComboBox.setPromptText(getTranslation("RANDOM"));
        
        // Listen for ComboBox changes (Translated → English)
        subclassComboBox.valueProperty().bindBidirectional(character.getSubclass());
        subclasses.add(getTranslation("RANDOM"));
        subclassComboBox.setItems(subclasses);

        for (StringProperty prop : character.getAvailableSubclasses()) {
            if (prop != null) {
                prop.addListener((_, oldVal, newVal) -> {
                    if (!oldVal.isEmpty()) {
                        subclasses.remove(subclasses.indexOf(oldVal));
                    }
                    if (!newVal.isEmpty()) {
                        subclasses.add(newVal);
                    }
                });
            }
        }

        TooltipLabel levelLabel = new TooltipLabel(getTranslation("LEVEL"), mainTabPane);
        
        ObservableList<String> levels = FXCollections.observableArrayList();
        
        ComboBox<String> levelComboBox = new ComboBox<>(levels);
        levelComboBox.setPromptText(getTranslation("RANDOM"));

        // Listen for ComboBox changes (Translated → English)
        levelComboBox.valueProperty().bindBidirectional(character.getLevelShown());
        
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
        List<TooltipComboBox<String>> feats = new ArrayList<>(maxFeats);

        Runnable updateFeatsLabel = () -> {
            if (character.getLevel().get() >= 3 || !character.getBackground().get().equals(getTranslation("RANDOM"))) {
                if (!getChildren().contains(featsLabel)) {
                    add(featsLabel, 0, 6);
                }
            } else {
                getChildren().remove(featsLabel);
            }
        };

        // Update the subclasses based on the selected class
        classComboBox.valueProperty().addListener((_, _, newVal) -> {
            // Get the original key for the selected class
            if (newVal != null && !newVal.equals(getTranslation("RANDOM"))) {

                if (newVal.equals(getTranslation("RANDOM")) || newVal.equals(getTranslation("NONE_F"))) {
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
                levels.clear();
            }

            updateSubclasses.run();
        });

        levelComboBox.valueProperty().addListener((_, _, _) -> {
            updateSubclasses.run();
        });

        // Done this way so that the tooltip updates when the origin feat changes (could be done by modifying the TooltipLabel class, but it seemed simpler this way... I'll be doing it in the future, for now this should work)
        List<TooltipLabel> originFeats = new ArrayList<>(1);
        originFeats.add(new TooltipLabel(getTranslation("FEAT"), mainTabPane));
        
        character.getBackground().addListener((_, _, newVal) -> {
            if (!newVal.equals(getTranslation("RANDOM"))) {
                TooltipLabel originFeat = new TooltipLabel(character.getOriginFeat().get(), mainTabPane);

                getChildren().remove(originFeats.get(0));
                originFeats.remove(0);
                add(originFeat, 0, 7);
                originFeats.add(originFeat);
            } else {
                getChildren().remove(originFeats.get(0));
            }
            updateFeatsLabel.run();
        });

        character.getAvailableFeats().addListener((_, oldVal, newVal) -> {
            for (int i = 0; i < maxFeats; i++) {
                if (i < newVal.intValue() && i >= oldVal.intValue()) {
                    add(feats.get(i), 0, 8 + i);
                } else if (i >= newVal.intValue()) {
                    getChildren().remove(feats.get(i));
                }
            }
        });

        Runnable updateFeats = () -> {
            int counter = 0;
            for (TooltipComboBox<String> feat : feats) {
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

                TooltipComboBox<String> comboBox = new TooltipComboBox<>(observableArrayList, mainTabPane);
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
        };

        character.getSelectableFeats().addListener((javafx.collections.ListChangeListener<StringProperty>) _ -> {
            updateFeats.run();
        });

        updateFeats.run();
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
