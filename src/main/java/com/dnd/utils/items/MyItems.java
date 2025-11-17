package com.dnd.utils.items;

import com.dnd.utils.observables.ObservableString;

public interface MyItems<T extends MyItems<T>> {
    String getName();
    ObservableString getNameProperty();
    void setName(String value);
    T copy();
}
