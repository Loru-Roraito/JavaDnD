package com.dnd.utils;

import com.dnd.ui.tooltip.TooltipComboBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ComboBoxUtils {
    /**
     * Updates the items of a ComboBox so that no value (except "RANDOM") is selected in more than one ComboBox.
     * @param comboBox The ComboBox to update.
     * @param comboBoxes The list of all ComboBoxes in the group.
     * @param baseValues The base values to show in the ComboBoxes.
     * @param randomValue The value representing "RANDOM" (should be localized if needed).
     */
    public static void updateItems(
            TooltipComboBox<String> comboBox,
            List<TooltipComboBox<String>> comboBoxes,
            ObservableList<String> baseValues,
            String randomValue
    ) {
        Set<String> selected = comboBoxes.stream()
                .filter(cb -> cb != comboBox)
                .map(TooltipComboBox::getValue)
                .filter(v -> v != null && !v.equals(randomValue))
                .collect(Collectors.toSet());

        List<String> newItems = new ArrayList<>();
        newItems.add(randomValue);

        for (String val : baseValues) {
            if (!val.equals(randomValue) && !selected.contains(val)) {
                newItems.add(val);
            }
        }

        ObservableList<String> items = comboBox.getItems();
        if (items == null) {
            comboBox.setItems(FXCollections.observableArrayList(newItems));
        } else {
            items.setAll(newItems);
        }

        String currentValue = comboBox.getValue();
        if (currentValue != null && newItems.contains(currentValue)) {
            comboBox.setValue(currentValue);
        } else {
            comboBox.setValue(randomValue);
        }
    }
}