package com.dnd.frontend.tooltip;

import java.util.ArrayList;
import java.util.List;

import com.dnd.frontend.language.DefinitionManager;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.stage.Window;

public class FrozenTooltipManager {
    // TODO: done quickly with AI (besides the main structure)
    private static final List<Popup> frozenTooltips = new ArrayList<>();
    private static final BooleanProperty isFrozen = new SimpleBooleanProperty(false);
    private static TabPane mainTabPane;

    public static void freeze(Tooltip tooltip, Node sourceNode, TabPane mainTabPane) {
        FrozenTooltipManager.mainTabPane = mainTabPane;
        if (tooltip == null || tooltip.getText() == null || tooltip.getText().isEmpty()) {
            closeAll();
            return;
        }
        String tooltipText = tooltip.getText();
        double tooltipX = tooltip.getX();
        double tooltipY = tooltip.getY();
        tooltip.hide();
        isFrozen.set(true);

        String sourceText = "";
        if (sourceNode instanceof Labeled labeled) {
            sourceText = labeled.getText();
        }

        createFrozenPopup(tooltipText, sourceText, mainTabPane, sourceNode, tooltipX, tooltipY);
    }

    private static void createFrozenPopup(String content, String sourceText, TabPane mainTabPane, Node positionNode, double x, double y) {
        TextFlow textFlow = new TextFlow();
        textFlow.setMaxWidth(300);

        DefinitionManager.fillTextFlow(textFlow, content, mainTabPane, sourceText);

        // Add T-key and focus handlers to all children
        for (Node child : textFlow.getChildren()) {
            if (child instanceof Text textNode) {
                boolean isInteractive = textNode.getStyle() != null && textNode.getStyle().contains("#694704");

                child.setFocusTraversable(true);
                child.setOnMouseEntered(_ -> child.requestFocus());

                if (isInteractive) {
                    String wordKey = textNode.getText().replace(" ", "_");
                    child.setOnKeyPressed(event -> {
                        if (event.getCode() == KeyCode.T) {
                            String wordTooltip = DefinitionManager.fetchTooltip(wordKey);
                            if (wordTooltip != null && !wordTooltip.isEmpty()) {
                                isFrozen.set(false);
                                isFrozen.set(true);
                                Bounds childBounds = child.localToScreen(child.getBoundsInLocal());
                                createFrozenPopup(wordTooltip, wordKey, mainTabPane, child, childBounds.getMinX(), childBounds.getMaxY() + 2);
                            } else {
                                closeAll();
                                event.consume();
                            }
                            event.consume();
                        } else if (event.getCode() == KeyCode.ESCAPE) {
                            closeAll();
                            event.consume();
                        }
                    });
                    child.setOnMouseClicked((event) -> {
                        DefinitionManager.openDefinitionTab(wordKey, mainTabPane);
                        closeAll();
                        event.consume();
                    });
                } else {
                    child.setOnKeyPressed(event -> {
                        if (event.getCode() == KeyCode.T || event.getCode() == KeyCode.ESCAPE) {
                            closeAll();
                            event.consume();
                        }
                    });
                }
            }
        }

        VBox container = new VBox(textFlow);
        container.setPadding(new Insets(8));
        container.setStyle(
            "-fx-background-color: #e2d4aa; " +
            "-fx-border-color: #7b3e19; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 2, 2);"
        );

        // Fallback: T or Escape on the container closes all
        container.setFocusTraversable(true);
        container.setOnMouseEntered(_ -> {
            // Only take focus if no child has it
            if (container.getScene() == null) return;
            Node focused = container.getScene().getFocusOwner();
            if (focused == null || !isDescendantOf(focused, container)) {
                container.requestFocus();
            }
        });
        container.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.T || event.getCode() == KeyCode.ESCAPE) {
                isFrozen.set(false);
                closeAll();
                event.consume();
            }
        });

        Popup popup = new Popup();
        popup.getContent().add(container);
        popup.setAutoHide(true);
        popup.setOnAutoHide(_ -> closeAll());

        // Position at the same location as the original tooltip
        Window window = positionNode.getScene().getWindow();
        popup.show(window, x, y);

        frozenTooltips.add(popup);
    }

    public static void closeAll() {
        isFrozen.set(false);
        for (Popup popup : frozenTooltips) {
            popup.hide();
        }
        frozenTooltips.clear();
        mainTabPane.requestFocus();
    }

    private static boolean isDescendantOf(Node node, Node ancestor) {
        Node current = node;
        while (current != null) {
            if (current == ancestor) return true;
            current = current.getParent();
        }
        return false;
    }

    public static BooleanProperty isFrozen() {
        return isFrozen;
    }
}
