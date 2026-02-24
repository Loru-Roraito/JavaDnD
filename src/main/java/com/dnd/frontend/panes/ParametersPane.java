package com.dnd.frontend.panes;

import com.dnd.backend.GroupManager;
import com.dnd.frontend.ViewModel;
import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.tooltip.TooltipComboBox;
import com.dnd.frontend.tooltip.TooltipLabel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ParametersPane extends GridPane {
    public ParametersPane(ViewModel character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");

        // Create a label as the title for the ComboBox
        TooltipLabel nameLabel = new TooltipLabel(getTranslation("NAME"), mainTabPane);
        add(nameLabel, 0, 0); // Add the label to the GridPane
        nameLabel.getStyleClass().add("bold-label"); // Add CSS class

        TextField name = new TextField();
        final int maxLength = 22;
        name.textProperty().addListener((_, _, newText) -> {
            if (newText != null && newText.length() > maxLength) {
                name.setText(newText.substring(0, maxLength));
            }
        });
        add(name, 0, 1); // Add the label to the GridPane
        name.getStyleClass().add("text-field");
        name.textProperty().bindBidirectional(character.getName());
        name.disableProperty().bind(character.isEditing().not());

        // Create a label as the title for the ComboBox
        TooltipLabel genderLabel = new TooltipLabel(getTranslation("GENDER"), mainTabPane);
        add(genderLabel, 0, 2); // Add the label to the GridPane
        genderLabel.getStyleClass().add("bold-label"); // Add CSS class
        
        // Populate the class list and translation map
        ObservableList<String> genders = FXCollections.observableArrayList();
        for (String genderKey : getStrings(new String[] {"genders"})) {
            genders.add(getTranslation(genderKey));
        }
        genders.add(0, getTranslation("RANDOM"));

        TooltipComboBox genderComboBox = new TooltipComboBox(genders, mainTabPane);
        genderComboBox.setPromptText(getTranslation("RANDOM"));
        add(genderComboBox, 0, 3);
        add(genderComboBox.getLabel(), 0, 3);
        genderComboBox.disableProperty().bind(character.isEditing().not());

        // Listen for ComboBox changes (Translated → English)
        genderComboBox.valueProperty().bindBidirectional(character.getGender());

        // Create a label as the title for the ComboBox
        TooltipLabel speciesLabel = new TooltipLabel(getTranslation("SPECIES"), mainTabPane);
        add(speciesLabel, 0, 4); // Add the label to the GridPane
        speciesLabel.getStyleClass().add("bold-label"); // Add CSS class

        // Populate the class list and translation map
        ObservableList<String> species = FXCollections.observableArrayList();
        for (String classKey : getStrings(new String[] {"species"})) {
            species.add(getTranslation(classKey));
        }
        species.add(0, getTranslation("RANDOM"));

        TooltipComboBox speciesComboBox = new TooltipComboBox(species, mainTabPane);
        speciesComboBox.setPromptText(getTranslation("RANDOM"));
        add(speciesComboBox, 0, 5);
        add(speciesComboBox.getLabel(), 0, 5);
        speciesComboBox.disableProperty().bind(character.isEditing().not());

        // Listen for ComboBox changes (Translated → English)
        speciesComboBox.valueProperty().bindBidirectional(character.getSpecies());

        // Create a label as the title for the second ComboBox
        TooltipLabel lineageLabel = new TooltipLabel(getTranslation("LINEAGE"), mainTabPane);
        add(lineageLabel, 0, 6); // Add the label to the GridPane
        lineageLabel.getStyleClass().add("bold-label"); // Add CSS class

        // Create the second ComboBox (lineage selection)
        ObservableList<String> lineages = FXCollections.observableArrayList();
        TooltipComboBox lineageComboBox = new TooltipComboBox(lineages, mainTabPane);
        lineageComboBox.setPromptText(getTranslation("RANDOM"));
        add(lineageComboBox, 0, 7);
        add(lineageComboBox.getLabel(), 0, 7);
        lineageComboBox.disableProperty().bind(character.isEditing().not());
        
        // Listen for ComboBox changes (Translated → English)
        lineageComboBox.valueProperty().bindBidirectional(character.getLineage());

        lineages.add(getTranslation("RANDOM"));

        for (StringProperty prop : character.getSelectableLineages()) {
            if (prop != null) {
                prop.addListener((_, oldVal, newVal) -> {
                    if (!oldVal.isEmpty()) {
                        lineages.remove(oldVal);
                    }
                    if (!newVal.isEmpty()) {
                        lineages.add(newVal);
                    }
                });
            }
        }
    
        // Update the lineages based on the selected species
        Runnable updateLineages = () -> {
            if (lineages.size() <= 1) {
                getChildren().removeAll(lineageLabel, lineageComboBox, lineageComboBox.getLabel()); // Remove from GridPane
            } else {
                if (!getChildren().contains(lineageLabel)) {
                    add(lineageLabel, 0, 6); // Add back to GridPane
                }
                if (!getChildren().contains(lineageComboBox)) {
                    add(lineageComboBox, 0, 7); // Add back to GridPane
                    add(lineageComboBox.getLabel(), 0, 7); // Add back to GridPane
                }
            }
        };
        speciesComboBox.valueProperty().addListener((_, _, _) -> {
            updateLineages.run();
        });
        updateLineages.run(); // Initial population

        getChildren().removeAll(lineageLabel, lineageComboBox); // Initially remove specific elements

        // Create a label as the title for the ComboBox
        TooltipLabel backgroundLabel = new TooltipLabel(getTranslation("BACKGROUND"), mainTabPane);
        add(backgroundLabel, 0, 8); // Add the label to the GridPane
        backgroundLabel.getStyleClass().add("bold-label"); // Add CSS class
        
        // Create the second ComboBox (subclass selection)
        ObservableList<String> backgrounds = FXCollections.observableArrayList();
        for (String classKey : getStrings(new String[] {"backgrounds"})) {
            backgrounds.add(getTranslation(classKey));
        }
        backgrounds.add(0, getTranslation("RANDOM"));

        TooltipComboBox backgroundComboBox = new TooltipComboBox(backgrounds, mainTabPane);
        backgroundComboBox.setPromptText(getTranslation("RANDOM"));
        add(backgroundComboBox, 0, 9);
        add(backgroundComboBox.getLabel(), 0, 9);
        backgroundComboBox.disableProperty().bind(character.isEditing().not());

        // Listen for ComboBox changes (Translated → English)
        backgroundComboBox.valueProperty().bindBidirectional(character.getBackground());

        // Create a label as the title for the ComboBox
        TooltipLabel alignmentLabel = new TooltipLabel(getTranslation("ALIGNMENT"), mainTabPane);
        add(alignmentLabel, 0, 10); // Add the label to the GridPane
        alignmentLabel.getStyleClass().add("bold-label"); // Add CSS class

        // Populate the class list and translation map
        ObservableList<String> alignments = FXCollections.observableArrayList();
        for (String alignmentKey : getStrings(new String[] {"alignments"})) {
            alignments.add(getTranslation(alignmentKey));
        }
        alignments.add(0, getTranslation("RANDOM"));

        TooltipComboBox alignmentComboBox = new TooltipComboBox(alignments, mainTabPane);
        alignmentComboBox.setPromptText(getTranslation("RANDOM"));

        // Listen for ComboBox changes (Translated → English)
        alignmentComboBox.valueProperty().bindBidirectional(character.getAlignment());
        add(alignmentComboBox, 0, 11);
        add(alignmentComboBox.getLabel(), 0, 11);
        alignmentComboBox.disableProperty().bind(character.isEditing().not());


        ObservableList<String> sizes = FXCollections.observableArrayList();
        sizes.add(getTranslation("RANDOM"));

        for (StringProperty prop : character.getSelectableSizes()) {
            if (prop != null) {
                prop.addListener((_, oldVal, newVal) -> {
                    if (!oldVal.isEmpty()) {
                        sizes.remove(oldVal);
                    }
                    if (!newVal.isEmpty()) {
                        sizes.add(newVal);
                    }
                });
            }
        }

        TooltipComboBox  sizeComboBox = new TooltipComboBox(sizes, mainTabPane);
        sizeComboBox.valueProperty().bindBidirectional(character.getSize());
        sizeComboBox.disableProperty().bind(character.isEditing().not());

        Text sizeBoldPart = new Text(getTranslation("SIZE"));
        sizeBoldPart.getStyleClass().add("bold-text");

        Text sizeValuePart = new Text();
        sizeValuePart.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    StringProperty[] selectableSizes = character.getSelectableSizes();
                    return (selectableSizes[1].get().equals(""))
                        ? ": " + selectableSizes[0].get()
                        : "";
                },
                character.getSpecies()
            )
        );

        TextFlow sizeTextFlow = new TextFlow(sizeBoldPart, sizeValuePart);

        Runnable updateSize = () -> {
            if (character.getSelectableSizes()[0].get().equals("")) {
                getChildren().removeAll(sizeTextFlow, sizeComboBox, sizeComboBox.getLabel());
            } else if (character.getSelectableSizes()[1].get().equals("")) {
                getChildren().removeAll(sizeComboBox, sizeComboBox.getLabel());
                if (!getChildren().contains(sizeTextFlow)) {
                    add(sizeTextFlow, 0, 12);
                }
            } else {
                if (!getChildren().contains(sizeTextFlow)) {
                    add(sizeTextFlow, 0, 12);
                }
                if (!getChildren().contains(sizeComboBox)) {
                    add(sizeComboBox, 0, 13);
                    add(sizeComboBox.getLabel(), 0, 13);
                }
            }
        };
        character.getSpecies().addListener((_, _, _) -> {
            updateSize.run();
        });
        updateSize.run();

        TooltipLabel speed = new TooltipLabel("", getTranslation("SPEED"), mainTabPane);
        speed.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    double value = character.getSpeed().get();
                    String formatted = (value == Math.floor(value))
                        ? String.format("%.0f", value)
                        : String.format("%.1f", value);
                    return getTranslation("SPEED") + ": " + formatted + getTranslation("LENGTH_UNIT");
                },
                character.getSpeed()
            )
        );
        add(speed, 0, 14);

        TooltipLabel darkvision = new TooltipLabel("", getTranslation("DARKVISION"), mainTabPane);
        darkvision.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    double value = character.getDarkvision().get();
                    String formatted = (value == Math.floor(value))
                        ? String.format("%.0f", value)
                        : String.format("%.1f", value);
                    return getTranslation("DARKVISION") + ": " + formatted + getTranslation("LENGTH_UNIT");
                },
                character.getDarkvision()
            )
        );

        // Show/hide darkvision label based on value
        character.getDarkvision().addListener((_, _, newVal) -> {
            if (newVal != null && newVal.intValue() > 0) {
                if (!getChildren().contains(darkvision)) {
                    add(darkvision, 0, 15);
                }
            } else {
                getChildren().remove(darkvision);
            }
        });
    }   

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }

    private String[] getStrings(String[] key) {
        return GroupManager.getInstance().getStrings(key);
    } 
}
