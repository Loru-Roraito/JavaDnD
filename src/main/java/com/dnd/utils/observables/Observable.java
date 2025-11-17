package com.dnd.utils.observables;

import java.util.function.Consumer;

public interface Observable<T> {
    T get();
    void addListener(Consumer<T> listener);
}