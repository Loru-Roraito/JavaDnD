package com.dnd.items;

import com.dnd.SpellManager;
import com.dnd.utils.ObservableString;

public class Spell implements MyItems<Spell> {
    private final ObservableString name; // Display name
    private final String nominative; // Original name, used as key in data files
    private final String school;
    private final String timeSpan;
    private final String durationSpan;
    private final String prepare; // When you can prepare the spell
    private final String[] focus; // Types of focus that can be used

    private final int level;
    private final int ability;
    private final int range;
    private final int time;
    private final int duration;

    private final Boolean concentration;
    private final Boolean ritual;
    private final Boolean overriding; // If true, the spell takes the place of spell with same name
    private final Boolean limited; // Only one limited spell's preparation can be changed at a time
    private final Boolean[] components; // Verbal, somatic, material

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public void setName(String value) {
        name.set(value);
    }

    @Override
    public Spell copy() {
        return new Spell(nominative, prepare, focus, ability, overriding, limited);
    }

    @Override
    public ObservableString getNameProperty() {
        return name;
    }

    public Spell(String nominative, String prepare, String[] focus, int ability, Boolean overriding, Boolean limited) {
        this.nominative = nominative;
        this.name = new ObservableString(nominative);
        this.prepare = prepare;
        this.focus = focus;
        this.ability = ability;
        this.overriding = overriding;
        this.limited = limited;
        
        this.school = getSpellString(new String[]{nominative, "school"});
        this.time = getSpellInt(new String[]{nominative, "time"});
        this.timeSpan = getSpellString(new String[]{nominative, "time_span"});
        this.duration = getSpellInt(new String[]{nominative, "duration"});
        this.durationSpan = getSpellString(new String[]{nominative, "duration_span"});
        this.level = getSpellInt(new String[]{nominative, "level"});
        this.range = getSpellInt(new String[]{nominative, "range"});
        this.concentration = getSpellBoolean(new String[]{nominative, "concentration"});
        this.ritual = getSpellBoolean(new String[]{nominative, "ritual"});
        this.components = getSpellBooleans(new String[]{nominative, "components"});
    }

    public Boolean equals(Spell other) {
        Boolean found = false;
        for (String otherFocus : other.focus) {
            for (String thisFocus : this.focus) {
                if (!thisFocus.equals(otherFocus)) {
                    found = true;
                    break;
                }
            }
        }
        return this.nominative.equals(other.nominative)
            && this.prepare.equals(other.prepare)
            && this.ability == other.ability
            && this.overriding.equals(other.overriding)
            && this.limited.equals(other.limited)
            && !found;
    }

    public String getNominative() {
        return nominative;
    }

    public String getPrepare() {
        return prepare;
    }

    public String[] getFocus() {
        return focus;
    }

    public int getAbility() {
        return ability;
    }

    public Boolean getOverriding() {
        return overriding;
    }

    public Boolean getLimited() {
        return limited;
    }

    public int getLevel() {
        return level;
    }

    public String getSchool() {
        return school;
    }

    public Boolean getConcentration() {
        return concentration;
    }

    public Boolean getRitual() {
        return ritual;
    }

    public String getTimeSpan() {
        return timeSpan;
    }

    public int getTime() {
        return time;
    }

    public String getDurationSpan() {
        return durationSpan;
    }

    public int getDuration() {
        return duration;
    }

    public int getRange() {
        return range;
    }

    public Boolean[] getComponents() {
        return components;
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
}