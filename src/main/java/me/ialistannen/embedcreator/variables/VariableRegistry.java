package me.ialistannen.embedcreator.variables;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.stage.Window;
import me.ialistannen.embedcreator.util.Util;
import me.ialistannen.embedcreator.variables.fetcher.GithubXaanitFetcher;

/**
 * Stores all known {@link Variable}s.
 */
public class VariableRegistry {

  private static final Logger LOGGER = Logger.getLogger("VariableRegistry");
  private static final ExecutorService executor = Executors.newCachedThreadPool(r -> {
    Thread thread = Executors.defaultThreadFactory().newThread(r);
    thread.setDaemon(true);
    return thread;
  });
  private static final Map<String, Variable> variables = new ConcurrentHashMap<>();

  /**
   * Adds the default variables.
   */
  public static void initDefaults() {
    addVariables(new GithubXaanitFetcher());
  }

  /**
   * Adds a {@link Variable} to the store.
   *
   * @param variable The {@link Variable} to add
   */
  @SuppressWarnings("WeakerAccess")
  public static void addVariable(Variable variable) {
    variables.put(variable.getName(), variable);
  }

  /**
   * Adds all {@link Variable}s from the {@link VariableFetcher}.
   *
   * @param fetcher The {@link VariableFetcher} to fetch from
   */
  @SuppressWarnings("WeakerAccess")
  public static void addVariables(VariableFetcher fetcher) {
    Future<?> future = executor.submit(() -> {
      for (Variable variable : fetcher.fetch()) {
        addVariable(variable);
      }
    });

    Window window = Util.showWaitingAnimation("Loading available variables...");
    CompletableFuture.runAsync(() -> {
      try {
        future.get();
      } catch (InterruptedException | ExecutionException e) {
        LOGGER.log(Level.WARNING, "Error fetching variables: " + fetcher, e);
      }
    }).thenRun(() -> Platform.runLater(window::hide));
  }

  /**
   * Removes a {@link Variable} from the store.
   *
   * @param variable The {@link Variable} to remove
   */
  @SuppressWarnings("unused")
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
