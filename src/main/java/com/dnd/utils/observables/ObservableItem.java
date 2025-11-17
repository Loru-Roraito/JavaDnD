package com.dnd.utils.observables;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.dnd.utils.items.Item;

public class ObservableItem implements Observable<Item> {
    private Item item;
    private final List<Consumer<Item>> listeners = new ArrayList<>();

    public ObservableItem(Item item) {
        this.item = item;
    }

    public void set(Item newValue) {
        Item oldValue = item;

        if (!oldValue.equals(newValue)) {
            item = newValue;
            notifyListeners();
        }
    }

    @Override
    public Item get() {
        return item;
    }

    @Override
    public void addListener(Consumer<Item> listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Consumer<Item> listener : listeners) {
            listener.accept(item);
        }
    }
}