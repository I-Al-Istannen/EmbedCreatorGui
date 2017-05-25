package me.ialistannen.embedcreator.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.ialistannen.embedcreator.util.WebUtil;
import me.ialistannen.embedcreator.variables.VariableRegistry;

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
   * @return The URL this {@link ImageView} displays. An empty string if none. May be a variable in
   * the form of {@code {name}}
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
    if (url.length() >= 3) {
      String name = url.substring(1, url.length() - 1);
      if (VariableRegistry.getVariable(name).isPresent()) {
        setImage(getVariablePlaceholderImage());
        return;
      }
    }
    WebUtil.getImage(url).ifPresent(image -> {
      setImage(image);
      this.url = url;
    });
  }

  private Image getVariablePlaceholderImage() {
    return new Image("/images/abacus.png");
  }
}
