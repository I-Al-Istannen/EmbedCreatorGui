package me.ialistannen.embedcreator.extraction;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ialistannen.embedcreator.view.EmbedField;

/**
 * Manages the {@link Provider}'s
 */
public class ProviderManager {

  private static final NopProvider NOP_PROVIDER = new NopProvider();

  private Map<ProviderType, Provider<?>> providers = new HashMap<>();

  /**
   * Adds a {@link Provider}.
   *
   * @param type The type of the {@link Provider} to add
   * @param provider The {@link Provider} to add
   * @throws IllegalArgumentException if {@link ProviderType#getResultClass()} is not assignable
   * from {@link Provider#getResultClass()}
   */
  public void addProvider(ProviderType type, Provider<?> provider) {
    if (!type.getResultClass().isAssignableFrom(provider.getResultClass())) {
      throw new IllegalArgumentException(
          "Provider of wrong type. "
              + "Expected '" + type.getResultClass()
              + "' got '" + provider.getResultClass() + "'"
      );
    }
    providers.put(type, provider);
  }

  /**
   * Returns everything a given {@link Provider} returns.
   *
   * @param type The {@link ProviderType} to use
   * @param <T> The type of the return value. Instance of whatever {@link
   * ProviderType#getResultClass()} returns
   * @return A List with everything the {@link Provider} returned. May be empty.
   * @throws ClassCastException if not all elements the Provider returned are of type {@link
   * ProviderType#getResultClass()}.
   */
  public <T> List<T> get(ProviderType type) {
    List<?> objects = providers.getOrDefault(type, NOP_PROVIDER).get();

    // better to throw that error here
    for (Object object : objects) {
      if (object == null) {
        continue;
      }
      if (!type.getResultClass().isAssignableFrom(object.getClass())) {
        throw new ClassCastException("'" + object + "' is not of type " + type.getResultClass());
      }
    }

    @SuppressWarnings("unchecked")
    List<T> casted = (List<T>) objects;
    return casted;
  }

  @Override
  public String toString() {
    return "ProviderManager{"
        + "providers=" + providers
        + '}';
  }

  /**
   * The different types of {@link Provider}s.
   */
  public enum ProviderType {
    AUTHOR_TEXT(TextWithUrl.class),
    AUTHOR_IMAGE(String.class),
    THUMBNAIL_IMAGE(String.class),
    TITLE(TextWithUrl.class),
    DESCRIPTION(String.class),
    FIELDS(EmbedField.class),
    IMAGE(String.class),
    FOOTER_IMAGE(String.class),
    FOOTER_TEXT(String.class);

    private Class<?> resultClass;

    ProviderType(Class<?> resultClass) {
      this.resultClass = resultClass;
    }

    /**
     * @return The type of the elements the provider returns
     */
    public Class<?> getResultClass() {
      return resultClass;
    }
  }

  /**
   * A provider returning nothing
   */
  private static class NopProvider implements Provider<Object> {

    @Override
    public List<Object> get() {
      return Collections.emptyList();
    }

    @Override
    public Class<Object> getResultClass() {
      return Object.class;
    }
  }
}
