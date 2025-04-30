package com.dnd.ui.tabs;

import com.dnd.TranslationManager;

import javafx.scene.control.Tab;

public class MagicTab extends Tab{
    public MagicTab(){
        setText(getTranslation("MAGIC"));
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }
}