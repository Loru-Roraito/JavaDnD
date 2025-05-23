package com.dnd.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ObservableBoolean implements Observable<Boolean> {
    public boolean value;
    public final List<Consumer<Boolean>> listeners = new ArrayList<>();

    public ObservableBoolean(boolean initialValue) {
        this.value = initialValue;
    }

    public void set(boolean newValue) {
        this.value = newValue;
        for (Consumer<Boolean> listener : listeners) {
            listener.accept(newValue);
        }
    }

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public void addListener(Consumer<Boolean> listener) {
        listeners.add(listener);
    }
}