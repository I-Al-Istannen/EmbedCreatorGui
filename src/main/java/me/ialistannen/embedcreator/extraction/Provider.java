package me.ialistannen.embedcreator.extraction;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Provides some data.
 *
 * @param <T> The type of the data it provides
 */
public interface Provider<T> {

  /**
   * Returns the value for this provider.
   *
   * @return The value (s) if any. Empty list if none.
   */
  List<T> get();

  /**
   * Returns the return type class of this {@link Provider}'s {@link #get()} method.
   *
   * @return The class of this {@link Provider}.
   */
  Class<T> getResultClass();

  /**
   * Returns a new Provider for a given {@link Class}.
   *
   * @param resultClass The class of the result type
   * @param supplier The supplier to get it from
   * @param <T> The type of the {@link Provider}
   * @return A {@link Provider} using the information outlined above.
   */
  static <T> Provider<T> ofType(Class<T> resultClass, Supplier<List<T>> supplier) {
    return new Provider<T>() {
      @Override
      public List<T> get() {
        return supplier.get();
      }

      @Override
      public Class<T> getResultClass() {
        return resultClass;
      }
    };
  }

  /**
   * Returns a new {@link Provider} returning a single instance of a given {@link Class}.
   *
   * @param resultClass The class of the result type
   * @param supplier The {@link Supplier} to get it from
   * @param <T> The type of the {@link Provider}
   * @return A {@link Provider} using the information outlined above
   */
  static <T> Provider<T> ofSingleType(Class<T> resultClass, Supplier<T> supplier) {
    return ofType(resultClass, () -> Collections.singletonList(supplier.get()));
  }
}
