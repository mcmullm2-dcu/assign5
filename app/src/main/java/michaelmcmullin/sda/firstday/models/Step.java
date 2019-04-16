package michaelmcmullin.sda.firstday.models;

/**
 * Class for storing information about a single {@link Step} in a {@link Procedure}.
 */
public class Step {
  private String id;
  private String name;
  private String description;

  /**
   * Firestore requires a constructor with no arguments.
   */
  public Step() {}

  /**
   * Creates an instance of {@link Step} populating its main fields.
   * @param name The name of this {@link Step} instance.
   * @param description A more detailed description of this {@link Step} instance.
   */
  public Step(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Gets the unique ID of this {@link Step} instance.
   * @return The unique ID of this {@link Step} instance.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the unique ID of this {@link Step} instance.
   * @param id The unique ID of this {@link Step} instance.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the name of this {@link Step} instance.
   * @return Returns the name of this {@link Step} instance.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets a more detailed description of this {@link Step} instance.
   * @return Returns a description of this {@link Step} instance.
   */
  public String getDescription() {
    return description;
  }
}
