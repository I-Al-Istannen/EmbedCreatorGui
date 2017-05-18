package me.ialistannen.embedcreator.view;

import java.io.IOException;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import me.ialistannen.embedcreator.util.LabeledUtil;

/**
 * An embed field
 */
public class EmbedField extends GridPane {

  @FXML
  private GridPane root;

  @FXML
  private Label name;

  @FXML
  private Label value;

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

  @FXML
  private void initialize() {
    LabeledUtil.makeEditable(name);
    LabeledUtil.makeEditable(value);
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
  public boolean isInline() {
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
    this.name.setText(name);
  }

  /**
   * @param value The value
   */
  private void setValue(String value) {
    this.value.setText(value);
  }
}
