package com.dnd.items;

import com.dnd.utils.ObservableString;

public interface MyItems<T extends MyItems<T>> {
    String getName();
    ObservableString getNameProperty();
    void setName(String value);
    T copy();
}
