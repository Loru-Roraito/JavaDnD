package com.dnd.backend;

import java.util.Arrays;

public class GroupManager extends Manager {
    private static final GroupManager instance = new GroupManager();
    
    private GroupManager() {
        initialize();
    }

    public static GroupManager getInstance() {
        return instance;
    }

    @Override
    protected String getJsonFileName() {
        return "groups.json";
    }

    public String[] getMagicClasses() {
        return getStrings(new String[] {"magic_classes"});
    }

    public String[] getSelectableFeats() {
        String[] allFeats = getStrings(new String[] {"feats"});
        return Arrays.stream(allFeats)
                .filter(feat -> {
                    String type = getString(new String[] {"feats", feat, "type"});
                    return type.equals("ORIGIN") || type.equals("GENERAL") || type.equals("EPIC_BOON");
                })
                .toArray(String[]::new);
    }

    public String[] getRepeatableFeats() {
        String[] selectableFeats = getSelectableFeats();
        return Arrays.stream(selectableFeats)
                .filter(feat -> {
                    return getBoolean(new String[] {"feats", feat, "repeatable"});
                })
                .toArray(String[]::new);
    }
}
