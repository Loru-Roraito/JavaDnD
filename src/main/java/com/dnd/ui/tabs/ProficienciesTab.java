package com.dnd.ui.tabs;

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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.collections.ListChangeListener;

public class ProficienciesTab extends Tab{
    private final TabPane mainTabPane;
    private final String[] choiceArrays;
    private final List<ComboBox<String>> choiceComboBoxes;
    private final List<String[]> startingValues;
    private final List<ObservableList<String>> groupItemsList;
    private final ViewModel character;
    private final GridPane gridPane = new GridPane();
    public ProficienciesTab(ViewModel character, TabPane mainTabPane) {
        setText(getTranslation("PROFICIENCIES"));
        gridPane.getStyleClass().add("grid-pane");

        this.character = character;
        this.choiceComboBoxes = new java.util.ArrayList<>();
        this.startingValues = new ArrayList<>();
        this.groupItemsList = new ArrayList<>();
        this.mainTabPane = mainTabPane;

        choiceArrays = getGroup(new String[] {"sets"});

        TooltipLabel languages = new TooltipLabel(getTranslation("LANGUAGES") + ":", getTranslation("LANGUAGES"), mainTabPane);
        gridPane.add(languages, 0, 0);

        TooltipLabel common = new TooltipLabel(getTranslation("COMMON_LANGUAGE"), mainTabPane);
        gridPane.add(common, 0, 1);
        
        List<ObservableList<String>> languageValuesList = new ArrayList<>(2);
        List<ComboBox<String>> languageComboBoxes = new ArrayList<>(2);

        // In the future more languages should be available. Take the abilities ComboBoxes as an example for the available values update
        // Populate the class list and translation map
        ObservableList<String> commonLanguages = FXCollections.observableArrayList();
        String[] languageKeys = getGroupTranslations(new String[] {"common_languages"});
        commonLanguages.addAll(Arrays.asList(languageKeys));
        commonLanguages.add(0, getTranslation("RANDOM"));
        languageValuesList.add(commonLanguages);
        languageValuesList.add(FXCollections.observableArrayList(commonLanguages));

        TooltipComboBox languageOne = new TooltipComboBox(languageValuesList.get(0), mainTabPane);
        languageOne.setPromptText(getTranslation("RANDOM"));
        gridPane.add(languageOne, 0, 2);
        languageOne.valueProperty().bindBidirectional(character.getLanguageOne());

        TooltipComboBox languageTwo = new TooltipComboBox(languageValuesList.get(1), mainTabPane);
        languageTwo.setPromptText(getTranslation("RANDOM"));
        gridPane.add(languageTwo, 0, 3);
        
        languageTwo.valueProperty().bindBidirectional(character.getLanguageTwo());

        languageComboBoxes.add(languageOne);
        languageComboBoxes.add(languageTwo);
        
        for (ComboBox<String> comboBox : languageComboBoxes) {
            comboBox.valueProperty().addListener((_, _, _) -> {
                for (int j = 0; j < languageComboBoxes.size(); j++) {
                    ComboBoxUtils.updateItems(languageComboBoxes.get(j), languageComboBoxes, languageValuesList.get(j), languageKeys, new String[] {getTranslation("RANDOM")});
                }
            });
        }

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

        character.getWeaponProficiencies().addListener((ListChangeListener<StringProperty>) _ -> {
            updateProficiencies.run();
        });
        character.getArmorProficiencies().addListener((ListChangeListener<StringProperty>) _ -> {
            updateProficiencies.run();
        });
        character.getToolProficiencies().addListener((ListChangeListener<StringProperty>) _ -> {
            updateProficiencies.run();
        });
        updateProficiencies.run();

        GridPane proficienciesGridPane = new GridPane();
        proficienciesGridPane.add(proficienciesFlow, 0, 0);
        proficienciesGridPane.setPadding(new Insets(0));

        TooltipTitledPane proficienciesPane = new TooltipTitledPane(getTranslation("PROFICIENCIES"), proficienciesGridPane);
        gridPane.add(proficienciesPane, 0, 4);


        TextFlow activesFlow = new TextFlow();
        activesFlow.maxWidthProperty().bind(gridPane.widthProperty().multiply(0.2));

        // Initial population
        updateBox(activesFlow, character.getActives());

        character.getActives().addListener((ListChangeListener<StringProperty>) _ -> {
            updateBox(activesFlow, character.getActives());
        });

        TooltipTitledPane activesPane = new TooltipTitledPane(getTranslation("ACTIVE_ABILITIES"), activesFlow);
        gridPane.add(activesPane, 0, 5);


        TextFlow passivesFlow = new TextFlow();
        passivesFlow.maxWidthProperty().bind(gridPane.widthProperty().multiply(0.2));

        // Initial population
        updateBox(passivesFlow, character.getPassives());

        character.getPassives().addListener((ListChangeListener<StringProperty>) _ -> {
            updateBox(passivesFlow, character.getPassives());
        });

        TooltipTitledPane passivesPane = new TooltipTitledPane(getTranslation("PASSIVE_ABILITIES"), passivesFlow);

        gridPane.add(passivesPane, 0, 6);
        setContent(gridPane);
    }

    private void updateProficienciesBox(TextFlow textFlow, List<StringProperty> properties) {
        for (ComboBox<String> comboBox : choiceComboBoxes) {
            gridPane.getChildren().remove(comboBox);
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
                gridPane.add(comboBox, 0, 6 + index);

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