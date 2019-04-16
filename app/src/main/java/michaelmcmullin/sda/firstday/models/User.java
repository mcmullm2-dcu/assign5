package michaelmcmullin.sda.firstday.models;

import android.net.Uri;

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
   * The email address of the {@link User} stored in this instance.
   */
  private String email;

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
   * @param email The email address for this {@link User} instance.
   * @param photo The <code>Uri</code> of the profile picture for this {@link User} instance.
   */
  public User(String name, String email, Uri photo) {
    this.name = name;
    this.email = email;
    this.photo = photo;
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
   * Gets the email address of this {@link User} instance.
   * @return Returns the {@link User} instance's email address.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Gets the profile picture <code>Uri</code> of this {@link User} instance.
   * @return Returns the {@link User} instance's photo <code>Uri</code>.
   */
  public Uri getPhoto() {
    return photo;
  }

}
