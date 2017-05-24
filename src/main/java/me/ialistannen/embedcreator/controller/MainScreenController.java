package me.ialistannen.embedcreator.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import me.ialistannen.embedcreator.extraction.GeneratorData.TextWithUrl;
import me.ialistannen.embedcreator.model.CharacterLimit;
import me.ialistannen.embedcreator.util.ImageUtil;
import me.ialistannen.embedcreator.view.ControlPanel;
import me.ialistannen.embedcreator.view.EditableTextFlow;
import me.ialistannen.embedcreator.view.EmbedField;
import me.ialistannen.embedcreator.view.FieldContainer;
import me.ialistannen.embedcreator.view.UrlImageView;
import me.ialistannen.embedcreator.view.UrlLabel;

/**
 * A controller for the Main scene
 */
public class MainScreenController {

  @FXML
  public ControlPanel controlPanel;

  @FXML
  public GridPane rootPane;

  @FXML
  private UrlImageView thumbnailImage;

  @FXML
  private UrlImageView authorAvatarImage;

  @FXML
  private UrlLabel authorName;

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
  private EditableTextFlow footerText;

  @FXML
  private void initialize() {
    setClips();

    ImageUtil.makeEditable(authorAvatarImage);
    ImageUtil.makeEditable(thumbnailImage);
    ImageUtil.makeEditable(image);
    ImageUtil.makeEditable(footerImage);

    controlPanel.setMainScreenController(this);

    descriptionText.setCharacterLimit(CharacterLimit.DESCRIPTION);
    footerText.setCharacterLimit(CharacterLimit.FOOTER);
    authorName.setCharacterLimit(CharacterLimit.AUTHOR);
    title.setCharacterLimit(CharacterLimit.TITLE);
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
   * Returns the length of the full embed text.
   *
   * @return The lenght of the whole text
   */
  public int getWholeTextLength() {
    return getChildrenRecursive(rootPane).stream()
        .filter(node -> node instanceof EditableTextFlow)
        .map(node -> (EditableTextFlow) node)
        .map(EditableTextFlow::getLengthResolveVariables)
        .reduce(Math::addExact)
        .orElse(0);
  }

  /**
   * Returns all children and their children and so on.
   *
   * @param node The Node to get all children for
   * @return All children and their children
   */
  private List<Node> getChildrenRecursive(Node node) {
    if (!(node instanceof Parent)) {
      return Collections.singletonList(node);
    }
    List<Node> children = new ArrayList<>();
    for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
      children.add(child);
      children.addAll(getChildrenRecursive(child));
    }
    return children;
  }

  /**
   * Adds a new dummy field.
   *
   * @param inline Whether it is an inline field.
   */
  public void addDummyField(boolean inline) {
    fieldContainer.addNewField("This is the field name", "And the value", inline);
  }

  public String getHeaderImage() {
    return authorAvatarImage.getUrl();
  }

  public TextWithUrl getAuthorName() {
    return new TextWithUrl(authorName.getText(), authorName.getUrl());
  }

  public String getFooterImage() {
    return footerImage.getUrl();
  }

  public String getFooterText() {
    return footerText.getText();
  }

  public String getThumbnailImage() {
    return thumbnailImage.getUrl();
  }

  public TextWithUrl getTitle() {
    return new TextWithUrl(title.getText(), title.getUrl());
  }

  public String getDescription() {
    return descriptionText.getText();
  }

  public String getImage() {
    return image.getUrl();
  }

  public List<EmbedField> getFields() {
    return fieldContainer.getFields();
  }
}
