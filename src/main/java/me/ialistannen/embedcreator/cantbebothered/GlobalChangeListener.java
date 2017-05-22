package me.ialistannen.embedcreator.cantbebothered;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A static change listener.
 */
public class GlobalChangeListener {

  private static final GlobalChangeListener instance = new GlobalChangeListener();

  private Set<Runnable> listener = Collections.newSetFromMap(new ConcurrentHashMap<>());

  /**
   * Notifies all Listeners that a change occurred.
   */
  public void notifyOfChange() {
    listener.forEach(Runnable::run);
  }

  /**
   * Adds a listener.
   *
   * @param listener The listener to add
   */
  public void addListener(Runnable listener) {
    this.listener.add(listener);
  }

  /**
   * @return The {@link GlobalChangeListener} instance.
   */
  public static synchronized GlobalChangeListener getInstance() {
    return instance;
  }
}
