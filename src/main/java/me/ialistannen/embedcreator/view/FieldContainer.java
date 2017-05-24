package me.ialistannen.embedcreator.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * A Container to store Fields inside
 */
public class FieldContainer extends FlowPane {

  private static final double FIELDS_PER_LINE = 3;

  /**
   * Creates a new {@link FieldContainer}.
   */
  public FieldContainer() {
    setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    setMinSize(10, 10);
    setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

    setVgap(10);
    setHgap(10);
  }

  /**
   * @return All {@link EmbedField}s in this container.
   */
  public List<EmbedField> getFields() {
    return getChildren().stream()
        .filter(node -> node instanceof EmbedField)
        .map(node -> (EmbedField) node)
        .collect(Collectors.toList());
  }

  /**
   * Adds a new field to this container.
   *
   * @param name The name of the field to add
   * @param value The value of the field to add
   * @param inline Whether it is an inline field
   */
  public void addNewField(String name, String value, boolean inline) {
    EmbedField field = new EmbedField(name, value, inline);
    field.setOwner(this);

    if (inline) {
      addNormalField(field);
    } else {
      addBlockField(field);
    }

    // no idea why I need to wait here. It makes no sense.
    Platform.runLater(() -> getScene().getWindow().sizeToScene());
  }

  private void addNormalField(EmbedField field) {
    field.maxWidthProperty().unbind();
    field.maxWidthProperty().bind(widthProperty());
    field.minWidthProperty().bind(
        widthProperty()
            .subtract(getHgap() * FIELDS_PER_LINE)
            .divide(FIELDS_PER_LINE)
    );
    getChildren().add(field);
  }

  private void addBlockField(EmbedField field) {
    getChildren().add(new Placeholder());

    addNormalField(field);

    getChildren().add(new Placeholder());
  }

  /**
   * Refreshes this node.
   */
  void refresh() {
    List<Node> children = new ArrayList<>(getChildren());
    getChildren().clear();
    for (Node child : children) {
      if (child instanceof EmbedField) {
        EmbedField field = (EmbedField) child;
        if (field.isInline()) {
          addNormalField(field);
        } else {
          addBlockField(field);
        }
      }
    }
    getScene().getWindow().sizeToScene();
  }

  private static class Placeholder extends Region {

    Placeholder() {
      setMinHeight(1);
      maxHeight(1);
      setPrefHeight(1);
      parentProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null && newValue instanceof Pane) {
          prefWidthProperty().unbind();
          prefWidthProperty().bind(((Pane) newValue).widthProperty());
        }
      });
    }
  }
}
