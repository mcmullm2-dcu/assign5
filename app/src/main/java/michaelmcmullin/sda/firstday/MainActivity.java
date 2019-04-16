package michaelmcmullin.sda.firstday;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import de.hdodenhof.circleimageview.CircleImageView;
import michaelmcmullin.sda.firstday.interfaces.User;
import michaelmcmullin.sda.firstday.utils.CurrentUser;

public class MainActivity extends AppCompatActivity {

  User user = new CurrentUser();

  /**
   * Called when {@link MainActivity} is started, initialising the Activity and inflating the
   * appropriate XML layout.
   *
   * @param savedInstanceState Used if this Activity is re-initialised, where it contains the
   *     most recently available data (or null).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Demo profile picture. If it's null, show default image (or colour in this case).
    CircleImageView profile = findViewById(R.id.profile_image);
    Uri profile_picture = user.getPhoto();
    if (profile_picture != null) {
      Glide.with(this).load(profile_picture).into(profile);
    } else {
      profile.setImageResource(R.drawable.ic_default_profile_picture);
    }

    // Demo user display name
    TextView msg = findViewById(R.id.welcome);
    msg.setText(user.getDisplayName());
  }

  /**
   * Specify the menu to display for this activity.
   * @param menu The options menu to place the menu items into.
   * @return Returns <code>true</code> if the menu is to be displayed.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  /**
   * Responds to a menu item being selected.
   * @param item The menu item selected by the user
   * @return Returns <code>true</code> if the menu item functionality is being handled here rather
   *     than by the normal system menu processing.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Log.i(AppConstants.TAG, "Starting onOptionsItemSelected method");

    switch (item.getItemId()) {
      case R.id.menuitem_log_out:
        user.logOut(this);
        return true;
    }
    return false;
  }
}
