package com.dnd.ui.panes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnd.DefinitionManager;
import com.dnd.MiscsManager;
import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.ui.tooltip.TooltipTitledPane;
import com.dnd.utils.ComboBoxUtils;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ProficienciesPane extends GridPane {
    private final TabPane mainTabPane;
    private final String[] choiceArrays;
    private final List<ComboBox<String>> choiceComboBoxes;
    private final List<String[]> startingValues;
    private final List<ObservableList<String>> groupItemsList;
    private final ViewModel character;
    public ProficienciesPane(ViewModel character, GridPane gridPane, TabPane mainTabPane) {
        this.character = character;
        this.choiceComboBoxes = new java.util.ArrayList<>();
        this.startingValues = new ArrayList<>();
        this.groupItemsList = new ArrayList<>();
        this.mainTabPane = mainTabPane;
        getStyleClass().add("grid-pane");

        choiceArrays = getGroup(new String[] {"sets"});

        TooltipLabel languages = new TooltipLabel(getTranslation("LANGUAGES") + ":", getTranslation("LANGUAGES"), mainTabPane);
        add(languages, 0, 0);

        TooltipLabel common = new TooltipLabel(getTranslation("COMMON_LANGUAGE"), mainTabPane);
        add(common, 0, 1);
        
        // In the future more languages should be available. Take the abilities ComboBoxes as an example for the available values update
        // Populate the class list and translation map
        ObservableList<String> commonLanguages = FXCollections.observableArrayList();
        for (String languageKey : getGroup(new String[] {"common_languages"})) {
            commonLanguages.add(getTranslation(languageKey));
        }
        commonLanguages.add(0, getTranslation("RANDOM"));

        TooltipComboBox languageOne = new TooltipComboBox(FXCollections.observableArrayList(commonLanguages), mainTabPane);
        languageOne.setPromptText(getTranslation("RANDOM"));
        add(languageOne, 0, 2);
        languageOne.valueProperty().bindBidirectional(character.getLanguageOne());

        TooltipComboBox languageTwo = new TooltipComboBox(FXCollections.observableArrayList(commonLanguages), mainTabPane);
        languageTwo.setPromptText(getTranslation("RANDOM"));
        add(languageTwo, 0, 3);
        languageTwo.valueProperty().bindBidirectional(character.getLanguageTwo());

        languageOne.valueProperty().addListener((_, _, newVal) -> {
            updateComboBox(languageTwo, commonLanguages, newVal);
        });

        languageTwo.valueProperty().addListener((_, _, newVal) -> {
            updateComboBox(languageOne, commonLanguages, newVal);
        });


        TextFlow proficienciesFlow = new TextFlow();
        proficienciesFlow.maxWidthProperty().bind(gridPane.widthProperty().multiply(0.2));

        Runnable updateProficiencies = () -> {
            proficienciesFlow.getChildren().clear();
            updateProficienciesBox(proficienciesFlow, character.getWeaponProficiencies());

            ObservableList<StringProperty> armorProficiencies = character.getArmorProficiencies();
            if (!armorProficiencies.isEmpty()) {
                if (!proficienciesFlow.getChildren().isEmpty()) {
                    proficienciesFlow.getChildren().add(new Text("\n\n"));
                }
                updateProficienciesBox(proficienciesFlow, armorProficiencies);
            }

            ObservableList<StringProperty> toolProficiencies = character.getToolProficiencies();
            if (!toolProficiencies.isEmpty()) {
                // Check if there is at least one proficiency that is NOT a group/set name
                boolean hasNonArray = toolProficiencies.stream()
                    .map(StringProperty::get)
                    .anyMatch(name -> !Arrays.asList(choiceArrays).contains(name));
                if (hasNonArray) {
                    if (!proficienciesFlow.getChildren().isEmpty()) {
                        proficienciesFlow.getChildren().add(new Text("\n\n"));
                    }
                }
                updateProficienciesBox(proficienciesFlow, toolProficiencies);
            }
        };

        character.getWeaponProficiencies().addListener((javafx.collections.ListChangeListener<StringProperty>) _ -> {
            updateProficiencies.run();
        });
        character.getArmorProficiencies().addListener((javafx.collections.ListChangeListener<StringProperty>) _ -> {
            updateProficiencies.run();
        });
        character.getToolProficiencies().addListener((javafx.collections.ListChangeListener<StringProperty>) _ -> {
            updateProficiencies.run();
        });
        updateProficiencies.run();

        GridPane proficienciesGridPane = new GridPane();
        proficienciesGridPane.add(proficienciesFlow, 0, 0);
        proficienciesGridPane.setPadding(new Insets(0));

        TooltipTitledPane proficienciesPane = new TooltipTitledPane(getTranslation("PROFICIENCIES"), proficienciesGridPane);
        add(proficienciesPane, 0, 4);


        TextFlow activesFlow = new TextFlow();
        activesFlow.maxWidthProperty().bind(gridPane.widthProperty().multiply(0.2));

        // Initial population
        updateBox(activesFlow, character.getActives());

        character.getActives().addListener((javafx.collections.ListChangeListener<StringProperty>) _ -> {
            updateBox(activesFlow, character.getActives());
        });

        TooltipTitledPane activesPane = new TooltipTitledPane(getTranslation("ACTIVE_ABILITIES"), activesFlow);
        add(activesPane, 0, 5);


        TextFlow passivesFlow = new TextFlow();
        passivesFlow.maxWidthProperty().bind(gridPane.widthProperty().multiply(0.2));

        // Initial population
        updateBox(passivesFlow, character.getPassives());

        character.getPassives().addListener((javafx.collections.ListChangeListener<StringProperty>) _ -> {
            updateBox(passivesFlow, character.getPassives());
        });

        TooltipTitledPane passivesPane = new TooltipTitledPane(getTranslation("PASSIVE_ABILITIES"), passivesFlow);

        add(passivesPane, 0, 6);
    }

    private void updateProficienciesBox(TextFlow textFlow, List<StringProperty> properties) {
        for (ComboBox<String> comboBox : choiceComboBoxes) {
            getChildren().remove(comboBox);
        }
        choiceComboBoxes.clear();
        startingValues.clear();
        groupItemsList.clear();

        Boolean isFirstLine = true;
        for (StringProperty proficiency : properties) {
            String name = proficiency.get();
            if (Arrays.asList(choiceArrays).contains(name)) {
                String[] items = getGroupTranslations(new String[] {"sets", name});
                ObservableList<String> groupItems = FXCollections.observableArrayList(items);
                groupItems.add(0, getTranslation("RANDOM"));
                TooltipComboBox comboBox = new TooltipComboBox(groupItems, mainTabPane);
                comboBox.setPromptText(getTranslation("RANDOM"));
                choiceComboBoxes.add(comboBox); 
                startingValues.add(items);  
                groupItemsList.add(groupItems);

                int index = choiceComboBoxes.size();
                add(comboBox, 0, 6 + index);

                comboBox.valueProperty().addListener((_, _, _) -> {
                    for (int j = 0; j < choiceComboBoxes.size(); j++) {
                        ComboBoxUtils.updateItems(choiceComboBoxes.get(j), choiceComboBoxes, groupItemsList.get(j), startingValues.get(j), new String[] {getTranslation("RANDOM")});
                    }
                });

                comboBox.valueProperty().bindBidirectional(character.getChoiceProficiencies().get(index - 1));

            } else {
                if (isFirstLine) {
                    isFirstLine = false;
                } else {
                    textFlow.getChildren().add(new Text("\n"));
                }
                Text wordText = new Text(name);
                textFlow.getChildren().add(wordText);
            }
        }
    }

    private void updateComboBox(ComboBox<String> comboBox, ObservableList<String> baseList, String excludedValue) {
        String currentValue = comboBox.getValue();
        ObservableList<String> newItems = FXCollections.observableArrayList(
            baseList.stream()
                .filter(s -> s.equals(getTranslation("RANDOM")) || !s.equals(excludedValue))
                .toList()
        );

        comboBox.setItems(newItems);

        // Preserve the selection if still valid
        if (currentValue != null && newItems.contains(currentValue)) {
            comboBox.setValue(currentValue);
        } else {
            comboBox.setValue(getTranslation("RANDOM"));
        }
    }

    private void updateBox(TextFlow textFlow, Iterable<StringProperty> properties) {
        textFlow.getChildren().clear();
        boolean isFirstLine = true;
        for (StringProperty property : properties) {
            if (isFirstLine) {
                isFirstLine = false;
            } else {
                textFlow.getChildren().add(new Text("\n\n"));
            }
            String name = property.get();
            String text = fetchMisc(name);
            Text wordText = new Text(name + ": ");
            wordText.setStyle("-fx-font-weight: bold;");
            textFlow.getChildren().add(wordText);
            DefinitionManager.getInstance().fillTextFlow(textFlow, text, mainTabPane, "");
        }
    }

    // Helper method to get translations
    private String fetchMisc(String key) {
        return MiscsManager.getInstance().fetchMisc(key);
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String[] getGroup(String[] key) {
        return TranslationManager.getInstance().getGroup(key);
    }

    private String[] getGroupTranslations(String[] key) {
        return TranslationManager.getInstance().getGroupTranslations(key);
    }
}