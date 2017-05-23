package me.ialistannen.embedcreator.model;

import java.util.Objects;

/**
 * All variables that are known.
 */
public class Variable {

  private int maxLength;

  private String name;

  /**
   * Creates a new variable with the given name and the max length of 32.
   *
   * @param name The name of the variable
   * @see #Variable(String, int)
   */
  public Variable(String name) {
    this(name, 32);
  }

  /**
   * Creates a new variable with the given name and max length.
   *
   * @param maxLength The maximum length this variable can have.
   * @param name The name of the variable
   */
  public Variable(String name, int maxLength) {
    this.maxLength = maxLength;
    this.name = name;
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

  @Override
  public String toString() {
    return "Variable{"
        + "maxLength=" + maxLength
        + ", name=`" + name + '\''
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
