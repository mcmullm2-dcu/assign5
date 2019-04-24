package michaelmcmullin.sda.firstday.interfaces;

/**
 * Interface used by classes that have defined ways to get and set their data in a form that other
 * classes can make use of.
 * @param <T> The type of data available to store and retrieve.
 */
public interface GetterSetter<T> {

  /**
   * Persists its data in a form that can be read by other classes.
   */
  void SetData();

  /**
   * Gets the persisted data.
   * @return The persisted data, or null.
   */
  T GetData();
}
