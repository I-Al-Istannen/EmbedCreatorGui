package me.ialistannen.embedcreator.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Stores all known {@link Variable}s.
 */
public class VariableRegistry {

  private static final Map<String, Variable> variables = new HashMap<>();

  /**
   * Adds a {@link Variable} to the store.
   *
   * @param variable The {@link Variable} to add
   */
  public static void addVariable(Variable variable) {
    variables.put(variable.getName(), variable);
  }

  /**
   * Removes a {@link Variable} from the store.
   *
   * @param variable The {@link Variable} to remove
   */
  public static void removeVariable(Variable variable) {
    variables.remove(variable.getName());
  }

  /**
   * Checks if there is a {@link Variable} with that name.
   *
   * @param name The name of the {@link Variable}.
   * @return True if there is a {@link Variable} with that name.
   */
  public static boolean hasVariable(String name) {
    return variables.containsKey(name);
  }

  /**
   * Finds a {@link Variable} by name.
   *
   * @param name The name of the {@link Variable}.
   * @return The found {@link Variable}, if any.
   */
  public static Optional<Variable> getVariable(String name) {
    return Optional.ofNullable(variables.get(name));
  }

  /**
   * @return All {@link Variable}s
   */
  public static Set<Variable> getAllVariables() {
    return new HashSet<>(variables.values());
  }
}
