package com.dnd.utils.items;

import com.dnd.utils.observables.ObservableString;

public class Proficiency implements MyItems<Proficiency> {
    private final ObservableString name;
    private final String group;
    
    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public void setName(String value) {
        name.set(value);
    }

    @Override
    public Proficiency copy() {
        return new Proficiency(name.get(), group);
    }

    @Override
    public ObservableString getNameProperty() {
        return name;
    }

    public Proficiency(String name, String group) {
        this.name = new ObservableString(name);
        this.group = group;
    }

    public String getStrings() {
        return group;
    }
}
