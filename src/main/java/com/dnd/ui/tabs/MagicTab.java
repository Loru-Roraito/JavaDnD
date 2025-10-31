package com.dnd.ui.tabs;

import com.dnd.SpellManager;
import com.dnd.TranslationManager;
import com.dnd.ui.tooltip.TooltipTitledPane;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.items.Spell;

import javafx.scene.control.Tab;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TabPane;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.Region;

public class MagicTab extends Tab {

    private final ViewModel character;
    private final TabPane mainTabPane; // Reference to the main TabPane

    public MagicTab(ViewModel character, TabPane mainTabPane) {
        this.character = character;
        this.mainTabPane = mainTabPane;
        setText(getTranslation("MAGIC"));

        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid-pane");
        setContent(gridPane);

        Button prepareSpellsButton = new Button(getTranslation("PREPARE_SPELLS"));
        prepareSpellsButton.setOnAction(_ -> openSpellSelectionWindow());
        gridPane.add(prepareSpellsButton, 0, 0);

        TooltipLabel abilityLabel = new TooltipLabel(getTranslation("SPELLCASTING_ABILITY"), mainTabPane);
        abilityLabel.textProperty().bind(Bindings.concat(getTranslation("SPELLCASTING_ABILITY"), ": ", character.getSpellcastingAbility(), " (", character.getSpellcastingAbilityModifier(), ")"));
        gridPane.add(abilityLabel, 1, 0, 2, 1);

        TooltipLabel attackLabel = new TooltipLabel(getTranslation("ATTACK_MODIFIER"), mainTabPane);
        attackLabel.textProperty().bind(Bindings.concat(getTranslation("ATTACK_MODIFIER"), ": ", character.getSpellcastingAttackModifier()));
        gridPane.add(attackLabel, 1, 1, 2, 1);

        TooltipLabel saveDCLabel = new TooltipLabel(getTranslation("DC"), mainTabPane);
        saveDCLabel.textProperty().bind(Bindings.concat(getTranslation("DC"), ": ", character.getSpellcastingSaveDC()));
        gridPane.add(saveDCLabel, 0, 1);

        VBox cantripsBox = new VBox(5);
        TooltipTitledPane cantripsPane = new TooltipTitledPane(getTranslation("CANTRIPS"), cantripsBox);
        gridPane.add(cantripsPane, 0, 2);
        GridPane.setVgrow(cantripsPane, Priority.ALWAYS);
        GridPane.setHgrow(cantripsPane, Priority.ALWAYS);

        VBox[] levelBoxes = new VBox[9];
        for (int i = 0; i < 9; i++) {
            int index = i;
            TooltipLabel titleLabel = new TooltipLabel(getTranslation("LEVEL") + " " + (index + 1), mainTabPane);
            HBox customHeader = new HBox(10);
            customHeader.getChildren().add(titleLabel);
            // Spacer to push checkboxes to the right
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            customHeader.getChildren().add(spacer);
            HBox checkBoxes = new HBox();
            customHeader.getChildren().add(checkBoxes);

            Runnable updateSpellSlots = () -> {
                checkBoxes.getChildren().clear();
                for (int j = 0; j < character.getSpellSlot(index).get(); j++) {
                    CheckBox spellLevel = new CheckBox();
                    checkBoxes.getChildren().add(spellLevel);

                    int newIndex = j;
                    Runnable updateAvailableSlots = () -> {
                        if (character.getAvailableSpellSlot(index).get() > newIndex) {
                            spellLevel.setSelected(true);
                        } else {
                            spellLevel.setSelected(false);
                        }
                    };
                    character.getAvailableSpellSlot(index).addListener((_, _, _) -> {
                        updateAvailableSlots.run();
                    });
                    updateAvailableSlots.run();

                    spellLevel.setOnAction(_ -> {
                        if (spellLevel.isSelected()) {
                            character.getAvailableSpellSlot(index).set(character.getAvailableSpellSlot(index).get() + 1);
                        } else {
                            character.getAvailableSpellSlot(index).set(character.getAvailableSpellSlot(index).get() - 1);
                        }
                    });
                }
            };
            character.getSpellSlot(index).addListener((_, _, _) -> {
                updateSpellSlots.run();
            });
            updateSpellSlots.run();

            VBox levelBox = new VBox(5);
            levelBoxes[index] = levelBox;
            TooltipTitledPane levelPane = new TooltipTitledPane("", levelBox);
            GridPane.setVgrow(levelPane, Priority.ALWAYS);
            GridPane.setHgrow(levelPane, Priority.ALWAYS);
            levelPane.setGraphic(customHeader);
            gridPane.add(levelPane, 1 + index / 3, 2 + index % 3);
        }

        Runnable updateCantrips = () -> {
            cantripsBox.getChildren().clear();
            ObservableList<Spell> cantrips = character.getCantrips();
            for (Spell cantrip : cantrips) {
                TooltipLabel cantripLabel = new TooltipLabel(cantrip, mainTabPane);
                cantripsBox.getChildren().add(cantripLabel);
            }
        };
        character.getCantrips().addListener((ListChangeListener<Spell>) _ -> {
            updateCantrips.run();
        });

        Runnable updateSpells = () -> {
            ObservableList<Spell> spells = character.getSpells();
            for (int i = 0; i < 9; i++) {
                levelBoxes[i].getChildren().clear();
            }
            for (Spell spell : spells) {
                int spellLevel = getSpellInt(new String[]{getOriginal(spell.getName()), "level"});
                VBox levelBox = levelBoxes[spellLevel - 1];

                TooltipLabel spellLabel = new TooltipLabel(spell, mainTabPane);
                levelBox.getChildren().add(spellLabel);
            }
        };
        character.getSpells().addListener((ListChangeListener<Spell>) _ -> {
            updateSpells.run();
        });

        updateCantrips.run();
        updateSpells.run();
    }

