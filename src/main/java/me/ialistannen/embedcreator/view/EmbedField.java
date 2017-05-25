package me.ialistannen.embedcreator.view;

import java.io.IOException;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import me.ialistannen.embedcreator.util.Util;
import me.ialistannen.embedcreator.validation.CharacterLimit;

/**
 * An embed field
 */
public class EmbedField extends GridPane {

  @FXML
  private GridPane root;

  @FXML
  private EditableTextFlow name;

  @FXML
  private EditableTextFlow value;

  private boolean inline;
  private FieldContainer owner;

  /**
   * Creates a new {@link EmbedField}.
   */
  private EmbedField() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/field/Field.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException("An error occurred loading the field FXML", e);
    }

    setupContextMenu();
  }

  /**
   * Creates a new {@link EmbedField}.
   *
   * @param name The name of the field
   * @param value The value of the field
   * @param inline Whether it is an inline field
   */
  EmbedField(String name, String value, boolean inline) {
    this();
    setName(name);
    setValue(value);
    setInline(inline);
  }

  /**
   * Creates the {@link ContextMenu}.
   */
  private void setupContextMenu() {
    Util.setContextMenu(this, () -> {
      MenuItem changeType;
      if (isInline()) {
        changeType = new MenuItem("Change to block field");
        changeType.setOnAction(click -> setInline(false));
      } else {
        changeType = new MenuItem("Change to inline field");
        changeType.setOnAction(click -> setInline(true));
      }
      MenuItem delete = new MenuItem("Delete");
      delete.setOnAction(click -> {
        owner.getChildren().remove(this);
        owner.refresh();
      });
      return new ContextMenu(changeType, delete);
    });
  }

  @FXML
  private void initialize() {
    name.setCharacterLimit(CharacterLimit.FIELD_NAME);
    value.setCharacterLimit(CharacterLimit.FIELD_VALUE);
  }

  /**
   * @param inline True if this field should be an inline field
   */
  private void setInline(boolean inline) {
    this.inline = inline;

    pseudoClassStateChanged(PseudoClass.getPseudoClass("inline"), inline);

    if (owner != null) {
      owner.refresh();
    }
  }

  /**
   * @return True if the field is an inline field.
   */
  boolean isInline() {
    return inline;
  }

  /**
   * @param owner The {@link FieldContainer} owner.
   */
  void setOwner(FieldContainer owner) {
    this.owner = owner;
  }

  /**
   * @param name The new name
   */
  private void setName(String name) {
    this.name.getChildren().clear();
    this.name.getChildren().add(new Text(name));
  }

  /**
   * @param value The value
   */
  private void setValue(String value) {
    this.value.getChildren().clear();
    this.value.getChildren().add(new Text(value));
  }

  @Override
  public String toString() {
    return "EmbedField{"
        + "name=" + name.getText()
        + ", value=" + value.getText()
        + ", inline=" + isInline()
        + '}';
  }
}
