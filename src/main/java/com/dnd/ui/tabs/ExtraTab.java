package com.dnd.ui.tabs;

import com.dnd.TranslationManager;

import javafx.scene.control.Tab;

public class ExtraTab extends Tab{
    public ExtraTab(){
        setText(getTranslation("EXTRA"));
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }
}