package com.dnd.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BindingBoolean extends ObservableBoolean {
    private final Supplier<Boolean> computeFunction;

    public BindingBoolean(Supplier<Boolean> computeFunction, Observable<?>... dependencies) {
        super(computeFunction.get()); // Set initial value
        this.computeFunction = computeFunction;

        for (Observable<?> dep : dependencies) {
            dep.addListener((_) -> this.recompute());
        }
    }

    private void recompute() {
        forceSet(computeFunction.get());
    }

    public void forceSet(boolean newValue) {
        this.value = newValue;
        for (Consumer<Boolean> listener : listeners) {
            listener.accept(newValue);
        }
    }
}
