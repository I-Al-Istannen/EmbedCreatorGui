package me.ialistannen.embedcreator.validation;

/**
 * An event called when a component is validated.
 */
public class ValidationEvent {

  private CharacterLimit characterLimit;
  private int currentTextLength;
  private ValidationResult result;

  /**
   * @param characterLimit The {@link CharacterLimit} that was used for this event
   * @param currentTextLength The current length of the text
   */
  public ValidationEvent(CharacterLimit characterLimit, int currentTextLength) {
    this.characterLimit = characterLimit;
    this.currentTextLength = currentTextLength;
    this.result = currentTextLength > characterLimit.getMaxSize()
        ? ValidationResult.TOO_LONG
        : ValidationResult.ACCEPTED;
  }

  /**
   * @return The {@link CharacterLimit} that bounded it.
   */
  public CharacterLimit getCharacterLimit() {
    return characterLimit;
  }

  /**
   * @return The {@link ValidationResult}
   */
  public ValidationResult getResult() {
    return result;
  }

  /**
   * @return The full length of the text
   */
  public int getCurrentTextLength() {
    return currentTextLength;
  }

  /**
   * The result of the validation.
   */
  public enum ValidationResult {
    TOO_LONG,
    ACCEPTED
  }
}
