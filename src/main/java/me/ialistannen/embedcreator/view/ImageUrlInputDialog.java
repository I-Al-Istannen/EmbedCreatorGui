package me.ialistannen.embedcreator.view;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javafx.scene.image.Image;
import me.ialistannen.embedcreator.util.WebUtil;
import me.ialistannen.embedcreator.variables.Variable;
import me.ialistannen.embedcreator.variables.VariableRegistry;

/**
 * A Dialog to ask for an {@link java.net.URL} pointing to an image.
 */
public class ImageUrlInputDialog extends UrlInputDialog {

  /**
   * A new {@link ImageUrlInputDialog}.
   */
  public ImageUrlInputDialog() {
    setHeaderText("Please input an URL for your image.");
    setTitle("Image URL");
  }

  @Override
  protected boolean isValid(String input) {
    return isImageVariable(input) || super.isValid(input) && isValidImageUrl(input);
  }

  private boolean isImageVariable(String input) {
    if (input.length() >= 3) {
      String variableName = input.substring(1, input.length() - 1);
      Optional<Variable> variableOptional = VariableRegistry.getVariable(variableName);
      if (variableOptional.isPresent()) {
        return variableOptional.get().isPicture();
      }
    }
    return false;
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
