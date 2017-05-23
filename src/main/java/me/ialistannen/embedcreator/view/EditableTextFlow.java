package me.ialistannen.embedcreator.view;

import java.util.Optional;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.ialistannen.embedcreator.cantbebothered.GlobalChangeListener;
import me.ialistannen.embedcreator.model.CharacterLimit;
import me.ialistannen.embedcreator.model.variables.Variable;
import me.ialistannen.embedcreator.model.variables.VariableRegistry;
import me.ialistannen.embedcreator.util.CustomCursor;
import me.ialistannen.embedcreator.util.SavedNodePosition;

/**
 * An editable {@link TextFlow}
 */
public class EditableTextFlow extends TextFlow {

  private SavedNodePosition savedNodePosition;
  private TextArea editingArea;
  private Pane parent;

  private CharacterLimit characterLimit;

  /**
   * Creates an empty TextFlow layout.
   */
  @SuppressWarnings("WeakerAccess") // needed for
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
    editingArea.setMinSize(getMinWidth(), getMinHeight());
    // some padding at the sides is nice
    editingArea.setMaxSize(getMaxWidth() - 30, getMaxHeight() - 30);
    editingArea.setPrefHeight(getHeight() + 30);
    editingArea.setPadding(getPadding());

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
      String text = editingArea.getText();
      Platform.runLater(() -> setText(text));
    }

    // may be called multiple times
    parent.getChildren().removeAll(editingArea, this);
    savedNodePosition.restore(this, parent);

    parent.getScene().getWindow().sizeToScene();
  }

  /**
   * @return the text of this {@link TextFlow}.
   */
  public String getText() {
    return getChildren().stream()
        .map(this::getText)
        .filter(string -> !string.isEmpty())
        .collect(Collectors.joining(""));
  }

  /**
   * @return The max length this text may have including variables.
   */
  public int getLengthResolveVariables() {
    int size = 0;
    for (Node node : getChildren()) {
      String text = getText(node);
      if (!node.getStyleClass().contains("variable") || text.length() < 2) {
        size += text.length();
      } else {
        text = text.substring(1, text.length() - 1);
        Optional<Variable> variable = VariableRegistry.getVariable(text);
        if (!variable.isPresent()) {
          size += text.length() + 2;
          continue;
        }
        System.out
            .println("Added " + variable.get().getMaxLength() + " for " + variable.get().getName());
        size += variable.get().getMaxLength();
      }
    }
    System.out.println(VariableRegistry.getAllVariables());
    return size;
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

  /**
   * Sets the text of this {@link TextFlow}.
   *
   * @param text The new text
   */
  private void setText(String text) {
    getChildren().clear();
    if (text.isEmpty()) {
      return;
    }

    int lastNormalChar = -1;
    for (int i = 0; i < text.length(); i++) {
      int codePoint = text.codePointAt(i);
      if (codePoint == '{') {

        if (lastNormalChar >= 0) {
          addText(text.substring(lastNormalChar, i), false);
          lastNormalChar = -1;
        }

        int endIndex = text.indexOf('}', i);

        if (endIndex < 0) {
          lastNormalChar = i;
          break;
        }

        addText(text.substring(i, endIndex + 1), true);
        i = endIndex;
        continue;
      }

      if (lastNormalChar < 0) {
        lastNormalChar = i;
      }
    }

    if (lastNormalChar >= 0) {
      addText(text.substring(lastNormalChar, text.length()), false);
    }

    highlightIfTooLong();

    GlobalChangeListener.getInstance().notifyOfChange();
  }

  /**
   * Adds text to this {@link TextFlow}.
   *
   * @param text The text to add
   * @param variable True if it is a variable
   */
  private void addText(String text, boolean variable) {
    Text textNode = new Text(text);
    if (variable && VariableRegistry.hasVariable(getVariableName(text))) {
      // add it a pulse later as otherwise CSS will not be applied to this node.
      Platform.runLater(() -> textNode.getStyleClass().add("variable"));
    }
    getChildren().add(textNode);
  }

  /**
   * Returns the variable name from a full text string.
   *
   * @param fullText The full variable name, in the form of {@code "{name}"}.
   * @return The name inside the brackets
   */
  private String getVariableName(String fullText) {
    return fullText.substring(1, fullText.length() - 1);
  }

  /**
   * Highlights the {@link TextFlow} if it is too long.
   */
  private void highlightIfTooLong() {
    getStyleClass().removeIf(string -> string.equalsIgnoreCase("too-long"));

    if (characterLimit != null) {
      if (getText().length() > characterLimit.getMaxSize()) {
        getStyleClass().add("too-long");
      }
    }
  }

  /**
   * @param characterLimit The {@link CharacterLimit} for this {@link EditableTextFlow}.
   */
  public void setCharacterLimit(CharacterLimit characterLimit) {
    this.characterLimit = characterLimit;
  }
}
