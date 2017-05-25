package me.ialistannen.embedcreator.extraction;

/**
 * Generates the String for the embed command.
 */
@FunctionalInterface
public interface Generator {

  /**
   * Generates a command string from a given {@link ProviderCollection}.
   *
   * @param data The {@link ProviderCollection} to use
   * @return The generated String
   */
  String generate(ProviderCollection data);
}
