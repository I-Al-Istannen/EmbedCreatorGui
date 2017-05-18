package me.ialistannen.embedcreator.controller.field;

import java.io.IOException;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import me.ialistannen.embedcreator.util.LabeledUtil;

/**
 * The controller for a EmbedField.
 *
 * <p><em>Needs to be added to a {@link FlowPane}!</em>
 */
public class FieldController {

  @FXML
  private GridPane root;

  @FXML
  private Label name;

  @FXML
  private Label value;

  private boolean isInline;

  @FXML
  private void initialize() {
    LabeledUtil.makeEditable(name);
    LabeledUtil.makeEditable(value);

    root.parentProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null) {
        return;
      }
      if (!(newValue instanceof FlowPane)) {
        throw new IllegalStateException("I need to be added to a FlowPane!");
      }
      recalculateWidth((FlowPane) newValue);
    });
  }

  /**
   * Recalculates the width of the field.
   *
   * @param parent The Parent {@link FlowPane}
   */
  private void recalculateWidth(FlowPane parent) {
    root.minWidthProperty().unbind();
    root.maxWidthProperty().unbind();

    if (!isInline) {
      root.minWidthProperty().bind(parent.widthProperty().subtract(50));
    } else {
      root.minWidthProperty().bind(parent.widthProperty().subtract(50).divide(3));
    }

    root.maxWidthProperty().bind(parent.widthProperty());
  }

  /**
   * Sets the name.
   *
   * @param name The new name
   */
  public void setName(String name) {
    this.name.setText(name);
  }

  /**
   * Sets the value.
   *
   * @param value The new value
   */
  public void setValue(String value) {
    this.value.setText(value);
  }

  /**
   * Sets an inline field.
   *
   * @param inline True if the field is an inline field
   */
  public void setInline(boolean inline) {
    isInline = inline;
    root.pseudoClassStateChanged(PseudoClass.getPseudoClass("inline"), inline);

    name.setWrapText(!isInline);
    value.setWrapText(!isInline);

    if (root.getParent() != null) {
      recalculateWidth((FlowPane) root.getParent());
    }
  }

  /**
   * @return True if this field is an inline field.
   */
  public boolean isInline() {
    return isInline;
  }

  /**
   * Creates a new EmbedField.
   *
   * @return The created {@link FieldController} and the {@link Parent} that resembles the pane.
   */
  public static Pair<FieldController, Parent> createNew() {
    try {
      FXMLLoader loader = new FXMLLoader(
          FieldController.class.getResource("/fxml/field/Field.fxml")
      );
      Parent parent = loader.load();
      FieldController controller = loader.getController();

      return new Pair<>(controller, parent);
    } catch (IOException e) {
      throw new RuntimeException("Error loading field stylesheet", e);
    }
  }
}
