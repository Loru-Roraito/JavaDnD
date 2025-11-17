module com.dnd {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.dnd to javafx.fxml;
    exports com.dnd;
    exports com.dnd.backend;
    exports com.dnd.frontend;
    exports com.dnd.frontend.language;
    exports com.dnd.frontend.panes;
    exports com.dnd.frontend.tabs;
    exports com.dnd.frontend.tooltip;
    exports com.dnd.utils;
    exports com.dnd.utils.items;
    exports com.dnd.utils.observables;
}