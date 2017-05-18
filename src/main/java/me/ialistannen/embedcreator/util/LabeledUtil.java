package me.ialistannen.embedcreator.util;

import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

/**
 * Some util classes for {@link javafx.scene.control.Labeled}
 */
public class LabeledUtil {

  /**
   * Makes a {@link Labeled} editable.
   *
   * @param label The {@link Labeled} to make editable
   */
  public static void makeEditable(Labeled label) {
    label.setCursor(CustomCursor.EDIT.cursor());

    label.setOnMouseClicked(event -> {
      if (event.getButton() != MouseButton.PRIMARY) {
        return;
      }
      Pane parent = (Pane) label.getParent();
      SavedNodePosition savedNodePosition = SavedNodePosition.of(label);
      parent.getChildren().remove(label);

      TextField textField = new TextField(label.getText());
      textField.setMinSize(0, 0);

      // fix padding screwing stuff up
      textField.paddingProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
          double addition = newValue.getLeft() + newValue.getRight();
          textField.setPrefWidth(label.getWidth() + addition);
        }
      });

      textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue) {
          restoreLabel(label, parent, savedNodePosition, textField);
        }
      });

      textField.setPrefSize(label.getWidth(), label.getHeight() + 20);
      textField.setMaxSize(Double.MAX_VALUE, label.getHeight() + 20);
      savedNodePosition.restore(textField, parent);

      textField.setOnKeyPressed(keyEvent -> {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
          restoreLabel(label, parent, savedNodePosition, textField);
        }
      });

      textField.setOnAction(finishEvent -> {
        label.setText(textField.getText());
        restoreLabel(label, parent, savedNodePosition, textField);

        if (label.getScene() != null && label.getScene().getWindow() != null) {
          label.getScene().getWindow().sizeToScene();
        }
      });

      textField.selectAll();
      textField.requestFocus();
    });
  }

  /**
   * Restores the label and hides the TextField.
   *
   * @param labeled The {@link Labeled} to make editable
   * @param parent The parent {@link Pane}
   * @param savedNodePosition The {@link SavedNodePosition} to restore
   * @param textField The TextField tp remove
   */
  private static void restoreLabel(Labeled labeled, Pane parent,
      SavedNodePosition savedNodePosition, TextField textField) {

    // may be called multiple times
    parent.getChildren().removeAll(labeled, textField);
    savedNodePosition.restore(labeled, parent);
  }
}
