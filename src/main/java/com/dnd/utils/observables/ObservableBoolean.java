package com.dnd.utils.observables;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ObservableBoolean implements Observable<Boolean> {
    public Boolean value;
    public final List<Consumer<Boolean>> listeners = new ArrayList<>();

    public ObservableBoolean(Boolean initialValue) {
        this.value = initialValue;
    }

    public void set(Boolean newValue) {
        Boolean oldValue = this.value;
        
        // Only update and notify if value actually changed
        if (!oldValue.equals(newValue)) {
            this.value = newValue;
            notifyListeners();
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

    private void notifyListeners() {
        for (Consumer<Boolean> listener : listeners) {
            listener.accept(value);
        }
    }
}