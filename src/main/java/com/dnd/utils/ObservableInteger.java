package com.dnd.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ObservableInteger implements Observable<Integer> {
    private int value;
    private final List<Consumer<Integer>> listeners = new ArrayList<>();

    public ObservableInteger(int initialValue) {
        this.value = initialValue;
    }

    public void set(int newValue) {
        Integer oldValue = this.value;
        
        // Only update and notify if value actually changed
        if (!Objects.equals(oldValue, newValue)) {
            this.value = newValue;
            notifyListeners();
        }
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public void addListener(Consumer<Integer> listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Consumer<Integer> listener : listeners) {
            listener.accept(value);
        }
    }
}