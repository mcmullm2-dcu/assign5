package michaelmcmullin.sda.firstday.interfaces;

import android.app.Activity;
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
   * Logs a user out of the app, redirecting them back to {@link LoginActivity}.
   * @param current The current activity this method is being called from.
   */
  void logOut(final Activity current);
}
