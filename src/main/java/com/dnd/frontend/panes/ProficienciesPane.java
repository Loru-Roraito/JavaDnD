package com.dnd.frontend.panes;

import java.util.Arrays;
import java.util.List;

import com.dnd.backend.GroupManager;
import com.dnd.frontend.ViewModel;
import com.dnd.frontend.language.DefinitionManager;
import com.dnd.frontend.language.MiscsManager;
import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.tooltip.TooltipComboBox;
import com.dnd.frontend.tooltip.TooltipLabel;
import com.dnd.frontend.tooltip.TooltipTitledPane;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ProficienciesPane extends GridPane {

    private final TabPane mainTabPane;
    private final String[] choiceArrays;
    private final List<TooltipComboBox> choiceComboBoxes;
    private final ViewModel character;
    private final VBox proficienciesBox = new VBox();

    public ProficienciesPane(ViewModel character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");

        // TODO: I want it dynamic
        DoubleBinding paneWidthBinding = mainTabPane.widthProperty().multiply(0.26);

        this.character = character;
        this.choiceComboBoxes = new java.util.ArrayList<>();
        this.mainTabPane = mainTabPane;

        choiceArrays = getStrings(new String[]{"sets"});

        TooltipLabel languages = new TooltipLabel(getTranslation("LANGUAGES") + ":", getTranslation("LANGUAGES"), mainTabPane);
        add(languages, 0, 0);
        languages.getStyleClass().add("bold-label"); // Add CSS class

        // TODO: spaces
        TooltipLabel common = new TooltipLabel("   " + getTranslation("COMMON_LANGUAGE"), getTranslation("COMMON_LANGUAGE"), mainTabPane);
        add(common, 0, 1);

        TooltipComboBox languageOne = new TooltipComboBox(character.getSelectableLanguages(), mainTabPane);
        languageOne.setPromptText(getTranslation("RANDOM"));
        add(languageOne, 0, 2);
        add(languageOne.getLabel(), 0, 2);
        languageOne.valueProperty().bindBidirectional(character.getLanguageOne());
        languageOne.disableProperty().bind(character.isEditing().not());

        TooltipComboBox languageTwo = new TooltipComboBox(character.getSelectableLanguages(), mainTabPane);
        languageTwo.setPromptText(getTranslation("RANDOM"));
        add(languageTwo, 0, 3);
        add(languageTwo.getLabel(), 0, 3);
        languageTwo.valueProperty().bindBidirectional(character.getLanguageTwo());
        languageTwo.disableProperty().bind(character.isEditing().not());

        TextFlow proficienciesFlow = new TextFlow();

        Runnable updateProficiencies = () -> {
            proficienciesFlow.getChildren().clear();
            proficienciesBox.getChildren().clear();
            updateProficienciesBox(proficienciesFlow, character.getWeaponProficiencies());

            ObservableList<String> armorProficiencies = character.getArmorProficiencies();
            if (!armorProficiencies.isEmpty()) {
                if (!proficienciesFlow.getChildren().isEmpty()) {
                    proficienciesFlow.getChildren().add(new Text("\n\n"));
                }
                updateProficienciesBox(proficienciesFlow, armorProficiencies);
            }

            ObservableList<String> toolProficiencies = character.getToolProficiencies();
            if (!toolProficiencies.isEmpty()) {
                // Check if there is at least one proficiency that is NOT a group/set name
                Boolean hasNonArray = toolProficiencies.stream()
                        .anyMatch(name -> !Arrays.asList(choiceArrays).contains(name));
                if (hasNonArray) {
                    if (!proficienciesFlow.getChildren().isEmpty()) {
                        proficienciesFlow.getChildren().add(new Text("\n\n"));
                    }
                }
                updateProficienciesBox(proficienciesFlow, toolProficiencies);
            }
        };

        character.getWeaponProficiencies().addListener((ListChangeListener<String>) _ -> {
            updateProficiencies.run();
        });
        character.getArmorProficiencies().addListener((ListChangeListener<String>) _ -> {
            updateProficiencies.run();
        });
        character.getToolProficiencies().addListener((ListChangeListener<String>) _ -> {
            updateProficiencies.run();
        });
        updateProficiencies.run();

        GridPane proficienciesGridPane = new GridPane();
        proficienciesGridPane.add(proficienciesFlow, 0, 0);
        proficienciesGridPane.setPadding(new Insets(0));

        TooltipTitledPane proficienciesPane = new TooltipTitledPane(getTranslation("PROFICIENCIES"), proficienciesGridPane);
        add(proficienciesPane, 0, 4);

        proficienciesGridPane.add(proficienciesBox, 0, 1);

        TitledPane traitsPane = new TitledPane();
        
        traitsPane.setText(getTranslation("TRAITS"));
        TextFlow traitsFlow = new TextFlow();
        updateBox(traitsFlow, character.getTraits());

        character.getTraits().addListener((ListChangeListener<String>) _
                -> updateBox(traitsFlow, character.getTraits())
        );

        traitsPane.setContent(traitsFlow);
        add(traitsPane, 0, 5);

        
        proficienciesPane.maxWidthProperty().bind(paneWidthBinding);
        traitsPane.maxWidthProperty().bind(paneWidthBinding);

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

    private void updateProficienciesBox(TextFlow textFlow, List<String> properties) {
        for (TooltipComboBox comboBox : choiceComboBoxes) {
            getChildren().remove(comboBox);
            getChildren().remove(comboBox.getLabel());
        }
        choiceComboBoxes.clear();

        Boolean isFirstLine = true;
        for (String proficiency : properties) {
            if (Arrays.asList(choiceArrays).contains(proficiency)) {
                String[] items = getTranslations(getStrings(new String[]{"sets", proficiency}));
                ObservableList<String> groupItems = FXCollections.observableArrayList(items);
                groupItems.add(0, getTranslation("RANDOM"));
                TooltipComboBox comboBox = new TooltipComboBox(groupItems, mainTabPane);
                choiceComboBoxes.add(comboBox);

                comboBox.disableProperty().bind(character.isEditing().not());

                int index = choiceComboBoxes.size();
                proficienciesBox.getChildren().add(comboBox);
                proficienciesBox.getChildren().add(comboBox.getLabel());

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
                Text wordText = new Text(proficiency);
                textFlow.getChildren().add(wordText);
            }
        }
    }

    private void updateBox(TextFlow textFlow, Iterable<String> properties) {
        textFlow.getChildren().clear();
        Boolean isFirstLine = true;
        for (String property : properties) {
            if (isFirstLine) {
                isFirstLine = false;
            } else {
                textFlow.getChildren().add(new Text("\n\n"));
            }
            String name = property;
            String text = getMisc(getOriginal(name));
            Text wordText = new Text(name + ": ");
            wordText.setStyle("-fx-font-weight: bold; -fx-font-size: 1.5em;");
            textFlow.getChildren().add(wordText);
            textFlow.getChildren().add(new Text("\n"));
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
