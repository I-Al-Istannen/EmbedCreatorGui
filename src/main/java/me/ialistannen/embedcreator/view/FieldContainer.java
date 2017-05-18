package me.ialistannen.embedcreator.view;

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

  }

  private static class Placeholder extends Region {

    Placeholder() {
      parentProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null && newValue instanceof Pane) {
          prefWidthProperty().unbind();
          prefWidthProperty().bind(((Pane) newValue).widthProperty());
        }
      });
    }
  }
}
