package com.dnd.utils.observables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ObservableString implements Observable<String> {
    private String value;
    private final List<Consumer<String>> listeners = new ArrayList<>();

    public ObservableString(String initialValue) {
        this.value = initialValue;
    }

    public void set(String newValue) {
        String oldValue = value;
        
        // Only update and notify if value actually changed
        if (!Objects.equals(oldValue, newValue)) {
            // TODO: done only to avoid triggering updates when refreshing comboboxes
            if (oldValue.equals("PLACEHOLDER") || newValue.equals("PLACEHOLDER")) {
                return;
            }
            value = newValue;
            notifyListeners();
        }
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public void addListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Consumer<String> listener : listeners) {
            listener.accept(value);
        }
    }
}