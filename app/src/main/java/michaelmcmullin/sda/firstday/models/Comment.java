package michaelmcmullin.sda.firstday.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;

/**
 * Class to store information about a {@link Comment} made about a {@link Procedure}.
 */
public class Comment {

  /**
   * Unique ID of this {@link Comment} instance.
   */
  private String id;

  /**
   * The {@link User} who created this {@link Comment} instance.
   */
  private User author;

  /**
   * The timestamp when this {@link Comment} instance was created.
   *
   * The technique for handling Firebase Timestamps is based on code found on StackOverflow:
   * https://stackoverflow.com/a/33111791/5233918
   * Author: Lyla (https://stackoverflow.com/users/497605/lyla)
   */
  private HashMap<String, Object> created;

  /**
   * The content of this {@link Comment} instance.
   */
  private String message;

  /**
   * Firestore requires a constructor with no arguments.
   */
  public Comment() {}

  /**
   * Creates an instance of {@link Comment} populating its fields.
   * @param author The {@link User} who created this {@link Comment} instance.
   * @param created The Timestamp indicating when this {@link Comment} instance was created.
   * @param message The content of this {@link Comment} instance.
   */
  public Comment(User author, HashMap<String, Object> created, String message) {
    this.author = author;
    this.created = created;
    this.message = message;
  }

  /**
   * Gets the unique ID of this {@link Comment} instance.
   * @return Returns the unique ID of this {@link Comment} instance.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the unique ID of this {@link Comment} instance.
   * @param id The unique ID of this {@link Comment} instance.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the author of this {@link Comment} instance.
   * @return Returns the {@link User} who created this {@link Comment} instance.
   */
  public User getAuthor() {
    return author;
  }

  /**
   * Gets the timestamp indicating when this {@link Comment} instance was first created.
   * @return A timestamp indicating when this {@link Comment} instance was created.
   */
  public HashMap<String, Object> getCreated() {
    if (created != null) {
      return created;
    }
    // Otherwise make a new object set to FieldValue.serverTimestamp()
    HashMap<String, Object> dateCreatedObj = new HashMap<String, Object>();
    dateCreatedObj.put("created", FieldValue.serverTimestamp());
    return dateCreatedObj;
  }

  /**
   * Gets this {@link Comment} instance's timestamp represented as a <code>long</code> value.
   * @return Returns the timestamp indicating when this {@link Comment} was created.
   */
  @Exclude
  public long getCreatedLong() {
    return (long)created.get("created");
  }

  /**
   * The content of this {@link Comment} instance text.
   * @return The text of this {@link Comment} instance.
   */
  public String getMessage() {
    return message;
  }
}
