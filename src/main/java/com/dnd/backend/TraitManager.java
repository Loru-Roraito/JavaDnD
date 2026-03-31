package com.dnd.backend;

public class TraitManager extends Manager {
    private static final TraitManager instance = new TraitManager();
    
    private TraitManager() {
        initialize();
    }

    public static TraitManager getInstance() {
        return instance;
    }

    @Override
    protected String getJsonFileName() {
        return "traits.json";
    }
}
