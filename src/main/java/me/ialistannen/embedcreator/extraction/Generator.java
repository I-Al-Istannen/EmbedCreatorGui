package me.ialistannen.embedcreator.extraction;

/**
 * Generates the String for the embed command.
 */
@FunctionalInterface
public interface Generator {

  /**
   * Converts a {@link GeneratorData} object to a command String.
   *
   * @param data The {@link GeneratorData} to use
   * @return The generated String
   */
  String generate(GeneratorData data);
}
