module com.dnd {
    requires com.fasterxml.jackson.databind;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.logging;

    opens com.dnd to javafx.fxml;
    exports com.dnd;
    exports com.dnd.ui.tabs;
    exports com.dnd.ui.panes;
    exports com.dnd.characters;
}