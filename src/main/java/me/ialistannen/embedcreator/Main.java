package me.ialistannen.embedcreator;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.ialistannen.embedcreator.model.variables.VariableRegistry;

public class Main extends Application {

  private static Main instance;

  private Stage primaryStage;

  /**
   * Prevent instantiation.
   *
   * <p><em>You should not instantiate it, okay?</em>
   */
  public Main() {
    if (instance != null) {
      throw new IllegalArgumentException("This class should not be instantiated by you!");
    }
    instance = this;
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    this.primaryStage = primaryStage;

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainScreen.fxml"));
    Parent parent = loader.load();

    Scene scene = new Scene(parent);
    primaryStage.setScene(scene);

    primaryStage.centerOnScreen();
    primaryStage.show();

    VariableRegistry.initDefaults();
  }

  /**
   * @return The primary {@link Stage}
   */
  public Stage getPrimaryStage() {
    return primaryStage;
  }

  /**
   * @return The instance of this class
   */
  public static Main getInstance() {
    return instance;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
