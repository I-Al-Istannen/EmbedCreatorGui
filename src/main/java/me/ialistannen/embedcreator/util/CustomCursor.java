package me.ialistannen.embedcreator.util;

import java.util.function.Supplier;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

/**
 * Some custom {@link Cursor}s.
 */
public enum CustomCursor {
  EDIT(() -> {
    try {
      Image cursorEditImage = new Image("/images/cursor_edit_2.png");
      return new ImageCursor(cursorEditImage, 0, 63);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Cursor.CROSSHAIR;
  });

  private Cursor cursor;

  CustomCursor(Supplier<Cursor> cursorSupplier) {
    this.cursor = cursorSupplier.get();
  }


  /**
   * @return The cursor for this entry.
   */
  public Cursor cursor() {
    return cursor;
  }
}