    private void openSpellSelectionWindow() {
        Stage spellStage = new Stage();
        spellStage.setTitle(getTranslation("PREPARE_SPELLS"));

        // Make it modal - blocks interaction with parent window
        spellStage.initModality(Modality.APPLICATION_MODAL);

        // Get the parent window to set as owner
        Stage parentStage = (Stage) getTabPane().getScene().getWindow();
        spellStage.initOwner(parentStage);

        VBox spellLayout = new VBox(10);
        ScrollPane SpellScroll = new ScrollPane(spellLayout);
        SpellScroll.setFitToWidth(true);
        Scene spellScene = new Scene(SpellScroll, 400, 600);
        spellScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        spellStage.setScene(spellScene);

        GridPane cantripsGrid = new GridPane();
        TooltipTitledPane cantripsPane = new TooltipTitledPane(getTranslation("CANTRIPS"), cantripsGrid);
        spellLayout.getChildren().add(cantripsPane);

        ObservableList<Spell> availableCantrips = character.getAvailableCantrips();
        ObservableList<Spell> cantrips = character.getCantrips();
        for (int i = 0; i < availableCantrips.size(); i++) {
            Spell cantrip = availableCantrips.get(i);
            CheckBox cantripCheckBox = new CheckBox();
            TooltipLabel cantripLabel = new TooltipLabel(cantrip, mainTabPane);
            for (Spell myCantrip : cantrips) {
                if (myCantrip.equals(cantrip)) {
                    cantripCheckBox.setSelected(true);
                    break;
                }
            }
            cantripCheckBox.setOnAction(_ -> {
                if (cantripCheckBox.isSelected()) {
                    cantrips.add(cantrip);
                } else {
                    cantrips.remove(cantrip);
                }
            });
            cantripCheckBox.disableProperty().bind(
                    character.getMaxCantrips().lessThanOrEqualTo(
                            Bindings.size(cantrips)
                    ).and(cantripCheckBox.selectedProperty().not())
            );

            cantripsGrid.add(cantripCheckBox, 0, i);
            cantripsGrid.add(cantripLabel, 1, i);
        }

        GridPane[] levelGrids = new GridPane[9];
        for (int i = 0; i < 9; i++) {
            GridPane levelGrid = new GridPane();
            levelGrids[i] = levelGrid;
            TooltipTitledPane levelPane = new TooltipTitledPane(getTranslation("LEVEL") + " " + (i + 1), levelGrid);
            spellLayout.getChildren().add(levelPane);
        }

        ObservableList<Spell> availableSpells = character.getAvailableSpells();
        ObservableList<Spell> spells = character.getSpells();
        for (int i = 0; i < availableSpells.size(); i++) {
            Spell spell = availableSpells.get(i);
            int spellLevel = spell.getLevel();
            GridPane levelGrid = levelGrids[spellLevel - 1];

            CheckBox spellCheckBox = new CheckBox();
            TooltipLabel spellLabel = new TooltipLabel(spell, mainTabPane);
            for (Spell mySpell : spells) {
                if (mySpell.equals(spell)) {
                    spellCheckBox.setSelected(true);
                    break;
                }
            }
            spellCheckBox.setOnAction(_ -> {
                if (spellCheckBox.isSelected()) {
                    spells.add(spell);
                } else {
                    spells.remove(spell);
                }
            });
            spellCheckBox.disableProperty().bind(
                    character.getMaxSpells().lessThanOrEqualTo(
                            Bindings.size(spells)
                    ).and(spellCheckBox.selectedProperty().not())
            );

            levelGrid.add(spellCheckBox, 0, i);
            levelGrid.add(spellLabel, 1, i);
        }

        // Show and wait - this makes it modal
        spellStage.showAndWait();
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String getOriginal(String spellName) {
        return TranslationManager.getInstance().getOriginal(spellName);
    }

    private int getSpellInt(String[] key) {
        return SpellManager.getInstance().getSpellInt(key);
    }
}
