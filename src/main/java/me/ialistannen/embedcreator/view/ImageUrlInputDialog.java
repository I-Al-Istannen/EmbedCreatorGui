package me.ialistannen.embedcreator.view;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;
import me.ialistannen.embedcreator.util.WebUtil;

/**
 * A Dialog to ask for an {@link java.net.URL} pointing to an image.
 */
public class ImageUrlInputDialog extends UrlInputDialog {

  /**
   * A new {@link ImageUrlInputDialog}.
   */
  public ImageUrlInputDialog() {
    super(ImageUrlInputDialog::isValidImageUrl);

    setHeaderText("Please input an URL for your image.");
    setTitle("Image URL");
  }

  /**
   * Checks if an url is a valid image url.
   *
   * @param input The url to check
   * @return True if it is a valid image url
   */
  private static boolean isValidImageUrl(String input) {
    try (InputStream inputStream = WebUtil.getWebsiteStream(input)) {
      Image image = new Image(inputStream);
      return image.getException() == null;
    } catch (UnirestException | IllegalArgumentException | IOException e) {
      return false;
    }
  }
}
