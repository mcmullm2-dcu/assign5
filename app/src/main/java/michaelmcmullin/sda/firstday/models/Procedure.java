package michaelmcmullin.sda.firstday.models;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

/**
 * Class for storing information about a single {@link Procedure} instance.
 */
public class Procedure {

  /**
   * Unique ID of this {@link Procedure} instance.
   */
  private String id;

  /**
   * The name of this {@link Procedure} instance.
   */
  private String name;

  /**
   * A description or summary of this {@link Procedure} instance.
   */
  private String description;

  /**
   * The {@link User} who owns this {@link Procedure} instance.
   */
  private User owner;

  /**
   * The timestamp of when this {@link Procedure} instance was first created.
   */
  private Date created;

  /**
   * Flag indicating if this {@link Procedure} is publicly accessible, or only available users who
   * have been explicitly given access.
   */
  private boolean is_public;

  /**
   * Flag indicating if this {@link Procedure} is still a draft, or only available to its owner.
   */
  private boolean is_draft;

  /**
   * Firestore requires a constructor with no arguments.
   */
  public Procedure() {}

  /**
   * Creates a new instance of {@link Procedure} setting all the main fields.
   * @param name The name of this {@link Procedure} instance.
   * @param description The description or summary of this {@link Procedure} instance.
   * @param owner The {@link User} who owns this {@link Procedure} instance.
   * @param created The timestamp indicating when this {@link Procedure} was first created.
   * @param is_public Indicates if this {@link Procedure} instance is publicly available.
   * @param is_draft Indicates if this {@link Procedure} instance is a draft copy.
   */
  public Procedure(String name, String description, User owner, Date created, boolean is_public, boolean is_draft) {
    this.name = name;
    this.description = description;
    this.owner = owner;
    this.created = created;
    this.is_public = is_public;
    this.is_draft = is_draft;
  }

  /**
   * Creates a new instance of {@link Procedure} setting the name and description.
   * @param name The name of this {@link Procedure} instance.
   * @param description The description or summary of this {@link Procedure} instance.
   */
  public Procedure(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Gets the unique ID of this {@link Procedure} instance.
   * @return Returns the unique ID of this {@link Procedure} instance.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the unique ID of this {@link Procedure} instance.
   * @param id The unique ID to assign to this {@link Procedure} instance.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the name of this {@link Procedure} instance.
   * @return Returns the name of this {@link Procedure} instance.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the description, or summary, of this {@link Procedure} instance.
   * @return Returns the description of this {@link Procedure} instance.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the {@link User} who owns this {@link Procedure} instance.
   * @return Returns the {@link User} who owns this {@link Procedure} instance.
   */
  public User getOwner() {
    return owner;
  }

  /**
   * Gets the timestamp indicating when this {@link Procedure} instance was first created.
   * @return A timestamp indicating when this {@link Procedure} instance was created.
   */
  @ServerTimestamp
  public Date getCreated() {
    return created;
  }

  /**
   * Gets a value indicating if this {@link Procedure} instance is publicly available.
   * @return Returns <code>true</code> if this {@link Procedure} instance is available to the public.
   */
  public boolean isPublic() {
    return is_public;
  }

  /**
   * Gets a value indicating if this {@link Procedure} instance is a draft.
   * @return Returns <code>true</code> if this {@link Procedure} instance is a draft.
   */
  public boolean isDraft() {
    return is_draft;
  }

  /**
   * Indicates if this is a new procedure (i.e. it has no ID set).
   * @return Returns <code>true</code> if this {@link Procedure} instance is a new procedure.
   */
  public boolean isNew() {
    return (this.id == null || this.id.isEmpty());
  }
}
