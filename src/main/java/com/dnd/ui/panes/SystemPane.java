package com.dnd.ui.panes;

import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.ui.tabs.CharacterTab;
import com.dnd.ui.tooltip.TooltipButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;


public class SystemPane extends GridPane {
    private String advantage = getTranslation("DISABLED_M");
    private final ViewModel character;
    public SystemPane(TabPane mainTabPane, AbilitiesPane abilitiesPane, HealthPane healthPane, ViewModel character) {
        getStyleClass().add("grid-pane");
        this.character = character;
        TooltipLabel generationLabel = new TooltipLabel(getTranslation("GENERATION_METHOD"), mainTabPane);
        add(generationLabel, 0, 0); // Add the label to the GridPane (Column 0, Row 0)  
        generationLabel.visibleProperty().bind(character.isEditing());
        generationLabel.managedProperty().bind(character.isEditing());

        ObservableList<String> generations = FXCollections.observableArrayList();
        for (String generationKey : getGroup(new String[] {"generation_methods"})) {
            generations.add(getTranslation(generationKey));
        }

        TooltipComboBox generationComboBox = new TooltipComboBox(generations, mainTabPane);
        generationComboBox.setPromptText(getTranslation("STANDARD_ARRAY"));
        add(generationComboBox, 0, 1); // Add the ComboBox to the GridPane (Column 0, Row 1);
        generationComboBox.visibleProperty().bind(character.isEditing());
        generationComboBox.managedProperty().bind(character.isEditing());

        // Listen for ComboBox changes
        generationComboBox.valueProperty().bindBidirectional(character.getGenerationMethod());

        generationComboBox.valueProperty().addListener((_, oldVal, newVal) -> {
            if (oldVal == null || !oldVal.equals(newVal)){
                abilitiesPane.chooseAbilitiesUI();
            }
        });

        TooltipLabel healthLabel = new TooltipLabel(getTranslation("HEALTH_METHOD"), mainTabPane);
        add(healthLabel, 1, 0); // Add the label to the GridPane
        healthLabel.visibleProperty().bind(character.isEditing());
        healthLabel.managedProperty().bind(character.isEditing());

        ObservableList<String> healths = FXCollections.observableArrayList();
        for (String healthKey : getGroup(new String[] {"health_methods"})) {
            healths.add(getTranslation(healthKey));
        }

        TooltipComboBox healthComboBox = new TooltipComboBox(healths, mainTabPane);
        healthComboBox.setPromptText(getTranslation("MEDIUM_HP"));
        add(healthComboBox, 1, 1); // Add the ComboBox to the GridPane
        healthComboBox.visibleProperty().bind(character.isEditing());
        healthComboBox.managedProperty().bind(character.isEditing());

        // Listen for ComboBox changes
        healthComboBox.valueProperty().bindBidirectional(character.getHealthMethod());

        healthComboBox.valueProperty().addListener((_, oldVal, newVal) -> {
            if (oldVal == null || !oldVal.equals(newVal)){
                healthPane.chooseHealthUI();
            }
        });

        TooltipLabel advantageLabel = new TooltipLabel(getTranslation("FORCED_ADVANTAGE"), mainTabPane);
        add(advantageLabel, 2, 0);
       
        ObservableList<String> advantages = FXCollections.observableArrayList(); 
        advantages.addAll(getTranslation("DISABLED_M"), getTranslation("NONE_M"), getTranslation("ADVANTAGE"), getTranslation("DISADVANTAGE"));

        TooltipComboBox advantageComboBox = new TooltipComboBox(advantages, mainTabPane);
        add(advantageComboBox, 2, 1); // Add the ComboBox to the GridPane
        advantageComboBox.setPromptText(getTranslation("DISABLED_M"));

        advantageComboBox.valueProperty().addListener((_, oldVal, newVal) -> {
            if (oldVal == null || !oldVal.equals(newVal)) {
                advantage = newVal;
            }
        });

        TooltipButton confirm = new TooltipButton(getTranslation("CONFIRM"), mainTabPane);
        add(confirm, 3, 1);
        confirm.setOnAction(_ -> {
            ViewModel newCharacter = character.duplicate();
            newCharacter.fill();
            newCharacter.isGenerator().set(false);
            newCharacter.isEditing().set(false);
            CharacterTab characterTab = new CharacterTab("", mainTabPane);
            characterTab.createSubTabPane(newCharacter);
            characterTab.setClosable(true); // Make the tab closable
            mainTabPane.getTabs().add(characterTab);
            characterTab.newEdit();
            mainTabPane.getSelectionModel().select(characterTab);
            newCharacter.setCharacterTab(characterTab);
        });
        confirm.visibleProperty().bind(character.isGenerator());
        confirm.managedProperty().bind(character.isGenerator());

        TooltipButton edit = new TooltipButton(getTranslation("EDIT"), mainTabPane);
        add(edit, 3, 1);
        edit.setOnAction(_ -> {
            character.isEditing().set(true);
        });
        edit.visibleProperty().bind(character.isGenerator().not().and(character.isEditing().not()));
        edit.managedProperty().bind(character.isGenerator().not().and(character.isEditing().not()));

        TooltipButton finish = new TooltipButton(getTranslation("FINISH"), mainTabPane);
        add(finish, 3, 1);
        finish.setOnAction(_ -> {
            character.fill();
            character.isEditing().set(false);
        });
        finish.visibleProperty().bind(character.isGenerator().not().and(character.isEditing()));
        finish.managedProperty().bind(character.isGenerator().not().and(character.isEditing()));

        TooltipButton save = new TooltipButton(getTranslation("SAVE"), mainTabPane);
        add(save, 4, 1);
        save.setOnAction(_ -> {
            character.save(false);
        });
        save.visibleProperty().bind(character.isGenerator().not().and(character.isEditing().not()));
        save.managedProperty().bind(character.isGenerator().not().and(character.isEditing().not()));

        TooltipButton saveAs = new TooltipButton(getTranslation("SAVE_AS"), mainTabPane);
        add(saveAs, 5, 1);
        saveAs.setOnAction(_ -> {
            character.save(true);
        });
        saveAs.visibleProperty().bind(character.isGenerator().not().and(character.isEditing().not()));
        saveAs.managedProperty().bind(character.isGenerator().not().and(character.isEditing().not()));

        TooltipButton load = new TooltipButton(getTranslation("LOAD"), mainTabPane);
        add(load, 4, 1);
        load.setOnAction(_ -> {
            ViewModel newCharacter = character.load();
            if (newCharacter != null) {
                newCharacter.isGenerator().set(false);
                newCharacter.isEditing().set(false);
                CharacterTab characterTab = new CharacterTab("", mainTabPane);
                characterTab.createSubTabPane(newCharacter);
                characterTab.setClosable(true); // Make the tab closable
                mainTabPane.getTabs().add(characterTab);
                characterTab.setText(newCharacter.getSaveName());
                mainTabPane.getSelectionModel().select(characterTab);
                newCharacter.setCharacterTab(characterTab);
            }
        });
        load.visibleProperty().bind(character.isGenerator());
        load.managedProperty().bind(character.isGenerator());
    }

    public ViewModel getCharacter() {
        return character;
    }
    
    public String getAdvantage() {
        return getOriginal(advantage);
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    // Helper method to get original translation keys
    private String getOriginal(String key) {
        return TranslationManager.getInstance().getOriginal(key);
    }

    private String[] getGroup(String[] key) {
        return TranslationManager.getInstance().getGroup(key);
    }
}