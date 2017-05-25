package me.ialistannen.embedcreator.extraction;

/**
 * Generates the String for the embed command.
 */
@FunctionalInterface
public interface Generator {

  /**
   * Generates a command string from a given {@link ProviderManager}.
   *
   * @param data The {@link ProviderManager} to use
   * @return The generated String
   */
  String generate(ProviderManager data);
}
