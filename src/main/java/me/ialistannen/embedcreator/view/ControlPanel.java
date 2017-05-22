package me.ialistannen.embedcreator.view;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import me.ialistannen.embedcreator.cantbebothered.GlobalChangeListener;
import me.ialistannen.embedcreator.controller.MainScreenController;
import me.ialistannen.embedcreator.model.CharacterLimit;

/**
 * The control panel.
 */
public class ControlPanel extends BorderPane {

  @FXML
  private Label statusValue;

  @FXML
  private Button addField;

  @FXML
  private Button generateButton;


  private MainScreenController mainScreenController;

  /**
   * Creates a new ControlPanel.
   */
  public ControlPanel() {
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getResource("/fxml/controls/ControlPanel.fxml")
    );
    fxmlLoader.setController(this);
    fxmlLoader.setRoot(this);

    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException("Error loading Control panel", e);
    }
  }

  @FXML
  private void initialize() {
    GlobalChangeListener.getInstance().addListener(
        () -> Platform.runLater(this::recalculateStatus)
    );
  }

  /**
   * Recalculate and set the status label.
   */
  private void recalculateStatus() {
    if (mainScreenController.getWholeTextLength() > CharacterLimit.WHOLE_EMBED.getMaxSize()) {
      statusValue.setText("Embed length must be <= " + CharacterLimit.WHOLE_EMBED.getMaxSize());
      statusValue.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), true);
    } else {
      statusValue.setText("Okay");
      statusValue.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), false);
    }
  }

  @FXML
  void onAddField(ActionEvent event) {
    ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(
        "Inline", "Inline", "Block"
    );
    choiceDialog.setTitle("What do you want?");
    choiceDialog.setHeaderText("What type of field do you want?\n"
        + "You can change it via right click later on.");
    Optional<String> fieldType = choiceDialog.showAndWait();
    fieldType.ifPresent(string -> {
      boolean inline = string.equalsIgnoreCase("Inline");
      mainScreenController.addDummyField(inline);
    });
  }

  @FXML
  void onExit(ActionEvent event) {
    System.exit(0);
  }

  @FXML
  void onGenerate(ActionEvent event) {
    System.out.println("Generating...");
  }

  /**
   * Sets the {@link MainScreenController} this {@link ControlPanel} should use.
   *
   * @param mainScreenController The {@link MainScreenController}
   */
  public void setMainScreenController(MainScreenController mainScreenController) {
    this.mainScreenController = mainScreenController;
  }
}
