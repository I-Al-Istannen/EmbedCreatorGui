package me.ialistannen.embedcreator.view;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;

/**
 * A URL input dialog
 */
class UrlInputDialog extends TextInputDialog {

  private static final Timer timer = new Timer(true);

  private Button okayButton;

  private LocalDateTime nextCheckTime;


  /**
   * A new {@link UrlInputDialog} without any extra validator.
   */
  UrlInputDialog() {
    okayButton = (Button) getDialogPane().lookupButton(ButtonType.OK);

    setupValidityListener();

    setError();

    setTitle("Please input an URL");
    setHeaderText("Please input an URL");
    setGraphic(new ImageView("/images/icon_url_64.png"));
  }

  /**
   * Creates the listener checking if an input is valid.
   */
  private void setupValidityListener() {
    getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
      // assume change is bad
      setError();
      nextCheckTime = LocalDateTime.now().plus(500, ChronoUnit.MILLIS);

      runAfterDelay(500, () -> {
        // do not flood stuff with requests if the user is still typing
        if (LocalDateTime.now().isBefore(nextCheckTime)) {
          return;
        }

        String text = getEditor().getText();
        if (!isValid(text)) {
          setError();
        } else {
          clearError();
        }
      });
    });
  }

  /**
   * Waits a given time before trying to run something.
   *
   * @param delayMillis The delay in millis to wait
   * @param action The action to run
   */
  @SuppressWarnings("SameParameterValue")
  private void runAfterDelay(long delayMillis, Runnable action) {
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        action.run();
      }
    }, delayMillis);
  }

  private void setError() {
    okayButton.setDisable(true);
  }

  private void clearError() {
    okayButton.setDisable(false);
  }

  /**
   * @param input The input url
   * @return True if it is a valid url
   */
  protected boolean isValid(String input) {
    return isLikelyValidUrl(input);
  }

  /**
   * Tries to find out whether an url is valid.
   *
   * @param input The url
   * @return True if it likely is a valid url.
   */
  private boolean isLikelyValidUrl(String input) {
    URL url;
    try {
      url = new URL(input);
    } catch (MalformedURLException e) {
      return false;
    }

    try {
      url.toURI();
    } catch (URISyntaxException e) {
      return false;
    }

    return true;
  }

  /**
   * Sets the text in the editor field.
   *
   * <p>Will display an empty String when you pass null.
   *
   * @param text The new text in the editor field
   */
  public void setUrl(String text) {
    getEditor().setText(text == null ? "" : text);
  }

}
