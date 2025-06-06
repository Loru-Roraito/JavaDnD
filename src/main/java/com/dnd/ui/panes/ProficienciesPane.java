package com.dnd.ui.panes;

import com.dnd.DefinitionManager;
import com.dnd.MiscsManager;
import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.tooltip.TooltipTitledPane;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ProficienciesPane extends GridPane {
    private final TabPane mainTabPane;
    public ProficienciesPane(ViewModel character, GridPane gridPane, TabPane mainTabPane) {
        this.mainTabPane = mainTabPane;
        getStyleClass().add("grid-pane");

        TextFlow activesFlow = new TextFlow();
        activesFlow.maxWidthProperty().bind(gridPane.widthProperty().multiply(0.2));


        // Initial population
        updateBox(activesFlow, character.getActives());

        // Listen for changes in the ObservableList and update the VBox accordingly
        character.getActives().addListener((javafx.collections.ListChangeListener<StringProperty>) _ -> {
            updateBox(activesFlow, character.getActives());
        });

        TooltipTitledPane activesPane = new TooltipTitledPane(getTranslation("ACTIVE_ABILITIES"), activesFlow);
        add(activesPane, 0, 0);


        TextFlow passivesFlow = new TextFlow();
        passivesFlow.maxWidthProperty().bind(gridPane.widthProperty().multiply(0.2));

        // Initial population
        updateBox(passivesFlow, character.getPassives());

        // Listen for changes in the ObservableList and update the VBox accordingly
        character.getPassives().addListener((javafx.collections.ListChangeListener<StringProperty>) _ -> {
            updateBox(passivesFlow, character.getPassives());
        });

        TooltipTitledPane passivesPane = new TooltipTitledPane(getTranslation("PASSIVE_ABILITIES"), passivesFlow);

        add(passivesPane, 0, 1);
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
}