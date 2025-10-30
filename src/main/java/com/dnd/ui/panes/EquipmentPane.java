package com.dnd.ui.panes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class EquipmentPane extends GridPane {
    private final String[] sets;
    private final List<TooltipComboBox> backgroundComboBoxes = new ArrayList<>();
    private final List<TooltipComboBox> classComboBoxes = new ArrayList<>();
    private String textClass = "";
    private String goldClass = "";
    private String textBackground = "";
    private String goldBackground = "";
    public EquipmentPane(ViewModel character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");

        GridPane moneyPane = new GridPane();
        moneyPane.getStyleClass().add("grid-pane");

        int width = 50;

        TooltipLabel copperLabel = new TooltipLabel(getTranslation("COPPER") + ":", getTranslation("COPPER"), mainTabPane);
        TextField copperField = new TextField();
        copperField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allow only digits
                copperField.setText(oldValue); // Revert to the old value if invalid input is detected
            }
        });
        copperField.textProperty().bindBidirectional(character.getMoneyShown(0));
        copperField.setPrefWidth(width);
        moneyPane.add(copperLabel, 0, 0);
        moneyPane.add(copperField, 1, 0);

        TooltipLabel silverLabel = new TooltipLabel(getTranslation("SILVER") + ":", getTranslation("SILVER"), mainTabPane);
        TextField silverField = new TextField();
        silverField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allow only digits
                silverField.setText(oldValue); // Revert to the old value if invalid input is detected
            }
        });
        silverField.textProperty().bindBidirectional(character.getMoneyShown(1));
        silverField.setPrefWidth(width);
        moneyPane.add(silverLabel, 2, 0);
        moneyPane.add(silverField, 3, 0);

        TooltipLabel electrumLabel = new TooltipLabel(getTranslation("ELECTRUM") + ":", getTranslation("ELECTRUM"), mainTabPane);
        TextField electrumField = new TextField();
        electrumField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allow only digits
                electrumField.setText(oldValue); // Revert to the old value if invalid input is detected
            }
        });
        electrumField.textProperty().bindBidirectional(character.getMoneyShown(2));
        electrumField.setPrefWidth(width);
        moneyPane.add(electrumLabel, 4, 0);
        moneyPane.add(electrumField, 5, 0);

        TooltipLabel goldLabel = new TooltipLabel(getTranslation("GOLD") + ":", getTranslation("GOLD"), mainTabPane);
        TextField goldField = new TextField();
        goldField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allow only digits
                goldField.setText(oldValue); // Revert to the old value if invalid input is detected
            }
        });
        goldField.textProperty().bindBidirectional(character.getMoneyShown(3));
        goldField.setPrefWidth(width);
        moneyPane.add(goldLabel, 6, 0);
        moneyPane.add(goldField, 7, 0);

        TooltipLabel platinumLabel = new TooltipLabel(getTranslation("PLATINUM") + ":", getTranslation("PLATINUM"), mainTabPane);
        TextField platinumField = new TextField();
        platinumField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allow only digits
                platinumField.setText(oldValue); // Revert to the old value if invalid input is detected
            }
        });
        platinumField.textProperty().bindBidirectional(character.getMoneyShown(4));
        platinumField.setPrefWidth(width);
        moneyPane.add(platinumLabel, 8, 0);
        moneyPane.add(platinumField, 9, 0);

        add(moneyPane, 0, 0, character.getClassEquipments().length, 1);

        sets = getGroup(new String[] {"sets"});

        ObservableList<String> backgroundEquipments = FXCollections.observableArrayList();
        backgroundEquipments.add(getTranslation("RANDOM"));
        TooltipComboBox backgroundEquipment = new TooltipComboBox(backgroundEquipments, mainTabPane);

        Runnable selectBackgroundEquipment = () -> {            
            String newVal = character.getBackgroundEquipment(0).get();
            if(newVal.equals(getTranslation("RANDOM"))) {
                backgroundEquipment.setValue(getTranslation("RANDOM"));
            } else if (newVal.equals(character.getBackground().get())) {
                backgroundEquipment.setValue(textBackground);
            } else if (newVal.equals(getTranslation("GOLD"))) {
                backgroundEquipment.setValue(goldBackground);
            }
        };
        
        Runnable updateBackgroundEquipment = () -> {
            String currentBackground = character.getBackground().get();
            if (currentBackground.equals(getTranslation("RANDOM")) || currentBackground.equals(getTranslation("NONE_F"))) {
                if(getChildren().contains(backgroundEquipment)) {
                    getChildren().remove(backgroundEquipment);
                    backgroundEquipments.clear();
                    backgroundEquipments.add(getTranslation("RANDOM"));
                }
            } else {
                if (!getChildren().contains(backgroundEquipment)) {
                    add(backgroundEquipment, 0, 1);
                } else {
                    backgroundEquipments.clear();
                    backgroundEquipments.add(getTranslation("RANDOM"));
                }
                textBackground = getTranslation("EQUIPMENT_OF") + currentBackground;
                goldBackground = String.valueOf(getInt(new String[] {"backgrounds", getOriginal(currentBackground), "gold"})) + " " + getTranslation("GOLD");
                backgroundEquipments.add(textBackground);
                backgroundEquipments.add(goldBackground);
            }
            selectBackgroundEquipment.run();
        };

        character.getBackground().addListener((_, _, _) -> {
            updateBackgroundEquipment.run();
        });

        character.getBackgroundEquipment(0).addListener((_, _, _) -> {
            selectBackgroundEquipment.run();
        });
        
        updateBackgroundEquipment.run();
        selectBackgroundEquipment.run();

        Runnable updateBackgrounds = () -> {
            String newVal = backgroundEquipment.getValue();
            for (int index = 0; index < backgroundComboBoxes.size(); index++) {
                TooltipComboBox comboBox = backgroundComboBoxes.get(index);
                comboBox.setValue(getTranslation("RANDOM"));
                comboBox.valueProperty().unbindBidirectional(character.getBackgroundEquipment(index + 1));
                getChildren().remove(comboBox);
            }
            backgroundComboBoxes.clear();
            if (newVal != null) {
                if (newVal.equals(getTranslation("RANDOM"))) {
                    character.getBackgroundEquipment(0).set(newVal);
                } else if (newVal.equals(getTranslation("EQUIPMENT_OF") + character.getBackground().get())) {
                    String background = character.getBackground().get();
                    character.getBackgroundEquipment(0).set(background);
                    
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
                            add(comboBox, index, 1);

                            comboBox.valueProperty().bindBidirectional(character.getBackgroundEquipment(index));
                        }
                    }
                } else if (!newVal.equals("")) {
                    character.getBackgroundEquipment(0).set(getTranslation("GOLD"));
                }
            }
        };
        
        backgroundEquipment.valueProperty().addListener((_, _, _) -> {
            updateBackgrounds.run();
        });

        updateBackgrounds.run();

        ObservableList<String> classEquipments = FXCollections.observableArrayList();
        classEquipments.add(getTranslation("RANDOM"));
        TooltipComboBox classEquipment = new TooltipComboBox(classEquipments, mainTabPane);

        Runnable selectClassEquipment = () -> {
            String newVal = character.getClassEquipment(0).get();
            if(newVal.equals(getTranslation("RANDOM"))) {
                classEquipment.setValue(getTranslation("RANDOM"));
            } else if (newVal.equals(character.getClasse().get())) {
                classEquipment.setValue(textClass);
            } else if (newVal.equals(getTranslation("GOLD"))) {
                classEquipment.setValue(goldClass);
            }
        };

        Runnable updateClassEquipment = () -> {
            String currentClass = character.getClasse().get();
            if (currentClass.equals(getTranslation("RANDOM")) || currentClass.equals(getTranslation("NONE_F"))) {
                if(getChildren().contains(classEquipment)) {
                    getChildren().remove(classEquipment);
                    classEquipments.clear();
                    classEquipments.add(getTranslation("RANDOM"));
                }
            } else {
                if (!getChildren().contains(classEquipment)) {
                    add(classEquipment, 0, 2);
                } else {
                    classEquipments.clear();
                    classEquipments.add(getTranslation("RANDOM"));
                }
                textClass = getTranslation("EQUIPMENT_OF") + getTranslation(currentClass);
                goldClass = String.valueOf(getInt(new String[] {"classes", getOriginal(currentClass), "gold"})) + " " + getTranslation("GOLD");
                classEquipments.add(textClass);
                classEquipments.add(goldClass);
            }
            selectClassEquipment.run();
        };

        character.getClasse().addListener((_, _, _) -> {
            updateClassEquipment.run();
        });
        
        character.getClassEquipment(0).addListener((_, _, _) -> {
            selectClassEquipment.run();
        });

        updateClassEquipment.run();
        selectClassEquipment.run();

        Runnable updateClasss = () -> {
            String newVal = classEquipment.getValue();
            for (int index = 0; index < classComboBoxes.size(); index++) {
                TooltipComboBox comboBox = classComboBoxes.get(index);
                comboBox.setValue(getTranslation("RANDOM"));
                comboBox.valueProperty().unbindBidirectional(character.getClassEquipment(index + 1));
                getChildren().remove(comboBox);
            }
            classComboBoxes.clear();
            if (newVal != null) {
                if (newVal.equals(getTranslation("RANDOM"))) {
                    character.getClassEquipment(0).set(newVal);
                } else if (newVal.equals(getTranslation("EQUIPMENT_OF") + character.getClasse().get())) {
                    String classe = character.getClasse().get();
                    character.getClassEquipment(0).set(character.getClasse().get());

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
                            add(comboBox, index, 2);

                            comboBox.valueProperty().bindBidirectional(character.getClassEquipment(index));
                        }
                    }
                } else if (!newVal.equals("")) {
                    character.getClassEquipment(0).set(getTranslation("GOLD"));
                }
            }
        };

        classEquipment.valueProperty().addListener((_, _, _) -> {
            updateClasss.run();
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