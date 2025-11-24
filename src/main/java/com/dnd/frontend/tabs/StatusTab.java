package com.dnd.frontend.tabs;

import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.ViewModel;
import com.dnd.frontend.tooltip.TooltipLabel;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class StatusTab extends Tab{
    public StatusTab(ViewModel character, TabPane mainTabPane){
        setText(getTranslation("STATUS"));
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid-pane");

        TooltipLabel blinded = new TooltipLabel(getTranslation("BLINDED"), mainTabPane);
        CheckBox blindedCheckBox = new CheckBox();
        blindedCheckBox.selectedProperty().bindBidirectional(character.getBlinded());
        gridPane.add(blinded, 0, 0);
        gridPane.add(blindedCheckBox, 1, 0);

        TooltipLabel charmed = new TooltipLabel(getTranslation("CHARMED"), mainTabPane);
        CheckBox charmedCheckBox = new CheckBox();
        charmedCheckBox.selectedProperty().bindBidirectional(character.getCharmed());
        gridPane.add(charmed, 0, 1);
        gridPane.add(charmedCheckBox, 1, 1);

        TooltipLabel deafened = new TooltipLabel(getTranslation("DEAFENED"), mainTabPane);
        CheckBox deafenedCheckBox = new CheckBox();
        deafenedCheckBox.selectedProperty().bindBidirectional(character.getDeafened());
        gridPane.add(deafened, 0, 2);
        gridPane.add(deafenedCheckBox, 1, 2);

        TooltipLabel frightened = new TooltipLabel(getTranslation("FRIGHTENED"), mainTabPane);
        CheckBox frightenedCheckBox = new CheckBox();
        frightenedCheckBox.selectedProperty().bindBidirectional(character.getFrightened());
        gridPane.add(frightened, 0, 3);
        gridPane.add(frightenedCheckBox, 1, 3);

        TooltipLabel grappled = new TooltipLabel(getTranslation("GRAPPLED"), mainTabPane);
        CheckBox grappledCheckBox = new CheckBox();
        grappledCheckBox.selectedProperty().bindBidirectional(character.getGrappled());
        gridPane.add(grappled, 0, 4);
        gridPane.add(grappledCheckBox, 1, 4);

        TooltipLabel incapacitated = new TooltipLabel(getTranslation("INCAPACITATED"), mainTabPane);
        CheckBox incapacitatedCheckBox = new CheckBox();
        incapacitatedCheckBox.selectedProperty().addListener((_, _, newVal) -> {
            if (!character.getIncapacitation().get()) {
                character.getIncapacitated().set(newVal);
            }
        });
        Runnable updateIncapacitated = () -> {
            if (character.getIncapacitation().get() || character.getIncapacitated().get()) {
                incapacitatedCheckBox.setSelected(true);
            } else {
                incapacitatedCheckBox.setSelected(false);
            }
        };
        character.getIncapacitation().addListener((_, _, _) -> updateIncapacitated.run());
        character.getIncapacitated().addListener((_, _, _) -> updateIncapacitated.run());
        incapacitatedCheckBox.disableProperty().bind(character.getIncapacitation());
        gridPane.add(incapacitated, 0, 5);
        gridPane.add(incapacitatedCheckBox, 1, 5);

        TooltipLabel invisible = new TooltipLabel(getTranslation("INVISIBLE"), mainTabPane);
        CheckBox invisibleCheckBox = new CheckBox();
        invisibleCheckBox.selectedProperty().bindBidirectional(character.getInvisible());
        gridPane.add(invisible, 0, 6);
        gridPane.add(invisibleCheckBox, 1, 6);

        TooltipLabel paralyzed = new TooltipLabel(getTranslation("PARALYZED"), mainTabPane);
        CheckBox paralyzedCheckBox = new CheckBox();
        paralyzedCheckBox.selectedProperty().bindBidirectional(character.getParalyzed());
        gridPane.add(paralyzed, 0, 7);
        gridPane.add(paralyzedCheckBox, 1, 7);

        TooltipLabel petrified = new TooltipLabel(getTranslation("PETRIFIED"), mainTabPane);
        CheckBox petrifiedCheckBox = new CheckBox();
        petrifiedCheckBox.selectedProperty().bindBidirectional(character.getPetrified());
        gridPane.add(petrified, 0, 8);
        gridPane.add(petrifiedCheckBox, 1, 8);

        TooltipLabel poisoned = new TooltipLabel(getTranslation("POISONED"), mainTabPane);
        CheckBox poisonedCheckBox = new CheckBox();
        poisonedCheckBox.selectedProperty().bindBidirectional(character.getPoisoned());
        gridPane.add(poisoned, 0, 9);
        gridPane.add(poisonedCheckBox, 1, 9);

        TooltipLabel prone = new TooltipLabel(getTranslation("PRONE"), mainTabPane);
        CheckBox proneCheckBox = new CheckBox();
        proneCheckBox.selectedProperty().bindBidirectional(character.getProne());
        proneCheckBox.disableProperty().bind(character.getProneness());
        gridPane.add(prone, 0, 10);
        gridPane.add(proneCheckBox, 1, 10);

        TooltipLabel restrained = new TooltipLabel(getTranslation("RESTRAINED"), mainTabPane);
        CheckBox restrainedCheckBox = new CheckBox();
        restrainedCheckBox.selectedProperty().bindBidirectional(character.getRestrained());
        gridPane.add(restrained, 0, 11);
        gridPane.add(restrainedCheckBox, 1, 11);

        TooltipLabel stunned = new TooltipLabel(getTranslation("STUNNED"), mainTabPane);
        CheckBox stunnedCheckBox = new CheckBox();
        stunnedCheckBox.selectedProperty().bindBidirectional(character.getStunned());
        gridPane.add(stunned, 0, 12);
        gridPane.add(stunnedCheckBox, 1, 12);

        TooltipLabel unconscious = new TooltipLabel(getTranslation("UNCONSCIOUS"), mainTabPane);
        CheckBox unconsciousCheckBox = new CheckBox();
        unconsciousCheckBox.selectedProperty().bindBidirectional(character.getUnconscious());
        gridPane.add(unconscious, 0, 13);
        gridPane.add(unconsciousCheckBox, 1, 13);

        TooltipLabel exhaustion = new TooltipLabel(getTranslation("EXHAUSTION"), mainTabPane);
        ComboBox<Integer> exhaustionCheckBox = new ComboBox<>();
        exhaustionCheckBox.getItems().addAll(0, 1, 2, 3, 4, 5, 6);
        exhaustionCheckBox.valueProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                character.getExhaustion().set(newVal);
            }
        });
        character.getExhaustion().addListener(newVal -> {
            exhaustionCheckBox.setValue(newVal);
        });
        exhaustionCheckBox.setValue(character.getExhaustion().get());
        gridPane.add(exhaustion, 0, 14);
        gridPane.add(exhaustionCheckBox, 1, 14);

        setContent(gridPane);
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }
}