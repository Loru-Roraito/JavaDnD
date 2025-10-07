package com.dnd.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
 
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;

public class ComboBoxUtils {
    // This class is a mess, it took me hours to get it to work properly. Mainly because of a bug that wouldn't display correctly the
    // updated items (I should have fixed that with the last line of code). Because I had to try many different approaches
    // (coming from my own ideas, stack overflow, and AI), there may be some bad language/logic. Everything should still work fine, but be advised

    /**
     * Updates the items of a ComboBox so that no value (except repeatables) is selected in more than one ComboBox.
     * This version:
     *  - edits the existing ObservableList (no setItems),
     *  - prevents ghost/blank rows,
     *  - preserves the current selection when still valid.
     *
     * @param comboBox       The ComboBox to update.
     * @param comboBoxes     The list of all ComboBoxes in the group.
     * @param baseValues     The EXISTING items list used by this comboBox (usually comboBox.getItems()).
     * @param startingValues The starting values to consider for availability.
     * @param repeatables    Values allowed to repeat across ComboBoxes (e.g., "RANDOM").
     */
    public static void updateItems(
            ComboBox<String> comboBox,
            List<ComboBox<String>> comboBoxes,
            ObservableList<String> baseValues,
            String[] startingValues,
            String[] repeatables
    ) {
        // Compute selections in OTHER boxes (exclude repeatables).
        final Set<String> repeatableSet = new LinkedHashSet<>(Arrays.asList(repeatables));
        final Set<String> selectedElsewhere = comboBoxes.stream()
                .filter(cb -> cb != comboBox)
                .map(ComboBox::getValue)
                .filter(v -> v != null && !repeatableSet.contains(v))
                .collect(Collectors.toSet());

        // Build the new list: repeatables first (deduped, stable order), then allowed starting values.
        final List<String> newItems = new ArrayList<>();
        newItems.addAll(repeatableSet);
        for (String v : startingValues) {
            if (v != null && !v.isEmpty() && !repeatableSet.contains(v) && !selectedElsewhere.contains(v)) {
                newItems.add(v);
            }
        }

        // Preserve selection by VALUE (index can shift).
        final String currentValue = comboBox.getValue();
        final boolean currentStillValid = currentValue != null && newItems.contains(currentValue);

        // If current value will be invalid, clear selection BEFORE mutating to avoid IOBE.
        if (!currentStillValid) {
            comboBox.getSelectionModel().clearSelection();
        }

        // Automatically replace contents of the EXISTING list (no clear()+addAll()).
        baseValues.setAll(newItems);

        // Restore selection if possible; otherwise pick first repeatable (if present) or clear.
        if (currentStillValid) {
            comboBox.getSelectionModel().select(currentValue);
        } else if (!repeatableSet.isEmpty()) {
            // Select the first repeatable that actually exists in the new list
            String fallback = repeatableSet.stream().filter(newItems::contains).findFirst().orElse(null);
            if (fallback != null) {
                comboBox.getSelectionModel().select(fallback);
            } else {
                comboBox.getSelectionModel().clearSelection();
            }
        } else {
            comboBox.getSelectionModel().clearSelection();
        }
    }
}