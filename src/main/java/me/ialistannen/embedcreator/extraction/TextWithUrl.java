package me.ialistannen.embedcreator.extraction;

import java.util.Objects;

/**
 * A text with an url.
 */
public class TextWithUrl {

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TextWithUrl that = (TextWithUrl) o;
    return Objects.equals(getText(), that.getText()) &&
        Objects.equals(getUrl(), that.getUrl());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getText(), getUrl());
  }

  @Override
  public String toString() {
    return "TextWithUrl{"
        + "text='" + text + '\''
        + ", url='" + url + '\''
        + '}';
  }
}
