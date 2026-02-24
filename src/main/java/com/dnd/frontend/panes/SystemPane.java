package com.dnd.frontend.panes;

import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.ViewModel;
import com.dnd.backend.GroupManager;
import com.dnd.frontend.tooltip.TooltipComboBox;
import com.dnd.frontend.tooltip.TooltipLabel;
import com.dnd.frontend.tabs.CharacterTab;
import com.dnd.frontend.tooltip.TooltipButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SystemPane extends GridPane {
    private String advantage = getTranslation("DISABLED_M");
    private final ViewModel character;

    public SystemPane(TabPane mainTabPane, AbilitiesPane abilitiesPane, HealthPane healthPane, TabPane classTabs, ViewModel character, Stage stage) {
        getStyleClass().add("grid-pane");
        this.character = character;
        TooltipLabel generationLabel = new TooltipLabel(getTranslation("GENERATION_METHOD"), mainTabPane);
        add(generationLabel, 0, 0); // Add the label to the GridPane (Column 0, Row 0)  
        generationLabel.visibleProperty().bind(character.isEditing());
        generationLabel.managedProperty().bind(character.isEditing());

        ObservableList<String> generations = FXCollections.observableArrayList();
        for (String generationKey : getStrings(new String[] {"generation_methods"})) {
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
        for (String healthKey : getStrings(new String[] {"health_methods"})) {
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
        advantages.addAll(getTranslation("DISABLED_M"), getTranslation("NONE"), getTranslation("ADVANTAGE"), getTranslation("DISADVANTAGE"));

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
            newCharacter.fill(true);
            newCharacter.isGenerator().set(false);
            newCharacter.isEditing().set(false);
            CharacterTab characterTab = new CharacterTab("", stage, mainTabPane);
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
            character.fill(false);
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
                CharacterTab characterTab = new CharacterTab("", stage, mainTabPane);
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

        TooltipButton shortRest = new TooltipButton(getTranslation("SHORT_REST"), mainTabPane);
        add(shortRest, 2, 2);
        shortRest.visibleProperty().bind(character.isEditing().not().and(character.isLongResting().not()).and(character.isLevelingUp().not()));
        shortRest.setOnAction(_ -> {
            if (!character.isShortResting().get()) {
                character.isShortResting().set(true);
                shortRest.setText(getTranslation("FINISH"));
            } else {
                character.isShortResting().set(false);
                shortRest.setText(getTranslation("SHORT_REST"));
            }
        });

        TooltipButton longRest = new TooltipButton(getTranslation("LONG_REST"), mainTabPane);
        add(longRest, 2, 3);
        longRest.visibleProperty().bind(character.isEditing().not().and(character.isShortResting().not()).and(character.isLevelingUp().not()));
        longRest.setOnAction(_ -> {
            if (!character.isLongResting().get()) {
                if (character.getCurrentHealth().get() > 0) {
                    character.isLongResting().set(true);
                    character.getUnconscious().set(true);
                    character.getCurrentHealth().set(character.getHealth().get());
                    for (int i = 0; i < 4; i++) {
                        character.getAvailableHitDie(i).set(character.getMaximumHitDie(i).get());
                    }
                    if (character.getExhaustion().get() > 0) {
                        character.getExhaustion().set(character.getExhaustion().get() - 1);
                    }

                    longRest.setText(getTranslation("FINISH"));
                }
            } else {
                character.isLongResting().set(false);
                character.getUnconscious().set(false);
                longRest.setText(getTranslation("LONG_REST"));
            }
        });

        TooltipButton levelUp = new TooltipButton(getTranslation("LEVEL_UP"), mainTabPane);
        add(levelUp, 2, 4);
        levelUp.setOnAction(_ -> {
            if (!character.isLevelingUp().get()) {
                int classIndex = classTabs.getSelectionModel().getSelectedIndex();
                character.areLevelingUp(classIndex).set(true);
                character.getLevel(classIndex).set(character.getLevel(classIndex).get() + 1);
                levelUp.setText(getTranslation("FINISH"));
                character.isLevelingUp().set(true);
            } else {
                character.fill(false);
                character.isLevelingUp().set(false);
                levelUp.setText(getTranslation("LEVEL_UP"));
            }
        });

        Runnable showLevelUp = () -> {
            if (!character.isEditing().get() && !character.isShortResting().get() && !character.isLongResting().get() && character.getTotalLevel().get() < 20) {
                levelUp.setVisible(true);
                levelUp.setManaged(true);
            } else {
                levelUp.setVisible(false);
                levelUp.setManaged(false);
            }
        };
        showLevelUp.run();
        character.isEditing().addListener(_ -> showLevelUp.run());
        character.isShortResting().addListener(_ -> showLevelUp.run());
        character.isLongResting().addListener(_ -> showLevelUp.run());
        character.getTotalLevel().addListener(_ -> showLevelUp.run());
    }

    public ViewModel getCharacter() {
        return character;
    }
    
    public String getAdvantage() {
        return getOriginal(advantage);
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }

    // Helper method to get original translation keys
    private String getOriginal(String key) {
        return TranslationManager.getOriginal(key);
    }

    private String[] getStrings(String[] key) {
        return GroupManager.getInstance().getStrings(key);
    }
}