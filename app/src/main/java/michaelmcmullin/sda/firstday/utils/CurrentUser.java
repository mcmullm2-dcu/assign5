package michaelmcmullin.sda.firstday.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import michaelmcmullin.sda.firstday.LoginActivity;
import michaelmcmullin.sda.firstday.MainActivity;
import michaelmcmullin.sda.firstday.interfaces.User;

/**
 * An implementation of the {@link User} interface to centralise some user operations.
 */
public class CurrentUser implements User {
  private FirebaseUser user;

  /**
   * Default constructor that sets the currently logged in user.
   */
  public CurrentUser() {
    reset();
  }

  @Override
  /**
   * Get the unique ID that identifies the currently logged in user.
   * @return Returns a unique user ID if there is a user logged in. Otherwise, it returns
   * <code>null</code>.
   */
  public String getUserId() {
    if (user != null) {
      return user.getUid();
    }
    return null;
  }

  /**
   * Gets the main display name for the currently logged in user.
   *
   * @return Returns a <code>String</code> value of the logged in user's name, or <code>null</code>
   *     if it's not available.
   */
  @Override
  public String getDisplayName() {
    if (user != null) {
      return user.getDisplayName();
    }
    return null;
  }

  /**
   * Gets the email address of the currently logged in user.
   *
   * @return Returns the logged in user's email address, if available, or <code>null</code> if it's
   *     not.
   */
  @Override
  public String getEmail() {
    if (user != null) {
      return user.getEmail();
    }
    return null;
  }

  /**
   * Gets the <code>Uri</code> of the current user's profile photo if available.
   *
   * @return Returns the logged in user's profile photo, if available.
   */
  @Override
  public Uri getPhoto() {
    if (user != null) {
      return user.getPhotoUrl();
    }
    return null;
  }

  /**
   * Logs a user out of the app, redirecting them back to {@link LoginActivity}.
   * @param current The current activity this method is being called from.
   */
  @Override
  public void logOut(final Activity current) {
    AuthUI.getInstance()
        .signOut(current)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          public void onComplete(@NonNull Task<Void> task) {
            Toast.makeText(current, "Logged Out", Toast.LENGTH_SHORT).show();
            // Back to Sign-in screen
            Intent intent = new Intent(current, LoginActivity.class);
            current.startActivity(intent);
            current.finish();
          }
        });
  }

  /**
   * Resets the current user. Useful for race conditions where this class is created before the user
   * login process has completed.
   */
  @Override
  public void reset() {
    user = FirebaseAuth.getInstance().getCurrentUser();
  }
}