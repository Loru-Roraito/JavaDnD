package com.dnd.items;

import com.dnd.TranslationManager;

public class Spell {
    private final String name;
    private final String school;
    private final String timeSpan;
    private final String durationSpan;
    private final String change; // When you can change the spell
    private final String prepare; // When you can prepare the spell
    private final String[] focus; // types of focus that can be used
    private final String[] materials; // TODO (probably a specific properties file)

    private final int level;
    private final int ability;
    private final int range;
    private final int time;
    private final int duration;

    private final Boolean concentration;
    private final Boolean ritual;
    private final Boolean overriding; // if true, the spell takes the place of spell with same name
    private final Boolean limited; // only one limited spell's preparation can be changed at a time
    private final Boolean[] components; // verbal, somatic, material

    public Spell(String nominative, String prepare, String change, String[] focus, int ability, Boolean overriding, Boolean limited) {
        this.name = nominative;
        this.change = change;
        this.prepare = prepare;
        this.focus = focus;
        this.ability = ability;
        this.overriding = overriding;
        this.limited = limited;
        
        this.school = getString(new String[]{"spells", name, "school"});
        this.time = getInt(new String[]{"spells", name, "time"});
        this.timeSpan = getString(new String[]{"spells", name, "time_span"});
        this.duration = getInt(new String[]{"spells", name, "duration"});
        this.durationSpan = getString(new String[]{"spells", name, "duration_span"});
        this.materials = getGroup(new String[]{"spells", name, "materials"});
        this.level = getInt(new String[]{"spells", name, "level"});
        this.range = getInt(new String[]{"spells", name, "range"});
        this.concentration = getBoolean(new String[]{"spells", name, "concentration"});
        this.ritual = getBoolean(new String[]{"spells", name, "ritual"});
        this.components = getBooleans(new String[]{"spells", name, "components"});
    }

    private Boolean getBoolean(String[] group) {
        return TranslationManager.getInstance().getBoolean(group);
    }

    private Boolean[] getBooleans(String[] group) {
        return TranslationManager.getInstance().getBooleans(group);
    }

    private int getInt(String[] group) {
        return TranslationManager.getInstance().getInt(group);
    }

    private String getString(String[] group) {
        return TranslationManager.getInstance().getString(group);
    }

    private String[] getGroup(String[] group) {
        return TranslationManager.getInstance().getGroup(group);
    }
}