package me.ialistannen.embedcreator.view;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.TextAlignment;
import me.ialistannen.embedcreator.model.variables.Variable;

/**
 * Shows {@link Variable} sand their description.
 */
class VariableList extends TableView<Variable> {

  private static final Logger LOGGER = Logger.getLogger("VariableList");


  @FXML
  private TableColumn<Variable, String> nameColumn;

  @FXML
  private TableColumn<Variable, Number> maxLengthColumn;

  @FXML
  private TableColumn<Variable, String> descriptionColumn;

  /**
   * Creates a new {@link VariableList}.
   */
  VariableList() {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/fxml/controls/VariableList.fxml")
    );
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Error loading the VariableList FXML file", e);
      throw new RuntimeException("Error loading the VariableList FXML file", e);
    }
  }

  @FXML
  private void initialize() {
    nameColumn.setCellValueFactory(param ->
        new SimpleStringProperty(param.getValue().getName())
    );
    maxLengthColumn.setCellValueFactory(param ->
        new SimpleIntegerProperty(param.getValue().getMaxLength())
    );
    descriptionColumn.setCellValueFactory(param ->
        new SimpleStringProperty(param.getValue().getDescription())
    );

    descriptionColumn.setCellFactory(param -> new TableCell<Variable, String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
          setGraphic(null);
          setText(null);
          return;
        }
        Label label = new Label(item);
        label.setWrapText(true);
        label.setPrefHeight(100);
        label.setMaxHeight(100);

        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(label);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        setPrefHeight(120);
        setMaxHeight(120);
        setGraphic(scrollPane);
      }
    });

    setColumnResizePolicy(param -> true);
    widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
      expandDescriptionColumn();
      Platform.runLater(this::refresh);
    }));
  }

  private void expandDescriptionColumn() {
    AtomicLong width = new AtomicLong();
    getColumns().forEach(col -> width.addAndGet((long) col.getWidth()));
    double tableWidth = getWidth();

    if (tableWidth > width.get()) {
      double descriptionColumnWidth = tableWidth - width.get();
      descriptionColumn.setPrefWidth(descriptionColumnWidth);
    } else {
      double prefWidth = tableWidth - (nameColumn.getWidth() + maxLengthColumn.getWidth());
      descriptionColumn.setPrefWidth(
          prefWidth - getPadding().getLeft() - getPadding().getRight() - 20
      );
    }
  }
}
