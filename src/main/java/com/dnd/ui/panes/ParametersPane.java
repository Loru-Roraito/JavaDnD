package com.dnd.ui.panes;

import java.util.HashMap;
import java.util.Map;

import com.dnd.Constants;
import com.dnd.TranslationManager;
import com.dnd.characters.GameCharacter;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ParametersPane extends GridPane {
    private final Map<String, String> gendersMap = new HashMap<>();
    private final Map<String, String> speciesMap = new HashMap<>();
    private final Map<String, String> backgroundsMap = new HashMap<>();
    private final Map<String, String> lineagesMap = new HashMap<>();
    private final Map<String, String> alignmentsMap = new HashMap<>();
    public ParametersPane(GameCharacter character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");

        // Create a label as the title for the ComboBox
        TooltipLabel nameLabel = new TooltipLabel(getTranslation("NAME"), mainTabPane);
        add(nameLabel, 0, 0); // Add the label to the GridPane

        TextField name = new TextField();
        final int maxLength = 22;
        name.textProperty().addListener((_, _, newText) -> {
            if (newText != null && newText.length() > maxLength) {
                name.setText(newText.substring(0, maxLength));
            }
        });
        add(name, 0, 1); // Add the label to the GridPane
        name.textProperty().bindBidirectional(character.getName());
        

        // Create a label as the title for the ComboBox
        TooltipLabel genderLabel = new TooltipLabel(getTranslation("GENDER"), mainTabPane);
        add(genderLabel, 0, 2); // Add the label to the GridPane
        
        // Populate the class list and translation map
        ObservableList<String> genders = FXCollections.observableArrayList();
        for (String genderKey : getGroup(new String[] {"GENDERS"})) {
            String translatedGenders = getTranslation(genderKey);
            genders.add(translatedGenders);
            gendersMap.put(translatedGenders, genderKey); // Map translated name to original key
        }
        genders.add(0, getTranslation("RANDOM"));
        gendersMap.put(getTranslation("RANDOM"), "RANDOM");

        TooltipComboBox<String> genderComboBox = new TooltipComboBox<>(genders, mainTabPane);
        genderComboBox.setPromptText(getTranslation("RANDOM"));
        add(genderComboBox, 0, 3);

        // Listen for ComboBox changes (Translated → English)
        genderComboBox.valueProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                String englishKey = gendersMap.get(newVal);
                if (englishKey != null && !englishKey.equals(character.getGender().get())) {
                    character.getGender().set(englishKey);
                }
            }
        });

        // Listen for character property changes (English → Translated)
        character.getGender().addListener((_, _, newVal) -> {
            String translated = getTranslation(newVal);
            if (translated != null && !translated.equals(genderComboBox.getValue())) {
                genderComboBox.setValue(translated);
            }
        });


        // Create a label as the title for the ComboBox
        TooltipLabel speciesLabel = new TooltipLabel(getTranslation("SPECIES"), mainTabPane);
        add(speciesLabel, 0, 4); // Add the label to the GridPane

        // Populate the class list and translation map
        ObservableList<String> species = FXCollections.observableArrayList();
        for (String classKey : getGroup(new String[] {"SPECIES"})) {
            String translatedSpecies = getTranslation(classKey);
            species.add(translatedSpecies);
            speciesMap.put(translatedSpecies, classKey); // Map translated name to original key
        }
        species.add(0, getTranslation("RANDOM"));
        speciesMap.put(getTranslation("RANDOM"), "RANDOM");

        TooltipComboBox<String> speciesComboBox = new TooltipComboBox<>(species, mainTabPane);
        speciesComboBox.setPromptText(getTranslation("RANDOM"));
        add(speciesComboBox, 0, 5);

        // Listen for ComboBox changes (Translated → English)
        speciesComboBox.valueProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                String englishKey = speciesMap.get(newVal);
                if (englishKey != null && !englishKey.equals(character.getSpecies().get())) {
                    character.getSpecies().set(englishKey);
                }
            }
        });

        // Listen for character property changes (English → Translated)
        character.getSpecies().addListener((_, _, newVal) -> {
            String translated = getTranslation(newVal);
            if (translated != null && !translated.equals(speciesComboBox.getValue())) {
                speciesComboBox.setValue(translated);
            }
        });
        

        // Create a label as the title for the second ComboBox
        TooltipLabel lineageLabel = new TooltipLabel(getTranslation("LINEAGE"), mainTabPane);
        add(lineageLabel, 0, 6); // Add the label to the GridPane

        // Create the second ComboBox (lineage selection)
        ObservableList<String> lineages = FXCollections.observableArrayList();
        TooltipComboBox<String> lineageComboBox = new TooltipComboBox<>(lineages, mainTabPane);
        lineageComboBox.setPromptText(getTranslation("RANDOM"));
        add(lineageComboBox, 0, 7);
        
        // Listen for ComboBox changes (Translated → English)
        lineageComboBox.valueProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                String englishKey = lineagesMap.get(newVal);
                if (englishKey != null && !englishKey.equals(character.getLineage().get())) {
                    character.getLineage().set(englishKey);
                }
            }
        });

        // Listen for character property changes (English → Translated)
        character.getLineage().addListener((_, _, newVal) -> {
            String translated = getTranslation(newVal);
            if (translated != null && !translated.equals(lineageComboBox.getValue())) {
                lineageComboBox.setValue(translated);
            }
        });

        lineages.add(getTranslation("RANDOM"));
        lineagesMap.put(getTranslation("RANDOM"), "RANDOM");
        lineageComboBox.setItems(lineages);

        for (StringProperty prop : character.getAvailableLineages()) {
            if (prop != null) {
                prop.addListener((_, oldVal, newVal) -> {
                    if (!oldVal.isEmpty()) {
                        String translated_lineage = getTranslation(oldVal);
                        lineages.remove(lineages.indexOf(translated_lineage));
                        lineagesMap.remove(translated_lineage);
                    }
                    if (!newVal.isEmpty()) {
                        String translated_lineage = getTranslation(newVal);
                        lineages.add(translated_lineage);
                        lineagesMap.put(translated_lineage, newVal);
                    }
                });
            }
        }
    
        // Update the lineages based on the selected species
        speciesComboBox.valueProperty().addListener((_, _, _) -> {
            // Dynamically add or remove the lineage elements
            if (lineages.isEmpty()) {
                getChildren().removeAll(lineageLabel, lineageComboBox); // Remove from GridPane
            } else {
                if (!getChildren().contains(lineageLabel)) {
                    add(lineageLabel, 0, 6); // Add back to GridPane
                }
                if (!getChildren().contains(lineageComboBox)) {
                    add(lineageComboBox, 0, 7); // Add back to GridPane
                }
            }
        });

        getChildren().removeAll(lineageLabel, lineageComboBox); // Initially remove specific elements


        // Create a label as the title for the ComboBox
        TooltipLabel backgroundLabel = new TooltipLabel(getTranslation("BACKGROUND"), mainTabPane);
        add(backgroundLabel, 0, 8); // Add the label to the GridPane
        
        // Create the second ComboBox (subclass selection)
        ObservableList<String> backgrounds = FXCollections.observableArrayList();
        for (String classKey : getGroup(new String[] {"BACKGROUNDS"})) {
            String translatedClass = getTranslation(classKey);
            backgrounds.add(translatedClass);
            backgroundsMap.put(translatedClass, classKey); // Map translated name to original key
        }
        backgrounds.add(0, getTranslation("RANDOM"));
        backgroundsMap.put(getTranslation("RANDOM"), "RANDOM");

        TooltipComboBox<String> backgroundComboBox = new TooltipComboBox<>(backgrounds, mainTabPane);
        backgroundComboBox.setPromptText(getTranslation("RANDOM"));
        add(backgroundComboBox, 0, 9);

        // Listen for ComboBox changes (Translated → English)
        backgroundComboBox.valueProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                String englishKey = backgroundsMap.get(newVal);
                if (englishKey != null && !englishKey.equals(character.getBackground().get())) {
                    character.getBackground().set(englishKey);
                }
            }
        });

        // Listen for character property changes (English → Translated)
        character.getBackground().addListener((_, _, newVal) -> {
            String translated = getTranslation(newVal);
            if (translated != null && !translated.equals(backgroundComboBox.getValue())) {
                backgroundComboBox.setValue(translated);
            }
        });


        // Create a label as the title for the ComboBox
        TooltipLabel alignmentLabel = new TooltipLabel(getTranslation("ALIGNMENT"), mainTabPane);
        add(alignmentLabel, 0, 10); // Add the label to the GridPane

        // Populate the class list and translation map
        ObservableList<String> alignments = FXCollections.observableArrayList();
        for (String alignmentKey : getGroup(new String[] {"ALIGNMENTS"})) {
            String translatedAlignments = getTranslation(alignmentKey);
            alignments.add(translatedAlignments);
            alignmentsMap.put(translatedAlignments, alignmentKey); // Map translated name to original key
        }
        alignments.add(0, getTranslation("RANDOM"));
        alignmentsMap.put(getTranslation("RANDOM"), "RANDOM");

        TooltipComboBox<String> alignmentComboBox = new TooltipComboBox<>(alignments, mainTabPane);
        alignmentComboBox.setPromptText(getTranslation("RANDOM"));
        add(alignmentComboBox, 0, 11);

        // Listen for ComboBox changes (Translated → English)
        alignmentComboBox.valueProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                String englishKey = alignmentsMap.get(newVal);
                if (englishKey != null && !englishKey.equals(character.getAlignment().get())) {
                    character.getAlignment().set(englishKey);
                }
            }
        });

        // Listen for character property changes (English → Translated)
        character.getAlignment().addListener((_, _, newVal) -> {
            String translated = getTranslation(newVal);
            if (translated != null && !translated.equals(alignmentComboBox.getValue())) {
                alignmentComboBox.setValue(translated);
            }
        });


        TooltipLabel speed = new TooltipLabel("", getTranslation("SPEED"), mainTabPane);
        speed.textProperty().bind(
            Bindings.concat(
                getTranslation("SPEED"),
                ": ",
                character.getSpeed().multiply(Constants.LENGTH_MULTIPLIER).asString(),
                getTranslation("LENGTH_UNIT")
            )
        );
        add(speed, 0, 12);


        TooltipLabel darkvision = new TooltipLabel("", getTranslation("DARKVISION"), mainTabPane);
        darkvision.textProperty().bind(
            Bindings.concat(
                getTranslation("DARKVISION"),
                ": ",
                character.getDarkvision().multiply(Constants.LENGTH_MULTIPLIER).asString(),
                getTranslation("LENGTH_UNIT")
            )
        );

        // Show/hide darkvision label based on value
        character.getDarkvision().addListener((_, _, newVal) -> {
            if (newVal != null && newVal.intValue() > 0) {
                if (!getChildren().contains(darkvision)) {
                    add(darkvision, 0, 13);
                }
            } else {
                getChildren().remove(darkvision);
            }
        });
    }   

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String[] getGroup(String[] key) {
        return TranslationManager.getInstance().getGroup(key);
    } 
}
