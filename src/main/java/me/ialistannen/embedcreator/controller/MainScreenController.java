package me.ialistannen.embedcreator.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import me.ialistannen.embedcreator.Main;
import me.ialistannen.embedcreator.util.ImageUtil;
import me.ialistannen.embedcreator.util.LabeledUtil;
import me.ialistannen.embedcreator.view.EditableTextFlow;
import me.ialistannen.embedcreator.view.FieldContainer;
import me.ialistannen.embedcreator.view.UrlImageView;
import me.ialistannen.embedcreator.view.UrlLabel;

/**
 * A controller for the Main scene
 */
public class MainScreenController {

  @FXML
  private UrlImageView thumbnailImage;

  @FXML
  private UrlImageView authorAvatarImage;

  @FXML
  private Label authorName;

  @FXML
  private UrlLabel title;

  @FXML
  private EditableTextFlow descriptionText;

  @FXML
  private FieldContainer fieldContainer;

  @FXML
  private UrlImageView image;

  @FXML
  private UrlImageView footerImage;

  @FXML
  private Label footerText;

  @FXML
  private VBox test;

  @FXML
  private void initialize() {
    setClips();

    initFields();

    ImageUtil.makeEditable(authorAvatarImage);
    ImageUtil.makeEditable(thumbnailImage);
    ImageUtil.makeEditable(image);
    ImageUtil.makeEditable(footerImage);

    LabeledUtil.makeEditable(authorName);
    LabeledUtil.makeEditable(footerText);
    LabeledUtil.makeEditable(title);

    descriptionText.getChildren().clear();
    addText("Welcome", false);
    addText(" {player} ", true);
    addText("to the server!", false);
  }

  private void addText(String text, boolean variable) {
    Text textNode = new Text(text);
    if(variable) {
      textNode.getStyleClass().add("variable");
    }
    descriptionText.getChildren().add(textNode);
  }

  /**
   * Sets the {@link Node#setClip(Node)} values.
   */
  private void setClips() {
    authorAvatarImage.setClip(getHeaderFooterClip());
    footerImage.setClip(getHeaderFooterClip());

    thumbnailImage.setClip(getRoundedEdgesClip(thumbnailImage));
    image.setClip(getRoundedEdgesClip(image));
  }

  /**
   * Returns the clip for the header and footer image.
   *
   * @return The {@link Node#setClip(Node)} for the Header and footer image
   */
  private Shape getHeaderFooterClip() {
    Circle circle = new Circle(10);
    circle.setLayoutY(10);
    circle.setLayoutX(10);
    return circle;
  }

  /**
   * Returns a clip that gives the specified node round edges.
   *
   * @param imageView The imageView to gift rounded edges to.
   * @return The {@link Node#setClip(Node)} for it
   */
  private Shape getRoundedEdgesClip(ImageView imageView) {
    Rectangle rectangle = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
    rectangle.setArcHeight(30);
    rectangle.setArcWidth(30);
    return rectangle;
  }

  /**
   * Creates the example field
   */
  private void initFields() {
    Timeline timeline = new Timeline();
    timeline.getKeyFrames().add(new KeyFrame(
            Duration.seconds(1),
            event -> {
              addNewExampleField(Math.random() < 0.8);

              // love you! Recalculating the layout at runtime is always nice
              Main.getInstance().getPrimaryStage().sizeToScene();
              Main.getInstance().getPrimaryStage().centerOnScreen();
            }
        )
    );
    timeline.setCycleCount(10);
    timeline.play();
  }

  private void addNewExampleField(boolean inline) {
    fieldContainer.addNewField("Test field name", "Test field value", inline);
  }
}
