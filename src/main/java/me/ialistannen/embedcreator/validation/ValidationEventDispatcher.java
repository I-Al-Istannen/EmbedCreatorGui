package me.ialistannen.embedcreator.validation;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * The dispatcher for ValidationEvent s
 */
public class ValidationEventDispatcher {

  private Set<Consumer<ValidationEvent>> listeners = Collections.newSetFromMap(
      new ConcurrentHashMap<>()
  );

  /**
   * Adds an event listener.
   *
   * @param listener The listener to add
   */
  public void addListener(Consumer<ValidationEvent> listener) {
    listeners.add(listener);
  }

  /**
   * Dispatches a {@link ValidationEvent} to all listeners.
   *
   * @param event The {@link ValidationEvent} to dispatch.
   */
  public void dispatchEvent(ValidationEvent event) {
    for (Consumer<ValidationEvent> listener : listeners) {
      listener.accept(event);
    }
  }
}
