package com.dnd.frontend.panes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dnd.backend.GroupManager;
import com.dnd.backend.ItemManager;
import com.dnd.frontend.ViewModel;
import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.tooltip.TooltipComboBox;
import com.dnd.frontend.tooltip.TooltipLabel;
import com.dnd.utils.items.Item;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;


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

        // TODO: dynamic
        int width = 40;

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

        sets = getStrings(new String[] {"sets"});

        ObservableList<String> backgroundEquipments = FXCollections.observableArrayList();
        backgroundEquipments.add(getTranslation("RANDOM"));
        TooltipComboBox backgroundEquipment = new TooltipComboBox(backgroundEquipments, mainTabPane);
        backgroundEquipment.managedProperty().bind(character.isGenerator());
        backgroundEquipment.visibleProperty().bind(character.isGenerator());

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
                textBackground = getTranslation("EQUIPMENT_OF") + " " + currentBackground;
                goldBackground = String.valueOf(getInt(new String[] {"backgrounds", getOriginal(currentBackground), "gold"})) + " " + getTranslation("GOLD");
                backgroundEquipments.add(textBackground);
                backgroundEquipments.add(goldBackground);
            }
            selectBackgroundEquipment.run();
        };

        character.getBackgroundEquipment(0).addListener((_, _, _) -> {
            updateBackgroundEquipment.run();
        });
        
        updateBackgroundEquipment.run();

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
                } else if (newVal.equals(getTranslation("EQUIPMENT_OF") + " " + character.getBackground().get())) {
                    String background = character.getBackground().get();
                    character.getBackgroundEquipment(0).set(background);
                    
                    String[] equips = getStrings(new String[] {"backgrounds", getOriginal(background), "equipment"});
                    for (String equip : equips) {
                        if (Arrays.asList(sets).contains(equip)) {
                            String[] set = getTranslations(getStrings(new String[] {"sets", equip}));
                            ObservableList<String> items = FXCollections.observableArrayList(set);
                            items.add(0, getTranslation("RANDOM"));
                            TooltipComboBox comboBox = new TooltipComboBox(items, mainTabPane);
                            comboBox.managedProperty().bind(character.isGenerator());
                            comboBox.visibleProperty().bind(character.isGenerator());
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
        classEquipment.managedProperty().bind(character.isGenerator());
        classEquipment.visibleProperty().bind(character.isGenerator());

        Runnable selectClassEquipment = () -> {
            String newVal = character.getClassEquipment(0).get();
            if(newVal.equals(getTranslation("RANDOM"))) {
                classEquipment.setValue(getTranslation("RANDOM"));
            } else if (newVal.equals(character.getClasse(0).get())) {
                classEquipment.setValue(textClass);
            } else if (newVal.equals(getTranslation("GOLD"))) {
                classEquipment.setValue(goldClass);
            }
        };

        Runnable updateClassEquipment = () -> {
            String currentClass = character.getClasse(0).get();
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
                textClass = getTranslation("EQUIPMENT_OF") + " " + getTranslation(currentClass);
                goldClass = String.valueOf(getInt(new String[] {"classes", getOriginal(currentClass), "gold"})) + " " + getTranslation("GOLD");
                classEquipments.add(textClass);
                classEquipments.add(goldClass);
            }
            selectClassEquipment.run();
        };
        
        character.getClassEquipment(0).addListener((_, _, _) -> {
            updateClassEquipment.run();
        });

        updateClassEquipment.run();

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
                } else if (newVal.equals(getTranslation("EQUIPMENT_OF") + " " + character.getClasse(0).get())) {
                    String classe = character.getClasse(0).get();
                    character.getClassEquipment(0).set(character.getClasse(0).get());

                    String[] equips = getStrings(new String[] {"classes", getOriginal(classe), "equipment"});
                    for (String equip : equips) {
                        if (Arrays.asList(sets).contains(equip)) {
                            String[] set = getTranslations(getStrings(new String[] {"sets", equip}));
                            ObservableList<String> items = FXCollections.observableArrayList(set);
                            items.add(0, getTranslation("RANDOM"));
                            TooltipComboBox comboBox = new TooltipComboBox(items, mainTabPane);
                            comboBox.managedProperty().bind(character.isGenerator());
                            comboBox.visibleProperty().bind(character.isGenerator());
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

        VBox itemsBox = new VBox();
        Runnable updateItems = () -> {
            Map<Item, IntegerProperty> itemQuantities = new HashMap<>();
            itemsBox.getChildren().clear();
            for (Item item : character.getItems()) {
                boolean itemPresent = false;
                for (Item myItem : itemQuantities.keySet()) {
                    if (myItem.equals(item)) {
                        itemQuantities.get(myItem).set(itemQuantities.get(myItem).get() + 1);
                        itemPresent = true;
                        break;
                    }
                }
                if (!itemPresent) {
                    HBox itemBox = new HBox();
                    IntegerProperty quantityProperty = new SimpleIntegerProperty(1);
                    itemQuantities.put(item, quantityProperty);
                    TooltipLabel itemLabel = new TooltipLabel(item, mainTabPane);

                    String type = item.getType();
                    String[] properties = item.getProperties();
                    String[] tags = item.getTags();
                    CheckBox main = new CheckBox();
                    CheckBox off = new CheckBox();

                    if (character.getMainHand().get().equals(item) || character.getArmor().get().equals(item)) {
                        main.setSelected(true);
                    }
                    if (character.getOffHand().get().equals(item) || character.getShield().get().equals(item)) {
                        off.setSelected(true);
                    }

                    if (!type.equals("WEAPON") && !type.equals("ARMOR")) {
                        main.setVisible(false);
                        off.setVisible(false);
                    } else if (Arrays.asList(tags).contains("SHIELDS")) {
                        main.setVisible(false);
                    } else if (!Arrays.asList(properties).contains("VERSATILE") && !Arrays.asList(properties).contains("TWO_HANDED")) {
                        off.setVisible(false);
                    }

                    Runnable updateMain = () -> {
                        if (type.equals("WEAPON")) {
                            Item mainHand = character.getMainHand().get();
                            Item offHand = character.getOffHand().get();
                            Item shield = character.getShield().get();
                            boolean isTwoHanded = Arrays.asList(properties).contains("TWO_HANDED");
                            if (isTwoHanded && ((!offHand.getNominative().equals("NONE_F") && !offHand.equals(item)) || !shield.getNominative().equals("NONE_M"))) {
                                main.disableProperty().set(true);
                            } else {
                                if (mainHand.equals(item) || mainHand.getNominative().equals("UNARMED_STRIKE")) {
                                    main.disableProperty().set(false);
                                } else {
                                    main.disableProperty().set(true);
                                }
                            }
                        } else if (type.equals("ARMOR")) {
                            Item armor = character.getArmor().get();
                            if (armor.equals(item) || armor.getNominative().equals("NONE_F")) {
                                main.disableProperty().set(false);
                            } else {
                                main.disableProperty().set(true);
                            }
                        }
                    };
                    updateMain.run();
                    character.getMainHand().addListener(_ -> updateMain.run());
                    if (Arrays.asList(properties).contains("TWO_HANDED")) {
                        character.getOffHand().addListener(_ -> updateMain.run());
                        character.getShield().addListener(_ -> updateMain.run());
                    }
                    character.getArmor().addListener(_ -> updateMain.run());

                    Runnable updateOff = () -> {
                        if (type.equals("WEAPON")) {
                            Item shield = character.getShield().get();
                            if (shield.getNominative().equals("NONE_M")) {
                                Item offHand = character.getOffHand().get();
                                Item mainHand = character.getMainHand().get();
                                boolean isVersatile = Arrays.asList(properties).contains("VERSATILE");
                                boolean isTwoHanded = Arrays.asList(properties).contains("TWO_HANDED");
                                if (isTwoHanded) {
                                    if (mainHand.equals(item)) {
                                        off.setSelected(true);
                                    } else {
                                        off.setSelected(false);
                                    }
                                    off.disableProperty().set(true);
                                } else if (isVersatile && !mainHand.equals(item)) {
                                    off.setSelected(false);
                                    off.disableProperty().set(true);
                                } else {
                                    if (offHand.equals(item) ||  offHand.getNominative().equals("NONE_F")) {
                                        off.disableProperty().set(false);
                                    } else {
                                        off.disableProperty().set(true);
                                    }
                                }
                            } else {
                                off.disableProperty().set(true);
                            }
                        } else if (type.equals("ARMOR")) {
                            Item shield = character.getShield().get();
                            Item offHand = character.getOffHand().get();
                            if (offHand.getNominative().equals("NONE_F") && (shield.equals(item) || shield.getNominative().equals("NONE_M"))) {
                                off.disableProperty().set(false);
                            } else {
                                off.disableProperty().set(true);
                            }
                        }
                    };
                    updateOff.run();
                    character.getOffHand().addListener(_ -> updateOff.run());
                    if (Arrays.asList(properties).contains("VERSATILE") || Arrays.asList(properties).contains("TWO_HANDED")) {
                        character.getMainHand().addListener(_ -> updateOff.run());
                    }
                    character.getShield().addListener(_ -> updateOff.run());

                    main.selectedProperty().addListener((_, _, newVal) -> {
                        if (newVal) {
                            if (type.equals("WEAPON")) {
                                character.getMainHand().set(item);
                            } else if (type.equals("ARMOR")) {
                                character.getArmor().set(item);
                            }
                        } else {
                            if (type.equals("WEAPON")) {
                                if (character.getMainHand().get().equals(item)) {
                                    character.getMainHand().set(new Item("UNARMED_STRIKE"));
                                }
                            } else if (type.equals("ARMOR")) {
                                if (character.getArmor().get().equals(item)) {
                                    character.getArmor().set(new Item("NONE_F"));
                                }
                            }
                        }
                    });

                    off.selectedProperty().addListener((_, _, newVal) -> {
                        if (newVal) {
                            if (type.equals("WEAPON")) {
                                character.getOffHand().set(item);
                            } else if (type.equals("ARMOR")) {
                                character.getShield().set(item);
                            }
                        } else {
                            if (type.equals("WEAPON")) {
                                if (character.getOffHand().get().equals(item)) {
                                    character.getOffHand().set(new Item("NONE_F"));
                                }
                            } else if (type.equals("ARMOR")) {
                                if (character.getShield().get().equals(item)) {
                                    character.getShield().set(new Item("NONE_M"));
                                }
                            }
                        }
                    });

                    quantityProperty.addListener((_, _, newVal) -> {
                        if (newVal.intValue() > 1) {
                            itemLabel.setText(newVal.intValue() + "x " + item.getName());
                            if (type.equals("WEAPON") && Arrays.asList(properties).contains("LIGHT")) {
                                off.setVisible(true);
                            }
                        } else {
                            itemLabel.setText(item.getName());
                        }
                    });

                    Button remove = new Button("-");
                    remove.setOnAction(_ -> {
                        int quantity = quantityProperty.get();
                        if (quantity > 1) {
                            quantityProperty.set(quantity - 1);
                            if (character.getMainHand().get().equals(item) && character.getOffHand().get().equals(item) && quantity - 1 == 1) {
                                character.getOffHand().set(new Item("NONE_F"));
                            }
                        } else {
                            if (character.getMainHand().get().equals(item)) {
                                character.getMainHand().set(new Item("UNARMED_STRIKE"));
                            }
                            if (character.getOffHand().get().equals(item)) {
                                character.getOffHand().set(new Item("NONE_F"));
                            }
                            else if (character.getArmor().get().equals(item)) {
                                character.getArmor().set(new Item("NONE_F"));
                            }
                            else if (character.getShield().get().equals(item)) {
                                character.getShield().set(new Item("NONE_M"));
                            }
                        }
                        character.getItems().remove(item);
                    });

                    itemBox.getChildren().addAll(remove, main, off, itemLabel);
                    itemsBox.getChildren().add(itemBox);
                }
            }
        };
        updateItems.run();
        character.getItems().addListener((ListChangeListener<Item>) _ -> {
            updateItems.run();
        });
        add(itemsBox, 0, 3);

        TextField addItem = new TextField();
        addItem.setPromptText(getTranslation("ADD_ITEM"));
        add(addItem, 0, 4);

        // Create autocomplete popup
        Popup suggestionPopup = new Popup();
        ListView<String> suggestionList = new ListView<>();
        suggestionPopup.getContent().add(suggestionList);
        suggestionPopup.setAutoHide(true);

        // Get all available items
        ObservableList<String> allItemsList = FXCollections.observableArrayList(getTranslations(getAllItems()));

        // Listen to text changes for autocomplete
        addItem.textProperty().addListener((_, _, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                suggestionPopup.hide();
                return;
            }

            // Extract the item name (remove quantity prefix if present)
            String searchText = newValue.trim();
            if (searchText.split(" ")[0].matches("\\d+") && searchText.split(" ").length > 1) {
                searchText = searchText.substring(searchText.indexOf(" ") + 1);
            }

            // Filter items based on input
            ObservableList<String> filteredItems = FXCollections.observableArrayList();
            for (String item : allItemsList) {
                if (item.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredItems.add(item);
                }
            }

            if (filteredItems.isEmpty()) {
                suggestionPopup.hide();
            } else {
                suggestionList.setItems(filteredItems);
        
                // Adjust height based on number of items (max 5 visible)
                int visibleItems = Math.min(filteredItems.size(), 5);
                suggestionList.setPrefHeight(visibleItems * 24 + 2); // TODO: make dynamic
                suggestionList.getSelectionModel().selectFirst();
                
                // Show popup below the text field
                if (!suggestionPopup.isShowing()) {
                    Bounds bounds = addItem.localToScreen(addItem.getBoundsInLocal());
                    if (bounds != null) {
                        suggestionPopup.show(addItem, bounds.getMinX(), bounds.getMaxY());
                    }
                }
            }
        });

        // Handle selection from suggestion list
        Runnable suggestionHandler = () -> {
            String selectedItem = suggestionList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Preserve quantity prefix if it exists
                String currentText = addItem.getText().trim();
                if (currentText.split(" ")[0].matches("\\d+")) {
                    String quantity = currentText.split(" ")[0];
                    addItem.setText(quantity + " " + selectedItem);
                } else {
                    addItem.setText(selectedItem);
                }
                suggestionPopup.hide();
                addItem.requestFocus();
                addItem.positionCaret(addItem.getText().length());
            }
        };

        suggestionList.setOnMouseClicked(_ -> {
            suggestionHandler.run();
        });
        
        // Handle keyboard navigation
        addItem.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String itemName = addItem.getText().trim();
                if (itemName.split(" ")[0].matches("\\d+")) {
                    itemName = itemName.split(" ")[0] + " " + getOriginal(itemName.substring(itemName.indexOf(" ") + 1));
                } else {
                    itemName = getOriginal(itemName);
                }
                if (!itemName.isEmpty()) {
                    character.addItem(itemName);
                    addItem.clear();
                }
            }
        });

        suggestionList.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case TAB -> {
                    suggestionHandler.run();
                    addItem.requestFocus();
                    suggestionList.getSelectionModel().clearSelection();
                }
                case ESCAPE -> {
                    suggestionPopup.hide();
                    addItem.requestFocus();
                }
                case ENTER -> {
                    String itemName = addItem.getText().trim();
                    if (itemName.split(" ")[0].matches("\\d+")) {
                        itemName = itemName.split(" ")[0] + " " + getOriginal(itemName.substring(itemName.indexOf(" ") + 1));
                    } else {
                        itemName = getOriginal(itemName);
                    }
                    if (!itemName.isEmpty()) {
                        character.addItem(itemName);
                        addItem.clear();
                    }
                }
                default -> {
                }
            }
        });

        // Hide popup when text field loses focus (unless clicking on suggestion list)
        addItem.focusedProperty().addListener((_, _, isNowFocused) -> {
            if (!isNowFocused && !suggestionList.isFocused()) {
                suggestionPopup.hide();
            }
        });
    }

    private String[] getAllItems() {
        return ItemManager.getInstance().getAllItems();
    }

    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }

    private String[] getTranslations(String[] key) {
        return TranslationManager.getTranslations(key);
    }

    private String getOriginal(String key) {
        return TranslationManager.getOriginal(key);
    }

    private int getInt(String[] key) {
        return GroupManager.getInstance().getInt(key);
    }

    private String[] getStrings(String[] key) {
        return GroupManager.getInstance().getStrings(key);
    }
}