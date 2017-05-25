package me.ialistannen.embedcreator.view;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.ialistannen.embedcreator.util.CustomCursor;
import org.controlsfx.control.PopOver;

/**
 * The colored bar on the side of an embed.
 */
public class EmbedColorBar extends Region {

  private Color color = Color.ROYALBLUE;

  public EmbedColorBar() {
    setEditCursor();

    setOnMouseClicked(event -> {
      setCursor(Cursor.DEFAULT);
      PopOver popOver = new PopOver();
      ColorPicker colorPicker = new ColorPicker(color);

      Button confirmButton = new Button("Save");
      confirmButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
      confirmButton.setOnAction(confirm -> {
        setColor(colorPicker.getValue());
        popOver.hide(Duration.seconds(0.5));
      });

      VBox.setMargin(confirmButton, new Insets(5, 0, 0, 0));

      VBox vBox = new VBox(colorPicker, confirmButton);
      vBox.setFillWidth(true);

      popOver.setContentNode(vBox);
      popOver.show(this, event.getScreenX(), event.getScreenY());

      popOver.setOnHidden(hidden -> setEditCursor());
    });

    refreshBackground();
  }

  private void setEditCursor() {
    setCursor(CustomCursor.EDIT.cursor());
  }

  /**
   * @param color The new {@link Color}
   */
  private void setColor(Color color) {
    this.color = color;
    refreshBackground();
  }

  private void refreshBackground() {
    BackgroundFill backgroundFill = new BackgroundFill(
        color,
        new CornerRadii(10, 0, 0, 10, false),
        null
    );
    Background background = new Background(backgroundFill);
    setBackground(background);
  }

  /**
   * @return The {@link Color} of the bar.
   */
  public Color getColor() {
    return color;
  }
}
