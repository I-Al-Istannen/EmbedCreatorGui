package me.ialistannen.embedcreator.util;

import java.util.function.Supplier;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;

/**
 * Some misc utility methods.
 */
public class Util {

  /**
   * Adds a {@link ContextMenu} to a {@link Node}.
   *
   * @param node The {@link Node} to attach it to
   * @param contextMenuSupplier The {@link Supplier} to generate a {@link ContextMenu} to attach
   */
  public static void setContextMenu(Node node, Supplier<ContextMenu> contextMenuSupplier) {
    node.setOnContextMenuRequested(event -> {
      if (node.getUserData() != null) {
        return;
      }
      node.setUserData("opened");

      ContextMenu contextMenu = contextMenuSupplier.get();
      contextMenu.setAutoHide(true);
      contextMenu.setOnHidden(hidden -> node.setUserData(null));

      contextMenu.show(node, event.getScreenX(), event.getScreenY());
    });
  }
}
