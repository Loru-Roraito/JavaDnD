package com.dnd.ui.panes;

import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class EquipmentPane extends GridPane {
    public EquipmentPane(ViewModel character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");

        ObservableList<String> backgroundEquipments = FXCollections.observableArrayList();
        backgroundEquipments.add(getTranslation("RANDOM"));
        backgroundEquipments.add("50" + getTranslation("GOLD"));
        TooltipComboBox<String> backgroundEquipment = new TooltipComboBox<>(backgroundEquipments, mainTabPane);
        character.getBackground().addListener((_, _, newVal) -> {
            if (newVal.equals(getTranslation("RANDOM")) || newVal.equals(getTranslation("NONE_F"))) {
                if(getChildren().contains(backgroundEquipment)) {
                    getChildren().remove(backgroundEquipment);
                    backgroundEquipments.remove(2);
                }
            } else {
                if (!getChildren().contains(backgroundEquipment)) {
                    add(backgroundEquipment, 0, 0);
                } else {
                    backgroundEquipments.remove(2);
                }
                String text = getTranslation("EQUIPMENT_OF") + getTranslation(newVal);
                backgroundEquipments.add(text);
                backgroundEquipment.setPromptText(text);
            }
        });

        ObservableList<String> classEquipments = FXCollections.observableArrayList();
        classEquipments.add(getTranslation("RANDOM"));
        classEquipments.add("50" + getTranslation("GOLD"));
        TooltipComboBox<String> classEquipment = new TooltipComboBox<>(classEquipments, mainTabPane);
        character.getClasse().addListener((_, _, newVal) -> {
            if (newVal.equals(getTranslation("RANDOM")) || newVal.equals(getTranslation("NONE_F"))) {
                if(getChildren().contains(classEquipment)) {
                    getChildren().remove(classEquipment);
                    classEquipments.remove(2);
                }
            } else {
                if (!getChildren().contains(classEquipment)) {
                    add(classEquipment, 0, 1);
                } else {
                    classEquipments.remove(2);
                }
                String text = getTranslation("EQUIPMENT_OF") + getTranslation(newVal);
                classEquipments.add(text);
                classEquipment.setPromptText(text);
            }
        });
    }

    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }
}