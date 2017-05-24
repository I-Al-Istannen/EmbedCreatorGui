package me.ialistannen.embedcreator.extraction;

import java.util.List;
import javafx.scene.paint.Color;
import me.ialistannen.embedcreator.Main;
import me.ialistannen.embedcreator.controller.MainScreenController;
import me.ialistannen.embedcreator.view.EmbedField;

/**
 * A class holding Data for the {@link Generator}.
 */
public class GeneratorData {

  private String authorImage;
  private TextWithUrl author;
  private String thumbnail;
  private TextWithUrl title;
  private String description;
  private String image;
  private List<EmbedField> fieldList;
  private String footerImage;
  private String footer;
  private Color sidebarColor;

  private GeneratorData(String authorImage, TextWithUrl author,
      String thumbnail,
      TextWithUrl title, String description, String image,
      List<EmbedField> fieldList,
      String footerImage, String footer,
      Color sidebarColor) {
    this.authorImage = authorImage;
    this.author = author;
    this.thumbnail = thumbnail;
    this.title = title;
    this.description = description;
    this.image = image;
    this.fieldList = fieldList;
    this.footerImage = footerImage;
    this.footer = footer;
    this.sidebarColor = sidebarColor;
  }

  public String getAuthorImage() {
    return authorImage;
  }

  public TextWithUrl getAuthor() {
    return author;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public TextWithUrl getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getImage() {
    return image;
  }

  public List<EmbedField> getFieldList() {
    return fieldList;
  }

  public String getFooterImage() {
    return footerImage;
  }

  public String getFooter() {
    return footer;
  }

  public Color getSidebarColor() {
    return sidebarColor;
  }

  public static GeneratorData fromMain() {
    MainScreenController mainScreenController = Main.getInstance().getMainScreenController();
    return new GeneratorData(
        mainScreenController.getHeaderImage(),
        mainScreenController.getAuthorName(),
        mainScreenController.getThumbnailImage(),
        mainScreenController.getTitle(),
        mainScreenController.getDescription(),
        mainScreenController.getImage(),
        mainScreenController.getFields(),
        mainScreenController.getFooterImage(),
        mainScreenController.getFooterText(),
        Color.ROYALBLUE
    );
  }

  /**
   * A text with an url.
   */
  public static class TextWithUrl {

    private String text;
    private String url;

    /**
     * @param text The text
     * @param url The url
     */
    public TextWithUrl(String text, String url) {
      this.text = text;
      this.url = url;
    }

    /**
     * @return The text
     */
    public String getText() {
      return text;
    }

    /**
     * @return The URL
     */
    public String getUrl() {
      return url;
    }
  }
}
