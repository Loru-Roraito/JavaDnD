package com.dnd.frontend.panes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnd.frontend.language.DefinitionManager;
import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.ViewModel;
import com.dnd.backend.GroupManager;
import com.dnd.frontend.ComboBoxUtils;
import com.dnd.frontend.language.MiscsManager;
import com.dnd.frontend.tooltip.TooltipComboBox;
import com.dnd.frontend.tooltip.TooltipLabel;
import com.dnd.frontend.tooltip.TooltipTitledPane;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.VBox;

public class ProficienciesPane extends GridPane {

    private final TabPane mainTabPane;
    private final String[] choiceArrays;
    private final List<TooltipComboBox> choiceComboBoxes;
    private final List<String[]> startingValues;
    private final List<ObservableList<String>> groupItemsList;
    private final ViewModel character;
    private final VBox proficienciesBox = new VBox();

    public ProficienciesPane(ViewModel character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");

        // TODO: I want it dynamic
        DoubleBinding paneWidthBinding = mainTabPane.widthProperty().multiply(0.26);

        this.character = character;
        this.choiceComboBoxes = new java.util.ArrayList<>();
        this.startingValues = new ArrayList<>();
        this.groupItemsList = new ArrayList<>();
        this.mainTabPane = mainTabPane;

        choiceArrays = getStrings(new String[]{"sets"});

        TooltipLabel languages = new TooltipLabel(getTranslation("LANGUAGES") + ":", getTranslation("LANGUAGES"), mainTabPane);
        add(languages, 0, 0);
        languages.getStyleClass().add("bold-label"); // Add CSS class

        // TODO: spaces
        TooltipLabel common = new TooltipLabel("   " + getTranslation("COMMON_LANGUAGE"), getTranslation("COMMON_LANGUAGE"), mainTabPane);
        add(common, 0, 1);

        List<ObservableList<String>> languageValuesList = new ArrayList<>(2);
        List<TooltipComboBox> languageComboBoxes = new ArrayList<>(2);

        // In the future more languages should be available. Take the abilities ComboBoxes as an example for the available values update
        // Populate the class list and translation map
        ObservableList<String> commonLanguages = FXCollections.observableArrayList();
        String[] languageKeys = getTranslations(getStrings(new String[]{"common_languages"}));
        commonLanguages.addAll(Arrays.asList(languageKeys));
        commonLanguages.add(0, getTranslation("RANDOM"));
        languageValuesList.add(commonLanguages);
        languageValuesList.add(FXCollections.observableArrayList(commonLanguages));

        TooltipComboBox languageOne = new TooltipComboBox(languageValuesList.get(0), mainTabPane);
        languageOne.setPromptText(getTranslation("RANDOM"));
        add(languageOne, 0, 2);
        add(languageOne.getLabel(), 0, 2);
        languageOne.valueProperty().bindBidirectional(character.getLanguageOne());
        languageOne.disableProperty().bind(character.isEditing().not());

        TooltipComboBox languageTwo = new TooltipComboBox(languageValuesList.get(1), mainTabPane);
        languageTwo.setPromptText(getTranslation("RANDOM"));
        add(languageTwo, 0, 3);
        add(languageTwo.getLabel(), 0, 3);
        languageTwo.valueProperty().bindBidirectional(character.getLanguageTwo());
        languageTwo.disableProperty().bind(character.isEditing().not());

        languageComboBoxes.add(languageOne);
        languageComboBoxes.add(languageTwo);

        for (TooltipComboBox comboBox : languageComboBoxes) {
            comboBox.valueProperty().addListener((_, _, _) -> {
                for (int j = 0; j < languageComboBoxes.size(); j++) {
                    ComboBoxUtils.updateItems(languageComboBoxes.get(j), languageComboBoxes, languageValuesList.get(j), languageKeys, new String[]{getTranslation("RANDOM")});
                }
            });
        }

        TextFlow proficienciesFlow = new TextFlow();

        Runnable updateProficiencies = () -> {
            proficienciesFlow.getChildren().clear();
            proficienciesBox.getChildren().clear();
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
                Boolean hasNonArray = toolProficiencies.stream()
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
        add(proficienciesPane, 0, 4);

        proficienciesGridPane.add(proficienciesBox, 0, 1);

        TextFlow traitsFlow = new TextFlow();
        updateBox(traitsFlow, character.getTraits());

        character.getTraits().addListener((ListChangeListener<StringProperty>) _
                -> updateBox(traitsFlow, character.getTraits())
        );

        add(traitsFlow, 0, 5);

        
        proficienciesPane.maxWidthProperty().bind(paneWidthBinding);
        traitsFlow.maxWidthProperty().bind(paneWidthBinding);

        // TODO: flow grows too much
        proficienciesFlow.setMaxHeight(Double.MAX_VALUE);
        proficienciesPane.setMaxHeight(Double.MAX_VALUE);
        traitsFlow.setMaxHeight(Double.MAX_VALUE);

        // Add user notes TextArea
        TextArea notesArea = new TextArea();
        notesArea.setWrapText(true);
        notesArea.setScrollTop(0);
                
        // Bind to character notes property (you'll need to add this to ViewModel)
        notesArea.textProperty().bindBidirectional(character.getUserDescription());
        
        add(notesArea, 0, 6);
        
        notesArea.maxWidthProperty().bind(paneWidthBinding);
        notesArea.setMaxHeight(Double.MAX_VALUE);
    }

    private void updateProficienciesBox(TextFlow textFlow, List<StringProperty> properties) {
        for (TooltipComboBox comboBox : choiceComboBoxes) {
            getChildren().remove(comboBox);
            getChildren().remove(comboBox.getLabel());
        }
        choiceComboBoxes.clear();
        startingValues.clear();
        groupItemsList.clear();

        Boolean isFirstLine = true;
        for (StringProperty proficiency : properties) {
            String name = proficiency.get();
            if (Arrays.asList(choiceArrays).contains(name)) {
                String[] items = getTranslations(getStrings(new String[]{"sets", name}));
                ObservableList<String> groupItems = FXCollections.observableArrayList(items);
                groupItems.add(0, getTranslation("RANDOM"));
                TooltipComboBox comboBox = new TooltipComboBox(groupItems, mainTabPane);
                choiceComboBoxes.add(comboBox);
                startingValues.add(items);
                groupItemsList.add(groupItems);

                comboBox.disableProperty().bind(character.isEditing().not());

                int index = choiceComboBoxes.size();
                proficienciesBox.getChildren().add(comboBox);
                proficienciesBox.getChildren().add(comboBox.getLabel());

                comboBox.valueProperty().addListener((_, _, _) -> {
                    for (int j = 0; j < choiceComboBoxes.size(); j++) {
                        ComboBoxUtils.updateItems(choiceComboBoxes.get(j), choiceComboBoxes, groupItemsList.get(j), startingValues.get(j), new String[]{getTranslation("RANDOM")});
                    }
                });

                comboBox.valueProperty().addListener((_, _, newVal) -> {
                    character.getChoiceToolProficiency(index - 1).setName(newVal);
                });

                character.getChoiceToolProficiency(index - 1).getNameProperty().addListener((newVal) -> {
                    comboBox.setValue(newVal);
                });

                comboBox.setValue(character.getChoiceToolProficiency(index - 1).getName());

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
        Boolean isFirstLine = true;
        for (StringProperty property : properties) {
            if (isFirstLine) {
                isFirstLine = false;
            } else {
                textFlow.getChildren().add(new Text("\n\n"));
            }
            String name = property.get();
            String text = getMisc(getOriginal(name));
            Text wordText = new Text(name + ": ");
            wordText.setStyle("-fx-font-weight: bold;");
            textFlow.getChildren().add(wordText);
            DefinitionManager.fillTextFlow(textFlow, text, mainTabPane, "");
        }
    }

    // Helper method to get translations
    private String getMisc(String key) {
        return MiscsManager.getMisc(key);
    }

    private String getOriginal(String translated) {
        return TranslationManager.getOriginal(translated);
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }

    private String[] getStrings(String[] key) {
        return GroupManager.getInstance().getStrings(key);
    }

    private String[] getTranslations(String[] key) {
        return TranslationManager.getTranslations(key);
    }
}
