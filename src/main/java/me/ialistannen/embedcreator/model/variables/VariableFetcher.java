package me.ialistannen.embedcreator.model.variables;

import java.util.Collection;

/**
 * A class that fetches {@link Variable}s from some source.
 */
@FunctionalInterface
public interface VariableFetcher {

  /**
   * Fetches {@link Variable}s.
   * <p>Runs on its own thread.
   *
   * @return The fetched {@link Variable}s
   */
  Collection<Variable> fetch();
}
