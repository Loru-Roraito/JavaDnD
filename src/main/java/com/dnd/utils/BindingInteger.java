package com.dnd.utils;

import java.util.function.Supplier;

public class BindingInteger extends ObservableInteger {
    private final Supplier<Integer> computeFunction;

    public BindingInteger(Supplier<Integer> computeFunction, Observable<?>... dependencies) {
        super(computeFunction.get()); // Set initial value
        this.computeFunction = computeFunction;

        for (Observable<?> dep : dependencies) {
            dep.addListener((_) -> this.recompute());
        }
    }

    private void recompute() {
        this.set(computeFunction.get());
    }
}
