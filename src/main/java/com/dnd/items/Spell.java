package com.dnd.items;

import com.dnd.SpellManager;

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
        
        this.school = getSpellString(new String[]{name, "school"});
        this.time = getSpellInt(new String[]{name, "time"});
        this.timeSpan = getSpellString(new String[]{name, "time_span"});
        this.duration = getSpellInt(new String[]{name, "duration"});
        this.durationSpan = getSpellString(new String[]{name, "duration_span"});
        this.materials = getSpellGroup(new String[]{name, "materials"});
        this.level = getSpellInt(new String[]{name, "level"});
        this.range = getSpellInt(new String[]{name, "range"});
        this.concentration = getSpellBoolean(new String[]{name, "concentration"});
        this.ritual = getSpellBoolean(new String[]{name, "ritual"});
        this.components = getSpellBooleans(new String[]{name, "components"});
    }

    private Boolean getSpellBoolean(String[] group) {
        return SpellManager.getInstance().getSpellBoolean(group);
    }

    private Boolean[] getSpellBooleans(String[] group) {
        return SpellManager.getInstance().getSpellBooleans(group);
    }

    private int getSpellInt(String[] group) {
        return SpellManager.getInstance().getSpellInt(group);
    }

    private String getSpellString(String[] group) {
        return SpellManager.getInstance().getSpellString(group);
    }

    private String[] getSpellGroup(String[] group) {
        return SpellManager.getInstance().getSpellGroup(group);
    }
}