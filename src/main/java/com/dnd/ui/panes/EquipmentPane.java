package com.dnd.ui.panes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class EquipmentPane extends GridPane {
    private final String[] sets;
    private final List<TooltipComboBox> backgroundComboBoxes = new ArrayList<>();
    private final List<TooltipComboBox> classComboBoxes = new ArrayList<>();
    public EquipmentPane(ViewModel character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");
        sets = getGroup(new String[] {"sets"});

        ObservableList<String> backgroundEquipments = FXCollections.observableArrayList();
        backgroundEquipments.add(getTranslation("RANDOM"));
        TooltipComboBox backgroundEquipment = new TooltipComboBox(backgroundEquipments, mainTabPane);
        character.getBackground().addListener((_, _, newVal) -> {
            if (newVal.equals(getTranslation("RANDOM"))) {
                if(getChildren().contains(backgroundEquipment)) {
                    getChildren().remove(backgroundEquipment);
                    backgroundEquipments.remove(2);
                    backgroundEquipments.remove(1);
                }
            } else {
                if (!getChildren().contains(backgroundEquipment)) {
                    add(backgroundEquipment, 0, 0);
                } else {
                    backgroundEquipments.remove(2);
                    backgroundEquipments.remove(1);
                }
                String text = getTranslation("EQUIPMENT_OF") + newVal;
                String gold = String.valueOf(getInt(new String[] {"backgrounds", getOriginal(newVal), "gold"})) + getTranslation("GOLD");
                backgroundEquipments.add(text);
                backgroundEquipments.add(gold);
                backgroundEquipment.setValue(text);
            }
        });

        character.getBackground().addListener((_, _, _) -> {
            String newVal = character.getBackgroundEquipment().get(0).get();
            if(newVal.equals(getTranslation("RANDOM")) || newVal.equals("50" + getTranslation("GOLD"))) {
                backgroundEquipment.setValue(newVal);                
            } else {
                backgroundEquipment.setValue(getTranslation("EQUIPMENT_OF") + newVal);
            }
        });
        
        backgroundEquipment.valueProperty().addListener((_, _, newVal) -> {
            for (int index = 0; index < backgroundComboBoxes.size(); index++) {
                TooltipComboBox comboBox = backgroundComboBoxes.get(index);
                comboBox.setValue(getTranslation("RANDOM"));
                comboBox.valueProperty().unbindBidirectional(character.getBackgroundEquipment().get(index));
                getChildren().remove(comboBox);
            }
            backgroundComboBoxes.clear();
            if (newVal.equals(getTranslation("RANDOM")) || newVal.equals("50" + getTranslation("GOLD"))) {
                character.getBackgroundEquipment().get(0).set(newVal);
            }
            else {
                String background = character.getBackground().get();
                if (!character.getBackgroundEquipment().isEmpty()) {
                    character.getBackgroundEquipment().get(0).set(background);
                }
                
                String[] equips = getGroup(new String[] {"backgrounds", getOriginal(background), "equipment"});
                for (String equip : equips) {
                    if (Arrays.asList(sets).contains(equip)) {
                        String[] set = getGroupTranslations(new String[] {"sets", equip});
                        ObservableList<String> items = FXCollections.observableArrayList(set);
                        items.add(0, getTranslation("RANDOM"));
                        TooltipComboBox comboBox = new TooltipComboBox(items, mainTabPane);
                        comboBox.setPromptText(getTranslation("RANDOM"));
                        backgroundComboBoxes.add(comboBox);
                        int index = backgroundComboBoxes.size();
                        add(comboBox, index, 0);

                        comboBox.valueProperty().bindBidirectional(character.getBackgroundEquipment().get(index));
                    }
                }
            }
        });

        ObservableList<String> classEquipments = FXCollections.observableArrayList();
        classEquipments.add(getTranslation("RANDOM"));
        TooltipComboBox classEquipment = new TooltipComboBox(classEquipments, mainTabPane);
        character.getClasse().addListener((_, _, newVal) -> {
            if (newVal.equals(getTranslation("RANDOM")) || newVal.equals(getTranslation("NONE_F"))) {
                if(getChildren().contains(classEquipment)) {
                    getChildren().remove(classEquipment);
                    classEquipments.remove(2);
                    classEquipments.remove(1);
                }
            } else {
                if (!getChildren().contains(classEquipment)) {
                    add(classEquipment, 0, 1);
                } else {
                    classEquipments.remove(2);
                    classEquipments.remove(1);
                }
                String text = getTranslation("EQUIPMENT_OF") + getTranslation(newVal);
                String gold = String.valueOf(getInt(new String[] {"classes", getOriginal(newVal), "gold"})) + getTranslation("GOLD");
                classEquipments.add(text);
                classEquipments.add(gold);
                classEquipment.setValue(text);
            }
        });

        character.getClasse().addListener((_, _, _) -> {
            String newVal = character.getClassEquipment().get(0).get();
            if(newVal.equals(getTranslation("RANDOM")) || newVal.equals("50" + getTranslation("GOLD"))) {
                classEquipment.setValue(newVal);                
            } else {
                classEquipment.setValue(getTranslation("EQUIPMENT_OF") + newVal);
            }
        });

        classEquipment.valueProperty().addListener((_, _, newVal) -> {
            for (int index = 0; index < classComboBoxes.size(); index++) {
                TooltipComboBox comboBox = classComboBoxes.get(index);
                comboBox.setValue(getTranslation("RANDOM"));
                comboBox.valueProperty().unbindBidirectional(character.getClassEquipment().get(index));
                getChildren().remove(comboBox);
            }
            classComboBoxes.clear();
            if (newVal.equals(getTranslation("RANDOM")) || newVal.equals("50" + getTranslation("GOLD"))) {
                character.getClassEquipment().get(0).set(newVal);
            } else {
                if (!character.getClassEquipment().isEmpty()) {
                    character.getClassEquipment().get(0).set(character.getClasse().get());
                }
            }
            String classe = character.getClasse().get();
            String[] equips = getGroup(new String[] {"classes", getOriginal(classe), "equipment"});
            for (String equip : equips) {
                if (Arrays.asList(sets).contains(equip)) {
                    String[] set = getGroupTranslations(new String[] {"sets", equip});
                    ObservableList<String> items = FXCollections.observableArrayList(set);
                    items.add(0, getTranslation("RANDOM"));
                    TooltipComboBox comboBox = new TooltipComboBox(items, mainTabPane);
                    comboBox.setPromptText(getTranslation("RANDOM"));
                    classComboBoxes.add(comboBox);
                    int index = classComboBoxes.size();
                    add(comboBox, index, 1);

                    comboBox.valueProperty().bindBidirectional(character.getClassEquipment().get(index));
                }
            }
        });
    }

    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String[] getGroup(String[] key) {
        return TranslationManager.getInstance().getGroup(key);
    } 

    private String[] getGroupTranslations(String[] key) {
        return TranslationManager.getInstance().getGroupTranslations(key);
    }

    private String getOriginal(String key) {
        return TranslationManager.getInstance().getOriginal(key);
    }

    private int getInt(String[] key) {
        return TranslationManager.getInstance().getInt(key);
    }
}