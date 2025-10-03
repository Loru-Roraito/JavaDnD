package com.dnd.ui.tabs;

import com.dnd.TranslationManager;
import com.dnd.ViewModel;

import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class CharacterTab extends Tab{
    private final ViewModel character;
    private final TabPane mainTabPane; // Reference to the main TabPane
    public CharacterTab(String title, ViewModel character, TabPane mainTabPane) {
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
        StatusTab statusTab = new StatusTab(character, mainTabPane);
        ExtraTab extraTab = new ExtraTab(character, mainTabPane);

        // Add sub-tabs
        subTabPane.getTabs().addAll(infoTab, magicTab, statusTab, extraTab);

        return subTabPane;
    }
}