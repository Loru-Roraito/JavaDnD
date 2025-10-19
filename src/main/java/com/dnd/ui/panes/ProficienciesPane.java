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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.collections.ListChangeListener;

public class ProficienciesPane extends GridPane {

    private final TabPane mainTabPane;
    private final String[] choiceArrays;
    private final List<ComboBox<String>> choiceComboBoxes;
    private final List<String[]> startingValues;
    private final List<ObservableList<String>> groupItemsList;
    private final ViewModel character;

    public ProficienciesPane(ViewModel character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");

        this.character = character;
        this.choiceComboBoxes = new java.util.ArrayList<>();
        this.startingValues = new ArrayList<>();
        this.groupItemsList = new ArrayList<>();
        this.mainTabPane = mainTabPane;

        choiceArrays = getGroup(new String[]{"sets"});

        TooltipLabel languages = new TooltipLabel(getTranslation("LANGUAGES") + ":", getTranslation("LANGUAGES"), mainTabPane);
        add(languages, 0, 0);

        TooltipLabel common = new TooltipLabel(getTranslation("COMMON_LANGUAGE"), mainTabPane);
        add(common, 0, 1);

        List<ObservableList<String>> languageValuesList = new ArrayList<>(2);
        List<ComboBox<String>> languageComboBoxes = new ArrayList<>(2);

        // In the future more languages should be available. Take the abilities ComboBoxes as an example for the available values update
        // Populate the class list and translation map
        ObservableList<String> commonLanguages = FXCollections.observableArrayList();
        String[] languageKeys = getGroupTranslations(new String[]{"common_languages"});
        commonLanguages.addAll(Arrays.asList(languageKeys));
        commonLanguages.add(0, getTranslation("RANDOM"));
        languageValuesList.add(commonLanguages);
        languageValuesList.add(FXCollections.observableArrayList(commonLanguages));

        TooltipComboBox languageOne = new TooltipComboBox(languageValuesList.get(0), mainTabPane);
        languageOne.setPromptText(getTranslation("RANDOM"));
        add(languageOne, 0, 2);
        languageOne.valueProperty().bindBidirectional(character.getLanguageOne());

        TooltipComboBox languageTwo = new TooltipComboBox(languageValuesList.get(1), mainTabPane);
        languageTwo.setPromptText(getTranslation("RANDOM"));
        add(languageTwo, 0, 3);

        languageTwo.valueProperty().bindBidirectional(character.getLanguageTwo());

        languageComboBoxes.add(languageOne);
        languageComboBoxes.add(languageTwo);

        for (ComboBox<String> comboBox : languageComboBoxes) {
            comboBox.valueProperty().addListener((_, _, _) -> {
                for (int j = 0; j < languageComboBoxes.size(); j++) {
                    ComboBoxUtils.updateItems(languageComboBoxes.get(j), languageComboBoxes, languageValuesList.get(j), languageKeys, new String[]{getTranslation("RANDOM")});
                }
            });
        }

        TextFlow proficienciesFlow = new TextFlow();

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

        GridPane.setVgrow(proficienciesFlow, Priority.ALWAYS);

        ScrollPane proficienciesScroll = new ScrollPane(proficienciesGridPane);
        proficienciesScroll.setFitToWidth(true);
        proficienciesScroll.setFitToHeight(true);
        proficienciesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        proficienciesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        TooltipTitledPane proficienciesPane = new TooltipTitledPane(getTranslation("PROFICIENCIES"), proficienciesScroll);
        proficienciesPane.setPrefWidth(200); // TODO: relative to the window size
        add(proficienciesPane, 0, 4);

        TextFlow activesFlow = new TextFlow();
        updateBox(activesFlow, character.getActives());

        character.getActives().addListener((ListChangeListener<StringProperty>) _
                -> updateBox(activesFlow, character.getActives())
        );

        activesFlow.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(activesFlow, Priority.ALWAYS);

        ScrollPane activesScroll = new ScrollPane(activesFlow);
        activesScroll.setFitToWidth(true);
        activesScroll.setFitToHeight(true);
        activesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        activesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        TooltipTitledPane activesPane = new TooltipTitledPane(getTranslation("ACTIVE_ABILITIES"), activesScroll);
        activesPane.setPrefWidth(200);

        add(activesPane, 0, 5);

        TextFlow passivesFlow = new TextFlow();
        updateBox(passivesFlow, character.getPassives());

        character.getPassives().addListener((ListChangeListener<StringProperty>) _ -> {
            updateBox(passivesFlow, character.getPassives());
        });

        passivesFlow.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(passivesFlow, Priority.ALWAYS);

        ScrollPane passivesScroll = new ScrollPane(passivesFlow);
        passivesScroll.setFitToWidth(true);
        passivesScroll.setFitToHeight(true);
        passivesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        passivesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        TooltipTitledPane passivesPane = new TooltipTitledPane(getTranslation("PASSIVE_ABILITIES"), passivesScroll);
        passivesPane.setPrefWidth(200); // TODO: relative to the window size

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
                String[] items = getGroupTranslations(new String[]{"sets", name});
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
                        ComboBoxUtils.updateItems(choiceComboBoxes.get(j), choiceComboBoxes, groupItemsList.get(j), startingValues.get(j), new String[]{getTranslation("RANDOM")});
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
            String text = fetchMisc(getOriginal(name));
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

    private String getOriginal(String translated) {
        return TranslationManager.getInstance().getOriginal(translated);
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
