package com.dnd.frontend.tabs;

import java.util.Optional;

import com.dnd.frontend.language.TranslationManager;
import com.dnd.frontend.ViewModel;

import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class CharacterTab extends Tab{
    private boolean isSaved = true;
    private final TabPane mainTabPane; // Reference to the main TabPaneù
    private ViewModel character;
    private final Stage stage;
    public CharacterTab(String title, Stage stage, TabPane mainTabPane) {
        this.stage = stage;
        this.mainTabPane = mainTabPane;
        
        // Set the tab title using the translation key
        setText(TranslationManager.getTranslation(title));

        // Make the tab closable (default behavior, can be overridden)
        setClosable(false);
    }

    // Create the TabPane for sub-tabs
    public void createSubTabPane(ViewModel character) {
        this.character = character;
        TabPane subTabPane = new TabPane();
        subTabPane.setSide(Side.LEFT); // Display tabs on the left
        subTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Prevent closing sub-tabs

        InfoTab infoTab = new InfoTab(character, mainTabPane, stage);
        MagicTab magicTab = new MagicTab(character, mainTabPane, infoTab);
        StatusTab statusTab = new StatusTab(character, mainTabPane);
        ExtraTab extraTab = new ExtraTab(character, mainTabPane);

        // Add sub-tabs
        subTabPane.getTabs().addAll(infoTab, magicTab, statusTab, extraTab);

        setContent(subTabPane);
        setupCloseHandler();
    }

    public void newEdit() {
        isSaved = false;
        String text = getText();
        if (!text.equals(getTranslation("GENERATOR_WINDOW")) && !text.endsWith("*")) {
            String name = !character.getSaveName().equals("") ? character.getSaveName() : character.getName().get();
            setText(name + "*");
        }
    }

    public void setSaved() {
        isSaved = true;
        setText(character.getSaveName());
    }

    private void setupCloseHandler() {
        setOnCloseRequest(event -> {
            if (!isSaved) {
                // Show confirmation dialog
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle(getTranslation("UNSAVED_CHANGES"));
                alert.setHeaderText(getTranslation("WARNING_UNSAVED"));
                alert.setContentText(getTranslation("SAVE_BEFORE_CLOSING"));
                
                ButtonType saveButton = new ButtonType(getTranslation("SAVE"));
                ButtonType dontSaveButton = new ButtonType(getTranslation("DONT_SAVE"));
                ButtonType cancelButton = new ButtonType(getTranslation("CANCEL"), ButtonBar.ButtonData.CANCEL_CLOSE);
                
                // Apply CSS to the alert dialog
                alert.getDialogPane().getStylesheets().add(
                    getClass().getResource("/styles.css").toExternalForm()
                );

                alert.getButtonTypes().setAll(saveButton, dontSaveButton, cancelButton);
                
                Optional<ButtonType> result = alert.showAndWait();
                
                if (result.isPresent()) {
                    if (result.get() == saveButton) {
                        if (!saveCharacter()) {
                            // Cancel close if save was cancelled or failed
                            event.consume();
                        }
                    } else if (result.get() == cancelButton) {
                        // Cancel the close
                        event.consume();
                    }
                    // If "Don't Save" was clicked, let the tab close
                }
            }
        });
    }

    public boolean saveCharacter() {
        if (character != null) {
            boolean saved = character.save(false);
            if (saved) {
                isSaved = true;
                setText(character.getSaveName());
            }
            return saved;
        }
        return false;
    }

    public boolean isSaved() {
        return isSaved;
    }

    private String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }
}