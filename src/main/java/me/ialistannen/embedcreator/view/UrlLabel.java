package me.ialistannen.embedcreator.view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import me.ialistannen.embedcreator.util.Util;

/**
 * A {@link Label} storing information about the URL it was assigned.
 */
public class UrlLabel extends EditableTextFlow {

  private String url;

  /**
   * Creates an empty label
   */
  public UrlLabel() {
    setupContextMenu();
  }

  /**
   * Creates and assigns the {@link ContextMenu}.
   */
  private void setupContextMenu() {
    Util.setContextMenu(this, () -> {
      MenuItem editUrl = new MenuItem("Edit URL");
      editUrl.setOnAction(event -> {
        UrlInputDialog urlInputDialog = new UrlInputDialog();
        urlInputDialog.setUrl(url);
        Optional<String> newUrl = urlInputDialog.showAndWait();
        newUrl.ifPresent(this::setUrl);
      });

      ContextMenu contextMenu = new ContextMenu(editUrl);

      if (url != null && Desktop.isDesktopSupported()) {
        MenuItem gotoUrl = new MenuItem("Go to URL");
        gotoUrl.setOnAction(event -> {
          try {
            Desktop.getDesktop().browse(new URI(url));
          } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
          }
        });

        contextMenu.getItems().add(gotoUrl);
      }
      return contextMenu;
    });
  }

  /**
   * @param url The new url.
   */
  private void setUrl(String url) {
    this.url = url;
    setupContextMenu();
  }
}
