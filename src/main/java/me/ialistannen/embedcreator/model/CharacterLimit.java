package me.ialistannen.embedcreator.model;

/**
 * The character limits for the individual parts.
 */
public enum CharacterLimit {

  WHOLE_EMBED(4000),
  // FIXME: 19.05.17 is not known
  /**
   * The value is actually not known to me.
   */
  AUTHOR(1024),
  TITLE(256),
  FIELD_NAME(256),
  FIELD_VALUE(1024),
  DESCRIPTION(2048),
  FOOTER(2048);

  private int maxSize;

  CharacterLimit(int maxSize) {
    this.maxSize = maxSize;
  }

  /**
   * @return The max size for this part.
   */
  public int getMaxSize() {
    return maxSize;
  }
}
