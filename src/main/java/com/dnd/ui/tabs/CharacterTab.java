package com.dnd.ui.tabs;

import com.dnd.TranslationManager;
import com.dnd.characters.GameCharacter;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.geometry.Side;

public class CharacterTab extends Tab{
    private final GameCharacter character;
    private final TabPane mainTabPane; // Reference to the main TabPane
    public CharacterTab(String title, GameCharacter character, TabPane mainTabPane) {
        this.character = character;
        this.mainTabPane = mainTabPane;
        
        // Set the tab title using the translation key
        setText(TranslationManager.getInstance().getTranslation(title));

        // Set the content of the tab (to be implemented by subclasses)
        setContent(createSubTabPane());

        // Make the tab closable (default behavior, can be overridden)
        setClosable(false);
    }

    // Create the TabPane for sub-tabs
    private TabPane createSubTabPane() {
        TabPane subTabPane = new TabPane();
        subTabPane.setSide(Side.LEFT); // Display tabs on the left
        subTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Prevent closing sub-tabs

        InfoTab infoTab = new InfoTab(character, mainTabPane);
        MagicTab magicTab = new MagicTab();
        StatusTab statusTab = new StatusTab();
        ExtraTab extraTab = new ExtraTab();

        // Add sub-tabs
        subTabPane.getTabs().addAll(infoTab, magicTab, statusTab, extraTab);

        return subTabPane;
    }
}