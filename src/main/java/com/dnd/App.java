package com.dnd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dnd.characters.GameCharacter;
import com.dnd.ui.tabs.CharacterTab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static TabPane mainTabPane;

    @Override
    public void start(Stage stage) throws IOException {
        TranslationManager.language = "it"; // Change the language (relevant files need to be present in resources)
        BorderPane root = new BorderPane();

        // Initialize the TabPane
        initializeTabPane(stage);

        // Set the TabPane as the center of the layout
        root.setCenter(mainTabPane);

        // Initialize and set the scene
        initializeScene(stage, root);
        
        // Set up window close handler
        stage.setOnCloseRequest(event -> {
            if (!checkUnsavedChanges(mainTabPane)) {
                event.consume(); // Cancel the close if user cancels or save fails
            }
        });

        stage.show();
    }
    
    private boolean checkUnsavedChanges(TabPane mainTabPane) {
        // Find all CharacterTab instances with unsaved changes
        List<CharacterTab> unsavedTabs = new ArrayList<>();
        
        for (Tab tab : mainTabPane.getTabs()) {
            if (tab instanceof CharacterTab characterTab && characterTab.isClosable()) {
                if (!characterTab.isSaved()) {
                    unsavedTabs.add(characterTab);
                }
            }
        }
        
        if (unsavedTabs.isEmpty()) {
            return true; // No unsaved changes, allow close
        }
        
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(getTranslation("UNSAVED_CHANGES"));
        alert.setHeaderText(getTranslation("WARNING_MULTIPLE_UNSAVED"));
        alert.setContentText(getTranslation("SAVE_ALL_BEFORE_CLOSING"));
        
        ButtonType saveAllButton = new ButtonType(getTranslation("SAVE_ALL"));
        ButtonType dontSaveButton = new ButtonType(getTranslation("DONT_SAVE"));
        ButtonType cancelButton = new ButtonType(getTranslation("CANCEL"), ButtonBar.ButtonData.CANCEL_CLOSE);
        
        // Apply CSS to the alert dialog
        alert.getDialogPane().getStylesheets().add(
            getClass().getResource("/styles.css").toExternalForm()
        );
        
        alert.getButtonTypes().setAll(saveAllButton, dontSaveButton, cancelButton);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent()) {
            if (result.get() == saveAllButton) {
                // Try to save all unsaved tabs
                for (CharacterTab characterTab : unsavedTabs) {
                    boolean saved = characterTab.saveCharacter();
                    if (!saved) {
                        // If any save fails or is cancelled, cancel the close
                        return false;
                    }
                }
                return true; // All saves succeeded
            } else if (result.get() == dontSaveButton) {
                return true; // User chose not to save, allow close
            }
            return false; // User cancelled
        }
        
        return false; // Dialog was closed without selection
    }

    private void initializeTabPane(Stage stage) {
        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS); // Enable closable tabs

        // Add the generator tab
        CharacterTab generatorTab = new CharacterTab("GENERATOR_WINDOW", stage, mainTabPane);
        ViewModel character = new ViewModel(new GameCharacter(), stage, generatorTab);
        generatorTab.createSubTabPane(character);
        mainTabPane.getTabs().add(generatorTab);
    }

    private void initializeScene(Stage stage, BorderPane root) {
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle(TranslationManager.getInstance().getTranslation("MAIN_TITLE"));
        stage.setScene(scene);

        // Get screen dimensions
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();

        stage.setMaximized(true);
        
        // Set maximum size to screen size
        stage.setMaxWidth(bounds.getWidth());
        stage.setMaxHeight(bounds.getHeight());
    }

    public static void main(String[] args) {
        launch();
    }

    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }
}