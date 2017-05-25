package me.ialistannen.embedcreator.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javafx.scene.image.Image;

/**
 * Some help with web requests.
 */
public class WebUtil {

  /**
   * Returns an {@link java.io.InputStream} for a website.
   *
   * @param url The {@link java.net.URL} to the website
   * @return The input stream for it
   * @throws UnirestException if an error happens during the request.
   */
  public static InputStream getWebsiteStream(String url) throws UnirestException {
    return Unirest.get(url)
        .header("User-Agent", "Mozilla/5.0")
        .asBinary()
        .getBody();
  }

  /**
   * Reads an {@link Image} from an URL.
   *
   * @param url The url to read from
   * @return The read image, if any
   */
  public static Optional<Image> getImage(String url) {
    try (InputStream inputStream = getWebsiteStream(url)) {
      Image image = new Image(inputStream);
      if (image.getException() == null) {
        return Optional.of(image);
      }
    } catch (UnirestException | IOException e) {
      return Optional.empty();
    }
    return Optional.empty();
  }
}
