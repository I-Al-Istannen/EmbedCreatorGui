package me.ialistannen.embedcreator.view;

import javafx.scene.image.ImageView;
import me.ialistannen.embedcreator.util.Webutil;

/**
 * A {@link ImageView} saving the URL of the image it displays
 */
public class UrlImageView extends ImageView {

  private String url;

  /**
   * A barren one without any url
   */
  public UrlImageView() {

  }

  /**
   * @return The URL this {@link ImageView} displays. An empty string if none.
   */
  public String getUrl() {
    return url == null ? "" : url;
  }

  /**
   * Sets the image.
   *
   * @param url The URL to the image
   */
  public void setImage(String url) {
    Webutil.getImage(url).ifPresent(image -> {
      setImage(image);
      this.url = url;
    });
  }
}
