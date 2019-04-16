package michaelmcmullin.sda.firstday.interfaces;

import android.app.Activity;
import android.net.Uri;
import michaelmcmullin.sda.firstday.LoginActivity;

/**
 * An <code>interface</code> to abstract certain operations with users so they avoid cluttering
 * Activity/Fragment code, and also enable us to change the underlying system later if required.
 */
public interface User {

  /**
   * Get the unique ID that identifies the currently logged in user.
   * @return Returns a unique user ID if there is a user logged in. Otherwise, it returns
   * <code>null</code>.
   */
  String getUserId();

  /**
   * Gets the main display name for the currently logged in user.
   * @return Returns a <code>String</code> value of the logged in user's name, or <code>null</code>
   *     if it's not available.
   */
  String getDisplayName();

  /**
   * Gets the email address of the currently logged in user.
   * @return Returns the logged in user's email address, if available, or <code>null</code> if it's
   *     not.
   */
  String getEmail();

  /**
   * Gets the <code>Uri</code> of the current user's profile photo if available.
   * @return Returns the logged in user's profile photo, if available.
   */
  Uri getPhoto();

  /**
   * Logs a user out of the app, redirecting them back to {@link LoginActivity}.
   * @param current The current activity this method is being called from.
   */
  void logOut(final Activity current);

  /**
   * Resets the current user. Useful for race conditions where this class is created before the
   * user login process has completed.
   */
  void reset();
}
