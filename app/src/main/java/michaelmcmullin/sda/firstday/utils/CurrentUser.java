package michaelmcmullin.sda.firstday.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import michaelmcmullin.sda.firstday.LoginActivity;
import michaelmcmullin.sda.firstday.MainActivity;
import michaelmcmullin.sda.firstday.interfaces.User;

/**
 * An implementation of the {@link User} interface to centralise some user operations.
 */
public class CurrentUser implements User {

  @Override
  /**
   * Get the unique ID that identifies the currently logged in user.
   * @return Returns a unique user ID if there is a user logged in. Otherwise, it returns
   * <code>null</code>.
   */
  public String getUserId() {
    // TODO: Implement this method.
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
}
