package com.dnd.ui.tabs;

import java.util.ArrayList;
import java.util.List;

import com.dnd.ThrowManager;
import com.dnd.TranslationManager;
import com.dnd.ViewModel;
import com.dnd.ui.panes.AbilitiesPane;
import com.dnd.ui.panes.ClassPane;
import com.dnd.ui.panes.CombatPane;
import com.dnd.ui.panes.SystemPane;
import com.dnd.ui.panes.EquipmentPane;
import com.dnd.ui.panes.HealthPane;
import com.dnd.ui.panes.ParametersPane;
import com.dnd.ui.panes.ProficienciesPane;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipTitledPane;
import com.dnd.utils.DieToast;

import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class InfoTab extends Tab {
    // Create a GridPane
    private final GridPane gridPane = new GridPane(); // Class-level field for the GridPane
    private final SystemPane systemPane;
    private final ViewModel character;
    private final Stage stage;

    public InfoTab(ViewModel character, TabPane mainTabPane, Stage stage) {
        this.character = character;
        this.stage = stage;
        setText(getTranslation("INFO"));

        // Add a style class to the GridPane
        gridPane.getStyleClass().add("grid-pane");

        // Add subareas to the grid
        AbilitiesPane abilitiesPane = new AbilitiesPane(character, mainTabPane, this);
        addTitledPane("ABILITIES_AND_SKILLS", abilitiesPane, 0, 0, 4, 1);
        HealthPane healthPane = new HealthPane(character, mainTabPane, this);
        addTitledPane("HEALTH", healthPane, 0, 1, 1, 1);
        addTitledPane("PARAMETERS", new ParametersPane(character, mainTabPane), 0, 2, 2, 1);
        EquipmentPane equipmentPane = new EquipmentPane(character, mainTabPane);
        addTitledPane("EQUIPMENT", equipmentPane , 3, 3, 2, 2);
        addClass(character, mainTabPane, 2, 1, 3, 2);
        systemPane = new SystemPane(mainTabPane, abilitiesPane, healthPane, character, stage);
        addTitledPane("SYSTEM", systemPane, 4, 0, 1, 1);
        addTitledPane("PROFICIENCIES_AND_FEATURES", new ProficienciesPane(character, mainTabPane), 0, 3, 3, 1);
        addTitledPane("COMBAT", new CombatPane(mainTabPane, character), 1, 1, 1, 1);

        // Set the GridPane as the content of the tab
        setContent(gridPane);
    }

    private void addTitledPane(String title, GridPane pane, int row, int column, int rowSpan, int columnSpan) {
        // Wrap the pane in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);

        TooltipTitledPane titledPane = new TooltipTitledPane(getTranslation(title), scrollPane);
        GridPane.setVgrow(titledPane, Priority.ALWAYS);
        GridPane.setHgrow(titledPane, Priority.ALWAYS);
        titledPane.setCollapsible(false);
        titledPane.setCursor(Cursor.DEFAULT);
        gridPane.add(titledPane, column, row);
        GridPane.setRowSpan(titledPane, rowSpan);
        GridPane.setColumnSpan(titledPane, columnSpan);
    }
    
    private void addClass(ViewModel character, TabPane mainTabPane, int row, int column, int rowSpan, int columnSpan) {
        int maxClasses = character.getMaxClasses();
        Tab[] tabs = new Tab[maxClasses - 1];
        List<TooltipComboBox> classes = new ArrayList<>(maxClasses);
        List<ObservableList<String>> classesValues = new ArrayList<>(maxClasses);

        ClassPane pane = new ClassPane(character, mainTabPane, 0, classes, classesValues);
        // Wrap the pane in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        TabPane tabPane = new TabPane();
        Tab tab = new Tab();
        tab.setClosable(false);
        tab.textProperty().bind(character.getClasse(0));
        tab.setContent(scrollPane);
        tabPane.getTabs().add(tab);

        Tab addTab = new Tab();
        addTab.setText("+");
        addTab.setClosable(false);

        character.isEditing().addListener((_, _, isEditing) -> {
            if (isEditing && !tabPane.getTabs().contains(addTab)) {
                tabPane.getTabs().add(addTab);
            } else if (!isEditing && tabPane.getTabs().contains(addTab)) {
                tabPane.getTabs().remove(addTab);
            }
        });

        if (character.isEditing().get()) {
            tabPane.getTabs().add(addTab);
        }

        classes.get(0).valueProperty().addListener((_, _, newVal) -> {
            if (newVal.equals(getTranslation("NONE_F"))) {
                int size = tabPane.getTabs().size();
                for (int i = 1; i < maxClasses; i++) {
                    character.getClasse(i).set(getTranslation("NONE_F"));
                }
                if (size + 1 == character.getMaxClasses() && !tabPane.getTabs().contains(addTab) && character.isEditing().get()) {
                    tabPane.getTabs().add(addTab);
                }
            }
        });

        for (int i = 1; i < maxClasses; i++) {
            int index = i;
            ClassPane newPane = new ClassPane(character, mainTabPane, index, classes, classesValues);
            // Wrap the pane in a ScrollPane
            ScrollPane newScrollPane = new ScrollPane(newPane);
            newScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            newScrollPane.setFitToWidth(true);
            newScrollPane.setFitToHeight(true);

            Tab newTab = new Tab();
            newTab.closableProperty().bind(character.isEditing());
            newTab.textProperty().bind(character.getClasse(index));
            newTab.setContent(newScrollPane);

            tabs[index - 1] = newTab;

            classes.get(index).valueProperty().addListener((_, _, newVal) -> {
                if (newVal.equals(getTranslation("NONE_F"))) {
                    tabPane.getTabs().remove(newTab);
                    int size = tabPane.getTabs().size();
                    if (size + 1 == character.getMaxClasses() && !tabPane.getTabs().contains(addTab) && character.isEditing().get()) {
                        tabPane.getTabs().add(addTab);
                    }
                }
            });

            newTab.setOnClosed(_ -> {
                character.getClasse(index).set(getTranslation("NONE_F"));
                int size = tabPane.getTabs().size();
                if (size + 1 == character.getMaxClasses() && !tabPane.getTabs().contains(addTab) && character.isEditing().get()) {
                    tabPane.getTabs().add(addTab);
                }
            });
        }

        GridPane.setVgrow(tabPane, Priority.ALWAYS);
        GridPane.setHgrow(tabPane, Priority.ALWAYS);
        gridPane.add(tabPane, column, row);
        GridPane.setRowSpan(tabPane, rowSpan);
        GridPane.setColumnSpan(tabPane, columnSpan);

        tabPane.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            int requiredLevels = 0;
            for (StringProperty classe : character.getClasses()) {
                if (classe.get().equals(getTranslation("RANDOM"))) {
                    requiredLevels ++;
                }
            }
            if (newVal.equals(addTab) && character.getTotalLevel().get() + requiredLevels < 20) {
                if (character.getClasse(0).get().equals(getTranslation("NONE_F"))) {
                    character.getClasse(0).set(getTranslation("RANDOM"));
                }
                int  i;
                for (i = 0; i < tabs.length; i++) {
                    Tab t = tabs[i];
                    if (!tabPane.getTabs().contains(t)) {
                        tabPane.getTabs().add(i + 1, t);
                        if (character.getClasse(i + 1).get().equals(getTranslation("NONE_F"))) {
                            character.getClasse(i + 1).set(getTranslation("RANDOM"));
                        }
                        break;
                    }
                }
                int size = tabPane.getTabs().size();

                if (size - 1 == maxClasses) {
                    tabPane.getTabs().remove(size - 1);
                }

                tabPane.getSelectionModel().select(i + 1);
            } else if (newVal.equals(addTab)) {
                tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);
            }
            // TODO: eventualmente nascondere addTab quando inutilizzabile
        });

        for (int i = 0; i < tabs.length; i++) {
            Tab t = tabs[i];
            if (!tabPane.getTabs().contains(t) && !character.getClasse(i + 1).get().equals(getTranslation("NONE_F"))) {
                tabPane.getTabs().add(Math.min(i + 1, tabPane.getTabs().size()), t);
            }
        }
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    public void throwDie(int times, int size, int base, int bonus, Boolean advantage, Boolean disadvantage, Boolean failState, int ability) {
        throwDie(times, size, base, bonus, advantage, disadvantage, ability);
    }

    // Method to update the die result
    public void throwDie(int times, int size, int base, int bonus, Boolean advantage, Boolean disadvantage, int ability) {
        int result = ThrowManager.getInstance().throwDice(times, size, base, bonus, advantage, disadvantage, ability, character.getBackend(), systemPane);
        DieToast.show(String.valueOf(result), stage, 2000);
    }
}
