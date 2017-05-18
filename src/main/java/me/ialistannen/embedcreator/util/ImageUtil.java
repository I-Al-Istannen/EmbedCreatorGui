package me.ialistannen.embedcreator.util;

import java.util.Optional;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import me.ialistannen.embedcreator.view.ImageUrlInputDialog;
import me.ialistannen.embedcreator.view.UrlImageView;

/**
 * Some utility functions for {@link Image}s and related stuff.
 */
public class ImageUtil {

  /**
   * Makes an {@link UrlImageView} editable.
   *
   * @param imageView The {@link UrlImageView} to make editable
   */
  public static void makeEditable(UrlImageView imageView) {
    imageView.setCursor(CustomCursor.EDIT.cursor());

    imageView.setOnMouseClicked(event -> {
      if (event.getButton() != MouseButton.PRIMARY) {
        return;
      }
      ImageUrlInputDialog imageUrlInputDialog = new ImageUrlInputDialog();
      imageUrlInputDialog.setUrl(imageView.getUrl());

      Optional<String> newUrlOptional = imageUrlInputDialog.showAndWait();

      newUrlOptional.ifPresent(imageView::setImage);
    });
  }

}
