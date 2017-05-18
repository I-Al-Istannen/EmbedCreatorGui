package me.ialistannen.embedcreator.view;

import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.ialistannen.embedcreator.util.CustomCursor;
import me.ialistannen.embedcreator.util.SavedNodePosition;

/**
 * An editable {@link TextFlow}
 */
public class EditableTextFlow extends TextFlow {

  private SavedNodePosition savedNodePosition;
  private TextArea editingArea;
  private Pane parent;

  /**
   * Creates an empty TextFlow layout.
   */
  public EditableTextFlow() {
    setOnMouseClicked(event -> {
      if (event.getButton() != MouseButton.PRIMARY) {
        return;
      }
      savePositionAndRemove();
      setupEditingArea();

      parent.getScene().getWindow().sizeToScene();
    });

    setCursor(CustomCursor.EDIT.cursor());
  }

  private void savePositionAndRemove() {
    savedNodePosition = SavedNodePosition.of(this);
    if (getParent() instanceof Pane) {
      parent = (Pane) getParent();
      parent.getChildren().remove(this);
    } else {
      throw new IllegalStateException("I am not added to a Pane!");
    }
  }

  private void setupEditingArea() {
    editingArea = new TextArea(getText());

    editingArea.setWrapText(true);
    editingArea.setMinSize(10, 10);
    editingArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    editingArea.setPrefSize(getWidth(), getHeight() + 30);

    editingArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue) {
        endEditing(false);
      }
    });

    editingArea.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case ESCAPE:
          endEditing(false);
          break;
        case ENTER:
          if (event.isShiftDown()) {
            editingArea.appendText("\n");
          } else {
            endEditing(true);
          }
      }
    });

    savedNodePosition.restore(editingArea, parent);
    editingArea.selectAll();
    editingArea.requestFocus();
  }

  private void endEditing(boolean keepNewText) {
    if (keepNewText) {
      getChildren().clear();
      getChildren().add(new Text(editingArea.getText()));
    }

    // may be called multiple times
    parent.getChildren().removeAll(editingArea, this);
    savedNodePosition.restore(this, parent);

    parent.getScene().getWindow().sizeToScene();
  }

  /**
   * @return the text of this {@link TextFlow}.
   */
  private String getText() {
    return getChildren().stream()
        .map(this::getText)
        .filter(string -> !string.isEmpty())
        .collect(Collectors.joining(" "));
  }

  /**
   * Extracts text from a {@link Node} as there is no universally shared superclass.
   *
   * @param node The {@link Node} to extract text from
   * @return The text or an empty String if none
   */
  private String getText(Node node) {
    if (node instanceof Text) {
      return ((Text) node).getText();
    } else if (node instanceof Labeled) {
      return ((Labeled) node).getText();
    }
    return "";
  }

}
