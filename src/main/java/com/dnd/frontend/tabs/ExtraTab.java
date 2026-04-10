package com.dnd.frontend.tabs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import com.dnd.backend.CustomItemWriter;
import com.dnd.backend.GroupManager;
import com.dnd.backend.ItemManager;
import com.dnd.frontend.ViewModel;
import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.tooltip.TooltipComboBox;
import com.dnd.frontend.tooltip.TooltipLabel;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class ExtraTab extends Tab{
    public ExtraTab(ViewModel character, TabPane mainTabPane){
        setText(getTranslation("EXTRA"));
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid-pane");
        
        TooltipLabel heightLabel = new TooltipLabel(getTranslation("HEIGHT"), mainTabPane);
        gridPane.add(heightLabel, 0, 0); // Add the label to the GridPane

        // This doesn't work with feet. Could be changed. Or you could use real measurement units.
        TextField height = new TextField();

        // TextFormatter to allow only numbers with up to 2 decimal digits, not starting with '.'
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            // Regex: optional digits, optional (dot and up to 2 digits), but not starting with dot
            if (newText.matches("^\\d+(\\.\\d{0,2})?$") || newText.isEmpty()) { // Allow empty input or it won't allow to delete the first digit
                return change;
            }
            return null;
        };
        height.setTextFormatter(new TextFormatter<>(filter));

        gridPane.add(height, 0, 1); // Add the label to the GridPane
        height.textProperty().bindBidirectional(character.getHeight());
        
        TooltipLabel weightLabel = new TooltipLabel(getTranslation("WEIGHT"), mainTabPane);
        gridPane.add(weightLabel, 0, 2); // Add the label to the GridPane

        TextField weight = new TextField();
        weight.setTextFormatter(new TextFormatter<>(filter));

        gridPane.add(weight, 0, 3); // Add the label to the GridPane
        weight.textProperty().bindBidirectional(character.getWeight());

        TooltipLabel type = new TooltipLabel("", mainTabPane);
        type.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    String value = character.getCreatureType().get();
                    return getTranslation("CREATURE_TYPE") + ": " + value;
                },
                character.getCreatureType()
            )
        );
        character.getCreatureType().addListener((_, _, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                if (!gridPane.getChildren().contains(type)) {
                    gridPane.add(type, 0, 5);
                }
            } else {
                gridPane.getChildren().remove(type);
            }
        });

        VBox itemBox = new VBox();
        Label sectionTitle = new Label(getTranslation("CUSTOM_ITEMS"));
        itemBox.getChildren().add(sectionTitle);

        ObservableList<String> itemTypes = FXCollections.observableArrayList(
            getTranslation("ITEM"),
            getTranslation("WEAPON"),
            getTranslation("ARMOR"),
            getTranslation("SHIELD")
        );
        TooltipComboBox itemType = new TooltipComboBox(itemTypes, mainTabPane);
        itemType.setValue(getTranslation("ITEM"));
        itemBox.getChildren().add(itemType);

        TextField itemName = new TextField();
        itemName.setPromptText(getTranslation("ITEM_NAME"));
        itemBox.getChildren().add(itemName);

        HBox itemWeightBox = new HBox();

        TextField itemWeight = new TextField();
        itemWeight.setPromptText(getTranslation("ITEM_WEIGHT"));
        itemWeight.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        Label itemWeightUnitLabel = new Label(" lbs");
        itemWeightBox.getChildren().addAll(itemWeight, itemWeightUnitLabel);
        itemBox.getChildren().add(itemWeightBox);

        HBox costBox = new HBox();

        TextField itemCost = new TextField();
        itemCost.setPromptText(getTranslation("ITEM_COST"));
        itemCost.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));

        Label itemCostUnitLabel = new Label(" " + getTranslation("ITEM_COST_UNIT") + ": ");
        ObservableList<String> costUnits = FXCollections.observableArrayList();
        for (String unit : getStrings(new String[] {"money"})) {
            costUnits.add(getTranslation(unit));
        }
        TooltipComboBox itemCostUnit = new TooltipComboBox(costUnits, mainTabPane);
        itemCostUnit.getSelectionModel().select(2);

        costBox.getChildren().addAll(itemCost, itemCostUnitLabel, itemCostUnit);
        itemBox.getChildren().add(costBox);

        TextArea itemDescription = new TextArea();
        itemDescription.setPromptText(getTranslation("ITEM_DESCRIPTION"));
        itemBox.getChildren().add(itemDescription);

        String[] weaponMasteries = getStrings(new String[] {"weapon_masteries"});
        String[] weaponAttributes = getStrings(new String[] {"weapon_attributes"});
        String[] weaponProperties = getStrings(new String[] {"weapon_properties"});
        String[] weaponTags = getStrings(new String[] {"weapon_tags"});
        String[] armorTags = getStrings(new String[] {"armor_tags"});

        VBox weaponBox = new VBox();

        HBox damageBox = new HBox();
        TextField hits = new TextField();
        hits.setPromptText(getTranslation("NUMBER_OF_HITS"));
        hits.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        Label d = new Label(" D ");
        TextField damage = new TextField();
        damage.setPromptText(getTranslation("HIT_DAMAGE"));
        damage.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        damageBox.getChildren().addAll(hits, d, damage);
        weaponBox.getChildren().add(damageBox);

        Label attributesLabel = new Label(getTranslation("WEAPON_ATTRIBUTES") + " (" + getTranslation("HOLD_MESSAGE") + ")");
        ListView<String> attributesList = new ListView<>(FXCollections.observableArrayList(weaponAttributes));
        configListView(attributesList);
        weaponBox.getChildren().addAll(attributesLabel, attributesList);

        Label propertiesLabel = new Label(getTranslation("WEAPON_PROPERTIES") + " (" + getTranslation("HOLD_MESSAGE") + ")");
        ListView<String> propertiesList = new ListView<>(FXCollections.observableArrayList(weaponProperties));
        configListView(propertiesList);
        weaponBox.getChildren().addAll(propertiesLabel, propertiesList);

        Label tagsLabel = new Label(getTranslation("WEAPON_TAGS") + " (" + getTranslation("HOLD_MESSAGE") + ")");
        ListView<String> weaponTagsList = new ListView<>(FXCollections.observableArrayList(weaponTags));
        configListView(weaponTagsList);
        weaponBox.getChildren().addAll(tagsLabel, weaponTagsList);

        Label masteryLabel = new Label(getTranslation("WEAPON_MASTERY"));
        ObservableList<String> masteriesList = FXCollections.observableArrayList();
        for (String mastery : weaponMasteries) {
            masteriesList.add(getTranslation(mastery));
        }
        TooltipComboBox masteryComboBox = new TooltipComboBox(masteriesList, mainTabPane);
        masteryComboBox.getSelectionModel().selectFirst();
        weaponBox.getChildren().addAll(masteryLabel, masteryComboBox);

        HBox rangeBox = new HBox();
        TextField shortRange = new TextField();
        shortRange.setPromptText(getTranslation("SHORT_RANGE"));
        shortRange.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        TextField longRange = new TextField();
        longRange.setPromptText(getTranslation("LONG_RANGE"));
        longRange.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        rangeBox.getChildren().addAll(shortRange, longRange);
        weaponBox.getChildren().add(rangeBox);

        ObservableList<String> ammoKeys = FXCollections.observableArrayList();
        for (String ammo : getAmmos()) {
            ammoKeys.add(getTranslation(ammo));
        }
        ComboBox<String> ammoComboBox = new ComboBox<>(FXCollections.observableArrayList(ammoKeys));
        ammoComboBox.getSelectionModel().selectFirst();
        weaponBox.getChildren().add(ammoComboBox);

        HBox versatileDamageBox = new HBox();
        TextField versatileHits = new TextField();
        versatileHits.setPromptText(getTranslation("NUMBER_OF_HITS"));
        versatileHits.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        Label vd = new Label(" D ");
        TextField versatileDamage = new TextField();
        versatileDamage.setPromptText(getTranslation("HIT_DAMAGE"));
        versatileDamage.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        versatileDamageBox.getChildren().addAll(versatileHits, vd, versatileDamage);
        weaponBox.getChildren().add(versatileDamageBox);

        Runnable updateWeaponConditionalFields = () -> {
            List<String> selectedProperties = propertiesList.getSelectionModel().getSelectedItems();

            boolean requiresAmmo = selectedProperties.contains("AMMUNITION");
            ammoComboBox.setVisible(requiresAmmo);
            ammoComboBox.setManaged(requiresAmmo);
            if (!requiresAmmo) {
                ammoComboBox.setValue(null);
            }

            boolean requiresRange = selectedProperties.contains("RANGED") || selectedProperties.contains("THROWN");      
            rangeBox.setManaged(requiresRange);
            rangeBox.setVisible(requiresRange);
            if (!requiresRange) {
                shortRange.clear();
                longRange.clear();
            }

            boolean requiresVersatile = selectedProperties.contains("VERSATILE");
            versatileDamageBox.setVisible(requiresVersatile);
            versatileDamageBox.setManaged(requiresVersatile);
            if (!requiresVersatile) {
                versatileDamage.clear();
                versatileHits.clear();
            }
        };
        updateWeaponConditionalFields.run();
        propertiesList.getSelectionModel().getSelectedItems().addListener((javafx.collections.ListChangeListener.Change<? extends String> _) -> updateWeaponConditionalFields.run());
        weaponTagsList.getSelectionModel().getSelectedItems().addListener((javafx.collections.ListChangeListener.Change<? extends String> _) -> updateWeaponConditionalFields.run());

        // Armor-specific fields
        VBox armorBox = new VBox();
        TextField armorClass = new TextField();

        HBox armorClassBox = new HBox();
        armorClass.setPromptText(getTranslation("STARTING_ARMOR_CLASS"));
        armorClass.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        Label acLabel = new Label(getTranslation("AC") + ": ");
        armorClassBox.getChildren().addAll(acLabel, armorClass);
        armorBox.getChildren().add(armorClassBox);

        HBox dexterityBox = new HBox();
        ComboBox<String> dexterityMode = new ComboBox<>();
        dexterityMode.getItems().addAll(
            getTranslation("NO_BONUS"),
            getTranslation("FULL_BONUS"),
            getTranslation("LIMITED_BONUS")
        );
        dexterityMode.setValue(getTranslation("FULL_BONUS"));
        TextField dexterityLimit = new TextField();
        dexterityLimit.setPromptText(getTranslation("DEXTERITY_BONUS_LIMIT"));
        dexterityLimit.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        dexterityLimit.setVisible(false);
        dexterityLimit.setManaged(false);
        dexterityBox.getChildren().addAll(dexterityMode, dexterityLimit);
        armorBox.getChildren().add(dexterityBox);

        dexterityMode.setOnAction(_ -> {
            boolean limited = getTranslation("LIMITED_BONUS").equals(dexterityMode.getValue());
            dexterityLimit.setVisible(limited);
            dexterityLimit.setManaged(limited);
            if (!limited) {
                dexterityLimit.clear();
            }
        });

        HBox strengthBox = new HBox();
        CheckBox strengthRequirement = new CheckBox(getTranslation("STRENGTH_REQUIREMENT"));
        TextField requiredStrength = new TextField();
        requiredStrength.setPromptText(getTranslation("STRENGTH_REQUIREMENT") + " ");
        requiredStrength.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        requiredStrength.setVisible(false);
        requiredStrength.setManaged(false);
        strengthBox.getChildren().addAll(strengthRequirement, requiredStrength);
        armorBox.getChildren().add(strengthBox);

        strengthRequirement.selectedProperty().addListener((_, _, selected) -> {
            requiredStrength.setVisible(selected);
            requiredStrength.setManaged(selected);
            if (!selected) {
                requiredStrength.clear();
            }
        });

        CheckBox stealthDisadvantage = new CheckBox(getTranslation("STEALTH_DISADVANTAGE"));
        armorBox.getChildren().add(stealthDisadvantage);

        ObservableList<String> armorTagsList = FXCollections.observableArrayList();
        for (String tag : armorTags) {
            armorTagsList.add(getTranslation(tag));
        }
        TooltipComboBox armorTagsComboBox = new TooltipComboBox(armorTagsList, mainTabPane);
        armorTagsComboBox.getSelectionModel().selectFirst();
        armorBox.getChildren().add(armorTagsComboBox);

        VBox shieldBox = new VBox();
        TextField shieldArmorClass = new TextField();
        shieldArmorClass.setPromptText(getTranslation("ARMOR_CLASS_BONUS"));
        shieldArmorClass.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d*$")) {
                return change;
            }
            return null;
        }));
        shieldBox.getChildren().add(shieldArmorClass);
        shieldBox.setVisible(false);
        shieldBox.setManaged(false);

        itemBox.getChildren().addAll(weaponBox, armorBox, shieldBox);

        weaponBox.setVisible(false);
        weaponBox.setManaged(false);
        armorBox.setVisible(false);
        armorBox.setManaged(false);
        shieldBox.setVisible(false);
        shieldBox.setManaged(false);

        itemType.valueProperty().addListener((_, _, selected) -> {
            if (getTranslation("WEAPON").equals(selected)) {
                weaponBox.setVisible(true);
                weaponBox.setManaged(true);
                armorBox.setVisible(false);
                armorBox.setManaged(false);
                shieldBox.setVisible(false);
                shieldBox.setManaged(false);
            } else if (getTranslation("ARMOR").equals(selected)) {
                weaponBox.setVisible(false);
                weaponBox.setManaged(false);
                armorBox.setVisible(true);
                armorBox.setManaged(true);
                shieldBox.setVisible(false);
                shieldBox.setManaged(false);
            } else if (getTranslation("SHIELD").equals(selected)) {
                weaponBox.setVisible(false);
                weaponBox.setManaged(false);
                armorBox.setVisible(false);
                armorBox.setManaged(false);
                shieldBox.setVisible(true);
                shieldBox.setManaged(true);
            } else {
                weaponBox.setVisible(false);
                weaponBox.setManaged(false);
                armorBox.setVisible(false);
                armorBox.setManaged(false);
                shieldBox.setVisible(false);
                shieldBox.setManaged(false);
            }
        });

        Button saveItem = new Button(getTranslation("ADD_UPDATE_ITEM"));
        Button loadItem = new Button(getTranslation("LOAD_SELECTED"));
        Button deleteItem = new Button(getTranslation("DELETE_SELECTED"));
        ComboBox<String> existingItems = new ComboBox<>();
        Map<String, String> itemDisplayNames = new HashMap<>();
        existingItems.setConverter(new StringConverter<>() {
            @Override
            public String toString(String key) {
                if (key == null || key.isBlank()) {
                    return "";
                }
                String display = itemDisplayNames.get(key);
                if (display != null && !display.isBlank()) {
                    return display;
                }
                return getTranslation(key);
            }

            @Override
            public String fromString(String string) {
                return existingItems.getValue();
            }
        });
        existingItems.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isBlank()) {
                    setText(null);
                    return;
                }
                String display = itemDisplayNames.get(item);
                if (display != null && !display.isBlank()) {
                    setText(display);
                    return;
                }
                setText(getTranslation(item));
            }
        });
        existingItems.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isBlank()) {
                    setText(null);
                    return;
                }
                String display = itemDisplayNames.get(item);
                if (display != null && !display.isBlank()) {
                    setText(display);
                    return;
                }
                setText(getTranslation(item));
            }
        });
        existingItems.getSelectionModel().selectFirst();

        Label itemSaveResultLabel = new Label();
        final String[] loadedItemKey = new String[] {null};

        Runnable refreshItems = () -> {
            java.util.List<String> keys;
            try {
                keys = CustomItemWriter.getCustomItemKeys();
            } catch (IOException ex) {
                System.getLogger(ExtraTab.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                itemSaveResultLabel.setText(getTranslation("FAILED_TO_LOAD_ITEM") + ": " + ex.getMessage());
                existingItems.getItems().clear();
                return;
            }
            itemDisplayNames.clear();
            for (String key : keys) {
                try {
                    CustomItemWriter.CustomItemData data = CustomItemWriter.getCustomItem(key);
                    if (data != null && data.getName() != null && !data.getName().isBlank()) {
                        itemDisplayNames.put(key, data.getName());
                    } else {
                        itemDisplayNames.put(key, key);
                    }
                } catch (IOException ex) {
                    System.getLogger(ExtraTab.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
            existingItems.getItems().setAll(keys);
            if (!existingItems.getItems().isEmpty()) {
                existingItems.getSelectionModel().selectFirst();
            }
        };

        loadItem.setOnAction((ActionEvent event) -> {
            String selectedKey = existingItems.getValue();
            if (selectedKey == null || selectedKey.isBlank()) {
                itemSaveResultLabel.setText(getTranslation("SELECT_ITEM_TO_LOAD"));
                return;
            }
            
            try {
                CustomItemWriter.CustomItemData data = CustomItemWriter.getCustomItem(selectedKey);
                if (data == null) {
                    itemSaveResultLabel.setText(getTranslation("ITEM_NOT_FOUND") + selectedKey);
                    refreshItems.run();
                    return;
                }

                itemName.setText(data.getName());
                itemWeight.setText(String.valueOf(data.getWeight()));
                itemCost.setText(String.valueOf(data.getCost()));
                itemDescription.setText(data.getDescription());
                String translatedCostUnit = getTranslation(data.getCostUnit());
                if (translatedCostUnit != null && !translatedCostUnit.isBlank()
                        && itemCostUnit.getItems().contains(translatedCostUnit)) {
                    itemCostUnit.setValue(translatedCostUnit);
                }
                
                String loadedType = getTranslation(data.getType()) != null ? getTranslation(data.getType()) : getTranslation("ITEM");
                if (loadedType.equals(getTranslation("ARMOR"))
                    && List.of(data.getTags()).contains("SHIELDS")
                        && data.getDexterity() == 0
                        && data.getStrength() == 0
                        && !data.isStealth()) {
                    itemType.setValue(getTranslation("SHIELD"));
                    shieldArmorClass.setText(String.valueOf(data.getArmorClass()));
                } else {
                    itemType.setValue(loadedType);
                }
                
                if (loadedType.equals(getTranslation("WEAPON"))) {
                    hits.setText(String.valueOf(data.getHits()));
                    damage.setText(String.valueOf(data.getDamage()));
                    versatileHits.setText(data.getVersatileHits() > 0 ? String.valueOf(data.getVersatileHits()) : "");
                    versatileDamage.setText(data.getVersatileDamage() > 0 ? String.valueOf(data.getVersatileDamage()) : "");
                    masteryComboBox.setValue(data.getMastery() == null || data.getMastery().isBlank()
                        ? null
                        : getTranslation(data.getMastery()));
                    shortRange.setText(data.getShortRange() > 0 ? String.valueOf(data.getShortRange()) : "");
                    longRange.setText(data.getLongRange() > 0 ? String.valueOf(data.getLongRange()) : "");
                    ammoComboBox.setValue(data.getAmmo() == null || data.getAmmo().isBlank()
                        ? null
                        : getTranslation(data.getAmmo()));
                    selectListValues(attributesList, data.getAttributes());
                    selectListValues(propertiesList, data.getProperties());
                    selectListValues(weaponTagsList, data.getTags());
                    updateWeaponConditionalFields.run();
                } else if (loadedType.equals(getTranslation("ARMOR"))) {
                    armorClass.setText(String.valueOf(data.getArmorClass()));
                    switch (data.getDexterity()) {
                        case 0 -> {
                            dexterityMode.setValue(getTranslation("NO_BONUS"));
                            dexterityLimit.clear();
                        }
                        case -1 -> {
                            dexterityMode.setValue(getTranslation("FULL_BONUS"));
                            dexterityLimit.clear();
                        }
                        default -> {
                            dexterityMode.setValue(getTranslation("LIMITED_BONUS"));
                            dexterityLimit.setText(String.valueOf(data.getDexterity()));
                        }
                    }
                    
                    if (data.getStrength() > 0) {
                        strengthRequirement.setSelected(true);
                        requiredStrength.setText(String.valueOf(data.getStrength()));
                    } else {
                        strengthRequirement.setSelected(false);
                        requiredStrength.clear();
                    }
                    stealthDisadvantage.setSelected(data.isStealth());
                    armorTagsComboBox.setValue(data.getTags() == null || data.getTags().length == 0
                        ? null
                        : getTranslation(data.getTags()[0]));
                }
                
                loadedItemKey[0] = data.getKey();
                itemSaveResultLabel.setText("Loaded " + data.getKey());
            } catch (IOException ex) {
                itemSaveResultLabel.setText(getTranslation("FAILED_TO_LOAD_ITEM") + ": " + ex.getMessage());
            }
        });

        deleteItem.setOnAction(_ -> {
            String selectedKey = existingItems.getValue();
            if (selectedKey == null || selectedKey.isBlank()) {
                itemSaveResultLabel.setText("Select an item to delete.");
                return;
            }

            try {
                CustomItemWriter.deleteItem(selectedKey);
                if (selectedKey.equals(loadedItemKey[0])) {
                    loadedItemKey[0] = null;
                    itemName.clear();
                    itemWeight.clear();
                    itemCost.clear();
                    itemDescription.clear();
                    hits.clear();
                    damage.clear();
                    versatileHits.clear();
                    versatileDamage.clear();
                    shortRange.clear();
                    longRange.clear();
                    ammoComboBox.setValue(null);
                    masteryComboBox.setValue(null);
                    attributesList.getSelectionModel().clearSelection();
                    propertiesList.getSelectionModel().clearSelection();
                    weaponTagsList.getSelectionModel().clearSelection();
                    armorClass.clear();
                    dexterityMode.setValue("NO_BONUS");
                    dexterityLimit.clear();
                    strengthRequirement.setSelected(false);
                    requiredStrength.clear();
                    stealthDisadvantage.setSelected(false);
                    armorTagsComboBox.setValue(null);
                    shieldArmorClass.clear();
                }
                refreshItems.run();
                itemSaveResultLabel.setText(getTranslation("DELETED") + ": " + selectedKey);
            } catch (java.io.IOException ex) {
                itemSaveResultLabel.setText(getTranslation("FAILED_TO_DELETE") + ": " + ex.getMessage());
            }
        });

        saveItem.setOnAction(_ -> {
            String name = itemName.getText();
            String weightText = itemWeight.getText();
            String costText = itemCost.getText();
            String costUnit = itemCostUnit.getValue();
            String originalCostUnit = getOriginal(costUnit);
            String description = itemDescription.getText();
            String itemTypeValue = itemType.getValue();

            if (name == null || name.trim().isEmpty()) {
                itemSaveResultLabel.setText(getTranslation("ITEM_NAME_REQUIRED"));
                return;
            }

            if (weightText == null || weightText.isBlank() || costText == null || costText.isBlank()) {
                itemSaveResultLabel.setText(getTranslation("WEIGHT_AND_COST_REQUIRED"));
                return;
            }

            try {
                int weightValue = Integer.parseInt(weightText);
                int costValue = Integer.parseInt(costText);
                String key;

                if (getTranslation("WEAPON").equals(itemTypeValue)) {
                        String hitsText = hits.getText();
                        String damageText = damage.getText();
                        if (hitsText == null || hitsText.isBlank() || damageText == null || damageText.isBlank()) {
                            itemSaveResultLabel.setText(getTranslation("WEAPON_REQUIRES_HITS_AND_DAMAGE"));
                            return;
                        }
                        int hitsValue = Integer.parseInt(hitsText);
                        int damageValue = Integer.parseInt(damageText);

                        Integer versatileHitsValue = null;
                        Integer versatileDamageValue = null;
                        if (propertiesList.getSelectionModel().getSelectedItems().contains("VERSATILE")) {
                            String versatileHitsText = versatileHits.getText();
                            String versatileText = versatileDamage.getText();
                            if (versatileText == null || versatileText.isBlank() || versatileHitsText == null || versatileHitsText.isBlank()) {
                                itemSaveResultLabel.setText(getTranslation("VERSATILE_REQUIRES_2HAND_DAMAGE"));
                                return;
                            }
                            versatileDamageValue = Integer.valueOf(versatileText);
                            versatileHitsValue = Integer.valueOf(versatileHitsText);
                        }

                        Integer shortRangeValue = null;
                        Integer longRangeValue = null;
                        List<String> selectedTags = List.copyOf(weaponTagsList.getSelectionModel().getSelectedItems());
                        List<String> selectedTagsOriginal = selectedTags.stream().map(this::getOriginal).toList();
                        if (selectedTagsOriginal.contains("RANGED") || selectedTagsOriginal.contains("THROWN")) {
                            String shortRangeText = shortRange.getText();
                            String longRangeText = longRange.getText();
                            if (shortRangeText == null || shortRangeText.isBlank() || longRangeText == null || longRangeText.isBlank()) {
                                itemSaveResultLabel.setText(getTranslation("RANGED_WEAPONS_REQUIRE_RANGE"));
                                return;
                            }
                            shortRangeValue = Integer.valueOf(shortRangeText);
                            longRangeValue = Integer.valueOf(longRangeText);
                        }

                        String ammo = null;
                        List<String> selectedProperties = List.copyOf(propertiesList.getSelectionModel().getSelectedItems());
                        List<String> selectedPropertiesOriginal = selectedProperties.stream().map(this::getOriginal).toList();
                        if (selectedPropertiesOriginal.contains("AMMUNITION")) {
                            ammo = getOriginal(ammoComboBox.getValue());
                            if (ammo == null || ammo.isBlank()) {
                                itemSaveResultLabel.setText(getTranslation("AMMUNITION_REQUIRED"));
                                return;
                            }
                        }

                        key = CustomItemWriter.upsertWeapon(
                            name, weightValue, costValue, originalCostUnit, description,
                            hitsValue,
                            damageValue,
                            versatileHitsValue,
                            versatileDamageValue,
                            attributesList.getSelectionModel().getSelectedItems().stream().map(this::getOriginal).toArray(String[]::new),
                            selectedPropertiesOriginal.toArray(String[]::new),
                            selectedTagsOriginal.toArray(String[]::new),
                            getOriginal(masteryComboBox.getValue()),
                            shortRangeValue,
                            longRangeValue,
                            ammo
                        );
                } else if (getTranslation("ARMOR").equals(itemTypeValue)) {
                        String acText = armorClass.getText();
                        if (acText == null || acText.isBlank()) {
                            itemSaveResultLabel.setText(getTranslation("ARMOR_REQUIRES_ARMOR_CLASS"));
                            return;
                        }
                        int ac = Integer.parseInt(acText);

                        int dex;
                        if (getTranslation("FULL_BONUS").equals(dexterityMode.getValue())) {
                            dex = -1;
                        } else if (getTranslation("LIMITED_BONUS").equals(dexterityMode.getValue())) {
                            String limit = dexterityLimit.getText();
                            if (limit == null || limit.isBlank()) {
                                itemSaveResultLabel.setText(getTranslation("DEXTERITY_BONUS_LIMIT_REQUIRED"));
                                return;
                            }
                            dex = Integer.parseInt(limit);
                        } else {
                            dex = 0;
                        }

                        int str = 0;
                        if (strengthRequirement.isSelected()) {
                            String strText = requiredStrength.getText();
                            if (strText == null || strText.isBlank()) {
                                itemSaveResultLabel.setText(getTranslation("STRENGTH_REQUIREMENT_REQUIRED"));
                                return;
                            }
                            str = Integer.parseInt(strText);
                        }

                        key = CustomItemWriter.upsertArmor(
                            name, weightValue, costValue, originalCostUnit, description,
                            ac,
                            dex,
                            str,
                            stealthDisadvantage.isSelected(),
                            new String[] {getOriginal(armorTagsComboBox.getValue())}
                        );
                } else if (getTranslation("SHIELD").equals(itemTypeValue)) {
                        String acText = shieldArmorClass.getText();
                        if (acText == null || acText.isBlank()) {
                            itemSaveResultLabel.setText(getTranslation("SHIELD_REQUIRES_ARMOR_CLASS"));
                            return;
                        }
                        int ac = Integer.parseInt(acText);
                        key = CustomItemWriter.upsertArmor(
                            name,
                            weightValue,
                            costValue,
                            originalCostUnit,
                            description,
                            ac,
                            0,
                            0,
                            false,
                            new String[] {"SHIELDS"}
                        );
                } else {
                    key = CustomItemWriter.upsertItem(name, weightValue, costValue, originalCostUnit, description);
                }

                if (loadedItemKey[0] != null && !loadedItemKey[0].equals(key)) {
                    CustomItemWriter.deleteItem(loadedItemKey[0]);
                }

                itemSaveResultLabel.setText(getTranslation("SAVED_AS") + ": " + key);
                loadedItemKey[0] = null;
                itemName.clear();
                itemWeight.clear();
                itemCost.clear();
                itemDescription.clear();
                hits.clear();
                damage.clear();
                versatileDamage.clear();
                shortRange.clear();
                longRange.clear();
                ammoComboBox.setValue(null);
                masteryComboBox.setValue(null);
                attributesList.getSelectionModel().clearSelection();
                propertiesList.getSelectionModel().clearSelection();
                weaponTagsList.getSelectionModel().clearSelection();
                updateWeaponConditionalFields.run();
                armorClass.clear();
                dexterityMode.setValue(getTranslation("NO_BONUS"));
                dexterityLimit.clear();
                strengthRequirement.setSelected(false);
                requiredStrength.clear();
                stealthDisadvantage.setSelected(false);
                armorTagsComboBox.setValue(null);
                shieldArmorClass.clear();
                itemType.setValue(getTranslation("ITEM"));
                itemCostUnit.getSelectionModel().select(2);
                refreshItems.run();
            } catch (IllegalArgumentException | java.io.IOException ex) {
                itemSaveResultLabel.setText(getTranslation("FAILED_TO_SAVE") + ": " + ex.getMessage());
            }
        });

        itemBox.getChildren().addAll(
            saveItem,
            existingItems,
            loadItem,
            deleteItem,
            itemSaveResultLabel
        );
        refreshItems.run();
        gridPane.add(itemBox, 0, 6);

        // Set the GridPane inside a ScrollPane so long forms remain usable.
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        setContent(scrollPane);
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }

    private static void configListView(ListView<String> listView) {
        // TODO: Done with AI, need to redo by hand
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.getStyleClass().add("auto-fit-list-view");
        listView.setFixedCellSize(24);
        listView.prefHeightProperty().bind(
            Bindings.size(listView.getItems())
                .multiply(listView.getFixedCellSize())
                .add(2)
        );
        listView.setMinHeight(Region.USE_PREF_SIZE);
        listView.setMaxHeight(Region.USE_PREF_SIZE);
        listView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(TranslationManager.getTranslation(item));
            }
        });
    }

    private static void selectListValues(ListView<String> listView, String[] values) {
        listView.getSelectionModel().clearSelection();
        if (values == null) {
            return;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                listView.getSelectionModel().select(value);
            }
        }
    }

    private String[] getStrings(String[] key) {
        return GroupManager.getInstance().getStrings(key);
    }

    private String[] getAmmos() {
        return ItemManager.getInstance().getAmmos();
    }

    private String getOriginal(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        String original = TranslationManager.getOriginal(value);
        if (original == null || original.isBlank()) {
            return value;
        }
        return original;
    }
}