package me.ialistannen.embedcreator.variables;

import java.util.Objects;

/**
 * All variables that are known.
 */
public class Variable {

  private int maxLength;

  private String name;
  private String description;

  /**
   * Creates a new variable with the given name and max length.
   *
   * <p>Default description is a placeholder.</p>
   *
   * @param maxLength The maximum length this variable can have.
   * @param name The name of the variable
   * @see #Variable(String, int, String)
   */
  public Variable(String name, int maxLength) {
    this(name, maxLength, "Placeholder");
  }

  /**
   * Creates a new variable with the given name and max length.
   *
   * @param maxLength The maximum length this variable can have.
   * @param name The name of the variable
   * @param description The description of the variable
   */
  public Variable(String name, int maxLength, String description) {
    this.maxLength = maxLength;
    this.name = name;
    this.description = description;
  }

  /**
   * @return The maximum length of the variable
   */
  public int getMaxLength() {
    return maxLength;
  }

  /**
   * @return The name for the variable
   */
  public String getName() {
    return name;
  }

  /**
   * @return The description of the variable
   */
  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return "Variable{"
        + "maxLength=" + maxLength
        + ", name='" + name + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Variable variable = (Variable) o;
    return getMaxLength() == variable.getMaxLength() &&
        Objects.equals(getName(), variable.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getMaxLength(), getName());
  }
}
