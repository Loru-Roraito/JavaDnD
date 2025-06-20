package com.dnd.ui.panes;

import com.dnd.DefinitionManager;
import com.dnd.MiscsManager;
import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.ui.tooltip.TooltipTitledPane;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ProficienciesPane extends GridPane {
    private final TabPane mainTabPane;
    public ProficienciesPane(ViewModel character, GridPane gridPane, TabPane mainTabPane) {
        this.mainTabPane = mainTabPane;
        getStyleClass().add("grid-pane");

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

        TooltipComboBox<String> languageOne = new TooltipComboBox<>(FXCollections.observableArrayList(commonLanguages), mainTabPane);
        languageOne.setPromptText(getTranslation("RANDOM"));
        add(languageOne, 0, 2);
        languageOne.valueProperty().bindBidirectional(character.getLanguageOne());

        TooltipComboBox<String> languageTwo = new TooltipComboBox<>(FXCollections.observableArrayList(commonLanguages), mainTabPane);
        languageTwo.setPromptText(getTranslation("RANDOM"));
        add(languageTwo, 0, 3);
        languageTwo.valueProperty().bindBidirectional(character.getLanguageTwo());

        languageOne.valueProperty().addListener((_, _, newVal) -> {
            updateComboBox(languageTwo, commonLanguages, newVal);
        });

        languageTwo.valueProperty().addListener((_, _, newVal) -> {
            updateComboBox(languageOne, commonLanguages, newVal);
        });


        TextFlow activesFlow = new TextFlow();
        activesFlow.maxWidthProperty().bind(gridPane.widthProperty().multiply(0.2));

        // Initial population
        updateBox(activesFlow, character.getActives());

        // Listen for changes in the ObservableList and update the VBox accordingly
        character.getActives().addListener((javafx.collections.ListChangeListener<StringProperty>) _ -> {
            updateBox(activesFlow, character.getActives());
        });

        TooltipTitledPane activesPane = new TooltipTitledPane(getTranslation("ACTIVE_ABILITIES"), activesFlow);
        add(activesPane, 0, 4);


        TextFlow passivesFlow = new TextFlow();
        passivesFlow.maxWidthProperty().bind(gridPane.widthProperty().multiply(0.2));

        // Initial population
        updateBox(passivesFlow, character.getPassives());

        // Listen for changes in the ObservableList and update the VBox accordingly
        character.getPassives().addListener((javafx.collections.ListChangeListener<StringProperty>) _ -> {
            updateBox(passivesFlow, character.getPassives());
        });

        TooltipTitledPane passivesPane = new TooltipTitledPane(getTranslation("PASSIVE_ABILITIES"), passivesFlow);

        add(passivesPane, 0, 5);
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
}