package me.ialistannen.embedcreator.util;

import java.util.function.Supplier;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import me.ialistannen.embedcreator.Main;

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

  /**
   * Shows a waiting animation.
   *
   * @return The created {@link Window}
   */
  public static Window showWaitingAnimation() {
    Stage stage = new Stage(StageStyle.TRANSPARENT);

    ProgressIndicator progressIndicator = new ProgressIndicator();
    progressIndicator.setMaxSize(200, 200);
    BorderPane rootPane = new BorderPane();

    progressIndicator.setBackground(null);
    rootPane.setBackground(null);
    rootPane.setCenter(progressIndicator);

    Scene scene = new Scene(rootPane);
    scene.setFill(new Color(0, 0, 0, 0.5));

    stage.setScene(scene);

    Stage primaryStage = Main.getInstance().getPrimaryStage();

    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(primaryStage);

    stage.setWidth(primaryStage.getWidth());
    stage.setHeight(primaryStage.getHeight());
    stage.setX(primaryStage.getX());
    stage.setY(primaryStage.getY());

    stage.show();

    return stage;
  }
}
