package michaelmcmullin.sda.firstday.models;

import android.net.Uri;
import michaelmcmullin.sda.firstday.utils.CurrentUser;

/**
 * Class to store basic information about a {@link User}. This is usually used for information
 * relating to users other than the current user.
 */
public class User {

  /**
   * Unique ID of the {@link User} stored in this instance.
   */
  private String id;

  /**
   * The display name of the {@link User} stored in this instance.
   */
  private String name;

  /**
   * The <code>Uri</code> of this {@link User} instance's profile photo.
   */
  private Uri photo;

  /**
   * Firestore requires a constructor with no arguments.
   */
  public User() {}

  /**
   * Create a new instance of {@link User} supplying their name and email address.
   * @param name The display name for this {@link User} instance.
   * @param photo The <code>Uri</code> of the profile picture for this {@link User} instance.
   */
  public User(String name, Uri photo) {
    this.name = name;
    this.photo = photo;
  }

  /**
   * Create a new instance of {@link User} from the current user.
   * @param current The currently logged in user.
   */
  public User(CurrentUser current) {
    this.name = current.getDisplayName();
    this.photo = current.getPhoto();
  }

  /**
   * Gets the unique {@link User} ID of this instance.
   * @return Returns the unique ID of this {@link User} instance.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the unique {@link User} ID of this instance.
   * @param id The unique {@link User} ID to apply to this instance.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the display name of this {@link User} instance.
   * @return Returns the {@link User} instance's display name.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the profile picture <code>Uri</code> of this {@link User} instance.
   * @return Returns the {@link User} instance's photo <code>Uri</code>.
   */
  public Uri getPhoto() {
    return photo;
  }

}
