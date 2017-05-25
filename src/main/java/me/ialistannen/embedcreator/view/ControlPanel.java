package me.ialistannen.embedcreator.view;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import me.ialistannen.embedcreator.controller.MainScreenController;
import me.ialistannen.embedcreator.extraction.Generator;
import me.ialistannen.embedcreator.extraction.ProviderCollection;
import me.ialistannen.embedcreator.extraction.ProviderCollection.ProviderType;
import me.ialistannen.embedcreator.validation.CharacterLimit;
import me.ialistannen.embedcreator.validation.ValidationEvent;
import me.ialistannen.embedcreator.validation.ValidationEvent.ValidationResult;
import me.ialistannen.embedcreator.variables.VariableRegistry;

/**
 * The control panel.
 */
public class ControlPanel extends BorderPane implements Consumer<ValidationEvent> {

  @FXML
  private Label statusValue;

  @FXML
  private Button addField;

  @FXML
  private Button generateButton;

  private MainScreenController mainScreenController;
  private Generator generator = new DebugPrintingGenerator();
  private ProviderCollection providerCollection;

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
  void onViewAllVariables(ActionEvent event) {
    VariableList variableList = new VariableList();
    variableList.getItems().addAll(VariableRegistry.getAllVariables());

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Variable list");
    alert.setHeaderText("Here is a list with all Variables");
    variableList.setPadding(new Insets(20));
    alert.getDialogPane().setContent(variableList);

    alert.setResizable(false);

    alert.show();
  }

  @FXML
  void onGenerate(ActionEvent event) {
    System.out.println("Generating...");
    System.out.println(generator.generate(providerCollection));
  }

  @FXML
  void onExit(ActionEvent event) {
    System.exit(0);
  }

  /**
   * Sets the {@link MainScreenController} this {@link ControlPanel} should use.
   *
   * @param mainScreenController The {@link MainScreenController}
   */
  public void setMainScreenController(MainScreenController mainScreenController) {
    this.mainScreenController = mainScreenController;
  }

  /**
   * @param providerCollection The {@link ProviderCollection} to use.
   */
  public void setProviderCollection(ProviderCollection providerCollection) {
    this.providerCollection = providerCollection;
  }

  @Override
  public void accept(ValidationEvent event) {
    if (event.getResult() == ValidationResult.ACCEPTED) {
      clearStatus();
    } else {
      setErrorStatus(event.getCharacterLimit(), event.getCurrentTextLength());
    }
  }

  private void clearStatus() {
    statusValue.setText("Okay!");
    statusValue.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), false);
  }

  private void setErrorStatus(CharacterLimit limit, int length) {
    String name = enumFormat(limit);
    String message = "'" + name + "' may be bigger than the limit, depending on e.g. variables."
        + "\n\nSize: " + length + " / " + limit.getMaxSize();

    statusValue.setText(message);

    statusValue.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), true);
  }

  private String enumFormat(Enum<?> enumEntry) {
    String name = enumEntry.name();
    StringBuilder output = new StringBuilder();
    boolean upperCase = true;

    for (char c : name.toCharArray()) {
      if (c == '_') {
        upperCase = true;
        output.append(" ");
        continue;
      }
      if (upperCase) {
        output.append(Character.toUpperCase(c));
        upperCase = false;
      } else {
        output.append(Character.toLowerCase(c));
      }
    }

    return output.toString();
  }

  private static class DebugPrintingGenerator implements Generator {

    @Override
    public String generate(ProviderCollection data) {
      for (ProviderType providerType : ProviderType.values()) {
        System.out.println(providerType.name() + ": " + data.get(providerType));
      }
      return "N/A";
    }
  }
}
