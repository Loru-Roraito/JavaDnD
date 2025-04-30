package com.dnd.ui.tabs;

import com.dnd.TranslationManager;

import javafx.scene.control.Tab;

public class StatusTab extends Tab{
    public StatusTab(){
        setText(getTranslation("STATUS"));
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }
}