package com.dnd.ui.tabs;

import com.dnd.TranslationManager;
import com.dnd.ui.tooltip.TooltipTitledPane;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipLabel;

import javafx.scene.control.Tab;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TabPane;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;

public class MagicTab extends Tab{
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

        VBox cantripsBox = new VBox(5);
        TooltipTitledPane cantripsPane = new TooltipTitledPane(getTranslation("CANTRIPS"), cantripsBox);
        gridPane.add(cantripsPane, 0, 1);

        VBox[] levelBoxes = new VBox[9];
        for (int i = 0; i < 9; i++) {
            VBox levelBox = new VBox(5);
            levelBoxes[i] = levelBox;
            TooltipTitledPane levelPane = new TooltipTitledPane(getTranslation("LEVEL") + " " + (i + 1), levelBox);
            gridPane.add(levelPane, 1 + i / 3, 1 + i % 3);
        }

        Runnable updateCantrips = () -> {
            cantripsBox.getChildren().clear();
            ObservableList<StringProperty> cantrips = character.getCantrips();
            for (StringProperty cantrip : cantrips) {
                TooltipLabel cantripLabel = new TooltipLabel(getTranslation(cantrip.get()), mainTabPane);
                cantripsBox.getChildren().add(cantripLabel);
            }
        };
        character.getCantrips().addListener((ListChangeListener<StringProperty>) _ -> {
            updateCantrips.run();
        });

        Runnable updateSpells = () -> {
            ObservableList<StringProperty> spells = character.getSpells();
            for (int i = 0; i < 9; i++) {
                levelBoxes[i].getChildren().clear();
            }
            for (StringProperty spell : spells) {
                int spellLevel = getInt(new String[] {"spells", spell.get(), "level"});
                VBox levelBox = levelBoxes[spellLevel - 1];
                TooltipLabel spellLabel = new TooltipLabel(getTranslation(spell.get()), mainTabPane);
                levelBox.getChildren().add(spellLabel);
            }
        };
        character.getSpells().addListener((ListChangeListener<StringProperty>) _ -> {
            updateSpells.run();
        });
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

        ObservableList<StringProperty> availableCantrips = character.getAvailableCantrips();
        ObservableList<StringProperty> cantrips = character.getCantrips();
        for (int i = 0; i < availableCantrips.size(); i++) {
            StringProperty cantrip = availableCantrips.get(i);
            CheckBox cantripCheckBox = new CheckBox();
            TooltipLabel cantripLabel = new TooltipLabel(getTranslation(cantrip.get()), mainTabPane);
            if (cantrips.contains(cantrip)) {
                cantripCheckBox.setSelected(true);
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

        ObservableList<StringProperty> availableSpells = character.getAvailableSpells();
        ObservableList<StringProperty> spells = character.getSpells();
        for (int i = 0; i < availableSpells.size(); i++) {
            StringProperty spell = availableSpells.get(i);
            int spellLevel = getInt(new String[] {"spells", spell.get(), "level"});
            GridPane levelGrid = levelGrids[spellLevel - 1];

            CheckBox spellCheckBox = new CheckBox();
            TooltipLabel spellLabel = new TooltipLabel(getTranslation(spell.get()), mainTabPane);
            if (spells.contains(spell)) {
                spellCheckBox.setSelected(true);
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

        Button saveButton = new Button(getTranslation("SAVE"));
        saveButton.setOnAction(_ -> spellStage.close());
        spellLayout.getChildren().add(saveButton);

        // Show and wait - this makes it modal
        spellStage.showAndWait();
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private int getInt(String[] key) {
        return TranslationManager.getInstance().getInt(key);
    }
}