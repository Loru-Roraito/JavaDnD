package com.dnd.utils.observables;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CustomObservableList<T> {
    private final List<T> items = new ArrayList<>();
    private final List<Consumer<CustomObservableList<T>>> listeners = new ArrayList<>();

    public void add(T item) {
        items.add(item);
        notifyListeners();
    }

    public void remove(T item) {
        items.remove(item);
        notifyListeners();
    }

    public void setAll(List<T> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyListeners();
    }

    public void clear() {
        items.clear();
        notifyListeners();
    }

    public List<T> getList() {
        return items;
    }

    public int size() {
        return items.size();
    }

    public List<T> asList() {
        return new ArrayList<>(items);
    }

    public void addListener(Consumer<CustomObservableList<T>> listener) {
        listeners.add(listener);
    }

    public Boolean isEmpty() {
        return items.isEmpty();
    }

    private void notifyListeners() {
        for (Consumer<CustomObservableList<T>> listener : listeners) {
            listener.accept(this);
        }
    }
}